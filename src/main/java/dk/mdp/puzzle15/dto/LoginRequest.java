package dk.mdp.puzzle15.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request containing credentials.")
public record LoginRequest(
        @Schema(description = "Username of the player", example = "player1")
        @NotBlank
        String username,
        @Schema(description = "Password of the player", example = "pass1")
        @NotBlank
        String password
) {
}
