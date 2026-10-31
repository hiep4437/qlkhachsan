package view.view;

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

public class ViewHoaDon extends JFrame {

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
    public JButton btnXemChiTiet, btnLamMoi;
    public JButton btnQuayLai = new JButton("Quay lại");

    public ViewHoaDon() {
        setTitle("Quản lý khách sạn - Quản lý hóa đơn");
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

        // 1. Tiêu đề
        JPanel pTieuDe = new JPanel(new BorderLayout());
        pTieuDe.setOpaque(false);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN & DOANH THU");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(30, 41, 59));

        lblThongKe = new JLabel("Tổng số: 0 hóa đơn | Đã thu: 0 đ | Chưa thu: 0 đ");
        lblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongKe.setForeground(new Color(100, 116, 139));

        pTieuDe.add(lblTieuDe, BorderLayout.WEST);
        pTieuDe.add(lblThongKe, BorderLayout.EAST);
        pTop.add(pTieuDe);
        pTop.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & lọc
        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pTimKiem.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTim.setForeground(new Color(51, 65, 85));

        txtTimKiem = new JTextField(16);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(200, 34));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Số phòng, tên khách, SĐT, mã HĐ...");

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTimKiem.setPreferredSize(new Dimension(100, 34));
        btnTimKiem.setFocusPainted(false);

        JLabel lblLocTT = new JLabel("Trạng thái:");
        lblLocTT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLocTT.setForeground(new Color(51, 65, 85));

        cboLocTrangThai = new JComboBox<>(new String[] {
            "Tất cả trạng thái",
            "Đã thanh toán (PAID)",
            "Chưa thanh toán (UNPAID)",
            "Đã hoàn tiền (REFUNDED)"
        });
        cboLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboLocTrangThai.setPreferredSize(new Dimension(190, 34));

        JLabel lblLocTG = new JLabel("Thời gian:");
        lblLocTG.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLocTG.setForeground(new Color(51, 65, 85));

        cboLocThoiGian = new JComboBox<>(new String[] {
            "Tất cả thời gian",
            "Hôm nay",
            "Tháng này",
            "Năm nay"
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

        // ================= CENTER: BẢNG HÓA ĐƠN =================
        model = new DefaultTableModel(
                new String[] {
                    "Mã HĐ", "Phòng", "Khách hàng", "Số điện thoại", "Ngày lập",
                    "Tiền phòng", "Phụ thu", "Giảm giá", "Tổng tiền", "Phương thức TT", "Trạng thái"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Integer.class;
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

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        // Cột 0: Mã HĐ
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

        // Cột 3: Số ĐT
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Cột 4: Ngày lập
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Cột 5: Tiền phòng
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        // Cột 6: Phụ thu
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

        // Cột 7: Giảm giá
        table.getColumnModel().getColumn(7).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);

        // Cột 8: Tổng tiền
        table.getColumnModel().getColumn(8).setPreferredWidth(120);
        DefaultTableCellRenderer totalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(RIGHT);
                setForeground(isSelected ? table.getSelectionForeground() : new Color(192, 57, 43));
                return c;
            }
        };
        table.getColumnModel().getColumn(8).setCellRenderer(totalRenderer);

        // Cột 9: Phương thức TT
        table.getColumnModel().getColumn(9).setPreferredWidth(110);
        table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);

        // Cột 10: Trạng thái badge
        table.getColumnModel().getColumn(10).setPreferredWidth(140);
        table.getColumnModel().getColumn(10).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                String tt = value == null ? "" : value.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (tt.contains("PAID") || tt.contains("ĐÃ THANH TOÁN")) {
                    setText("ĐÃ THANH TOÁN");
                    setBackground(new Color(209, 231, 221));
                    setForeground(new Color(15, 81, 50));
                } else if (tt.contains("REFUNDED") || tt.contains("HOÀN TIỀN")) {
                    setText("ĐÃ HOÀN TIỀN");
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(132, 32, 41));
                } else if (tt.contains("UNPAID") || tt.contains("CHƯA THANH TOÁN")) {
                    setText("CHƯA THANH TOÁN");
                    setBackground(new Color(255, 243, 205));
                    setForeground(new Color(102, 77, 3));
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

        // Click đúp vào dòng để xem chi tiết
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    btnXemChiTiet.doClick();
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

        btnXemChiTiet = new JButton("Xem & In hóa đơn");
        btnXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXemChiTiet.setBackground(new Color(13, 110, 253));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setPreferredSize(new Dimension(160, 36));
        btnXemChiTiet.setFocusPainted(false);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLamMoi.setPreferredSize(new Dimension(120, 36));
        btnLamMoi.setFocusPainted(false);

        pBottom.add(btnXemChiTiet);
        pBottom.add(btnLamMoi);

        pMain.add(pBottom, BorderLayout.SOUTH);
        setContentPane(pMain);
    }

    public void lamMoi() {
        txtTimKiem.setText("");
        cboLocTrangThai.setSelectedIndex(0);
        cboLocThoiGian.setSelectedIndex(0);
        table.clearSelection();
    }
}
