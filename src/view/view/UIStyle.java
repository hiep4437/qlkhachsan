package view.view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

// class nay de style lai toan bo ui cho muot, nhin sang hon
public class UIStyle {

    // font chu segoe ui chuan tren win
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);

    // bang mau hien dai kieu dashboard
    public static final Color COLOR_PRIMARY = new Color(37, 99, 235);    // xanh duong hien dai
    public static final Color COLOR_PRIMARY_HOVER = new Color(29, 78, 216);
    public static final Color COLOR_DARK_BG = new Color(15, 23, 42);      // slate dam cho header
    public static final Color COLOR_APP_BG = new Color(248, 250, 252);    // mau nen trang xam nhe
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(226, 232, 240);    // vien xam nhat

    // mau trang thai phong
    public static final Color COLOR_AVAILABLE = new Color(16, 185, 129);  // xanh ngoc
    public static final Color COLOR_AVAILABLE_BG = new Color(236, 253, 245);
    public static final Color COLOR_AVAILABLE_TEXT = new Color(6, 95, 70);

    public static final Color COLOR_OCCUPIED = new Color(244, 63, 94);   // do hong hien dai
    public static final Color COLOR_OCCUPIED_BG = new Color(255, 241, 242);
    public static final Color COLOR_OCCUPIED_TEXT = new Color(159, 18, 57);

    public static final Color COLOR_CLEANING = new Color(245, 158, 11);   // cam ho phach
    public static final Color COLOR_CLEANING_BG = new Color(254, 252, 232);
    public static final Color COLOR_CLEANING_TEXT = new Color(146, 64, 14);

    public static final Color COLOR_MAINTENANCE = new Color(100, 116, 139); // xam slate
    public static final Color COLOR_MAINTENANCE_BG = new Color(241, 245, 249);
    public static final Color COLOR_MAINTENANCE_TEXT = new Color(51, 65, 85);

    // bat look and feel cua he dieu hanh de bo may cai nut 3d xau xi thoi napoleon
    public static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // loi thi ke no, dung swing mac dinh
        }

        // set font mac dinh sang segoe ui cho dep
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("TabbedPane.font", FONT_BOLD);
    }

    // border bo tron goc nhin cho muot
    public static class RoundedBorder extends AbstractBorder {
        private Color color;
        private int radius;
        private int thickness;

        public RoundedBorder(Color color, int radius, int thickness) {
            this.color = color;
            this.radius = radius;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + thickness / 2.0, y + thickness / 2.0,
                    width - thickness, height - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2 + 2, radius / 2 + 4, radius / 2 + 2, radius / 2 + 4);
        }
    }

    // tao nut bam dep co hover, bo tron goc
    public static JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(
                            Math.min(255, bg.getRed() + 15),
                            Math.min(255, bg.getGreen() + 15),
                            Math.min(255, bg.getBlue() + 15)
                    ));
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(8, 16, 8, 16));
        return btn;
    }

    public static void setButtonIcon(JButton button, String iconFileName) {
        Icon icon = createIcon(iconFileName, 24, null);
        if (icon != null) {
            button.setIcon(icon);
            button.setIconTextGap(6);
            button.setMargin(new Insets(2, 4, 2, 4));
        }
    }

    public static Icon createTintedIcon(String iconFileName, int size, Color tint) {
        return createIcon(iconFileName, size, tint);
    }

    private static Icon createIcon(String iconFileName, int size, Color tint) {
        String resourcePath = "/img/" + iconFileName;
        java.net.URL iconResource = UIStyle.class.getResource(resourcePath);
        if (iconResource == null) {
            System.err.println("Không tìm thấy icon nút: " + resourcePath);
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(iconResource);
            if (image == null) {
                System.err.println("Không đọc được ảnh icon: " + resourcePath);
                return null;
            }
            if (tint != null) {
                BufferedImage tintedImage = new BufferedImage(
                        image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int alpha = (image.getRGB(x, y) >>> 24) & 0xFF;
                        tintedImage.setRGB(x, y, (alpha << 24) | (tint.getRGB() & 0x00FFFFFF));
                    }
                }
                image = tintedImage;
            }
            return new ScaledIcon(image, size);
        } catch (IOException e) {
            System.err.println("Không thể đọc icon " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }

    private static class ScaledIcon implements Icon {
        private final BufferedImage image;
        private final int size;

        private ScaledIcon(BufferedImage image, int size) {
            this.image = image;
            this.size = size;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.drawImage(image, x, y, size, size, null);
            graphics2D.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    // style input text nhin phang va co padding
    public static void styleTextField(JTextField tf) {
        tf.setFont(FONT_REGULAR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(COLOR_BORDER, 6, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    // style password field
    public static void stylePasswordField(JPasswordField pf) {
        pf.setFont(FONT_REGULAR);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(COLOR_BORDER, 6, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}
