
package view.view;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public final class AppWindowIcon {

    private AppWindowIcon() {
    }

    public static void apply(JFrame frame) {
        URL logoResource = AppWindowIcon.class.getResource("/img/loogo.png");

        if (logoResource != null) {
            frame.setIconImage(new ImageIcon(logoResource).getImage());
        } else {
            System.err.println("Không tìm thấy logo: /img/loogo.png");
        }
    }
}
