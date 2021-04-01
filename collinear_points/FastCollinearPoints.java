/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
  private Point[] points;
  private Point[] pointsCopy;
  private int numSegments;
  private LineSegment[] lineSegments;

  public FastCollinearPoints(Point[] inputPoints) {
    validateInput(inputPoints);
    points = inputPoints.clone();
    pointsCopy = inputPoints.clone();
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
    if (points.length < 4) return 0;

    int numSegments = 0;
    for (int i = 0; i < points.length; i++) {
      Point p = points[i];
      Comparator<Point> slopeOrder = p.slopeOrder();
      Arrays.sort(pointsCopy);
      Arrays.sort(pointsCopy, slopeOrder);

      int numCollinearPoints = 2;
      Point startPoint = pointsCopy[1];
      Point endPoint = null;
      for (int j = 2; j < pointsCopy.length; j++) {
        Point currentPoint = pointsCopy[j];
        if (slopeOrder.compare(startPoint, currentPoint) == 0) {
          numCollinearPoints++;
          endPoint = currentPoint;
        } else {
          if (numCollinearPoints >= 4 && p.compareTo(startPoint) < 0 && p.compareTo(endPoint) < 0) {
            numSegments++;
          }

          numCollinearPoints = 2;
          startPoint = currentPoint;
        }
      }

      if (numCollinearPoints >= 4 && p.compareTo(startPoint) < 0 && p.compareTo(endPoint) < 0) {
        numSegments++;
      }
    }
    return numSegments;
  }

  private LineSegment[] findLineSegments() {
    if (points.length < 4) return new LineSegment[0];

    LineSegment[] lineSegs = new LineSegment[numSegments];
    int numSegmentsFound = 0;
    for (int i = 0; i < points.length; i++) {
      Point p = points[i];
      Comparator<Point> slopeOrder = p.slopeOrder();
      Arrays.sort(pointsCopy);
      Arrays.sort(pointsCopy, slopeOrder);

      int numCollinearPoints = 2;
      Point startPoint = pointsCopy[1];
      Point endPoint = null;

      for (int j = 2; j < pointsCopy.length; j++) {
        Point currentPoint = pointsCopy[j];
        if (slopeOrder.compare(startPoint, currentPoint) == 0) {
          numCollinearPoints++;
          endPoint = currentPoint;
        } else {
          if (numCollinearPoints >= 4 && p.compareTo(startPoint) < 0 && p.compareTo(endPoint) < 0) {
            LineSegment segment = new LineSegment(p, endPoint);
            lineSegs[numSegmentsFound] = segment;
            numSegmentsFound++;
          }

          numCollinearPoints = 2;
          startPoint = currentPoint;
        }
      }

      if (numCollinearPoints >= 4 && p.compareTo(startPoint) < 0 && p.compareTo(endPoint) < 0) {
        LineSegment segment = new LineSegment(p, endPoint);
        lineSegs[numSegmentsFound] = segment;
        numSegmentsFound++;
      }
    }

    return lineSegs;
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
