package run.antleg.sharp.modules.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.post.command.CreatePostCommand;
import run.antleg.sharp.modules.post.command.UpdatePostCommand;
import run.antleg.sharp.modules.post.dto.OnlyPostId;
import run.antleg.sharp.modules.post.dto.RichPost;
import run.antleg.sharp.modules.post.dto.RichPostSummary;
import run.antleg.sharp.modules.post.model.PostId;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.util.List;
import java.util.Objects;

@Tag(name = "文章")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @GetMapping
    public List<RichPostSummary> get(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return handlers.getSummaries(myUserDetails);
    }

    @GetMapping("/{id}")
    public RichPost getById(@AuthenticationPrincipal MyUserDetails myUserDetails,
                            @PathVariable("id") PostId postId) {
        return handlers.getById(myUserDetails, postId);
    }

    @PostMapping
    public OnlyPostId create(@AuthenticationPrincipal MyUserDetails myUserDetails,
                             @RequestBody @Valid CreatePostCommand cmd) {
        return new OnlyPostId(handlers.create(myUserDetails, cmd));
    }

    @PatchMapping("/{id}")
    public OnlyPostId patch(@AuthenticationPrincipal MyUserDetails myUserDetails,
                      @PathVariable("id") PostId postId,
                      @RequestBody @Valid UpdatePostCommand cmd) {
        return new OnlyPostId(handlers.update(myUserDetails, postId, cmd));
    }

    private final PostHandlers handlers;

    public PostController(PostHandlers handlers) {
        this.handlers = Objects.requireNonNull(handlers);
    }
}
