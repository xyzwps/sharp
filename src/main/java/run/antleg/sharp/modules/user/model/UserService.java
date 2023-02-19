package run.antleg.sharp.modules.user.model;

import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.command.UpsertUserCommand;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    public User createUser(User user) {
        assert user.getId() == null;
        return this.userRepository.save(user);
    }

    public User updateUser(UserId userId, UpsertUserCommand cmd) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        cmd.updateUser(user);
        return userRepository.save(user);
    }


    public Optional<User> findUserById(UserId id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }
}
