package run.antleg.sharp.modules.anthology;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand;
import run.antleg.sharp.modules.anthology.model.Anthology;
import run.antleg.sharp.modules.anthology.model.AnthologyId;

@Tag(name = "文集")
@RestController
@RequestMapping("/api/anthologies")
public class AnthologyController {

    @Operation(summary = "创建文集")
    @PostMapping
    public Anthology create(@RequestBody @Valid UpsertAnthologyCommand cmd) {
        return anthologyHandlers.create(cmd);
    }

    @Operation(summary = "更新文集")
    @PatchMapping("/{anthologyId}")
    public Anthology patch(@RequestBody @Valid UpsertAnthologyCommand cmd,
                           @PathVariable("anthologyId") AnthologyId anthologyId) {
        return anthologyHandlers.update(anthologyId, cmd);
    }

    @Operation(summary = "删除文集")
    @DeleteMapping("/{anthologyId}")
    public OK delete(@PathVariable("anthologyId") AnthologyId anthologyId) {
        anthologyHandlers.delete(anthologyId);
        return OK.INSTANCE; // TODO: 规范 http status 使用
    }

    @Operation(summary = "查询一个文集")
    @GetMapping("/{anthologyId}")
    public Anthology getById(@PathVariable("anthologyId") AnthologyId anthologyId) {
        return anthologyHandlers.get(anthologyId);
    }

    // TODO: 分页查询/查询全部

    private final AnthologyHandlers anthologyHandlers;

    public AnthologyController(AnthologyHandlers anthologyHandlers) {
        this.anthologyHandlers = anthologyHandlers;
    }


}
