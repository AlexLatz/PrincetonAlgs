import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BoggleSolver {
    private class BoggleTrie {
        private class Node {
            boolean isString;
            Node[] links = new Node[26];
        }

        private Node root;
        private Node lastPrefix;
        private String lastQuery;

        public BoggleTrie(final String[] dictionary) {
            root = new Node();
            lastPrefix = root;
            lastQuery = "";
            for (final String s : dictionary) add(s);
        }

        public void add(final String s) {
            root = add(root, s, 0, false);
        }

        private Node add(Node n, final String s, final int d, boolean abort) {
            if (n == null) n = new Node();
            if (d == s.length() && !abort) n.isString = true;
            else if (!abort) {
                if (s.charAt(d) == 'Q') if (d + 1 == s.length() || s.charAt(d + 1) != 'U') abort = true;
                n.links[charToRadix(s.charAt(d))] = add(n.links[charToRadix(s.charAt(d))], s, d + 1, abort);
            }
            return n;
        }

        private Node get(final Node n, final String key, final int d) {
            if (n == null) return null;
            if (d == key.length()) return n;
            return get(n.links[charToRadix(key.charAt(d))], key, d + 1);
        }

        public boolean contains(final String word) {
            final Node n;
            if (isPrefix(lastQuery, word)) n = get(lastPrefix, word, lastQuery.length());
            else n = get(root, word, 0);
            if (n == null) return false;
            lastPrefix = n;
            lastQuery = word;
            return n.isString;
        }

        public boolean prefix(final String prefix) {
            final Node n;
            if (isPrefix(lastQuery, prefix)) n = get(lastPrefix, prefix, lastQuery.length());
            else n = get(root, prefix, 0);
            if (n != null) {
                lastQuery = prefix;
                lastPrefix = n;
            }
            return n != null;
        }

        private boolean isPrefix(final String prefix, final String word) {
            if (prefix.length() > word.length()) return false;
            for (int i = 0; i < prefix.length(); i++) if (word.charAt(i) != prefix.charAt(i)) return false;
            return true;
        }

        private int charToRadix(final char c) {
            return Character.getNumericValue(c) - 10;
        }
    }

    private final BoggleTrie dict;

    public BoggleSolver(final String[] dictionary) {
        dict = new BoggleTrie(dictionary);
    }

    public Iterable<String> getAllValidWords(final BoggleBoard board) {
        final ArrayList<String> words = new ArrayList<>();
        final boolean[][] marked = new boolean[board.rows()][board.cols()];
        for (int x = 0; x < board.rows(); x++) {
            for (int y = 0; y < board.cols(); y++) {
                dfs(x, y, "", words, marked, board);
            }
        }
        return words;
    }

    private void dfs(final int x, final int y, String current, final ArrayList<String> words, final boolean[][] marked, final BoggleBoard board) {
        if (x < 0 || x >= board.rows() || y < 0 || y >= board.cols()) return;
        final char c = board.getLetter(x, y);
        current += c;
        if (c == 'Q') current += 'U';
        if (marked[x][y]) return;
        if (dict.contains(current) && current.length() > 2 && !words.contains(current)) words.add(current);
        if (!dict.prefix(current)) return;
        marked[x][y] = true;
        for (final int[] coord : adj(x, y, board))
            if (!marked[coord[0]][coord[1]]) dfs(coord[0], coord[1], current, words, marked, board);
        marked[x][y] = false;
    }

    private Iterable<int[]> adj(final int x, final int y, final BoggleBoard board) {
        final ArrayList<int[]> adj = new ArrayList<>();
        for (int x2 = x - 1; x2 <= x + 1; x2++) {
            if (x2 < 0 || x2 >= board.rows()) continue;
            for (int y2 = y - 1; y2 <= y + 1; y2++) {
                if (y2 < 0 || y2 >= board.cols() || x2 == x && y2 == y) continue;
                adj.add(new int[]{x2, y2});
            }
        }
        return adj;
    }

    public int scoreOf(final String word) {
        if (!dict.contains(word) || word.length() < 3) return 0;
        else if (word.length() < 5) return 1;
        else if (word.length() < 6) return 2;
        else if (word.length() < 7) return 3;
        else if (word.length() < 8) return 5;
        else return 11;
    }

    public static void main(final String[] args) {
        final In in = new In(args[0]);
        final String[] dictionary = in.readAllStrings();
        final BoggleSolver solver = new BoggleSolver(dictionary);
        final BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (final String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
