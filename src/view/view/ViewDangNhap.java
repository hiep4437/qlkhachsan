package view.view;

import dao.KetNoiCSDL;
import java.awt.*;
import javax.swing.*;

public class ViewDangNhap extends JFrame {
    public JTextField txtTenDangNhap;
    public JPasswordField txtMatKhau;
    public JButton btnDangNhap, btnThoat;
    public JButton btnCauHinhServer;
    public JLabel lblThongBaoServer;

    public ViewDangNhap() {
        setTitle("Đăng nhập - Quản lý khách sạn");
        AppWindowIcon.apply(this);
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chinh
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblLogo = new JLabel("QUẢN LÝ KHÁCH SẠN");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(new Color(26, 82, 118));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Đăng nhập để vào hệ thống vận hành");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblSub);
        headerPanel.add(Box.createVerticalStrut(15));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // Row 1: Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        txtTenDangNhap = new JTextField(15);
        txtTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTenDangNhap.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtTenDangNhap, gbc);

        // Row 2: Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        txtMatKhau = new JPasswordField(15);
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMatKhau.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtMatKhau, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDangNhap.setBackground(new Color(41, 128, 185));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setPreferredSize(new Dimension(120, 34));

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnThoat.setPreferredSize(new Dimension(90, 34));

        btnPanel.add(btnDangNhap);
        btnPanel.add(btnThoat);

        // Bottom config panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCauHinhServer = new JButton(" Cấu hình máy chủ");
        btnCauHinhServer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCauHinhServer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCauHinhServer.setFocusPainted(false);

        lblThongBaoServer = new JLabel(
                "CSDL: " + KetNoiCSDL.getHost() + ":" + KetNoiCSDL.getPort() + "/" + KetNoiCSDL.getDatabase());
        lblThongBaoServer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblThongBaoServer.setForeground(new Color(100, 100, 100));

        bottomPanel.add(lblThongBaoServer, BorderLayout.WEST);
        bottomPanel.add(btnCauHinhServer, BorderLayout.EAST);

        // Center container
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(formPanel, BorderLayout.CENTER);
        centerContainer.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerContainer, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Events
        btnThoat.addActionListener(e -> System.exit(0));
        btnCauHinhServer.addActionListener(e -> hienThiDialogCauHinhServer());

        // Phím Enter đăng nhập
        getRootPane().setDefaultButton(btnDangNhap);
    }

    public void capNhatThongTinServer() {
        lblThongBaoServer.setText(
                "CSDL: " + KetNoiCSDL.getHost() + ":" + KetNoiCSDL.getPort() + "/" + KetNoiCSDL.getDatabase());
    }

    public void hienThiDialogCauHinhServer() {
        JDialog dialog = new JDialog(this, "Cấu hình kết nối máy chủ CSDL", true);
        dialog.setSize(440, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField txtHost = new JTextField(KetNoiCSDL.getHost(), 15);
        JTextField txtPort = new JTextField(KetNoiCSDL.getPort(), 15);
        JTextField txtDb = new JTextField(KetNoiCSDL.getDatabase(), 15);
        JTextField txtUser = new JTextField(KetNoiCSDL.getUser(), 15);
        JPasswordField txtPass = new JPasswordField(KetNoiCSDL.getPass(), 15);

        g.gridx = 0;
        g.gridy = 0;
        pForm.add(new JLabel("Máy chủ (Host):"), g);
        g.gridx = 1;
        g.gridy = 0;
        pForm.add(txtHost, g);

        g.gridx = 0;
        g.gridy = 1;
        pForm.add(new JLabel("Cổng (Port):"), g);
        g.gridx = 1;
        g.gridy = 1;
        pForm.add(txtPort, g);

        g.gridx = 0;
        g.gridy = 2;
        pForm.add(new JLabel("Tên CSDL (Database):"), g);
        g.gridx = 1;
        g.gridy = 2;
        pForm.add(txtDb, g);

        g.gridx = 0;
        g.gridy = 3;
        pForm.add(new JLabel("Tài khoản (User):"), g);
        g.gridx = 1;
        g.gridy = 3;
        pForm.add(txtUser, g);

        g.gridx = 0;
        g.gridy = 4;
        pForm.add(new JLabel("Mật khẩu (Password):"), g);
        g.gridx = 1;
        g.gridy = 4;
        pForm.add(txtPass, g);

        JLabel lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnTest = new JButton("Kiểm tra kết nối");
        JButton btnLuu = new JButton("Lưu cấu hình");
        JButton btnMacDinh = new JButton("Mặc định");
        JButton btnDong = new JButton("Đóng");

        pButtons.add(btnTest);
        pButtons.add(btnLuu);
        pButtons.add(btnMacDinh);
        pButtons.add(btnDong);

        btnTest.addActionListener(ev -> {
            String passStr = new String(txtPass.getPassword());
            lblStatus.setText("Đang kiểm tra kết nối...");
            lblStatus.setForeground(Color.BLUE);
            String err = KetNoiCSDL.testConnection(txtHost.getText(), txtPort.getText(), txtDb.getText(),
                    txtUser.getText(), passStr);
            if (err == null) {
                lblStatus.setText("Kết nối thành công tới CSDL MySQL!");
                lblStatus.setForeground(new Color(39, 174, 96));
            } else {
                lblStatus.setText("Kết nối thất bại: " + err);
                lblStatus.setForeground(new Color(192, 57, 43));
            }
        });

        btnLuu.addActionListener(ev -> {
            String passStr = new String(txtPass.getPassword());
            boolean ok = KetNoiCSDL.saveConfig(txtHost.getText(), txtPort.getText(), txtDb.getText(),
                    txtUser.getText(), passStr);
            if (ok) {
                capNhatThongTinServer();
                JOptionPane.showMessageDialog(dialog, "Đã lưu cấu hình máy chủ thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Không thể lưu cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnMacDinh.addActionListener(ev -> {
            txtHost.setText("localhost");
            txtPort.setText("3306");
            txtDb.setText("hotel_management");
            txtUser.setText("root");
            txtPass.setText("");
            lblStatus.setText("Đã khôi phục giá trị mặc định. Bấm 'Lưu cấu hình' để áp dụng.");
            lblStatus.setForeground(Color.DARK_GRAY);
        });

        btnDong.addActionListener(ev -> dialog.dispose());

        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.add(pForm, BorderLayout.CENTER);
        pCenter.add(lblStatus, BorderLayout.SOUTH);

        dialog.add(pCenter, BorderLayout.CENTER);
        dialog.add(pButtons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public String getTenDangNhap() {
        return txtTenDangNhap.getText().trim();
    }

    public String getMatKhau() {
        return new String(txtMatKhau.getPassword());
    }

    public JButton getBtnDangNhap() {
        return btnDangNhap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewDangNhap().setVisible(true));
    }
}
