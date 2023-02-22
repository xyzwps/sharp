package run.antleg.sharp.modules.tag.command;

import lombok.Data;

import java.util.Set;

@Data
public class DoTagCommand {
    private Set<Long> added;
    private Set<Long> removed;

    public boolean shouldDoNothing() {
        return !shouldAdd() && !shouldRemove();
    }

    public boolean shouldAdd() {
        return added != null && !added.isEmpty();
    }

    public boolean shouldRemove() {
        return removed != null && !removed.isEmpty();
    }
}
