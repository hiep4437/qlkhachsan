package view;

import java.awt.Image;
import java.awt.Window;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Tiện ích đặt biểu tượng (App Icon) cho các cửa sổ JFrame/JDialog.
 */
public final class AppIcon {

    private static Image iconImage;

    static {
        try {
            URL url = AppIcon.class.getResource("/img/logo.png");
            if (url != null) {
                iconImage = new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}
    }

    private AppIcon() {}

    /**
     * Áp dụng icon cho bất kỳ cửa sổ Window (JFrame, JDialog).
     * @param window Cửa sổ cần gắn icon
     */
    public static void apply(Window window) {
        if (window != null && iconImage != null) {
            window.setIconImage(iconImage);
        }
    }

    /**
     * Lấy đối tượng Image của Icon ứng dụng.
     * @return Image hoặc null nếu không tìm thấy
     */
    public static Image getImage() {
        return iconImage;
    }
}

