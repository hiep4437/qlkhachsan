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

    private boolean completed = false;
    private DecimalFormat df = new DecimalFormat("#,### đ");

    private DatPhong booking;
    private DatPhongService bookingService = new DatPhongService();

    // Các thành phần form
    private JTextField txtPhuThu;
    private JTextField txtGiamGia;
    private JComboBox<String> cboPhuongThuc;
    private JLabel lblTongTien;
    private double tienPhong = 0;
    private double tongThanhToan = 0;

    public DialogTraPhongThanhToan(Window parent, DatPhong b) {
        super(parent, "Trả phòng & Xuất hóa đơn thanh toán", ModalityType.APPLICATION_MODAL);
        this.booking = b;

        setSize(580, 640);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 1. Header Panel
        JPanel pHead = new JPanel(new BorderLayout());
        pHead.setBackground(new Color(192, 57, 43));
        pHead.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN & TRẢ PHÒNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        String roomStr = (booking != null && !booking.getSoPhong().isEmpty()) ? "Phòng " + booking.getSoPhong() : "";
        JLabel lblSub = new JLabel("Xác nhận tính tiền và kết thúc lượt lưu trú " + roomStr);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(250, 219, 216));

        pHead.add(lblTitle, BorderLayout.NORTH);
        pHead.add(lblSub, BorderLayout.SOUTH);
        add(pHead, BorderLayout.NORTH);

        // 2. Body Panel
        JPanel pBody = new JPanel();
        pBody.setLayout(new BoxLayout(pBody, BoxLayout.Y_AXIS));
        pBody.setBackground(Color.WHITE);
        pBody.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Khối 1: Thông tin khách & phòng
        JPanel pInfo = new JPanel(new GridLayout(4, 2, 8, 8));
        pInfo.setBackground(new Color(248, 249, 250));
        pInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                new EmptyBorder(12, 14, 12, 14)));

        String tenKhach = (booking != null && !booking.getTenKhachHang().isEmpty()) ? booking.getTenKhachHang() : "Khách lưu trú";
        String sdt = (booking != null && !booking.getSoDienThoai().isEmpty()) ? booking.getSoDienThoai() : "---";
        String cccd = (booking != null && !booking.getCccd().isEmpty()) ? booking.getCccd() : "---";
        String soPhong = (booking != null && !booking.getSoPhong().isEmpty()) ? booking.getSoPhong() : "---";
        String loaiPhong = (booking != null && !booking.getTenLoaiPhong().isEmpty()) ? booking.getTenLoaiPhong() : "Tiêu chuẩn";

        Date dNhan = (booking != null && booking.getNgayNhanPhong() != null) ? booking.getNgayNhanPhong() : Date.valueOf(LocalDate.now());
        Date dTraHienTai = Date.valueOf(LocalDate.now());

        long soDem = 1;
        try {
            soDem = Math.max(1, ChronoUnit.DAYS.between(dNhan.toLocalDate(), dTraHienTai.toLocalDate()));
        } catch (Exception ignored) {}

        double donGia = (booking != null && booking.getGiaMoiDem() > 0) ? booking.getGiaMoiDem() : 300000;
        tienPhong = soDem * donGia;

        pInfo.add(taoLabel("Khách hàng:", true));
        pInfo.add(taoLabel(tenKhach + " (SĐT: " + sdt + ")", false));

        pInfo.add(taoLabel("Số CCCD:", true));
        pInfo.add(taoLabel(cccd, false));

        pInfo.add(taoLabel("Phòng lưu trú:", true));
        pInfo.add(taoLabel("P." + soPhong + " (" + loaiPhong + " - " + df.format(donGia) + "/đêm)", false));

        pInfo.add(taoLabel("Thời gian ở:", true));
        pInfo.add(taoLabel(dNhan + " đến " + dTraHienTai + " (" + soDem + " đêm)", false));

        pBody.add(pInfo);
        pBody.add(Box.createVerticalStrut(15));

        // Khối 2: Chi tiết tính tiền
        JPanel pCalc = new JPanel(new GridBagLayout());
        pCalc.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 4);

        // Tiền phòng
        g.gridx = 0; g.gridy = 0; g.weightx = 0.4;
        pCalc.add(new JLabel("Tiền phòng (" + soDem + " đêm):"), g);
        g.gridx = 1; g.gridy = 0; g.weightx = 0.6;
        JLabel lblTienPhong = new JLabel(df.format(tienPhong));
        lblTienPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pCalc.add(lblTienPhong, g);

        // Phụ thu
        g.gridx = 0; g.gridy = 1;
        pCalc.add(new JLabel("Phụ thu (dịch vụ, nước, đồ uống):"), g);
        g.gridx = 1; g.gridy = 1;
        txtPhuThu = new JTextField("0");
        txtPhuThu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pCalc.add(txtPhuThu, g);

        // Giảm giá
        g.gridx = 0; g.gridy = 2;
        pCalc.add(new JLabel("Giảm giá / Chiết khấu:"), g);
        g.gridx = 1; g.gridy = 2;
        txtGiamGia = new JTextField("0");
        txtGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pCalc.add(txtGiamGia, g);

        // Phương thức thanh toán
        g.gridx = 0; g.gridy = 3;
        pCalc.add(new JLabel("Phương thức thanh toán:"), g);
        g.gridx = 1; g.gridy = 3;
        cboPhuongThuc = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản QR", "Thẻ tín dụng / POS" });
        cboPhuongThuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pCalc.add(cboPhuongThuc, g);

        // TỔNG TIỀN PHẢI THANH TOÁN
        g.gridx = 0; g.gridy = 4;
        JLabel lblTongTieuDe = new JLabel("TỔNG THANH TOÁN:");
        lblTongTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTieuDe.setForeground(new Color(192, 57, 43));
        pCalc.add(lblTongTieuDe, g);

        g.gridx = 1; g.gridy = 4;
        lblTongTien = new JLabel(df.format(tienPhong));
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(new Color(192, 57, 43));
        pCalc.add(lblTongTien, g);

        pBody.add(pCalc);

        // Ghi chú nghiệp vụ
        pBody.add(Box.createVerticalStrut(10));
        JLabel lblNote = new JLabel("Lưu ý: Sau khi xác nhận thanh toán, phòng sẽ tự động chuyển sang ĐANG DỌN (CLEANING).");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(new Color(108, 117, 125));
        pBody.add(lblNote);

        add(pBody, BorderLayout.CENTER);

        // Tự động tính toán khi thay đổi phụ thu, giảm giá
        Runnable capNhatTong = () -> {
            try {
                double pt = parseDoubleSafe(txtPhuThu.getText());
                double gg = parseDoubleSafe(txtGiamGia.getText());
                tongThanhToan = Math.max(0, tienPhong + pt - gg);
                lblTongTien.setText(df.format(tongThanhToan));
            } catch (Exception ex) {
                lblTongTien.setText(df.format(tienPhong));
            }
        };

        txtPhuThu.addActionListener(e -> capNhatTong.run());
        txtGiamGia.addActionListener(e -> capNhatTong.run());
        txtPhuThu.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                capNhatTong.run();
            }
        });
        txtGiamGia.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                capNhatTong.run();
            }
        });

        // 3. Bottom Action Buttons
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pBottom.setBackground(new Color(245, 247, 250));
        pBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton btnInHoaDon = new JButton("In / Xem trước hóa đơn");
        btnInHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnInHoaDon.setFocusPainted(false);

        JButton btnXacNhan = new JButton("Xác nhận thanh toán & Trả phòng");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXacNhan.setBackground(new Color(192, 57, 43));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);

        JButton btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnHuy.setFocusPainted(false);

        pBottom.add(btnInHoaDon);
        pBottom.add(btnHuy);
        pBottom.add(btnXacNhan);
        add(pBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        // In / Xem trước phiếu thanh toán
        btnInHoaDon.addActionListener(e -> {
            capNhatTong.run();
            hienThiPhieuInHoaDon();
        });

        // Xác nhận thanh toán & Trả phòng
        btnXacNhan.addActionListener(e -> {
            capNhatTong.run();

            double pt = parseDoubleSafe(txtPhuThu.getText());
            double gg = parseDoubleSafe(txtGiamGia.getText());
            double tong = Math.max(0, tienPhong + pt - gg);
            String phuongThuc = (String) cboPhuongThuc.getSelectedItem();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Xác nhận đã nhận đủ số tiền: " + df.format(tong) + " (" + phuongThuc + ")?\n"
                            + "Hệ thống sẽ hoàn tất Check-out và chuyển Phòng " + booking.getSoPhong() + " sang ĐANG DỌN.",
                    "Xác nhận thanh toán & Trả phòng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) return;

            int bookingId = booking.getIdDatPhong();
            int roomId = booking.getMaPhong();

            boolean ok = bookingService.traPhongVaThanhToan(bookingId, roomId, tienPhong, pt, gg, tong, phuongThuc);
            if (ok) {
                completed = true;
                HoaDon hoaDon = new HoaDonService().timTheoDatPhong(bookingId);
                JOptionPane.showMessageDialog(this,
                        "Thanh toán và Check-out thành công!\n"
                                + "Tổng tiền: " + df.format(tong) + " (" + phuongThuc + ")\n"
                                + "Phòng " + booking.getSoPhong() + " đã chuyển sang trạng thái ĐANG DỌN."
                                + (hoaDon == null ? "\nKhông thể tải hóa đơn để xem trước."
                                        : "\nHóa đơn sẵn sàng để xem trước và in."),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                Window parent = getOwner();
                dispose();
                if (hoaDon != null) {
                    SwingUtilities.invokeLater(() -> DialogChiTietHoaDon.hienThiXemTruocHoaDon(parent, hoaDon));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thực hiện trả phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void hienThiPhieuInHoaDon() {
        JDialog dlgIn = new JDialog(this, "Phiếu thanh toán khách sạn", true);
        dlgIn.setSize(480, 560);
        dlgIn.setLocationRelativeTo(this);
        dlgIn.setLayout(new BorderLayout());

        JTextArea txtBill = new JTextArea();
        txtBill.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBill.setEditable(false);
        txtBill.setMargin(new Insets(15, 20, 15, 20));

        double pt = parseDoubleSafe(txtPhuThu.getText());
        double gg = parseDoubleSafe(txtGiamGia.getText());
        double tong = Math.max(0, tienPhong + pt - gg);
        String phuongThuc = (String) cboPhuongThuc.getSelectedItem();

        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("             KHÁCH SẠN HOTEL MANAGEMENT         \n");
        sb.append("         Địa chỉ: 123 Đường ABC, TP. Hà Nội     \n");
        sb.append("             Điện thoại: 024.3828.9999          \n");
        sb.append("------------------------------------------------\n");
        sb.append("             HÓA ĐƠN THANH TOÁN (BILL)          \n");
        sb.append("------------------------------------------------\n");
        sb.append(String.format("Mã đơn đặt phòng : #%d\n", booking.getIdDatPhong()));
        sb.append(String.format("Khách hàng       : %s\n", booking.getTenKhachHang()));
        sb.append(String.format("Số CCCD          : %s\n", booking.getCccd()));
        sb.append(String.format("Số điện thoại    : %s\n", booking.getSoDienThoai()));
        sb.append(String.format("Phòng            : P.%s (%s)\n", booking.getSoPhong(), booking.getTenLoaiPhong()));
        sb.append(String.format("Ngày nhận phòng  : %s\n", booking.getNgayNhanPhong()));
        sb.append(String.format("Ngày trả phòng   : %s\n", LocalDate.now()));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("Tiền phòng       : %15s\n", df.format(tienPhong)));
        sb.append(String.format("Phụ thu          : %15s\n", df.format(pt)));
        sb.append(String.format("Giảm giá         : %15s\n", "-" + df.format(gg)));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("TỔNG CỘNG        : %15s\n", df.format(tong)));
        sb.append(String.format("Phương thức TT   : %s\n", phuongThuc));
        sb.append("================================================\n");
        sb.append("       Cảm ơn Quý khách & Hẹn gặp lại!          \n");

        txtBill.setText(sb.toString());

        JScrollPane sp = new JScrollPane(txtBill);
        dlgIn.add(sp, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(ev -> dlgIn.dispose());
        pBtn.add(btnDong);
        dlgIn.add(pBtn, BorderLayout.SOUTH);

        dlgIn.setVisible(true);
    }

    private double parseDoubleSafe(String s) {
        if (s == null) return 0;
        try {
            return Double.parseDouble(s.trim().replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private JLabel taoLabel(String text, boolean bold) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        if (bold) lbl.setForeground(new Color(51, 65, 85));
        return lbl;
    }

    public boolean isCompleted() {
        return completed;
    }
}
