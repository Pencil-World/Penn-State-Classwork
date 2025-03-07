/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame {
    // create panels for application north
    private final JPanel topPanel = new JPanel(); // entire panel
    private final JPanel firstLine = new JPanel(); // first line widgets
    private final JPanel secondLine = new JPanel(); // second line widgets

    // create the widgets for firstLine Panel.
    private final JLabel shapeLabel = new JLabel("Shape: ");
    private final JComboBox shapeComboBox = new JComboBox<>(new String[]{"Line", "Oval" , "Rectangle"});

    private final JButton color1Button = new JButton("1st Color...");
    private final JButton color2Button = new JButton("2nd Color...");

    private final JButton undoButton = new JButton("Undo");
    private final JButton clearButton = new JButton("Clear");

    //create the widgets for secondLine Panel.
    private final JLabel optionsLabel = new JLabel("Options: ");

    private final JCheckBox filledCheckBox = new JCheckBox("Filled");
    private final JCheckBox useGradientCheckBox = new JCheckBox("Use Gradient");
    private final JCheckBox dashedCheckBox = new JCheckBox("Dashed");

    private final JLabel lineWidthLabel = new JLabel("Line Width:");
    private final JSpinner lineWidthField = new JSpinner(new SpinnerNumberModel(10, 3, 100, 1));

    private final JLabel dashLengthLabel = new JLabel("Dash Length:");
    private final JSpinner dashLengthField = new JSpinner(new SpinnerNumberModel(15, 3, 100, 1));

    // variables for drawPanel.
    private DrawPanel drawPanel = new DrawPanel();
    private final ArrayList<MyShapes> allShapes = new ArrayList<MyShapes>();
    private Color c1 = Color.BLACK;
    private Color c2 = Color.BLACK;

    // add status label
    private final JLabel statusLabel = new JLabel();
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame() {
        setLayout(new BorderLayout());
        topPanel.setLayout(new BorderLayout());

        // firstLine widgets
        firstLine.add(shapeLabel);
        firstLine.add(shapeComboBox);
        firstLine.add(color1Button);
        firstLine.add(color2Button);
        firstLine.add(undoButton);
        firstLine.add(clearButton);

        // secondLine widgets
        secondLine.add(optionsLabel);
        secondLine.add(filledCheckBox);
        secondLine.add(useGradientCheckBox);
        secondLine.add(dashedCheckBox);
        secondLine.add(lineWidthLabel);
        secondLine.add(lineWidthField);
        secondLine.add(dashLengthLabel); 
        secondLine.add(dashLengthField); 

        // add to top panel
        topPanel.add(firstLine, BorderLayout.NORTH);
        topPanel.add(secondLine, BorderLayout.SOUTH);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        //add listeners and event handlers
        
        color1Button.addActionListener(listener -> {
            Color temp = c1;
            c1 = JColorChooser.showDialog(null, "Select Color 1", c1);
            if (c1 == null) {
                c1 = temp;
            }
        });
        
        color2Button.addActionListener(listener -> {
            Color temp = c2;
            c2 = JColorChooser.showDialog(null, "Select Color 2", c2);
            if (c2 == null) {
                c2 = temp;
            }
        });
        
        undoButton.addActionListener(listener -> {
            if(!allShapes.isEmpty()) {
                allShapes.remove(allShapes.size() - 1);
                drawPanel.repaint();
            }
        });
        
        clearButton.addActionListener(listener -> {
            allShapes.clear();
            drawPanel.repaint();
        });
    }

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel {
        private Point startPoint;
        private ArrayList<MyShapes> tempShapes = new ArrayList<MyShapes>();

        public DrawPanel() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseHandler());
        }

        private MyShapes buildShape(Point start, Point end) {
            BasicStroke strk = dashedCheckBox.isSelected() 
                ? new BasicStroke(Integer.parseInt(lineWidthField.getValue().toString()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{Float.parseFloat(dashLengthField.getValue().toString())}, 0) 
                : new BasicStroke(Integer.parseInt(lineWidthField.getValue().toString()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); 
            Paint paint = useGradientCheckBox.isSelected() 
                ? new GradientPaint(0, 0, c1, 50, 50, c2, true) 
                : new GradientPaint(0, 0, c1, 50, 50, c1, true);

            switch(shapeComboBox.getSelectedItem().toString()) {
                case "Line":
                    return new MyLine(start, end, paint, strk);
                case "Oval":
                    return new MyOval(start, end, paint, strk, filledCheckBox.isSelected());
                case "Rectangle":
                    return new MyRectangle(start, end, paint, strk, filledCheckBox.isSelected());
                default:
                    return null;
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (MyShapes shape : allShapes)
                shape.draw(g2d);
            for (MyShapes shape : tempShapes)
                shape.draw(g2d);

            tempShapes.clear();
        }

        private class MouseHandler extends MouseAdapter implements MouseMotionListener {
            @Override
            public void mousePressed(MouseEvent event) {
                startPoint = event.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                MyShapes currShape = buildShape(startPoint, event.getPoint());
                if (currShape != null) {
                    allShapes.add(currShape);
                    drawPanel.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                statusLabel.setText("(" + event.getX() + "," + event.getY() + ")");
                MyShapes currShape = buildShape(startPoint, event.getPoint());
                if (currShape != null) {
                    tempShapes.add(currShape);
                    drawPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                statusLabel.setText("(" + event.getX() + "," + event.getY() + ")");
            }
        }
    }
}
