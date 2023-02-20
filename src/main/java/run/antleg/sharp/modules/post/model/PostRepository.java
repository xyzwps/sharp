package run.antleg.sharp.modules.post.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, PostId> {

    List<PostSummary> getByUserId(UserId userId); // TODO: 这个是不是真的有用嘞
}
