package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickHullAnimation extends JFrame {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final int POINT_RADIUS = 6;

    private List<Point> points;

    public QuickHullAnimation() {
        initPoints();
        initUI();
        setVisible(true);
    }

    private void initPoints() {
        points = new ArrayList<>();

        // Use a loop to take points from the user
        int numPoints = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of points:"));
        for (int i = 0; i < numPoints; i++) {
            int x = Integer.parseInt(JOptionPane.showInputDialog("Enter x-coordinate for point " + (i + 1) + ":"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Enter y-coordinate for point " + (i + 1) + ":"));
            points.add(new Point(x, y));
        }
    }

    private void initUI() {
        setTitle("QuickHull Visualization");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawPoints(g);
        drawConvexHull(g);
    }

    private void drawPoints(Graphics g) {
        g.setColor(Color.BLUE);
        for (Point point : points) {
            g.fillOval(point.x - POINT_RADIUS, point.y - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }
    }

    private void drawConvexHull(Graphics g) {
        List<Point> convexHull = quickHull(new ArrayList<>(points));
        g.setColor(Color.RED);

        for (int i = 0; i < convexHull.size() - 1; i++) {
            Point p1 = convexHull.get(i);
            Point p2 = convexHull.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Connect the last and first points to close the hull
        Point first = convexHull.get(0);
        Point last = convexHull.get(convexHull.size() - 1);
        g.drawLine(last.x, last.y, first.x, first.y);
    }

    private List<Point> quickHull(List<Point> pointList) {
        List<Point> convexHull = new ArrayList<>();

        if (pointList.size() < 3) {
            return new ArrayList<>(pointList);
        }

        int minIndex = 0;
        int maxIndex = 0;

        for (int i = 1; i < pointList.size(); i++) {
            if (pointList.get(i).x < pointList.get(minIndex).x) {
                minIndex = i;
            }
            if (pointList.get(i).x > pointList.get(maxIndex).x) {
                maxIndex = i;
            }
        }

        Point minPoint = pointList.get(minIndex);
        Point maxPoint = pointList.get(maxIndex);

        convexHull.add(minPoint);
        convexHull.add(maxPoint);

        pointList.remove(minPoint);
        pointList.remove(maxPoint);

        List<Point> leftSet = new ArrayList<>();
        List<Point> rightSet = new ArrayList<>();

        for (Point point : pointList) {
            if (isOnLeft(minPoint, maxPoint, point)) {
                leftSet.add(point);
            } else if (isOnLeft(maxPoint, minPoint, point)) {
                rightSet.add(point);
            }
        }

        hullSet(minPoint, maxPoint, rightSet, convexHull);
        hullSet(maxPoint, minPoint, leftSet, convexHull);

        return convexHull;
    }

    private void hullSet(Point p1, Point p2, List<Point> set, List<Point> hull) {
        int insertIndex = hull.indexOf(p2);

        if (set.isEmpty()) {
            return;
        }

        if (set.size() == 1) {
            Point point = set.get(0);
            set.remove(point);
            hull.add(insertIndex, point);
            return;
        }

        int index = -1;
        int dist = Integer.MIN_VALUE;

        for (int i = 0; i < set.size(); i++) {
            Point point = set.get(i);
            int distance = distance(p1, p2, point);

            if (distance > dist) {
                index = i;
                dist = distance;
            }
        }

        Point point = set.get(index);
        set.remove(index);
        hull.add(insertIndex, point);

        List<Point> leftSetP1 = new ArrayList<>();
        for (Point i : set) {
            if (isOnLeft(p1, point, i)) {
                leftSetP1.add(i);
            }
        }

        List<Point> leftSetP2 = new ArrayList<>();
        for (Point i : set) {
            if (isOnLeft(point, p2, i)) {
                leftSetP2.add(i);
            }
        }

        hullSet(p1, point, leftSetP1, hull);
        hullSet(point, p2, leftSetP2, hull);
    }

    private boolean isOnLeft(Point a, Point b, Point c) {
        int val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        return val > 0;
    }

    private int distance(Point a, Point b, Point c) {
        int num = (b.y - a.y) * c.x - (b.x - a.x) * c.y + b.x * a.y - b.y * a.x;
        return Math.abs(num);
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuickHullAnimation::new);
    }
}

