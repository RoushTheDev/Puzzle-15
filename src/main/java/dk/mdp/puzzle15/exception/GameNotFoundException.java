package dk.mdp.puzzle15.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(final UUID gameId) {
        super("Game (%s) not found.".formatted(gameId));
    }
}
