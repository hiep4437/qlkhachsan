package view.view;

import dao.KetNoiCSDL;
import java.awt.*;
import javax.swing.*;

public class ViewDangNhap extends JFrame {
    public JTextField oNhapTenDangNhap;
    public JPasswordField oNhapMatKhau;
    public JButton nutDangNhap, nutThoat;
    public JButton nutCauHinhServer;
    public JLabel nhanThongBaoServer;

    public ViewDangNhap() {
        setTitle("Đăng nhập - Quản lý khách sạn");
        AppWindowIcon.apply(this);
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chinh
        JPanel khungChinh = new JPanel(new BorderLayout(10, 10));
        khungChinh.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        khungChinh.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel khungDauTrang = new JPanel();
        khungDauTrang.setLayout(new BoxLayout(khungDauTrang, BoxLayout.Y_AXIS));
        khungDauTrang.setOpaque(false);

        JLabel nhanBieuTuong = new JLabel("QUẢN LÝ KHÁCH SẠN");
        nhanBieuTuong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nhanBieuTuong.setForeground(new Color(26, 82, 118));
        nhanBieuTuong.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nhanPhu = new JLabel("Đăng nhập");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(Color.GRAY);
        nhanPhu.setAlignmentX(Component.CENTER_ALIGNMENT);

        khungDauTrang.add(nhanBieuTuong);
        khungDauTrang.add(Box.createVerticalStrut(4));
        khungDauTrang.add(nhanPhu);
        khungDauTrang.add(Box.createVerticalStrut(15));

        // Form Panel
        JPanel khungBieuMau = new JPanel(new GridBagLayout());
        khungBieuMau.setOpaque(false);
        GridBagConstraints ranhGioi = new GridBagConstraints();
        ranhGioi.fill = GridBagConstraints.HORIZONTAL;
        ranhGioi.insets = new Insets(6, 4, 6, 4);

        // Row 1: Username
        ranhGioi.gridx = 0;
        ranhGioi.gridy = 0;
        ranhGioi.weightx = 0.3;
        JLabel nhanNguoiDung = new JLabel("Tên đăng nhập:");
        nhanNguoiDung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        khungBieuMau.add(nhanNguoiDung, ranhGioi);

        ranhGioi.gridx = 1;
        ranhGioi.gridy = 0;
        ranhGioi.weightx = 0.7;
        oNhapTenDangNhap = new JTextField(15);
        oNhapTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTenDangNhap.setPreferredSize(new Dimension(200, 30));
        khungBieuMau.add(oNhapTenDangNhap, ranhGioi);

        // Row 2: Password
        ranhGioi.gridx = 0;
        ranhGioi.gridy = 1;
        ranhGioi.weightx = 0.3;
        JLabel nhanMatKhau = new JLabel("Mật khẩu:");
        nhanMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 13));
        khungBieuMau.add(nhanMatKhau, ranhGioi);

        ranhGioi.gridx = 1;
        ranhGioi.gridy = 1;
        ranhGioi.weightx = 0.7;
        oNhapMatKhau = new JPasswordField(15);
        oNhapMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapMatKhau.setPreferredSize(new Dimension(200, 30));
        khungBieuMau.add(oNhapMatKhau, ranhGioi);

        // Buttons Panel
        JPanel nutKhung = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        nutKhung.setOpaque(false);

        nutDangNhap = new JButton("Đăng nhập");
        nutDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutDangNhap.setBackground(new Color(41, 128, 185));
        nutDangNhap.setForeground(Color.WHITE);
        nutDangNhap.setFocusPainted(false);
        nutDangNhap.setPreferredSize(new Dimension(120, 34));

        nutThoat = new JButton("Thoát");
        nutThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutThoat.setPreferredSize(new Dimension(90, 34));

        nutKhung.add(nutDangNhap);
        nutKhung.add(nutThoat);

        // Bottom config panel
        JPanel khungDuoi = new JPanel(new BorderLayout());
        khungDuoi.setOpaque(false);
        khungDuoi.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        nutCauHinhServer = new JButton(" Cấu hình máy chủ");
        nutCauHinhServer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nutCauHinhServer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutCauHinhServer.setFocusPainted(false);

        nhanThongBaoServer = new JLabel(
                "CSDL: " + KetNoiCSDL.getHost() + ":" + KetNoiCSDL.getPort() + "/" + KetNoiCSDL.getDatabase());
        nhanThongBaoServer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        nhanThongBaoServer.setForeground(new Color(100, 100, 100));

        khungDuoi.add(nhanThongBaoServer, BorderLayout.WEST);
        khungDuoi.add(nutCauHinhServer, BorderLayout.EAST);

        // Center container
        JPanel khungGiua = new JPanel(new BorderLayout());
        khungGiua.setOpaque(false);
        khungGiua.add(khungBieuMau, BorderLayout.CENTER);
        khungGiua.add(nutKhung, BorderLayout.SOUTH);

        khungChinh.add(khungDauTrang, BorderLayout.NORTH);
        khungChinh.add(khungGiua, BorderLayout.CENTER);
        khungChinh.add(khungDuoi, BorderLayout.SOUTH);

        add(khungChinh);

        // Events
        nutThoat.addActionListener(suKien -> System.exit(0));
        nutCauHinhServer.addActionListener(suKien -> hienThiDialogCauHinhServer());

        // Phím Enter đăng nhập
        getRootPane().setDefaultButton(nutDangNhap);
    }

    public void capNhatThongTinServer() {
        nhanThongBaoServer.setText(
                "CSDL: " + KetNoiCSDL.getHost() + ":" + KetNoiCSDL.getPort() + "/" + KetNoiCSDL.getDatabase());
    }

    public void hienThiDialogCauHinhServer() {
        JDialog hopThoai = new JDialog(this, "Cấu hình kết nối máy chủ CSDL", true);
        hopThoai.setSize(440, 320);
        hopThoai.setLocationRelativeTo(this);
        hopThoai.setLayout(new BorderLayout(10, 10));

        JPanel khungBieuMau = new JPanel(new GridBagLayout());
        khungBieuMau.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(6, 6, 6, 6);

        JTextField oNhapMayChu = new JTextField(KetNoiCSDL.getHost(), 15);
        JTextField oNhapCong = new JTextField(KetNoiCSDL.getPort(), 15);
        JTextField oNhapCoSoDuLieu = new JTextField(KetNoiCSDL.getDatabase(), 15);
        JTextField oNhapNguoiDung = new JTextField(KetNoiCSDL.getUser(), 15);
        JPasswordField oNhapMatKhau = new JPasswordField(KetNoiCSDL.getPass(), 15);

        gia.gridx = 0;
        gia.gridy = 0;
        khungBieuMau.add(new JLabel("Máy chủ (Host):"), gia);
        gia.gridx = 1;
        gia.gridy = 0;
        khungBieuMau.add(oNhapMayChu, gia);

        gia.gridx = 0;
        gia.gridy = 1;
        khungBieuMau.add(new JLabel("Cổng (Port):"), gia);
        gia.gridx = 1;
        gia.gridy = 1;
        khungBieuMau.add(oNhapCong, gia);

        gia.gridx = 0;
        gia.gridy = 2;
        khungBieuMau.add(new JLabel("Tên CSDL (Database):"), gia);
        gia.gridx = 1;
        gia.gridy = 2;
        khungBieuMau.add(oNhapCoSoDuLieu, gia);

        gia.gridx = 0;
        gia.gridy = 3;
        khungBieuMau.add(new JLabel("Tài khoản (User):"), gia);
        gia.gridx = 1;
        gia.gridy = 3;
        khungBieuMau.add(oNhapNguoiDung, gia);

        gia.gridx = 0;
        gia.gridy = 4;
        khungBieuMau.add(new JLabel("Mật khẩu (Password):"), gia);
        gia.gridx = 1;
        gia.gridy = 4;
        khungBieuMau.add(oNhapMatKhau, gia);

        JLabel nhanTrangThai = new JLabel(" ");
        nhanTrangThai.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        nhanTrangThai.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel khungCacNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton nutTest = new JButton("Kiểm tra kết nối");
        JButton nutLuu = new JButton("Lưu cấu hình");
        JButton nutMacDinh = new JButton("Mặc định");
        JButton nutHang = new JButton("Đóng");

        khungCacNut.add(nutTest);
        khungCacNut.add(nutLuu);
        khungCacNut.add(nutMacDinh);
        khungCacNut.add(nutHang);

        nutTest.addActionListener(suKien -> {
            String chuoiMatKhau = new String(oNhapMatKhau.getPassword());
            nhanTrangThai.setText("Đang kiểm tra kết nối...");
            nhanTrangThai.setForeground(Color.BLUE);
            String loi = KetNoiCSDL.testConnection(oNhapMayChu.getText(), oNhapCong.getText(), oNhapCoSoDuLieu.getText(),
                    oNhapNguoiDung.getText(), chuoiMatKhau);
            if (loi == null) {
                nhanTrangThai.setText("Kết nối thành công tới CSDL MySQL!");
                nhanTrangThai.setForeground(new Color(39, 174, 96));
            } else {
                nhanTrangThai.setText("Kết nối thất bại: " + loi);
                nhanTrangThai.setForeground(new Color(192, 57, 43));
            }
        });

        nutLuu.addActionListener(suKien -> {
            String chuoiMatKhau = new String(oNhapMatKhau.getPassword());
            boolean thanhCong = KetNoiCSDL.saveConfig(oNhapMayChu.getText(), oNhapCong.getText(), oNhapCoSoDuLieu.getText(),
                    oNhapNguoiDung.getText(), chuoiMatKhau);
            if (thanhCong) {
                capNhatThongTinServer();
                JOptionPane.showMessageDialog(hopThoai, "Đã lưu cấu hình máy chủ thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                hopThoai.dispose();
            } else {
                JOptionPane.showMessageDialog(hopThoai, "Không thể lưu cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        nutMacDinh.addActionListener(suKien -> {
            oNhapMayChu.setText("localhost");
            oNhapCong.setText("3306");
            oNhapCoSoDuLieu.setText("hotel_management");
            oNhapNguoiDung.setText("root");
            oNhapMatKhau.setText("");
            nhanTrangThai.setText("Đã khôi phục giá trị mặc định. Bấm 'Lưu cấu hình' để áp dụng.");
            nhanTrangThai.setForeground(Color.DARK_GRAY);
        });

        nutHang.addActionListener(suKien -> hopThoai.dispose());

        JPanel khungGiua = new JPanel(new BorderLayout());
        khungGiua.add(khungBieuMau, BorderLayout.CENTER);
        khungGiua.add(nhanTrangThai, BorderLayout.SOUTH);

        hopThoai.add(khungGiua, BorderLayout.CENTER);
        hopThoai.add(khungCacNut, BorderLayout.SOUTH);
        hopThoai.setVisible(true);
    }

    public String getTenDangNhap() {
        return oNhapTenDangNhap.getText().trim();
    }

    public String getMatKhau() {
        return new String(oNhapMatKhau.getPassword());
    }

    public JButton getBtnDangNhap() {
        return nutDangNhap;
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> new ViewDangNhap().setVisible(true));
    }
}
