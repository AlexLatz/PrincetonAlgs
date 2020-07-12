import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    final Digraph digraph;
    HashMap<Set<Integer>, int[]> prev;
    public SAP(final Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        digraph = new Digraph(G);
        prev = new HashMap<>();
    }

    public int length(final int v, final int w) {
        checkInput(v, w);
        final Set<Integer> verts = prep(v, w);
        if (prev.get(verts) != null)
            return prev.get(verts)[0];
        else
            return -1;
    }

    public int ancestor(final int v, final int w) {
        checkInput(v, w);
        final Set<Integer> verts = prep(v, w);
        if (prev.get(verts) != null)
            return prev.get(verts)[1];
        else
            return -1;
    }

    public int length(final Iterable<Integer> v, final Iterable<Integer> w) {
        checkInput(v, w);
        return sap(v, w)[0];
    }

    public int ancestor(final Iterable<Integer> v, final Iterable<Integer> w) {
        return sap(v, w)[1];
    }

    private void sap(final int v, final int w) {
        final Set<Integer> verts = new HashSet<>();
        verts.add(v);
        verts.add(w);
        final BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        final BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (vPath.hasPathTo(i) && vPath.distTo(i) < distance && wPath.hasPathTo(i) && wPath.distTo(i) < distance) {
                final int totalDist = vPath.distTo(i) + wPath.distTo(i);
                if (distance > totalDist) {
                    distance = totalDist;
                    ancestor = i;
                }
            }
        }
        if (distance != Integer.MAX_VALUE)
            prev.put(verts, new int[] { distance, ancestor });
    }

    private int[] sap(final Iterable<Integer> v, final Iterable<Integer> w) {
        final BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        final BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (vPath.hasPathTo(i) && vPath.distTo(i) < distance && wPath.hasPathTo(i) && wPath.distTo(i) < distance) {
                final int totalDist = vPath.distTo(i) + wPath.distTo(i);
                if (distance > totalDist) {
                    distance = totalDist;
                    ancestor = i;
                }
            }
        }
        if (distance != Integer.MAX_VALUE)
            return new int[] { distance, ancestor };
        else
            return new int[] { -1, -1 };
    }

    private Set<Integer> prep(final int v, final int w) {
        sap(v, w);
        final Set<Integer> verts = new HashSet<>();
        verts.add(v);
        verts.add(w);
        return verts;
    }
    private void checkInput(int a, int b) {
        if (a > digraph.V() || a < 0 || b > digraph.V() || b < 0) throw new IllegalArgumentException();
    }
    private void checkInput(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer n : v) if (n == null || n.intValue() > digraph.V() || n.intValue() < 0) throw new IllegalArgumentException(); 
        for (Integer n : w) if (n == null || n.intValue() > digraph.V() || n.intValue() < 0) throw new IllegalArgumentException(); 
    }
    public static void main(final String[] args) {
        final In in = new In(args[0]);
        final Digraph G = new Digraph(in);
        final SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            final int v = StdIn.readInt();
            final int w = StdIn.readInt();
            final int length = sap.length(v, w);
            final int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}