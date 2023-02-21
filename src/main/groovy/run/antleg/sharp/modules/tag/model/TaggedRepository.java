package run.antleg.sharp.modules.tag.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedRepository extends JpaRepository<Tagged, UniverseTaggedId> {
}
