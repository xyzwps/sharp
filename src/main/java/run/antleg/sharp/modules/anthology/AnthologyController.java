package run.antleg.sharp.modules.anthology;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand;
import run.antleg.sharp.modules.anthology.model.Anthology;
import run.antleg.sharp.modules.anthology.model.AnthologyId;
import run.antleg.sharp.modules.anthology.model.AnthologyService;
import run.antleg.sharp.modules.anthology.model.AuthorId;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

import static run.antleg.sharp.modules.errors.Errors.ANTHOLOGY_NOT_FOUND;

@Tag(name = "文集")
@RestController
@RequestMapping("/api/anthologies")
public class AnthologyController {

    @Operation(summary = "创建文集")
    @PostMapping
    @Secured(Roles.ROLE_USER)
    public Anthology create(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestBody @Valid UpsertAnthologyCommand cmd
    ) {
        // TODO: 防重复
        var anthology = Anthology.builder()
                .authorId(AuthorId.from(userDetails.getUserId()))
                .title(cmd.getTitle())
                .createTime(LocalDateTime.now())
                .build();
        return anthologyService.save(anthology);
    }

    @Operation(summary = "更新文集")
    @PatchMapping("/{anthologyId}")
    @Secured(Roles.ROLE_USER)
    @Transactional
    public Anthology patch(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestBody @Valid UpsertAnthologyCommand cmd,
            @PathVariable("anthologyId") AnthologyId anthologyId
    ) {
        var anthology = findMyAnthology(userDetails, anthologyId)
                .orElseThrow(() -> new AppException(ANTHOLOGY_NOT_FOUND));
        anthology.setTitle(cmd.getTitle());
        return anthologyService.save(anthology);
    }

    @Operation(summary = "删除文集")
    @DeleteMapping("/{anthologyId}")
    @Secured(Roles.ROLE_USER)
    public OK delete(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @PathVariable("anthologyId") AnthologyId anthologyId
    ) {
        findMyAnthology(userDetails, anthologyId)
                .ifPresent(anthologyService::delete);
        return OK.INSTANCE; // TODO: 规范 http status 使用
    }

    @Operation(summary = "查询一个文集")
    @GetMapping("/{anthologyId}")
    @Secured(Roles.ROLE_USER)
    public Anthology getById(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @PathVariable("anthologyId") AnthologyId anthologyId
    ) {
        return findMyAnthology(userDetails, anthologyId)
                .orElseThrow(() -> new AppException(ANTHOLOGY_NOT_FOUND));
    }

    private Optional<Anthology> findMyAnthology(MyUserDetails userDetails, AnthologyId anthologyId) {
        return anthologyService.findById(anthologyId)
                .filter(it -> it.getAuthorId().equals(AuthorId.from(userDetails.getUserId())));
    }

    // TODO: 分页查询/查询全部

    private final AnthologyService anthologyService;

    public AnthologyController(AnthologyService anthologyService) {
        this.anthologyService = anthologyService;
    }


}
