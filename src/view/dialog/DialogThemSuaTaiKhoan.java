package view.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.NguoiDung;

public class DialogThemSuaTaiKhoan extends JDialog {

    private JTextField oNhapTenDangNhap;
    private JPasswordField oNhapMatKhau;
    private JPasswordField oNhapXacNhanMatKhau;
    private JTextField oNhapHoTen;
    private JComboBox<String> hopChonVaiTro;
    private JComboBox<String> hopChonTrangThai;
    private JLabel nhanGhiChuMatKhau;

    private JButton nutLuu;
    private JButton nutHuy;

    private boolean daLuu = false;
    private NguoiDung nguoiDung;
    private final boolean dangSua;

    public DialogThemSuaTaiKhoan(Window doiTuongCha, NguoiDung nguoiDungCanSua) {
        super(doiTuongCha,
              nguoiDungCanSua == null ? "Thêm tài khoản người dùng mới" : "Chỉnh sửa tài khoản: " + nguoiDungCanSua.getTenDangNhap(),
              ModalityType.APPLICATION_MODAL);

        this.nguoiDung   = nguoiDungCanSua != null ? nguoiDungCanSua : new NguoiDung();
        this.dangSua = (nguoiDungCanSua != null && nguoiDungCanSua.getIdNguoiDung() > 0);

        setSize(500, 530);
        setLocationRelativeTo(doiTuongCha);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────────
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(30, 60, 114));
        khungTieuDe.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel(dangSua ? "CHỈNH SỬA THÔNG TIN TÀI KHOẢN" : "TẠO TÀI KHOẢN NGƯỜI DÙNG MỚI");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel(dangSua
                ? "Cập nhật họ tên, mật khẩu, vai trò hoặc trạng thái tài khoản"
                : "Nhập thông tin đăng nhập và vai trò cho người dùng mới");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(220, 230, 242));

        khungTieuDe.add(nhanTieuDe, BorderLayout.NORTH);
        khungTieuDe.add(nhanPhu,   BorderLayout.SOUTH);
        add(khungTieuDe, BorderLayout.NORTH);

        // ── Form Body ─────────────────────────────────────────────────────────
        JPanel khungNoiDung = new JPanel(new GridBagLayout());
        khungNoiDung.setBorder(new EmptyBorder(20, 24, 15, 24));
        khungNoiDung.setBackground(Color.WHITE);

        GridBagConstraints gia = new GridBagConstraints();
        gia.fill    = GridBagConstraints.HORIZONTAL;
        gia.insets  = new Insets(8, 8, 8, 8);

        Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font phongChuNhap = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Họ và tên
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0;
        JLabel nhan1 = new JLabel("Họ và tên (*):");
        nhan1.setFont(phongChuNhan);
        khungNoiDung.add(nhan1, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapHoTen = new JTextField(18);
        oNhapHoTen.setFont(phongChuNhap);
        oNhapHoTen.setPreferredSize(new Dimension(220, 32));
        khungNoiDung.add(oNhapHoTen, gia);

        // 2. Tên đăng nhập
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0;
        JLabel nhan2 = new JLabel("Tên đăng nhập (*):");
        nhan2.setFont(phongChuNhan);
        khungNoiDung.add(nhan2, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapTenDangNhap = new JTextField(18);
        oNhapTenDangNhap.setFont(phongChuNhap);
        oNhapTenDangNhap.setPreferredSize(new Dimension(220, 32));
        khungNoiDung.add(oNhapTenDangNhap, gia);

        // 3. Mật khẩu
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0;
        JLabel nhan3 = new JLabel(dangSua ? "Mật khẩu mới:" : "Mật khẩu (*):");
        nhan3.setFont(phongChuNhan);
        khungNoiDung.add(nhan3, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapMatKhau = new JPasswordField(18);
        oNhapMatKhau.setFont(phongChuNhap);
        oNhapMatKhau.setPreferredSize(new Dimension(220, 32));
        khungNoiDung.add(oNhapMatKhau, gia);

        // 4. Xác nhận mật khẩu
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0;
        JLabel nhan4 = new JLabel("Xác nhận mật khẩu:");
        nhan4.setFont(phongChuNhan);
        khungNoiDung.add(nhan4, gia);

        gia.gridx = 1; gia.weightx = 1;
        JPanel khungMatKhau2 = new JPanel(new BorderLayout(0, 2));
        khungMatKhau2.setOpaque(false);
        oNhapXacNhanMatKhau = new JPasswordField(18);
        oNhapXacNhanMatKhau.setFont(phongChuNhap);
        oNhapXacNhanMatKhau.setPreferredSize(new Dimension(220, 32));
        nhanGhiChuMatKhau = new JLabel(dangSua ? " (Để trống nếu không đổi mật khẩu)" : "");
        nhanGhiChuMatKhau.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        nhanGhiChuMatKhau.setForeground(Color.GRAY);
        khungMatKhau2.add(oNhapXacNhanMatKhau, BorderLayout.CENTER);
        khungMatKhau2.add(nhanGhiChuMatKhau, BorderLayout.SOUTH);
        khungNoiDung.add(khungMatKhau2, gia);

        // 5. Vai trò
        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0;
        JLabel nhan5 = new JLabel("Vai trò:");
        nhan5.setFont(phongChuNhan);
        khungNoiDung.add(nhan5, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonVaiTro = new JComboBox<>(new String[]{"STAFF", "ADMIN"});
        hopChonVaiTro.setFont(phongChuNhap);
        hopChonVaiTro.setPreferredSize(new Dimension(220, 32));
        hopChonVaiTro.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                                    boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                if ("ADMIN".equals(giaTri)) setText("Quản trị viên (ADMIN)");
                else setText("Nhân viên (STAFF)");
                return this;
            }
        });
        khungNoiDung.add(hopChonVaiTro, gia);

        // 6. Trạng thái
        gia.gridx = 0; gia.gridy = 5; gia.weightx = 0;
        JLabel nhan6 = new JLabel("Trạng thái:");
        nhan6.setFont(phongChuNhan);
        khungNoiDung.add(nhan6, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Đã khóa"});
        hopChonTrangThai.setFont(phongChuNhap);
        hopChonTrangThai.setPreferredSize(new Dimension(220, 32));
        khungNoiDung.add(hopChonTrangThai, gia);

        add(khungNoiDung, BorderLayout.CENTER);

        // Điền dữ liệu khi là Edit
        if (dangSua) {
            oNhapHoTen.setText(this.nguoiDung.getHoTen() != null ? this.nguoiDung.getHoTen() : "");
            oNhapTenDangNhap.setText(this.nguoiDung.getTenDangNhap() != null ? this.nguoiDung.getTenDangNhap() : "");
            hopChonVaiTro.setSelectedItem("ADMIN".equalsIgnoreCase(this.nguoiDung.getVaiTro()) ? "ADMIN" : "STAFF");
            hopChonTrangThai.setSelectedIndex(this.nguoiDung.isTrangThai() ? 0 : 1);
        }

        // ── Footer Buttons ────────────────────────────────────────────────────
        JPanel khungChan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        khungChan.setBackground(new Color(245, 247, 250));
        khungChan.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        nutHuy = new JButton("Hủy bỏ");
        nutHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutHuy.setPreferredSize(new Dimension(100, 34));
        nutHuy.setFocusPainted(false);
        nutHuy.addActionListener(suKien -> dispose());

        nutLuu = new JButton(dangSua ? "Cập nhật" : "Tạo tài khoản");
        view.view.UIStyle.setButtonIcon(nutLuu, dangSua ? "sua.png" : "them.png");
        nutLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutLuu.setBackground(new Color(13, 110, 253));
        nutLuu.setForeground(Color.WHITE);
        nutLuu.setPreferredSize(new Dimension(145, 34));
        nutLuu.setFocusPainted(false);
        nutLuu.addActionListener(suKien -> xuLyLuu());

        khungChan.add(nutHuy);
        khungChan.add(nutLuu);
        add(khungChan, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String hoTen      = oNhapHoTen.getText().trim();
        String tenDangNhap = oNhapTenDangNhap.getText().trim();
        String matKhau    = new String(oNhapMatKhau.getPassword()).trim();
        String xacNhan    = new String(oNhapXacNhanMatKhau.getPassword()).trim();
        boolean hoatHang  = hopChonTrangThai.getSelectedIndex() == 0;
        String vaiTro     = (String) hopChonVaiTro.getSelectedItem();

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ và tên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapHoTen.requestFocus(); return;
        }
        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapTenDangNhap.requestFocus(); return;
        }

        // Khi thêm mới, bắt buộc có mật khẩu
        if (!dangSua && matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapMatKhau.requestFocus(); return;
        }

        // Nếu có nhập mật khẩu thì phải khớp xác nhận
        if (!matKhau.isEmpty()) {
            if (matKhau.length() < 4) {
                JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 4 ký tự!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                oNhapMatKhau.requestFocus(); return;
            }
            if (!matKhau.equals(xacNhan)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                oNhapXacNhanMatKhau.requestFocus(); return;
            }
        }

        this.nguoiDung.setHoTen(hoTen);
        this.nguoiDung.setTenDangNhap(tenDangNhap);
        this.nguoiDung.setVaiTro(vaiTro);
        this.nguoiDung.setTrangThai(hoatHang);
        // Chỉ set mật khẩu nếu user có nhập (khi sửa, trống = giữ nguyên)
        if (!matKhau.isEmpty()) {
            this.nguoiDung.setMatKhau(matKhau);
        }

        daLuu = true;
        dispose();
    }

    public boolean isSaved()   { return daLuu; }
    public NguoiDung   getUser()   { return nguoiDung;  }
}
