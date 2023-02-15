package run.antleg.sharp.modules.anthology

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import run.antleg.sharp.modules.anthology.command.CreateAnthologyCommand
import run.antleg.sharp.modules.anthology.model.Anthology
import run.antleg.sharp.modules.anthology.model.AnthologyService

import java.time.LocalDateTime

@Service
class AnthologyHandlers {

    @Autowired
    AnthologyService anthologyService

    Anthology create(CreateAnthologyCommand cmd) {
        var anthology = new Anthology(
                authorId: cmd.authorId,
                title: cmd.title,
                createTime: LocalDateTime.now())
        return anthologyService.save(anthology)
    }
}
