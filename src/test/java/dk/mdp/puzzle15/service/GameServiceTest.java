package dk.mdp.puzzle15.service;

import dk.mdp.puzzle15.dto.GameResponse;
import dk.mdp.puzzle15.exception.GameNotFoundException;
import dk.mdp.puzzle15.model.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceTest {

    private final String player = "player1";
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void testStartNewGameReturnsNewSession() {
        final GameResponse response = gameService.startNewGame(player);

        assertNotNull(response);
        assertNotNull(response.gameId());
        assertEquals(4, response.board().length);
        assertFalse(response.solved());
    }

    @Test
    void testGetGameReturnsGameResponse() {
        final GameResponse created = gameService.startNewGame(player);
        final GameResponse fetched = gameService.getGame(created.gameId(), player);

        assertEquals(created.gameId(), fetched.gameId());
        assertNotNull(fetched.board());
    }

    @Test
    void testMakeMoveReturnsUpdatedBoard() {
        final GameResponse created = gameService.startNewGame(player);
        final UUID gameId = created.gameId();

        final GameResponse moved = gameService.makeMove(gameId, player, Direction.RIGHT);

        assertNotNull(moved);
        assertEquals(gameId, moved.gameId());
        assertNotEquals(created.board(), moved.board());
    }

    @Test
    void testTerminateGameRemovesSession() {
        final GameResponse response = gameService.startNewGame(player);
        final UUID gameId = response.gameId();

        gameService.terminateGame(gameId, "admin");

        assertThrows(GameNotFoundException.class, () -> gameService.getGame(gameId, player));
    }

    @Test
    void testGetGameThrowsIfUserIsNotOwner() {
        final GameResponse response = gameService.startNewGame("playerX");
        final UUID gameId = response.gameId();

        final SecurityException exception = assertThrows(SecurityException.class, () -> gameService.getGame(gameId, "playerY"));

        assertTrue(exception.getMessage().contains("not authorized"));
    }

    @Test
    void testGetGameThrowsIfGameNotFound() {
        final UUID unknownId = UUID.randomUUID();

        assertThrows(GameNotFoundException.class, () -> gameService.getGame(unknownId, player));
    }
}
