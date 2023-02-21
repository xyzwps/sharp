package run.antleg.sharp.modules.anthology

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import run.antleg.sharp.config.security.Roles
import run.antleg.sharp.modules.SecurityUtils
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand
import run.antleg.sharp.modules.anthology.model.Anthology
import run.antleg.sharp.modules.anthology.model.AnthologyId
import run.antleg.sharp.modules.anthology.model.AnthologyService
import run.antleg.sharp.modules.anthology.model.AuthorId
import run.antleg.sharp.modules.errors.AppException

import java.time.LocalDateTime

import static run.antleg.sharp.modules.errors.Errors.*

@Service
class AnthologyHandlers {

    @Autowired
    AnthologyService anthologyService

    @Secured(Roles.ROLE_USER)
    Anthology create(UpsertAnthologyCommand cmd) {
        var userDetails = SecurityUtils.getPrincipal()
        var anthology = new Anthology(
                authorId: AuthorId.from(userDetails.getUserId()),
                title: cmd.title,
                createTime: LocalDateTime.now())
        return anthologyService.save(anthology)
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    Anthology update(AnthologyId anthologyId, UpsertAnthologyCommand cmd) {
        var userDetails = SecurityUtils.getPrincipal()
        var anthology = anthologyService.findById(anthologyId)
                .filter { it.authorId == AuthorId.from(userDetails.getUserId()) }
                .orElseThrow { new AppException(ANTHOLOGY_NOT_FOUND) }
        anthology.title = cmd.title
        return anthologyService.save(anthology)
    }

    @Secured(Roles.ROLE_USER)
    Anthology get(AnthologyId anthologyId) {
        var userDetails = SecurityUtils.getPrincipal()
        return anthologyService.findById(anthologyId)
                .filter { it.authorId == AuthorId.from(userDetails.getUserId()) }
                .orElseThrow { new AppException(ANTHOLOGY_NOT_FOUND) }
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    void delete(AnthologyId anthologyId) {
        var userDetails = SecurityUtils.getPrincipal()
        anthologyService.findById(anthologyId)
                .filter { it.authorId == AuthorId.from(userDetails.getUserId()) }
                .ifPresent { anthologyService.delete(it) }
    }
}
