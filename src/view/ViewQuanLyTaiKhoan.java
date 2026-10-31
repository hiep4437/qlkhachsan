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
import model.users;

public class ViewQuanLyTaiKhoan extends JFrame {

    // ── Bảng dữ liệu ──────────────────────────────────────────────────────────
    public JTable table;
    public DefaultTableModel model;
    public TableRowSorter<DefaultTableModel> sorter;

    // ── Thanh tìm kiếm & Lọc ──────────────────────────────────────────────────
    public JTextField         txtTimKiem;
    public JComboBox<String>  cboLocVaiTro;
    public JComboBox<String>  cboLocTrangThai;
    public JButton            btnTimKiem;
    public JButton            btnLamMoiTim;
    public JLabel             lblThongKe;

    // ── Nút chức năng ─────────────────────────────────────────────────────────
    public JButton btnThem;
    public JButton btnSua;
    public JButton btnXoa;
    public JButton btnKhoaMoKhoa;
    public JButton btnDatLaiMatKhau;
    public JButton btnLamMoi;
    public JButton btnQuayLai;

    // ── Field compat cũ (vẫn cần cho controller cũ) ───────────────────────────
    public JTextField      txtMaNguoiDung  = new JTextField();
    public JTextField      txtTenDangNhap  = new JTextField();
    public JPasswordField  txtMatKhau      = new JPasswordField();
    public JTextField      txtHoTen        = new JTextField();
    public JComboBox<String> cboVaiTro     = new JComboBox<>(new String[]{"STAFF","ADMIN"});
    public JComboBox<String> cboTrangThai  = new JComboBox<>(new String[]{"1 - Hoạt động","0 - Đã khóa"});

    private users currentUser;

    public ViewQuanLyTaiKhoan() {
        this(new users(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewQuanLyTaiKhoan(users currentUser) {
        this.currentUser = currentUser;
        setTitle("Quản lý khách sạn - Quản lý tài khoản");
        AppWindowIcon.apply(this);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel pMain = new JPanel(new BorderLayout(0, 10));
        pMain.setBackground(new Color(245, 247, 250));
        pMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── TOP: Tiêu đề + Thanh tìm kiếm ────────────────────────────────────
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
        pTop.setBackground(new Color(245, 247, 250));

        // 1. Tiêu đề + Thống kê
        JPanel pTieuDe = new JPanel(new BorderLayout());
        pTieuDe.setOpaque(false);

        JLabel lblTieuDe = new JLabel("DANH SÁCH QUẢN LÝ TÀI KHOẢN");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(30, 41, 59));

        lblThongKe = new JLabel("Tổng số: 0 tài khoản | Admin: 0 | Nhân viên: 0");
        lblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblThongKe.setForeground(new Color(100, 116, 139));

        pTieuDe.add(lblTieuDe, BorderLayout.WEST);
        pTieuDe.add(lblThongKe, BorderLayout.EAST);
        pTop.add(pTieuDe);
        pTop.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & lọc
        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pTimKiem.setOpaque(false);

        Font fLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fInput = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lTim = new JLabel("Tìm kiếm:");
        lTim.setFont(fLabel);
        lTim.setForeground(new Color(51, 65, 85));

        txtTimKiem = new JTextField(16);
        txtTimKiem.setFont(fInput);
        txtTimKiem.setPreferredSize(new Dimension(210, 34));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên đăng nhập, họ tên...");

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(fInput);
        btnTimKiem.setPreferredSize(new Dimension(110, 34));
        btnTimKiem.setFocusPainted(false);

        JLabel lVaiTro = new JLabel("Vai trò:");
        lVaiTro.setFont(fLabel);
        lVaiTro.setForeground(new Color(51, 65, 85));

        cboLocVaiTro = new JComboBox<>(new String[]{"Tất cả vai trò", "ADMIN", "STAFF"});
        cboLocVaiTro.setFont(fInput);
        cboLocVaiTro.setPreferredSize(new Dimension(155, 34));

        JLabel lTrangThai = new JLabel("Trạng thái:");
        lTrangThai.setFont(fLabel);
        lTrangThai.setForeground(new Color(51, 65, 85));

        cboLocTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Hoạt động", "Khóa"});
        cboLocTrangThai.setFont(fInput);
        cboLocTrangThai.setPreferredSize(new Dimension(165, 34));

        btnLamMoiTim = new JButton("Xóa lọc");
        btnLamMoiTim.setFont(fInput);
        btnLamMoiTim.setPreferredSize(new Dimension(90, 34));
        btnLamMoiTim.setFocusPainted(false);

        pTimKiem.add(lTim);
        pTimKiem.add(txtTimKiem);
        pTimKiem.add(btnTimKiem);
        pTimKiem.add(Box.createHorizontalStrut(12));
        pTimKiem.add(lVaiTro);
        pTimKiem.add(cboLocVaiTro);
        pTimKiem.add(lTrangThai);
        pTimKiem.add(cboLocTrangThai);
        pTimKiem.add(btnLamMoiTim);

        pTop.add(pTimKiem);
        pMain.add(pTop, BorderLayout.NORTH);

        // ── CENTER: Bảng danh sách tài khoản ─────────────────────────────────
        model = new DefaultTableModel(
                new String[]{"Mã ID", "Tên đăng nhập", "Họ và tên", "Vai trò", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int column) {
                return column == 0 ? Integer.class : String.class;
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

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(238, 242, 246));
        header.setForeground(new Color(30, 41, 59));
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Căn chỉnh cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        DefaultTableCellRenderer boldRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return comp;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(boldRenderer);

        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Cột Vai trò – badge
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, f, r, c);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!sel && v != null) {
                    if ("ADMIN".equalsIgnoreCase(v.toString())) {
                        setBackground(new Color(224, 231, 255));
                        setForeground(new Color(79, 70, 229));
                    } else {
                        setBackground(new Color(241, 245, 249));
                        setForeground(new Color(51, 65, 85));
                    }
                } else {
                    setBackground(t.getSelectionBackground());
                    setForeground(t.getSelectionForeground());
                }
                return this;
            }
        });

        // Cột Trạng thái – badge
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, f, r, c);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!sel && v != null) {
                    String s = v.toString();
                    if (s.contains("Hoạt động") || s.contains("Đang")) {
                        setText("Hoạt động");
                        setBackground(new Color(209, 231, 221));
                        setForeground(new Color(15, 81, 50));
                    } else {
                        setText("Đã khóa");
                        setBackground(new Color(248, 215, 218));
                        setForeground(new Color(132, 32, 41));
                    }
                } else {
                    setBackground(t.getSelectionBackground());
                    setForeground(t.getSelectionForeground());
                }
                return this;
            }
        });

        // Double-click mở modal sửa
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    btnSua.doClick();
                }
            }
        });

        // Khi chọn dòng, ghi vào các field compat cũ (vẫn dùng trong UserController)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
                txtMaNguoiDung.setText(model.getValueAt(modelRow, 0).toString());
                txtTenDangNhap.setText(model.getValueAt(modelRow, 1).toString());
                txtHoTen.setText(model.getValueAt(modelRow, 2).toString());
                cboVaiTro.setSelectedItem(model.getValueAt(modelRow, 3).toString());
                String status = model.getValueAt(modelRow, 4).toString();
                cboTrangThai.setSelectedIndex(status.contains("Hoạt động") ? 0 : 1);
                txtMatKhau.setText("");
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pMain.add(scrollPane, BorderLayout.CENTER);

        // ── BOTTOM: Nút thao tác ──────────────────────────────────────────────
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pBottom.setOpaque(false);

        btnThem = makeBtn("Thêm tài khoản", new Color(13, 110, 253), Color.WHITE, 165);
        btnSua  = makeBtn("Sửa tài khoản",  new Color(255, 193, 7),  new Color(33, 37, 41), 150);
        btnXoa  = makeBtn("Xóa tài khoản",  new Color(220, 53, 69), Color.WHITE, 150);
        btnKhoaMoKhoa    = makeBtn("Khóa / Mở khóa", new Color(108, 117, 125), Color.WHITE, 155);
        btnDatLaiMatKhau = makeBtn("Đặt lại mật khẩu", new Color(52, 58, 64),  Color.WHITE, 165);
        btnLamMoi        = makeBtn("Làm mới", null, null, 130);
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pBottom.add(btnThem);
        pBottom.add(btnSua);
        pBottom.add(btnXoa);
        pBottom.add(btnKhoaMoKhoa);
        pBottom.add(btnDatLaiMatKhau);
        pBottom.add(btnLamMoi);

        pMain.add(pBottom, BorderLayout.SOUTH);
        setContentPane(pMain);

        // btnQuayLai chỉ tạo để tránh NPE khi ViewTrangChu set action listener
        btnQuayLai = new JButton("Quay lại");
    }

    private JButton makeBtn(String text, Color bg, Color fg, int width) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        if (bg != null) { b.setBackground(bg); b.setForeground(fg); }
        b.setPreferredSize(new Dimension(width, 36));
        b.setFocusPainted(false);
        return b;
    }

    /** Đếm ngược 5s và disable nút Làm mới */
    public void batDauDemNguocLamMoi(Runnable onRefresh) {
        btnLamMoi.setEnabled(false);
        final int[] giay = {5};
        btnLamMoi.setText("Đang tải (" + giay[0] + "s)");
        if (onRefresh != null) onRefresh.run();

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            giay[0]--;
            if (giay[0] > 0) {
                btnLamMoi.setText("Đang tải (" + giay[0] + "s)");
            } else {
                timer.stop();
                btnLamMoi.setText("Làm mới");
                btnLamMoi.setEnabled(true);
            }
        });
        timer.start();
    }

    public void lamMoiForm() {
        txtMaNguoiDung.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtHoTen.setText("");
        cboVaiTro.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new ViewQuanLyTaiKhoan().setVisible(true);
        });
    }
}
