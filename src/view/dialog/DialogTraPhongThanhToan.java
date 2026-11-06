package view.dialog;

import view.dialog.DialogChiTietHoaDon;

import java.awt.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.DatPhong;
import model.HoaDon;
import service.DatPhongService;
import service.HoaDonService;

public class DialogTraPhongThanhToan extends JDialog {

    private boolean hoanTat = false;
    private DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,### đ");

    private DatPhong datPhong;
    private DatPhongService dichVuDatPhong = new DatPhongService();

    // Các thành phần form
    private JTextField oNhapPhuThu;
    private JTextField oNhapGiamGia;
    private JComboBox<String> hopChonPhuongThuc;
    private JLabel nhanChungTien;
    private double tienPhong = 0;
    private double chungThanhToan = 0;

    public DialogTraPhongThanhToan(Window doiTuongCha, DatPhong nut) {
        super(doiTuongCha, "Trả phòng & Xuất hóa đơn thanh toán", ModalityType.APPLICATION_MODAL);
        this.datPhong = nut;

        setSize(580, 640);
        setResizable(false);
        setLocationRelativeTo(doiTuongCha);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 1. Header Panel
        JPanel khungDau = new JPanel(new BorderLayout());
        khungDau.setBackground(new Color(192, 57, 43));
        khungDau.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel nhanTieuDe = new JLabel("HÓA ĐƠN THANH TOÁN & TRẢ PHÒNG");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nhanTieuDe.setForeground(Color.WHITE);

        String chuoiPhong = (datPhong != null && !datPhong.getSoPhong().isEmpty()) ? "Phòng " + datPhong.getSoPhong() : "";
        JLabel nhanPhu = new JLabel("Xác nhận tính tiền và kết thúc lượt lưu trú " + chuoiPhong);
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(250, 219, 216));

        khungDau.add(nhanTieuDe, BorderLayout.NORTH);
        khungDau.add(nhanPhu, BorderLayout.SOUTH);
        add(khungDau, BorderLayout.NORTH);

        // 2. Body Panel
        JPanel khungNoiDung = new JPanel();
        khungNoiDung.setLayout(new BoxLayout(khungNoiDung, BoxLayout.Y_AXIS));
        khungNoiDung.setBackground(Color.WHITE);
        khungNoiDung.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Khối 1: Thông tin khách & phòng
        JPanel khungThongTin = new JPanel(new GridLayout(4, 2, 8, 8));
        khungThongTin.setBackground(new Color(248, 249, 250));
        khungThongTin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                new EmptyBorder(12, 14, 12, 14)));

        String tenKhach = (datPhong != null && !datPhong.getTenKhachHang().isEmpty()) ? datPhong.getTenKhachHang() : "Khách lưu trú";
        String sdt = (datPhong != null && !datPhong.getSoDienThoai().isEmpty()) ? datPhong.getSoDienThoai() : "---";
        String cccd = (datPhong != null && !datPhong.getCccd().isEmpty()) ? datPhong.getCccd() : "---";
        String soPhong = (datPhong != null && !datPhong.getSoPhong().isEmpty()) ? datPhong.getSoPhong() : "---";
        String loaiPhong = (datPhong != null && !datPhong.getTenLoaiPhong().isEmpty()) ? datPhong.getTenLoaiPhong() : "Tiêu chuẩn";

        Date ngayNhan = (datPhong != null && datPhong.getNgayNhanPhong() != null) ? datPhong.getNgayNhanPhong() : Date.valueOf(LocalDate.now());
        Date ngayTraHienTai = Date.valueOf(LocalDate.now());

        long soDem = 1;
        try {
            soDem = Math.max(1, ChronoUnit.DAYS.between(ngayNhan.toLocalDate(), ngayTraHienTai.toLocalDate()));
        } catch (Exception biBoQua) {}

        double donGia = (datPhong != null && datPhong.getGiaMoiDem() > 0) ? datPhong.getGiaMoiDem() : 300000;
        tienPhong = soDem * donGia;

        khungThongTin.add(taoLabel("Khách hàng:", true));
        khungThongTin.add(taoLabel(tenKhach + " (SĐT: " + sdt + ")", false));

        khungThongTin.add(taoLabel("Số CCCD:", true));
        khungThongTin.add(taoLabel(cccd, false));

        khungThongTin.add(taoLabel("Phòng lưu trú:", true));
        khungThongTin.add(taoLabel("P." + soPhong + " (" + loaiPhong + " - " + dinhDangTienTeChinh.format(donGia) + "/đêm)", false));

        khungThongTin.add(taoLabel("Thời gian ở:", true));
        khungThongTin.add(taoLabel(ngayNhan + " đến " + ngayTraHienTai + " (" + soDem + " đêm)", false));

        khungNoiDung.add(khungThongTin);
        khungNoiDung.add(Box.createVerticalStrut(15));

        // Khối 2: Chi tiết tính tiền
        JPanel khungTinhToan = new JPanel(new GridBagLayout());
        khungTinhToan.setOpaque(false);
        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(6, 4, 6, 4);

        // Tiền phòng
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0.4;
        khungTinhToan.add(new JLabel("Tiền phòng (" + soDem + " đêm):"), gia);
        gia.gridx = 1; gia.gridy = 0; gia.weightx = 0.6;
        JLabel nhanTienPhong = new JLabel(dinhDangTienTeChinh.format(tienPhong));
        nhanTienPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        khungTinhToan.add(nhanTienPhong, gia);

        // Phụ thu
        gia.gridx = 0; gia.gridy = 1;
        khungTinhToan.add(new JLabel("Phụ thu (dịch vụ, nước, đồ uống):"), gia);
        gia.gridx = 1; gia.gridy = 1;
        oNhapPhuThu = new JTextField("0");
        oNhapPhuThu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        khungTinhToan.add(oNhapPhuThu, gia);

        // Giảm giá
        gia.gridx = 0; gia.gridy = 2;
        khungTinhToan.add(new JLabel("Giảm giá / Chiết khấu:"), gia);
        gia.gridx = 1; gia.gridy = 2;
        oNhapGiamGia = new JTextField("0");
        oNhapGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        khungTinhToan.add(oNhapGiamGia, gia);

        // Phương thức thanh toán
        gia.gridx = 0; gia.gridy = 3;
        khungTinhToan.add(new JLabel("Phương thức thanh toán:"), gia);
        gia.gridx = 1; gia.gridy = 3;
        hopChonPhuongThuc = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản QR", "Thẻ tín dụng / POS" });
        hopChonPhuongThuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        khungTinhToan.add(hopChonPhuongThuc, gia);

        // TỔNG TIỀN PHẢI THANH TOÁN
        gia.gridx = 0; gia.gridy = 4;
        JLabel nhanChungTieuDe = new JLabel("TỔNG THANH TOÁN:");
        nhanChungTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nhanChungTieuDe.setForeground(new Color(192, 57, 43));
        khungTinhToan.add(nhanChungTieuDe, gia);

        gia.gridx = 1; gia.gridy = 4;
        nhanChungTien = new JLabel(dinhDangTienTeChinh.format(tienPhong));
        nhanChungTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanChungTien.setForeground(new Color(192, 57, 43));
        khungTinhToan.add(nhanChungTien, gia);

        khungNoiDung.add(khungTinhToan);

        // Ghi chú nghiệp vụ
        khungNoiDung.add(Box.createVerticalStrut(10));
        JLabel nhanGhiChu = new JLabel("Lưu ý: Sau khi xác nhận thanh toán, phòng sẽ tự động chuyển sang ĐANG DỌN (CLEANING).");
        nhanGhiChu.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        nhanGhiChu.setForeground(new Color(108, 117, 125));
        khungNoiDung.add(nhanGhiChu);

        add(khungNoiDung, BorderLayout.CENTER);

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

        oNhapPhuThu.addActionListener(suKien -> capNhatChung.run());
        oNhapGiamGia.addActionListener(suKien -> capNhatChung.run());
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

        // 3. Bottom Action Buttons
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        khungDuoi.setBackground(new Color(245, 247, 250));
        khungDuoi.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton nutInHoaDon = new JButton("In / Xem trước hóa đơn");
        nutInHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nutInHoaDon.setFocusPainted(false);

        JButton nutXacNhan = new JButton("Xác nhận thanh toán & Trả phòng");
        nutXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutXacNhan.setBackground(new Color(192, 57, 43));
        nutXacNhan.setForeground(Color.WHITE);
        nutXacNhan.setFocusPainted(false);

        JButton nutHuy = new JButton("Hủy bỏ");
        nutHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutHuy.setFocusPainted(false);

        khungDuoi.add(nutInHoaDon);
        khungDuoi.add(nutHuy);
        khungDuoi.add(nutXacNhan);
        add(khungDuoi, BorderLayout.SOUTH);

        nutHuy.addActionListener(suKien -> dispose());

        // In / Xem trước phiếu thanh toán
        nutInHoaDon.addActionListener(suKien -> {
            capNhatChung.run();
            hienThiPhieuInHoaDon();
        });

        // Xác nhận thanh toán & Trả phòng
        nutXacNhan.addActionListener(suKien -> {
            capNhatChung.run();

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
            int maPhong = datPhong.getMaPhong();

            boolean thanhCong = dichVuDatPhong.traPhongVaThanhToan(maDatPhong, maPhong, tienPhong, phuThu, doHoa, chung, phuongThuc);
            if (thanhCong) {
                hoanTat = true;
                HoaDon hoaDon = new HoaDonService().timTheoDatPhong(maDatPhong);
                JOptionPane.showMessageDialog(this,
                        "Thanh toán và Check-out thành công!\n"
                                + "Tổng tiền: " + dinhDangTienTeChinh.format(chung) + " (" + phuongThuc + ")\n"
                                + "Phòng " + datPhong.getSoPhong() + " đã chuyển sang trạng thái ĐANG DỌN."
                                + (hoaDon == null ? "\nKhông thể tải hóa đơn để xem trước."
                                        : "\nHóa đơn sẵn sàng để xem trước và in."),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                Window doiTuongCha = getOwner();
                dispose();
                if (hoaDon != null) {
                    SwingUtilities.invokeLater(() -> DialogChiTietHoaDon.hienThiXemTruocHoaDon(doiTuongCha, hoaDon));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thực hiện trả phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void hienThiPhieuInHoaDon() {
        JDialog hopThoaiIn = new JDialog(this, "Phiếu thanh toán khách sạn", true);
        hopThoaiIn.setSize(480, 560);
        hopThoaiIn.setLocationRelativeTo(this);
        hopThoaiIn.setLayout(new BorderLayout());

        JTextArea oNhapBill = new JTextArea();
        oNhapBill.setFont(new Font("Monospaced", Font.PLAIN, 13));
        oNhapBill.setEditable(false);
        oNhapBill.setMargin(new Insets(15, 20, 15, 20));

        double phuThu = parseDoubleSafe(oNhapPhuThu.getText());
        double doHoa = parseDoubleSafe(oNhapGiamGia.getText());
        double chung = Math.max(0, tienPhong + phuThu - doHoa);
        String phuongThuc = (String) hopChonPhuongThuc.getSelectedItem();

        StringBuilder boTaoChuoi = new StringBuilder();
        boTaoChuoi.append("================================================\n");
        boTaoChuoi.append("             KHÁCH SẠN HOTEL MANAGEMENT         \n");
        boTaoChuoi.append("         Địa chỉ: 123 Đường ABC, TP. Hà Nội     \n");
        boTaoChuoi.append("             Điện thoại: 024.3828.9999          \n");
        boTaoChuoi.append("------------------------------------------------\n");
        boTaoChuoi.append("             HÓA ĐƠN THANH TOÁN (BILL)          \n");
        boTaoChuoi.append("------------------------------------------------\n");
        boTaoChuoi.append(String.format("Mã đơn đặt phòng : #%d\n", datPhong.getIdDatPhong()));
        boTaoChuoi.append(String.format("Khách hàng       : %s\n", datPhong.getTenKhachHang()));
        boTaoChuoi.append(String.format("Số CCCD          : %s\n", datPhong.getCccd()));
        boTaoChuoi.append(String.format("Số điện thoại    : %s\n", datPhong.getSoDienThoai()));
        boTaoChuoi.append(String.format("Phòng            : P.%s (%s)\n", datPhong.getSoPhong(), datPhong.getTenLoaiPhong()));
        boTaoChuoi.append(String.format("Ngày nhận phòng  : %s\n", datPhong.getNgayNhanPhong()));
        boTaoChuoi.append(String.format("Ngày trả phòng   : %s\n", LocalDate.now()));
        boTaoChuoi.append("------------------------------------------------\n");
        boTaoChuoi.append(String.format("Tiền phòng       : %15s\n", dinhDangTienTeChinh.format(tienPhong)));
        boTaoChuoi.append(String.format("Phụ thu          : %15s\n", dinhDangTienTeChinh.format(phuThu)));
        boTaoChuoi.append(String.format("Giảm giá         : %15s\n", "-" + dinhDangTienTeChinh.format(doHoa)));
        boTaoChuoi.append("------------------------------------------------\n");
        boTaoChuoi.append(String.format("TỔNG CỘNG        : %15s\n", dinhDangTienTeChinh.format(chung)));
        boTaoChuoi.append(String.format("Phương thức TT   : %s\n", phuongThuc));
        boTaoChuoi.append("================================================\n");
        boTaoChuoi.append("       Cảm ơn Quý khách & Hẹn gặp lại!          \n");

        oNhapBill.setText(boTaoChuoi.toString());

        JScrollPane thanhCuon = new JScrollPane(oNhapBill);
        hopThoaiIn.add(thanhCuon, BorderLayout.CENTER);

        JPanel khungNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nutHang = new JButton("Đóng");
        nutHang.addActionListener(suKien -> hopThoaiIn.dispose());
        khungNut.add(nutHang);
        hopThoaiIn.add(khungNut, BorderLayout.SOUTH);

        hopThoaiIn.setVisible(true);
    }

    private double parseDoubleSafe(String chuoi) {
        if (chuoi == null) return 0;
        try {
            return Double.parseDouble(chuoi.trim().replaceAll("[^0-9.]", ""));
        } catch (Exception suKien) {
            return 0;
        }
    }

    private JLabel taoLabel(String vanBan, boolean inDam) {
        JLabel nhan = new JLabel(vanBan);
        nhan.setFont(new Font("Segoe UI", inDam ? Font.BOLD : Font.PLAIN, 13));
        if (inDam) nhan.setForeground(new Color(51, 65, 85));
        return nhan;
    }

    public boolean isCompleted() {
        return hoanTat;
    }
}
