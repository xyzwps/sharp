package run.antleg.sharp.modules.tag.dto;

import lombok.Data;
import run.antleg.sharp.modules.tag.model.Tag;

@Data
public class SimpleTag {
    private String name;
    private Long id;

    public static SimpleTag from(Tag tag) {
        var it = new SimpleTag();
        it.name = tag.getName();
        it.id = tag.getId();
        return it;
    }
}
