package view.view;

import dao.DBConecter;
import java.awt.*;
import javax.swing.*;
import view.IConn;

public class ViewDangNhap extends JFrame {
    public JTextField oNhapTenDangNhap;
    public JPasswordField oNhapMatKhau;
    public JButton btDangNhap, btThoat;
    public JButton btCauHinhServer;
    public JLabel nhanThongBaoServer;

    public ViewDangNhap() {
        setTitle("Đăng nhập - Quản lý khách sạn");
        IConn.apply(this);
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
        JPanel btKhung = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btKhung.setOpaque(false);

        btDangNhap = new JButton("Đăng nhập");
        btDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btDangNhap.setBackground(new Color(41, 128, 185));
        btDangNhap.setForeground(Color.WHITE);
        btDangNhap.setFocusPainted(false);
        btDangNhap.setPreferredSize(new Dimension(120, 34));

        btThoat = new JButton("Thoát");
        btThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btThoat.setPreferredSize(new Dimension(90, 34));

        btKhung.add(btDangNhap);
        btKhung.add(btThoat);

        // Bottom config panel
        JPanel khungDuoi = new JPanel(new BorderLayout());
        khungDuoi.setOpaque(false);
        khungDuoi.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btCauHinhServer = new JButton(" Cấu hình máy chủ");
        btCauHinhServer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btCauHinhServer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btCauHinhServer.setFocusPainted(false);

        nhanThongBaoServer = new JLabel(
                "CSDL: " + DBConecter.getHost() + ":" + DBConecter.getPort() + "/" + DBConecter.getDatabase());
        nhanThongBaoServer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        nhanThongBaoServer.setForeground(new Color(100, 100, 100));

        khungDuoi.add(nhanThongBaoServer, BorderLayout.WEST);
        khungDuoi.add(btCauHinhServer, BorderLayout.EAST);

        // Center container
        JPanel khungGiua = new JPanel(new BorderLayout());
        khungGiua.setOpaque(false);
        khungGiua.add(khungBieuMau, BorderLayout.CENTER);
        khungGiua.add(btKhung, BorderLayout.SOUTH);

        khungChinh.add(khungDauTrang, BorderLayout.NORTH);
        khungChinh.add(khungGiua, BorderLayout.CENTER);
        khungChinh.add(khungDuoi, BorderLayout.SOUTH);

        add(khungChinh);

        // Events
        btThoat.addActionListener(ac -> System.exit(0));
        btCauHinhServer.addActionListener(ac -> hienThiDialogCauHinhServer());

        // Phím Enter đăng nhập
        getRootPane().setDefaultButton(btDangNhap);
    }

    public void capNhatThongTinServer() {
        nhanThongBaoServer.setText("CSDL: " + DBConecter.getHost() + ":" + DBConecter.getPort() + "/" + DBConecter.getDatabase());
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

        JTextField oNhapMayChu = new JTextField(DBConecter.getHost(), 15);
        JTextField oNhapCong = new JTextField(DBConecter.getPort(), 15);
        JTextField oNhapCoSoDuLieu = new JTextField(DBConecter.getDatabase(), 15);
        JTextField oNhapNguoiDung = new JTextField(DBConecter.getUser(), 15);
        JPasswordField oNhapMatKhau = new JPasswordField(DBConecter.getPass(), 15);

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
        JButton btTest = new JButton("Kiểm tra kết nối");
        JButton btLuu = new JButton("Lưu cấu hình");
        JButton btMacDinh = new JButton("Mặc định");
        JButton btHang = new JButton("Đóng");

        khungCacNut.add(btTest);
        khungCacNut.add(btLuu);
        khungCacNut.add(btMacDinh);
        khungCacNut.add(btHang);

        btTest.addActionListener(ac -> {
            String chuoiMatKhau = new String(oNhapMatKhau.getPassword());
            nhanTrangThai.setText("Đang kiểm tra kết nối...");
            nhanTrangThai.setForeground(Color.BLUE);
            String loi = DBConecter.testConnection(oNhapMayChu.getText(), oNhapCong.getText(), oNhapCoSoDuLieu.getText(),
                    oNhapNguoiDung.getText(), chuoiMatKhau);
            if (loi == null) {
                nhanTrangThai.setText("Kết nối thành công tới CSDL MySQL!");
                nhanTrangThai.setForeground(new Color(39, 174, 96));
            } else {
                nhanTrangThai.setText("Kết nối thất bại: " + loi);
                nhanTrangThai.setForeground(new Color(192, 57, 43));
            }
        });

        btLuu.addActionListener(ac -> {
            String chuoiMatKhau = new String(oNhapMatKhau.getPassword());
            boolean thanhCong = DBConecter.saveConfig(oNhapMayChu.getText(), oNhapCong.getText(), oNhapCoSoDuLieu.getText(),
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

        btMacDinh.addActionListener(ac -> {
            oNhapMayChu.setText("localhost");
            oNhapCong.setText("3306");
            oNhapCoSoDuLieu.setText("hotel_management");
            oNhapNguoiDung.setText("root");
            oNhapMatKhau.setText("");
            nhanTrangThai.setText("Đã khôi phục giá trị mặc định. Bấm 'Lưu cấu hình' để áp dụng.");
            nhanTrangThai.setForeground(Color.DARK_GRAY);
        });

        btHang.addActionListener(ac -> hopThoai.dispose());

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
        return btDangNhap;
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> new ViewDangNhap().setVisible(true));
    }
}
