import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] board;
    private final int[] zero;

    public Board(final int[][] tiles) {
        this.board = clone2D(tiles);
        this.zero = new int[2];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0) {
                    zero[0] = y;
                    zero[1] = x;
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = board.length + "\n";
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                s += " " + board[y][x];
            }
            s += "\n";
        }
        return s;
    }

    public int dimension() {
        return board.length;
    }

    private int[] numToCoords(final int num) {
        if (num < 1)
            throw new IllegalArgumentException("0 in coord converter");
        return new int[] { (num - 1) / dimension(), (num - 1) % dimension() };
    }

    private int[][] clone2D(final int[][] arr) {
        final int[][] newArr = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i].clone();
        }
        return newArr;
    }

    public int hamming() {
        int count = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0)
                    continue;
                final int num = y * dimension() + (x + 1);
                if (board[y][x] != num)
                    count++;
            }
        }
        return count;
    }

    public int manhattan() {
        int sum = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0)
                    continue;
                final int[] coords = numToCoords(board[y][x]);
                sum += Math.abs(coords[0] - y) + Math.abs(coords[1] - x);
            }
        }
        return sum;
    }

    public boolean isGoal() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0)
                    continue;
                final int[] coords = numToCoords(board[y][x]);
                if (coords[0] != y || coords[1] != x)
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        try {
            final Board that = (Board) obj;
            if (that.dimension() != this.dimension())
                return false;
            return Arrays.deepEquals(this.board, that.board);
        } catch (final Exception e) {
            return false;
        }
    }

    private void exch(final int[][] arr, final int y1, final int x1, final int y2, final int x2) {
        final int tmp = arr[y1][x1];
        arr[y1][x1] = arr[y2][x2];
        arr[y2][x2] = tmp;
    }

    public Iterable<Board> neighbors() {
        final ArrayList<Board> boards = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if ((Math.abs(zero[0] - y) + Math.abs(zero[1] - x)) == 1) {
                    final int[][] tmpA = clone2D(this.board);
                    exch(tmpA, y, x, zero[0], zero[1]);
                    boards.add(new Board(tmpA));
                }
            }
        }
        return boards;
    }

    public Board twin() {
        int[] swapA = zero;
        int[] swapB = zero;
        while (Arrays.equals(swapA, zero) || Arrays.equals(swapB, zero) || Arrays.equals(swapA, swapB)) {
            swapA = new int[] { StdRandom.uniform(dimension()), StdRandom.uniform(dimension()) };
            swapB = new int[] { StdRandom.uniform(dimension()), StdRandom.uniform(dimension()) };
        }
        final int[][] clone = clone2D(this.board);
        exch(clone, swapA[0], swapA[1], swapB[0], swapB[1]);
        return new Board(clone);
    }

    public static void main(final String[] args) {
        final In in = new In(args[0]);
        final int n = in.readInt();
        final int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        final Board initial = new Board(tiles);
        StdOut.println(initial);
        StdOut.println("dimension: " + initial.dimension());
        StdOut.println("hamming: " + initial.hamming());
        StdOut.println("manhattan: " + initial.manhattan());
        StdOut.println("solved: " + initial.isGoal());
        final Board other = new Board(tiles);
        StdOut.println("equals method works: " + initial.equals(other));
        StdOut.println("possible moves:");
        for (final Board b : initial.neighbors()) {
            StdOut.println(b);
        }
        StdOut.println("twin: " + initial.twin());
    }
}