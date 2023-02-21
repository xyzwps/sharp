package run.antleg.sharp.modules.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.PostIdUserType;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostContent {
    @Id
    @Type(PostIdUserType.class)
    @JsonIgnore
    private PostId id;
    @Enumerated(EnumType.STRING)
    private PostType type;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

