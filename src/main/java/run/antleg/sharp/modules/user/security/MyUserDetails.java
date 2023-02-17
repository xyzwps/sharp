package run.antleg.sharp.modules.user.security;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import run.antleg.sharp.config.hibernate.UserIdUserType;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity(name = "MyUserDetails")
@Data
@Table(name = "user_details")
public class MyUserDetails implements UserDetails {

    @Id
    @Type(UserIdUserType.class)
    private UserId userId;

    private String password;

    private LocalDateTime passwordExp;

    private boolean locked;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Roles.ROLE_USER));
    }


    @Override
    public String getUsername() {
        if (user == null) throw new IllegalStateException("No corresponding user");
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Optional.ofNullable(passwordExp)
                .map(LocalDateTime.now()::isBefore)
                .orElse(true);
    }

    @Override
    public boolean isEnabled() {
        return !locked;
    }
}
