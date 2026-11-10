package view.view;

import view.ChonNgay;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.time.LocalDate;
import java.time.YearMonth;
import view.IConn;

public class ViewBaoCaoThongKe extends JFrame {

    private static final Color MAU_VANBAN = new Color(30, 41, 59);
    private static final Color MAU_MUTED = new Color(100, 116, 139);
    private static final Color MAU_GREEN = new Color(16, 185, 129);
    private static final Color MAU_HOPHACH = new Color(245, 158, 11);

    public JLabel nhanGiaTriChungDoanhThu;
    public JLabel nhanGiaTriDoanhThuThang;
    public JLabel nhanGiaTriDoanhThuNgay;
    public JComboBox<String> hopChonKyBaoCao;
    public ChonNgay oNhapTuNgay;
    public ChonNgay oNhapDenNgay;
    public JButton btLoc;
    public JLabel nhanThongKeKy;
    public JTable bangPhong;
    public DefaultTableModel moHinhPhong;
    public JTable bangHoaDon;
    public DefaultTableModel moHinhHoaDon;

    private final BieuDoDoanhThuPhong bieuThucHienDoanhThuPhong = new BieuDoDoanhThuPhong();
    private LocalDate tuNgay = LocalDate.now().withDayOfMonth(1);
    private LocalDate denNgay = LocalDate.now();
    private boolean dangCapNhatNgay;

    public ViewBaoCaoThongKe() {
        setTitle("Báo cáo doanh thu");
        IConn.apply(this);
        setSize(1240, 760);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel chinh = new JPanel(new BorderLayout(0, 16));
        chinh.setBackground(Color.WHITE);
        chinh.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel tren = new JPanel();
        tren.setOpaque(false);
        tren.setLayout(new BoxLayout(tren, BoxLayout.Y_AXIS));
        tren.add(taoTieuDe());
        tren.add(Box.createVerticalStrut(18));
        tren.add(taoCardsDoanhThu());
        tren.add(Box.createVerticalStrut(16));
        tren.add(taoBoLoc());

        chinh.add(tren, BorderLayout.NORTH);
        chinh.add(taoNoiDungBaoCao(), BorderLayout.CENTER);
        setContentPane(chinh);
    }

    private JPanel taoTieuDe() {
        JPanel khung = new JPanel(new BorderLayout(12, 0));
        khung.setOpaque(false);

        JPanel cacTieuDe = new JPanel();
        cacTieuDe.setOpaque(false);
        cacTieuDe.setLayout(new BoxLayout(cacTieuDe, BoxLayout.Y_AXIS));

        JLabel deMuc = new JLabel("Báo cáo doanh thu");
        deMuc.setFont(new Font("Segoe UI", Font.BOLD, 25));
        deMuc.setForeground(MAU_VANBAN);

        JLabel phuDe = new JLabel("Tổng quan doanh thu và hiệu quả kinh doanh của khách sạn");
        phuDe.setForeground(MAU_MUTED);

        cacTieuDe.add(deMuc);
        cacTieuDe.add(Box.createVerticalStrut(4));
        cacTieuDe.add(phuDe);

        JLabel huyHieu = new JLabel("THỐNG KÊ TÀI CHÍNH");
        huyHieu.setForeground(Color.BLUE);
        huyHieu.setBackground(new Color(239, 246, 255));
        huyHieu.setOpaque(true);
        huyHieu.setBorder(new EmptyBorder(8, 12, 8, 12));

        khung.add(cacTieuDe, BorderLayout.WEST);
        khung.add(huyHieu, BorderLayout.EAST);
        return khung;
    }

    private JPanel taoCardsDoanhThu() {
        JPanel cacThe = new JPanel(new java.awt.GridLayout(1, 3, 14, 0));
        cacThe.setOpaque(false);

        nhanGiaTriChungDoanhThu = new JLabel("0 đ");
        nhanGiaTriDoanhThuThang = new JLabel("0 đ");
        nhanGiaTriDoanhThuNgay = new JLabel("0 đ");

        cacThe.add(taoCardDoanhThu(
                "DOANH THU KỲ", "Tổng tiền đã thu trong khoảng chọn",
                nhanGiaTriChungDoanhThu, Color.BLUE));
        cacThe.add(taoCardDoanhThu(
                "HÓA ĐƠN ĐÃ THU", "Số hóa đơn thanh toán trong kỳ",
                nhanGiaTriDoanhThuThang, MAU_GREEN));
        cacThe.add(taoCardDoanhThu(
                "LƯỢT THUÊ PHÒNG", "Số lượt thuê có phát sinh doanh thu",
                nhanGiaTriDoanhThuNgay, MAU_HOPHACH));
        return cacThe;
    }

    private JPanel taoCardDoanhThu(String deMuc, String phuDe, JLabel giaTri, Color diemNhan) {
        JPanel the = new JPanel(new BorderLayout(12, 0));
        the.setBackground(Color.WHITE);
        the.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel thanhDiemNhan = new JPanel();
        thanhDiemNhan.setBackground(diemNhan);
        thanhDiemNhan.setPreferredSize(new Dimension(4, 56));
        the.add(thanhDiemNhan, BorderLayout.WEST);

        JPanel chiTiet = new JPanel();
        chiTiet.setOpaque(false);
        chiTiet.setLayout(new BoxLayout(chiTiet, BoxLayout.Y_AXIS));

        JLabel tieuDe = new JLabel(deMuc);
        tieuDe.setForeground(MAU_MUTED);

        giaTri.setFont(new Font("Segoe UI", Font.BOLD, 24));
        giaTri.setForeground(MAU_VANBAN);

        JLabel moTa = new JLabel(phuDe);
        moTa.setForeground(MAU_MUTED);

        chiTiet.add(tieuDe);
        chiTiet.add(Box.createVerticalStrut(7));
        chiTiet.add(giaTri);
        chiTiet.add(Box.createVerticalStrut(5));
        chiTiet.add(moTa);
        the.add(chiTiet, BorderLayout.CENTER);
        return the;
    }

    private JPanel taoBoLoc() {
        JPanel khung = new JPanel(new BorderLayout(16, 0));
        khung.setBackground(Color.WHITE);
        khung.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(12, 16, 12, 16)));

        JPanel dieuKhien = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        dieuKhien.setOpaque(false);

        JLabel luaChonSanNhan = new JLabel("Kỳ nhanh");
        luaChonSanNhan.setForeground(MAU_VANBAN);

        hopChonKyBaoCao = new JComboBox<>(new String[] {
            "Tháng này",
            "Hôm nay",
            "7 ngày gần đây",
            "30 ngày gần đây",
            "Tháng trước",
            "Năm nay",
            "Toàn bộ thời gian",
            "Tùy chỉnh"
        });
        hopChonKyBaoCao.setPreferredSize(new Dimension(150, 36));
        hopChonKyBaoCao.addActionListener(ac -> capNhatTheoKyNhanh());

        JLabel tuNgayNhan = new JLabel("Từ ngày");
        tuNgayNhan.setForeground(MAU_VANBAN);
        oNhapTuNgay = new ChonNgay(tuNgay, null);
        oNhapTuNgay.addDateChangeListener(ac -> {
            tuNgay = oNhapTuNgay.getDate();
            if (!dangCapNhatNgay) {
                datKyTuyChinh();
            }
        });

        JLabel denNgayNhan = new JLabel("Đến ngày");
        denNgayNhan.setForeground(MAU_VANBAN);
        oNhapDenNgay = new ChonNgay(denNgay, null);
        oNhapDenNgay.addDateChangeListener(ac -> {
            denNgay = oNhapDenNgay.getDate();
            if (!dangCapNhatNgay) {
                datKyTuyChinh();
            }
        });

        btLoc = new JButton("Xem thống kê");
        btLoc.setPreferredSize(new Dimension(136, 38));
        btLoc.setMaximumSize(new Dimension(136, 38));

        dieuKhien.add(luaChonSanNhan);
        dieuKhien.add(hopChonKyBaoCao);
        dieuKhien.add(tuNgayNhan);
        dieuKhien.add(oNhapTuNgay);
        dieuKhien.add(denNgayNhan);
        dieuKhien.add(oNhapDenNgay);
        dieuKhien.add(btLoc);

        nhanThongKeKy = new JLabel("Thực thu trong kỳ: 0 đ");
        nhanThongKeKy.setForeground(Color.BLUE);
        nhanThongKeKy.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel noiDung = new JPanel();
        noiDung.setOpaque(false);
        noiDung.setLayout(new BoxLayout(noiDung, BoxLayout.Y_AXIS));
        noiDung.add(dieuKhien);
        noiDung.add(Box.createVerticalStrut(8));
        noiDung.add(nhanThongKeKy);
        khung.add(noiDung, BorderLayout.CENTER);
        return khung;
    }

    private void capNhatTheoKyNhanh() {
        String luaChonSan = (String) hopChonKyBaoCao.getSelectedItem();
        if (luaChonSan == null || luaChonSan.equals("Tùy chỉnh")) {
            return;
        }

        LocalDate homNay = LocalDate.now();
        switch (luaChonSan) {
            case "Hôm nay":
                tuNgay = homNay;
                denNgay = homNay;
                break;
            case "7 ngày gần đây":
                tuNgay = homNay.minusDays(6);
                denNgay = homNay;
                break;
            case "30 ngày gần đây":
                tuNgay = homNay.minusDays(29);
                denNgay = homNay;
                break;
            case "Tháng trước":
                YearMonth truocThang = YearMonth.from(homNay).minusMonths(1);
                tuNgay = truocThang.atDay(1);
                denNgay = truocThang.atEndOfMonth();
                break;
            case "Năm nay":
                tuNgay = homNay.withDayOfYear(1);
                denNgay = homNay;
                break;
            case "Toàn bộ thời gian":
                tuNgay = LocalDate.of(2026, 1, 1);
                denNgay = homNay;
                break;
            case "Tháng này":
            default:
                tuNgay = homNay.withDayOfMonth(1);
                denNgay = homNay;
                break;
        }
        capNhatONgay();
    }

    private void datKyTuyChinh() {
        if (hopChonKyBaoCao.getSelectedItem() != null
                && !hopChonKyBaoCao.getSelectedItem().equals("Tùy chỉnh")) {
            hopChonKyBaoCao.setSelectedItem("Tùy chỉnh");
        }
    }

    private void capNhatONgay() {
        dangCapNhatNgay = true;
        try {
            oNhapTuNgay.setDate(tuNgay);
            oNhapDenNgay.setDate(denNgay);
        } finally {
            dangCapNhatNgay = false;
        }
    }

    private JTabbedPane taoNoiDungBaoCao() {
        JTabbedPane cacTab = new JTabbedPane();
        cacTab.setBackground(Color.WHITE);
        cacTab.setBorder(BorderFactory.createEmptyBorder());

        moHinhPhong = new DefaultTableModel(new String[] {
            "Phòng", "Loại phòng", "Tầng", "Lượt thuê", "Doanh thu", "Tỷ trọng"
        }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }
        };
        bangPhong = taoBang(moHinhPhong);
        bangPhong.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dinhChieuCotPhong();

        JPanel thePhong = new JPanel(new java.awt.GridLayout(1, 2, 14, 0));
        thePhong.setOpaque(false);
        thePhong.setBorder(new EmptyBorder(14, 2, 2, 2));
        thePhong.add(taoTheNoiDung(
                "Doanh thu theo phòng",
                "Top phòng đóng góp doanh thu trong kỳ đã chọn",
                bieuThucHienDoanhThuPhong));
        thePhong.add(taoTheNoiDung(
                "Hiệu quả từng phòng",
                "Doanh thu và lượt thuê được sắp xếp theo doanh thu",
                new JScrollPane(bangPhong)));
        cacTab.addTab("Tổng quan theo phòng", thePhong);

        moHinhHoaDon = new DefaultTableModel(new String[] {
            "Mã HĐ", "Thời gian", "Phòng", "Khách hàng", "Số điện thoại",
            "Tiền phòng", "Phụ thu", "Giảm giá", "Thực thu", "Thanh toán"
        }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }
        };
        bangHoaDon = taoBang(moHinhHoaDon);
        bangHoaDon.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dinhChieuCotHoaDon();

        cacTab.addTab("Chi tiết hóa đơn", taoTheNoiDung(
                "Hóa đơn đã thanh toán",
                "Danh sách giao dịch trong kỳ báo cáo",
                new JScrollPane(bangHoaDon)));
        return cacTab;
    }

    private JPanel taoTheNoiDung(String deMuc, String phuDe, java.awt.Component noiDung) {
        JPanel the = new JPanel(new BorderLayout(0, 12));
        the.setBackground(Color.WHITE);
        the.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel tieuDe = new JPanel();
        tieuDe.setOpaque(false);
        tieuDe.setLayout(new BoxLayout(tieuDe, BoxLayout.Y_AXIS));

        JLabel nhanTieuDe = new JLabel(deMuc);
        nhanTieuDe.setForeground(MAU_VANBAN);

        JLabel nhanPhuDe = new JLabel(phuDe);
        nhanPhuDe.setForeground(MAU_MUTED);

        tieuDe.add(nhanTieuDe);
        tieuDe.add(Box.createVerticalStrut(4));
        tieuDe.add(nhanPhuDe);

        if (noiDung instanceof JScrollPane) {
            JScrollPane cuonPane = (JScrollPane) noiDung;
            cuonPane.setBorder(BorderFactory.createEmptyBorder());
            cuonPane.getViewport().setBackground(Color.WHITE);
            cuonPane.getVerticalScrollBar().setUnitIncrement(16);
        }

        the.add(tieuDe, BorderLayout.NORTH);
        the.add(noiDung, BorderLayout.CENTER);
        return the;
    }

    private JTable taoBang(DefaultTableModel moHinh) {
        JTable bang = new JTable(moHinh);
        bang.setRowHeight(36);
        bang.setFillsViewportHeight(true);
        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.setShowVerticalLines(false);
        bang.setShowHorizontalLines(true);
        bang.setGridColor(new Color(241, 245, 249));
        bang.setSelectionBackground(new Color(239, 246, 255));
        bang.setSelectionForeground(MAU_VANBAN);
        bang.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer boKetXuat = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable nguon, Object giaTri, boolean duocChon, boolean dangDuocChon, int hang, int chiSoCot) {
                java.awt.Component thanhPhan = super.getTableCellRendererComponent(
                        nguon, giaTri, duocChon, dangDuocChon, hang, chiSoCot);
                if (!duocChon) {
                    thanhPhan.setBackground(hang % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    thanhPhan.setForeground(MAU_VANBAN);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return thanhPhan;
            }
        };
        bang.setDefaultRenderer(Object.class, boKetXuat);

        JTableHeader tieuDe = bang.getTableHeader();
        tieuDe.setForeground(MAU_MUTED);
        tieuDe.setBackground(new Color(248, 250, 252));
        tieuDe.setReorderingAllowed(false);
        tieuDe.setPreferredSize(new Dimension(tieuDe.getPreferredSize().width, 40));
        return bang;
    }

    private void dinhChieuCotPhong() {
        int[] cacDoRong = { 66, 104, 48, 78, 118, 72 };
        for (int chiSo = 0; chiSo < cacDoRong.length; chiSo++) {
            bangPhong.getColumnModel().getColumn(chiSo).setPreferredWidth(cacDoRong[chiSo]);
        }
    }

    private void dinhChieuCotHoaDon() {
        int[] cacDoRong = { 62, 126, 70, 140, 112, 98, 84, 84, 104, 110 };
        for (int chiSo = 0; chiSo < cacDoRong.length; chiSo++) {
            bangHoaDon.getColumnModel().getColumn(chiSo).setPreferredWidth(cacDoRong[chiSo]);
        }
    }

    public void capNhatBieuDoPhong(List<String> tenPhong, List<String> chiTiet, List<Double> doanhThu) {
        bieuThucHienDoanhThuPhong.capNhat(tenPhong, chiTiet, doanhThu);
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    private static class BieuDoDoanhThuPhong extends JPanel {
        private static final int TOIDA_CACHANG = 6;
        private final List<DongBieuDo> cacHang = new ArrayList<>();

        private BieuDoDoanhThuPhong() {
            setOpaque(false);
            setPreferredSize(new Dimension(300, 330));
        }

        private void capNhat(List<String> tenPhong, List<String> chiTiet, List<Double> doanhThu) {
            cacHang.clear();
            int soLuong = Math.min(TOIDA_CACHANG, Math.min(tenPhong.size(), Math.min(chiTiet.size(), doanhThu.size())));
            for (int chiSo = 0; chiSo < soLuong; chiSo++) {
                cacHang.add(new DongBieuDo(tenPhong.get(chiSo), chiTiet.get(chiSo), doanhThu.get(chiSo)));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics doHoa) {
            super.paintComponent(doHoa);
            Graphics2D gia = (Graphics2D) doHoa.create();
            gia.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (cacHang.isEmpty()) {
                gia.setColor(MAU_MUTED);
                String thongDiep = "Chưa có dữ liệu doanh thu theo phòng";
                int vanBanX = Math.max(12, (getWidth() - gia.getFontMetrics().stringWidth(thongDiep)) / 2);
                gia.drawString(thongDiep, vanBanX, Math.max(36, getHeight() / 2));
                gia.dispose();
                return;
            }

            double doanhThuToiDa = 0;
            for (DongBieuDo hang : cacHang) {
                doanhThuToiDa = Math.max(doanhThuToiDa, hang.doanhThu);
            }
            doanhThuToiDa = Math.max(1, doanhThuToiDa);

            int trai = 2;
            int phai = 2;
            int leTrenThanh = 31;
            int chieuCaoHang = Math.max(47, (getHeight() - 8) / cacHang.size());
            int doRongThanh = Math.max(1, getWidth() - trai - phai);

            for (int chiSo = 0; chiSo < cacHang.size(); chiSo++) {
                DongBieuDo hang = cacHang.get(chiSo);
                int toaDoY = 8 + chiSo * chieuCaoHang;

                gia.setColor(MAU_VANBAN);
                gia.drawString(hang.tenPhong, trai, toaDoY + 13);

                String soTien = String.format("%,.0f đ", hang.doanhThu);
                Font phongChuSoTien = gia.getFont();
                int doRongSoTien = gia.getFontMetrics(phongChuSoTien).stringWidth(soTien);
                gia.setColor(Color.BLUE);
                gia.drawString(soTien, Math.max(trai, getWidth() - phai - doRongSoTien), toaDoY + 13);

                gia.setColor(MAU_MUTED);
                gia.drawString(hang.chiTiet, trai, toaDoY + 27);

                int toaDoYThanh = toaDoY + leTrenThanh;
                gia.setColor(new Color(241, 245, 249));
                gia.fillRoundRect(trai, toaDoYThanh, doRongThanh, 7, 7, 7);
                int doRongNoiDung = (int) Math.round(doRongThanh * hang.doanhThu / doanhThuToiDa);
                if (doRongNoiDung > 0) {
                    gia.setColor(chiSo == 0 ? Color.BLUE : new Color(96, 165, 250));
                    gia.fillRoundRect(trai, toaDoYThanh, Math.max(7, doRongNoiDung), 7, 7, 7);
                }

                gia.setStroke(new BasicStroke(1f));
                gia.setColor(new Color(241, 245, 249));
                if (chiSo < cacHang.size() - 1) {
                    gia.drawLine(trai, toaDoY + chieuCaoHang - 4, getWidth() - phai, toaDoY + chieuCaoHang - 4);
                }
            }
            gia.dispose();
        }
    }

    private static class DongBieuDo {
        private final String tenPhong;
        private final String chiTiet;
        private final double doanhThu;

        private DongBieuDo(String tenPhong, String chiTiet, double doanhThu) {
            this.tenPhong = tenPhong;
            this.chiTiet = chiTiet;
            this.doanhThu = doanhThu;
        }
    }
}
