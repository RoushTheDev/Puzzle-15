package dk.mdp.puzzle15.dto;

import dk.mdp.puzzle15.model.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Move request containing the direction to move the empty field.")
public record MoveRequest(
        @Schema(description = "Direction to move the empty field", example = "UP")
        @NotNull
        Direction direction
) {
}
