package view.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.KhachHang;

public class DialogThemSuaKhachHang extends JDialog {

    private JTextField oNhapHoTen;
    private JTextField oNhapCCCD;
    private JTextField oNhapSDT;
    private JTextField oNhapThuDienTu;
    private JTextField oNhapDiaChi;
    private JButton btLuu;
    private JButton btHuy;

    private boolean daLuu = false;
    private KhachHang khachHang;

    public DialogThemSuaKhachHang(Window doiTuongCha, KhachHang khachHangCanSua) {
        super(doiTuongCha, khachHangCanSua == null ? "Thêm khách hàng mới" : "Chỉnh sửa khách hàng: " + khachHangCanSua.getNameKhachHang(),
                ModalityType.APPLICATION_MODAL);

        this.khachHang = khachHangCanSua != null ? khachHangCanSua : new KhachHang();
        boolean dangSua = (khachHangCanSua != null && khachHangCanSua.getIdKhachHang() > 0);

        setSize(480, 480);
        setLocationRelativeTo(doiTuongCha);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(30, 60, 114));
        khungTieuDe.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel(dangSua ? "CHỈNH SỬA THÔNG TIN KHÁCH HÀNG" : "THÊM KHÁCH HÀNG MỚI");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel(dangSua ? "Cập nhật họ tên, CCCD, số điện thoại hoặc địa chỉ"
                : "Nhập thông tin chi tiết khách hàng để lưu vào hệ thống");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(220, 230, 242));

        khungTieuDe.add(nhanTieuDe, BorderLayout.NORTH);
        khungTieuDe.add(nhanPhu, BorderLayout.SOUTH);
        add(khungTieuDe, BorderLayout.NORTH);

        // Form Body
        JPanel khungNoiDung = new JPanel(new GridBagLayout());
        khungNoiDung.setBorder(new EmptyBorder(20, 24, 15, 24));
        khungNoiDung.setBackground(Color.WHITE);

        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(8, 8, 8, 8);

        Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font phongChuNhap = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Họ và tên
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0;
        JLabel nhanHoTen = new JLabel("Họ và tên (*):");
        nhanHoTen.setFont(phongChuNhan);
        khungNoiDung.add(nhanHoTen, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapHoTen = new JTextField(15);
        oNhapHoTen.setFont(phongChuNhap);
        oNhapHoTen.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapHoTen, gia);

        // 2. Số CCCD
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0;
        JLabel nhanCCCD = new JLabel("Số CCCD (*):");
        nhanCCCD.setFont(phongChuNhan);
        khungNoiDung.add(nhanCCCD, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapCCCD = new JTextField(15);
        oNhapCCCD.setFont(phongChuNhap);
        oNhapCCCD.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapCCCD, gia);

        // 3. Số điện thoại
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0;
        JLabel nhanSDT = new JLabel("Số điện thoại (*):");
        nhanSDT.setFont(phongChuNhan);
        khungNoiDung.add(nhanSDT, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapSDT = new JTextField(15);
        oNhapSDT.setFont(phongChuNhap);
        oNhapSDT.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapSDT, gia);

        // 4. Email
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0;
        JLabel nhanThuDienTu = new JLabel("Email:");
        nhanThuDienTu.setFont(phongChuNhan);
        khungNoiDung.add(nhanThuDienTu, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapThuDienTu = new JTextField(15);
        oNhapThuDienTu.setFont(phongChuNhap);
        oNhapThuDienTu.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapThuDienTu, gia);

        // 5. Địa chỉ
        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0;
        JLabel nhanDiaChi = new JLabel("Địa chỉ:");
        nhanDiaChi.setFont(phongChuNhan);
        khungNoiDung.add(nhanDiaChi, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapDiaChi = new JTextField(15);
        oNhapDiaChi.setFont(phongChuNhap);
        oNhapDiaChi.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapDiaChi, gia);

        add(khungNoiDung, BorderLayout.CENTER);

        // Điền dữ liệu nếu là sửa
        if (dangSua) {
            oNhapHoTen.setText(this.khachHang.getNameKhachHang() != null ? this.khachHang.getNameKhachHang() : "");
            oNhapCCCD.setText(this.khachHang.getSoCCCD() != null ? this.khachHang.getSoCCCD() : "");
            oNhapSDT.setText(this.khachHang.getSoDienThoai() != null ? this.khachHang.getSoDienThoai() : "");
            oNhapThuDienTu.setText(this.khachHang.getEmail() != null ? this.khachHang.getEmail() : "");
            oNhapDiaChi.setText(this.khachHang.getDiaChi() != null ? this.khachHang.getDiaChi() : "");
        }

        // Footer buttons
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
        String hoTen = oNhapHoTen.getText().trim();
        String cccd = oNhapCCCD.getText().trim();
        String sdt = oNhapSDT.getText().trim();
        String thuDienTu = oNhapThuDienTu.getText().trim();
        String diaChi = oNhapDiaChi.getText().trim();

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapHoTen.requestFocus();
            return;
        }

        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapCCCD.requestFocus();
            return;
        }

        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapSDT.requestFocus();
            return;
        }

        this.khachHang.setNameKhachHang(hoTen);
        this.khachHang.setSoCCCD(cccd);
        this.khachHang.setSoDienThoai(sdt);
        this.khachHang.setEmail(thuDienTu);
        this.khachHang.setDiaChi(diaChi);

        daLuu = true;
        dispose();
    }

    public boolean isSaved() {
        return daLuu;
    }

    public KhachHang getCustomer() {
        return khachHang;
    }
}
