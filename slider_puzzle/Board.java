/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
  private final int[][] tiles;
  private final int hammingDistance;
  private final int manhattanDistance;

  public Board(int[][] inputTiles) {
    int sideLength = inputTiles.length;
    tiles = new int[sideLength][sideLength];
    for (int row = 0; row < sideLength; row++) {
      for (int col = 0; col < sideLength; col++) {
        tiles[row][col] = inputTiles[row][col];
      }
    }
    hammingDistance = calculateHammingDistance();
    manhattanDistance = calculateManhattanDistance();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(dimension());
    for (int row = 0; row < dimension(); row++) {
      sb.append("\n");
      for (int col = 0; col < dimension(); col++) {
        if (col != 0) sb.append(" ");
        sb.append(tiles[row][col]);
      }
    }
    return sb.toString();
  }

  public int dimension() {
    return tiles.length;
  }

  public int hamming() {
    return hammingDistance;
  }

  private int calculateHammingDistance() {
    int sideLength = dimension();
    int numTilesInWrongPosition = 0;
    for (int row = 0; row < sideLength; row++) {
      for (int col = 0; col < sideLength; col++) {
        if (blankTile(row, col)) continue;

        if (tileInWrongPosition(row, col)) numTilesInWrongPosition++;
      }
    }
    return numTilesInWrongPosition;
  }

  private boolean blankTile(int row, int col) {
    return tiles[row][col] == 0;
  }

  private boolean tileInWrongPosition(int row, int col) {
    return tileManhattanDistance(row, col) != 0;
  }

  public int manhattan() {
    return manhattanDistance;
  }

  private int calculateManhattanDistance() {
    int sideLength = dimension();
    int totalDistance = 0;
    for (int row = 0; row < sideLength; row++) {
      for (int col = 0; col < sideLength; col++) {
        if (blankTile(row, col)) continue;

        totalDistance += tileManhattanDistance(row, col);
      }
    }
    return totalDistance;
  }

  private int tileManhattanDistance(int row, int col) {
    int tile = tiles[row][col];
    int correctRow = (tile - 1) / dimension();
    int correctCol = (tile - 1) % dimension();
    return Math.abs(row - correctRow) + Math.abs(col - correctCol);
  }

  public boolean isGoal() {
    return manhattan() == 0;
  }

  public boolean equals(Object y) {
    if (this == y) return true;
    if (y == null) return false;
    if (this.getClass() != y.getClass()) return false;

    Board that = (Board) y;
    if (dimension() != that.dimension()) return false;

    for (int row = 0; row < dimension(); row++) {
      for (int col = 0; col < dimension(); col++) {
        if (this.tiles[row][col] != that.tiles[row][col]) {
          return false;
        }
      }
    }

    return true;
  }

  public Iterable<Board> neighbors() {
    return new NeighborIterable(tiles);
  }

  private class NeighborIterable implements Iterable<Board> {
    private final int[][] tiles;

    public NeighborIterable(int[][] inputTiles) {
      tiles = inputTiles;
    }

    public Iterator<Board> iterator() {
      return new NeighborIterator(tiles);
    }
  }

  private class NeighborIterator implements Iterator<Board> {
    private final int[][] tiles;
    private int emptyRow = -1;
    private int emptyCol = -1;
    private int numNeighbors;
    private int currentNeighbor;
    private final int[] neighborEmptyRows;
    private final int[] neighborEmptyCols;

    public NeighborIterator(int[][] inputTiles) {
      int sideLength = inputTiles.length;
      tiles = new int[sideLength][sideLength];

      for (int row = 0; row < sideLength; row++) {
        for (int col = 0; col < sideLength; col++) {
          tiles[row][col] = inputTiles[row][col];
          if (tiles[row][col] == 0) {
            emptyRow = row;
            emptyCol = col;
          }
        }
      }

      numNeighbors = 4;
      if (emptyRow == 0 || emptyRow == sideLength - 1) numNeighbors--;
      if (emptyCol == 0 || emptyCol == sideLength - 1) numNeighbors--;

      neighborEmptyRows = new int[numNeighbors];
      neighborEmptyCols = new int[numNeighbors];
      int numNeighborsFound = 0;
      if (inBounds(emptyRow - 1)) {
        neighborEmptyRows[numNeighborsFound] = emptyRow - 1;
        neighborEmptyCols[numNeighborsFound] = emptyCol;
        numNeighborsFound++;
      }
      if (inBounds(emptyRow + 1)) {
        neighborEmptyRows[numNeighborsFound] = emptyRow + 1;
        neighborEmptyCols[numNeighborsFound] = emptyCol;
        numNeighborsFound++;
      }
      if (inBounds(emptyCol - 1)) {
        neighborEmptyRows[numNeighborsFound] = emptyRow;
        neighborEmptyCols[numNeighborsFound] = emptyCol - 1;
        numNeighborsFound++;
      }
      if (inBounds(emptyCol + 1)) {
        neighborEmptyRows[numNeighborsFound] = emptyRow;
        neighborEmptyCols[numNeighborsFound] = emptyCol + 1;
      }
    }

    private boolean inBounds(int i) {
      return i >= 0 && i < tiles.length;
    }

    public boolean hasNext() {
      return currentNeighbor != numNeighbors;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Board next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      int sideLength = tiles.length;
      int[][] newTiles = new int[sideLength][sideLength];
      int newEmptyRow = neighborEmptyRows[currentNeighbor];
      int newEmptyCol = neighborEmptyCols[currentNeighbor];
      for (int row = 0; row < sideLength; row++) {
        for (int col = 0; col < sideLength; col++) {
          if (row == emptyRow && col == emptyCol) {
            newTiles[row][col] = tiles[newEmptyRow][newEmptyCol];
          } else if (row == newEmptyRow && col == newEmptyCol) {
            newTiles[row][col] = 0;
          } else {
            newTiles[row][col] = tiles[row][col];
          }
        }
      }
      currentNeighbor++;
      return new Board(newTiles);
    }
  }

  public Board twin() {
    int sideLength = tiles.length;
    int[][] newTiles = new int[sideLength][sideLength];
    for (int row = 0; row < sideLength; row++) {
      for (int col = 0; col < sideLength; col++) {
        newTiles[row][col] = tiles[row][col];
      }
    }

    if (tiles[0][0] == 0) {
      newTiles[0][1] = tiles[1][0];
      newTiles[1][0] = tiles[0][1];
    } else if (tiles[0][1] == 0) {
      newTiles[0][0] = tiles[1][0];
      newTiles[1][0] = tiles[0][0];
    } else {
      newTiles[0][0] = tiles[0][1];
      newTiles[0][1] = tiles[0][0];
    }

    return new Board(newTiles);
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] inputTiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        inputTiles[i][j] = in.readInt();

    Board b = new Board(inputTiles);
    StdOut.println(b);
    StdOut.println(b.hamming());
    StdOut.println(b.manhattan());
    StdOut.println(b.isGoal());
    StdOut.println(b.equals(b));
    StdOut.println(b.equals(null));

    Iterable<Board> neighbors = b.neighbors();
    for (Board neighbor : neighbors) {
      StdOut.println(neighbor);
    }

    Board twin = b.twin();
    StdOut.println(twin);
    StdOut.println(b.equals(twin));
    StdOut.println(b.equals(twin.twin()));
  }
}
