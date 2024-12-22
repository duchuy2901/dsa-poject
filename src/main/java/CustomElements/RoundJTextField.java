package CustomElements;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class RoundJTextField extends JTextField {
    private static final long serialVersionUID = 1L;
    private String placeholder;

    public RoundJTextField(int size, String placeholder) {
        super(size);
        this.placeholder = placeholder;
        setForeground(Color.GRAY); // Màu của placeholder
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getForeground(), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        )); // Border cho JTextField
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint(); // Vẽ lại JTextField khi nhận focus để ẩn placeholder
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint(); // Vẽ lại JTextField khi mất focus để hiển thị lại placeholder nếu cần
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && !hasFocus()) {
            Font prevFont = g.getFont();
            Color prevColor = g.getColor();

            g.setColor(getForeground());
            g.setFont(getFont().deriveFont(Font.ITALIC));

            // Tính toán vị trí để căn giữa chữ
            int stringWidth = g.getFontMetrics().stringWidth(placeholder);
            int stringHeight = g.getFontMetrics().getHeight();
            int x = (getWidth() - stringWidth) / 2;
            int y = ((getHeight() - stringHeight) / 2) + g.getFontMetrics().getAscent();

            g.drawString(placeholder, x, y);

            g.setColor(prevColor);
            g.setFont(prevFont);
        }
    }
}
