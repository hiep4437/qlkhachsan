package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class ViewDatPhong extends JFrame {

    // Bảng dữ liệu
    public JTable table;
    public DefaultTableModel model;
    public TableRowSorter<DefaultTableModel> sorter;

    // Tìm kiếm & Lọc
    public JTextField txtTimKiem;
    public JButton btnTimKiem;
    public JComboBox<String> cboLocTrangThai;
    public JComboBox<String> cboLocThoiGian;
    public JLabel lblThongKe;

    // Các nút chức năng ở bottom
    public JButton btnThem, btnCheckIn, btnCheckOut, btnSua, btnHuyDat, btnLamMoi;
    public JButton btnQuayLai = new JButton("Quay lại");

    // Khai báo field cũ để đảm bảo tương thích
    public JTextField txtCCCD = new JTextField();
    public JTextField txtSoNguoi = new JTextField();
    public JTextField txtGhiChu = new JTextField();
    public JComboBox<String> cboNgayNhan = new JComboBox<>();
    public JComboBox<String> cboNgayTra = new JComboBox<>();
    public JComboBox<String> cboTang = new JComboBox<>();
    public JComboBox<String> cboTrangThai = new JComboBox<>();
    public JButton btnKiemTraCCCD = new JButton();
    public JButton btnTimPhong = new JButton();
    public JButton btnLayGia = new JButton();
    public JButton btnDatLai = new JButton();

    public ViewDatPhong() {
        setTitle("Quản lý khách sạn - Quản lý đặt phòng");
        AppWindowIcon.apply(this);
        setSize(1180, 700);
        setMinimumSize(new Dimension(1000, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pMain = new JPanel(new BorderLayout(0, 10));
        pMain.setBackground(new Color(245, 247, 250));
        pMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ================= TOP: HEADER & THANH TÌM KIẾM =================
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
        pTop.setBackground(new Color(245, 247, 250));

        // 1. Tiêu đề & Thống kê
        JPanel pTieuDe = new JPanel(new BorderLayout());
        pTieuDe.setOpaque(false);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ ĐẶT PHÒNG & LƯU TRÚ");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(30, 41, 59));

        lblThongKe = new JLabel("Tổng số: 0 đơn | Đang ở: 0 | Chờ nhận phòng: 0");
        lblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongKe.setForeground(new Color(100, 116, 139));

        pTieuDe.add(lblTieuDe, BorderLayout.WEST);
        pTieuDe.add(lblThongKe, BorderLayout.EAST);
        pTop.add(pTieuDe);
        pTop.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & bộ lọc nghiệp vụ
        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pTimKiem.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTim.setForeground(new Color(51, 65, 85));

        txtTimKiem = new JTextField(16);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(200, 34));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên khách, SĐT, số phòng, CCCD...");

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTimKiem.setPreferredSize(new Dimension(100, 34));
        btnTimKiem.setFocusPainted(false);

        JLabel lblLocTT = new JLabel("Trạng thái:");
        lblLocTT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLocTT.setForeground(new Color(51, 65, 85));

        cboLocTrangThai = new JComboBox<>(new String[] {
            "Tất cả trạng thái",
            "Đang ở (Đã nhận phòng)",
            "Chờ nhận phòng (Đã đặt)",
            "Đã trả phòng",
            "Đã hủy"
        });
        cboLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboLocTrangThai.setPreferredSize(new Dimension(190, 34));

        JLabel lblLocTG = new JLabel("Thời gian:");
        lblLocTG.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLocTG.setForeground(new Color(51, 65, 85));

        cboLocThoiGian = new JComboBox<>(new String[] {
            "Tất cả thời gian",
            "Hôm nay",
            "Tháng này"
        });
        cboLocThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboLocThoiGian.setPreferredSize(new Dimension(140, 34));

        pTimKiem.add(lblTim);
        pTimKiem.add(txtTimKiem);
        pTimKiem.add(btnTimKiem);
        pTimKiem.add(Box.createHorizontalStrut(10));
        pTimKiem.add(lblLocTT);
        pTimKiem.add(cboLocTrangThai);
        pTimKiem.add(Box.createHorizontalStrut(10));
        pTimKiem.add(lblLocTG);
        pTimKiem.add(cboLocThoiGian);

        pTop.add(pTimKiem);
        pMain.add(pTop, BorderLayout.NORTH);

        // ================= CENTER: BẢNG ĐẶT PHÒNG =================
        model = new DefaultTableModel(
                new String[] {
                    "Mã ĐP", "Phòng", "Khách hàng", "Số điện thoại", "Số CCCD",
                    "Ngày nhận", "Ngày trả", "Số người", "Trạng thái", "Ghi chú"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 7) return Integer.class;
                return String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(38);
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

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Cột 0: Mã ĐP
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Cột 1: Phòng
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        DefaultTableCellRenderer boldRoomRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(isSelected ? table.getSelectionForeground() : new Color(31, 78, 121));
                return c;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(boldRoomRenderer);

        // Cột 2: Khách hàng
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        DefaultTableCellRenderer boldGuestRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return c;
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(boldGuestRenderer);

        // Cột 3: SĐT
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Cột 4: CCCD
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Cột 5: Ngày nhận
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Cột 6: Ngày trả
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        // Cột 7: Số người
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        // Cột 8: Badge trạng thái
        table.getColumnModel().getColumn(8).setPreferredWidth(140);
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                String tt = value == null ? "" : value.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (tt.contains("Đang ở") || tt.contains("Đã nhận") || tt.contains("CHECKED_IN")) {
                    setText("ĐANG Ở");
                    setBackground(new Color(254, 237, 237));
                    setForeground(new Color(192, 57, 43));
                } else if (tt.contains("Chờ nhận") || tt.contains("Đã đặt") || tt.contains("BOOKED")) {
                    setText("CHỜ NHẬN PHÒNG");
                    setBackground(new Color(225, 238, 255));
                    setForeground(new Color(13, 110, 253));
                } else if (tt.contains("Đã trả") || tt.contains("CHECKED_OUT")) {
                    setText("ĐÃ TRẢ PHÒNG");
                    setBackground(new Color(230, 235, 240));
                    setForeground(new Color(100, 110, 120));
                } else if (tt.contains("Đã hủy") || tt.contains("CANCELLED")) {
                    setText("ĐÃ HỦY");
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(132, 32, 41));
                } else {
                    setText(tt);
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }

                if (selected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                }
                return this;
            }
        });

        // Cột 9: Ghi chú
        table.getColumnModel().getColumn(9).setPreferredWidth(150);

        // Click đúp vào dòng để mở modal sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    btnSua.doClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pMain.add(scrollPane, BorderLayout.CENTER);

        // ================= BOTTOM: CÁC NÚT THAO TÁC =================
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pBottom.setOpaque(false);

        btnThem = new JButton("Đặt phòng mới");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThem.setBackground(new Color(13, 110, 253));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(150, 36));
        btnThem.setFocusPainted(false);

        btnCheckIn = new JButton("Nhận phòng (Check-in)");
        btnCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCheckIn.setBackground(new Color(25, 135, 84));
        btnCheckIn.setForeground(Color.WHITE);
        btnCheckIn.setPreferredSize(new Dimension(170, 36));
        btnCheckIn.setFocusPainted(false);

        btnCheckOut = new JButton("Trả phòng & Thanh toán");
        btnCheckOut.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCheckOut.setBackground(new Color(192, 57, 43));
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.setPreferredSize(new Dimension(185, 36));
        btnCheckOut.setFocusPainted(false);

        btnSua = new JButton("Sửa đơn");
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(new Color(33, 37, 41));
        btnSua.setPreferredSize(new Dimension(100, 36));
        btnSua.setFocusPainted(false);

        btnHuyDat = new JButton("Hủy đặt");
        btnHuyDat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuyDat.setBackground(new Color(108, 117, 125));
        btnHuyDat.setForeground(Color.WHITE);
        btnHuyDat.setPreferredSize(new Dimension(100, 36));
        btnHuyDat.setFocusPainted(false);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLamMoi.setPreferredSize(new Dimension(120, 36));
        btnLamMoi.setFocusPainted(false);

        pBottom.add(btnThem);
        pBottom.add(btnCheckIn);
        pBottom.add(btnCheckOut);
        pBottom.add(btnSua);
        pBottom.add(btnHuyDat);
        pBottom.add(btnLamMoi);

        pMain.add(pBottom, BorderLayout.SOUTH);
        setContentPane(pMain);
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

    public void lamMoi() {
        txtTimKiem.setText("");
        cboLocTrangThai.setSelectedIndex(0);
        cboLocThoiGian.setSelectedIndex(0);
        table.clearSelection();
    }
}
