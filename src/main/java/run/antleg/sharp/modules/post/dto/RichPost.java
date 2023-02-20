package run.antleg.sharp.modules.post.dto;

import lombok.Data;
import run.antleg.sharp.modules.post.model.Post;
import run.antleg.sharp.modules.post.model.PostContent;
import run.antleg.sharp.modules.post.model.PostId;
import run.antleg.sharp.modules.user.model.UserSummary;

import java.time.LocalDateTime;

@Data
public class RichPost {

    private PostId id;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private UserSummary author; // TODO: 这里用用户全部信息不大合适

    private PostContent content;

    public RichPost(Post post, UserSummary author) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.createTime = post.getCreateTime();
        this.updateTime = post.getUpdateTime();
        this.content = post.getContent();
        this.author = author;
    }
}
