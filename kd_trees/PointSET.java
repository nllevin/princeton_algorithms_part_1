/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
  private TreeSet<Point2D> points = new TreeSet<Point2D>();

  public boolean isEmpty() {
    return points.isEmpty();
  }

  public int size() {
    return points.size();
  }

  public void insert(Point2D point) {
    validateItem(point);

    points.add(point);
  }

  public boolean contains(Point2D point) {
    validateItem(point);

    return points.contains(point);
  }

  public void draw() {
    for (Point2D point : points) {
      point.draw();
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    validateItem(rect);

    LinkedList<Point2D> rangePoints = new LinkedList<Point2D>();
    for (Point2D point : points) {
      if (rect.contains(point)) {
        rangePoints.add(point);
      }
    }
    return rangePoints;
  }

  public Point2D nearest(Point2D queryPoint) {
    validateItem(queryPoint);

    double minDistanceSquared = Double.POSITIVE_INFINITY;
    Point2D nearestPoint = null;
    for (Point2D point : points) {
      double distanceSquared = point.distanceSquaredTo(queryPoint);
      if (distanceSquared < minDistanceSquared) {
        minDistanceSquared = distanceSquared;
        nearestPoint = point;
      }
    }
    return nearestPoint;
  }

  private void validateItem(Object item) {
    if (item == null) throw new IllegalArgumentException();
  }

  public static void main(String[] args) {
    PointSET set = new PointSET();
    Point2D origin = new Point2D(0, 0);
    RectHV rect = new RectHV(0.5, 0.5, 1, 1);

    StdOut.println(set.isEmpty());
    StdOut.println(set.contains(origin));
    set.insert(origin);
    StdOut.println(set.size());
    StdOut.println(set.contains(origin));

    set.insert(new Point2D(0.1, 0.7));
    set.insert(new Point2D(0.5, 0.7));
    set.insert(new Point2D(0.2, 0.3));
    set.insert(new Point2D(0.8, 0.8));
    set.insert(new Point2D(0.85, 0.85));
    set.insert(new Point2D(0.9, 0.9));

    StdOut.println(set.nearest(new Point2D(0.6, 0.6)));
    StdOut.println(set.range(rect));

    set.draw();
  }
}
