package run.antleg.sharp.modules.user.model;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    public User createUser(User user) {
        assert user.getUserId() == null;
        return this.userRepository.save(user);
    }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }
}
