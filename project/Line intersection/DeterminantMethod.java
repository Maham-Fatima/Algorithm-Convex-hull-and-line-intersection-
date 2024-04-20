package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeterminantMethod extends JFrame {

    private JButton checkIntersectionButton;
    private JLabel resultLabel;

    public DeterminantMethod() {
        setTitle("Line Intersection Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLayout(new BorderLayout());

        // Panel for buttons and result label
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(144, 238, 144)); // Light green color

        checkIntersectionButton = new JButton("Check Intersection");
        checkIntersectionButton.setBackground(new Color(255, 165, 0)); // Orange color
        checkIntersectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIntersection();
            }
        });

        resultLabel = new JLabel("");
        resultLabel.setForeground(Color.BLUE);

        buttonPanel.add(checkIntersectionButton);
        buttonPanel.add(resultLabel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void checkIntersection() {
        try {
            // Prompt user for input coordinates
            int x1 = Integer.parseInt(JOptionPane.showInputDialog("Enter x1:"));
            int y1 = Integer.parseInt(JOptionPane.showInputDialog("Enter y1:"));
            int x2 = Integer.parseInt(JOptionPane.showInputDialog("Enter x2:"));
            int y2 = Integer.parseInt(JOptionPane.showInputDialog("Enter y2:"));
            int x3 = Integer.parseInt(JOptionPane.showInputDialog("Enter x3:"));
            int y3 = Integer.parseInt(JOptionPane.showInputDialog("Enter y3:"));
            int x4 = Integer.parseInt(JOptionPane.showInputDialog("Enter x4:"));
            int y4 = Integer.parseInt(JOptionPane.showInputDialog("Enter y4:"));
            

            // Check for intersection using determinant method
            boolean intersection = doLinesIntersect(x1, y1, x2, y2, x3, y3, x4, y4);

            // Display the result
            resultLabel.setText("Lines " + (intersection ? "intersect" : "do not intersect"));
        } catch (NumberFormatException ex) {
            // Show a dialog for input error
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter integers for coordinates.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Determinant method to check if two lines intersect
    private boolean doLinesIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int det1 = determinant(x1, y1, x2, y2, x3, y3);
        int det2 = determinant(x1, y1, x2, y2, x4, y4);
        int det3 = determinant(x3, y3, x4, y4, x1, y1);
        int det4 = determinant(x3, y3, x4, y4, x2, y2);

        return (det1 * det2 < 0) && (det3 * det4 < 0);
    }

    private int determinant(int x1, int y1, int x2, int y2, int x3, int y3) {
        return x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeterminantMethod().setVisible(true);
            }
        });
    }
}
