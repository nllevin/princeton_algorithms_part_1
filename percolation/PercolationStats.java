/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_CONST = 1.96;
  private final double[] results;
  private int trialsCompleted = 0;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }

    results = new double[trials];
    for (int i = 0; i < trials; i++) {
      Percolation perc = new Percolation(n);

      while (!perc.percolates()) {
        int row = 1 + StdRandom.uniform(n);
        int col = 1 + StdRandom.uniform(n);
        perc.open(row, col);
      }

      int numOpenSites = perc.numberOfOpenSites();
      double result = (double) numOpenSites / (n * n);
      results[i] = result;
      trialsCompleted = trialsCompleted + 1;
    }
  }

  // test client
  public static void main(String[] args) {
    // Stopwatch timer = new Stopwatch();
    int gridSize = Integer.parseInt(args[0]);
    int numTrials = Integer.parseInt(args[1]);
    PercolationStats p = new PercolationStats(gridSize, numTrials);
    StdOut.printf("mean                    = %f\n", p.mean());
    StdOut.printf("stddev                  = %f\n", p.stddev());
    StdOut.printf(
            "95%% confidence interval = [%f, %f]\n",
            p.confidenceLo(),
            p.confidenceHi()
    );
    // StdOut.printf("time elapsed            = %fs\n", timer.elapsedTime());
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(results, 0, trialsCompleted);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(results, 0, trialsCompleted);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - CONFIDENCE_CONST * stddev() / Math.sqrt(trialsCompleted);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + CONFIDENCE_CONST * stddev() / Math.sqrt(trialsCompleted);
  }
}
