package view;

import java.io.File;
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
            return;
        }

        File logoFile = new File("img", "loogo.png");
        if (logoFile.isFile()) {
            frame.setIconImage(new ImageIcon(logoFile.getPath()).getImage());
        } else {
            System.err.println("Không tìm thấy logo cửa sổ: /img/loogo.png hoặc " + logoFile.getPath());
        }
    }
}
