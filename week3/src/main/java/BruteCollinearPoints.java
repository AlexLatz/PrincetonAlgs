import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private final LineSegment[] seg;

    public BruteCollinearPoints(final Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (int i1 = 0; i1 < points.length; i1++) {
            if (points[i1] == null)
                throw new IllegalArgumentException();
        }
        final Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        for (int j = 0; j < pointsCopy.length - 1; j++) {
            if (pointsCopy[j].compareTo(pointsCopy[j + 1]) == 0)
                throw new IllegalArgumentException();
        }
        final ArrayList<LineSegment> segments = new ArrayList<>();
        for (int p = 0; p < (pointsCopy.length - 3); p++) {
            for (int q = p + 1; q < (pointsCopy.length - 2); q++) {
                final double qSlope = pointsCopy[p].slopeTo(pointsCopy[q]);
                for (int r = q + 1; r < (pointsCopy.length - 1); r++) {
                    final double rSlope = pointsCopy[p].slopeTo(pointsCopy[r]);
                    for (int s = r + 1; s < pointsCopy.length; s++) {
                        final double sSlope = pointsCopy[p].slopeTo(pointsCopy[s]);
                        if (Double.compare(qSlope, rSlope) == 0 && Double.compare(qSlope, sSlope) == 0) {
                            segments.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }
        seg = segments.toArray(new LineSegment[segments.size()]);
    }

    public int numberOfSegments() {
        return seg.length;
    }

    public LineSegment[] segments() {
        return seg.clone();
    }

    public static void main(final String[] args) {

        // read the n points from a file
        final In in = new In(args[0]);
        final int n = in.readInt();
        final Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            final int x = in.readInt();
            final int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (final Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        final BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (final LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
