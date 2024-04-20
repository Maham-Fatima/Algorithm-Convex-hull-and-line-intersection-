package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuickEliminationGUI extends JFrame {

    private List<Point> points;
    private List<Point> convexHull;

    public QuickEliminationGUI() {
        points = new ArrayList<>();
        convexHull = new ArrayList<>();

        setTitle("Quick Elimination Convex Hull Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.RED); // Set background color to red

        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK); // Set button panel background color to black

        JButton generatePointsButton = new JButton("Generate Random Points");
        generatePointsButton.setForeground(Color.BLACK); // Set button text color to white
        generatePointsButton.setBackground(new Color(255, 255, 51)); // Set button background color to bright yellow
        generatePointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomPoints();
                drawingPanel.repaint();
            }
        });

        JButton runAlgorithmButton = new JButton("Run Convex Hull Algorithm");
        runAlgorithmButton.setForeground(new Color(255, 255, 51)); // Set button text color to bright yellow
        runAlgorithmButton.setBackground(new Color(255, 69, 0)); // Set button background color to red-orange
        runAlgorithmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runQuickElimination();
                drawingPanel.repaint();
            }
        });

        buttonPanel.add(generatePointsButton);
        buttonPanel.add(runAlgorithmButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generateRandomPoints() {
        points.clear();
        int numPoints = 20;

        for (int i = 0; i < numPoints; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            points.add(new Point(x, y));
        }

        convexHull.clear();
    }

    private void runQuickElimination() {
        convexHull = QuickElimination.quickElimination(points);
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw points
            g.setColor(new Color(65, 105, 225)); // Set point color to bright yellow
            for (Point point : points) {
                g.fillOval(point.x - 5, point.y - 5, 10, 10);
            }

            // Draw convex hull
            g.setColor(new Color(255, 69, 0)); // Set convex hull color to red-orange
            for (Point hullPoint : convexHull) {
                g.fillOval(hullPoint.x - 5, hullPoint.y - 5, 10, 10);
            }

            // Connect convex hull points
            g.setColor(new Color(255, 69, 0)); // Set line color to red-orange
            for (int i = 0; i < convexHull.size() - 1; i++) {
                Point p1 = convexHull.get(i);
                Point p2 = convexHull.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuickEliminationGUI().setVisible(true);
            }
        });
    }
}

class QuickElimination {

    public static List<Point> quickElimination(List<Point> points) {
        if (points.size() < 3) {
            // Convex hull not possible with less than 3 points
            return points;
        }

        // Sort points based on x-coordinate
        points.sort(Comparator.comparingInt(point -> point.x));

        List<Point> upperHull = new ArrayList<>();
        List<Point> lowerHull = new ArrayList<>();

        // Build upper and lower hulls
        for (Point point : points) {
            while (upperHull.size() >= 2 &&
                    orientation(upperHull.get(upperHull.size() - 2), upperHull.get(upperHull.size() - 1), point) <= 0) {
                upperHull.remove(upperHull.size() - 1);
            }
            upperHull.add(point);
        }

        for (int i = points.size() - 1; i >= 0; i--) {
            Point point = points.get(i);
            while (lowerHull.size() >= 2 &&
                    orientation(lowerHull.get(lowerHull.size() - 2), lowerHull.get(lowerHull.size() - 1), point) <= 0) {
                lowerHull.remove(lowerHull.size() - 1);
            }
            lowerHull.add(point);
        }

        // Remove duplicate points at the start and end of upperHull and lowerHull
        upperHull.remove(upperHull.size() - 1);
        lowerHull.remove(lowerHull.size() - 1);
        upperHull.addAll(lowerHull);

        return upperHull;
    }

    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Collinear
        return (val > 0) ? 1 : -1; // Clockwise or counterclockwise
    }
}
