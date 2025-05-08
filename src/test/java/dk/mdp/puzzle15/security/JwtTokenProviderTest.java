package dk.mdp.puzzle15.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private User user;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        user = new User("testuser", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_TEST")));
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        final String token = jwtTokenProvider.generateToken(user);

        assertNotNull(token);
        final String username = jwtTokenProvider.getUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateTokenReturnsTrueForMatchingUser() {
        final String token = jwtTokenProvider.generateToken(user);

        final boolean isValid = jwtTokenProvider.validateToken(token, user);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenReturnsFalseForWrongUser() {
        final String token = jwtTokenProvider.generateToken(user);
        final User otherUser = new User("notme", "password", Collections.emptyList());

        final boolean isValid = jwtTokenProvider.validateToken(token, otherUser);
        assertFalse(isValid);
    }
}
