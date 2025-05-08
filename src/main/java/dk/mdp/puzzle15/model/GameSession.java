package dk.mdp.puzzle15.model;

import java.time.Instant;
import java.util.UUID;

public record GameSession(UUID gameId, String username, Board board, Instant createdAt) {
}
