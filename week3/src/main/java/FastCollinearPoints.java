import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private final LineSegment[] seg;

    public FastCollinearPoints(final Point[] points) {
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
        final Point[] sortedPoints = pointsCopy.clone();
        for (int i = 0; i < pointsCopy.length; i++) {
            Arrays.sort(sortedPoints, pointsCopy[i].slopeOrder());
            int beg = 1;
            int end = 2;
            while (beg < pointsCopy.length) {
                while (end < pointsCopy.length
                        && Double.compare(pointsCopy[i].slopeTo(sortedPoints[beg]), pointsCopy[i].slopeTo(sortedPoints[end])) == 0)
                    end++;
                if (end - beg >= 3) {
                    Arrays.sort(sortedPoints, beg, end);
                    if (pointsCopy[i].compareTo(sortedPoints[beg]) < 0)
                        segments.add(new LineSegment(pointsCopy[i], sortedPoints[end - 1]));
                }
                beg = end;
                end++;
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
        final FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (final LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.segments().length);
        StdDraw.show();
    }
}