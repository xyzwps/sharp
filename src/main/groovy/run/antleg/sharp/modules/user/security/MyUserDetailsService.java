package run.antleg.sharp.modules.user.security;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserDetailsRepository userDetailsRepository;

    private final EntityManager em;

    public MyUserDetailsService(MyUserDetailsRepository userDetailsRepository,EntityManager em) {
        this.userDetailsRepository = userDetailsRepository;
        this.em = em;
    }

    // TODO: cache
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " is not found."));
    }

    // TODO: cache
    public Optional<MyUserDetails> loadUserByUserId(UserId userId) throws UsernameNotFoundException {
        return userDetailsRepository.findById(userId);
    }

    public MyUserDetails save(MyUserDetails userDetails) {
        em.persist(userDetails); // TODO: 改
        return userDetails;
    }
}
