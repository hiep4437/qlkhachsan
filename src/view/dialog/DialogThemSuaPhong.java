package view.dialog;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Phong;
import model.LoaiPhong;

public class DialogThemSuaPhong extends JDialog {

    private JTextField oNhapSoPhong;
    private JSpinner vongQuayTang;
    private JComboBox<LoaiPhong> hopChonLoaiPhong;
    private JTextField oNhapGiaMoiDem;
    private JComboBox<String> hopChonTrangThai;
    private JTextArea oNhapMoTa;
    private JButton btLuu;
    private JButton btHuy;

    private boolean daLuu = false;
    private Phong phong;

    public DialogThemSuaPhong(Window doiTuongCha, Phong phongCanSua, List<LoaiPhong> danhSachLoaiPhong) {
        super(doiTuongCha, phongCanSua == null ? "Thêm phòng mới" : "Chỉnh sửa phòng " + phongCanSua.getSoPhong(),
                ModalityType.APPLICATION_MODAL);

        this.phong = phongCanSua != null ? phongCanSua : new Phong();
        boolean dangSua = (phongCanSua != null && phongCanSua.getMaPhong() > 0);

        setSize(520, 560);
        setLocationRelativeTo(doiTuongCha);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(30, 60, 114));
        khungTieuDe.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel(dangSua ? "CHỈNH SỬA THÔNG TIN PHÒNG" : "THÊM PHÒNG MỚI VÀO HỆ THỐNG");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel(dangSua ? "Cập nhật loại phòng, giá, trạng thái hoặc thông tin phòng"
                : "Nhập thông tin chi tiết để thêm phòng mới");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(220, 230, 242));

        khungTieuDe.add(nhanTieuDe, BorderLayout.NORTH);
        khungTieuDe.add(nhanPhu, BorderLayout.SOUTH);
        add(khungTieuDe, BorderLayout.NORTH);

        // Body Form Panel
        JPanel khungNoiDung = new JPanel(new GridBagLayout());
        khungNoiDung.setBorder(new EmptyBorder(20, 24, 15, 24));
        khungNoiDung.setBackground(Color.WHITE);

        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(8, 8, 8, 8);

        Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font phongChuNhap = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Số phòng
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0;
        JLabel nhanSoPhong = new JLabel("Số phòng (*):");
        nhanSoPhong.setFont(phongChuNhan);
        khungNoiDung.add(nhanSoPhong, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapSoPhong = new JTextField(15);
        oNhapSoPhong.setFont(phongChuNhap);
        oNhapSoPhong.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapSoPhong, gia);

        // 2. Tầng
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0;
        JLabel nhanTang = new JLabel("Tầng:");
        nhanTang.setFont(phongChuNhan);
        khungNoiDung.add(nhanTang, gia);

        gia.gridx = 1; gia.weightx = 1;
        vongQuayTang = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        vongQuayTang.setFont(phongChuNhap);
        vongQuayTang.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(vongQuayTang, gia);

        // 3. Loại phòng (JComboBox load từ DB)
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0;
        JLabel nhanLoaiPhong = new JLabel("Loại phòng (*):");
        nhanLoaiPhong.setFont(phongChuNhan);
        khungNoiDung.add(nhanLoaiPhong, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonLoaiPhong = new JComboBox<>();
        hopChonLoaiPhong.setFont(phongChuNhap);
        hopChonLoaiPhong.setPreferredSize(new Dimension(200, 32));

        if (danhSachLoaiPhong != null) {
            for (LoaiPhong nhoHon : danhSachLoaiPhong) {
                hopChonLoaiPhong.addItem(nhoHon);
            }
        }
        hopChonLoaiPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                if (giaTri instanceof LoaiPhong) {
                    LoaiPhong loai = (LoaiPhong) giaTri;
                    setText(loai.getTenLoaiPhong() + " (Sức chứa " + loai.getSucChua() + " người)");
                }
                return this;
            }
        });
        khungNoiDung.add(hopChonLoaiPhong, gia);

        // 4. Giá mỗi đêm
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0;
        JLabel nhanGia = new JLabel("Giá mỗi đêm (VNĐ) (*):");
        nhanGia.setFont(phongChuNhan);
        khungNoiDung.add(nhanGia, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapGiaMoiDem = new JTextField(15);
        oNhapGiaMoiDem.setFont(phongChuNhap);
        oNhapGiaMoiDem.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapGiaMoiDem, gia);

        // 5. Trạng thái
        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0;
        JLabel nhanTrangThai = new JLabel("Trạng thái:");
        nhanTrangThai.setFont(phongChuNhan);
        khungNoiDung.add(nhanTrangThai, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonTrangThai = new JComboBox<>(new String[] { "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ" });
        hopChonTrangThai.setFont(phongChuNhap);
        hopChonTrangThai.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(hopChonTrangThai, gia);

        // 6. Mô tả
        gia.gridx = 0; gia.gridy = 5; gia.weightx = 0;
        gia.anchor = GridBagConstraints.NORTHWEST;
        JLabel nhanMoTa = new JLabel("Mô tả / Ghi chú:");
        nhanMoTa.setFont(phongChuNhan);
        khungNoiDung.add(nhanMoTa, gia);

        gia.gridx = 1; gia.weightx = 1;
        gia.anchor = GridBagConstraints.CENTER;
        oNhapMoTa = new JTextArea(3, 15);
        oNhapMoTa.setFont(phongChuNhap);
        oNhapMoTa.setLineWrap(true);
        oNhapMoTa.setWrapStyleWord(true);
        JScrollPane thanhCuonMoTa = new JScrollPane(oNhapMoTa);
        thanhCuonMoTa.setPreferredSize(new Dimension(200, 75));
        khungNoiDung.add(thanhCuonMoTa, gia);

        add(khungNoiDung, BorderLayout.CENTER);

        // Tự động tính tầng khi gõ số phòng (nếu thêm mới)
        oNhapSoPhong.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent suKien) {
                if (!dangSua) {
                    String thanhCuon = oNhapSoPhong.getText().trim();
                    if (!thanhCuon.isEmpty() && Character.isDigit(thanhCuon.charAt(0))) {
                        int bangHienTai = Character.getNumericValue(thanhCuon.charAt(0));
                        if (bangHienTai >= 1 && bangHienTai <= 9) {
                            vongQuayTang.setValue(bangHienTai);
                        }
                    }
                }
            }
        });

        // Điền dữ liệu nếu là Edit
        if (dangSua) {
            oNhapSoPhong.setText(this.phong.getSoPhong());
            int bangHienTai = this.phong.getTang() > 0 ? this.phong.getTang() : 1;
            vongQuayTang.setValue(bangHienTai);

            // Chọn đúng loại phòng
            if (danhSachLoaiPhong != null) {
                for (int chiSo = 0; chiSo < hopChonLoaiPhong.getItemCount(); chiSo++) {
                    LoaiPhong loai = hopChonLoaiPhong.getItemAt(chiSo);
                    if (loai.getMaLoaiPhong() == this.phong.getMaLoaiPhong()) {
                        hopChonLoaiPhong.setSelectedIndex(chiSo);
                        break;
                    }
                }
            }

            DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#.##");
            oNhapGiaMoiDem.setText(dinhDangTienTeChinh.format(this.phong.getGiaMoiDem()));

            String trangThai = this.phong.getTrangThai();
            if ("AVAILABLE".equalsIgnoreCase(trangThai) || "TRỐNG".equalsIgnoreCase(trangThai)) {
                hopChonTrangThai.setSelectedItem("TRỐNG");
            } else if ("OCCUPIED".equalsIgnoreCase(trangThai) || "CÓ KHÁCH".equalsIgnoreCase(trangThai)) {
                hopChonTrangThai.setSelectedItem("CÓ KHÁCH");
            } else if ("CLEANING".equalsIgnoreCase(trangThai) || "ĐANG DỌN".equalsIgnoreCase(trangThai)) {
                hopChonTrangThai.setSelectedItem("ĐANG DỌN");
            } else if ("MAINTENANCE".equalsIgnoreCase(trangThai) || "BẢO TRÌ".equalsIgnoreCase(trangThai)) {
                hopChonTrangThai.setSelectedItem("BẢO TRÌ");
            }

            oNhapMoTa.setText(this.phong.getMoTa() != null ? this.phong.getMoTa() : "");
        } else {
            oNhapGiaMoiDem.setText("300000");
        }

        // Footer Buttons
        JPanel khungChan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        khungChan.setBackground(new Color(245, 247, 250));
        khungChan.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        btHuy = new JButton("Hủy bỏ");
        btHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btHuy.setPreferredSize(new Dimension(100, 34));
        btHuy.setFocusPainted(false);
        btHuy.addActionListener(ac -> dispose());

        btLuu = new JButton(dangSua ? "Cập nhật" : "Thêm mới");
        btLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btLuu.setBackground(new Color(13, 110, 253));
        btLuu.setForeground(Color.WHITE);
        btLuu.setPreferredSize(new Dimension(120, 34));
        btLuu.setFocusPainted(false);
        btLuu.addActionListener(ac -> xuLyLuu());

        khungChan.add(btHuy);
        khungChan.add(btLuu);
        add(khungChan, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String soPhong = oNhapSoPhong.getText().trim();
        if (soPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapSoPhong.requestFocus();
            return;
        }

        LoaiPhong loaiDaChon = (LoaiPhong) hopChonLoaiPhong.getSelectedItem();
        if (loaiDaChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        float gia;
        try {
            String giaStr = oNhapGiaMoiDem.getText().trim().replace(",", "").replace(".", "");
            gia = Float.parseFloat(giaStr);
            if (gia < 0) {
                JOptionPane.showMessageDialog(this, "Giá mỗi đêm không được âm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                oNhapGiaMoiDem.requestFocus();
                return;
            }
        } catch (NumberFormatException ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Giá mỗi đêm phải là số hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapGiaMoiDem.requestFocus();
            return;
        }

        int tang = (int) vongQuayTang.getValue();
        String trangThaiChon = (String) hopChonTrangThai.getSelectedItem();
        String moTa = oNhapMoTa.getText().trim();

        // Gán vào đối tượng room
        this.phong.setSoPhong(soPhong);
        this.phong.setTang(tang);
        this.phong.setMaLoaiPhong(loaiDaChon.getMaLoaiPhong());
        this.phong.setTenLoaiPhong(loaiDaChon.getTenLoaiPhong());
        this.phong.setGiaMoiDem(gia);
        this.phong.setTrangThai(trangThaiChon);
        this.phong.setMoTa(moTa);

        daLuu = true;
        dispose();
    }

    public boolean isSaved() {
        return daLuu;
    }

    public Phong getRoom() {
        return phong;
    }
}
