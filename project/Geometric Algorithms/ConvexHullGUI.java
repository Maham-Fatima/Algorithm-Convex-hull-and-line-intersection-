package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class ConvexHullGUI extends JFrame {
    private ArrayList<Point> points;

    public ConvexHullGUI() {
        points = new ArrayList<>();

        setTitle("Convex Hull Graham Scan");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (Point point : points) {
            g.fillOval(point.x - 3, point.y - 3, 6, 6);
        }

        if (points.size() >= 3) {
            ArrayList<Point> convexHull = grahamScan(points);

            g.setColor(Color.RED);
            drawConvexHull(g, convexHull);
        }
    }

    private ArrayList<Point> grahamScan(ArrayList<Point> inputPoints) {
        ArrayList<Point> sortedPoints = new ArrayList<>(inputPoints);
        Collections.sort(sortedPoints, Comparator.comparingInt((Point p) -> p.y).thenComparingInt(p -> p.x));

        Point pivot = sortedPoints.remove(0);
        Collections.sort(sortedPoints, Comparator.comparingDouble(p -> Math.atan2(p.y - pivot.y, p.x - pivot.x)));

        Stack<Point> stack = new Stack<>();
        stack.push(pivot);
        stack.push(sortedPoints.get(0));
        stack.push(sortedPoints.get(1));

        for (int i = 2; i < sortedPoints.size(); i++) {
            while (orientation(nextToTop(stack), stack.peek(), sortedPoints.get(i)) != 2) {
                stack.pop();
            }
            stack.push(sortedPoints.get(i));
        }

        return new ArrayList<>(stack);
    }

    private Point nextToTop(Stack<Point> stack) {
        Point top = stack.pop();
        Point nextToTop = stack.peek();
        stack.push(top);
        return nextToTop;
    }

    private int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    private void drawConvexHull(Graphics g, ArrayList<Point> convexHull) {
        for (int i = 0; i < convexHull.size(); i++) {
            Point p1 = convexHull.get(i);
            Point p2 = convexHull.get((i + 1) % convexHull.size());
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConvexHullGUI convexHullGUI = new ConvexHullGUI();
            convexHullGUI.setVisible(true);
        });
    }
}
