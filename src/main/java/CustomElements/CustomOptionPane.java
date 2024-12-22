package CustomElements;


import javax.swing.*;
import java.awt.*;

public class CustomOptionPane {

    public static void main(String[] args) {
        // Set custom look and feel for JOptionPane
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Customize JOptionPane
        UIManager.put("OptionPane.background", new Color(240, 240, 240)); // Background color
        UIManager.put("Panel.background", new Color(240, 240, 240)); // Panel background color
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 16)); // Message font
        UIManager.put("Button.background", new Color(60, 179, 113)); // Button background color
        UIManager.put("Button.foreground", Color.WHITE); // Button text color
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14)); // Button font
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 14)); // Button font for option pane

        // Show custom JOptionPane
        JOptionPane.showMessageDialog(null, "This is a custom message dialog.", "Custom Dialog", JOptionPane.INFORMATION_MESSAGE);
    }
}
