package view.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class ViewBaoCaoThongKe extends JFrame {

    private static final Color COLOR_TEXT = new Color(30, 41, 59);
    private static final Color COLOR_MUTED = new Color(100, 116, 139);
    private static final Color COLOR_GREEN = new Color(16, 185, 129);
    private static final Color COLOR_AMBER = new Color(245, 158, 11);

    public JLabel lblValTongDoanhThu;
    public JLabel lblValDoanhThuThang;
    public JLabel lblValDoanhThuNgay;
    public JComboBox<String> cboKyBaoCao;
    public JTextField txtTuNgay;
    public JTextField txtDenNgay;
    public JButton btnChonTuNgay;
    public JButton btnChonDenNgay;
    public JButton btnLoc;
    public JLabel lblThongKeKy;
    public JTable tblPhong;
    public DefaultTableModel modelPhong;
    public JTable tblHoaDon;
    public DefaultTableModel modelHoaDon;

    private final BieuDoDoanhThuPhong bieuDoDoanhThuPhong = new BieuDoDoanhThuPhong();
    private LocalDate tuNgay = LocalDate.now().withDayOfMonth(1);
    private LocalDate denNgay = LocalDate.now();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ViewBaoCaoThongKe() {
        setTitle("Báo cáo doanh thu");
        AppWindowIcon.apply(this);
        setSize(1240, 760);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(UIStyle.COLOR_APP_BG);
        main.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(taoTieuDe());
        top.add(Box.createVerticalStrut(18));
        top.add(taoCardsDoanhThu());
        top.add(Box.createVerticalStrut(16));
        top.add(taoBoLoc());

        main.add(top, BorderLayout.NORTH);
        main.add(taoNoiDungBaoCao(), BorderLayout.CENTER);
        setContentPane(main);
    }

    private JPanel taoTieuDe() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Báo cáo doanh thu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(COLOR_TEXT);

        JLabel subtitle = new JLabel("Tổng quan doanh thu và hiệu quả kinh doanh của khách sạn");
        subtitle.setFont(UIStyle.FONT_REGULAR);
        subtitle.setForeground(COLOR_MUTED);

        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);

        JLabel badge = new JLabel("THỐNG KÊ TÀI CHÍNH");
        badge.setFont(UIStyle.FONT_SMALL_BOLD);
        badge.setForeground(UIStyle.COLOR_PRIMARY);
        badge.setBackground(new Color(239, 246, 255));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(8, 12, 8, 12));

        panel.add(titles, BorderLayout.WEST);
        panel.add(badge, BorderLayout.EAST);
        return panel;
    }

    private JPanel taoCardsDoanhThu() {
        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);

        lblValTongDoanhThu = new JLabel("0 đ");
        lblValDoanhThuThang = new JLabel("0 đ");
        lblValDoanhThuNgay = new JLabel("0 đ");

        cards.add(taoCardDoanhThu(
                "DOANH THU KỲ", "Tổng tiền đã thu trong khoảng chọn",
                lblValTongDoanhThu, UIStyle.COLOR_PRIMARY));
        cards.add(taoCardDoanhThu(
                "HÓA ĐƠN ĐÃ THU", "Số hóa đơn thanh toán trong kỳ",
                lblValDoanhThuThang, COLOR_GREEN));
        cards.add(taoCardDoanhThu(
                "LƯỢT THUÊ PHÒNG", "Số lượt thuê có phát sinh doanh thu",
                lblValDoanhThuNgay, COLOR_AMBER));
        return cards;
    }

    private JPanel taoCardDoanhThu(String title, String subtitle, JLabel value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(4, 56));
        card.add(accentBar, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(title);
        heading.setFont(UIStyle.FONT_SMALL_BOLD);
        heading.setForeground(COLOR_MUTED);

        value.setFont(new Font("Segoe UI", Font.BOLD, 24));
        value.setForeground(COLOR_TEXT);

        JLabel description = new JLabel(subtitle);
        description.setFont(UIStyle.FONT_SMALL);
        description.setForeground(COLOR_MUTED);

        details.add(heading);
        details.add(Box.createVerticalStrut(7));
        details.add(value);
        details.add(Box.createVerticalStrut(5));
        details.add(description);
        card.add(details, BorderLayout.CENTER);
        return card;
    }

    private JPanel taoBoLoc() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER),
                new EmptyBorder(12, 16, 12, 16)));

        JPanel controls = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);

        JLabel presetLabel = new JLabel("Kỳ nhanh");
        presetLabel.setFont(UIStyle.FONT_BOLD);
        presetLabel.setForeground(COLOR_TEXT);

        cboKyBaoCao = new JComboBox<>(new String[] {
            "Tháng này",
            "Hôm nay",
            "7 ngày gần đây",
            "30 ngày gần đây",
            "Tháng trước",
            "Năm nay",
            "Toàn bộ thời gian",
            "Tùy chỉnh"
        });
        cboKyBaoCao.setFont(UIStyle.FONT_REGULAR);
        cboKyBaoCao.setPreferredSize(new Dimension(150, 36));
        cboKyBaoCao.addActionListener(event -> capNhatTheoKyNhanh());

        JLabel tuNgayLabel = new JLabel("Từ ngày");
        tuNgayLabel.setFont(UIStyle.FONT_BOLD);
        tuNgayLabel.setForeground(COLOR_TEXT);
        txtTuNgay = taoONgay(tuNgay);
        btnChonTuNgay = taoNutLich();
        btnChonTuNgay.addActionListener(event -> moLichChonNgay(tuNgay, date -> {
            tuNgay = date;
            capNhatONgay();
            datKyTuyChinh();
        }));

        JLabel denNgayLabel = new JLabel("Đến ngày");
        denNgayLabel.setFont(UIStyle.FONT_BOLD);
        denNgayLabel.setForeground(COLOR_TEXT);
        txtDenNgay = taoONgay(denNgay);
        btnChonDenNgay = taoNutLich();
        btnChonDenNgay.addActionListener(event -> moLichChonNgay(denNgay, date -> {
            denNgay = date;
            capNhatONgay();
            datKyTuyChinh();
        }));

        btnLoc = UIStyle.createButton("Xem thống kê", UIStyle.COLOR_PRIMARY, Color.WHITE);
        btnLoc.setPreferredSize(new Dimension(136, 38));
        btnLoc.setMaximumSize(new Dimension(136, 38));

        controls.add(presetLabel);
        controls.add(cboKyBaoCao);
        controls.add(tuNgayLabel);
        controls.add(txtTuNgay);
        controls.add(btnChonTuNgay);
        controls.add(denNgayLabel);
        controls.add(txtDenNgay);
        controls.add(btnChonDenNgay);
        controls.add(btnLoc);

        lblThongKeKy = new JLabel("Thực thu trong kỳ: 0 đ");
        lblThongKeKy.setFont(UIStyle.FONT_BOLD);
        lblThongKeKy.setForeground(UIStyle.COLOR_PRIMARY);
        lblThongKeKy.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(controls);
        content.add(Box.createVerticalStrut(8));
        content.add(lblThongKeKy);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JTextField taoONgay(LocalDate date) {
        JTextField field = new JTextField(date.format(DATE_FORMATTER));
        field.setFont(UIStyle.FONT_REGULAR);
        field.setEditable(false);
        field.setFocusable(false);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setPreferredSize(new Dimension(120, 36));
        field.setMinimumSize(new Dimension(120, 36));
        field.setMaximumSize(new Dimension(120, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER),
                new EmptyBorder(6, 8, 6, 8)));
        return field;
    }

    private JButton taoNutLich() {
        JButton button = new JButton("Lịch");
        button.setFont(UIStyle.FONT_SMALL_BOLD);
        button.setForeground(COLOR_TEXT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(64, 36));
        button.setMaximumSize(new Dimension(64, 36));
        return button;
    }

    private void capNhatTheoKyNhanh() {
        String preset = (String) cboKyBaoCao.getSelectedItem();
        if (preset == null || preset.equals("Tùy chỉnh")) {
            return;
        }

        LocalDate today = LocalDate.now();
        switch (preset) {
            case "Hôm nay":
                tuNgay = today;
                denNgay = today;
                break;
            case "7 ngày gần đây":
                tuNgay = today.minusDays(6);
                denNgay = today;
                break;
            case "30 ngày gần đây":
                tuNgay = today.minusDays(29);
                denNgay = today;
                break;
            case "Tháng trước":
                YearMonth previousMonth = YearMonth.from(today).minusMonths(1);
                tuNgay = previousMonth.atDay(1);
                denNgay = previousMonth.atEndOfMonth();
                break;
            case "Năm nay":
                tuNgay = today.withDayOfYear(1);
                denNgay = today;
                break;
            case "Toàn bộ thời gian":
                tuNgay = LocalDate.of(1000, 1, 1);
                denNgay = today;
                break;
            case "Tháng này":
            default:
                tuNgay = today.withDayOfMonth(1);
                denNgay = today;
                break;
        }
        capNhatONgay();
    }

    private void datKyTuyChinh() {
        if (cboKyBaoCao.getSelectedItem() != null
                && !cboKyBaoCao.getSelectedItem().equals("Tùy chỉnh")) {
            cboKyBaoCao.setSelectedItem("Tùy chỉnh");
        }
    }

    private void capNhatONgay() {
        txtTuNgay.setText(tuNgay.format(DATE_FORMATTER));
        txtDenNgay.setText(denNgay.format(DATE_FORMATTER));
    }

    private JTabbedPane taoNoiDungBaoCao() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIStyle.FONT_BOLD);
        tabs.setBackground(UIStyle.COLOR_APP_BG);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        modelPhong = new DefaultTableModel(new String[] {
            "Phòng", "Loại phòng", "Tầng", "Lượt thuê", "Doanh thu", "Tỷ trọng"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPhong = taoBang(modelPhong);
        tblPhong.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dinhChieuCotPhong();

        JPanel tabPhong = new JPanel(new java.awt.GridLayout(1, 2, 14, 0));
        tabPhong.setOpaque(false);
        tabPhong.setBorder(new EmptyBorder(14, 2, 2, 2));
        tabPhong.add(taoTheNoiDung(
                "Doanh thu theo phòng",
                "Top phòng đóng góp doanh thu trong kỳ đã chọn",
                bieuDoDoanhThuPhong));
        tabPhong.add(taoTheNoiDung(
                "Hiệu quả từng phòng",
                "Doanh thu và lượt thuê được sắp xếp theo doanh thu",
                new JScrollPane(tblPhong)));
        tabs.addTab("Tổng quan theo phòng", tabPhong);

        modelHoaDon = new DefaultTableModel(new String[] {
            "Mã HĐ", "Thời gian", "Phòng", "Khách hàng", "Số điện thoại",
            "Tiền phòng", "Phụ thu", "Giảm giá", "Thực thu", "Thanh toán"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHoaDon = taoBang(modelHoaDon);
        tblHoaDon.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dinhChieuCotHoaDon();

        tabs.addTab("Chi tiết hóa đơn", taoTheNoiDung(
                "Hóa đơn đã thanh toán",
                "Danh sách giao dịch trong kỳ báo cáo",
                new JScrollPane(tblHoaDon)));
        return tabs;
    }

    private JPanel taoTheNoiDung(String title, String subtitle, java.awt.Component content) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyle.FONT_HEADER);
        titleLabel.setForeground(COLOR_TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UIStyle.FONT_SMALL);
        subtitleLabel.setForeground(COLOR_MUTED);

        heading.add(titleLabel);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitleLabel);

        if (content instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) content;
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        }

        card.add(heading, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JTable taoBang(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(UIStyle.FONT_REGULAR);
        table.setRowHeight(36);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(COLOR_TEXT);
        table.setIntercellSpacing(new Dimension(0, 1));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable source, Object value, boolean selected, boolean focused, int row, int column) {
                java.awt.Component component = super.getTableCellRendererComponent(
                        source, value, selected, focused, row, column);
                if (!selected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    component.setForeground(COLOR_TEXT);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return component;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(UIStyle.FONT_BOLD);
        header.setForeground(COLOR_MUTED);
        header.setBackground(new Color(248, 250, 252));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        return table;
    }

    private void dinhChieuCotPhong() {
        int[] widths = { 66, 104, 48, 78, 118, 72 };
        for (int i = 0; i < widths.length; i++) {
            tblPhong.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void dinhChieuCotHoaDon() {
        int[] widths = { 62, 126, 70, 140, 112, 98, 84, 84, 104, 110 };
        for (int i = 0; i < widths.length; i++) {
            tblHoaDon.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public void capNhatBieuDoPhong(List<String> tenPhong, List<String> chiTiet, List<Double> doanhThu) {
        bieuDoDoanhThuPhong.capNhat(tenPhong, chiTiet, doanhThu);
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    private void moLichChonNgay(LocalDate selectedDate, Consumer<LocalDate> onDateSelected) {
        JDialog calendar = new JDialog(this, "Chọn ngày", true);
        calendar.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(8, 10));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel monthHeader = new JPanel(new BorderLayout(8, 0));
        monthHeader.setOpaque(false);
        YearMonth[] monthHienThi = { YearMonth.from(selectedDate) };
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(UIStyle.FONT_BOLD);
        monthLabel.setForeground(COLOR_TEXT);

        JButton previousMonth = new JButton("<");
        JButton nextMonth = new JButton(">");
        previousMonth.setFocusable(false);
        nextMonth.setFocusable(false);
        monthHeader.add(previousMonth, BorderLayout.WEST);
        monthHeader.add(monthLabel, BorderLayout.CENTER);
        monthHeader.add(nextMonth, BorderLayout.EAST);

        JPanel daysGrid = new JPanel(new GridLayout(0, 7, 4, 4));
        daysGrid.setOpaque(false);

        Runnable[] updateCalendar = new Runnable[1];
        updateCalendar[0] = () -> {
            monthLabel.setText(monthHienThi[0].getMonth().getDisplayName(
                    java.time.format.TextStyle.FULL, java.util.Locale.forLanguageTag("vi"))
                    + " " + monthHienThi[0].getYear());
            daysGrid.removeAll();

            String[] weekDays = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
            for (String weekDay : weekDays) {
                JLabel dayHeading = new JLabel(weekDay, SwingConstants.CENTER);
                dayHeading.setFont(UIStyle.FONT_SMALL_BOLD);
                dayHeading.setForeground(COLOR_MUTED);
                daysGrid.add(dayHeading);
            }

            LocalDate firstDay = monthHienThi[0].atDay(1);
            int leadingDays = firstDay.getDayOfWeek().getValue() - 1;
            for (int i = 0; i < leadingDays; i++) {
                daysGrid.add(new JLabel());
            }

            for (int day = 1; day <= monthHienThi[0].lengthOfMonth(); day++) {
                LocalDate date = monthHienThi[0].atDay(day);
                JButton dayButton = new JButton(Integer.toString(day));
                dayButton.setFocusable(false);
                dayButton.setFont(UIStyle.FONT_REGULAR);
                dayButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
                if (date.equals(selectedDate)) {
                    dayButton.setBackground(UIStyle.COLOR_PRIMARY);
                    dayButton.setForeground(Color.WHITE);
                    dayButton.setOpaque(true);
                } else {
                    dayButton.setBackground(Color.WHITE);
                    dayButton.setForeground(COLOR_TEXT);
                }
                dayButton.addActionListener(event -> {
                    onDateSelected.accept(date);
                    calendar.dispose();
                });
                daysGrid.add(dayButton);
            }
            daysGrid.revalidate();
            daysGrid.repaint();
            calendar.pack();
        };

        previousMonth.addActionListener(event -> {
            monthHienThi[0] = monthHienThi[0].minusMonths(1);
            updateCalendar[0].run();
        });
        nextMonth.addActionListener(event -> {
            monthHienThi[0] = monthHienThi[0].plusMonths(1);
            updateCalendar[0].run();
        });

        content.add(monthHeader, BorderLayout.NORTH);
        content.add(daysGrid, BorderLayout.CENTER);
        calendar.setContentPane(content);
        updateCalendar[0].run();
        calendar.setResizable(false);
        calendar.setLocationRelativeTo(btnChonTuNgay);
        calendar.setVisible(true);
    }

    private static class BieuDoDoanhThuPhong extends JPanel {
        private static final int MAX_ROWS = 6;
        private final List<DongBieuDo> rows = new ArrayList<>();

        private BieuDoDoanhThuPhong() {
            setOpaque(false);
            setPreferredSize(new Dimension(300, 330));
        }

        private void capNhat(List<String> tenPhong, List<String> chiTiet, List<Double> doanhThu) {
            rows.clear();
            int count = Math.min(MAX_ROWS, Math.min(tenPhong.size(), Math.min(chiTiet.size(), doanhThu.size())));
            for (int i = 0; i < count; i++) {
                rows.add(new DongBieuDo(tenPhong.get(i), chiTiet.get(i), doanhThu.get(i)));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (rows.isEmpty()) {
                g.setFont(UIStyle.FONT_REGULAR);
                g.setColor(COLOR_MUTED);
                String message = "Chưa có dữ liệu doanh thu theo phòng";
                int textX = Math.max(12, (getWidth() - g.getFontMetrics().stringWidth(message)) / 2);
                g.drawString(message, textX, Math.max(36, getHeight() / 2));
                g.dispose();
                return;
            }

            double maxRevenue = 0;
            for (DongBieuDo row : rows) {
                maxRevenue = Math.max(maxRevenue, row.doanhThu);
            }
            maxRevenue = Math.max(1, maxRevenue);

            int left = 2;
            int right = 2;
            int barTopOffset = 31;
            int rowHeight = Math.max(47, (getHeight() - 8) / rows.size());
            int barWidth = Math.max(1, getWidth() - left - right);

            for (int i = 0; i < rows.size(); i++) {
                DongBieuDo row = rows.get(i);
                int y = 8 + i * rowHeight;

                g.setFont(UIStyle.FONT_BOLD);
                g.setColor(COLOR_TEXT);
                g.drawString(row.tenPhong, left, y + 13);

                String amount = String.format("%,.0f đ", row.doanhThu);
                g.setFont(UIStyle.FONT_SMALL_BOLD);
                Font amountFont = g.getFont();
                int amountWidth = g.getFontMetrics(amountFont).stringWidth(amount);
                g.setColor(UIStyle.COLOR_PRIMARY);
                g.drawString(amount, Math.max(left, getWidth() - right - amountWidth), y + 13);

                g.setFont(UIStyle.FONT_SMALL);
                g.setColor(COLOR_MUTED);
                g.drawString(row.chiTiet, left, y + 27);

                int barY = y + barTopOffset;
                g.setColor(new Color(241, 245, 249));
                g.fillRoundRect(left, barY, barWidth, 7, 7, 7);
                int filledWidth = (int) Math.round(barWidth * row.doanhThu / maxRevenue);
                if (filledWidth > 0) {
                    g.setColor(i == 0 ? UIStyle.COLOR_PRIMARY : new Color(96, 165, 250));
                    g.fillRoundRect(left, barY, Math.max(7, filledWidth), 7, 7, 7);
                }

                g.setStroke(new BasicStroke(1f));
                g.setColor(new Color(241, 245, 249));
                if (i < rows.size() - 1) {
                    g.drawLine(left, y + rowHeight - 4, getWidth() - right, y + rowHeight - 4);
                }
            }
            g.dispose();
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
