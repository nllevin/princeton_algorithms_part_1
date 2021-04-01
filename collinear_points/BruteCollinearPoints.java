/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
  private Point[] points;
  private int numSegments;
  private LineSegment[] lineSegments;

  public BruteCollinearPoints(Point[] inputPoints) {
    validateInput(inputPoints);
    points = inputPoints.clone();
    Arrays.sort(points);
    numSegments = countSegments();
    lineSegments = findLineSegments();
  }

  public int numberOfSegments() {
    return numSegments;
  }

  public LineSegment[] segments() {
    return lineSegments.clone();
  }

  private void validateInput(Point[] points) {
    if (points == null) throw new IllegalArgumentException();
    for (Point point : points) {
      if (point == null) throw new IllegalArgumentException();
    }
    for (int i = 0; i < points.length - 1; i++) {
      Point point1 = points[i];
      for (int j = i + 1; j < points.length; j++) {
        Point point2 = points[j];
        if (point1.compareTo(point2) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }
  }

  private int countSegments() {
    int count = 0;
    for (int p = 0; p < points.length - 3; p++) {
      Point point1 = points[p];
      for (int q = p + 1; q < points.length - 2; q++) {
        Point point2 = points[q];
        for (int r = q + 1; r < points.length - 1; r++) {
          Point point3 = points[r];
          for (int s = r + 1; s < points.length; s++) {
            Point point4 = points[s];
            if (pointsAreCollinear(point1, point2, point3, point4)) count++;
          }
        }
      }
    }
    return count;
  }

  private LineSegment[] findLineSegments() {
    LineSegment[] lineSegs = new LineSegment[numSegments];
    int numSegmentsFound = 0;

    for (int p = 0; p < points.length - 3; p++) {
      Point point1 = points[p];
      for (int q = p + 1; q < points.length - 2; q++) {
        Point point2 = points[q];
        for (int r = q + 1; r < points.length - 1; r++) {
          Point point3 = points[r];
          for (int s = r + 1; s < points.length; s++) {
            Point point4 = points[s];
            if (pointsAreCollinear(point1, point2, point3, point4)) {
              LineSegment segment = new LineSegment(point1, point4);
              lineSegs[numSegmentsFound] = segment;
              numSegmentsFound++;
            }
          }
        }
      }
    }

    return lineSegs;
  }

  private boolean pointsAreCollinear(Point p, Point q, Point r, Point s) {
    double slope1 = p.slopeTo(q);
    double slope2 = p.slopeTo(r);
    if (slope1 != slope2) return false;

    double slope3 = p.slopeTo(s);
    return slope2 == slope3;
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
