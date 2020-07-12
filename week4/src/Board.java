import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] board;
    private int zeroX;
    private int zeroY;
    private int twinY, twinX, sTwinY, sTwinX;
    public Board(final int[][] tiles) {
        this.board = clone2D(tiles);
        this.zeroX = 0;
        this.zeroY = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0) {
                    this.zeroY = y;
                    this.zeroX = x;
                }
            }
        }
        while (true) {
            twinY = StdRandom.uniform(dimension());
            twinX = StdRandom.uniform(dimension());
            sTwinY = StdRandom.uniform(dimension());
            sTwinX = StdRandom.uniform(dimension());
            if (board[twinY][twinX] != 0 && board[sTwinY][sTwinX] != 0 && board[twinY][twinX] != board[sTwinY][sTwinX]) break;
        }
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append(board.length);
        s.append("\n");
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x]<10) s.append("  ");
                else s.append(" ");
                s.append(board[y][x]);
            }
            s.append("\n");
        }
        return s.toString();
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
        } catch (final NullPointerException e) {
            return false;
        } catch (final ClassCastException c) {
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
                if ((Math.abs(zeroY - y) + Math.abs(zeroX - x)) == 1) {
                    final int[][] tmpA = clone2D(this.board);
                    exch(tmpA, y, x, zeroY, zeroX);
                    boards.add(new Board(tmpA));
                }
            }
        }
        return boards;
    }

    public Board twin() {
        final int[][] clone = clone2D(this.board);
        exch(clone, twinY, twinX, sTwinY, sTwinX);
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