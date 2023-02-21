package run.antleg.sharp.modules.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.tag.command.CreateTagCommand;
import run.antleg.sharp.modules.tag.command.DoTagCommand;
import run.antleg.sharp.modules.tag.dto.SimpleTag;
import run.antleg.sharp.modules.tag.model.TaggedType;
import run.antleg.sharp.modules.tag.model.UniverseTaggedId;

import java.util.List;
import java.util.Objects;

@Tag(name = "标签")
@RestController
@RequestMapping("/api/tags")
public class TagController {


    @GetMapping("/search")
    public List<SimpleTag> search(@RequestParam("q") String q) {
        return handlers.search(q).stream().map(SimpleTag::from).toList();
    }

    @PostMapping
    public SimpleTag create(@RequestBody @Valid CreateTagCommand cmd) {
        return SimpleTag.from(handlers.create(cmd));
    }

    @Operation(summary = "打/去标签", responses = @ApiResponse(description = "被打上的标签"))
    @PatchMapping("/{type}/{id}")
    public List<SimpleTag> doTag(@PathVariable("type") String type,
                                 @PathVariable("id") String id,
                                 @RequestBody @Valid DoTagCommand cmd) {
        if (cmd.shouldDoNothing()) return List.of();

        var taggedType = TaggedType.from(type, () -> new AppException(Errors.TAGGED_TYPE_UNSUPPORTED));
        var utid = UniverseTaggedId.builder().type(taggedType).id(id).build();
        return handlers.doTag(utid, cmd).stream().map(SimpleTag::from).toList();
    }

    @Operation(summary = "查询标签")
    @GetMapping("/{type}/{id}")
    public List<SimpleTag> getTagged(@PathVariable("type") String type,
                                     @PathVariable("id") String id) {
        var taggedType = TaggedType.from(type, () -> new AppException(Errors.TAGGED_TYPE_UNSUPPORTED));
        var utid = UniverseTaggedId.builder().type(taggedType).id(id).build();
        return handlers.getTagged(utid).stream().map(SimpleTag::from).toList();
    }


    private final TagHandlers handlers;

    public TagController(TagHandlers handlers) {
        this.handlers = Objects.requireNonNull(handlers);
    }
}
