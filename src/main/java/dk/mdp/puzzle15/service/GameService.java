package dk.mdp.puzzle15.service;

import dk.mdp.puzzle15.dto.GameResponse;
import dk.mdp.puzzle15.exception.GameNotFoundException;
import dk.mdp.puzzle15.model.Board;
import dk.mdp.puzzle15.model.Direction;
import dk.mdp.puzzle15.model.GameSession;
import dk.mdp.puzzle15.util.BoardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GameService {

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    public GameResponse startNewGame(final String username) {
        final UUID gameId = UUID.randomUUID();
        final Board board = BoardUtils.generateSolvableBoard();
        final GameSession session = new GameSession(gameId, username, board, Instant.now());
        sessions.put(gameId, session);
        log.info("Created new game ({}) session for user {}", gameId, username);
        return toResponse(session);
    }

    public GameResponse getGame(final UUID gameId, final String username) {
        final GameSession session = getValidatedSession(gameId, username);
        return toResponse(session);
    }

    public GameResponse makeMove(final UUID gameId, final String username, final Direction direction) {
        final GameSession session = getValidatedSession(gameId, username);
        final Board moved = BoardUtils.applyMove(session.board(), direction);

        final GameSession updatedSession = new GameSession(session.gameId(), session.username(), moved, session.createdAt());

        sessions.put(gameId, updatedSession);
        return toResponse(updatedSession);
    }

    public void terminateGame(final UUID gameId, final String adminUsername) {
        log.info("Game ({}) session that terminated by {}", gameId, adminUsername);
        sessions.remove(gameId);
    }

    private GameSession getValidatedSession(final UUID gameId, final String username) {
        final GameSession session = sessions.get(gameId);
        if (session == null) {
            throw new GameNotFoundException(gameId);
        }
        if (!session.username().equals(username)) {
            throw new SecurityException("Player not authorized for this game");
        }
        return session;
    }

    private GameResponse toResponse(final GameSession session) {
        return new GameResponse(
                session.gameId(),
                session.board().tiles(),
                session.board().isSolved()
        );
    }
}
