import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    public static void encode() {
        final LinkedList<Character> list = getList();
        while (!BinaryStdIn.isEmpty()) {
            final char c = BinaryStdIn.readChar();
            final int i = list.indexOf(c);
            list.remove(i);
            list.addFirst(c);
            BinaryStdOut.write(i, 8);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        final LinkedList<Character> list = getList();
        while (!BinaryStdIn.isEmpty()) {
            final int i = BinaryStdIn.readInt(8);
            BinaryStdOut.write(list.get(i));
            list.addFirst(list.remove(i));
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> getList() {
        final LinkedList<Character> list = new LinkedList<>();
        for (char c = 0; c < 256; c++) list.add(c);
        return list;
    }

    public static void main(final String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
