package run.antleg.sharp.modules.tag.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UniverseTaggedId.class)
public class Tagged {
    @Id
    @Enumerated(EnumType.STRING)
    private TaggedType type;
    @Id
    private String id;
    private Set<Long> tagIds;

    public UniverseTaggedId universeTaggedId() {
        return UniverseTaggedId.builder().type(type).id(id).build();
    }
}