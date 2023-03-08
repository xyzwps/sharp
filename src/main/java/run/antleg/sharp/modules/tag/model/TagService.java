package run.antleg.sharp.modules.tag.model;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    public List<Tag> search(String q) {
        if (q.isEmpty()) return List.of();
        return tagRepository.findTop10ByNameLikeAndDeleted("%" + q + "%", false);
    }

    public List<Tag> find(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return List.of();
        return CollectionUtils.mutList(tagRepository.findAllById(tagIds)).stream()
                .filter(it -> !it.isDeleted()).toList();
    }

    public Optional<Tagged> findTagged(UniverseTaggedId id) {
        return taggedRepository.findById(id);
    }

    public void save(Tagged tagged) {
        this.taggedRepository.saveAndFlush(tagged);
    }

    public Tag createTag(String name) {
        var tag = tagRepository.findByName(name).orElseGet(() -> tagRepository.save(Tag.builder()
                .name(name)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .deleted(false)
                .build()));

        if (tag.isDeleted()) {
            throw new AppException(Errors.TAG_UNSUPPORTED);
        }
        return tag;
    }

    public Set<Long> appendTags(UniverseTaggedId id, Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return Set.of();

        return taggedRepository.findById(id)
                .map((tagged) -> {
                    tagged.getTagIds().addAll(tagIds);
                    taggedRepository.save(tagged);
                    return tagged.getTagIds();
                })
                .orElseGet(() -> {
                    var tagged = Tagged.builder()
                            .tagIds(tagIds).id(id.getId()).type(id.getType())
                            .build();
                    em.persist(tagged); // TODO: 改
                    return tagIds;
                });
    }

    public Set<Long> getTags(UniverseTaggedId id) {
        return taggedRepository.findById(id).map(Tagged::getTagIds).orElse(Set.of());
    }


    private final TagRepository tagRepository;
    private final TaggedRepository taggedRepository;
    private final EntityManager em;

    public TagService(TagRepository tagRepository,
                      TaggedRepository taggedRepository,
                      EntityManager em) {
        this.tagRepository = Objects.requireNonNull(tagRepository);
        this.taggedRepository = Objects.requireNonNull(taggedRepository);
        this.em = Objects.requireNonNull(em);
    }
}
