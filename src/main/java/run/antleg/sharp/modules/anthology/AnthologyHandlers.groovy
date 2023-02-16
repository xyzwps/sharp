package run.antleg.sharp.modules.anthology

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand
import run.antleg.sharp.modules.anthology.model.Anthology
import run.antleg.sharp.modules.anthology.model.AnthologyId
import run.antleg.sharp.modules.anthology.model.AnthologyService
import run.antleg.sharp.modules.errors.AppException

import java.time.LocalDateTime

import static run.antleg.sharp.modules.errors.Errors.*

@Service
class AnthologyHandlers {

    @Autowired
    AnthologyService anthologyService

    Anthology create(UpsertAnthologyCommand cmd) {
        var anthology = new Anthology(
                authorId: cmd.authorId,
                title: cmd.title,
                createTime: LocalDateTime.now())
        return anthologyService.save(anthology)
    }

    @Transactional
    Anthology update(AnthologyId anthologyId, UpsertAnthologyCommand cmd) {
        var anthology = anthologyService.findById(anthologyId)
                .filter { it.authorId == cmd.authorId }
                .orElseThrow { new AppException(ANTHOLOGY_NOT_FOUND) }
        anthology.title = cmd.title
        return anthologyService.save(anthology)
    }

    Anthology get(AnthologyId anthologyId) {
        return anthologyService.findById(anthologyId) // TODO: 判断是不是当前用户的
//                .filter { it.authorId == cmd.authorId }
                .orElseThrow { new AppException(ANTHOLOGY_NOT_FOUND) }
    }

    @Transactional
    void delete(AnthologyId anthologyId) {
        anthologyService.findById(anthologyId) // TODO: 判断是不是当前用户的
//                .filter { it.authorId == cmd.authorId }
                .ifPresent { anthologyService.delete(it) }
    }
}
