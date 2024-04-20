package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class bruteforce extends JFrame {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private int[] path;
    private int[][] graph;
    private int numVertices;
    private int currentVertex;
    private boolean[] visited;

    public bruteforce() {
        takeUserInput();
        this.path = new int[numVertices];
        this.visited = new boolean[numVertices];

        initializeUI();
        reset();
    }

    private void takeUserInput() {
        String input = JOptionPane.showInputDialog("Enter the number of vertices:");
        try {
            numVertices = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            takeUserInput();
        }

        graph = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j) {
                    String value = JOptionPane.showInputDialog("Is there an edge between Vertex " + i + " and Vertex " + j + "? (0 for no, 1 for yes)");
                    try {
                        graph[i][j] = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter 0 or 1.");
                        j--;  // Retry the same index
                    }
                }
            }
        }
    }

    private void initializeUI() {
        setTitle("Brute Force Animation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBruteForce();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(new GraphPanel(), BorderLayout.CENTER);
    }

    private void reset() {
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
            path[i] = -1;
        }
        currentVertex = 0;
    }

    private void startBruteForce() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentVertex < numVertices) {
                    visited[currentVertex] = true;
                    path[currentVertex] = currentVertex;
                    currentVertex++;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    private class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int radius = 20;
            int xOffset = 50;
            int yOffset = 50;

            for (int i = 0; i < numVertices; i++) {
                int x = xOffset + i * 100;
                int y = yOffset;
                g.setColor(visited[i] ? Color.RED : Color.BLACK);
                g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
                g.drawString(Integer.toString(i), x, y);
            }

            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (graph[i][j] == 1) {
                        int startX = xOffset + i * 100 + radius;
                        int startY = yOffset + radius;
                        int endX = xOffset + j * 100 - radius;
                        int endY = yOffset + radius;
                        g.setColor(Color.BLUE);
                        g.drawLine(startX, startY, endX, endY);
                    }
                }
            }

            // Draw the path
            for (int i = 0; i < numVertices; i++) {
                if (path[i] != -1 && i < numVertices - 1) {
                    int startX = xOffset + path[i] * 100 + radius;
                    int startY = yOffset + radius;
                    int endX = xOffset + path[i + 1] * 100 - radius;
                    int endY = yOffset + radius;
                    g.setColor(Color.GREEN);
                    g.drawLine(startX, startY, endX, endY);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            bruteforce animation = new bruteforce();
            animation.setVisible(true);
        });
    }
}
