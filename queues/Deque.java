/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
  private Node first = null;
  private Node last = null;
  private int size = 0;

  private class Node {
    Item item;
    Node next;
    Node prev;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void addFirst(Item item) {
    validateAddition(item);

    Node newFirst = new Node();
    newFirst.item = item;
    newFirst.next = first;
    newFirst.prev = null;

    if (isEmpty()) last = newFirst;
    else first.prev = newFirst;

    first = newFirst;
    size++;
  }

  public void addLast(Item item) {
    validateAddition(item);
    Node newLast = new Node();
    newLast.item = item;
    newLast.next = null;
    newLast.prev = last;

    if (isEmpty()) first = newLast;
    else last.next = newLast;

    last = newLast;
    size++;
  }

  private void validateAddition(Item item) {
    if (item == null) throw new IllegalArgumentException();
  }

  public Item removeFirst() {
    validateRemoval();
    Item removed = first.item;
    first = first.next;
    size--;
    if (isEmpty()) last = null;
    else first.prev = null;
    return removed;
  }

  public Item removeLast() {
    validateRemoval();
    Item removed = last.item;
    last = last.prev;
    size--;
    if (isEmpty()) first = null;
    else last.next = null;
    return removed;
  }

  private void validateRemoval() {
    if (isEmpty()) throw new java.util.NoSuchElementException();
  }

  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();

      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  public static void main(String[] args) {
    Deque<Integer> d = new Deque<Integer>();
    try {
      d.removeFirst();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught it");
    }
    try {
      d.removeLast();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught it");
    }
    try {
      d.addFirst(null);
    } catch (IllegalArgumentException e) {
      StdOut.println("Caught it");
    }
    try {
      d.addLast(null);
    } catch (IllegalArgumentException e) {
      StdOut.println("Caught it");
    }

    StdOut.println(d.isEmpty() == true);
    d.addFirst(1);
    StdOut.println(d.isEmpty() == false);
    StdOut.println(d.removeFirst() == 1);
    StdOut.println(d.isEmpty() == true);
    d.addLast(2);
    StdOut.println(d.removeFirst() == 2);
    d.addLast(3);
    StdOut.println(d.removeLast() == 3);
    d.addFirst(4);
    StdOut.println(d.removeLast() == 4);

    for (int i = 0; i < 5; i++) {
      d.addFirst(i);
    }
    StdOut.println(d.size() == 5);
    for (int i = 5; i < 10; i++) {
      d.addLast(i);
    }
    StdOut.println(d.size() == 10);

    for (Integer i : d) {
      StdOut.println(i);
    }

    Iterator<Integer> i = d.iterator();
    StdOut.println(i.hasNext() == true);
    StdOut.println(i.next() == 4);
    StdOut.println(i.next() == 3);
    try {
      i.remove();
    } catch (UnsupportedOperationException e) {
      StdOut.println("Caught it");
    }
    while (i.hasNext()) i.next();
    try {
      i.next();
    } catch (java.util.NoSuchElementException e) {
      StdOut.println("Caught it");
    }

    d.removeFirst();
    d.removeLast();
    for (Integer j : d) {
      StdOut.println(j);
    }
  }
}
