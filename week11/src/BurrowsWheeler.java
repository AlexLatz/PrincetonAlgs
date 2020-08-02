import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    public static void transform() {
        final String in = BinaryStdIn.readString();
        final CircularSuffixArray csa = new CircularSuffixArray(in);
        int first = -1;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            if (csa.index(i) == 0) first = i;
            int c = csa.index(i) - 1;
            if (c < 0) c += in.length();
            BinaryStdOut.write(in.charAt(c));
        }
        BinaryStdOut.write(first);
        BinaryStdOut.write(s.toString());
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        final String in = BinaryStdIn.readString();
        final int[] count = new int[R + 1];
        for (int i = 0; i < in.length(); i++) {
            final char c = in.charAt(i);
            count[c + 1]++;
        }
        for (int r = 1; r < R; r++) count[r] += count[r - 1];
        final int[] next = new int[in.length()];
        final char[] aux = new char[in.length()];
        for (int i = 0; i < in.length(); i++) {
            aux[count[in.charAt(i)]] = in.charAt(i);
            next[count[in.charAt(i)]] = i;
            count[in.charAt(i)]++;
        }
        for (int i = 0; i < in.length(); i++) {
            BinaryStdOut.write(aux[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(final String[] args) {
        if (args[0].equals("+")) inverseTransform();
        else if (args[0].equals("-")) transform();
    }
}
