package dk.mdp.puzzle15.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response format")
public record ErrorResponse(
        @Schema(description = "Timestamp of the error", example = "2025-05-08T12:00:00Z")
        String timestamp,
        @Schema(description = "HTTP status code", example = "500")
        int status,
        @Schema(description = "HTTP status description", example = "Internal Server Error")
        String error,
        @Schema(description = "Error message detail", example = "An internal process stopped unexpected")
        String message,
        @Schema(description = "Path of the failed request", example = "/api/test")
        String path
) {
}
