package view.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import model.KhachHang;
import model.Phong;
import service.DatPhongService;
import service.KhachHangService;

public class DialogDatPhongNhanh extends javax.swing.JDialog {

    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DecimalFormat dinhDangTienTe = new DecimalFormat("#,### đ");

    private Phong phong;
    private DatPhongService dichVuDatPhong = new DatPhongService();
    private KhachHangService dichVuKhachHang = new KhachHangService();
    private Runnable onThanhCong;
    private boolean daLuu = false;

    public DialogDatPhongNhanh(Window cha, Phong phong, Runnable onThanhCong) {
        super(cha, "Đặt phòng / Check-in - Phòng " + (phong != null ? phong.getSoPhong() : ""), ModalityType.APPLICATION_MODAL);
        this.phong = phong;
        this.onThanhCong = onThanhCong;

        initComponents();
        view.AppIcon.apply(this);

        khoiTaoDuLieu();
        setLocationRelativeTo(cha);
    }

    private void khoiTaoDuLieu() {
        if (phong == null) return;

        nhanTieuDe.setText("ĐẶT PHÒNG NHANH: PHÒNG " + phong.getSoPhong());
        String loai = (phong.getTenLoaiPhong() != null && !phong.getTenLoaiPhong().isEmpty()) ? phong.getTenLoaiPhong() : "Tiêu chuẩn";
        nhanMoTa.setText("Loại: " + loai + "  |  Đơn giá: " + dinhDangTienTe.format(phong.getGiaMoiDem()) + "/đêm");

        oNhapNgayNhan.setEditor(new javax.swing.JSpinner.DateEditor(oNhapNgayNhan, "yyyy-MM-dd"));
        oNhapNgayTra.setEditor(new javax.swing.JSpinner.DateEditor(oNhapNgayTra, "yyyy-MM-dd"));
        LocalDate homNay = LocalDate.now();
        LocalDate ngayMai = homNay.plusDays(1);
        oNhapNgayNhan.setValue(java.sql.Date.valueOf(homNay));
        oNhapNgayTra.setValue(java.sql.Date.valueOf(ngayMai));
        oNhapGhiChu.setText("Khách đặt trực tiếp");

        int sucChua = Math.max(1, phong.getSucChua());
        vongQuaySoNguoi.setModel(new SpinnerNumberModel(1, 1, sucChua, 1));

        capNhatTamTinh();

        // Lắng nghe đổi ngày để tính lại tiền
        oNhapNgayNhan.addChangeListener(e -> capNhatTamTinh());
        oNhapNgayTra.addChangeListener(e -> capNhatTamTinh());

        btKiemTraCCCD.addActionListener(e -> kiemTraCCCD());
        btNhanPhong.addActionListener(e -> xuLyNhanPhong());
        btHuy.addActionListener(e -> dispose());
    }

    private void capNhatTamTinh() {
        try {
            java.util.Date uNhan = (java.util.Date) oNhapNgayNhan.getValue();
            java.util.Date uTra = (java.util.Date) oNhapNgayTra.getValue();
            LocalDate ngNhan = new Date(uNhan.getTime()).toLocalDate();
            LocalDate ngTra = new Date(uTra.getTime()).toLocalDate();
            long dem = ChronoUnit.DAYS.between(ngNhan, ngTra);
            if (dem <= 0) dem = 1;
            nhanTamTinh.setText(dinhDangTienTe.format(dem * phong.getGiaMoiDem()) + " (" + dem + " đêm)");
        } catch (Exception ex) {
            nhanTamTinh.setText(dinhDangTienTe.format(phong.getGiaMoiDem()) + " (1 đêm)");
        }
    }

    private void kiemTraCCCD() {
        String cccd = oNhapCCCD.getText().trim();
        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD cần tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KhachHang tim = new KhachHang();
        tim.setSoCCCD(cccd);
        KhachHang timThay = dichVuKhachHang.timKiem(tim);
        if (timThay != null) {
            oNhapHoTen.setText(timThay.getNameKhachHang());
            oNhapSDT.setText(timThay.getSoDienThoai());
            oNhapDiaChi.setText(timThay.getDiaChi());
            JOptionPane.showMessageDialog(this, "Tìm thấy khách quen: " + timThay.getNameKhachHang(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin khách này. Vui lòng nhập họ tên và SĐT để tạo mới.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            oNhapHoTen.requestFocus();
        }
    }

    private void xuLyNhanPhong() {
        String cccd = oNhapCCCD.getText().trim();
        String hoTen = oNhapHoTen.getText().trim();
        String sdt = oNhapSDT.getText().trim();

        if (cccd.isEmpty() || hoTen.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ CCCD, Họ tên và Số điện thoại khách hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date ngayNhan;
        Date ngayTra;
        try {
            java.util.Date uNhan = (java.util.Date) oNhapNgayNhan.getValue();
            java.util.Date uTra = (java.util.Date) oNhapNgayTra.getValue();
            ngayNhan = new Date(uNhan.getTime());
            ngayTra = new Date(uTra.getTime());
            if (ngayNhan.toLocalDate().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Ngày nhận phòng phải từ hôm nay trở đi!", "Ngày nhận không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ngayTra.after(ngayNhan)) {
                JOptionPane.showMessageDialog(this, "Ngày trả phòng phải sau ngày nhận phòng!", "Lỗi ngày tháng", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận và ngày trả hợp lệ!", "Lỗi ngày tháng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang tim = new KhachHang();
        tim.setSoCCCD(cccd);
        KhachHang khachHang = dichVuKhachHang.timKiem(tim);
        int maKhachHang;
        if (khachHang == null) {
            KhachHang moi = new KhachHang(0, hoTen, cccd, sdt, "", oNhapDiaChi.getText().trim());
            dichVuKhachHang.them(moi);
            KhachHang khachHangMoi = dichVuKhachHang.timKiem(tim);
            if (khachHangMoi != null) {
                maKhachHang = khachHangMoi.getIdKhachHang();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lưu thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            maKhachHang = khachHang.getIdKhachHang();
        }

        int soNguoi = (int) vongQuaySoNguoi.getValue();
        int maDatPhong = dichVuDatPhong.datPhongNhanh(maKhachHang, ngayNhan, ngayTra, "CHECKED_IN",
                oNhapGhiChu.getText().trim(), phong.getMaPhong(), phong.getGiaMoiDem(), soNguoi);

        if (maDatPhong > 0) {
            daLuu = true;
            JOptionPane.showMessageDialog(this,
                    "Nhận phòng thành công cho khách: " + hoTen + "\nPhòng " + phong.getSoPhong()
                            + " đã chuyển sang trạng thái CÓ KHÁCH (OCCUPIED).",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (onThanhCong != null) {
                onThanhCong.run();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Đặt phòng thất bại. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isDaLuu() {
        return daLuu;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        khungTieuDe = new javax.swing.JPanel();
        nhanTieuDe = new javax.swing.JLabel();
        nhanMoTa = new javax.swing.JLabel();
        nhanCCCD = new javax.swing.JLabel();
        oNhapCCCD = new javax.swing.JTextField();
        btKiemTraCCCD = new javax.swing.JButton();
        nhanHoTen = new javax.swing.JLabel();
        oNhapHoTen = new javax.swing.JTextField();
        nhanSDT = new javax.swing.JLabel();
        oNhapSDT = new javax.swing.JTextField();
        nhanDiaChi = new javax.swing.JLabel();
        oNhapDiaChi = new javax.swing.JTextField();
        nhanNgayNhan = new javax.swing.JLabel();
        oNhapNgayNhan = new javax.swing.JSpinner();
        nhanNgayTra = new javax.swing.JLabel();
        oNhapNgayTra = new javax.swing.JSpinner();
        nhanSoNguoi = new javax.swing.JLabel();
        vongQuaySoNguoi = new javax.swing.JSpinner();
        nhanGhiChu = new javax.swing.JLabel();
        oNhapGhiChu = new javax.swing.JTextField();
        nhanTamTinhTieuDe = new javax.swing.JLabel();
        nhanTamTinh = new javax.swing.JLabel();
        btNhanPhong = new javax.swing.JButton();
        btHuy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Đặt phòng nhanh");
        setResizable(false);

        khungChinh.setBackground(new java.awt.Color(255, 255, 255));

        khungTieuDe.setBackground(new java.awt.Color(30, 132, 73));

        nhanTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        nhanTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        nhanTieuDe.setText("ĐẶT PHÒNG NHANH");

        nhanMoTa.setForeground(new java.awt.Color(234, 250, 234));
        nhanMoTa.setText("Thông tin phòng và giá tiền");

        javax.swing.GroupLayout khungTieuDeLayout = new javax.swing.GroupLayout(khungTieuDe);
        khungTieuDe.setLayout(khungTieuDeLayout);
        khungTieuDeLayout.setHorizontalGroup(
            khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungTieuDeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDe)
                    .addComponent(nhanMoTa))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        khungTieuDeLayout.setVerticalGroup(
            khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungTieuDeLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(nhanTieuDe)
                .addGap(4, 4, 4)
                .addComponent(nhanMoTa)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        nhanCCCD.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanCCCD.setText("Số CCCD (*):");

        oNhapCCCD.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btKiemTraCCCD.setText("Tìm KH");
        btKiemTraCCCD.setFocusPainted(false);

        nhanHoTen.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanHoTen.setText("Họ tên khách (*):");

        oNhapHoTen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanSDT.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanSDT.setText("Số điện thoại (*):");

        oNhapSDT.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanDiaChi.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanDiaChi.setText("Địa chỉ:");

        oNhapDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

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

        nhanGhiChu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanGhiChu.setText("Ghi chú:");

        oNhapGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        nhanTamTinhTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTamTinhTieuDe.setText("Tạm tính:");

        nhanTamTinh.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTamTinh.setForeground(new java.awt.Color(41, 128, 185));
        nhanTamTinh.setText("0 đ");

        btNhanPhong.setBackground(new java.awt.Color(39, 174, 96));
        btNhanPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btNhanPhong.setForeground(new java.awt.Color(255, 255, 255));
        btNhanPhong.setText("Nhận phòng");
        btNhanPhong.setFocusPainted(false);

        btHuy.setBackground(new java.awt.Color(204, 0, 51));
        btHuy.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btHuy.setForeground(new java.awt.Color(255, 255, 255));
        btHuy.setText("Hủy");
        btHuy.setFocusPainted(false);

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungTieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanCCCD)
                    .addComponent(nhanHoTen)
                    .addComponent(nhanSDT)
                    .addComponent(nhanDiaChi)
                    .addComponent(nhanNgayNhan)
                    .addComponent(nhanNgayTra)
                    .addComponent(nhanSoNguoi)
                    .addComponent(nhanGhiChu)
                    .addComponent(nhanTamTinhTieuDe))
                .addGap(20, 20, 20)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, khungChinhLayout.createSequentialGroup()
                        .addComponent(oNhapCCCD, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btKiemTraCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(oNhapHoTen, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapSDT, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapDiaChi, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapNgayNhan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapNgayTra, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vongQuaySoNguoi, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapGhiChu, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTamTinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btNhanPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addComponent(khungTieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanCCCD)
                    .addComponent(oNhapCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btKiemTraCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanHoTen)
                    .addComponent(oNhapHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanSDT)
                    .addComponent(oNhapSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanDiaChi)
                    .addComponent(oNhapDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanNgayNhan)
                    .addComponent(oNhapNgayNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanNgayTra)
                    .addComponent(oNhapNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanSoNguoi)
                    .addComponent(vongQuaySoNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanGhiChu)
                    .addComponent(oNhapGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanTamTinhTieuDe)
                    .addComponent(nhanTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btNhanPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btHuy;
    private javax.swing.JButton btKiemTraCCCD;
    private javax.swing.JButton btNhanPhong;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungTieuDe;
    private javax.swing.JLabel nhanCCCD;
    private javax.swing.JLabel nhanDiaChi;
    private javax.swing.JLabel nhanGhiChu;
    private javax.swing.JLabel nhanHoTen;
    private javax.swing.JLabel nhanMoTa;
    private javax.swing.JLabel nhanNgayNhan;
    private javax.swing.JLabel nhanNgayTra;
    private javax.swing.JLabel nhanSDT;
    private javax.swing.JLabel nhanSoNguoi;
    private javax.swing.JLabel nhanTamTinh;
    private javax.swing.JLabel nhanTamTinhTieuDe;
    private javax.swing.JLabel nhanTieuDe;
    private javax.swing.JTextField oNhapCCCD;
    private javax.swing.JTextField oNhapDiaChi;
    private javax.swing.JTextField oNhapGhiChu;
    private javax.swing.JTextField oNhapHoTen;
    private javax.swing.JSpinner oNhapNgayNhan;
    private javax.swing.JSpinner oNhapNgayTra;
    private javax.swing.JTextField oNhapSDT;
    private javax.swing.JSpinner vongQuaySoNguoi;
    // End of variables declaration//GEN-END:variables
}

