package view;

import java.io.File;
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
            return;
        }

        File tepBieuTuong = new File("img", "loogo.png");
        if (tepBieuTuong.isFile()) {
            cuaSo.setIconImage(new ImageIcon(tepBieuTuong.getPath()).getImage());
        } else {
            System.err.println("Không tìm thấy logo cửa sổ: /img/loogo.png hoặc " + tepBieuTuong.getPath());
        }
    }
}
