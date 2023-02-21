package run.antleg.sharp.modules.tag.dto

import run.antleg.sharp.modules.tag.model.Tag

class SimpleTag {
    String name
    Long id
    static SimpleTag from(Tag tag) {
        return new SimpleTag(name: tag.name, id: tag.id)
    }
}
