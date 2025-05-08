package dk.mdp.puzzle15.exception;

import dk.mdp.puzzle15.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(final BindException exception, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(final AuthenticationException exception, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException exception, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(final SecurityException exception, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException ex, final HttpServletRequest request) {
        final HttpStatus status = resolveResponseStatus(ex);
        return buildErrorResponse(status, ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<Object> buildErrorResponse(final HttpStatus status, final String message, final String path) {
        final ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        log.error(errorResponse.toString());
        return ResponseEntity.status(status).body(errorResponse);
    }

    private HttpStatus resolveResponseStatus(final Exception exception) {
        final ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        return (annotation != null) ? annotation.code() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
