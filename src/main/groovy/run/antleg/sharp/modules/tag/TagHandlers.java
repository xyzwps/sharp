package run.antleg.sharp.modules.tag;

import io.vavr.collection.Set;
import jakarta.transaction.Transactional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.tag.command.CreateTagCommand;
import run.antleg.sharp.modules.tag.command.DoTagCommand;
import run.antleg.sharp.modules.tag.model.Tag;
import run.antleg.sharp.modules.tag.model.TagService;
import run.antleg.sharp.modules.tag.model.Tagged;
import run.antleg.sharp.modules.tag.model.UniverseTaggedId;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static run.antleg.sharp.util.When.*;
import static run.antleg.sharp.util.CollectionUtils.*;

@Service
public class TagHandlers {

    @Secured(Roles.ROLE_USER)
    public List<Tag> search(String q) {
        if (q == null || q.isBlank()) return List.of();
        return tagService.search(q.trim());
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    public Tag create(CreateTagCommand cmd) {
        var name = cmd.getName().trim();
        return tagService.createTag(name);
    }

    /**
     * @return 被打上的（added）标签的列表
     */
    @Secured(Roles.ROLE_USER)
    @Transactional
    public List<Tag> doTag(UniverseTaggedId id, DoTagCommand cmd) {

        var tagged = tagService.findTagged(id)
                .orElse(Tagged.builder().id(id.getId()).type(id.getType()).tagIds(new HashSet<>()).build());

        if (cmd.shouldRemove()) {
            tagged.getTagIds().removeAll(cmd.getRemoved());
        }

        var addedTags = when(cmd.shouldAdd())
                .then(() -> {
                    var addableTags = tagService.find(cmd.getAdded());
                    var newTagIds = mutSet(addableTags.stream().map(Tag::getId).toList(), tagged.getTagIds());
                    tagged.setTagIds(newTagIds);
                    return addableTags;
                })
                .orElse(List.of());

        tagService.save(tagged);
        return addedTags;
    }

    public List<Tag> getTagged(UniverseTaggedId id) {
        return tagService.findTagged(id)
                .map(Tagged::getTagIds)
                .filter(it -> !it.isEmpty())
                .map(tagService::find)
                .orElse(List.of());
    }


    private final TagService tagService;

    public TagHandlers(TagService tagService) {
        this.tagService = Objects.requireNonNull(tagService);
    }
}
