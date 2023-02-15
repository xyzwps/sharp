package run.antleg.sharp.modules.anthology.model;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AnthologyService {
    private final AnthologyRepository anthologyRepository;

    public AnthologyService(AnthologyRepository anthologyRepository) {
        this.anthologyRepository = Objects.requireNonNull(anthologyRepository);
    }

    public Anthology save(Anthology anthology) {
        return anthologyRepository.save(anthology);
    }
}
