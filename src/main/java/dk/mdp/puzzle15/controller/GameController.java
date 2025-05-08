package dk.mdp.puzzle15.controller;

import dk.mdp.puzzle15.dto.ErrorResponse;
import dk.mdp.puzzle15.dto.GameResponse;
import dk.mdp.puzzle15.dto.MoveRequest;
import dk.mdp.puzzle15.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/game")
@Tag(name = "Game", description = "Endpoints related to the board game.")
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    @Operation(summary = "Start a new 15 Puzzle game")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Game created", content = @Content(schema = @Schema(implementation = GameResponse.class)))
    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<GameResponse> startGame(@AuthenticationPrincipal final UserDetails user) {
        final GameResponse response = gameService.startNewGame(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gameId}")
    @Operation(summary = "Get the current state of a game")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Game retrieved", content = @Content(schema = @Schema(implementation = GameResponse.class)))
    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<GameResponse> getGame(@PathVariable final UUID gameId, @AuthenticationPrincipal final UserDetails user) {
        final GameResponse response = gameService.getGame(gameId, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{gameId}/move")
    @Operation(summary = "Make a move in the puzzle")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Move succeeded", content = @Content(schema = @Schema(implementation = GameResponse.class)))
    @ApiResponse(responseCode = "400", description = "Malformed request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Game not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<GameResponse> move(@PathVariable final UUID gameId, @Valid @RequestBody final MoveRequest moveRequest, @AuthenticationPrincipal final UserDetails user) {
        final GameResponse response = gameService.makeMove(gameId, user.getUsername(), moveRequest.direction());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{gameId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Terminate a game (ADMIN only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Game deleted", content = @Content(schema = @Schema(implementation = GameResponse.class)))
    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> terminateGame(@PathVariable final UUID gameId, @AuthenticationPrincipal final UserDetails user) {
        gameService.terminateGame(gameId, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
