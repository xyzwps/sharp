package run.antleg.sharp.modules.tag.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findTop10ByNameLikeAndDeleted(String pattern, boolean deleted);
    Optional<Tag> findByName(String name);
}
