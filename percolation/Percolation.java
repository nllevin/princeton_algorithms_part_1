/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private static final byte OPEN = 0b100;
  private static final byte TOP_CONNECTED = 0b010;
  private static final byte BOTTOM_CONNECTED = 0b001;
  private static final byte PERCOLATES_STATE = 0b111;

  private final int sideLength;
  private final WeightedQuickUnionUF sitesUF;
  private byte[] sitesState;
  private int numOpenSites = 0;
  private boolean percolates = false;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) throw new IllegalArgumentException();

    int numSites = n * n;

    sideLength = n;
    sitesUF = new WeightedQuickUnionUF(numSites);
    sitesState = new byte[numSites];
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (isOpen(row, col)) return;

    numOpenSites++;
    setInitialOpenState(row, col);
    connectWithOpenNeighbors(row, col);
  }

  private void setInitialOpenState(int row, int col) {
    int siteIndex = rowAndColToIndex(row, col);
    byte initialState = initialOpenState(row);
    sitesState[siteIndex] = initialState;
  }

  private byte initialOpenState(int row) {
    if (row == 1 && row == sideLength) {
      percolates = true;
      return OPEN | TOP_CONNECTED | BOTTOM_CONNECTED;
    }
    else if (row == 1) {
      return OPEN | TOP_CONNECTED;
    }
    else if (row == sideLength) {
      return OPEN | BOTTOM_CONNECTED;
    }

    return OPEN;
  }

  private void connectWithOpenNeighbors(int row, int col) {
    int siteIndex = rowAndColToIndex(row, col);
    int[][] neighbors = possibleNeighbors(row, col);

    for (int[] neighbor : neighbors) {
      int neighborRow = neighbor[0];
      int neighborCol = neighbor[1];
      if (outOfBounds(neighborRow, neighborCol)) continue;
      if (!isOpen(neighborRow, neighborCol)) continue;

      int neighborIndex = rowAndColToIndex(neighborRow, neighborCol);
      connectWithNeighbor(siteIndex, neighborIndex);
    }
  }

  private void connectWithNeighbor(int siteIndex, int neighborIndex) {
    int siteRootIndex = sitesUF.find(siteIndex);
    byte siteRootState = sitesState[siteRootIndex];

    int neighborRootIndex = sitesUF.find(neighborIndex);
    byte neighborRootState = sitesState[neighborRootIndex];

    sitesUF.union(siteRootIndex, neighborRootIndex);

    byte updatedState = (byte) (siteRootState | neighborRootState);
    int newRootIndex = sitesUF.find(siteRootIndex);
    sitesState[newRootIndex] = updatedState;
    if (updatedState == PERCOLATES_STATE) {
      percolates = true;
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    int siteIndex = rowAndColToIndex(row, col);
    return (sitesState[siteIndex] & OPEN) != 0;
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    int siteIndex = rowAndColToIndex(row, col);
    int siteRootIndex = sitesUF.find(siteIndex);
    byte siteRootState = sitesState[siteRootIndex];
    return (siteRootState & TOP_CONNECTED) != 0;
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return numOpenSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return percolates;
  }

  private int rowAndColToIndex(int row, int col) {
    if (outOfBounds(row, col)) {
      throw new IllegalArgumentException();
    }

    int rowIndex = row - 1;
    int colIndex = col - 1;
    return rowIndex * sideLength + colIndex;
  }

  private int[][] possibleNeighbors(int row, int col) {
    return new int[][] {
            new int[] { row - 1, col },
            new int[] { row, col - 1 },
            new int[] { row + 1, col },
            new int[] { row, col + 1 },
            };
  }

  private boolean outOfBounds(int row, int col) {
    return row < 1 || col < 1 || row > sideLength || col > sideLength;
  }
}
