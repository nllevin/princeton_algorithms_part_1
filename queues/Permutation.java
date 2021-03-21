/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
  public static void main(String[] args) {
    double k = Double.parseDouble(args[0]);
    double n = 0;
    RandomizedQueue<String> rq = new RandomizedQueue<String>();

    while (!StdIn.isEmpty()) {
      String str = StdIn.readString();
      n++;

      if (n <= k) {
        rq.enqueue(str);
      } else if (k > 0) {
        double inclusionProbability = k / n;
        double randomValue = StdRandom.uniform();
        if (randomValue < inclusionProbability) {
          rq.dequeue();
          rq.enqueue(str);
        }
      }
    }

    while (!rq.isEmpty()) {
      StdOut.println(rq.dequeue());
    }
  }
}
