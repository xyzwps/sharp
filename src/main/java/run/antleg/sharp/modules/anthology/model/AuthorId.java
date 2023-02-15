package run.antleg.sharp.modules.anthology.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(type = "integer", format = "int64")
public record AuthorId(Long value) implements Serializable {
}
