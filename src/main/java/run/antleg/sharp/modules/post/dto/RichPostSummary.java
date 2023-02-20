package run.antleg.sharp.modules.post.dto;

import lombok.Data;
import run.antleg.sharp.modules.post.model.PostId;
import run.antleg.sharp.modules.post.model.PostSummary;
import run.antleg.sharp.modules.user.model.User;

import java.time.LocalDateTime;

@Data
public class RichPostSummary {

    private PostId id;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private User author; // TODO: 这里用用户全部信息不大合适

    public RichPostSummary(PostSummary summary, User author) {
        this.id = summary.getId();
        this.title = summary.getTitle();
        this.createTime = summary.getCreateTime();
        this.updateTime = summary.getUpdateTime();
        this.author = author;
    }
}
