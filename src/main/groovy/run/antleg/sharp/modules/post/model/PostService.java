package run.antleg.sharp.modules.post.model;

import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    public List<PostSummary> getPostSummariesByUserId(UserId userId) {
        return postRepository.getByUserId(userId);
    }

    public Optional<Post> findById(PostId postId) {
        return postRepository.findById(postId); // TODO: onetoone 好像没生效
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = Objects.requireNonNull(postRepository);
    }
}
