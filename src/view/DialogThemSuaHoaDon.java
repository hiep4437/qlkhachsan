package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.invoices;

public class DialogThemSuaHoaDon extends JDialog {

    private JTextField txtMaDatPhong;
    private JTextField txtTienPhong;
    private JTextField txtPhuThu;
    private JTextField txtGiamGia;
    private JTextField txtTongTien;
    private JComboBox<String> cboPhuongThuc;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu;
    private JButton btnHuy;

    private boolean saved = false;
    private invoices invoice;
    private DecimalFormat df = new DecimalFormat("#,###");

    public DialogThemSuaHoaDon(Window parent, invoices invoiceToEdit) {
        super(parent, invoiceToEdit == null ? "Lập hóa đơn mới" : "Chỉnh sửa hóa đơn #" + invoiceToEdit.getIdHoaDon(),
                ModalityType.APPLICATION_MODAL);

        this.invoice = invoiceToEdit != null ? invoiceToEdit : new invoices();
        boolean isEdit = (invoiceToEdit != null && invoiceToEdit.getIdHoaDon() > 0);

        setSize(520, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 60, 114));
        pHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(isEdit ? "CHỈNH SỬA HÓA ĐƠN #" + this.invoice.getIdHoaDon() : "LẬP HÓA ĐƠN THANH TOÁN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(isEdit ? "Cập nhật tiền phòng, phụ thu, giảm giá hoặc trạng thái hóa đơn"
                : "Nhập thông tin đặt phòng và các khoản tiền để tạo hóa đơn mới");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(220, 230, 242));

        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(lblSub, BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        // Body Form
        JPanel pBody = new JPanel(new GridBagLayout());
        pBody.setBorder(new EmptyBorder(20, 24, 15, 24));
        pBody.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 8, 8, 8);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Mã đặt phòng
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel lblMaDP = new JLabel("Mã đặt phòng (*):");
        lblMaDP.setFont(fontLabel);
        pBody.add(lblMaDP, g);

        g.gridx = 1; g.weightx = 1;
        txtMaDatPhong = new JTextField(15);
        txtMaDatPhong.setFont(fontInput);
        txtMaDatPhong.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtMaDatPhong, g);

        // 2. Tiền phòng
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel lblTienPhong = new JLabel("Tiền phòng (VNĐ):");
        lblTienPhong.setFont(fontLabel);
        pBody.add(lblTienPhong, g);

        g.gridx = 1; g.weightx = 1;
        txtTienPhong = new JTextField("0", 15);
        txtTienPhong.setFont(fontInput);
        txtTienPhong.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtTienPhong, g);

        // 3. Phụ thu
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        JLabel lblPhuThu = new JLabel("Phụ thu (VNĐ):");
        lblPhuThu.setFont(fontLabel);
        pBody.add(lblPhuThu, g);

        g.gridx = 1; g.weightx = 1;
        txtPhuThu = new JTextField("0", 15);
        txtPhuThu.setFont(fontInput);
        txtPhuThu.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtPhuThu, g);

        // 4. Giảm giá
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel lblGiamGia = new JLabel("Giảm giá (VNĐ):");
        lblGiamGia.setFont(fontLabel);
        pBody.add(lblGiamGia, g);

        g.gridx = 1; g.weightx = 1;
        txtGiamGia = new JTextField("0", 15);
        txtGiamGia.setFont(fontInput);
        txtGiamGia.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtGiamGia, g);

        // 5. Tổng tiền (tự động tính)
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel lblTongTien = new JLabel("Tổng tiền (VNĐ):");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongTien.setForeground(new Color(13, 110, 253));
        pBody.add(lblTongTien, g);

        g.gridx = 1; g.weightx = 1;
        txtTongTien = new JTextField("0", 15);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(new Color(240, 244, 250));
        txtTongTien.setForeground(new Color(13, 110, 253));
        txtTongTien.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtTongTien, g);

        // 6. Phương thức thanh toán
        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        JLabel lblPT = new JLabel("Phương thức:");
        lblPT.setFont(fontLabel);
        pBody.add(lblPT, g);

        g.gridx = 1; g.weightx = 1;
        cboPhuongThuc = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ" });
        cboPhuongThuc.setFont(fontInput);
        cboPhuongThuc.setPreferredSize(new Dimension(200, 32));
        pBody.add(cboPhuongThuc, g);

        // 7. Trạng thái thanh toán
        g.gridx = 0; g.gridy = 6; g.weightx = 0;
        JLabel lblTT = new JLabel("Trạng thái:");
        lblTT.setFont(fontLabel);
        pBody.add(lblTT, g);

        g.gridx = 1; g.weightx = 1;
        cboTrangThai = new JComboBox<>(new String[] { "PAID", "UNPAID", "REFUNDED" });
        cboTrangThai.setFont(fontInput);
        cboTrangThai.setPreferredSize(new Dimension(200, 32));
        cboTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String s = String.valueOf(value);
                if ("PAID".equalsIgnoreCase(s)) setText("Đã thanh toán (PAID)");
                else if ("UNPAID".equalsIgnoreCase(s)) setText("Chưa thanh toán (UNPAID)");
                else if ("REFUNDED".equalsIgnoreCase(s)) setText("Đã hoàn tiền (REFUNDED)");
                return this;
            }
        });
        pBody.add(cboTrangThai, g);

        add(pBody, BorderLayout.CENTER);

        // Lắng nghe thay đổi các ô tiền để tự động tính tổng
        KeyAdapter tinhTongAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTongTien();
            }
        };
        txtTienPhong.addKeyListener(tinhTongAdapter);
        txtPhuThu.addKeyListener(tinhTongAdapter);
        txtGiamGia.addKeyListener(tinhTongAdapter);

        // Điền dữ liệu nếu là sửa
        if (isEdit) {
            txtMaDatPhong.setText(String.valueOf(this.invoice.getIdDatPhong()));
            txtTienPhong.setText(String.valueOf(Math.round(this.invoice.getTienPhong())));
            txtPhuThu.setText(String.valueOf(Math.round(this.invoice.getPhuThu())));
            txtGiamGia.setText(String.valueOf(Math.round(this.invoice.getGiamGia())));
            tinhTongTien();

            if (this.invoice.getPhuongThucThanhToan() != null) {
                cboPhuongThuc.setSelectedItem(this.invoice.getPhuongThucThanhToan());
            }

            String st = this.invoice.getTrangThaiThanhToan();
            if (st != null) {
                if (st.contains("PAID") || st.contains("Đã thanh toán")) cboTrangThai.setSelectedItem("PAID");
                else if (st.contains("REFUNDED") || st.contains("hoàn tiền")) cboTrangThai.setSelectedItem("REFUNDED");
                else cboTrangThai.setSelectedItem("UNPAID");
            }
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

        btnLuu = new JButton(isEdit ? "Cập nhật" : "Lưu hóa đơn");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(new Color(13, 110, 253));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(130, 34));
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> xuLyLuu());

        pFooter.add(btnHuy);
        pFooter.add(btnLuu);
        add(pFooter, BorderLayout.SOUTH);
    }

    private void tinhTongTien() {
        try {
            float tp = Float.parseFloat(txtTienPhong.getText().trim().replaceAll("[^0-9.]", ""));
            float pt = Float.parseFloat(txtPhuThu.getText().trim().replaceAll("[^0-9.]", ""));
            float gg = Float.parseFloat(txtGiamGia.getText().trim().replaceAll("[^0-9.]", ""));
            float tong = tp + pt - gg;
            if (tong < 0) tong = 0;
            txtTongTien.setText(df.format(tong) + " đ");
        } catch (Exception ignored) {}
    }

    private void xuLyLuu() {
        int maDatPhong;
        try {
            maDatPhong = Integer.parseInt(txtMaDatPhong.getText().trim());
            if (maDatPhong <= 0) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Mã đặt phòng phải là số nguyên dương hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtMaDatPhong.requestFocus();
            return;
        }

        float tp, pt, gg;
        try {
            tp = Float.parseFloat(txtTienPhong.getText().trim().replaceAll("[^0-9.]", ""));
            pt = Float.parseFloat(txtPhuThu.getText().trim().replaceAll("[^0-9.]", ""));
            gg = Float.parseFloat(txtGiamGia.getText().trim().replaceAll("[^0-9.]", ""));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Các khoản tiền phải là số hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        float tong = tp + pt - gg;
        if (tong < 0) tong = 0;

        this.invoice.setIdDatPhong(maDatPhong);
        this.invoice.setTienPhong(tp);
        this.invoice.setPhuThu(pt);
        this.invoice.setGiamGia(gg);
        this.invoice.setTongTien(tong);
        this.invoice.setPhuongThucThanhToan((String) cboPhuongThuc.getSelectedItem());
        this.invoice.setTrangThaiThanhToan((String) cboTrangThai.getSelectedItem());

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public invoices getInvoice() {
        return invoice;
    }
}

