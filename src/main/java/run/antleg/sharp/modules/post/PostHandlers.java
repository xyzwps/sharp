package run.antleg.sharp.modules.post;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.post.command.CreatePostCommand;
import run.antleg.sharp.modules.post.command.UpdatePostCommand;
import run.antleg.sharp.modules.post.dto.RichPost;
import run.antleg.sharp.modules.post.dto.RichPostSummary;
import run.antleg.sharp.modules.post.model.*;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static run.antleg.sharp.util.CollectionUtils.*;

@Service
public class PostHandlers {

    @Secured(Roles.ROLE_USER)
    public List<RichPostSummary> getSummaries(MyUserDetails userDetails) {
        var summaries = postService.getPostSummariesByUserId(userDetails.getUserId());
        var userIds = summaries.stream().map(PostSummary::getUserId).collect(Collectors.toSet());
        var users = userService.findUserByIds(userIds);
        var userIdToUser = keyBy(users, User::getId);
        return summaries.stream().map(it -> new RichPostSummary(it, userIdToUser.get(it.getUserId()))).toList();
    }

    @Secured(Roles.ROLE_USER)
    public RichPost getById(MyUserDetails ignored, PostId postId) {
        var post = postService.findById(postId)
                .orElseThrow(() -> new AppException(Errors.POST_NOT_FOUND));
        var author = userService.findUserById(post.getUserId()).orElse(null);
        return new RichPost(post, author);
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    public PostId create(MyUserDetails userDetails, CreatePostCommand cmd) {
        var post = Post.builder()
                .userId(userDetails.getUserId())
                .title(cmd.getTitle())
                .idem(cmd.getIdem())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        var savedPost = postService.save(post);
        var content = PostContent.builder()
                .id(savedPost.getId())
                .content(cmd.getContent())
                .type(cmd.getType())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        em.persist(content);
        return postService.findById(savedPost.getId())
                .orElseThrow(() -> new AppException(Errors.IMPOSSIBLE))
                .getId();
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    public PostId update(MyUserDetails userDetails, PostId postId, UpdatePostCommand cmd) {
        var post = postService.findById(postId)
                .filter(it -> Objects.equals(it.getUserId(), userDetails.getUserId()))
                .orElseThrow(() -> new AppException(Errors.POST_NOT_FOUND));

        post.setTitle(cmd.getTitle());
        post.setUpdateTime(LocalDateTime.now());

        post.getContent().setContent(cmd.getContent());
        post.getContent().setUpdateTime(LocalDateTime.now());

        return postService.save(post).getId();
    }


    private final PostService postService;
    private final UserService userService;
    private final EntityManager em;

    public PostHandlers(PostService postService, UserService userService, EntityManager em) {
        this.postService = Objects.requireNonNull(postService);
        this.userService = Objects.requireNonNull(userService);
        this.em = Objects.requireNonNull(em);
    }

}
