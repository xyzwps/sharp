package run.antleg.sharp.modules.anthology.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnthologyRepository extends CrudRepository<Anthology, AnthologyId> {
}
