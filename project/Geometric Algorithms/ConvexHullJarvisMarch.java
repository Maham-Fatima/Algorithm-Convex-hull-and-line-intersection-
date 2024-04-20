package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ConvexHullJarvisMarch extends JFrame {
    private List<Point> points;
    private List<Point> convexHull;

    public ConvexHullJarvisMarch() {
        points = new ArrayList<>();
        convexHull = new ArrayList<>();

        setTitle("Convex Hull (Jarvis March)");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 240, 240)); // Light pastel background color

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                repaint();
            }
        });

        JButton findConvexHullButton = new JButton("Find Convex Hull");
        findConvexHullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findConvexHull();
                repaint();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(findConvexHullButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void findConvexHull() {
        if (points.size() < 3) {
            JOptionPane.showMessageDialog(this, "At least 3 points are required to find the convex hull.");
            return;
        }

        convexHull = JarvisMarch.findConvexHull(points);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw points
        g2d.setColor(Color.BLUE);
        for (Point point : points) {
            g2d.fillOval(point.x - 5, point.y - 5, 10, 10);
        }

        // Draw convex hull
        g2d.setColor(Color.RED);
        for (int i = 0; i < convexHull.size() - 1; i++) {
            Point p1 = convexHull.get(i);
            Point p2 = convexHull.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        if (!convexHull.isEmpty()) {
            Point firstPoint = convexHull.get(0);
            Point lastPoint = convexHull.get(convexHull.size() - 1);
            g2d.drawLine(lastPoint.x, lastPoint.y, firstPoint.x, firstPoint.y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConvexHullJarvisMarch convexHullGUI = new ConvexHullJarvisMarch();
            convexHullGUI.setVisible(true);
        });
    }
}

class JarvisMarch {
    public static List<Point> findConvexHull(List<Point> points) {
        int n = points.size();
        if (n < 3) {
            return points;
        }

        List<Point> hull = new ArrayList<>();
        int l = 0;
        for (int i = 1; i < n; i++) {
            if (points.get(i).x < points.get(l).x) {
                l = i;
            }
        }

        int p = l, q;
        do {
            hull.add(points.get(p));
            q = (p + 1) % n;
            for (int i = 0; i < n; i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                    q = i;
                }
            }
            p = q;
        } while (p != l);

        return hull;
    }

    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return 0; // colinear
        }
        return (val > 0) ? 1 : 2; // clockwise or counterclockwise
    }
}
