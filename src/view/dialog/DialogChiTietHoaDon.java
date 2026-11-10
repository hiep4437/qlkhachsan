package view.dialog;

import java.awt.*;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.HoaDon;
import service.HoaDonService;

public class DialogChiTietHoaDon extends JDialog {

    private HoaDon hoaDon;
    private HoaDonService dichVuHoaDon = new HoaDonService();
    private DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,### đ");
    private SimpleDateFormat dinhDangNgay = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean daThanhToanXong = false;

    public DialogChiTietHoaDon(Window doiTuongCha, HoaDon hoaDon) {
        super(doiTuongCha, "Chi tiết hóa đơn thanh toán #" + (hoaDon != null ? hoaDon.getIdHoaDon() : ""), ModalityType.APPLICATION_MODAL);
        this.hoaDon = hoaDon;

        setSize(540, 620);
        setResizable(false);
        setLocationRelativeTo(doiTuongCha);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        if (hoaDon == null) return;

        // Top Header
        JPanel khungDau = new JPanel(new BorderLayout());
        khungDau.setBackground(new Color(24, 44, 97));
        khungDau.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel("CHI TIẾT HÓA ĐƠN #" + hoaDon.getIdHoaDon());
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nhanTieuDe.setForeground(Color.WHITE);

        String luongTrinh = hoaDon.getTrangThaiThanhToan();
        String luongChuoi = (luongTrinh != null && (luongTrinh.contains("PAID") || luongTrinh.contains("Đã thanh toán"))) ? "ĐÃ THANH TOÁN" : "CHƯA THANH TOÁN";
        JLabel nhanTrangThai = new JLabel(luongChuoi);
        nhanTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nhanTrangThai.setForeground(luongChuoi.equals("ĐÃ THANH TOÁN") ? new Color(46, 204, 113) : new Color(241, 196, 15));

        khungDau.add(nhanTieuDe, BorderLayout.WEST);
        khungDau.add(nhanTrangThai, BorderLayout.EAST);
        add(khungDau, BorderLayout.NORTH);

        // Center Content
        JPanel khungNoiDung = new JPanel();
        khungNoiDung.setLayout(new BoxLayout(khungNoiDung, BoxLayout.Y_AXIS));
        khungNoiDung.setBackground(Color.WHITE);
        khungNoiDung.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Block 1: Thông tin phòng & khách
        JPanel khungThongTin = new JPanel(new GridLayout(4, 2, 10, 8));
        khungThongTin.setBackground(new Color(248, 249, 250));
        khungThongTin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                new EmptyBorder(12, 14, 12, 14)));

        String soPhong = !hoaDon.getSoPhong().isEmpty() ? "Phòng " + hoaDon.getSoPhong() : "---";
        String tenKhach = !hoaDon.getTenKhachHang().isEmpty() ? hoaDon.getTenKhachHang() : "---";
        String sdt = !hoaDon.getSoDienThoai().isEmpty() ? hoaDon.getSoDienThoai() : "---";
        String cccd = !hoaDon.getSoCCCD().isEmpty() ? hoaDon.getSoCCCD() : "---";
        String ngayLap = hoaDon.getNgayLap() != null ? dinhDangNgay.format(hoaDon.getNgayLap()) : "---";

        khungThongTin.add(taoLabel("Phòng thanh toán:", true));
        khungThongTin.add(taoLabel(soPhong + " (" + hoaDon.getTenLoaiPhong() + ")", false));

        khungThongTin.add(taoLabel("Khách hàng:", true));
        khungThongTin.add(taoLabel(tenKhach + " (SĐT: " + sdt + ")", false));

        khungThongTin.add(taoLabel("Số CCCD / CMND:", true));
        khungThongTin.add(taoLabel(cccd, false));

        khungThongTin.add(taoLabel("Thời gian lập HĐ:", true));
        khungThongTin.add(taoLabel(ngayLap, false));

        khungNoiDung.add(khungThongTin);
        khungNoiDung.add(Box.createVerticalStrut(15));

        // Block 2: Chi tiết các khoản tiền
        JPanel khungTien = new JPanel(new GridLayout(5, 2, 10, 10));
        khungTien.setOpaque(false);
        khungTien.setBorder(new EmptyBorder(10, 10, 10, 10));

        khungTien.add(taoLabel("Tiền phòng:", false));
        khungTien.add(taoMoneyLabel(dinhDangTienTeChinh.format(hoaDon.getTienPhong())));

        khungTien.add(taoLabel("Phụ thu (dịch vụ):", false));
        khungTien.add(taoMoneyLabel(dinhDangTienTeChinh.format(hoaDon.getPhuThu())));

        khungTien.add(taoLabel("Giảm giá / Ưu đãi:", false));
        khungTien.add(taoMoneyLabel("-" + dinhDangTienTeChinh.format(hoaDon.getGiamGia())));

        khungTien.add(taoLabel("Phương thức thanh toán:", false));
        khungTien.add(taoLabel(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan() : "Tiền mặt", true));

        JLabel nhanTieuDeTong = new JLabel("TỔNG TIỀN THANH TOÁN:");
        nhanTieuDeTong.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nhanTieuDeTong.setForeground(new Color(192, 57, 43));
        khungTien.add(nhanTieuDeTong);

        JLabel nhanGiaTriTong = new JLabel(dinhDangTienTeChinh.format(hoaDon.getTongTien()));
        nhanGiaTriTong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nhanGiaTriTong.setForeground(new Color(192, 57, 43));
        nhanGiaTriTong.setHorizontalAlignment(SwingConstants.RIGHT);
        khungTien.add(nhanGiaTriTong);

        khungNoiDung.add(khungTien);
        add(khungNoiDung, BorderLayout.CENTER);

        // Bottom buttons
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        khungDuoi.setBackground(new Color(245, 247, 250));
        khungDuoi.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton btIn = new JButton("In hóa đơn (Bill)");
        btIn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btIn.setFocusPainted(false);

        JButton btThanhToanNgay = new JButton("Xác nhận thanh toán ngay");
        btThanhToanNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btThanhToanNgay.setBackground(new Color(39, 174, 96));
        btThanhToanNgay.setForeground(Color.WHITE);
        btThanhToanNgay.setFocusPainted(false);

        JButton btHang = new JButton("Đóng");
        btHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btHang.setFocusPainted(false);

        // Ẩn nút "Thanh toán ngay" nếu đã thanh toán
        if (luongChuoi.equals("ĐÃ THANH TOÁN")) {
            btThanhToanNgay.setVisible(false);
        }

        khungDuoi.add(btIn);
        if (btThanhToanNgay.isVisible()) {
            khungDuoi.add(btThanhToanNgay);
        }
        khungDuoi.add(btHang);
        add(khungDuoi, BorderLayout.SOUTH);

        btHang.addActionListener(ac -> dispose());

        btThanhToanNgay.addActionListener(ac -> {
            int tepCauHinh = JOptionPane.showConfirmDialog(
                    this,
                    "Xác nhận đã nhận đủ " + dinhDangTienTeChinh.format(hoaDon.getTongTien()) + " cho Hóa đơn #" + hoaDon.getIdHoaDon() + "?",
                    "Xác nhận thanh toán",
                    JOptionPane.YES_NO_OPTION);
            if (tepCauHinh == JOptionPane.YES_OPTION) {
                if (dichVuHoaDon.capNhatTrangThai(hoaDon.getIdHoaDon(), "PAID", hoaDon.getPhuongThucThanhToan()) > 0) {
                    daThanhToanXong = true;
                    JOptionPane.showMessageDialog(this, "Hóa đơn đã được thanh toán thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btIn.addActionListener(ac -> inHoaDon());
    }

    private void inHoaDon() {
        hienThiXemTruocHoaDon(this, hoaDon);
    }

    public static void hienThiXemTruocHoaDon(Window doiTuongCha, HoaDon hoaDon) {
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(doiTuongCha, "Không tìm thấy hóa đơn để in.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog hopThoaiIn = new JDialog(doiTuongCha, "In hóa đơn #" + hoaDon.getIdHoaDon(), Dialog.ModalityType.APPLICATION_MODAL);
        hopThoaiIn.setSize(460, 520);
        hopThoaiIn.setLocationRelativeTo(doiTuongCha);
        hopThoaiIn.setLayout(new BorderLayout());

        JTextArea oNhap = new JTextArea();
        oNhap.setFont(new Font("Monospaced", Font.PLAIN, 13));
        oNhap.setEditable(false);
        oNhap.setMargin(new Insets(15, 20, 15, 20));

        oNhap.setText(noiDungHoaDon(hoaDon));
        hopThoaiIn.add(new JScrollPane(oNhap), BorderLayout.CENTER);

        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btIn = new JButton("In");
        btIn.addActionListener(ac -> thucHienIn(doiTuongCha, oNhap));
        JButton btDong = new JButton("Đóng");
        btDong.addActionListener(ac -> hopThoaiIn.dispose());
        khungDuoi.add(btIn);
        khungDuoi.add(btDong);
        hopThoaiIn.add(khungDuoi, BorderLayout.SOUTH);

        hopThoaiIn.setVisible(true);
    }

    private static String noiDungHoaDon(HoaDon hoaDon) {
        DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,### đ");
        SimpleDateFormat dinhDangNgay = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder boTaoChuoi = new StringBuilder();
        boTaoChuoi.append("==============================================\n");
        boTaoChuoi.append("            KHÁCH SẠN HOTEL MANAGEMENT        \n");
        boTaoChuoi.append("        Địa chỉ: 123 Đường ABC, TP. Hà Nội    \n");
        boTaoChuoi.append("----------------------------------------------\n");
        boTaoChuoi.append("            PHIẾU THANH TOÁN HÓA ĐƠN          \n");
        boTaoChuoi.append("----------------------------------------------\n");
        boTaoChuoi.append(String.format("Mã hóa đơn     : #%d\n", hoaDon.getIdHoaDon()));
        boTaoChuoi.append(String.format("Mã đặt phòng   : #%d\n", hoaDon.getIdDatPhong()));
        boTaoChuoi.append(String.format("Phòng          : %s (%s)\n", hoaDon.getSoPhong(), hoaDon.getTenLoaiPhong()));
        boTaoChuoi.append(String.format("Khách hàng     : %s\n", hoaDon.getTenKhachHang()));
        boTaoChuoi.append(String.format("Số CCCD        : %s\n", hoaDon.getSoCCCD()));
        boTaoChuoi.append(String.format("Số điện thoại  : %s\n", hoaDon.getSoDienThoai()));
        boTaoChuoi.append(String.format("Thời gian lập  : %s\n", hoaDon.getNgayLap() != null ? dinhDangNgay.format(hoaDon.getNgayLap()) : ""));
        boTaoChuoi.append("----------------------------------------------\n");
        boTaoChuoi.append(String.format("Tiền phòng     : %15s\n", dinhDangTienTeChinh.format(hoaDon.getTienPhong())));
        boTaoChuoi.append(String.format("Phụ thu        : %15s\n", dinhDangTienTeChinh.format(hoaDon.getPhuThu())));
        boTaoChuoi.append(String.format("Giảm giá       : %15s\n", "-" + dinhDangTienTeChinh.format(hoaDon.getGiamGia())));
        boTaoChuoi.append("----------------------------------------------\n");
        boTaoChuoi.append(String.format("TỔNG TIỀN      : %15s\n", dinhDangTienTeChinh.format(hoaDon.getTongTien())));
        boTaoChuoi.append(String.format("Phương thức TT : %s\n", hoaDon.getPhuongThucThanhToan()));
        boTaoChuoi.append(String.format("Trạng thái     : %s\n", hoaDon.getTrangThaiThanhToan()));
        boTaoChuoi.append("==============================================\n");
        boTaoChuoi.append("      Xin cảm ơn Quý khách & Hẹn gặp lại!     \n");
        return boTaoChuoi.toString();
    }

    private static void thucHienIn(Window doiTuongCha, JTextArea oNhap) {
        try {
            if (!oNhap.print()) {
                JOptionPane.showMessageDialog(doiTuongCha, "Đã hủy in hóa đơn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ngoaiLe) {
            JOptionPane.showMessageDialog(doiTuongCha, "Không thể in hóa đơn: " + ngoaiLe.getMessage(), "Lỗi in hóa đơn",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel taoLabel(String vanBan, boolean inDam) {
        JLabel nhan = new JLabel(vanBan);
        nhan.setFont(new Font("Segoe UI", inDam ? Font.BOLD : Font.PLAIN, 13));
        if (inDam) nhan.setForeground(new Color(51, 65, 85));
        return nhan;
    }

    private JLabel taoMoneyLabel(String vanBan) {
        JLabel nhan = new JLabel(vanBan);
        nhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhan.setHorizontalAlignment(SwingConstants.RIGHT);
        nhan.setForeground(new Color(30, 41, 59));
        return nhan;
    }

    public boolean isDaThanhToanXong() {
        return daThanhToanXong;
    }
}
