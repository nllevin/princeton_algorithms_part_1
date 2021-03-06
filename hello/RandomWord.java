/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int wordIndex = 1;
        String selectedWord = null;

        while (!StdIn.isEmpty()) {
            String currentWord = StdIn.readString();
            double probability = 1.0 / wordIndex;
            if (StdRandom.bernoulli(probability)) {
                selectedWord = currentWord;
            }
            wordIndex++;
        }

        StdOut.println(selectedWord);
    }
}
