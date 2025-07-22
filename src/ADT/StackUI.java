package ADT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StackUI extends JFrame {
    private Stack stack;
    private JTextArea displayArea;
    private JTextField inputField;
    
    public StackUI() {
        stack = new Stack();
        setTitle("Simple Stack UI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        inputField = new JTextField(10);
        JButton pushButton = new JButton("Push");
        JButton popButton = new JButton("Pop");
        JButton peekButton = new JButton("Peek");
        
        topPanel.add(new JLabel("Value:"));
        topPanel.add(inputField);
        topPanel.add(pushButton);
        topPanel.add(popButton);
        topPanel.add(peekButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        pushButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(inputField.getText());
                stack.push(value);
                updateDisplay();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer.");
            }
        });
        
        popButton.addActionListener(e -> {
            try {
                int value = stack.pop();
                JOptionPane.showMessageDialog(this, "Popped: " + value);
                updateDisplay();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        
        peekButton.addActionListener(e -> {
            try {
                int value = stack.peek();
                JOptionPane.showMessageDialog(this, "Top: " + value);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        
        updateDisplay();
    }
    
    private void updateDisplay() {
        displayArea.setText("Stack: " + stack.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StackUI().setVisible(true);
        });
    }
} 