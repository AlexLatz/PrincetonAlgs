import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private class Node {
        private Node lb;
        private Node rt;
        private final boolean horizontal;
        private final Point2D p;
        private final RectHV rect;

        public Node(final boolean horizontal, final Point2D p, final RectHV rect) {
            this.horizontal = horizontal;
            this.p = p;
            this.rect = rect;
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            p.draw();
            if (horizontal) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            }
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(final Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(false, p, new RectHV(0, 0, 1, 1));
            size = 1;
            return;
        }
        Node x = root;
        Node prev = null;
        int level = 1;
        while (x != null) {
            prev = x;
            if (level % 2 != 0) {
                if (p.x() < x.p.x())
                    x = x.lb;
                else if (p.x() == x.p.x() && p.y() == x.p.y())
                    return;
                else
                    x = x.rt;
            } else {
                if (p.y() < x.p.y())
                    x = x.lb;
                else if (p.y() == x.p.y() && p.x() == x.p.x())
                    return;
                else
                    x = x.rt;
            }
            level++;
        }
        if (level % 2 != 0) {
            if (p.y() < prev.p.y()) {
                prev.lb = new Node(false, p,
                        new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.rect.xmax(), prev.p.y()));
            } else {
                prev.rt = new Node(false, p,
                        new RectHV(prev.rect.xmin(), prev.p.y(), prev.rect.xmax(), prev.rect.ymax()));
            }
        } else {
            if (p.x() < prev.p.x()) {
                prev.lb = new Node(true, p,
                        new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.p.x(), prev.rect.ymax()));
            } else {
                prev.rt = new Node(true, p,
                        new RectHV(prev.p.x(), prev.rect.ymin(), prev.rect.xmax(), prev.rect.ymax()));
            }
        }
        size++;
    }

    public boolean contains(final Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        while (x != null) {
            if (x.p.equals(p))
                return true;
            if (x.horizontal) {
                if (p.y() < x.p.y())
                    x = x.lb;
                else
                    x = x.rt;
            } else {
                if (p.x() < x.p.x())
                    x = x.lb;
                else
                    x = x.rt;
            }
        }
        return false;
    }

    public void draw() {
        draw(root);
    }

    private void draw(final Node x) {
        if (x == null)
            return;
        x.draw();
        draw(x.lb);
        draw(x.rt);
    }

    public Iterable<Point2D> range(final RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        final ArrayList<Point2D> points = new ArrayList<>();
        range(points, rect, root);
        return points;
    }

    private void range(final ArrayList<Point2D> points, final RectHV rect, final Node x) {
        if (x == null || !x.rect.intersects(rect))
            return;
        if (rect.contains(x.p))
            points.add(x.p);
        range(points, rect, x.lb);
        range(points, rect, x.rt);
    }

    public Point2D nearest(final Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        final Point2D best = root.p;
        return nearest(best, p, root);
    }

    private Point2D nearest(Point2D best, final Point2D p, final Node x) {
        if (x == null)
            return best;
        final double dist = best.distanceSquaredTo(p);
        if (x.rect.distanceSquaredTo(p) < dist) {
            if (x.p.distanceSquaredTo(p) < dist)
                best = x.p;
            if (x.horizontal) {
                if (x.p.y() > p.y()) {
                    best = nearest(best, p, x.lb);
                    best = nearest(best, p, x.rt);
                } else {
                    best = nearest(best, p, x.rt);
                    best = nearest(best, p, x.lb);
                }
            } else {
                if (x.p.x() > p.x()) {
                    best = nearest(best, p, x.lb);
                    best = nearest(best, p, x.rt);
                } else {
                    best = nearest(best, p, x.rt);
                    best = nearest(best, p, x.lb);
                }
            }
        }
        return best;
    }

    public static void main(final String[] args) {
        /*
         * final RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
         * StdDraw.enableDoubleBuffering(); final KdTree kdtree = new KdTree(); while
         * (true) { if (StdDraw.isMousePressed()) { final double x = StdDraw.mouseX();
         * final double y = StdDraw.mouseY(); StdOut.printf("%8.6f %8.6f\n", x, y);
         * final Point2D p = new Point2D(x, y); if (rect.contains(p)) {
         * StdOut.printf("%8.6f %8.6f\n", x, y); kdtree.insert(p); StdDraw.clear();
         * kdtree.draw(); StdDraw.show(); } } StdDraw.pause(20); }
         */
        final KdTree tree = new KdTree();
        tree.insert(new Point2D(0, 1));
        tree.insert(new Point2D(1, 0));
        tree.insert(new Point2D(1, 0));
        tree.insert(new Point2D(0, 0));
        for (final Point2D p : tree.range(new RectHV(0, 0, 1, 1))) {
            StdOut.println(p);
        }
    }
}