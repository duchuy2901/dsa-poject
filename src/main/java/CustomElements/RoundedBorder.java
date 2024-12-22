package CustomElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedBorder extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color clickColor;
    private Color textColor;

    public RoundedBorder(String label) {
        super(label);

        // Set default colors
        normalColor = new Color(102, 255, 178);
        hoverColor = new Color(0, 200, 0);
        clickColor = new Color(0, 150, 0);
        textColor = Color.WHITE;

        // Set button properties
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(true);
        setBackground(normalColor);
        setForeground(textColor);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setFont(new Font("Arial", Font.BOLD, 14));

        // Add mouse listener for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    // Paint the round background and label.
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradientPaint = new GradientPaint(0, 0, getBackground().brighter(), 0, height, getBackground().darker());
        g2d.setPaint(gradientPaint);
        g2d.fillRoundRect(0, 0, width, height, 30, 30);

        super.paintComponent(g);

        g2d.dispose();
    }

    // Paint the border of the button using a simple stroke.
    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }
}
