/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] items;
  private int size;

  public RandomizedQueue() {
    items = (Item[]) new Object[1];
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void enqueue(Item item) {
    if (item == null) throw new IllegalArgumentException();

    if (size == items.length) resize(2 * size);
    items[size++] = item;
  }

  public Item dequeue() {
    if (isEmpty()) throw new java.util.NoSuchElementException();

    int randomIndex = StdRandom.uniform(size);
    Item dequeued = items[randomIndex];
    items[randomIndex] = items[size - 1];
    items[size - 1] = null;
    size--;
    if (size > 0 && size == items.length / 4) resize(items.length / 2);
    return dequeued;
  }

  public Item sample() {
    if (isEmpty()) throw new java.util.NoSuchElementException();

    int randomIndex = StdRandom.uniform(size);
    return items[randomIndex];
  }

  private void resize(int capacity) {
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < size; i++) {
      copy[i] = items[i];
    }
    items = copy;
  }

  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    int[] randomIndices = StdRandom.permutation(size);
    int current = 0;

    public boolean hasNext() {
      return current < randomIndices.length;
    }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();

      int randomIndex = randomIndices[current++];
      return items[randomIndex];
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
    StdOut.println(rq.isEmpty() == true);
    StdOut.println(rq.size() == 0);
    rq.enqueue(0);
    StdOut.println(rq.isEmpty() == false);
    StdOut.println(rq.size() == 1);
    StdOut.println(rq.sample() == 0);
    StdOut.println(rq.size() == 1);
    StdOut.println(rq.dequeue() == 0);
    StdOut.println(rq.isEmpty() == true);

    for (int i = 0; i < 10; i++) {
      rq.enqueue(i);
    }

    Iterator<Integer> rqIterator = rq.iterator();
    try {
      rqIterator.remove();
    } catch (UnsupportedOperationException e) {
      StdOut.println("Caught iterator removal");
    }
    while (rqIterator.hasNext()) {
      StdOut.printf("Iterated: %d\n", rqIterator.next());
    }
    try {
      rqIterator.next();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught empty iterator next()");
    }

    for (int i = 0; i < 10; i++) {
      StdOut.printf("Sample: %d\n", rq.sample());
    }
    while (!rq.isEmpty()) {
      StdOut.printf("Dequeue: %d\n", rq.dequeue());
    }
    try {
      rq.enqueue(null);
    } catch (IllegalArgumentException e) {
      StdOut.println("Caught null enque");
    }
    try {
      rq.dequeue();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught empty dequeue");
    }
    try {
      rq.sample();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught empty sample");
    }
  }
}
