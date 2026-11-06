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
    public static final Font PHONGCHU_TIEUDE_CHINH = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font PHONGCHU_TIEUDE_DAU = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font PHONGCHU_IN_DAM = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font PHONGCHU_THUONG = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font PHONGCHU_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font PHONGCHU_SMALL_INDAM = new Font("Segoe UI", Font.BOLD, 11);

    // bang mau hien dai kieu dashboard
    public static final Color MAU_CHINH = new Color(37, 99, 235);    // xanh duong hien dai
    public static final Color MAU_CHINH_HOVER = new Color(29, 78, 216);
    public static final Color MAU_DARK_MAUNEN = new Color(15, 23, 42);      // slate dam cho header
    public static final Color MAU_APP_MAUNEN = new Color(248, 250, 252);    // mau nen trang xam nhe
    public static final Color MAU_THE_MAUNEN = Color.WHITE;
    public static final Color MAU_VIEN = new Color(226, 232, 240);    // vien xam nhat

    // mau trang thai phong
    public static final Color MAU_TRONG = new Color(16, 185, 129);  // xanh ngoc
    public static final Color MAU_TRONG_MAUNEN = new Color(236, 253, 245);
    public static final Color MAU_TRONG_VANBAN = new Color(6, 95, 70);

    public static final Color MAU_OCCUPIED = new Color(244, 63, 94);   // do hong hien dai
    public static final Color MAU_OCCUPIED_MAUNEN = new Color(255, 241, 242);
    public static final Color MAU_OCCUPIED_VANBAN = new Color(159, 18, 57);

    public static final Color MAU_CLEANING = new Color(245, 158, 11);   // cam ho phach
    public static final Color MAU_CLEANING_MAUNEN = new Color(254, 252, 232);
    public static final Color MAU_CLEANING_VANBAN = new Color(146, 64, 14);

    public static final Color MAU_MAINTENANCE = new Color(100, 116, 139); // xam slate
    public static final Color MAU_MAINTENANCE_MAUNEN = new Color(241, 245, 249);
    public static final Color MAU_MAINTENANCE_VANBAN = new Color(51, 65, 85);

    // bat look and feel cua he dieu hanh de bo may cai nut 3d xau xi thoi napoleon
    public static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception suKien) {
            // loi thi ke no, dung swing mac dinh
        }

        // set font mac dinh sang segoe ui cho dep
        Font phongChuMacDinh = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", phongChuMacDinh);
        UIManager.put("Button.font", phongChuMacDinh);
        UIManager.put("TextField.font", phongChuMacDinh);
        UIManager.put("PasswordField.font", phongChuMacDinh);
        UIManager.put("ComboBox.font", phongChuMacDinh);
        UIManager.put("Table.font", phongChuMacDinh);
        UIManager.put("TableHeader.font", PHONGCHU_IN_DAM);
        UIManager.put("TabbedPane.font", PHONGCHU_IN_DAM);
    }

    // border bo tron goc nhin cho muot
    public static class RoundedBorder extends AbstractBorder {
        private Color mau;
        private int banKinh;
        private int doDay;

        public RoundedBorder(Color mau, int banKinh, int doDay) {
            this.mau = mau;
            this.banKinh = banKinh;
            this.doDay = doDay;
        }

        @Override
        public void paintBorder(Component thanhPhan, Graphics gia, int toaDoX, int toaDoY, int chieuRong, int chieuCao) {
            Graphics2D doHoa = (Graphics2D) gia.create();
            doHoa.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            doHoa.setColor(mau);
            doHoa.setStroke(new BasicStroke(doDay));
            doHoa.draw(new RoundRectangle2D.Double(toaDoX + doDay / 2.0, toaDoY + doDay / 2.0,
                    chieuRong - doDay, chieuCao - doDay, banKinh, banKinh));
            doHoa.dispose();
        }

        @Override
        public Insets getBorderInsets(Component thanhPhan) {
            return new Insets(banKinh / 2 + 2, banKinh / 2 + 4, banKinh / 2 + 2, banKinh / 2 + 4);
        }
    }

    // tao nut bam dep co hover, bo tron goc
    public static JButton createButton(String vanBan, Color mauNen, Color mauChu) {
        JButton nut = new JButton(vanBan) {
            @Override
            protected void paintComponent(Graphics gia) {
                Graphics2D doHoa = (Graphics2D) gia.create();
                doHoa.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    doHoa.setColor(mauNen.darker());
                } else if (getModel().isRollover()) {
                    doHoa.setColor(new Color(
                            Math.min(255, mauNen.getRed() + 15),
                            Math.min(255, mauNen.getGreen() + 15),
                            Math.min(255, mauNen.getBlue() + 15)
                    ));
                } else {
                    doHoa.setColor(mauNen);
                }
                doHoa.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                doHoa.dispose();
                super.paintComponent(gia);
            }
        };

        nut.setFont(PHONGCHU_IN_DAM);
        nut.setForeground(mauChu);
        nut.setContentAreaFilled(false);
        nut.setBorderPainted(false);
        nut.setFocusPainted(false);
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nut.setMargin(new Insets(8, 16, 8, 16));
        return nut;
    }

    public static void setButtonIcon(JButton nut, String tenTepBieuTuong) {
        Icon bieuTuong = createIcon(tenTepBieuTuong, 24, null);
        if (bieuTuong != null) {
            nut.setIcon(bieuTuong);
            nut.setIconTextGap(6);
            nut.setMargin(new Insets(2, 4, 2, 4));
        }
    }

    public static Icon createTintedIcon(String tenTepBieuTuong, int kichThuoc, Color mauSac) {
        return createIcon(tenTepBieuTuong, kichThuoc, mauSac);
    }

    private static Icon createIcon(String tenTepBieuTuong, int kichThuoc, Color mauSac) {
        String duongDanTaiNguyen = "/img/" + tenTepBieuTuong;
        java.net.URL taiNguyenBieuTuong = UIStyle.class.getResource(duongDanTaiNguyen);
        if (taiNguyenBieuTuong == null) {
            System.err.println("Không tìm thấy icon nút: " + duongDanTaiNguyen);
            return null;
        }

        try {
            BufferedImage hinhAnh = ImageIO.read(taiNguyenBieuTuong);
            if (hinhAnh == null) {
                System.err.println("Không đọc được ảnh icon: " + duongDanTaiNguyen);
                return null;
            }
            if (mauSac != null) {
                BufferedImage tintedHinhAnh = new BufferedImage(
                        hinhAnh.getWidth(), hinhAnh.getHeight(), BufferedImage.TYPE_INT_ARGB);
                for (int toaDoY = 0; toaDoY < hinhAnh.getHeight(); toaDoY++) {
                    for (int toaDoX = 0; toaDoX < hinhAnh.getWidth(); toaDoX++) {
                        int heSoAlpha = (hinhAnh.getRGB(toaDoX, toaDoY) >>> 24) & 0xFF;
                        tintedHinhAnh.setRGB(toaDoX, toaDoY, (heSoAlpha << 24) | (mauSac.getRGB() & 0x00FFFFFF));
                    }
                }
                hinhAnh = tintedHinhAnh;
            }
            return new ScaledIcon(hinhAnh, kichThuoc);
        } catch (IOException suKien) {
            System.err.println("Không thể đọc icon " + duongDanTaiNguyen + ": " + suKien.getMessage());
            return null;
        }
    }

    private static class ScaledIcon implements Icon {
        private final BufferedImage hinhAnh;
        private final int kichThuoc;

        private ScaledIcon(BufferedImage hinhAnh, int kichThuoc) {
            this.hinhAnh = hinhAnh;
            this.kichThuoc = kichThuoc;
        }

        @Override
        public void paintIcon(Component thanhPhan, Graphics doHoa, int toaDoX, int toaDoY) {
            Graphics2D doHoaHaiChieu = (Graphics2D) doHoa.create();
            doHoaHaiChieu.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            doHoaHaiChieu.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            doHoaHaiChieu.drawImage(hinhAnh, toaDoX, toaDoY, kichThuoc, kichThuoc, null);
            doHoaHaiChieu.dispose();
        }

        @Override
        public int getIconWidth() {
            return kichThuoc;
        }

        @Override
        public int getIconHeight() {
            return kichThuoc;
        }
    }

    // style input text nhin phang va co padding
    public static void styleTextField(JTextField truongNhap) {
        truongNhap.setFont(PHONGCHU_THUONG);
        truongNhap.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(MAU_VIEN, 6, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    // style password field
    public static void stylePasswordField(JPasswordField truongMatKhau) {
        truongMatKhau.setFont(PHONGCHU_THUONG);
        truongMatKhau.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(MAU_VIEN, 6, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}
