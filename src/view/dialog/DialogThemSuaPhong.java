package view.dialog;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Phong;
import model.LoaiPhong;

public class DialogThemSuaPhong extends JDialog {

    private JTextField txtSoPhong;
    private JSpinner spnTang;
    private JComboBox<LoaiPhong> cboLoaiPhong;
    private JTextField txtGiaMoiDem;
    private JComboBox<String> cboTrangThai;
    private JTextArea txtMoTa;
    private JButton btnLuu;
    private JButton btnHuy;

    private boolean saved = false;
    private Phong room;

    public DialogThemSuaPhong(Window parent, Phong roomToEdit, List<LoaiPhong> danhSachLoaiPhong) {
        super(parent, roomToEdit == null ? "Thêm phòng mới" : "Chỉnh sửa phòng " + roomToEdit.getSoPhong(),
                ModalityType.APPLICATION_MODAL);

        this.room = roomToEdit != null ? roomToEdit : new Phong();
        boolean isEdit = (roomToEdit != null && roomToEdit.getMaPhong() > 0);

        setSize(520, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 60, 114));
        pHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(isEdit ? "CHỈNH SỬA THÔNG TIN PHÒNG" : "THÊM PHÒNG MỚI VÀO HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(isEdit ? "Cập nhật loại phòng, giá, trạng thái hoặc thông tin phòng"
                : "Nhập thông tin chi tiết để thêm phòng mới");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(220, 230, 242));

        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(lblSub, BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        // Body Form Panel
        JPanel pBody = new JPanel(new GridBagLayout());
        pBody.setBorder(new EmptyBorder(20, 24, 15, 24));
        pBody.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 8, 8, 8);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Số phòng
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel lblSoPhong = new JLabel("Số phòng (*):");
        lblSoPhong.setFont(fontLabel);
        pBody.add(lblSoPhong, g);

        g.gridx = 1; g.weightx = 1;
        txtSoPhong = new JTextField(15);
        txtSoPhong.setFont(fontInput);
        txtSoPhong.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtSoPhong, g);

        // 2. Tầng
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel lblTang = new JLabel("Tầng:");
        lblTang.setFont(fontLabel);
        pBody.add(lblTang, g);

        g.gridx = 1; g.weightx = 1;
        spnTang = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spnTang.setFont(fontInput);
        spnTang.setPreferredSize(new Dimension(200, 32));
        pBody.add(spnTang, g);

        // 3. Loại phòng (JComboBox load từ DB)
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        JLabel lblLoaiPhong = new JLabel("Loại phòng (*):");
        lblLoaiPhong.setFont(fontLabel);
        pBody.add(lblLoaiPhong, g);

        g.gridx = 1; g.weightx = 1;
        cboLoaiPhong = new JComboBox<>();
        cboLoaiPhong.setFont(fontInput);
        cboLoaiPhong.setPreferredSize(new Dimension(200, 32));

        if (danhSachLoaiPhong != null) {
            for (LoaiPhong lt : danhSachLoaiPhong) {
                cboLoaiPhong.addItem(lt);
            }
        }
        cboLoaiPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LoaiPhong) {
                    LoaiPhong rt = (LoaiPhong) value;
                    setText(rt.getTenLoaiPhong() + " (Sức chứa " + rt.getSucChua() + " người)");
                }
                return this;
            }
        });
        pBody.add(cboLoaiPhong, g);

        // 4. Giá mỗi đêm
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel lblGia = new JLabel("Giá mỗi đêm (VNĐ) (*):");
        lblGia.setFont(fontLabel);
        pBody.add(lblGia, g);

        g.gridx = 1; g.weightx = 1;
        txtGiaMoiDem = new JTextField(15);
        txtGiaMoiDem.setFont(fontInput);
        txtGiaMoiDem.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtGiaMoiDem, g);

        // 5. Trạng thái
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(fontLabel);
        pBody.add(lblTrangThai, g);

        g.gridx = 1; g.weightx = 1;
        cboTrangThai = new JComboBox<>(new String[] { "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ" });
        cboTrangThai.setFont(fontInput);
        cboTrangThai.setPreferredSize(new Dimension(200, 32));
        pBody.add(cboTrangThai, g);

        // 6. Mô tả
        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblMoTa = new JLabel("Mô tả / Ghi chú:");
        lblMoTa.setFont(fontLabel);
        pBody.add(lblMoTa, g);

        g.gridx = 1; g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        txtMoTa = new JTextArea(3, 15);
        txtMoTa.setFont(fontInput);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane spMoTa = new JScrollPane(txtMoTa);
        spMoTa.setPreferredSize(new Dimension(200, 75));
        pBody.add(spMoTa, g);

        add(pBody, BorderLayout.CENTER);

        // Tự động tính tầng khi gõ số phòng (nếu thêm mới)
        txtSoPhong.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (!isEdit) {
                    String sp = txtSoPhong.getText().trim();
                    if (!sp.isEmpty() && Character.isDigit(sp.charAt(0))) {
                        int t = Character.getNumericValue(sp.charAt(0));
                        if (t >= 1 && t <= 9) {
                            spnTang.setValue(t);
                        }
                    }
                }
            }
        });

        // Điền dữ liệu nếu là Edit
        if (isEdit) {
            txtSoPhong.setText(this.room.getSoPhong());
            int t = this.room.getTang() > 0 ? this.room.getTang() : 1;
            spnTang.setValue(t);

            // Chọn đúng loại phòng
            if (danhSachLoaiPhong != null) {
                for (int i = 0; i < cboLoaiPhong.getItemCount(); i++) {
                    LoaiPhong rt = cboLoaiPhong.getItemAt(i);
                    if (rt.getMaLoaiPhong() == this.room.getMaLoaiPhong()) {
                        cboLoaiPhong.setSelectedIndex(i);
                        break;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("#.##");
            txtGiaMoiDem.setText(df.format(this.room.getGiaMoiDem()));

            String tt = this.room.getTrangThai();
            if ("AVAILABLE".equalsIgnoreCase(tt) || "TRỐNG".equalsIgnoreCase(tt)) {
                cboTrangThai.setSelectedItem("TRỐNG");
            } else if ("OCCUPIED".equalsIgnoreCase(tt) || "CÓ KHÁCH".equalsIgnoreCase(tt)) {
                cboTrangThai.setSelectedItem("CÓ KHÁCH");
            } else if ("CLEANING".equalsIgnoreCase(tt) || "ĐANG DỌN".equalsIgnoreCase(tt)) {
                cboTrangThai.setSelectedItem("ĐANG DỌN");
            } else if ("MAINTENANCE".equalsIgnoreCase(tt) || "BẢO TRÌ".equalsIgnoreCase(tt)) {
                cboTrangThai.setSelectedItem("BẢO TRÌ");
            }

            txtMoTa.setText(this.room.getMoTa() != null ? this.room.getMoTa() : "");
        } else {
            txtGiaMoiDem.setText("300000");
        }

        // Footer Buttons
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
        String soPhong = txtSoPhong.getText().trim();
        if (soPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSoPhong.requestFocus();
            return;
        }

        LoaiPhong loaiDaChon = (LoaiPhong) cboLoaiPhong.getSelectedItem();
        if (loaiDaChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        float gia;
        try {
            String giaStr = txtGiaMoiDem.getText().trim().replace(",", "").replace(".", "");
            gia = Float.parseFloat(giaStr);
            if (gia < 0) {
                JOptionPane.showMessageDialog(this, "Giá mỗi đêm không được âm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtGiaMoiDem.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá mỗi đêm phải là số hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtGiaMoiDem.requestFocus();
            return;
        }

        int tang = (int) spnTang.getValue();
        String trangThaiChon = (String) cboTrangThai.getSelectedItem();
        String moTa = txtMoTa.getText().trim();

        // Gán vào đối tượng room
        this.room.setSoPhong(soPhong);
        this.room.setTang(tang);
        this.room.setMaLoaiPhong(loaiDaChon.getMaLoaiPhong());
        this.room.setTenLoaiPhong(loaiDaChon.getTenLoaiPhong());
        this.room.setGiaMoiDem(gia);
        this.room.setTrangThai(trangThaiChon);
        this.room.setMoTa(moTa);

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public Phong getRoom() {
        return room;
    }
}

