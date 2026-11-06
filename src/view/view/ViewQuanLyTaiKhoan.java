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
import model.NguoiDung;

public class ViewQuanLyTaiKhoan extends JFrame {

    // ── Bảng dữ liệu ──────────────────────────────────────────────────────────
    public JTable bang;
    public DefaultTableModel moHinh;
    public TableRowSorter<DefaultTableModel> boSapXep;

    // ── Thanh tìm kiếm & Lọc ──────────────────────────────────────────────────
    public JTextField         oNhapTimKiem;
    public JComboBox<String>  hopChonLocVaiTro;
    public JComboBox<String>  hopChonLocTrangThai;
    public JButton            nutTimKiem;
    public JButton            nutLamMoiTim;
    public JLabel             nhanThongKe;

    // ── Nút chức năng ─────────────────────────────────────────────────────────
    public JButton nutThem;
    public JButton nutSua;
    public JButton nutXoa;
    public JButton nutKhoaMoKhoa;
    public JButton nutDatLaiMatKhau;
    public JButton nutLamMoi;
    public JButton nutQuayLai;

    // ── Field compat cũ (vẫn cần cho controller cũ) ───────────────────────────
    public JTextField      oNhapMaNguoiDung  = new JTextField();
    public JTextField      oNhapTenDangNhap  = new JTextField();
    public JPasswordField  oNhapMatKhau      = new JPasswordField();
    public JTextField      oNhapHoTen        = new JTextField();
    public JComboBox<String> hopChonVaiTro     = new JComboBox<>(new String[]{"STAFF","ADMIN"});
    public JComboBox<String> hopChonTrangThai  = new JComboBox<>(new String[]{"1 - Hoạt động","0 - Đã khóa"});

    private NguoiDung nguoiDungHienTai;

    public ViewQuanLyTaiKhoan() {
        this(new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewQuanLyTaiKhoan(NguoiDung nguoiDungHienTai) {
        this.nguoiDungHienTai = nguoiDungHienTai;
        setTitle("Quản lý khách sạn - Quản lý tài khoản");
        AppWindowIcon.apply(this);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel khungChinh = new JPanel(new BorderLayout(0, 10));
        khungChinh.setBackground(new Color(245, 247, 250));
        khungChinh.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── TOP: Tiêu đề + Thanh tìm kiếm ────────────────────────────────────
        JPanel khungTren = new JPanel();
        khungTren.setLayout(new BoxLayout(khungTren, BoxLayout.Y_AXIS));
        khungTren.setBackground(new Color(245, 247, 250));

        // 1. Tiêu đề + Thống kê
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("DANH SÁCH QUẢN LÝ TÀI KHOẢN");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanTieuDe.setForeground(new Color(30, 41, 59));

        nhanThongKe = new JLabel("Tổng số: 0 tài khoản | Admin: 0 | Nhân viên: 0");
        nhanThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nhanThongKe.setForeground(new Color(100, 116, 139));

        khungTieuDe.add(nhanTieuDe, BorderLayout.WEST);
        khungTieuDe.add(nhanThongKe, BorderLayout.EAST);
        khungTren.add(khungTieuDe);
        khungTren.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & lọc
        JPanel khungTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungTimKiem.setOpaque(false);

        Font truongNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font truongNhap = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel nhanTim = new JLabel("Tìm kiếm:");
        nhanTim.setFont(truongNhan);
        nhanTim.setForeground(new Color(51, 65, 85));

        oNhapTimKiem = new JTextField(16);
        oNhapTimKiem.setFont(truongNhap);
        oNhapTimKiem.setPreferredSize(new Dimension(210, 34));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText", "Tên đăng nhập, họ tên...");

        nutTimKiem = new JButton("Tìm kiếm");
        UIStyle.setButtonIcon(nutTimKiem, "timkiem.png");
        nutTimKiem.setFont(truongNhap);
        nutTimKiem.setPreferredSize(new Dimension(110, 34));
        nutTimKiem.setFocusPainted(false);

        JLabel nhanVaiTro = new JLabel("Vai trò:");
        nhanVaiTro.setFont(truongNhan);
        nhanVaiTro.setForeground(new Color(51, 65, 85));

        hopChonLocVaiTro = new JComboBox<>(new String[]{"Tất cả vai trò", "ADMIN", "STAFF"});
        hopChonLocVaiTro.setFont(truongNhap);
        hopChonLocVaiTro.setPreferredSize(new Dimension(155, 34));

        JLabel nhanTrangThai = new JLabel("Trạng thái:");
        nhanTrangThai.setFont(truongNhan);
        nhanTrangThai.setForeground(new Color(51, 65, 85));

        hopChonLocTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Hoạt động", "Khóa"});
        hopChonLocTrangThai.setFont(truongNhap);
        hopChonLocTrangThai.setPreferredSize(new Dimension(165, 34));

        nutLamMoiTim = new JButton("Xóa lọc");
        UIStyle.setButtonIcon(nutLamMoiTim, "load.png");
        nutLamMoiTim.setFont(truongNhap);
        nutLamMoiTim.setPreferredSize(new Dimension(90, 34));
        nutLamMoiTim.setFocusPainted(false);

        khungTimKiem.add(nhanTim);
        khungTimKiem.add(oNhapTimKiem);
        khungTimKiem.add(nutTimKiem);
        khungTimKiem.add(Box.createHorizontalStrut(12));
        khungTimKiem.add(nhanVaiTro);
        khungTimKiem.add(hopChonLocVaiTro);
        khungTimKiem.add(nhanTrangThai);
        khungTimKiem.add(hopChonLocTrangThai);
        khungTimKiem.add(nutLamMoiTim);

        khungTren.add(khungTimKiem);
        khungChinh.add(khungTren, BorderLayout.NORTH);

        // ── CENTER: Bảng danh sách tài khoản ─────────────────────────────────
        moHinh = new DefaultTableModel(
                new String[]{"Mã ID", "Tên đăng nhập", "Họ và tên", "Vai trò", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int hang, int chiSoCot) { return false; }
            @Override public Class<?> getColumnClass(int chiSoCot) {
                return chiSoCot == 0 ? Integer.class : String.class;
            }
        };

        bang = new JTable(moHinh);
        bang.setRowHeight(36);
        bang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.setGridColor(new Color(230, 235, 240));
        bang.setShowVerticalLines(false);
        bang.setShowHorizontalLines(true);
        bang.setSelectionBackground(new Color(225, 238, 255));
        bang.setSelectionForeground(new Color(15, 23, 42));

        JTableHeader tieuDe = bang.getTableHeader();
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tieuDe.setBackground(new Color(238, 242, 246));
        tieuDe.setForeground(new Color(30, 41, 59));
        tieuDe.setPreferredSize(new Dimension(tieuDe.getWidth(), 38));

        boSapXep = new TableRowSorter<>(moHinh);
        bang.setRowSorter(boSapXep);
        List<RowSorter.SortKey> cacKhoaSapXep = new ArrayList<>();
        cacKhoaSapXep.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        boSapXep.setSortKeys(cacKhoaSapXep);

        // Căn chỉnh cột
        DefaultTableCellRenderer boKetXuatGiua = new DefaultTableCellRenderer();
        boKetXuatGiua.setHorizontalAlignment(SwingConstants.CENTER);

        bang.getColumnModel().getColumn(0).setPreferredWidth(60);
        bang.getColumnModel().getColumn(0).setMaxWidth(80);
        bang.getColumnModel().getColumn(0).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(1).setPreferredWidth(160);
        DefaultTableCellRenderer boKetXuatDam = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable bangHienTai, Object giaTriHienTai, boolean chuoi, boolean tieuDiem, int hangHienTai, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bangHienTai, giaTriHienTai, chuoi, tieuDiem, hangHienTai, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(1).setCellRenderer(boKetXuatDam);

        bang.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Cột Vai trò – badge
        bang.getColumnModel().getColumn(3).setPreferredWidth(130);
        bang.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable bangHienTai, Object giaTriHienTai, boolean duocChon, boolean tieuDiem, int hangHienTai, int chiSoCot) {
                super.getTableCellRendererComponent(bangHienTai, giaTriHienTai, duocChon, tieuDiem, hangHienTai, chiSoCot);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!duocChon && giaTriHienTai != null) {
                    if ("ADMIN".equalsIgnoreCase(giaTriHienTai.toString())) {
                        setBackground(new Color(224, 231, 255));
                        setForeground(new Color(79, 70, 229));
                    } else {
                        setBackground(new Color(241, 245, 249));
                        setForeground(new Color(51, 65, 85));
                    }
                } else {
                    setBackground(bangHienTai.getSelectionBackground());
                    setForeground(bangHienTai.getSelectionForeground());
                }
                return this;
            }
        });

        // Cột Trạng thái – badge
        bang.getColumnModel().getColumn(4).setPreferredWidth(140);
        bang.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable bangHienTai, Object giaTriHienTai, boolean duocChon, boolean tieuDiem, int hangHienTai, int chiSoCot) {
                super.getTableCellRendererComponent(bangHienTai, giaTriHienTai, duocChon, tieuDiem, hangHienTai, chiSoCot);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!duocChon && giaTriHienTai != null) {
                    String chuoi = giaTriHienTai.toString();
                    if (chuoi.contains("Hoạt động") || chuoi.contains("Đang")) {
                        setText("Hoạt động");
                        setBackground(new Color(209, 231, 221));
                        setForeground(new Color(15, 81, 50));
                    } else {
                        setText("Đã khóa");
                        setBackground(new Color(248, 215, 218));
                        setForeground(new Color(132, 32, 41));
                    }
                } else {
                    setBackground(bangHienTai.getSelectionBackground());
                    setForeground(bangHienTai.getSelectionForeground());
                }
                return this;
            }
        });

        // Double-click mở modal sửa
        bang.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent suKien) {
                if (suKien.getClickCount() == 2 && bang.getSelectedRow() != -1) {
                    nutSua.doClick();
                }
            }
        });

        // Khi chọn dòng, ghi vào các field compat cũ (vẫn dùng trong NguoiDungController)
        bang.getSelectionModel().addListSelectionListener(suKien -> {
            if (!suKien.getValueIsAdjusting() && bang.getSelectedRow() != -1) {
                int hangMoHinh = bang.convertRowIndexToModel(bang.getSelectedRow());
                oNhapMaNguoiDung.setText(moHinh.getValueAt(hangMoHinh, 0).toString());
                oNhapTenDangNhap.setText(moHinh.getValueAt(hangMoHinh, 1).toString());
                oNhapHoTen.setText(moHinh.getValueAt(hangMoHinh, 2).toString());
                hopChonVaiTro.setSelectedItem(moHinh.getValueAt(hangMoHinh, 3).toString());
                String trangThai = moHinh.getValueAt(hangMoHinh, 4).toString();
                hopChonTrangThai.setSelectedIndex(trangThai.contains("Hoạt động") ? 0 : 1);
                oNhapMatKhau.setText("");
            }
        });

        JScrollPane cuonPane = new JScrollPane(bang);
        cuonPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235), 1));
        cuonPane.getViewport().setBackground(Color.WHITE);
        khungChinh.add(cuonPane, BorderLayout.CENTER);

        // ── BOTTOM: Nút thao tác ──────────────────────────────────────────────
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        khungDuoi.setOpaque(false);

        nutThem = makeBtn("Thêm tài khoản", new Color(13, 110, 253), Color.BLACK, 165);
        UIStyle.setButtonIcon(nutThem, "them.png");
        nutSua  = makeBtn("Sửa tài khoản",  new Color(255, 193, 7), Color.BLACK, 150);
        UIStyle.setButtonIcon(nutSua, "sua.png");
        nutXoa  = makeBtn("Xóa tài khoản",  new Color(220, 53, 69), Color.BLACK, 150);
        UIStyle.setButtonIcon(nutXoa, "xoa.png");
        nutKhoaMoKhoa    = makeBtn("Khóa / Mở khóa", new Color(108, 117, 125), Color.WHITE, 155);
        nutDatLaiMatKhau = makeBtn("Đặt lại mật khẩu", new Color(52, 58, 64),  Color.WHITE, 165);
        nutLamMoi        = makeBtn("Làm mới", null, null, 130);
        UIStyle.setButtonIcon(nutLamMoi, "load.png");
        nutLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        khungDuoi.add(nutThem);
        khungDuoi.add(nutSua);
        khungDuoi.add(nutXoa);
        khungDuoi.add(nutKhoaMoKhoa);
        khungDuoi.add(nutDatLaiMatKhau);
        khungDuoi.add(nutLamMoi);

        khungChinh.add(khungDuoi, BorderLayout.SOUTH);
        setContentPane(khungChinh);

        // btnQuayLai chỉ tạo để tránh NPE khi ViewTrangChu set action listener
        nutQuayLai = new JButton("Quay lại");
    }

    private JButton makeBtn(String vanBan, Color mauNen, Color mauChu, int chieuRong) {
        JButton nut = new JButton(vanBan);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 13));
        if (mauNen != null) { nut.setBackground(mauNen); nut.setForeground(mauChu); }
        nut.setPreferredSize(new Dimension(chieuRong, 36));
        nut.setFocusPainted(false);
        return nut;
    }

    public void lamMoiForm() {
        oNhapMaNguoiDung.setText("");
        oNhapTenDangNhap.setText("");
        oNhapMatKhau.setText("");
        oNhapHoTen.setText("");
        hopChonVaiTro.setSelectedIndex(0);
        hopChonTrangThai.setSelectedIndex(0);
        bang.clearSelection();
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception biBoQua) {}
            new ViewQuanLyTaiKhoan().setVisible(true);
        });
    }
}
