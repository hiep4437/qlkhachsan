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

public class ViewQuanLyKhachHang extends JFrame {

    // Bảng dữ liệu
    public JTable table;
    public DefaultTableModel model;
    public TableRowSorter<DefaultTableModel> sorter;

    // Tìm kiếm & Thống kê
    public JTextField txtTimKiem;
    public JButton btnTimKiem;
    public JLabel lblThongKe;

    // Các nút chức năng ở bottom
    public JButton btnThem, btnSua, btnLichSu, btnXoa, btnLamMoi;
    public JButton btnQuayLai = new JButton("Quay lại");

    // Khai báo field cũ để đảm bảo tương thích
    public JTextField txtMa = new JTextField();
    public JTextField txtHoTen = new JTextField();
    public JTextField txtCCCD = new JTextField();
    public JTextField txtSDT = new JTextField();
    public JTextField txtEmail = new JTextField();
    public JTextField txtDiaChi = new JTextField();

    public ViewQuanLyKhachHang() {
        setTitle("Quản lý khách sạn - Quản lý khách hàng");
        AppWindowIcon.apply(this);
        setSize(1100, 680);
        setMinimumSize(new Dimension(950, 550));
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

        JLabel lblTieuDe = new JLabel("DANH SÁCH QUẢN LÝ KHÁCH HÀNG");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(30, 41, 59));

        lblThongKe = new JLabel("Tổng số: 0 khách hàng");
        lblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongKe.setForeground(new Color(100, 116, 139));

        pTieuDe.add(lblTieuDe, BorderLayout.WEST);
        pTieuDe.add(lblThongKe, BorderLayout.EAST);
        pTop.add(pTieuDe);
        pTop.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm
        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pTimKiem.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTim.setForeground(new Color(51, 65, 85));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(280, 34));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Họ tên, CCCD, số điện thoại, địa chỉ...");

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTimKiem.setPreferredSize(new Dimension(110, 34));
        btnTimKiem.setFocusPainted(false);

        pTimKiem.add(lblTim);
        pTimKiem.add(txtTimKiem);
        pTimKiem.add(btnTimKiem);

        pTop.add(pTimKiem);
        pMain.add(pTop, BorderLayout.NORTH);

        // ================= CENTER: BẢNG KHÁCH HÀNG =================
        model = new DefaultTableModel(
                new String[] { "Mã KH", "Họ và tên", "Số CCCD", "Số điện thoại", "Email", "Địa chỉ", "Số lần thuê phòng" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 6) return Integer.class;
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

        // Header bảng
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

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        DefaultTableCellRenderer boldRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return c;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(boldRenderer);

        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(160);

        // Cột 6: Số lần thuê phòng (Badge)
        table.getColumnModel().getColumn(6).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (selected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                    return this;
                }

                int times = 0;
                try {
                    times = Integer.parseInt(value.toString());
                } catch (Exception ignored) {}

                if (times >= 3) {
                    setText(times + " lần (Khách quen)");
                    setBackground(new Color(209, 231, 221));
                    setForeground(new Color(15, 81, 50));
                } else if (times > 0) {
                    setText(times + " lần");
                    setBackground(new Color(225, 238, 255));
                    setForeground(new Color(13, 110, 253));
                } else {
                    setText("Chưa lưu trú");
                    setBackground(new Color(245, 245, 245));
                    setForeground(new Color(120, 120, 120));
                }
                return this;
            }
        });

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

        btnThem = new JButton("Thêm khách hàng");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThem.setBackground(new Color(13, 110, 253));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(160, 36));
        btnThem.setFocusPainted(false);

        btnSua = new JButton("Sửa thông tin");
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(new Color(33, 37, 41));
        btnSua.setPreferredSize(new Dimension(130, 36));
        btnSua.setFocusPainted(false);

        btnLichSu = new JButton("Lịch sử thuê phòng");
        btnLichSu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLichSu.setBackground(new Color(25, 135, 84));
        btnLichSu.setForeground(Color.WHITE);
        btnLichSu.setPreferredSize(new Dimension(170, 36));
        btnLichSu.setFocusPainted(false);

        btnXoa = new JButton("Xóa khách hàng");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(140, 36));
        btnXoa.setFocusPainted(false);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLamMoi.setPreferredSize(new Dimension(120, 36));
        btnLamMoi.setFocusPainted(false);

        pBottom.add(btnThem);
        pBottom.add(btnSua);
        pBottom.add(btnLichSu);
        pBottom.add(btnXoa);
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
        table.clearSelection();
    }
}
