import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(final String[] args) {
        final RandomizedQueue<String> r = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) r.enqueue(StdIn.readString());
        for (int i = 0; i < Integer.parseInt(args[0]); i++) StdOut.println(r.dequeue());
    }    
}