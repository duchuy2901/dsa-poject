package CustomElements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {
    public CustomScrollPane(Component view) {
        super(view);

        // Tùy chỉnh đường viền và màu nền của JScrollPane
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(240, 240, 240,100));

        // Loại bỏ các nút mũi tên trên thanh cuộn
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY;
                this.trackColor = Color.LIGHT_GRAY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }

            @Override
            protected void layoutVScrollbar(JScrollBar sb) {
                // Thu nhỏ thanh cuộn
                sb.setPreferredSize(new Dimension(10, sb.getPreferredSize().height));
                sb.setBackground(new Color(240,240,240,100));
                // Thêm khoảng cách bên trái
                sb.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

                // Sử dụng layout mặc định
                super.layoutVScrollbar(sb);
            }
            @Override
            protected void layoutHScrollbar(JScrollBar sb) {
                // Thu nhỏ thanh cuộn
                sb.setPreferredSize(new Dimension(10, sb.getPreferredSize().height));
                sb.setBackground(new Color(240,240,240,100));
                // Thêm khoảng cách bên trái
                sb.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

                // Sử dụng layout mặc định
                super.layoutVScrollbar(sb);
            }
        });
    }

    // Optional: Override paintComponent method to add additional customizations

    @Override
    protected void paintComponent(Graphics g) {
        // Gọi paintComponent của lớp cha để vẽ các thành phần JScrollPane
        super.paintComponent(g);

        // Tùy chỉnh thêm nếu cần
    }
}
