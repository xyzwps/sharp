package run.antleg.sharp.modules.tag.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedRepository extends CrudRepository<Tagged, UniverseTaggedId> {
}
