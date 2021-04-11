/* *****************************************************************************
 *  Name:              Noah Levin
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class KdTree {
  private Node root = null;

  public boolean isEmpty() {
    return root == null;
  }

  public int size() {
    return size(root);
  }

  private int size(Node node) {
    if (node == null) {
      return 0;
    }

    return node.size;
  }

  public void insert(Point2D point) {
    validateItem(point);

    root = insert(point, root, 0);
  }

  private Node insert(Point2D point, Node node, int depth) {
    if (node == null) {
      return new Node(point);
    } else if (point.equals(node.point)) {
      return node;
    }

    double cmp;
    if (depth % 2 == 0) {
      cmp = point.x() - node.point.x();
    } else {
      cmp = point.y() - node.point.y();
    }

    if (cmp < 0) {
      node.left = insert(point, node.left, depth + 1);
    } else {
      node.right = insert(point, node.right, depth + 1);
    }
    node.size = 1 + size(node.left) + size(node.right);

    return node;
  }

  public boolean contains(Point2D point) {
    validateItem(point);

    return contains(point, root, 0);
  }

  private boolean contains(Point2D point, Node node, int depth) {
    if (node == null) {
      return false;
    } else if (point.equals(node.point)) {
      return true;
    }

    double cmp;
    if (depth % 2 == 0) {
      cmp = point.x() - node.point.x();
    } else {
      cmp = point.y() - node.point.y();
    }

    if (cmp < 0) {
      return contains(point, node.left, depth + 1);
    } else {
      return contains(point, node.right, depth + 1);
    }
  }

  public void draw() {
    LinkedList<Node> queueBFS = new LinkedList<Node>();
    LinkedList<Integer> depths = new LinkedList<Integer>();
    LinkedList<RectHV> rects = new LinkedList<RectHV>();

    queueBFS.add(root);
    depths.add(0);
    rects.add(new RectHV(0, 0, 1, 1));

    while (!queueBFS.isEmpty()) {
      Node node = queueBFS.removeFirst();
      int depth = depths.removeFirst();
      RectHV rect = rects.removeFirst();

      Point2D point = node.point;
      StdDraw.setPenRadius();
      if (depth % 2 == 0) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
      } else {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
      }
      StdDraw.setPenRadius(0.01);
      StdDraw.setPenColor();
      StdDraw.point(point.x(), point.y());

      if (node.left != null) {
        queueBFS.add(node.left);
        depths.add(depth + 1);

        if (depth % 2 == 0) {
          rects.add(new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax()));
        } else {
          rects.add(new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y()));
        }
      }

      if (node.right != null) {
        queueBFS.add(node.right);
        depths.add(depth + 1);

        if (depth % 2 == 0) {
          rects.add(new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } else {
          rects.add(new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax()));
        }
      }
    }

    StdDraw.show();
  }

  public Iterable<Point2D> range(RectHV rect) {
    validateItem(rect);

    LinkedList<Point2D> rangePoints = new LinkedList<Point2D>();
    if (!isEmpty()) {
      rangeSearch(root, rect, rangePoints, 0);
    }
    return rangePoints;
  }

  private void rangeSearch(Node node, RectHV rect, LinkedList<Point2D> rangePoints, int depth) {
    Point2D point = node.point;
    if (rect.contains(point)) {
      rangePoints.add(point);
    }

    boolean evenDepth = (depth % 2 == 0);
    boolean shouldGoLeft = evenDepth ? point.x() > rect.xmin() : point.y() > rect.ymin();
    if (shouldGoLeft && node.left != null) {
      rangeSearch(node.left, rect, rangePoints, depth + 1);
    }

    boolean shouldGoRight = evenDepth ? point.x() <= rect.xmax() : point.y() <= rect.ymax();
    if (shouldGoRight && node.right != null) {
      rangeSearch(node.right, rect, rangePoints, depth + 1);
    }
  }

  public Point2D nearest(Point2D queryPoint) {
    validateItem(queryPoint);

    Point2D nearestPoint = null;
    double minDistanceSquared = Double.POSITIVE_INFINITY;
    LinkedList<Node> nodesStack = new LinkedList<Node>();
    LinkedList<Integer> nodesDepth = new LinkedList<Integer>();
    LinkedList<RectHV> nodeRects = new LinkedList<RectHV>();

    if (!isEmpty()) {
      nodesStack.push(root);
      nodesDepth.push(0);
      nodeRects.push(new RectHV(0, 0, 1, 1));
    }

    while (!nodesStack.isEmpty()) {
      Node node = nodesStack.pop();
      Point2D point = node.point;
      int depth = nodesDepth.pop();
      RectHV rect = nodeRects.pop();

      if (rect.distanceSquaredTo(queryPoint) > minDistanceSquared) {
        continue;
      }

      double distanceSquared = point.distanceSquaredTo(queryPoint);
      if (distanceSquared < minDistanceSquared) {
        minDistanceSquared = distanceSquared;
        nearestPoint = point;
      }

      Node leftNode = node.left;
      Node rightNode = node.right;
      int newDepth = depth + 1;
      boolean goLeftFirst;
      RectHV leftNodeRect;
      RectHV rightNodeRect;
      if (depth % 2 == 0) {
        goLeftFirst = queryPoint.x() < point.x();
        leftNodeRect = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
        rightNodeRect = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
      } else {
        goLeftFirst = queryPoint.y() < point.y();
        leftNodeRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        rightNodeRect = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
      }

      if (!goLeftFirst && leftNode != null) {
        nodesStack.push(leftNode);
        nodesDepth.push(newDepth);
        nodeRects.push(leftNodeRect);
      }
      if (rightNode != null) {
        nodesStack.push(rightNode);
        nodesDepth.push(newDepth);
        nodeRects.push(rightNodeRect);
      }
      if (goLeftFirst && leftNode != null) {
        nodesStack.push(leftNode);
        nodesDepth.push(newDepth);
        nodeRects.push(leftNodeRect);
      }
    }

    return nearestPoint;
  }

  private void validateItem(Object item) {
    if (item == null) throw new IllegalArgumentException();
  }

  private class Node {
    private Point2D point;
    private Node left;
    private Node right;
    private int size;

    public Node(Point2D p) {
      point = p;
      size = 1;
    }
  }

  public static void main(String[] args) {
    KdTree tree = new KdTree();
    Point2D firstPoint = new Point2D(0.05, 0.05);
    RectHV rect = new RectHV(0, 0, 0.5, 0.5);

    StdOut.println(tree.isEmpty());
    StdOut.println(tree.size());
    StdOut.println(tree.contains(new Point2D(0.05, 0.05)));
    tree.insert(firstPoint);
    StdOut.println(tree.size());
    StdOut.println(tree.contains(new Point2D(0.05, 0.05)));

    tree.insert(new Point2D(0.9, 0.9));
    tree.insert(new Point2D(0.2, 0.3));
    tree.insert(new Point2D(0.5, 0.4));
    tree.insert(new Point2D(0.8, 0.8));
    tree.insert(new Point2D(0.1, 0.7));
    tree.insert(new Point2D(0.85, 0.85));

    StdOut.println(tree.size());

    tree.insert(new Point2D(0.9, 0.9));
    StdOut.println(tree.size());

    StdOut.println(tree.contains(new Point2D(0.2, 0.3)));
    StdOut.println(tree.contains(new Point2D(0.2, 0.31)));

    StdOut.println(tree.range(rect));
    StdOut.println(tree.nearest(new Point2D(0.6, 0.6)));
    StdOut.println(tree.nearest(new Point2D(0.2, 0.8)));

    tree.draw();
  }
}
