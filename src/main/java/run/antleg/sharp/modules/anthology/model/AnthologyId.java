package run.antleg.sharp.modules.anthology.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(type = "string")
public record AnthologyId(String value) implements Serializable {
}
