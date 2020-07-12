import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Node solved;
    private final boolean solvable;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private Node prev;
        private final int prio;
        private final int manhattan;

        public Node(final Board board, final int moves) {
            this.board = board;
            this.moves = moves;
            this.manhattan = this.board.manhattan();
            this.prio = this.manhattan + this.moves;
        }

        public Node(final Board board, final int moves, final Node prev) {
            this(board, moves);
            this.prev = prev;
        }

        public int compareTo(final Node that) {
            if (this.prio < that.prio)
                return -1;
            if (this.prio > that.prio)
                return 1;
            else
                return 0;
        }
    }

    public Solver(final Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        final MinPQ<Node> pq = new MinPQ<>();
        final MinPQ<Node> altPQ = new MinPQ<>();
        pq.insert(new Node(initial, 0));
        altPQ.insert(new Node(initial.twin(), 0));
        int step = 0;
        while (!pq.min().board.isGoal() && !altPQ.min().board.isGoal()) {
            if (step % 2 != 0) {
                final Node min = pq.delMin();
                for (final Board b : min.board.neighbors()) {
                    if (min.moves == 0) {
                        pq.insert(new Node(b, min.moves + 1, min));
                        continue;
                    }
                    if (!min.prev.board.equals(b))
                        pq.insert(new Node(b, min.moves + 1, min));
                }
            } else {
                final Node min = altPQ.delMin();
                for (final Board b : min.board.neighbors()) {
                    if (min.moves == 0) {
                        altPQ.insert(new Node(b, min.moves + 1, min));
                        continue;
                    }
                    if (!min.prev.board.equals(b))
                        altPQ.insert(new Node(b, min.moves + 1, min));
                }
            }
            step++;
        }
        this.solvable = pq.min().manhattan == 0;
        this.solved = pq.min();
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public int moves() {
        if (!solvable)
            return -1;
        else
            return solved.moves;
    }

    public Iterable<Board> solution() {
        if (!solvable)
            return null;
        Node current = solved;
        final Stack<Board> boards = new Stack<>();
        while (current != null) {
            boards.push(current.board);
            current = current.prev;
        }
        return boards;
    }

    public static void main(final String[] args) {

        // create initial board from file
        final In in = new In(args[0]);
        final int n = in.readInt();
        final int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        final Board initial = new Board(tiles);

        // solve the puzzle
        final Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (final Board board : solver.solution())
                StdOut.println(board);
        }
    }
}