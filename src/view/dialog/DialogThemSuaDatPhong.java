package view.dialog;

import java.awt.Component;
import java.awt.Window;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import java.time.format.DateTimeFormatter;
import dao.KhachHangDAO;
import dao.PhongDAO;
import model.DatPhong;
import model.KhachHang;
import model.Phong;
import service.DatPhongService;

public class DialogThemSuaDatPhong extends javax.swing.JDialog {

    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private boolean daLuu = false;
    private DatPhong datPhong;
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private PhongDAO phongDAO = new PhongDAO();
    private DatPhongService dichVuDatPhong = new DatPhongService();

    private int maKhachHangDaChon = 0;
    private int maPhongDaChon = 0;
    private float giaPhongDaChon = 0;
    private final boolean dangSua;

    public DialogThemSuaDatPhong(Window doiTuongCha, DatPhong datPhongCanSua) {
        super(doiTuongCha, datPhongCanSua == null ? "Đặt phòng mới" : "Chỉnh sửa đơn đặt phòng #" + datPhongCanSua.getIdDatPhong(),
                ModalityType.APPLICATION_MODAL);
        initComponents();
        view.AppIcon.apply(this);
        this.datPhong = datPhongCanSua != null ? datPhongCanSua : new DatPhong();
        this.dangSua = (datPhongCanSua != null && datPhongCanSua.getIdDatPhong() > 0);

        // Load danh sách phòng
        ArrayList<Phong> tatCaPhong = phongDAO.xuat();
        for (Phong hangHienTai : tatCaPhong) {
            boolean dangTrong = "AVAILABLE".equalsIgnoreCase(hangHienTai.getTrangThai()) || "TRỐNG".equalsIgnoreCase(hangHienTai.getTrangThai());
            if (dangTrong || (dangSua && hangHienTai.getSoPhong().equals(this.datPhong.getSoPhong()))) {
                hopChonPhong.addItem(hangHienTai);
            }
        }
        hopChonPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                if (giaTri instanceof Phong) {
                    Phong phong = (Phong) giaTri;
                    setText("Phòng " + phong.getSoPhong() + " - " + phong.getTenLoaiPhong()
                            + " (Tầng " + phong.getTang() + ", tối đa " + phong.getSucChua() + " khách)");
                }
                return this;
            }
        });

        oNhapNgayNhan.setEditor(new javax.swing.JSpinner.DateEditor(oNhapNgayNhan, "yyyy-MM-dd"));
        oNhapNgayTra.setEditor(new javax.swing.JSpinner.DateEditor(oNhapNgayTra, "yyyy-MM-dd"));
        LocalDate homNay = LocalDate.now();
        LocalDate ngayMai = homNay.plusDays(1);
        oNhapNgayNhan.setValue(java.sql.Date.valueOf(homNay));
        oNhapNgayTra.setValue(java.sql.Date.valueOf(ngayMai));

        hopChonTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(dangSua
                ? new String[] { "BOOKED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED" }
                : new String[] { "BOOKED" }));
        hopChonTrangThai.setEnabled(dangSua);
        hopChonTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                String chuoi = String.valueOf(giaTri);
                if ("BOOKED".equalsIgnoreCase(chuoi)) setText("Đã đặt");
                else if ("CHECKED_IN".equalsIgnoreCase(chuoi)) setText("Đã nhận phòng");
                else if ("CHECKED_OUT".equalsIgnoreCase(chuoi)) setText("Đã trả phòng");
                else if ("CANCELLED".equalsIgnoreCase(chuoi)) setText("Đã hủy");
                return this;
            }
        });

        if (dangSua) {
            nhanTieuDe.setText("CHỈNH SỬA ĐƠN ĐẶT PHÒNG #" + this.datPhong.getIdDatPhong());
            nhanPhu.setText("Cập nhật ngày nhận/trả, số người, trạng thái hoặc phòng");
            btLuu.setText("Cập nhật");

            oNhapCCCD.setText(this.datPhong.getCccd() != null ? this.datPhong.getCccd() : "");
            if (this.datPhong.getNgayNhanPhong() != null) {
                oNhapNgayNhan.setValue(new java.util.Date(this.datPhong.getNgayNhanPhong().getTime()));
            }
            if (this.datPhong.getNgayTraPhong() != null) {
                oNhapNgayTra.setValue(new java.util.Date(this.datPhong.getNgayTraPhong().getTime()));
            }
            oNhapGhiChu.setText(this.datPhong.getGhiChu() != null ? this.datPhong.getGhiChu() : "");

            String luongTrinh = this.datPhong.getTrangThai();
            if (luongTrinh != null) {
                if (luongTrinh.contains("CHECKED_IN") || luongTrinh.contains("Đã nhận")) hopChonTrangThai.setSelectedItem("CHECKED_IN");
                else if (luongTrinh.contains("CHECKED_OUT") || luongTrinh.contains("Đã trả")) hopChonTrangThai.setSelectedItem("CHECKED_OUT");
                else if (luongTrinh.contains("CANCELLED") || luongTrinh.contains("Đã hủy")) hopChonTrangThai.setSelectedItem("CANCELLED");
                else hopChonTrangThai.setSelectedItem("BOOKED");
            }

            if (this.datPhong.getSoPhong() != null) {
                for (int chiSo = 0; chiSo < hopChonPhong.getItemCount(); chiSo++) {
                    Phong hangHienTai = hopChonPhong.getItemAt(chiSo);
                    if (hangHienTai.getSoPhong().equals(this.datPhong.getSoPhong())) {
                        hopChonPhong.setSelectedIndex(chiSo);
                        break;
                    }
                }
            }
            capNhatSucChuaPhong();
            int sucChua = phongDangChon() != null ? Math.max(1, phongDangChon().getSucChua()) : 1;
            vongQuaySoNguoi.setValue(Math.min(Math.max(1, this.datPhong.getSoNguoi()), sucChua));

            if (this.datPhong.getIdKhachHang() > 0) {
                KhachHang timThu = new KhachHang();
                timThu.setIdKhachHang(this.datPhong.getIdKhachHang());
                KhachHang timThay = khachHangDAO.timKiem(timThu);
                if (timThay != null) {
                    maKhachHangDaChon = timThay.getIdKhachHang();
                    oNhapHoTen.setText(timThay.getNameKhachHang());
                    oNhapSDT.setText(timThay.getSoDienThoai());
                    if (oNhapCCCD.getText().isEmpty()) {
                        oNhapCCCD.setText(timThay.getSoCCCD());
                    }
                }
            }
        } else {
            capNhatSucChuaPhong();
        }

        setLocationRelativeTo(doiTuongCha);
    }

    private Phong phongDangChon() {
        return (Phong) hopChonPhong.getSelectedItem();
    }

    private void capNhatSucChuaPhong() {
        Phong chon = phongDangChon();
        if (chon != null) {
            int sucChua = Math.max(1, chon.getSucChua());
            vongQuaySoNguoi.setModel(new SpinnerNumberModel(1, 1, sucChua, 1));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        khungTieuDe = new javax.swing.JPanel();
        nhanTieuDe = new javax.swing.JLabel();
        nhanPhu = new javax.swing.JLabel();
        nhanCCCD = new javax.swing.JLabel();
        oNhapCCCD = new javax.swing.JTextField();
        btKiemTraCCCD = new javax.swing.JButton();
        nhanHoTen = new javax.swing.JLabel();
        oNhapHoTen = new javax.swing.JTextField();
        nhanSDT = new javax.swing.JLabel();
        oNhapSDT = new javax.swing.JTextField();
        nhanPhong = new javax.swing.JLabel();
        hopChonPhong = new javax.swing.JComboBox<>();
        nhanNgayNhan = new javax.swing.JLabel();
        oNhapNgayNhan = new javax.swing.JSpinner();
        nhanNgayTra = new javax.swing.JLabel();
        oNhapNgayTra = new javax.swing.JSpinner();
        nhanSoNguoi = new javax.swing.JLabel();
        vongQuaySoNguoi = new javax.swing.JSpinner();
        nhanTrangThai = new javax.swing.JLabel();
        hopChonTrangThai = new javax.swing.JComboBox<>();
        nhanGhiChu = new javax.swing.JLabel();
        oNhapGhiChu = new javax.swing.JTextField();
        btHuy = new javax.swing.JButton();
        btLuu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin đặt phòng");
        setResizable(false);

        khungChinh.setBackground(new java.awt.Color(255, 255, 255));

        khungTieuDe.setBackground(new java.awt.Color(30, 60, 114));

        nhanTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        nhanTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        nhanTieuDe.setText("TẠO ĐƠN ĐặT PHÒNG MỚI");

        nhanPhu.setForeground(new java.awt.Color(220, 230, 242));
        nhanPhu.setText("Nhập thông tin khách hàng và chọn phòng lưu trú");

        javax.swing.GroupLayout khungTieuDeLayout = new javax.swing.GroupLayout(khungTieuDe);
        khungTieuDe.setLayout(khungTieuDeLayout);
        khungTieuDeLayout.setHorizontalGroup(
            khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungTieuDeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDe)
                    .addComponent(nhanPhu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        khungTieuDeLayout.setVerticalGroup(
            khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungTieuDeLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(nhanTieuDe)
                .addGap(4, 4, 4)
                .addComponent(nhanPhu)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        nhanCCCD.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanCCCD.setText("Số CCCD (*):");

        oNhapCCCD.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btKiemTraCCCD.setBackground(new java.awt.Color(255, 255, 254));
        btKiemTraCCCD.setText("Tìm");
        btKiemTraCCCD.setFocusPainted(false);
        btKiemTraCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKiemTraCCCDActionPerformed(evt);
            }
        });

        nhanHoTen.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanHoTen.setText("Họ tên khách (*):");

        oNhapHoTen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanSDT.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanSDT.setText("Số điện thoại (*):");

        oNhapSDT.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanPhong.setText("Chọn phòng (*):");

        hopChonPhong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hopChonPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hopChonPhongActionPerformed(evt);
            }
        });

        nhanNgayNhan.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanNgayNhan.setText("Ngày nhận:");

        oNhapNgayNhan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        oNhapNgayNhan.setModel(new javax.swing.SpinnerDateModel());

        nhanNgayTra.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanNgayTra.setText("Ngày trả:");

        oNhapNgayTra.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        oNhapNgayTra.setModel(new javax.swing.SpinnerDateModel());

        nhanSoNguoi.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanSoNguoi.setText("Số người:");

        vongQuaySoNguoi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        vongQuaySoNguoi.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        nhanTrangThai.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTrangThai.setText("Trạng thái:");

        hopChonTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanGhiChu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanGhiChu.setText("Ghi chú:");

        oNhapGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btHuy.setBackground(new java.awt.Color(204, 0, 51));
        btHuy.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btHuy.setForeground(new java.awt.Color(255, 255, 255));
        btHuy.setText("Hủy bỏ");
        btHuy.setFocusPainted(false);
        btHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHuyActionPerformed(evt);
            }
        });

        btLuu.setBackground(new java.awt.Color(13, 110, 253));
        btLuu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btLuu.setForeground(new java.awt.Color(255, 255, 255));
        btLuu.setText("Lưu đặt phòng");
        btLuu.setFocusPainted(false);
        btLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLuuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungTieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanCCCD)
                    .addComponent(nhanHoTen)
                    .addComponent(nhanSDT)
                    .addComponent(nhanPhong)
                    .addComponent(nhanNgayNhan)
                    .addComponent(nhanNgayTra)
                    .addComponent(nhanSoNguoi)
                    .addComponent(nhanTrangThai)
                    .addComponent(nhanGhiChu))
                .addGap(55, 55, 55)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(khungChinhLayout.createSequentialGroup()
                                .addComponent(oNhapCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btKiemTraCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(oNhapHoTen)
                            .addComponent(oNhapSDT)
                            .addComponent(hopChonPhong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(oNhapNgayNhan)
                            .addComponent(oNhapNgayTra)
                            .addComponent(vongQuaySoNguoi)
                            .addComponent(hopChonTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(oNhapGhiChu))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addComponent(khungTieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanCCCD)
                    .addComponent(oNhapCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btKiemTraCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanHoTen)
                    .addComponent(oNhapHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanSDT)
                    .addComponent(oNhapSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanPhong)
                    .addComponent(hopChonPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanNgayNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oNhapNgayNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oNhapNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanSoNguoi)
                    .addComponent(vongQuaySoNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanTrangThai)
                    .addComponent(hopChonTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanGhiChu)
                    .addComponent(oNhapGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btKiemTraCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKiemTraCCCDActionPerformed
        String cccd = oNhapCCCD.getText().trim();
        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD để tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KhachHang taiKhoan = new KhachHang();
        taiKhoan.setSoCCCD(cccd);
        KhachHang timThay = khachHangDAO.timKiem(taiKhoan);
        if (timThay != null) {
            maKhachHangDaChon = timThay.getIdKhachHang();
            oNhapHoTen.setText(timThay.getNameKhachHang());
            oNhapSDT.setText(timThay.getSoDienThoai());
            JOptionPane.showMessageDialog(this, "Tìm thấy khách hàng: " + timThay.getNameKhachHang(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin khách hàng này. Vui lòng nhập họ tên & SĐT để hệ thống tự động lưu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            oNhapHoTen.requestFocus();
        }
    }//GEN-LAST:event_btKiemTraCCCDActionPerformed

    private void hopChonPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hopChonPhongActionPerformed
        capNhatSucChuaPhong();
    }//GEN-LAST:event_hopChonPhongActionPerformed

    private void btHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHuyActionPerformed
        dispose();
    }//GEN-LAST:event_btHuyActionPerformed

    private void btLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLuuActionPerformed
        xuLyLuu();
    }//GEN-LAST:event_btLuuActionPerformed

    private void xuLyLuu() {
        String cccd = oNhapCCCD.getText().trim();
        String hoTen = oNhapHoTen.getText().trim();
        String sdt = oNhapSDT.getText().trim();

        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapCCCD.requestFocus();
            return;
        }
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapHoTen.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapSDT.requestFocus();
            return;
        }

        Phong phongChon = phongDangChon();
        if (phongChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng lưu trú!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ngayNhan;
        LocalDate ngayTra;
        try {
            java.util.Date uNhan = (java.util.Date) oNhapNgayNhan.getValue();
            java.util.Date uTra = (java.util.Date) oNhapNgayTra.getValue();
            ngayNhan = new Date(uNhan.getTime()).toLocalDate();
            ngayTra = new Date(uTra.getTime()).toLocalDate();
        } catch (Exception ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ngayTra.isBefore(ngayNhan) || ngayTra.isEqual(ngayNhan)) {
            JOptionPane.showMessageDialog(this, "Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 ngày!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (maKhachHangDaChon <= 0) {
            KhachHang timThu = new KhachHang();
            timThu.setSoCCCD(cccd);
            KhachHang khTonTai = khachHangDAO.timKiem(timThu);
            if (khTonTai != null) {
                maKhachHangDaChon = khTonTai.getIdKhachHang();
            } else {
                KhachHang khachMoi = new KhachHang(0, hoTen, cccd, sdt, "", "");
                if (khachHangDAO.them(khachMoi) > 0) {
                    KhachHang vuaThem = khachHangDAO.timKiem(khachMoi);
                    if (vuaThem != null) {
                        maKhachHangDaChon = vuaThem.getIdKhachHang();
                    }
                }
            }
        }

        if (maKhachHangDaChon <= 0) {
            JOptionPane.showMessageDialog(this, "Không thể lưu thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.datPhong.setIdKhachHang(maKhachHangDaChon);
        this.datPhong.setMaPhong(phongChon.getMaPhong());
        this.datPhong.setNgayNhanPhong(Date.valueOf(ngayNhan));
        this.datPhong.setNgayTraPhong(Date.valueOf(ngayTra));
        this.datPhong.setSoNguoi((int) vongQuaySoNguoi.getValue());
        this.datPhong.setGhiChu(oNhapGhiChu.getText().trim());
        this.datPhong.setTrangThai((String) hopChonTrangThai.getSelectedItem());

        daLuu = true;
        dispose();
    }

    public boolean isSaved() {
        return daLuu;
    }

    public DatPhong getBooking() {
        return datPhong;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btHuy;
    private javax.swing.JButton btKiemTraCCCD;
    private javax.swing.JButton btLuu;
    private javax.swing.JComboBox<Phong> hopChonPhong;
    private javax.swing.JComboBox<String> hopChonTrangThai;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungTieuDe;
    private javax.swing.JLabel nhanCCCD;
    private javax.swing.JLabel nhanGhiChu;
    private javax.swing.JLabel nhanHoTen;
    private javax.swing.JLabel nhanNgayNhan;
    private javax.swing.JLabel nhanNgayTra;
    private javax.swing.JLabel nhanPhong;
    private javax.swing.JLabel nhanPhu;
    private javax.swing.JLabel nhanSDT;
    private javax.swing.JLabel nhanSoNguoi;
    private javax.swing.JLabel nhanTieuDe;
    private javax.swing.JLabel nhanTrangThai;
    private javax.swing.JTextField oNhapCCCD;
    private javax.swing.JTextField oNhapGhiChu;
    private javax.swing.JTextField oNhapHoTen;
    private javax.swing.JSpinner oNhapNgayNhan;
    private javax.swing.JSpinner oNhapNgayTra;
    private javax.swing.JTextField oNhapSDT;
    private javax.swing.JSpinner vongQuaySoNguoi;
    // End of variables declaration//GEN-END:variables
}

