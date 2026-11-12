package view.dialog;

import dao.DBConecter;
import java.awt.Color;
import java.awt.Window;
import javax.swing.JOptionPane;

/**
 * Dialog cau hinh ket noi may chu CSDL.
 */
public class DialogCauHinhServer extends javax.swing.JDialog {

    private Runnable onSaved;

    public DialogCauHinhServer(Window parent, Runnable onSaved) {
        super(parent, "C\u1ea5u h\u00ecnh k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7 CSDL", ModalityType.APPLICATION_MODAL);
        this.onSaved = onSaved;
        initComponents();
        view.AppIcon.apply(this);
        setLocationRelativeTo(parent);
        // Pre-fill from current config
        oNhapMayChu.setText(DBConecter.getHost());
        oNhapCong.setText(DBConecter.getPort());
        oNhapCoSoDuLieu.setText(DBConecter.getDatabase());
        oNhapNguoiDung.setText(DBConecter.getUser());
        oNhapMatKhau.setText(DBConecter.getPass());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungNen = new javax.swing.JPanel();
        khungBieuMau = new javax.swing.JPanel();
        lblMayChu = new javax.swing.JLabel();
        oNhapMayChu = new javax.swing.JTextField();
        lblCong = new javax.swing.JLabel();
        oNhapCong = new javax.swing.JTextField();
        lblCoSoDuLieu = new javax.swing.JLabel();
        oNhapCoSoDuLieu = new javax.swing.JTextField();
        lblNguoiDung = new javax.swing.JLabel();
        oNhapNguoiDung = new javax.swing.JTextField();
        lblMatKhau = new javax.swing.JLabel();
        oNhapMatKhau = new javax.swing.JPasswordField();
        nhanTrangThai = new javax.swing.JLabel();
        khungNut = new javax.swing.JPanel();
        btTest = new javax.swing.JButton();
        btLuu = new javax.swing.JButton();
        btMacDinh = new javax.swing.JButton();
        btDong = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cấu hình máy chủ CSDL");
        setResizable(false);

        khungNen.setBackground(new java.awt.Color(245, 247, 250));

        khungBieuMau.setBackground(new java.awt.Color(245, 247, 250));

        lblMayChu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblMayChu.setText("Máy chủ (Host):");

        oNhapMayChu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblCong.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCong.setText("Cổng (Port):");

        oNhapCong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblCoSoDuLieu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCoSoDuLieu.setText("Tên CSDL (Database):");

        oNhapCoSoDuLieu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblNguoiDung.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNguoiDung.setText("Tài khoản (User):");

        oNhapNguoiDung.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblMatKhau.setText("Mật khẩu (Password):");

        oNhapMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        javax.swing.GroupLayout khungBieuMauLayout = new javax.swing.GroupLayout(khungBieuMau);
        khungBieuMau.setLayout(khungBieuMauLayout);
        khungBieuMauLayout.setHorizontalGroup(
            khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungBieuMauLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMayChu)
                    .addComponent(lblCong)
                    .addComponent(lblCoSoDuLieu)
                    .addComponent(lblNguoiDung)
                    .addComponent(lblMatKhau))
                .addGap(12, 12, 12)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oNhapMayChu)
                    .addComponent(oNhapCong)
                    .addComponent(oNhapCoSoDuLieu)
                    .addComponent(oNhapNguoiDung)
                    .addComponent(oNhapMatKhau))
                .addGap(16, 16, 16))
        );
        khungBieuMauLayout.setVerticalGroup(
            khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungBieuMauLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMayChu)
                    .addComponent(oNhapMayChu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCong)
                    .addComponent(oNhapCong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCoSoDuLieu)
                    .addComponent(oNhapCoSoDuLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNguoiDung)
                    .addComponent(oNhapNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(khungBieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMatKhau)
                    .addComponent(oNhapMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        nhanTrangThai.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        nhanTrangThai.setForeground(new java.awt.Color(100, 100, 100));
        nhanTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nhanTrangThai.setText(" ");

        khungNut.setBackground(new java.awt.Color(245, 247, 250));

        btTest.setBackground(new java.awt.Color(255, 255, 254));
        btTest.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btTest.setText("Kiểm tra kết nối");
        btTest.setFocusPainted(false);
        btTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTestActionPerformed(evt);
            }
        });

        btLuu.setBackground(new java.awt.Color(41, 128, 185));
        btLuu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btLuu.setForeground(new java.awt.Color(255, 255, 255));
        btLuu.setText("Lưu cấu hình");
        btLuu.setFocusPainted(false);
        btLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLuuActionPerformed(evt);
            }
        });

        btMacDinh.setBackground(new java.awt.Color(255, 255, 254));
        btMacDinh.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btMacDinh.setText("Mặc định");
        btMacDinh.setFocusPainted(false);
        btMacDinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMacDinhActionPerformed(evt);
            }
        });

        btDong.setBackground(new java.awt.Color(255, 255, 254));
        btDong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btDong.setText("Đóng");
        btDong.setFocusPainted(false);
        btDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout khungNutLayout = new javax.swing.GroupLayout(khungNut);
        khungNut.setLayout(khungNutLayout);
        khungNutLayout.setHorizontalGroup(
            khungNutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, khungNutLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btTest)
                .addGap(8, 8, 8)
                .addComponent(btLuu)
                .addGap(8, 8, 8)
                .addComponent(btMacDinh)
                .addGap(8, 8, 8)
                .addComponent(btDong)
                .addGap(16, 16, 16))
        );
        khungNutLayout.setVerticalGroup(
            khungNutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungNutLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(khungNutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btTest, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btMacDinh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDong, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout khungNenLayout = new javax.swing.GroupLayout(khungNen);
        khungNen.setLayout(khungNenLayout);
        khungNenLayout.setHorizontalGroup(
            khungNenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungNenLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(khungNenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(khungBieuMau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhanTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(khungNut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        khungNenLayout.setVerticalGroup(
            khungNenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungNenLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(khungBieuMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(nhanTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(khungNut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungNen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungNen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTestActionPerformed
        String pass = new String(oNhapMatKhau.getPassword());
        nhanTrangThai.setText("\u0110ang ki\u1ec3m tra k\u1ebft n\u1ed1i...");
        nhanTrangThai.setForeground(Color.BLUE);
        String loi = DBConecter.testConnection(
                oNhapMayChu.getText(), oNhapCong.getText(),
                oNhapCoSoDuLieu.getText(), oNhapNguoiDung.getText(), pass);
        if (loi == null) {
            nhanTrangThai.setText("K\u1ebft n\u1ed1i th\u00e0nh c\u00f4ng t\u1edbi CSDL MySQL!");
            nhanTrangThai.setForeground(new Color(39, 174, 96));
        } else {
            nhanTrangThai.setText("K\u1ebft n\u1ed1i th\u1ea5t b\u1ea1i: " + loi);
            nhanTrangThai.setForeground(new Color(192, 57, 43));
        }
    }//GEN-LAST:event_btTestActionPerformed

    private void btLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLuuActionPerformed
        String pass = new String(oNhapMatKhau.getPassword());
        boolean ok = DBConecter.saveConfig(
                oNhapMayChu.getText(), oNhapCong.getText(),
                oNhapCoSoDuLieu.getText(), oNhapNguoiDung.getText(), pass);
        if (ok) {
            if (onSaved != null) onSaved.run();
            JOptionPane.showMessageDialog(this,
                    "\u0110\u00e3 l\u01b0u c\u1ea5u h\u00ecnh m\u00e1y ch\u1ee7 th\u00e0nh c\u00f4ng!", "Th\u00f4ng b\u00e1o",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Kh\u00f4ng th\u1ec3 l\u01b0u c\u1ea5u h\u00ecnh!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btLuuActionPerformed

    private void btMacDinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMacDinhActionPerformed
        oNhapMayChu.setText("localhost");
        oNhapCong.setText("3306");
        oNhapCoSoDuLieu.setText("hotel_management");
        oNhapNguoiDung.setText("root");
        oNhapMatKhau.setText("");
        nhanTrangThai.setText("\u0110\u00e3 kh\u00f4i ph\u1ee5c gi\u00e1 tr\u1ecb m\u1eb7c \u0111\u1ecbnh. B\u1ea5m 'L\u01b0u c\u1ea5u h\u00ecnh' \u0111\u1ec3 \u00e1p d\u1ee5ng.");
        nhanTrangThai.setForeground(Color.DARK_GRAY);
    }//GEN-LAST:event_btMacDinhActionPerformed

    private void btDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDongActionPerformed
        dispose();
    }//GEN-LAST:event_btDongActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDong;
    private javax.swing.JButton btLuu;
    private javax.swing.JButton btMacDinh;
    private javax.swing.JButton btTest;
    private javax.swing.JPanel khungBieuMau;
    private javax.swing.JPanel khungNen;
    private javax.swing.JPanel khungNut;
    private javax.swing.JLabel lblCoSoDuLieu;
    private javax.swing.JLabel lblCong;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblMayChu;
    private javax.swing.JLabel lblNguoiDung;
    private javax.swing.JLabel nhanTrangThai;
    private javax.swing.JTextField oNhapCoSoDuLieu;
    private javax.swing.JTextField oNhapCong;
    private javax.swing.JPasswordField oNhapMatKhau;
    private javax.swing.JTextField oNhapMayChu;
    private javax.swing.JTextField oNhapNguoiDung;
    // End of variables declaration//GEN-END:variables
}
