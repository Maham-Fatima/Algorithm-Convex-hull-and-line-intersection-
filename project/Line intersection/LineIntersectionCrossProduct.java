package org.example;

import java.awt.Point;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LineIntersectionCrossProduct extends JFrame {
    private Point[] line1, line2;
    private int pointCount;

    public LineIntersectionCrossProduct() {
        line1 = new Point[2];
        line2 = new Point[2];
        pointCount = 0;

        setTitle("Line Intersection (Cross Product)");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 240, 240)); // Light pastel background color

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pointCount == 0) {
                    line1[0] = e.getPoint();
                    pointCount++;
                } else if (pointCount == 1) {
                    line1[1] = e.getPoint();
                    pointCount++;
                    repaint();
                } else if (pointCount == 2) {
                    line2[0] = e.getPoint();
                    pointCount++;
                } else if (pointCount == 3) {
                    line2[1] = e.getPoint();
                    pointCount = 0; // Reset pointCount for the next pair of lines
                    repaint();
                    checkIntersection();
                }
            }
        });
    }

    private void checkIntersection() {
        int crossProduct1 = computeCrossProduct(line1[0], line1[1], line2[0]);
        int crossProduct2 = computeCrossProduct(line1[0], line1[1], line2[1]);

        if ((crossProduct1 > 0 && crossProduct2 < 0) || (crossProduct1 < 0 && crossProduct2 > 0)) {
            JOptionPane.showMessageDialog(this, "Line segments intersect!");
        } else {
            JOptionPane.showMessageDialog(this, "Line segments do not intersect.");
        }
    }

    private int computeCrossProduct(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (line1[0] != null && line1[1] != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 102, 204)); // Bold blue color
            g2d.setStroke(new BasicStroke(5)); // Set line thickness
            g2d.drawLine(line1[0].x, line1[0].y, line1[1].x, line1[1].y);
            drawDirection(g, line1[0], line1[1], "Line 1");
        }

        if (line2[0] != null && line2[1] != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(204, 0, 0)); // Bold red color
            g2d.setStroke(new BasicStroke(5)); // Set line thickness
            g2d.drawLine(line2[0].x, line2[0].y, line2[1].x, line2[1].y);
            drawDirection(g, line2[0], line2[1], "Line 2");
        }
    }

    private void drawDirection(Graphics g, Point p1, Point p2, String label) {
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;

        g.setColor(Color.BLACK);
        g.drawString(label, midX, midY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineIntersectionCrossProduct intersectionGUI = new LineIntersectionCrossProduct();
            JOptionPane.showMessageDialog(intersectionGUI, "Click on two points for Line 1, then two points for Line 2.");
            intersectionGUI.setVisible(true);
        });
    }
}

