package dk.mdp.puzzle15.util;

import dk.mdp.puzzle15.exception.InvalidMoveException;
import dk.mdp.puzzle15.model.Board;
import dk.mdp.puzzle15.model.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardUtilsTest {

    private static List<Arguments> provideValidMoves() {
        return List.of(
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 0, 10, 11}, {12, 13, 14, 15}}, 2, 1, Direction.UP, 1, 1),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 0, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 1, 1, Direction.DOWN, 2, 1),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 6, 0, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 1, 2, Direction.LEFT, 1, 1),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 0, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 1, 1, Direction.RIGHT, 1, 2)
        );
    }

    private static List<Arguments> provideInvalidMoves() {
        return List.of(
                Arguments.of(new int[][]{{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 0, 0, Direction.UP),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 0, 15}}, 3, 2, Direction.DOWN),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {0, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 1, 0, Direction.LEFT),
                Arguments.of(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 0}, {8, 9, 10, 11}, {12, 13, 14, 15}}, 1, 3, Direction.RIGHT)
        );
    }

    @Test
    void testGenerateSolvableBoardReturnsValidBoard() {
        final Board board = BoardUtils.generateSolvableBoard();

        assertNotNull(board);
        assertEquals(4, board.tiles().length);
        assertEquals(4, board.tiles()[0].length);

        final boolean[] found = new boolean[16];
        for (final int[] row : board.tiles()) {
            for (final int val : row) {
                assertTrue(val >= 0 && val <= 15, "Tile value out of range: " + val);
                found[val] = true;
            }
        }
        for (int i = 0; i < 16; i++) {
            assertTrue(found[i], "Missing tile: " + i);
        }
    }

    @ParameterizedTest
    @MethodSource("provideValidMoves")
    void testApplyMoveSwapsCorrectly(final int[][] tiles, final int emptyRow, final int emptyCol, final Direction direction, final int expectedRow, final int expectedCol) {
        final Board board = new Board(tiles, emptyRow, emptyCol);
        final Board moved = BoardUtils.applyMove(board, direction);

        assertEquals(expectedRow, moved.emptyRow());
        assertEquals(expectedCol, moved.emptyCol());

        assertEquals(0, moved.tiles()[expectedRow][expectedCol]);
        assertEquals(tiles[expectedRow][expectedCol], moved.tiles()[emptyRow][emptyCol]);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidMoves")
    void testApplyMoveThrowsForInvalidMoves(final int[][] tiles, final int emptyRow, final int emptyCol, final Direction direction) {
        final Board board = new Board(tiles, emptyRow, emptyCol);
        assertThrows(InvalidMoveException.class, () -> BoardUtils.applyMove(board, direction));
    }

    @Test
    void testIsSolvedReturnsTrueForSolvedBoard() {
        final int[][] solved = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
        assertTrue(BoardUtils.isSolved(solved));
    }

    @Test
    void testIsSolvedReturnsFalseForUnsolvedBoard() {
        final int[][] unsolved = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 0}, {13, 14, 15, 12}};
        assertFalse(BoardUtils.isSolved(unsolved));
    }
}
