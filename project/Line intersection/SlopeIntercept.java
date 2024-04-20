package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

public class SlopeIntercept extends JFrame implements ActionListener {
    int i=0;
    ArrayList<Double> intersection = new ArrayList<>();
    JPanel main = new JPanel(new BorderLayout());
    JPanel center = new JPanel(new GridLayout(0,1));

    JPanel bottom = new JPanel(new FlowLayout());
    Label label = new Label("Enter Lines:");
    TextField NoOfLine = new TextField();
    Label label1 = new Label("Enter Start X:");
    Label label2 = new Label("Enter Start Y:");
    Label label3 = new Label("Enter End X:");
    Label label4 = new Label("Enter End Y:");

    TextField StartX = new TextField();
    TextField StartY = new TextField();
    TextField EndX = new TextField();
    TextField EndY = new TextField();
    Button submit = new Button("submit");
    Button Next = new Button("next");

    Button reset = new Button("reset");
    double[][] point;
    SlopeIntercept(){
        submit.addActionListener(this);
        Next.addActionListener(this);
        reset.addActionListener(this);
        center.setPreferredSize(new Dimension(650,400));
        center.setBackground(Color.blue);
        bottom.setPreferredSize(new Dimension(650,100));
        bottom.add(label);
        bottom.add(NoOfLine);
        bottom.add(submit);

        main.add(center,BorderLayout.CENTER);
        main.add(bottom,BorderLayout.SOUTH);

        setTitle("Slope intercept form");
        setSize(700,500);
        add(main);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == submit) {
            int line = Integer.parseInt(NoOfLine.getText());
            point = new double[line][4];

            bottom.removeAll();
            bottom.add(label1);
            bottom.add(StartX);
            bottom.add(label2);
            bottom.add(StartY);
            bottom.add(label3);
            bottom.add(EndX);
            bottom.add(label4);
            bottom.add(EndY);
            bottom.add(Next);
            bottom.revalidate();
        }
        if(e.getSource() == Next){

            point[i][0] = Integer.parseInt(StartX.getText());
            point[i][1] = Integer.parseInt(StartY.getText());
            point[i][2] = Integer.parseInt(EndX.getText());
            point[i][3] = Integer.parseInt(EndY.getText());
            i++;
            StartX.setText("");
            StartY.setText("");
            EndX.setText("");
            EndY.setText("");
            if(i == point.length){
                for (double[] doubles : point) {
                    System.out.println(Arrays.toString(doubles));// display points on console
                }

                String result = searchAllIntersections();
                LinePanel line = new LinePanel(point,intersection);
                center.add(line);
                bottom.removeAll();
                if(result == null){
                    result="No Line intersection found";
                }
                label.setText("Result: "+result);
                bottom.add(label);
                bottom.add(reset);
                bottom.revalidate();
            }
        }
        if(e.getSource() == reset){
            dispose();
            new Frame();
        }
    }
    public String searchAllIntersections(){
        String result= "";
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        for (int j = 0; j < point.length; j++) {
            for (int k = j+1; k < point.length; k++) {
                double[] output= findIntersection(new double[]{point[j][0],point[j][1],point[j][2],point[j][3]},new double[]{point[k][0],point[k][1],point[k][2],point[k][3]});
                if(output != null){
                    if (output[0] == Double.POSITIVE_INFINITY) {
                        result = "Line " + j + " is overlapping with Line " + k + "\n"+result;
                    }else {
                        result = "Line " + j + " intersects at (" + decimalFormat.format(output[0]) + "," + decimalFormat.format(output[1]) + ") with Line " + k + " " + result;
                        intersection.add(output[0]);
                        intersection.add(output[1]);
                    }
                }
            }
        }
        return result.toString();
    }
    public double[] findIntersection(double[] line1, double[] line2) {
        double x1 = line1[0];
        double y1 = line1[1];
        double x2 = line1[2];
        double y2 = line1[3];

        double x3 = line2[0];
        double y3 = line2[1];
        double x4 = line2[2];
        double y4 = line2[3];
        System.out.println(Arrays.toString(line1)+" "+Arrays.toString(line2));
        // Calculate slopes
        double m1 = (y2 - y1) / (x2 - x1);
        double m2 = (y4 - y3) / (x4 - x3);

        double b1 = y1 - (m1*x1);
        double b2 = y3 - (m2*x3);
        System.out.println("m1: "+m1);
        System.out.println("m2: "+m2);
        System.out.println("b1: "+b1);
        System.out.println("b2: "+b2);
        // Check if the lines are overlapping (coincident)
        if (m1 == m2 && b1 == b2) {
            return new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}; // Overlapping lines
        }

        // Check if the lines are parallel
        if (m1 == m2) {
            return null; // Lines are parallel, no intersection
        }

        // Calculate intersection  by slope intercept
        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        if(round(abs(x)) <= min(x2,x4) && round(abs(x)) >= min(x1,x3)) {
            System.out.println("Info: x=" + x + ", y=" + y);
            return new double[]{x, y};
        }
        return null;

    }



    public static void main(String[] args) {
        new Frame();
    }
}

class LinePanel extends JPanel {
    double[][] point;
    ArrayList<Double> intersection = new ArrayList<>();
    LinePanel(double[][] points, ArrayList<Double> intersection){
        this.point = new double[points.length][4];
        this.intersection.addAll(intersection);
        for (int i = 0; i < point.length; i++) {
            point[i][0] = points[i][0];
            point[i][1] = points[i][1];
            point[i][2] = points[i][2];
            point[i][3] = points[i][3];
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < point.length; i++) {
            g.drawLine((int) point[i][0], (int) point[i][1], (int) point[i][2], (int) point[i][3]);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED); // Set the color for the intersection points

        for (int i = 0; i < intersection.size(); i += 2) {
            double x = intersection.get(i);
            double y = intersection.get(i + 1);

            // Draw a small circle at the intersection point
            Ellipse2D.Double circle = new Ellipse2D.Double(x - 5, y - 5, 10, 10);
            g2d.fill(circle);
        }
    }
}
