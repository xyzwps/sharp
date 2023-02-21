package run.antleg.sharp.modules.tag.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniverseTaggedId implements Serializable {
    @Enumerated(EnumType.STRING)
    private TaggedType type;
    private String id;
}
