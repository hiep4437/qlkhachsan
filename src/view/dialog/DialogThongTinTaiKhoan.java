package view.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.NguoiDung;
import service.NguoiDungService;

public class DialogThongTinTaiKhoan extends JDialog {

    private NguoiDung nguoiDung;
    private NguoiDungService dichVuNguoiDung = new NguoiDungService();
    private Runnable khiUpdatedHamGoiLai;

    private JTextField oNhapMa;
    private JTextField oNhapTenDangNhap;
    private JTextField oNhapFullTen;
    private JTextField oNhapVaiTro;
    private JTextField oNhapTrangThai;

    private JPasswordField oNhapCuMatKhau;
    private JPasswordField oNhapMoiMatKhau;
    private JPasswordField oNhapXacNhanMatKhau;

    public DialogThongTinTaiKhoan(Window doiTuongCha, NguoiDung nguoiDung, Runnable khiUpdatedHamGoiLai) {
        super(doiTuongCha, "Thông tin tài khoản cá nhân", ModalityType.APPLICATION_MODAL);
        this.nguoiDung = nguoiDung;
        this.khiUpdatedHamGoiLai = khiUpdatedHamGoiLai;

        setSize(500, 520);
        setResizable(false);
        setLocationRelativeTo(doiTuongCha);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 1. Header Banner
        JPanel khungDauTrang = new JPanel(new BorderLayout());
        khungDauTrang.setBackground(new Color(24, 44, 97));
        khungDauTrang.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel khungAnhDaiDien = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        khungAnhDaiDien.setOpaque(false);

        JLabel nhanAnhDaiDien = new JLabel("");
        nhanAnhDaiDien.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        nhanAnhDaiDien.setForeground(Color.WHITE);

        JPanel tieuDeThongTin = new JPanel(new GridLayout(2, 1, 2, 2));
        tieuDeThongTin.setOpaque(false);

        JLabel nhanTieuDeTen = new JLabel(nguoiDung.getHoTen());
        nhanTieuDeTen.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDeTen.setForeground(Color.WHITE);

        String chuoiVaiTro = nguoiDung.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)";
        JLabel nhanTieuDeVaiTro = new JLabel("Vai trò: " + chuoiVaiTro + "  •  @" + nguoiDung.getTenDangNhap());
        nhanTieuDeVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanTieuDeVaiTro.setForeground(new Color(200, 225, 255));

        tieuDeThongTin.add(nhanTieuDeTen);
        tieuDeThongTin.add(nhanTieuDeVaiTro);

        khungAnhDaiDien.add(nhanAnhDaiDien);
        khungAnhDaiDien.add(tieuDeThongTin);
        khungDauTrang.add(khungAnhDaiDien, BorderLayout.WEST);

        add(khungDauTrang, BorderLayout.NORTH);

        // 2. Tabs: Thong tin & Doi mat khau
        JTabbedPane khungTab = new JTabbedPane();
        khungTab.setFont(new Font("Segoe UI", Font.BOLD, 13));

        khungTab.addTab(" Thông tin cá nhân", taoPanelThongTin());
        khungTab.addTab("Đổi mật khẩu", taoPanelDoiMatKhau());

        add(khungTab, BorderLayout.CENTER);

        // 3. Bottom Close Button
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        khungDuoi.setBackground(new Color(245, 247, 250));
        khungDuoi.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btDong = new JButton("Đóng");
        btDong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btDong.addActionListener(ac -> dispose());
        khungDuoi.add(btDong);

        add(khungDuoi, BorderLayout.SOUTH);
    }

    private JPanel taoPanelThongTin() {
        JPanel khung = new JPanel(new BorderLayout(10, 10));
        khung.setBackground(Color.WHITE);
        khung.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel bieuMau = new JPanel(new GridBagLayout());
        bieuMau.setOpaque(false);
        GridBagConstraints gia = new GridBagConstraints();
        gia.insets = new Insets(6, 6, 6, 6);
        gia.fill = GridBagConstraints.HORIZONTAL;

        oNhapMa = new JTextField(String.valueOf(nguoiDung.getIdNguoiDung()));
        oNhapMa.setEditable(false);
        oNhapMa.setBackground(new Color(240, 243, 246));

        oNhapTenDangNhap = new JTextField(nguoiDung.getTenDangNhap());
        oNhapTenDangNhap.setEditable(false);
        oNhapTenDangNhap.setBackground(new Color(240, 243, 246));

        oNhapVaiTro = new JTextField(nguoiDung.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)");
        oNhapVaiTro.setEditable(false);
        oNhapVaiTro.setBackground(new Color(240, 243, 246));

        oNhapTrangThai = new JTextField(nguoiDung.isTrangThai() ? "Đang hoạt động" : "Đã bị khóa");
        oNhapTrangThai.setEditable(false);
        oNhapTrangThai.setBackground(new Color(240, 243, 246));

        oNhapFullTen = new JTextField(nguoiDung.getHoTen(), 20);
        oNhapFullTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Row 0: ID
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0.3;
        bieuMau.add(new JLabel("Mã người dùng:"), gia);
        gia.gridx = 1; gia.gridy = 0; gia.weightx = 0.7;
        bieuMau.add(oNhapMa, gia);

        // Row 1: Username
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0.3;
        bieuMau.add(new JLabel("Tên đăng nhập:"), gia);
        gia.gridx = 1; gia.gridy = 1; gia.weightx = 0.7;
        bieuMau.add(oNhapTenDangNhap, gia);

        // Row 2: Full name
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0.3;
        bieuMau.add(new JLabel("Họ và tên:"), gia);
        gia.gridx = 1; gia.gridy = 2; gia.weightx = 0.7;
        bieuMau.add(oNhapFullTen, gia);

        // Row 3: Role
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0.3;
        bieuMau.add(new JLabel("Vai trò:"), gia);
        gia.gridx = 1; gia.gridy = 3; gia.weightx = 0.7;
        bieuMau.add(oNhapVaiTro, gia);

        // Row 4: Status
        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0.3;
        bieuMau.add(new JLabel("Trạng thái:"), gia);
        gia.gridx = 1; gia.gridy = 4; gia.weightx = 0.7;
        bieuMau.add(oNhapTrangThai, gia);

        khung.add(bieuMau, BorderLayout.CENTER);

        JPanel btKhung = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        btKhung.setOpaque(false);

        JButton btLuu = new JButton("Lưu họ tên mới");
        btLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btLuu.setBackground(new Color(37, 99, 235));
        btLuu.setForeground(Color.WHITE);
        btLuu.setFocusPainted(false);
        btLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btLuu.addActionListener(ac -> capNhatHoTen());
        btKhung.add(btLuu);

        khung.add(btKhung, BorderLayout.SOUTH);

        return khung;
    }

    private JPanel taoPanelDoiMatKhau() {
        JPanel khung = new JPanel(new BorderLayout(10, 10));
        khung.setBackground(Color.WHITE);
        khung.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel bieuMau = new JPanel(new GridBagLayout());
        bieuMau.setOpaque(false);
        GridBagConstraints gia = new GridBagConstraints();
        gia.insets = new Insets(8, 6, 8, 6);
        gia.fill = GridBagConstraints.HORIZONTAL;

        oNhapCuMatKhau = new JPasswordField(20);
        oNhapMoiMatKhau = new JPasswordField(20);
        oNhapXacNhanMatKhau = new JPasswordField(20);

        // Row 0: Old pass
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0.4;
        bieuMau.add(new JLabel("Mật khẩu hiện tại:"), gia);
        gia.gridx = 1; gia.gridy = 0; gia.weightx = 0.6;
        bieuMau.add(oNhapCuMatKhau, gia);

        // Row 1: New pass
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0.4;
        bieuMau.add(new JLabel("Mật khẩu mới:"), gia);
        gia.gridx = 1; gia.gridy = 1; gia.weightx = 0.6;
        bieuMau.add(oNhapMoiMatKhau, gia);

        // Row 2: Confirm pass
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0.4;
        bieuMau.add(new JLabel("Nhập lại mật khẩu mới:"), gia);
        gia.gridx = 1; gia.gridy = 2; gia.weightx = 0.6;
        bieuMau.add(oNhapXacNhanMatKhau, gia);

        khung.add(bieuMau, BorderLayout.CENTER);

        JPanel btKhung = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        btKhung.setOpaque(false);

        JButton btDoiMatKhau = new JButton("Đổi mật khẩu");
        btDoiMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btDoiMatKhau.setBackground(new Color(16, 185, 129));
        btDoiMatKhau.setForeground(Color.WHITE);
        btDoiMatKhau.setFocusPainted(false);
        btDoiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btDoiMatKhau.addActionListener(ac -> thucHienDoiMatKhau());
        btKhung.add(btDoiMatKhau);

        khung.add(btKhung, BorderLayout.SOUTH);

        return khung;
    }

    private void capNhatHoTen() {
        String tenMoi = oNhapFullTen.getText().trim();
        if (tenMoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ và tên không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ketQua = dichVuNguoiDung.capNhatHoTen(nguoiDung.getIdNguoiDung(), tenMoi);
        if (ketQua > 0) {
            nguoiDung.setHoTen(tenMoi);
            JOptionPane.showMessageDialog(this, "Cập nhật họ tên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (khiUpdatedHamGoiLai != null) {
                khiUpdatedHamGoiLai.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật họ tên. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void thucHienDoiMatKhau() {
        String matKhauCu = new String(oNhapCuMatKhau.getPassword());
        String matKhauMoi = new String(oNhapMoiMatKhau.getPassword());
        String xacNhanMatKhau = new String(oNhapXacNhanMatKhau.getPassword());

        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhanMatKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới và xác nhận mật khẩu không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (matKhauMoi.length() < 4) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 4 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ketQua = dichVuNguoiDung.doiMatKhau(nguoiDung.getIdNguoiDung(), matKhauCu, matKhauMoi);
        if (ketQua == -1) {
            JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else if (ketQua > 0) {
            nguoiDung.setMatKhau(matKhauMoi);
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            oNhapCuMatKhau.setText("");
            oNhapMoiMatKhau.setText("");
            oNhapXacNhanMatKhau.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

