/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pizza.servings.calculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author iayfn
 */
public class PizzaServingsCalculator extends JFrame {
    private JLabel titleLabel;
    
    private JPanel sizePanel;
    private JTextField sizeTextField;
    
    private JButton calculateButton;
    
    private JLabel resultLabel;

    public PizzaServingsCalculator() {
        setTitle("Pizza Servings Calculator");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        titleLabel = new JLabel("Pizza Servings Calculator", SwingConstants.CENTER);
        titleLabel.setForeground(Color.RED);
        add(titleLabel);

        sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Enter the size of the pizza in inches: ");
        sizePanel.add(sizeLabel);
        sizeTextField = new JTextField(4);
        sizePanel.add(sizeTextField);
        add(sizePanel);
        
        calculateButton = new JButton("Calculate Servings");
        calculateButton.addActionListener(new ActionListener());
        add(calculateButton);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        add(resultLabel);

        setVisible(true);
    }

    private class calculateServings implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                double size = Double.parseDouble(sizeTextField.getText());
                double servings = Math.pow(size / 8, 2);
                resultLabel.setText(String.format("A %d inch pizza will serve %.2f people", (int)size, servings));
            } catch (NumberFormatException e) {
                resultLabel.setText("Please enter a valid number.");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PizzaServingsCalculator gui = new PizzaServingsCalculator();
    }
    
}
