package view.dialog;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.HoaDon;

public class DialogThemSuaHoaDon extends JDialog {

    private JTextField oNhapMaDatPhong;
    private JTextField oNhapTienPhong;
    private JTextField oNhapPhuThu;
    private JTextField oNhapGiamGia;
    private JTextField oNhapChungTien;
    private JComboBox<String> hopChonPhuongThuc;
    private JComboBox<String> hopChonTrangThai;
    private JButton btLuu;
    private JButton btHuy;

    private boolean daLuu = false;
    private HoaDon hoaDon;
    private DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,###");

    public DialogThemSuaHoaDon(Window doiTuongCha, HoaDon hoaDonCanSua) {
        super(doiTuongCha, hoaDonCanSua == null ? "Lập hóa đơn mới" : "Chỉnh sửa hóa đơn #" + hoaDonCanSua.getIdHoaDon(),
                ModalityType.APPLICATION_MODAL);

        this.hoaDon = hoaDonCanSua != null ? hoaDonCanSua : new HoaDon();
        boolean dangSua = (hoaDonCanSua != null && hoaDonCanSua.getIdHoaDon() > 0);

        setSize(520, 560);
        setLocationRelativeTo(doiTuongCha);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(30, 60, 114));
        khungTieuDe.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel(dangSua ? "CHỈNH SỬA HÓA ĐƠN #" + this.hoaDon.getIdHoaDon() : "LẬP HÓA ĐƠN THANH TOÁN");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel(dangSua ? "Cập nhật tiền phòng, phụ thu, giảm giá hoặc trạng thái hóa đơn"
                : "Nhập thông tin đặt phòng và các khoản tiền để tạo hóa đơn mới");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(220, 230, 242));

        khungTieuDe.add(nhanTieuDe, BorderLayout.NORTH);
        khungTieuDe.add(nhanPhu, BorderLayout.SOUTH);
        add(khungTieuDe, BorderLayout.NORTH);

        // Body Form
        JPanel khungNoiDung = new JPanel(new GridBagLayout());
        khungNoiDung.setBorder(new EmptyBorder(20, 24, 15, 24));
        khungNoiDung.setBackground(Color.WHITE);

        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(8, 8, 8, 8);

        Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font phongChuNhap = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. Mã đặt phòng
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0;
        JLabel nhanMaDP = new JLabel("Mã đặt phòng (*):");
        nhanMaDP.setFont(phongChuNhan);
        khungNoiDung.add(nhanMaDP, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapMaDatPhong = new JTextField(15);
        oNhapMaDatPhong.setFont(phongChuNhap);
        oNhapMaDatPhong.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapMaDatPhong, gia);

        // 2. Tiền phòng
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0;
        JLabel nhanTienPhong = new JLabel("Tiền phòng (VNĐ):");
        nhanTienPhong.setFont(phongChuNhan);
        khungNoiDung.add(nhanTienPhong, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapTienPhong = new JTextField("0", 15);
        oNhapTienPhong.setFont(phongChuNhap);
        oNhapTienPhong.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapTienPhong, gia);

        // 3. Phụ thu
        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0;
        JLabel nhanPhuThu = new JLabel("Phụ thu (VNĐ):");
        nhanPhuThu.setFont(phongChuNhan);
        khungNoiDung.add(nhanPhuThu, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapPhuThu = new JTextField("0", 15);
        oNhapPhuThu.setFont(phongChuNhap);
        oNhapPhuThu.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapPhuThu, gia);

        // 4. Giảm giá
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0;
        JLabel nhanGiamGia = new JLabel("Giảm giá (VNĐ):");
        nhanGiamGia.setFont(phongChuNhan);
        khungNoiDung.add(nhanGiamGia, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapGiamGia = new JTextField("0", 15);
        oNhapGiamGia.setFont(phongChuNhap);
        oNhapGiamGia.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapGiamGia, gia);

        // 5. Tổng tiền (tự động tính)
        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0;
        JLabel nhanChungTien = new JLabel("Tổng tiền (VNĐ):");
        nhanChungTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nhanChungTien.setForeground(new Color(13, 110, 253));
        khungNoiDung.add(nhanChungTien, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapChungTien = new JTextField("0", 15);
        oNhapChungTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        oNhapChungTien.setEditable(false);
        oNhapChungTien.setBackground(new Color(240, 244, 250));
        oNhapChungTien.setForeground(new Color(13, 110, 253));
        oNhapChungTien.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapChungTien, gia);

        // 6. Phương thức thanh toán
        gia.gridx = 0; gia.gridy = 5; gia.weightx = 0;
        JLabel nhanPhuongThuc = new JLabel("Phương thức:");
        nhanPhuongThuc.setFont(phongChuNhan);
        khungNoiDung.add(nhanPhuongThuc, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonPhuongThuc = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ" });
        hopChonPhuongThuc.setFont(phongChuNhap);
        hopChonPhuongThuc.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(hopChonPhuongThuc, gia);

        // 7. Trạng thái thanh toán
        gia.gridx = 0; gia.gridy = 6; gia.weightx = 0;
        JLabel nhanTrangThai = new JLabel("Trạng thái:");
        nhanTrangThai.setFont(phongChuNhan);
        khungNoiDung.add(nhanTrangThai, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonTrangThai = new JComboBox<>(new String[] { "PAID", "UNPAID", "REFUNDED" });
        hopChonTrangThai.setFont(phongChuNhap);
        hopChonTrangThai.setPreferredSize(new Dimension(200, 32));
        hopChonTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                String chuoi = String.valueOf(giaTri);
                if ("PAID".equalsIgnoreCase(chuoi)) setText("Đã thanh toán (PAID)");
                else if ("UNPAID".equalsIgnoreCase(chuoi)) setText("Chưa thanh toán (UNPAID)");
                else if ("REFUNDED".equalsIgnoreCase(chuoi)) setText("Đã hoàn tiền (REFUNDED)");
                return this;
            }
        });
        khungNoiDung.add(hopChonTrangThai, gia);

        add(khungNoiDung, BorderLayout.CENTER);

        // Lắng nghe thay đổi các ô tiền để tự động tính tổng
        KeyAdapter tinhChungAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent suKien) {
                tinhTongTien();
            }
        };
        oNhapTienPhong.addKeyListener(tinhChungAdapter);
        oNhapPhuThu.addKeyListener(tinhChungAdapter);
        oNhapGiamGia.addKeyListener(tinhChungAdapter);

        // Điền dữ liệu nếu là sửa
        if (dangSua) {
            oNhapMaDatPhong.setText(String.valueOf(this.hoaDon.getIdDatPhong()));
            oNhapTienPhong.setText(String.valueOf(Math.round(this.hoaDon.getTienPhong())));
            oNhapPhuThu.setText(String.valueOf(Math.round(this.hoaDon.getPhuThu())));
            oNhapGiamGia.setText(String.valueOf(Math.round(this.hoaDon.getGiamGia())));
            tinhTongTien();

            if (this.hoaDon.getPhuongThucThanhToan() != null) {
                hopChonPhuongThuc.setSelectedItem(this.hoaDon.getPhuongThucThanhToan());
            }

            String luongTrinh = this.hoaDon.getTrangThaiThanhToan();
            if (luongTrinh != null) {
                if (luongTrinh.contains("PAID") || luongTrinh.contains("Đã thanh toán")) hopChonTrangThai.setSelectedItem("PAID");
                else if (luongTrinh.contains("REFUNDED") || luongTrinh.contains("hoàn tiền")) hopChonTrangThai.setSelectedItem("REFUNDED");
                else hopChonTrangThai.setSelectedItem("UNPAID");
            }
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

        btLuu = new JButton(dangSua ? "Cập nhật" : "Lưu hóa đơn");
        btLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btLuu.setBackground(new Color(13, 110, 253));
        btLuu.setForeground(Color.WHITE);
        btLuu.setPreferredSize(new Dimension(130, 34));
        btLuu.setFocusPainted(false);
        btLuu.addActionListener(ac -> xuLyLuu());

        khungChan.add(btHuy);
        khungChan.add(btLuu);
        add(khungChan, BorderLayout.SOUTH);
    }

    private void tinhTongTien() {
        try {
            float thanhTieuDe = Float.parseFloat(oNhapTienPhong.getText().trim().replaceAll("[^0-9.]", ""));
            float phuThu = Float.parseFloat(oNhapPhuThu.getText().trim().replaceAll("[^0-9.]", ""));
            float doHoa = Float.parseFloat(oNhapGiamGia.getText().trim().replaceAll("[^0-9.]", ""));
            float chung = thanhTieuDe + phuThu - doHoa;
            if (chung < 0) chung = 0;
            oNhapChungTien.setText(dinhDangTienTeChinh.format(chung) + " đ");
        } catch (Exception biBoQua) {}
    }

    private void xuLyLuu() {
        int maDatPhong;
        try {
            maDatPhong = Integer.parseInt(oNhapMaDatPhong.getText().trim());
            if (maDatPhong <= 0) throw new Exception();
        } catch (Exception ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Mã đặt phòng phải là số nguyên dương hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            oNhapMaDatPhong.requestFocus();
            return;
        }

        float thanhTieuDe, phuThu, doHoa;
        try {
            thanhTieuDe = Float.parseFloat(oNhapTienPhong.getText().trim().replaceAll("[^0-9.]", ""));
            phuThu = Float.parseFloat(oNhapPhuThu.getText().trim().replaceAll("[^0-9.]", ""));
            doHoa = Float.parseFloat(oNhapGiamGia.getText().trim().replaceAll("[^0-9.]", ""));
        } catch (Exception ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Các khoản tiền phải là số hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        float chung = thanhTieuDe + phuThu - doHoa;
        if (chung < 0) chung = 0;

        this.hoaDon.setIdDatPhong(maDatPhong);
        this.hoaDon.setTienPhong(thanhTieuDe);
        this.hoaDon.setPhuThu(phuThu);
        this.hoaDon.setGiamGia(doHoa);
        this.hoaDon.setTongTien(chung);
        this.hoaDon.setPhuongThucThanhToan((String) hopChonPhuongThuc.getSelectedItem());
        this.hoaDon.setTrangThaiThanhToan((String) hopChonTrangThai.getSelectedItem());

        daLuu = true;
        dispose();
    }

    public boolean isSaved() {
        return daLuu;
    }

    public HoaDon getInvoice() {
        return hoaDon;
    }
}
