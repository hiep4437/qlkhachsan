package view.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.KhachHang;

public class DialogThemSuaKhachHang extends JDialog {

    private JTextField txtHoTen;
    private JTextField txtCCCD;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JButton btnLuu;
    private JButton btnHuy;

    private boolean saved = false;
    private KhachHang customer;

    public DialogThemSuaKhachHang(Window parent, KhachHang customerToEdit) {
        super(parent, customerToEdit == null ? "Thêm khách hàng mới" : "Chỉnh sửa khách hàng: " + customerToEdit.getNameKhachHang(),
                ModalityType.APPLICATION_MODAL);

        this.customer = customerToEdit != null ? customerToEdit : new KhachHang();
        boolean isEdit = (customerToEdit != null && customerToEdit.getIdKhachHang() > 0);

        setSize(480, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 60, 114));
        pHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(isEdit ? "CHỈNH SỬA THÔNG TIN KHÁCH HÀNG" : "THÊM KHÁCH HÀNG MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(isEdit ? "Cập nhật họ tên, CCCD, số điện thoại hoặc địa chỉ"
                : "Nhập thông tin chi tiết khách hàng để lưu vào hệ thống");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(220, 230, 242));

        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(lblSub, BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        // Form Body
        JPanel pBody = new JPanel(new GridBagLayout());
        pBody.setBorder(new EmptyBorder(20, 24, 15, 24));
        pBody.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 8, 8, 8);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Họ và tên
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel lblHoTen = new JLabel("Họ và tên (*):");
        lblHoTen.setFont(fontLabel);
        pBody.add(lblHoTen, g);

        g.gridx = 1; g.weightx = 1;
        txtHoTen = new JTextField(15);
        txtHoTen.setFont(fontInput);
        txtHoTen.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtHoTen, g);

        // 2. Số CCCD
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel lblCCCD = new JLabel("Số CCCD (*):");
        lblCCCD.setFont(fontLabel);
        pBody.add(lblCCCD, g);

        g.gridx = 1; g.weightx = 1;
        txtCCCD = new JTextField(15);
        txtCCCD.setFont(fontInput);
        txtCCCD.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtCCCD, g);

        // 3. Số điện thoại
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        JLabel lblSDT = new JLabel("Số điện thoại (*):");
        lblSDT.setFont(fontLabel);
        pBody.add(lblSDT, g);

        g.gridx = 1; g.weightx = 1;
        txtSDT = new JTextField(15);
        txtSDT.setFont(fontInput);
        txtSDT.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtSDT, g);

        // 4. Email
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(fontLabel);
        pBody.add(lblEmail, g);

        g.gridx = 1; g.weightx = 1;
        txtEmail = new JTextField(15);
        txtEmail.setFont(fontInput);
        txtEmail.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtEmail, g);

        // 5. Địa chỉ
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(fontLabel);
        pBody.add(lblDiaChi, g);

        g.gridx = 1; g.weightx = 1;
        txtDiaChi = new JTextField(15);
        txtDiaChi.setFont(fontInput);
        txtDiaChi.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtDiaChi, g);

        add(pBody, BorderLayout.CENTER);

        // Điền dữ liệu nếu là sửa
        if (isEdit) {
            txtHoTen.setText(this.customer.getNameKhachHang() != null ? this.customer.getNameKhachHang() : "");
            txtCCCD.setText(this.customer.getSoCCCD() != null ? this.customer.getSoCCCD() : "");
            txtSDT.setText(this.customer.getSoDienThoai() != null ? this.customer.getSoDienThoai() : "");
            txtEmail.setText(this.customer.getEmail() != null ? this.customer.getEmail() : "");
            txtDiaChi.setText(this.customer.getDiaChi() != null ? this.customer.getDiaChi() : "");
        }

        // Footer buttons
        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pFooter.setBackground(new Color(245, 247, 250));
        pFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnHuy.setPreferredSize(new Dimension(100, 34));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());

        btnLuu = new JButton(isEdit ? "Cập nhật" : "Thêm mới");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(new Color(13, 110, 253));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(120, 34));
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> xuLyLuu());

        pFooter.add(btnHuy);
        pFooter.add(btnLuu);
        add(pFooter, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String hoTen = txtHoTen.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return;
        }

        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtCCCD.requestFocus();
            return;
        }

        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }

        this.customer.setNameKhachHang(hoTen);
        this.customer.setSoCCCD(cccd);
        this.customer.setSoDienThoai(sdt);
        this.customer.setEmail(email);
        this.customer.setDiaChi(diaChi);

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public KhachHang getCustomer() {
        return customer;
    }
}

