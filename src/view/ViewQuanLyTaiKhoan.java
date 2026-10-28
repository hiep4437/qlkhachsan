package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.users;

public class ViewQuanLyTaiKhoan extends JFrame {

    public JTextField txtMaNguoiDung;
    public JTextField txtTenDangNhap;
    public JPasswordField txtMatKhau;
    public JTextField txtHoTen;
    public JComboBox<String> cboVaiTro;
    public JComboBox<String> cboTrangThai;

    public JTextField txtTimKiem;
    public JComboBox<String> cboLocVaiTro;
    public JComboBox<String> cboLocTrangThai;
    public JButton btnTimKiem;
    public JButton btnLamMoiTim;

    public JTable table;
    public DefaultTableModel model;

    public JButton btnThem;
    public JButton btnSua;
    public JButton btnXoa;
    public JButton btnKhoaMoKhoa;
    public JButton btnDatLaiMatKhau;
    public JButton btnLamMoi;
    public JButton btnQuayLai;

    private users currentUser;

    public ViewQuanLyTaiKhoan() {
        this(new users(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewQuanLyTaiKhoan(users currentUser) {
        this.currentUser = currentUser;

        setTitle("Quản lý khách sạn - Quản lý tài khoản");
        setSize(1050, 680);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // Tieu de
        JLabel lblTieuDe = new JLabel("QUẢN LÝ TÀI KHOẢN NGƯỜI DÙNG");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(24, 44, 97));
        lblTieuDe.setHorizontalAlignment(JLabel.CENTER);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // 1. Panel nhap thong tin
        txtMaNguoiDung = new JTextField();
        txtMaNguoiDung.setEditable(false);
        txtMaNguoiDung.setBackground(new Color(245, 245, 245));

        txtTenDangNhap = new JTextField();
        txtMatKhau = new JPasswordField();
        txtHoTen = new JTextField();

        cboVaiTro = new JComboBox<>(new String[] { "STAFF", "ADMIN" });
        cboTrangThai = new JComboBox<>(new String[] { "1 - Hoạt động", "0 - Đã khóa" });

        JPanel pNhap = new JPanel(new GridLayout(3, 4, 12, 10));
        pNhap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 205, 215)),
                        "Thông tin tài khoản",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 13),
                        new Color(24, 44, 97)
                ),
                BorderFactory.createEmptyBorder(8, 12, 10, 12)
        ));

        pNhap.add(new JLabel("Mã người dùng:"));
        pNhap.add(txtMaNguoiDung);

        pNhap.add(new JLabel("Tên đăng nhập (*):"));
        pNhap.add(txtTenDangNhap);

        pNhap.add(new JLabel("Mật khẩu (*):"));
        JPanel pPass = new JPanel(new BorderLayout());
        pPass.add(txtMatKhau, BorderLayout.CENTER);
        JLabel lblGhiChuPass = new JLabel(" (Để trống nếu giữ nguyên)");
        lblGhiChuPass.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblGhiChuPass.setForeground(Color.GRAY);
        pPass.add(lblGhiChuPass, BorderLayout.SOUTH);
        pNhap.add(pPass);

        pNhap.add(new JLabel("Họ và tên (*):"));
        pNhap.add(txtHoTen);

        pNhap.add(new JLabel("Vai trò:"));
        pNhap.add(cboVaiTro);

        pNhap.add(new JLabel("Trạng thái:"));
        pNhap.add(cboTrangThai);

        // 2. Panel tim kiem & loc
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        pTim.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        txtTimKiem = new JTextField(15);
        cboLocVaiTro = new JComboBox<>(new String[] { "Tất cả vai trò", "ADMIN", "STAFF" });
        cboLocTrangThai = new JComboBox<>(new String[] { "Tất cả trạng thái", "Hoạt động", "Khóa" });

        btnTimKiem = new JButton("🔍 Tìm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLamMoiTim = new JButton("🔄 Làm mới");
        btnLamMoiTim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLamMoiTim.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pTim.add(new JLabel("Từ khóa:"));
        pTim.add(txtTimKiem);
        pTim.add(new JLabel("Vai trò:"));
        pTim.add(cboLocVaiTro);
        pTim.add(new JLabel("Trạng thái:"));
        pTim.add(cboLocTrangThai);
        pTim.add(btnTimKiem);
        pTim.add(btnLamMoiTim);

        // 3. Bang nguoi dung
        model = new DefaultTableModel(
                new String[] { "Mã ID", "Tên đăng nhập", "Họ và tên", "Vai trò", "Trạng thái" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Can giua cac cot ID, Vai tro, Trang thai
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);

        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Custom renderer cho cot trang thai
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!isSelected && value != null) {
                    String valStr = value.toString();
                    if (valStr.contains("Hoạt động")) {
                        setForeground(new Color(16, 185, 129));
                    } else {
                        setForeground(new Color(220, 38, 38));
                    }
                }
                return c;
            }
        });

        // 4. Panel nut chuc nang
        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnThem = new JButton("➕ Thêm mới");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThem.setBackground(new Color(37, 99, 235));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSua = new JButton("✏️ Cập nhật");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXoa = new JButton("🗑️ Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setBackground(new Color(239, 68, 68));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnKhoaMoKhoa = new JButton("🔒 Khóa / Mở khóa");
        btnKhoaMoKhoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnKhoaMoKhoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnDatLaiMatKhau = new JButton("🔑 Đặt lại mật khẩu");
        btnDatLaiMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDatLaiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLamMoi = new JButton("🔄 Đặt lại form");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnQuayLai = new JButton("Quay lại");
        btnQuayLai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pNut.add(btnThem);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnKhoaMoKhoa);
        pNut.add(btnDatLaiMatKhau);
        pNut.add(btnLamMoi);
        pNut.add(btnQuayLai);

        // Bo cuc tong the
        JPanel pTren = new JPanel(new BorderLayout(5, 5));
        pTren.add(lblTieuDe, BorderLayout.NORTH);
        pTren.add(pNhap, BorderLayout.CENTER);
        pTren.add(pTim, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        add(pTren, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pNut, BorderLayout.SOUTH);

        table.setAutoCreateRowSorter(true);

        // Su kien chon dong tren bang
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (!e.getValueIsAdjusting() && selectedRow != -1) {
                int row = table.convertRowIndexToModel(selectedRow);
                txtMaNguoiDung.setText(model.getValueAt(row, 0).toString());
                txtTenDangNhap.setText(model.getValueAt(row, 1).toString());
                txtHoTen.setText(model.getValueAt(row, 2).toString());

                String role = model.getValueAt(row, 3).toString();
                cboVaiTro.setSelectedItem(role);

                String status = model.getValueAt(row, 4).toString();
                if (status.contains("Hoạt động")) {
                    cboTrangThai.setSelectedIndex(0);
                } else {
                    cboTrangThai.setSelectedIndex(1);
                }

                txtMatKhau.setText("");
            }
        });

        // Dat lai form
        btnLamMoi.addActionListener(e -> lamMoiForm());

        // Quay lai
        btnQuayLai.addActionListener(e -> {
            new ViewTrangChu(currentUser).setVisible(true);
            dispose();
        });
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
        SwingUtilities.invokeLater(() -> new ViewQuanLyTaiKhoan().setVisible(true));
    }
}
