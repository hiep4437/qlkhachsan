package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.invoices;
import service.InvoiceService;

public class DialogChiTietHoaDon extends JDialog {

    private invoices invoice;
    private InvoiceService invoiceService = new InvoiceService();
    private DecimalFormat df = new DecimalFormat("#,### đ");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean daThanhToanXong = false;

    public DialogChiTietHoaDon(Window parent, invoices inv) {
        super(parent, "Chi tiết hóa đơn thanh toán #" + (inv != null ? inv.getIdHoaDon() : ""), ModalityType.APPLICATION_MODAL);
        this.invoice = inv;

        setSize(540, 620);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        if (invoice == null) return;

        // Top Header
        JPanel pHead = new JPanel(new BorderLayout());
        pHead.setBackground(new Color(24, 44, 97));
        pHead.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN #" + invoice.getIdHoaDon());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        String st = invoice.getTrangThaiThanhToan();
        String stText = (st != null && (st.contains("PAID") || st.contains("Đã thanh toán"))) ? "ĐÃ THANH TOÁN" : "CHƯA THANH TOÁN";
        JLabel lblStatus = new JLabel(stText);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(stText.equals("ĐÃ THANH TOÁN") ? new Color(46, 204, 113) : new Color(241, 196, 15));

        pHead.add(lblTitle, BorderLayout.WEST);
        pHead.add(lblStatus, BorderLayout.EAST);
        add(pHead, BorderLayout.NORTH);

        // Center Content
        JPanel pBody = new JPanel();
        pBody.setLayout(new BoxLayout(pBody, BoxLayout.Y_AXIS));
        pBody.setBackground(Color.WHITE);
        pBody.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Block 1: Thông tin phòng & khách
        JPanel pInfo = new JPanel(new GridLayout(4, 2, 10, 8));
        pInfo.setBackground(new Color(248, 249, 250));
        pInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                new EmptyBorder(12, 14, 12, 14)));

        String soPhong = !invoice.getSoPhong().isEmpty() ? "Phòng " + invoice.getSoPhong() : "---";
        String tenKhach = !invoice.getTenKhachHang().isEmpty() ? invoice.getTenKhachHang() : "---";
        String sdt = !invoice.getSoDienThoai().isEmpty() ? invoice.getSoDienThoai() : "---";
        String cccd = !invoice.getSoCCCD().isEmpty() ? invoice.getSoCCCD() : "---";
        String ngayLap = invoice.getNgayLap() != null ? sdf.format(invoice.getNgayLap()) : "---";

        pInfo.add(taoLabel("Phòng thanh toán:", true));
        pInfo.add(taoLabel(soPhong + " (" + invoice.getTenLoaiPhong() + ")", false));

        pInfo.add(taoLabel("Khách hàng:", true));
        pInfo.add(taoLabel(tenKhach + " (SĐT: " + sdt + ")", false));

        pInfo.add(taoLabel("Số CCCD / CMND:", true));
        pInfo.add(taoLabel(cccd, false));

        pInfo.add(taoLabel("Thời gian lập HĐ:", true));
        pInfo.add(taoLabel(ngayLap, false));

        pBody.add(pInfo);
        pBody.add(Box.createVerticalStrut(15));

        // Block 2: Chi tiết các khoản tiền
        JPanel pMoney = new JPanel(new GridLayout(5, 2, 10, 10));
        pMoney.setOpaque(false);
        pMoney.setBorder(new EmptyBorder(10, 10, 10, 10));

        pMoney.add(taoLabel("Tiền phòng:", false));
        pMoney.add(taoMoneyLabel(df.format(invoice.getTienPhong())));

        pMoney.add(taoLabel("Phụ thu (dịch vụ):", false));
        pMoney.add(taoMoneyLabel(df.format(invoice.getPhuThu())));

        pMoney.add(taoLabel("Giảm giá / Ưu đãi:", false));
        pMoney.add(taoMoneyLabel("-" + df.format(invoice.getGiamGia())));

        pMoney.add(taoLabel("Phương thức thanh toán:", false));
        pMoney.add(taoLabel(invoice.getPhuongThucThanhToan() != null ? invoice.getPhuongThucThanhToan() : "Tiền mặt", true));

        JLabel lblTongTitle = new JLabel("TỔNG TIỀN THANH TOÁN:");
        lblTongTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTitle.setForeground(new Color(192, 57, 43));
        pMoney.add(lblTongTitle);

        JLabel lblTongVal = new JLabel(df.format(invoice.getTongTien()));
        lblTongVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongVal.setForeground(new Color(192, 57, 43));
        lblTongVal.setHorizontalAlignment(SwingConstants.RIGHT);
        pMoney.add(lblTongVal);

        pBody.add(pMoney);
        add(pBody, BorderLayout.CENTER);

        // Bottom buttons
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pBottom.setBackground(new Color(245, 247, 250));
        pBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton btnIn = new JButton("In hóa đơn (Bill)");
        btnIn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnIn.setFocusPainted(false);

        JButton btnThanhToanNgay = new JButton("Xác nhận thanh toán ngay");
        btnThanhToanNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThanhToanNgay.setBackground(new Color(39, 174, 96));
        btnThanhToanNgay.setForeground(Color.WHITE);
        btnThanhToanNgay.setFocusPainted(false);

        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnDong.setFocusPainted(false);

        // Ẩn nút "Thanh toán ngay" nếu đã thanh toán
        if (stText.equals("ĐÃ THANH TOÁN")) {
            btnThanhToanNgay.setVisible(false);
        }

        pBottom.add(btnIn);
        if (btnThanhToanNgay.isVisible()) {
            pBottom.add(btnThanhToanNgay);
        }
        pBottom.add(btnDong);
        add(pBottom, BorderLayout.SOUTH);

        btnDong.addActionListener(e -> dispose());

        btnThanhToanNgay.addActionListener(e -> {
            int cf = JOptionPane.showConfirmDialog(
                    this,
                    "Xác nhận đã nhận đủ " + df.format(invoice.getTongTien()) + " cho Hóa đơn #" + invoice.getIdHoaDon() + "?",
                    "Xác nhận thanh toán",
                    JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                if (invoiceService.capNhatTrangThai(invoice.getIdHoaDon(), "PAID", invoice.getPhuongThucThanhToan()) > 0) {
                    daThanhToanXong = true;
                    JOptionPane.showMessageDialog(this, "Hóa đơn đã được thanh toán thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnIn.addActionListener(e -> inHoaDon());
    }

    private void inHoaDon() {
        JDialog dlgIn = new JDialog(this, "In hóa đơn #" + invoice.getIdHoaDon(), true);
        dlgIn.setSize(460, 520);
        dlgIn.setLocationRelativeTo(this);
        dlgIn.setLayout(new BorderLayout());

        JTextArea txt = new JTextArea();
        txt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txt.setEditable(false);
        txt.setMargin(new Insets(15, 20, 15, 20));

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("            KHÁCH SẠN HOTEL MANAGEMENT        \n");
        sb.append("        Địa chỉ: 123 Đường ABC, TP. Hà Nội    \n");
        sb.append("----------------------------------------------\n");
        sb.append("            PHIẾU THANH TOÁN HÓA ĐƠN          \n");
        sb.append("----------------------------------------------\n");
        sb.append(String.format("Mã hóa đơn     : #%d\n", invoice.getIdHoaDon()));
        sb.append(String.format("Mã đặt phòng   : #%d\n", invoice.getIdDatPhong()));
        sb.append(String.format("Phòng          : %s (%s)\n", invoice.getSoPhong(), invoice.getTenLoaiPhong()));
        sb.append(String.format("Khách hàng     : %s\n", invoice.getTenKhachHang()));
        sb.append(String.format("Số CCCD        : %s\n", invoice.getSoCCCD()));
        sb.append(String.format("Số điện thoại  : %s\n", invoice.getSoDienThoai()));
        sb.append(String.format("Thời gian lập  : %s\n", invoice.getNgayLap() != null ? sdf.format(invoice.getNgayLap()) : ""));
        sb.append("----------------------------------------------\n");
        sb.append(String.format("Tiền phòng     : %15s\n", df.format(invoice.getTienPhong())));
        sb.append(String.format("Phụ thu        : %15s\n", df.format(invoice.getPhuThu())));
        sb.append(String.format("Giảm giá       : %15s\n", "-" + df.format(invoice.getGiamGia())));
        sb.append("----------------------------------------------\n");
        sb.append(String.format("TỔNG TIỀN      : %15s\n", df.format(invoice.getTongTien())));
        sb.append(String.format("Phương thức TT : %s\n", invoice.getPhuongThucThanhToan()));
        sb.append(String.format("Trạng thái     : %s\n", invoice.getTrangThaiThanhToan()));
        sb.append("==============================================\n");
        sb.append("      Xin cảm ơn Quý khách & Hẹn gặp lại!     \n");

        txt.setText(sb.toString());
        dlgIn.add(new JScrollPane(txt), BorderLayout.CENTER);

        JPanel pB = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(ev -> dlgIn.dispose());
        pB.add(btnClose);
        dlgIn.add(pB, BorderLayout.SOUTH);

        dlgIn.setVisible(true);
    }

    private JLabel taoLabel(String text, boolean bold) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        if (bold) lbl.setForeground(new Color(51, 65, 85));
        return lbl;
    }

    private JLabel taoMoneyLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setForeground(new Color(30, 41, 59));
        return lbl;
    }

    public boolean isDaThanhToanXong() {
        return daThanhToanXong;
    }
}

