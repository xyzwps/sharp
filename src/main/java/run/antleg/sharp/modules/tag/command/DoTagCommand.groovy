package run.antleg.sharp.modules.tag.command

class DoTagCommand {
    Set<Long> added
    Set<Long> removed

    boolean shouldDoNothing() {
        return !shouldAdd() && !shouldRemove()
    }

    boolean shouldAdd() {
        return added != null && !added.isEmpty()
    }

    boolean shouldRemove() {
        return removed != null && !removed.isEmpty()
    }
}
