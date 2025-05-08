package dk.mdp.puzzle15.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String method = request.getMethod();
        final String uri = request.getRequestURI();
        final String query = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        final String fullPath = uri + query;

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String user = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";

        log.info("➡️  [{}] {} called {}{}", user, method, uri, query);

        filterChain.doFilter(request, response);

        final int status = response.getStatus();
        log.info("⬅️  [{}] {} {} -> HTTP {}", user, method, fullPath, status);
    }
}
