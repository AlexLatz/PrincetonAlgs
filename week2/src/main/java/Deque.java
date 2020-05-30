import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    private class DequeIterator implements Iterator<Item> {
        private Node current;
        public DequeIterator(final Node first) {
            this.current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            final Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Node first;
    private Node last;
    private int count;

    public Deque() {
        first = null;
        last = first;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return count;
    }

    public void addFirst(final Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        final Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (size() == 0) last = first;
        if (oldfirst != null) oldfirst.prev = first;
        count++;
    }

    public void addLast(final Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        final Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (size() == 0)
            first = last;
        if (oldlast != null)
            oldlast.next = last;
        count++;
    }

    public Item removeFirst() {
        if (size() == 0)
            throw new NoSuchElementException();
        final Node oldfirst = first;
        first = oldfirst.next;
        if (first != null && last != null) first.prev = null;
        if (size() == 1) last = null;
        count--;
        return oldfirst.item;
    }

    public Item removeLast() {
        if (size() == 0)
            throw new NoSuchElementException();
        final Node oldlast = last;
        last = oldlast.prev;
        if (first != null && last != null) last.next = null;
        if (size() == 1) first = null;
        count--;
        return oldlast.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    public static void main(final String[] args) {
        final Deque<Integer> d = new Deque<>();
        StdOut.println(d.isEmpty());
        d.addLast(1);
        d.addFirst(0);
        StdOut.println(d.size());
        for (final int n : d)
            StdOut.println(n);
        StdOut.println(d.removeLast()); 
        StdOut.println(d.removeFirst());
    }
}