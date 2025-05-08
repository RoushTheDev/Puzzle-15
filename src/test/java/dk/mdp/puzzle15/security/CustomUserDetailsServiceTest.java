package dk.mdp.puzzle15.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsServiceTest {

    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService();
    }

    @Test
    void testLoadUserByUsernameReturnsExpectedUser() {
        final UserDetails user = userDetailsService.loadUserByUsername("player1");

        assertNotNull(user);
        assertEquals("player1", user.getUsername());
        assertTrue(user.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_REGULAR"))
        );
    }

    @Test
    void testLoadUserByUsernameReturnsAdminWithAdminRole() {
        final UserDetails user = userDetailsService.loadUserByUsername("admin");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertTrue(user.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
        );
    }

    @Test
    void testLoadUserByUsernameThrowsForUnknownUser() {
        final UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknownUser"));

        assertEquals("User not found: unknownUser", exception.getMessage());
    }
}
