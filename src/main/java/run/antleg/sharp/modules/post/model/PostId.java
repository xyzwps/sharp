package run.antleg.sharp.modules.post.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(type = "integer", format = "int64")
public record PostId(Long value) implements Serializable {
}
