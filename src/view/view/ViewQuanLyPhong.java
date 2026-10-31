package view.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
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

public class ViewQuanLyPhong extends JFrame {

    // Bảng dữ liệu
    public JTable table;
    public DefaultTableModel model;
    public TableRowSorter<DefaultTableModel> sorter;

    // Tìm kiếm & Lọc
    public JTextField txtTimKiem;
    public JButton btnTimKiem;
    public JComboBox<String> cboLocTrangThai;
    public JLabel lblThongKe;

    // Các nút chức năng ở bottom
    public JButton btnThem, btnSua, btnXoa, btnLamMoi;
    public JButton btnQuayLai = new JButton("Quay lại");

    // Khai báo các field cũ để đảm bảo tương thích mã nguồn
    public JTextField txtMaPhong = new JTextField();
    public JTextField txtSoPhong = new JTextField();
    public JTextField txtMaLoai = new JTextField();
    public JTextField txtGia = new JTextField();
    public JTextField txtMoTa = new JTextField();
    public JComboBox<String> cboTrangThai = new JComboBox<>(new String[] { "", "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ" });

    public ViewQuanLyPhong() {
        setTitle("Quản lý khách sạn - Quản lý phòng");
        AppWindowIcon.apply(this);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 550));
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

        JLabel lblTieuDe = new JLabel("DANH SÁCH QUẢN LÝ PHÒNG");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(30, 41, 59));

        lblThongKe = new JLabel("Tổng số: 0 phòng | Trống: 0 | Có khách: 0 | Đang dọn: 0 | Bảo trì: 0");
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

        txtTimKiem = new JTextField(18);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(220, 34));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Số phòng, loại phòng, tầng...");

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTimKiem.setPreferredSize(new Dimension(110, 34));
        btnTimKiem.setFocusPainted(false);

        JLabel lblLoc = new JLabel("Lọc trạng thái:");
        lblLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLoc.setForeground(new Color(51, 65, 85));

        cboLocTrangThai = new JComboBox<>(new String[] {
            "Tất cả trạng thái", "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ"
        });
        cboLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboLocTrangThai.setPreferredSize(new Dimension(160, 34));

        pTimKiem.add(lblTim);
        pTimKiem.add(txtTimKiem);
        pTimKiem.add(btnTimKiem);
        pTimKiem.add(Box.createHorizontalStrut(15));
        pTimKiem.add(lblLoc);
        pTimKiem.add(cboLocTrangThai);

        pTop.add(pTimKiem);
        pMain.add(pTop, BorderLayout.NORTH);

        // ================= CENTER: BẢNG DỮ LIỆU HIỆN ĐẠI =================
        // Cột: Mã phòng, Số phòng, Tầng, Loại phòng, Giá mỗi đêm, Trạng thái, Mô tả
        model = new DefaultTableModel(
                new String[] { "ID", "Số phòng", "Tầng", "Loại phòng", "Giá mỗi đêm", "Trạng thái", "Mô tả" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 2) return Integer.class;
                return String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
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

        // Sắp xếp
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Căn chỉnh các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        DefaultTableCellRenderer boldCenterRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                          boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(CENTER);
                return c;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(boldCenterRenderer);

        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3).setPreferredWidth(180);

        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        // Renderer trạng thái dạng badge đẹp
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
                                                          int row, int column) {
                super.getTableCellRendererComponent(table, value, selected, focus, row, column);

                String tt = value == null ? "" : value.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (selected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                    return this;
                }

                switch (tt) {
                    case "AVAILABLE":
                    case "TRỐNG":
                        setText("TRỐNG");
                        setBackground(new Color(209, 231, 221));
                        setForeground(new Color(15, 81, 50));
                        break;
                    case "OCCUPIED":
                    case "CÓ KHÁCH":
                        setText("CÓ KHÁCH");
                        setBackground(new Color(248, 215, 218));
                        setForeground(new Color(132, 32, 41));
                        break;
                    case "CLEANING":
                    case "ĐANG DỌN":
                        setText("ĐANG DỌN");
                        setBackground(new Color(255, 243, 205));
                        setForeground(new Color(102, 77, 3));
                        break;
                    case "MAINTENANCE":
                    case "BẢO TRÌ":
                        setText("BẢO TRÌ");
                        setBackground(new Color(226, 227, 229));
                        setForeground(new Color(65, 70, 75));
                        break;
                    default:
                        setText(tt);
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                        break;
                }
                return this;
            }
        });

        table.getColumnModel().getColumn(6).setPreferredWidth(250);

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

        // ================= BOTTOM: CÁC NÚT THÊM, SỬA, XÓA, LÀM MỚI =================
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);
        pBottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel pLeftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pLeftButtons.setOpaque(false);

        btnThem = new JButton("Thêm phòng");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThem.setBackground(new Color(13, 110, 253));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(140, 36));
        btnThem.setFocusPainted(false);

        btnSua = new JButton("Sửa phòng");
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(new Color(33, 37, 41));
        btnSua.setPreferredSize(new Dimension(130, 36));
        btnSua.setFocusPainted(false);

        btnXoa = new JButton("Xóa phòng");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(130, 36));
        btnXoa.setFocusPainted(false);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLamMoi.setPreferredSize(new Dimension(140, 36));
        btnLamMoi.setFocusPainted(false);

        pLeftButtons.add(btnThem);
        pLeftButtons.add(btnSua);
        pLeftButtons.add(btnXoa);
        pLeftButtons.add(btnLamMoi);

        pBottom.add(pLeftButtons, BorderLayout.CENTER);
        pMain.add(pBottom, BorderLayout.SOUTH);

        setContentPane(pMain);

        btnQuayLai.addActionListener(e -> {
            new ViewTrangChu().setVisible(true);
            dispose();
        });
    }

    public void lamMoi() {
        txtTimKiem.setText("");
        cboLocTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }

    public String chuyenTrangThai(String tt) {
        if (tt == null) return "";
        switch (tt) {
            case "AVAILABLE": return "TRỐNG";
            case "OCCUPIED": return "CÓ KHÁCH";
            case "CLEANING": return "ĐANG DỌN";
            case "MAINTENANCE": return "BẢO TRÌ";
            default: return tt;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ViewQuanLyPhong().setVisible(true);
        });
    }
}
