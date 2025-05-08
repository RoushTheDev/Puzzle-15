package dk.mdp.puzzle15.exception;

import dk.mdp.puzzle15.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void testHandleBindExceptionReturns400() {
        final BindException exception = new BindException(new Object(), "target");
        final ResponseEntity<Object> response = exceptionHandler.handleBindException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body.message());
    }

    @Test
    void testHandleAuthenticationExceptionReturns401() {
        final AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Authentication failed");

        final ResponseEntity<Object> response = exceptionHandler.handleAuthenticationException(exception, mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Authentication failed", body.message());
    }

    @Test
    void testHandleAccessDeniedExceptionReturns403() {
        final AccessDeniedException exception = new AccessDeniedException("Forbidden");
        final ResponseEntity<Object> response = exceptionHandler.handleAccessDeniedException(exception, mockRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Forbidden", body.message());
    }
    @Test
    void testHandleSecurityExceptionReturns403() {
        final SecurityException exception = new SecurityException("Access denied by security check");
        final ResponseEntity<Object> response = exceptionHandler.handleSecurityException(exception, mockRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Access denied by security check", body.message());
    }

    @Test
    void testHandleRuntimeExceptionWithResponseStatus() {
        final GameNotFoundException exception = new GameNotFoundException(UUID.randomUUID());
        final ResponseEntity<Object> response = exceptionHandler.handleRuntimeException(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertTrue(body.message().contains("not found"));
    }

    @Test
    void testHandleRuntimeExceptionWithoutResponseStatus() {
        final RuntimeException exception = new RuntimeException("Unexpected failure");
        final ResponseEntity<Object> response = exceptionHandler.handleRuntimeException(exception, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        final ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals("Unexpected failure", body.message());
    }
}
