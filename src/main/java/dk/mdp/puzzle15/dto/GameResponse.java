package dk.mdp.puzzle15.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "JWT response containing the game with its state.")
public record GameResponse(
        @Schema(description = "ID of the game", example = "14851263-cde4-4a7e-a2a3-5385674893a1")
        UUID gameId,
        @Schema(description = "Representation of the board")
        int[][] board,
        @Schema(description = "Flag for whether the board is solved", example = "false")
        boolean solved
) {
}
