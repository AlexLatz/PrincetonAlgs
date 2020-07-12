import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> tree;

    public PointSET() {
        tree = new SET<>();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int size() {
        return tree.size();
    }

    public void insert(final Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!tree.contains(p))
            tree.add(p);
    }

    public boolean contains(final Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return tree.contains(p);
    }

    public void draw() {
        for (final Point2D p : tree) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(final RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        final ArrayList<Point2D> points = new ArrayList<>();
        for (final Point2D p : tree) {
            if (rect.contains(p))
                points.add(p);
        }
        return points;
    }

    public Point2D nearest(final Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (size() == 0) return null;
        Point2D min = tree.min();
        for (final Point2D point : tree) {
            if (p.distanceSquaredTo(point) < p.distanceSquaredTo(min)) min = point;
        }
        return min;
    }
}