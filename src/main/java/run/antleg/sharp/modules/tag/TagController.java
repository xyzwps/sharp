package run.antleg.sharp.modules.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.tag.command.CreateTagCommand;
import run.antleg.sharp.modules.tag.command.DoTagCommand;
import run.antleg.sharp.modules.tag.dto.SimpleTag;
import run.antleg.sharp.modules.tag.model.TagService;
import run.antleg.sharp.modules.tag.model.Tagged;
import run.antleg.sharp.modules.tag.model.TaggedType;
import run.antleg.sharp.modules.tag.model.UniverseTaggedId;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static run.antleg.sharp.util.CollectionUtils.mutSet;
import static run.antleg.sharp.util.When.when;

@Tag(name = "标签")
@RestController
@RequestMapping("/api/tags")
public class TagController {


    @GetMapping("/search")
    @Secured(Roles.ROLE_USER)
    public List<SimpleTag> search(@RequestParam("q") String q) {
        if (q == null || q.isBlank()) return List.of();
        return tagService.search(q.trim()).stream().map(SimpleTag::from).toList();
    }

    @PostMapping
    @Secured(Roles.ROLE_USER)
    @Transactional
    public SimpleTag create(@RequestBody @Valid CreateTagCommand cmd) {
        var name = cmd.getName().trim();
        return SimpleTag.from(tagService.createTag(name));
    }

    @Operation(summary = "打/去标签", responses = @ApiResponse(description = "被打上的标签"))
    @PatchMapping("/{type}/{id}")
    @Secured(Roles.ROLE_USER)
    @Transactional
    public List<SimpleTag> doTag(@PathVariable("type") String type,
                                 @PathVariable("id") String id,
                                 @RequestBody @Valid DoTagCommand cmd) {
        if (cmd.shouldDoNothing()) return List.of();

        var taggedType = TaggedType.from(type, () -> new AppException(Errors.TAGGED_TYPE_UNSUPPORTED));
        var utid = UniverseTaggedId.builder().type(taggedType).id(id).build();

        var tagged = tagService.findTagged(utid)
                .orElse(Tagged.builder().id(utid.getId()).type(utid.getType()).tagIds(new HashSet<>()).build());

        if (cmd.shouldRemove()) {
            tagged.getTagIds().removeAll(cmd.getRemoved());
        }

        var addedTags = when(cmd.shouldAdd())
                .then(() -> {
                    var addableTags = tagService.find(cmd.getAdded());
                    var newTagIds = mutSet(addableTags.stream().map(run.antleg.sharp.modules.tag.model.Tag::getId).toList(), tagged.getTagIds());
                    tagged.setTagIds(newTagIds);
                    return addableTags;
                })
                .orElse(List.of());

        tagService.save(tagged);
        return addedTags.stream().map(SimpleTag::from).toList();
    }

    @Operation(summary = "查询标签")
    @GetMapping("/{type}/{id}")
    public List<SimpleTag> getTagged(@PathVariable("type") String type,
                                     @PathVariable("id") String id) {
        var taggedType = TaggedType.from(type, () -> new AppException(Errors.TAGGED_TYPE_UNSUPPORTED));
        var utid = UniverseTaggedId.builder().type(taggedType).id(id).build();
        return tagService.findTagged(utid)
                .map(Tagged::getTagIds)
                .filter(it -> !it.isEmpty())
                .map(tagService::find)
                .orElse(List.of())
                .stream()
                .map(SimpleTag::from)
                .toList();
    }

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = Objects.requireNonNull(tagService);
    }
}
