
package view.view;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public final class AppWindowIcon {

    private AppWindowIcon() {
    }

    public static void apply(JFrame cuaSo) {
        URL taiNguyenBieuTuong = AppWindowIcon.class.getResource("/img/loogo.png");

        if (taiNguyenBieuTuong != null) {
            cuaSo.setIconImage(new ImageIcon(taiNguyenBieuTuong).getImage());
        } else {
            System.err.println("Không tìm thấy logo: /img/loogo.png");
        }
    }
}
