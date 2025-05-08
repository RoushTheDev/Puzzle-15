package dk.mdp.puzzle15.model;

import dk.mdp.puzzle15.util.BoardUtils;

public record Board(int[][] tiles, int emptyRow, int emptyCol) {

    public static final int SIZE = 4;

    public boolean isSolved() {
        return BoardUtils.isSolved(tiles);
    }

    public int get(final int row, final int col) {
        return tiles[row][col];
    }

    public Board copy() {
        final int[][] newTiles = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(tiles[i], 0, newTiles[i], 0, SIZE);
        }
        return new Board(newTiles, emptyRow, emptyCol);
    }
}
