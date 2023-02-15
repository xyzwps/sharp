package run.antleg.sharp.modules.anthology.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.AnthologyIdUserType;
import run.antleg.sharp.config.hibernate.AuthorIdUserType;

import java.time.LocalDateTime;

@Entity
@Data
public class Anthology {

    @Id
    @Type(AnthologyIdUserType.class)
    @GeneratedValue(generator = "anthology_id_generator")
    @GenericGenerator(name = "anthology_id_generator", strategy = "run.antleg.sharp.config.hibernate.AnthologyIdGenerator")
    private AnthologyId anthologyId;

    @Type(AuthorIdUserType.class)
    private AuthorId authorId;

    private String title;

    private LocalDateTime createTime;
}
