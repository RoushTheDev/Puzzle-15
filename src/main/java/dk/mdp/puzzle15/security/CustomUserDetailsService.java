package dk.mdp.puzzle15.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public final class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, CustomUserDetails> users = Map.of(
            "player1", new CustomUserDetails("player1", "{noop}pass1", UserRole.REGULAR),
            "player2", new CustomUserDetails("player2", "{noop}pass2", UserRole.REGULAR),
            "admin", new CustomUserDetails("admin", "{noop}adminpass", UserRole.ADMIN)
    );

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final CustomUserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}
