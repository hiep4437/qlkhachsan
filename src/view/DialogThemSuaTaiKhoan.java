package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.users;

public class DialogThemSuaTaiKhoan extends JDialog {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;
    private JTextField txtHoTen;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;
    private JLabel lblGhiChuPass;

    private JButton btnLuu;
    private JButton btnHuy;

    private boolean saved = false;
    private users user;
    private final boolean isEdit;

    public DialogThemSuaTaiKhoan(Window parent, users userToEdit) {
        super(parent,
              userToEdit == null ? "Thêm tài khoản người dùng mới" : "Chỉnh sửa tài khoản: " + userToEdit.getTenDangNhap(),
              ModalityType.APPLICATION_MODAL);

        this.user   = userToEdit != null ? userToEdit : new users();
        this.isEdit = (userToEdit != null && userToEdit.getIdNguoiDung() > 0);

        setSize(500, 530);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────────
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 60, 114));
        pHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(isEdit ? "CHỈNH SỬA THÔNG TIN TÀI KHOẢN" : "TẠO TÀI KHOẢN NGƯỜI DÙNG MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(isEdit
                ? "Cập nhật họ tên, mật khẩu, vai trò hoặc trạng thái tài khoản"
                : "Nhập thông tin đăng nhập và vai trò cho người dùng mới");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(220, 230, 242));

        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(lblSub,   BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        // ── Form Body ─────────────────────────────────────────────────────────
        JPanel pBody = new JPanel(new GridBagLayout());
        pBody.setBorder(new EmptyBorder(20, 24, 15, 24));
        pBody.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.insets  = new Insets(8, 8, 8, 8);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Họ và tên
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel l1 = new JLabel("Họ và tên (*):");
        l1.setFont(fontLabel);
        pBody.add(l1, g);

        g.gridx = 1; g.weightx = 1;
        txtHoTen = new JTextField(18);
        txtHoTen.setFont(fontInput);
        txtHoTen.setPreferredSize(new Dimension(220, 32));
        pBody.add(txtHoTen, g);

        // 2. Tên đăng nhập
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel l2 = new JLabel("Tên đăng nhập (*):");
        l2.setFont(fontLabel);
        pBody.add(l2, g);

        g.gridx = 1; g.weightx = 1;
        txtTenDangNhap = new JTextField(18);
        txtTenDangNhap.setFont(fontInput);
        txtTenDangNhap.setPreferredSize(new Dimension(220, 32));
        pBody.add(txtTenDangNhap, g);

        // 3. Mật khẩu
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        JLabel l3 = new JLabel(isEdit ? "Mật khẩu mới:" : "Mật khẩu (*):");
        l3.setFont(fontLabel);
        pBody.add(l3, g);

        g.gridx = 1; g.weightx = 1;
        txtMatKhau = new JPasswordField(18);
        txtMatKhau.setFont(fontInput);
        txtMatKhau.setPreferredSize(new Dimension(220, 32));
        pBody.add(txtMatKhau, g);

        // 4. Xác nhận mật khẩu
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel l4 = new JLabel("Xác nhận mật khẩu:");
        l4.setFont(fontLabel);
        pBody.add(l4, g);

        g.gridx = 1; g.weightx = 1;
        JPanel pPass2 = new JPanel(new BorderLayout(0, 2));
        pPass2.setOpaque(false);
        txtXacNhanMatKhau = new JPasswordField(18);
        txtXacNhanMatKhau.setFont(fontInput);
        txtXacNhanMatKhau.setPreferredSize(new Dimension(220, 32));
        lblGhiChuPass = new JLabel(isEdit ? " (Để trống nếu không đổi mật khẩu)" : "");
        lblGhiChuPass.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblGhiChuPass.setForeground(Color.GRAY);
        pPass2.add(txtXacNhanMatKhau, BorderLayout.CENTER);
        pPass2.add(lblGhiChuPass, BorderLayout.SOUTH);
        pBody.add(pPass2, g);

        // 5. Vai trò
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel l5 = new JLabel("Vai trò:");
        l5.setFont(fontLabel);
        pBody.add(l5, g);

        g.gridx = 1; g.weightx = 1;
        cboVaiTro = new JComboBox<>(new String[]{"STAFF", "ADMIN"});
        cboVaiTro.setFont(fontInput);
        cboVaiTro.setPreferredSize(new Dimension(220, 32));
        cboVaiTro.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if ("ADMIN".equals(value)) setText("Quản trị viên (ADMIN)");
                else setText("Nhân viên (STAFF)");
                return this;
            }
        });
        pBody.add(cboVaiTro, g);

        // 6. Trạng thái
        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        JLabel l6 = new JLabel("Trạng thái:");
        l6.setFont(fontLabel);
        pBody.add(l6, g);

        g.gridx = 1; g.weightx = 1;
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Đã khóa"});
        cboTrangThai.setFont(fontInput);
        cboTrangThai.setPreferredSize(new Dimension(220, 32));
        pBody.add(cboTrangThai, g);

        add(pBody, BorderLayout.CENTER);

        // Điền dữ liệu khi là Edit
        if (isEdit) {
            txtHoTen.setText(this.user.getHoTen() != null ? this.user.getHoTen() : "");
            txtTenDangNhap.setText(this.user.getTenDangNhap() != null ? this.user.getTenDangNhap() : "");
            cboVaiTro.setSelectedItem("ADMIN".equalsIgnoreCase(this.user.getVaiTro()) ? "ADMIN" : "STAFF");
            cboTrangThai.setSelectedIndex(this.user.isTrangThai() ? 0 : 1);
        }

        // ── Footer Buttons ────────────────────────────────────────────────────
        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pFooter.setBackground(new Color(245, 247, 250));
        pFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnHuy.setPreferredSize(new Dimension(100, 34));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());

        btnLuu = new JButton(isEdit ? "Cập nhật" : "Tạo tài khoản");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(new Color(13, 110, 253));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(145, 34));
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> xuLyLuu());

        pFooter.add(btnHuy);
        pFooter.add(btnLuu);
        add(pFooter, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String hoTen      = txtHoTen.getText().trim();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau    = new String(txtMatKhau.getPassword()).trim();
        String xacNhan    = new String(txtXacNhanMatKhau.getPassword()).trim();
        boolean hoatDong  = cboTrangThai.getSelectedIndex() == 0;
        String vaiTro     = (String) cboVaiTro.getSelectedItem();

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ và tên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus(); return;
        }
        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDangNhap.requestFocus(); return;
        }

        // Khi thêm mới, bắt buộc có mật khẩu
        if (!isEdit && matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtMatKhau.requestFocus(); return;
        }

        // Nếu có nhập mật khẩu thì phải khớp xác nhận
        if (!matKhau.isEmpty()) {
            if (matKhau.length() < 4) {
                JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 4 ký tự!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtMatKhau.requestFocus(); return;
            }
            if (!matKhau.equals(xacNhan)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtXacNhanMatKhau.requestFocus(); return;
            }
        }

        this.user.setHoTen(hoTen);
        this.user.setTenDangNhap(tenDangNhap);
        this.user.setVaiTro(vaiTro);
        this.user.setTrangThai(hoatDong);
        // Chỉ set mật khẩu nếu user có nhập (khi sửa, trống = giữ nguyên)
        if (!matKhau.isEmpty()) {
            this.user.setMatKhau(matKhau);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved()   { return saved; }
    public users   getUser()   { return user;  }
}

