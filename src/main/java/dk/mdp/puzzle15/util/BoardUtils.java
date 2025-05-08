package dk.mdp.puzzle15.util;

import dk.mdp.puzzle15.exception.InvalidMoveException;
import dk.mdp.puzzle15.model.Board;
import dk.mdp.puzzle15.model.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class BoardUtils {

    private static final Random RANDOM = new Random();

    private BoardUtils() {}

    public static Board generateSolvableBoard() {
        List<Integer> numbers;
        do {
            numbers = shuffledNumbers();
        } while (!isSolvable(numbers));

        final int[][] tiles = new int[Board.SIZE][Board.SIZE];
        int emptyRow = 0;
        int emptyCol = 0;
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            int row = i / Board.SIZE;
            int col = i % Board.SIZE;
            tiles[row][col] = numbers.get(i);
            if (tiles[row][col] == 0) {
                emptyRow = row;
                emptyCol = col;
            }
        }

        return new Board(tiles, emptyRow, emptyCol);
    }

    public static Board applyMove(final Board board, final Direction direction) {
        final int row = board.emptyRow();
        final int col = board.emptyCol();
        int newRow = row;
        int newCol = col;

        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        if (!isInBounds(newRow, newCol)) {
            throw new InvalidMoveException();
        }

        final Board copy = board.copy();
        final int[][] tiles = copy.tiles();
        tiles[row][col] = tiles[newRow][newCol];
        tiles[newRow][newCol] = 0;

        return new Board(tiles, newRow, newCol);
    }

    public static boolean isSolved(final int[][] tiles) {
        int expected = 1;
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (row == Board.SIZE - 1 && col == Board.SIZE - 1) {
                    return tiles[row][col] == 0;
                }
                if (tiles[row][col] != expected++) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> shuffledNumbers() {
        final List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, RANDOM);
        return numbers;
    }

    private static boolean isSolvable(final List<Integer> numbers) {
        int inversions = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                int a = numbers.get(i);
                int b = numbers.get(j);
                if (a != 0 && b != 0 && a > b) {
                    inversions++;
                }
            }
        }

        int blankIndex = numbers.indexOf(0);
        int blankRowFromBottom = Board.SIZE - (blankIndex / Board.SIZE);

        // Puzzle is solvable if:
        // 0 is on even row from bottom and inversions is odd
        // or
        // 0 is on odd row from bottom and inversions is even
        return (blankRowFromBottom % 2 == 0) == (inversions % 2 == 1);
    }

    private static boolean isInBounds(final int row, final int col) {
        return row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE;
    }
}
