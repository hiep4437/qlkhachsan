package view.dialog;

import java.awt.Component;
import java.awt.Window;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import model.DatPhong;
import model.HoaDon;
import service.DatPhongService;
import service.HoaDonService;

public class DialogTraPhongThanhToan extends javax.swing.JDialog {

    private boolean hoanTat = false;
    private DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,### đ");

    private DatPhong datPhong;
    private DatPhongService dichVuDatPhong = new DatPhongService();

    private double tienPhong = 0;
    private double chungThanhToan = 0;

    public DialogTraPhongThanhToan(Window doiTuongCha, DatPhong nut) {
        super(doiTuongCha, "Trả phòng & Xuất hóa đơn thanh toán", ModalityType.APPLICATION_MODAL);
        initComponents();
        view.AppIcon.apply(this);
        this.datPhong = nut;

        if (datPhong != null) {
            String chuoiPhong = (!datPhong.getSoPhong().isEmpty()) ? "Phòng " + datPhong.getSoPhong() : "";
            nhanPhu.setText("Xác nhận tính tiền và kết thúc lượt lưu trú " + chuoiPhong);

            String tenKhach = (!datPhong.getTenKhachHang().isEmpty()) ? datPhong.getTenKhachHang() : "Khách lưu trú";
            String sdt = (!datPhong.getSoDienThoai().isEmpty()) ? datPhong.getSoDienThoai() : "---";
            String cccd = (!datPhong.getCccd().isEmpty()) ? datPhong.getCccd() : "---";
            String soPhong = (!datPhong.getSoPhong().isEmpty()) ? datPhong.getSoPhong() : "---";
            String loaiPhong = (!datPhong.getTenLoaiPhong().isEmpty()) ? datPhong.getTenLoaiPhong() : "Tiêu chuẩn";

            Date ngayNhan = datPhong.getNgayNhanPhong() != null ? datPhong.getNgayNhanPhong() : Date.valueOf(LocalDate.now());
            Date ngayTraHienTai = Date.valueOf(LocalDate.now());

            long soDem = 1;
            try {
                soDem = Math.max(1, ChronoUnit.DAYS.between(ngayNhan.toLocalDate(), ngayTraHienTai.toLocalDate()));
            } catch (Exception biBoQua) {}

            double donGia = datPhong.getGiaMoiDem() > 0 ? datPhong.getGiaMoiDem() : 300000;
            tienPhong = soDem * donGia;

            nhanKhach.setText(tenKhach + " (SĐT: " + sdt + ")");
            nhanCCCD.setText(cccd);
            nhanPhong.setText("Phòng " + soPhong + " (" + loaiPhong + ")");
            nhanNgay.setText(ngayNhan + "  ➔  " + ngayTraHienTai + " (" + soDem + " đêm)");
            nhanDonGia.setText(dinhDangTienTeChinh.format(donGia) + "/đêm");
            nhanTienPhong.setText(dinhDangTienTeChinh.format(tienPhong));
            nhanChungTien.setText(dinhDangTienTeChinh.format(tienPhong));
        }

        // Tự động tính toán khi thay đổi phụ thu, giảm giá
        Runnable capNhatChung = () -> {
            try {
                double phuThu = parseDoubleSafe(oNhapPhuThu.getText());
                double doHoa = parseDoubleSafe(oNhapGiamGia.getText());
                chungThanhToan = Math.max(0, tienPhong + phuThu - doHoa);
                nhanChungTien.setText(dinhDangTienTeChinh.format(chungThanhToan));
            } catch (Exception ngoaiLe) {
                nhanChungTien.setText(dinhDangTienTeChinh.format(tienPhong));
            }
        };

        oNhapPhuThu.addActionListener(ac -> capNhatChung.run());
        oNhapGiamGia.addActionListener(ac -> capNhatChung.run());
        oNhapPhuThu.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent suKien) {
                capNhatChung.run();
            }
        });
        oNhapGiamGia.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent suKien) {
                capNhatChung.run();
            }
        });

        setLocationRelativeTo(doiTuongCha);
    }

    private double parseDoubleSafe(String vanBan) {
        if (vanBan == null) return 0;
        String chuoi = vanBan.replaceAll("[^0-9.]", "").trim();
        try {
            return Double.parseDouble(chuoi);
        } catch (Exception e) {
            return 0;
        }
    }

    private HoaDon taoHoaDonThanhToan() {
        double phuThu = parseDoubleSafe(oNhapPhuThu.getText());
        double giamGia = parseDoubleSafe(oNhapGiamGia.getText());
        double tong = Math.max(0, tienPhong + phuThu - giamGia);

        HoaDon hdTam = new HoaDon();
        hdTam.setIdDatPhong(datPhong.getIdDatPhong());
        hdTam.setTenKhachHang(datPhong.getTenKhachHang());
        hdTam.setSoDienThoai(datPhong.getSoDienThoai());
        hdTam.setSoCCCD(datPhong.getCccd());
        hdTam.setSoPhong(datPhong.getSoPhong());
        hdTam.setTenLoaiPhong(datPhong.getTenLoaiPhong());
        hdTam.setNgayLap(new java.sql.Timestamp(System.currentTimeMillis()));
        hdTam.setTienPhong((float) tienPhong);
        hdTam.setPhuThu((float) phuThu);
        hdTam.setGiamGia((float) giamGia);
        hdTam.setTongTien((float) tong);
        hdTam.setPhuongThucThanhToan((String) hopChonPhuongThuc.getSelectedItem());
        hdTam.setTrangThaiThanhToan("PAID");
        return hdTam;
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
        khungDau = new javax.swing.JPanel();
        nhanTieuDe = new javax.swing.JLabel();
        nhanPhu = new javax.swing.JLabel();
        khungThongTin = new javax.swing.JPanel();
        lblKhach = new javax.swing.JLabel();
        nhanKhach = new javax.swing.JLabel();
        lblCCCD = new javax.swing.JLabel();
        nhanCCCD = new javax.swing.JLabel();
        lblPhong = new javax.swing.JLabel();
        nhanPhong = new javax.swing.JLabel();
        lblNgay = new javax.swing.JLabel();
        nhanNgay = new javax.swing.JLabel();
        lblDonGia = new javax.swing.JLabel();
        nhanDonGia = new javax.swing.JLabel();
        lblTienPhong = new javax.swing.JLabel();
        nhanTienPhong = new javax.swing.JLabel();
        lblPhuThu = new javax.swing.JLabel();
        oNhapPhuThu = new javax.swing.JTextField();
        lblGiamGia = new javax.swing.JLabel();
        oNhapGiamGia = new javax.swing.JTextField();
        lblPhuongThuc = new javax.swing.JLabel();
        hopChonPhuongThuc = new javax.swing.JComboBox<>();
        lblChungTien = new javax.swing.JLabel();
        nhanChungTien = new javax.swing.JLabel();
        nhanGhiChu = new javax.swing.JLabel();
        btInHoaDon = new javax.swing.JButton();
        btHuy = new javax.swing.JButton();
        btXacNhan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Trả phòng & Thanh toán");
        setResizable(false);

        khungChinh.setBackground(new java.awt.Color(255, 255, 255));

        khungDau.setBackground(new java.awt.Color(192, 57, 43));

        nhanTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nhanTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        nhanTieuDe.setText("HÓA ĐƠN THANH TOÁN & TRẢ PHÒNG");

        nhanPhu.setForeground(new java.awt.Color(250, 219, 216));
        nhanPhu.setText("Xác nhận tính tiền và kết thúc lượt lưu trú");

        javax.swing.GroupLayout khungDauLayout = new javax.swing.GroupLayout(khungDau);
        khungDau.setLayout(khungDauLayout);
        khungDauLayout.setHorizontalGroup(
            khungDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungDauLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(khungDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDe)
                    .addComponent(nhanPhu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        khungDauLayout.setVerticalGroup(
            khungDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungDauLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(nhanTieuDe)
                .addGap(4, 4, 4)
                .addComponent(nhanPhu)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        khungThongTin.setBackground(new java.awt.Color(248, 249, 250));

        lblKhach.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblKhach.setText("Khách hàng:");

        nhanKhach.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nhanKhach.setText("---");

        lblCCCD.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCCCD.setText("Số CCCD:");

        nhanCCCD.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nhanCCCD.setText("---");

        lblPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPhong.setText("Phòng lưu trú:");

        nhanPhong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nhanPhong.setText("---");

        lblNgay.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNgay.setText("Thời gian:");

        nhanNgay.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nhanNgay.setText("---");

        lblDonGia.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDonGia.setText("Đơn giá phòng:");

        nhanDonGia.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nhanDonGia.setText("---");

        lblTienPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTienPhong.setText("Tiền phòng:");

        nhanTienPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTienPhong.setForeground(new java.awt.Color(192, 57, 43));
        nhanTienPhong.setText("0 đ");

        javax.swing.GroupLayout khungThongTinLayout = new javax.swing.GroupLayout(khungThongTin);
        khungThongTin.setLayout(khungThongTinLayout);
        khungThongTinLayout.setHorizontalGroup(
            khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungThongTinLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKhach)
                    .addComponent(lblCCCD)
                    .addComponent(lblPhong)
                    .addComponent(lblNgay)
                    .addComponent(lblDonGia)
                    .addComponent(lblTienPhong))
                .addGap(20, 20, 20)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanKhach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanCCCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanPhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanDonGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanTienPhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        khungThongTinLayout.setVerticalGroup(
            khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungThongTinLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKhach)
                    .addComponent(nhanKhach))
                .addGap(6, 6, 6)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCCCD)
                    .addComponent(nhanCCCD))
                .addGap(6, 6, 6)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhong)
                    .addComponent(nhanPhong))
                .addGap(6, 6, 6)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNgay)
                    .addComponent(nhanNgay))
                .addGap(6, 6, 6)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDonGia)
                    .addComponent(nhanDonGia))
                .addGap(6, 6, 6)
                .addGroup(khungThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTienPhong)
                    .addComponent(nhanTienPhong))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        lblPhuThu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPhuThu.setText("Phụ thu (VNĐ):");

        oNhapPhuThu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        oNhapPhuThu.setText("0");

        lblGiamGia.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblGiamGia.setText("Giảm giá (VNĐ):");

        oNhapGiamGia.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        oNhapGiamGia.setText("0");

        lblPhuongThuc.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPhuongThuc.setText("Phương thức:");

        hopChonPhuongThuc.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hopChonPhuongThuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ" }));

        lblChungTien.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblChungTien.setForeground(new java.awt.Color(192, 57, 43));
        lblChungTien.setText("TỔNG THANH TOÁN:");

        nhanChungTien.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        nhanChungTien.setForeground(new java.awt.Color(192, 57, 43));
        nhanChungTien.setText("0 đ");

        nhanGhiChu.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        nhanGhiChu.setForeground(new java.awt.Color(108, 117, 125));
        nhanGhiChu.setText("Lưu ý: Sau khi xác nhận thanh toán, phòng sẽ về trạng thái đang dọn.");

        btInHoaDon.setBackground(new java.awt.Color(255, 255, 254));
        btInHoaDon.setText("In hoá đơn");
        btInHoaDon.setFocusPainted(false);
        btInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInHoaDonActionPerformed(evt);
            }
        });

        btHuy.setBackground(new java.awt.Color(255, 255, 254));
        btHuy.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btHuy.setText("Hủy bỏ");
        btHuy.setFocusPainted(false);
        btHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHuyActionPerformed(evt);
            }
        });

        btXacNhan.setBackground(new java.awt.Color(192, 57, 43));
        btXacNhan.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btXacNhan.setForeground(new java.awt.Color(255, 255, 255));
        btXacNhan.setText("Xác nhận thanh toán");
        btXacNhan.setFocusPainted(false);
        btXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXacNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanGhiChu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(khungThongTin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(khungChinhLayout.createSequentialGroup()
                                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPhuThu)
                                    .addComponent(lblGiamGia)
                                    .addComponent(lblPhuongThuc)
                                    .addComponent(lblChungTien))
                                .addGap(20, 20, 20)
                                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(oNhapPhuThu, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(oNhapGiamGia)
                                    .addComponent(hopChonPhuongThuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nhanChungTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(khungChinhLayout.createSequentialGroup()
                                .addComponent(btInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addComponent(khungDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(khungThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhuThu)
                    .addComponent(oNhapPhuThu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiamGia)
                    .addComponent(oNhapGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhuongThuc)
                    .addComponent(hopChonPhuongThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChungTien)
                    .addComponent(nhanChungTien))
                .addGap(12, 12, 12)
                .addComponent(nhanGhiChu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
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

    private void btHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHuyActionPerformed
        dispose();
    }//GEN-LAST:event_btHuyActionPerformed

    private void btInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInHoaDonActionPerformed
        DialogChiTietHoaDon.inHoaDonThucTe(this, taoHoaDonThanhToan(), false);
    }//GEN-LAST:event_btInHoaDonActionPerformed

    private void btXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXacNhanActionPerformed
        double phuThu = parseDoubleSafe(oNhapPhuThu.getText());
        double doHoa = parseDoubleSafe(oNhapGiamGia.getText());
        double chung = Math.max(0, tienPhong + phuThu - doHoa);
        String phuongThuc = (String) hopChonPhuongThuc.getSelectedItem();

        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận đã nhận đủ số tiền: " + dinhDangTienTeChinh.format(chung) + " (" + phuongThuc + ")?\n"
                        + "Hệ thống sẽ hoàn tất Check-out và chuyển Phòng " + datPhong.getSoPhong() + " sang ĐANG DỌN.",
                "Xác nhận thanh toán & Trả phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (xacNhan != JOptionPane.YES_OPTION) return;

        int maDatPhong = datPhong.getIdDatPhong();
        boolean thanhToanThanhCong = dichVuDatPhong.traPhongVaThanhToan(
                maDatPhong,
                datPhong.getMaPhong(),
                tienPhong,
                phuThu,
                doHoa,
                chung,
                phuongThuc);

        if (thanhToanThanhCong) {
            hoanTat = true;
            HoaDon hoaDon = new HoaDonService().timTheoDatPhong(maDatPhong);
            if (hoaDon == null) {
                hoaDon = taoHoaDonThanhToan();
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thành công, nhưng không tải được hóa đơn đã lưu. Hóa đơn tạm sẽ được dùng để in.",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
            int chonIn = JOptionPane.showConfirmDialog(this,
                    "Trả phòng & thanh toán thành công!\nPhòng đã chuyển sang trạng thái đang dọn.\n\nBạn có muốn in hóa đơn ngay không?",
                    "Thành công",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (chonIn == JOptionPane.YES_OPTION) {
                DialogChiTietHoaDon.inHoaDonThucTe(this, hoaDon, false);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại! Vui lòng kiểm tra lại kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btXacNhanActionPerformed

    public boolean isCompleted() {
        return hoanTat;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btHuy;
    private javax.swing.JButton btInHoaDon;
    private javax.swing.JButton btXacNhan;
    private javax.swing.JComboBox<String> hopChonPhuongThuc;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungDau;
    private javax.swing.JPanel khungThongTin;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblChungTien;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblKhach;
    private javax.swing.JLabel lblNgay;
    private javax.swing.JLabel lblPhong;
    private javax.swing.JLabel lblPhuThu;
    private javax.swing.JLabel lblPhuongThuc;
    private javax.swing.JLabel lblTienPhong;
    private javax.swing.JLabel nhanCCCD;
    private javax.swing.JLabel nhanChungTien;
    private javax.swing.JLabel nhanDonGia;
    private javax.swing.JLabel nhanGhiChu;
    private javax.swing.JLabel nhanKhach;
    private javax.swing.JLabel nhanNgay;
    private javax.swing.JLabel nhanPhong;
    private javax.swing.JLabel nhanPhu;
    private javax.swing.JLabel nhanTienPhong;
    private javax.swing.JLabel nhanTieuDe;
    private javax.swing.JTextField oNhapGiamGia;
    private javax.swing.JTextField oNhapPhuThu;
    // End of variables declaration//GEN-END:variables
}
