import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private class CircularSuffix implements Comparable<CircularSuffix> {
        final int index;
        final String s;

        public CircularSuffix(final int index, final String s) {
            this.index = index;
            this.s = s;
        }

        @Override
        public int compareTo(final CircularSuffix that) {
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt((this.index + i) % s.length());
                final char c1 = s.charAt((that.index + i) % s.length());
                if (c < c1) return -1;
                else if (c > c1) return 1;
            }
            return Integer.compare(this.index, that.index);
        }
    }

    private final int length;
    private final int[] index;

    public CircularSuffixArray(final String s) {
        if (s == null) throw new IllegalArgumentException();
        length = s.length();
        index = new int[length];
        final CircularSuffix[] suffixes = new CircularSuffix[length];
        for (int i = 0; i < length; i++) suffixes[i] = new CircularSuffix(i, s);
        Arrays.sort(suffixes);
        for (int i = 0; i < length; i++) index[i] = suffixes[i].index;
    }

    public int length() {
        return length;
    }

    public int index(final int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException();
        return index[i];
    }

    public static void main(final String[] args) {
        final CircularSuffixArray a = new CircularSuffixArray("ABRACADABRA!");
        StdOut.println(a.length());
        StdOut.println(a.index(0));
    }
}
