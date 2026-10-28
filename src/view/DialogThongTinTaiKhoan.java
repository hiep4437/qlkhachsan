package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.users;
import service.UserService;

public class DialogThongTinTaiKhoan extends JDialog {

    private users user;
    private UserService userService = new UserService();
    private Runnable onUpdatedCallback;

    private JTextField txtId;
    private JTextField txtUsername;
    private JTextField txtFullName;
    private JTextField txtRole;
    private JTextField txtStatus;

    private JPasswordField txtOldPass;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;

    public DialogThongTinTaiKhoan(Window parent, users user, Runnable onUpdatedCallback) {
        super(parent, "Thông tin tài khoản cá nhân", ModalityType.APPLICATION_MODAL);
        this.user = user;
        this.onUpdatedCallback = onUpdatedCallback;

        setSize(500, 520);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 1. Header Banner
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 44, 97));
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        avatarPanel.setOpaque(false);

        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        lblAvatar.setForeground(Color.WHITE);

        JPanel infoHeader = new JPanel(new GridLayout(2, 1, 2, 2));
        infoHeader.setOpaque(false);

        JLabel lblNameHeader = new JLabel(user.getHoTen());
        lblNameHeader.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblNameHeader.setForeground(Color.WHITE);

        String roleStr = user.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)";
        JLabel lblRoleHeader = new JLabel("Vai trò: " + roleStr + "  •  @" + user.getTenDangNhap());
        lblRoleHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRoleHeader.setForeground(new Color(200, 225, 255));

        infoHeader.add(lblNameHeader);
        infoHeader.add(lblRoleHeader);

        avatarPanel.add(lblAvatar);
        avatarPanel.add(infoHeader);
        headerPanel.add(avatarPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Tabs: Thong tin & Doi mat khau
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("👤 Thông tin cá nhân", taoPanelThongTin());
        tabbedPane.addTab("🔑 Đổi mật khẩu", taoPanelDoiMatKhau());

        add(tabbedPane, BorderLayout.CENTER);

        // 3. Bottom Close Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        bottomPanel.add(btnClose);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel taoPanelThongTin() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(String.valueOf(user.getIdNguoiDung()));
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 243, 246));

        txtUsername = new JTextField(user.getTenDangNhap());
        txtUsername.setEditable(false);
        txtUsername.setBackground(new Color(240, 243, 246));

        txtRole = new JTextField(user.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)");
        txtRole.setEditable(false);
        txtRole.setBackground(new Color(240, 243, 246));

        txtStatus = new JTextField(user.isTrangThai() ? "🟢 Đang hoạt động" : "🔒 Đã bị khóa");
        txtStatus.setEditable(false);
        txtStatus.setBackground(new Color(240, 243, 246));

        txtFullName = new JTextField(user.getHoTen(), 20);
        txtFullName.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Row 0: ID
        g.gridx = 0; g.gridy = 0; g.weightx = 0.3;
        form.add(new JLabel("Mã người dùng:"), g);
        g.gridx = 1; g.gridy = 0; g.weightx = 0.7;
        form.add(txtId, g);

        // Row 1: Username
        g.gridx = 0; g.gridy = 1; g.weightx = 0.3;
        form.add(new JLabel("Tên đăng nhập:"), g);
        g.gridx = 1; g.gridy = 1; g.weightx = 0.7;
        form.add(txtUsername, g);

        // Row 2: Full name
        g.gridx = 0; g.gridy = 2; g.weightx = 0.3;
        form.add(new JLabel("Họ và tên:"), g);
        g.gridx = 1; g.gridy = 2; g.weightx = 0.7;
        form.add(txtFullName, g);

        // Row 3: Role
        g.gridx = 0; g.gridy = 3; g.weightx = 0.3;
        form.add(new JLabel("Vai trò:"), g);
        g.gridx = 1; g.gridy = 3; g.weightx = 0.7;
        form.add(txtRole, g);

        // Row 4: Status
        g.gridx = 0; g.gridy = 4; g.weightx = 0.3;
        form.add(new JLabel("Trạng thái:"), g);
        g.gridx = 1; g.gridy = 4; g.weightx = 0.7;
        form.add(txtStatus, g);

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        btnPanel.setOpaque(false);

        JButton btnLuu = new JButton("💾 Lưu họ tên mới");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(new Color(37, 99, 235));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> capNhatHoTen());
        btnPanel.add(btnLuu);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel taoPanelDoiMatKhau() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 6, 8, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtOldPass = new JPasswordField(20);
        txtNewPass = new JPasswordField(20);
        txtConfirmPass = new JPasswordField(20);

        // Row 0: Old pass
        g.gridx = 0; g.gridy = 0; g.weightx = 0.4;
        form.add(new JLabel("Mật khẩu hiện tại:"), g);
        g.gridx = 1; g.gridy = 0; g.weightx = 0.6;
        form.add(txtOldPass, g);

        // Row 1: New pass
        g.gridx = 0; g.gridy = 1; g.weightx = 0.4;
        form.add(new JLabel("Mật khẩu mới:"), g);
        g.gridx = 1; g.gridy = 1; g.weightx = 0.6;
        form.add(txtNewPass, g);

        // Row 2: Confirm pass
        g.gridx = 0; g.gridy = 2; g.weightx = 0.4;
        form.add(new JLabel("Nhập lại mật khẩu mới:"), g);
        g.gridx = 1; g.gridy = 2; g.weightx = 0.6;
        form.add(txtConfirmPass, g);

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        btnPanel.setOpaque(false);

        JButton btnDoiPass = new JButton("🔑 Đổi mật khẩu");
        btnDoiPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDoiPass.setBackground(new Color(16, 185, 129));
        btnDoiPass.setForeground(Color.WHITE);
        btnDoiPass.setFocusPainted(false);
        btnDoiPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDoiPass.addActionListener(e -> thucHienDoiMatKhau());
        btnPanel.add(btnDoiPass);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void capNhatHoTen() {
        String newName = txtFullName.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ và tên không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kq = userService.capNhatHoTen(user.getIdNguoiDung(), newName);
        if (kq > 0) {
            user.setHoTen(newName);
            JOptionPane.showMessageDialog(this, "Cập nhật họ tên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (onUpdatedCallback != null) {
                onUpdatedCallback.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật họ tên. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void thucHienDoiMatKhau() {
        String oldPass = new String(txtOldPass.getPassword());
        String newPass = new String(txtNewPass.getPassword());
        String confirmPass = new String(txtConfirmPass.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới và xác nhận mật khẩu không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 4 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kq = userService.doiMatKhau(user.getIdNguoiDung(), oldPass, newPass);
        if (kq == -1) {
            JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else if (kq > 0) {
            user.setMatKhau(newPass);
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            txtOldPass.setText("");
            txtNewPass.setText("");
            txtConfirmPass.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

