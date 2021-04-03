/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
  private SearchNode finalNode;

  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<SearchNode> solverPQ = new MinPQ<SearchNode>();
    solverPQ.insert(new SearchNode(initial, null, 0));

    MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
    twinPQ.insert(new SearchNode(initial.twin(), null, 0));

    while (true) {
      SearchNode solverNode = step(solverPQ);
      if (solverNode != null) {
        finalNode = solverNode;
        break;
      }

      SearchNode twinNode = step(twinPQ);
      if (twinNode != null) break;
    }
  }

  private SearchNode step(MinPQ<SearchNode> searchPQ) {
    SearchNode currentNode = searchPQ.delMin();
    Board currentBoard = currentNode.board;
    SearchNode prevNode = currentNode.prevNode;
    if (currentBoard.isGoal()) {
      return currentNode;
    }

    Iterable<Board> neighbors = currentBoard.neighbors();
    for (Board neighbor : neighbors) {
      if (prevNode != null && neighbor.equals(prevNode.board)) {
        continue;
      }

      SearchNode newNode = new SearchNode(neighbor, currentNode, currentNode.moves + 1);
      searchPQ.insert(newNode);
    }
    return null;
  }

  public boolean isSolvable() {
    return finalNode != null;
  }

  public int moves() {
    return isSolvable() ? finalNode.moves : -1;
  }

  public Iterable<Board> solution() {
    if (!isSolvable()) return null;

    Stack<Board> solutionSteps = new Stack<Board>();
    SearchNode currentNode = finalNode;
    while (currentNode != null) {
      solutionSteps.push(currentNode.board);
      currentNode = currentNode.prevNode;
    }

    return solutionSteps;
  }

  private class SearchNode implements Comparable<SearchNode> {
    private final Board board;
    private final SearchNode prevNode;
    private final int moves;
    private final int distance;

    public SearchNode(Board current, SearchNode previous, int numMoves) {
      board = current;
      prevNode = previous;
      moves = numMoves;
      distance = current.manhattan();
    }

    public int compareTo(SearchNode that) {
      return (moves + distance) - (that.moves + that.distance);
    }
  }

  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
