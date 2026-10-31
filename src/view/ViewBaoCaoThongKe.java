package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ViewBaoCaoThongKe extends JFrame {

    // Cards KPI
    public JLabel lblValTongDoanhThu;
    public JLabel lblValDoanhThuThang;
    public JLabel lblValDoanhThuNgay;
    // Bộ lọc
    public JComboBox<String> cboKyBaoCao;
    public JButton btnLoc;
    public JButton btnLamMoi;
    public JLabel lblThongKeKy;

    // Bảng 1: Doanh thu theo phòng
    public JTable tblPhong;
    public DefaultTableModel modelPhong;

    // Bảng 2: Danh sách hóa đơn trong kỳ
    public JTable tblHoaDon;
    public DefaultTableModel modelHoaDon;

    public ViewBaoCaoThongKe() {
        setTitle("Báo cáo thống kê doanh thu");
        AppWindowIcon.apply(this);
        setSize(1180, 720);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pMain = new JPanel(new BorderLayout(0, 12));
        pMain.setBackground(new Color(245, 247, 250));
        pMain.setBorder(new EmptyBorder(15, 18, 15, 18));

        // ================= TOP: HEADER & KPI CARDS =================
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
        pTop.setOpaque(false);

        // Header Title
        JPanel pTitle = new JPanel(new BorderLayout());
        pTitle.setOpaque(false);

        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 41, 59));

        JLabel lblBadgeAdmin = new JLabel("DÀNH CHO QUẢN TRỊ VIÊN");
        lblBadgeAdmin.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadgeAdmin.setForeground(new Color(192, 57, 43));
        lblBadgeAdmin.setBackground(new Color(253, 237, 236));
        lblBadgeAdmin.setOpaque(true);
        lblBadgeAdmin.setBorder(new EmptyBorder(4, 8, 4, 8));

        pTitle.add(lblTitle, BorderLayout.WEST);
        pTitle.add(lblBadgeAdmin, BorderLayout.EAST);
        pTop.add(pTitle);
        pTop.add(Box.createVerticalStrut(12));

        // 3 KPI Cards Panel
        JPanel pCards = new JPanel(new GridLayout(1, 3, 12, 0));
        pCards.setOpaque(false);

        // Card 1: Tổng doanh thu lũy kế
        lblValTongDoanhThu = new JLabel("0 đ");
        pCards.add(taoCardKPI("TỔNG DOANH THU LŨY KẾ", lblValTongDoanhThu, "Tất cả hóa đơn đã thanh toán",
                new Color(39, 174, 96), new Color(234, 250, 241)));

        // Card 2: Doanh thu tháng này
        lblValDoanhThuThang = new JLabel("0 đ");
        pCards.add(taoCardKPI("DOANH THU THÁNG NÀY", lblValDoanhThuThang, "Tháng hiện tại",
                new Color(41, 128, 185), new Color(235, 245, 251)));

        // Card 3: Doanh thu hôm nay
        lblValDoanhThuNgay = new JLabel("0 đ");
        pCards.add(taoCardKPI("DOANH THU HÔM NAY", lblValDoanhThuNgay, "Doanh thu trong ngày",
                new Color(211, 84, 0), new Color(254, 249, 231)));

        pTop.add(pCards);
        pTop.add(Box.createVerticalStrut(12));

        // Thanh công cụ bộ lọc
        JPanel pToolbar = new JPanel(new BorderLayout());
        pToolbar.setOpaque(false);

        JPanel pFilterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pFilterLeft.setOpaque(false);

        JLabel lblKy = new JLabel("Kỳ báo cáo:");
        lblKy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKy.setForeground(new Color(51, 65, 85));

        cboKyBaoCao = new JComboBox<>(new String[] {
            "Tháng này",
            "Hôm nay",
            "7 ngày qua",
            "Tháng trước",
            "Năm nay",
            "Toàn bộ thời gian"
        });
        cboKyBaoCao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboKyBaoCao.setPreferredSize(new Dimension(160, 34));

        btnLoc = new JButton("Xem báo cáo");
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setBackground(new Color(13, 110, 253));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setPreferredSize(new Dimension(130, 34));
        btnLoc.setFocusPainted(false);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLamMoi.setPreferredSize(new Dimension(110, 34));
        btnLamMoi.setFocusPainted(false);

        pFilterLeft.add(lblKy);
        pFilterLeft.add(cboKyBaoCao);
        pFilterLeft.add(btnLoc);
        pFilterLeft.add(btnLamMoi);

        lblThongKeKy = new JLabel("Tổng thực thu trong kỳ: 0 đ");
        lblThongKeKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblThongKeKy.setForeground(new Color(192, 57, 43));

        pToolbar.add(pFilterLeft, BorderLayout.WEST);
        pToolbar.add(lblThongKeKy, BorderLayout.EAST);

        pTop.add(pToolbar);
        pMain.add(pTop, BorderLayout.NORTH);

        // ================= CENTER: TABS DỮ LIỆU BÁO CÁO =================
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab con 1: Doanh thu theo phòng
        JPanel tabPhong = new JPanel(new BorderLayout());
        modelPhong = new DefaultTableModel(
                new String[] { "Số phòng", "Hạng loại phòng", "Vị trí tầng", "Lượt khách thuê", "Tổng doanh thu đóng góp", "Tỷ trọng (%)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblPhong = new JTable(modelPhong);
        dinhDangTable(tblPhong);

        JScrollPane spPhong = new JScrollPane(tblPhong);
        spPhong.getViewport().setBackground(Color.WHITE);
        tabPhong.add(spPhong, BorderLayout.CENTER);
        subTabs.addTab("Doanh thu theo từng phòng", tabPhong);

        // Tab con 2: Chi tiết các hóa đơn thanh toán trong kỳ
        JPanel tabHoaDon = new JPanel(new BorderLayout());
        modelHoaDon = new DefaultTableModel(
                new String[] { "Mã HĐ", "Thời gian thanh toán", "Phòng", "Khách hàng", "Số điện thoại",
                               "Tiền phòng", "Phụ thu", "Giảm giá", "Thực thu (Tổng)", "Phương thức TT" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        dinhDangTable(tblHoaDon);

        JScrollPane spHoaDon = new JScrollPane(tblHoaDon);
        spHoaDon.getViewport().setBackground(Color.WHITE);
        tabHoaDon.add(spHoaDon, BorderLayout.CENTER);
        subTabs.addTab("Chi tiết các hóa đơn đã thanh toán trong kỳ", tabHoaDon);

        pMain.add(subTabs, BorderLayout.CENTER);
        setContentPane(pMain);
    }

    private JPanel taoCardKPI(String tieuDe, JLabel lblVal, String sub, Color fgColor, Color bgColor) {
        JLabel lblSub = (sub != null) ? new JLabel(sub) : new JLabel();
        return taoCardKPI(tieuDe, lblVal, lblSub, fgColor, bgColor);
    }

    private JPanel taoCardKPI(String tieuDe, JLabel lblVal, JLabel lblSub, Color fgColor, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fgColor, 1),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel lblTitle = new JLabel(tieuDe);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(new Color(100, 110, 120));

        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(fgColor);

        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(120, 130, 140));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(lblVal);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSub);

        return card;
    }

    private void dinhDangTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(230, 235, 240));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(new Color(225, 238, 255));
        table.setSelectionForeground(new Color(15, 23, 42));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(238, 242, 246));
        header.setForeground(new Color(30, 41, 59));
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
    }

    public void batDauDemNguocLamMoi(Runnable onRefresh) {
        btnLamMoi.setEnabled(false);
        final int[] giayConLai = { 5 };
        btnLamMoi.setText("Đang tải (" + giayConLai[0] + "s)");

        if (onRefresh != null) {
            onRefresh.run();
        }

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            giayConLai[0]--;
            if (giayConLai[0] > 0) {
                btnLamMoi.setText("Đang tải (" + giayConLai[0] + "s)");
            } else {
                timer.stop();
                btnLamMoi.setText("Làm mới");
                btnLamMoi.setEnabled(true);
            }
        });
        timer.start();
    }
}
