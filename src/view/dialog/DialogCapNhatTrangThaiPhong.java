package view.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JOptionPane;
import model.Phong;
import service.PhongService;

public class DialogCapNhatTrangThaiPhong extends javax.swing.JDialog {

    private Phong phong;
    private PhongService dichVuPhong = new PhongService();
    private Runnable onThanhCong;
    private boolean daCapNhat = false;

    public DialogCapNhatTrangThaiPhong(Window cha, Phong phong, Runnable onThanhCong) {
        super(cha, "Cập nhật trạng thái - Phòng " + (phong != null ? phong.getSoPhong() : ""),
                ModalityType.APPLICATION_MODAL);
        this.phong = phong;
        this.onThanhCong = onThanhCong;

        initComponents();
        view.AppIcon.apply(this);

        khoiTaoDuLieu();
        setLocationRelativeTo(cha);
    }

    private void khoiTaoDuLieu() {
        if (phong == null)
            return;

        nhanThongTinPhong.setText("Phòng " + phong.getSoPhong() + " ("
                + (phong.getTenLoaiPhong() != null ? phong.getTenLoaiPhong() : "Tiêu chuẩn") + ")");

        String trangThaiHienTai = phong.getTrangThai() != null ? phong.getTrangThai().toUpperCase() : "AVAILABLE";
        if (trangThaiHienTai.contains("AVAILABLE") || trangThaiHienTai.contains("TRỐNG")
                || trangThaiHienTai.contains("TRONG")) {
            nhanTrangThaiHienTai.setText("TRỐNG");
            nhanTrangThaiHienTai.setForeground(new Color(39, 174, 96));
            hopChonTrangThaiMoi.setSelectedIndex(0);
        } else if (trangThaiHienTai.contains("CLEANING") || trangThaiHienTai.contains("ĐANG DỌN")
                || trangThaiHienTai.contains("DỌN")) {
            nhanTrangThaiHienTai.setText("ĐANG DỌN DẸP");
            nhanTrangThaiHienTai.setForeground(new Color(211, 84, 0));
            hopChonTrangThaiMoi.setSelectedIndex(0); // Mặc định gợi ý chuyển sang AVAILABLE
        } else if (trangThaiHienTai.contains("MAINTENANCE") || trangThaiHienTai.contains("BẢO TRÌ")) {
            nhanTrangThaiHienTai.setText("BẢO TRÌ / SỬA CHỮA");
            nhanTrangThaiHienTai.setForeground(new Color(127, 140, 141));
            hopChonTrangThaiMoi.setSelectedIndex(0); // Mặc định gợi ý chuyển sang AVAILABLE
        } else {
            nhanTrangThaiHienTai.setText(trangThaiHienTai);
        }

        btCapNhat.addActionListener(e -> xuLyCapNhat());
        btDong.addActionListener(e -> dispose());
    }

    private void xuLyCapNhat() {
        int chon = hopChonTrangThaiMoi.getSelectedIndex();
        String trangThaiMoi = "AVAILABLE";
        String tenTrangThai = "TRỐNG";

        if (chon == 1) {
            trangThaiMoi = "CLEANING";
            tenTrangThai = "ĐANG DỌN DẸP";
        } else if (chon == 2) {
            trangThaiMoi = "MAINTENANCE";
            tenTrangThai = "BẢO TRÌ";
        }

        dichVuPhong.doiTrangThai(phong.getMaPhong(), trangThaiMoi);
        daCapNhat = true;

        JOptionPane.showMessageDialog(this,
                "Phòng " + phong.getSoPhong() + " đã được chuyển sang trạng thái: " + tenTrangThai,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

        if (onThanhCong != null) {
            onThanhCong.run();
        }
        dispose();
    }

    public boolean isDaCapNhat() {
        return daCapNhat;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        khungTieuDe = new javax.swing.JPanel();
        nhanTieuDe = new javax.swing.JLabel();
        nhanMoTa = new javax.swing.JLabel();
        nhanPhong = new javax.swing.JLabel();
        nhanThongTinPhong = new javax.swing.JLabel();
        nhanTrangThaiHienTaiTieuDe = new javax.swing.JLabel();
        nhanTrangThaiHienTai = new javax.swing.JLabel();
        nhanTrangThaiMoiTieuDe = new javax.swing.JLabel();
        hopChonTrangThaiMoi = new javax.swing.JComboBox<>();
        nhanGhiChu = new javax.swing.JLabel();
        oNhapGhiChu = new javax.swing.JTextField();
        btCapNhat = new javax.swing.JButton();
        btDong = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cập nhật trạng thái phòng");
        setResizable(false);

        khungChinh.setBackground(new java.awt.Color(255, 255, 255));

        khungTieuDe.setBackground(new java.awt.Color(24, 44, 97));

        nhanTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        nhanTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        nhanTieuDe.setText("CẬP NHẬT TRẠNG THÁI PHÒNG");

        nhanMoTa.setForeground(new java.awt.Color(220, 230, 242));
        nhanMoTa.setText("Chuyển trạng thái dọn dẹp hoặc bảo trì phòng");

        javax.swing.GroupLayout khungTieuDeLayout = new javax.swing.GroupLayout(khungTieuDe);
        khungTieuDe.setLayout(khungTieuDeLayout);
        khungTieuDeLayout.setHorizontalGroup(
                khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(khungTieuDeLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(nhanTieuDe)
                                                .addComponent(nhanMoTa))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        khungTieuDeLayout.setVerticalGroup(
                khungTieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(khungTieuDeLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(nhanTieuDe)
                                .addGap(4, 4, 4)
                                .addComponent(nhanMoTa)
                                .addContainerGap(14, Short.MAX_VALUE)));

        nhanPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanPhong.setText("Phòng:");

        nhanThongTinPhong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanThongTinPhong.setForeground(new java.awt.Color(24, 44, 97));
        nhanThongTinPhong.setText("Phòng 101");

        nhanTrangThaiHienTaiTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTrangThaiHienTaiTieuDe.setText("Trạng thái hiện tại:");

        nhanTrangThaiHienTai.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTrangThaiHienTai.setText("Đang dọn dẹp (CLEANING)");

        nhanTrangThaiMoiTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTrangThaiMoiTieuDe.setText("Chuyển sang (*):");

        hopChonTrangThaiMoi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hopChonTrangThaiMoi.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Trống - Sẵn sàng", "Đang dọn dẹp", "Bảo trì/Sửa chữa" }));
        hopChonTrangThaiMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hopChonTrangThaiMoiActionPerformed(evt);
            }
        });

        nhanGhiChu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanGhiChu.setText("Ghi chú:");

        oNhapGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btCapNhat.setBackground(new java.awt.Color(39, 174, 96));
        btCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btCapNhat.setForeground(new java.awt.Color(255, 255, 255));
        btCapNhat.setText("Cập nhật");
        btCapNhat.setFocusPainted(false);

        btDong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btDong.setText("Đóng");
        btDong.setFocusPainted(false);

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
                khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(khungTieuDe, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(khungChinhLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(khungChinhLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(khungChinhLayout.createSequentialGroup()
                                                .addComponent(btDong, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(khungChinhLayout.createSequentialGroup()
                                                .addGroup(khungChinhLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(nhanPhong)
                                                        .addComponent(nhanTrangThaiHienTaiTieuDe)
                                                        .addComponent(nhanTrangThaiMoiTieuDe)
                                                        .addComponent(nhanGhiChu))
                                                .addGap(20, 20, 20)
                                                .addGroup(khungChinhLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(nhanThongTinPhong,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(nhanTrangThaiHienTai,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(hopChonTrangThaiMoi, 0, 260, Short.MAX_VALUE)
                                                        .addComponent(oNhapGhiChu))))
                                .addContainerGap(25, Short.MAX_VALUE)));
        khungChinhLayout.setVerticalGroup(
                khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(khungChinhLayout.createSequentialGroup()
                                .addComponent(khungTieuDe, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(
                                        khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nhanPhong)
                                                .addComponent(nhanThongTinPhong, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(khungChinhLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nhanTrangThaiHienTaiTieuDe)
                                        .addComponent(nhanTrangThaiHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(khungChinhLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nhanTrangThaiMoiTieuDe)
                                        .addComponent(hopChonTrangThaiMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(
                                        khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nhanGhiChu)
                                                .addComponent(oNhapGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(
                                        khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btDong, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hopChonTrangThaiMoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_hopChonTrangThaiMoiActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_hopChonTrangThaiMoiActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCapNhat;
    private javax.swing.JButton btDong;
    private javax.swing.JComboBox<String> hopChonTrangThaiMoi;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungTieuDe;
    private javax.swing.JLabel nhanGhiChu;
    private javax.swing.JLabel nhanMoTa;
    private javax.swing.JLabel nhanPhong;
    private javax.swing.JLabel nhanThongTinPhong;
    private javax.swing.JLabel nhanTieuDe;
    private javax.swing.JLabel nhanTrangThaiHienTai;
    private javax.swing.JLabel nhanTrangThaiHienTaiTieuDe;
    private javax.swing.JLabel nhanTrangThaiMoiTieuDe;
    private javax.swing.JTextField oNhapGhiChu;
    // End of variables declaration//GEN-END:variables
}
