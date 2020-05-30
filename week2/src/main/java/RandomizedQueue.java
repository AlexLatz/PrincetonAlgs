import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] order;
        private int pointer;

        public RandomizedQueueIterator() {
            order = StdRandom.permutation(count);
            pointer = 0;
        }

        public boolean hasNext() {
            return pointer < order.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            final Item item = arr[order[pointer]];
            pointer++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Item[] arr;
    private int count;

    public RandomizedQueue() {
        arr = (Item[]) new Object[2];
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    private void expand() {
        final Item[] arrtemp = (Item[]) new Object[arr.length * 2];
        for (int i = 0; i < arr.length; i++)
            arrtemp[i] = arr[i];
        arr = arrtemp;
    }

    private void shrink() {
        final Item[] arrtemp = (Item[]) new Object[arr.length / 2];
        for (int i = 0; i < arrtemp.length; i++)
            arrtemp[i] = arr[i];
        arr = arrtemp;
    }

    private void delete(final int i) {
        arr[i] = arr[count--];
        arr[count] = null;
    }

    public void enqueue(final Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (count >= arr.length / 2)
            expand();
        arr[count] = item;
        count++;
    }

    public Item dequeue() {
        if (count == 0)
            throw new NoSuchElementException();
        if (count <= arr.length / 4)
            shrink();
        final int i = StdRandom.uniform(count);
        final Item item = arr[i];
        delete(i);
        return item;
    }

    public Item sample() {
        if (count == 0)
            throw new NoSuchElementException();
        return arr[StdRandom.uniform(count)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(final String[] args) {
        final RandomizedQueue<Integer> r = new RandomizedQueue<>();
        StdOut.println("empty " + r.isEmpty());
        r.enqueue(0);
        StdOut.println("size " + r.size());
        StdOut.println("sample " + r.sample());
        for (final int i : r)
            StdOut.println(i);
        r.dequeue();

    }
}