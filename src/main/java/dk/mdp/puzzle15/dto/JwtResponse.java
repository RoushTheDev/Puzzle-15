package dk.mdp.puzzle15.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT response containing the token.")
public record JwtResponse(
        @Schema(description = "JWT token to be used in Authorization header", example = "eyJhbGciOiJIzUI1NiJ9.eyJzdWIiOiJwbGF5ZXIxIiwicm9sZSI6IlJPTEVfUhTHVUxBUiIsImlhdCI6MTc0NjY5NzA1NCwiZXhwIjoxNzQ2NzAwNjU0fQ.78DCql9Otj21MHO_AtGa0cApNG0TndjRUrsMangWqYg")
        String token
) {
}
