import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node solved;
    private boolean solvable;
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;
        private int prio;
        private int manhattan;
        public Node(Board board, int moves) {
            this.board = board;
            this.moves = moves;
            this.manhattan = this.board.manhattan();
            this.prio = this.manhattan + this.moves;
        }
        public Node(Board board, int moves, Node prev) {
            this(board,moves);
            this.prev = prev;
        }
        public int compareTo(Node that) {
            if (this.prio < that.prio) return -1;
            if (this.prio > that.prio) return 1;
            else return 0;
        }
    }
    public Solver(Board initial) {
        if (initial==null) throw new IllegalArgumentException();
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> altPQ = new MinPQ<>();
        pq.insert(new Node(initial, 0));
        altPQ.insert(new Node(initial.twin(), 0));
        int step = 0;
        while(pq.min().manhattan>0 && altPQ.min().manhattan>0) {
            if (step%2==1) {
                Node min = pq.delMin();
                for (Board b : min.board.neighbors()) {
                    if (min.moves==0) {
                        pq.insert(new Node(b, min.moves+1, min));
                        continue;
                    }
                    if (!min.prev.board.equals(b)) pq.insert(new Node(b, min.moves+1, min));
                }
            } else {
                Node min = altPQ.delMin();
                for (Board b : min.board.neighbors()) {
                    if (min.moves==0) {
                        altPQ.insert(new Node(b, min.moves+1, min));
                        continue;
                    }
                    if (!min.prev.board.equals(b)) altPQ.insert(new Node(b, min.moves+1, min));
                }
            }
            step++;
        }
        this.solvable = pq.min().manhattan==0;
        this.solved = pq.min();
    }    
    public boolean isSolvable() {
        return this.solvable;
    }
    public int moves() {
        if (!solvable) return -1;
        else return solved.moves;
    }
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Node current = solved;
        Stack<Board> boards = new Stack<>();
        while (current!=null) {
            boards.push(current.board); 
            current = current.prev;
        }
        return boards;
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}