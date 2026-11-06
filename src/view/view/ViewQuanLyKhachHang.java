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

public class ViewQuanLyKhachHang extends JFrame {

    // Bảng dữ liệu
    public JTable bang;
    public DefaultTableModel moHinh;
    public TableRowSorter<DefaultTableModel> boSapXep;

    // Tìm kiếm & Thống kê
    public JTextField oNhapTimKiem;
    public JButton nutTimKiem;
    public JLabel nhanThongKe;

    // Các nút chức năng ở bottom
    public JButton nutThem, nutSua, nutLichSu, nutXoa, nutLamMoi;
    public JButton nutQuayLai = new JButton("Quay lại");

    // Khai báo field cũ để đảm bảo tương thích
    public JTextField oNhapMa = new JTextField();
    public JTextField oNhapHoTen = new JTextField();
    public JTextField oNhapCCCD = new JTextField();
    public JTextField oNhapSDT = new JTextField();
    public JTextField oNhapThuDienTu = new JTextField();
    public JTextField oNhapDiaChi = new JTextField();

    public ViewQuanLyKhachHang() {
        setTitle("Quản lý khách sạn - Quản lý khách hàng");
        AppWindowIcon.apply(this);
        setSize(1100, 680);
        setMinimumSize(new Dimension(950, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel khungChinh = new JPanel(new BorderLayout(0, 10));
        khungChinh.setBackground(new Color(245, 247, 250));
        khungChinh.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ================= TOP: HEADER & THANH TÌM KIẾM =================
        JPanel khungTren = new JPanel();
        khungTren.setLayout(new BoxLayout(khungTren, BoxLayout.Y_AXIS));
        khungTren.setBackground(new Color(245, 247, 250));

        // 1. Tiêu đề
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("DANH SÁCH QUẢN LÝ KHÁCH HÀNG");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanTieuDe.setForeground(new Color(30, 41, 59));

        nhanThongKe = new JLabel("Tổng số: 0 khách hàng");
        nhanThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nhanThongKe.setForeground(new Color(100, 116, 139));

        khungTieuDe.add(nhanTieuDe, BorderLayout.WEST);
        khungTieuDe.add(nhanThongKe, BorderLayout.EAST);
        khungTren.add(khungTieuDe);
        khungTren.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm
        JPanel khungTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungTimKiem.setOpaque(false);

        JLabel nhanTim = new JLabel("Tìm kiếm:");
        nhanTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanTim.setForeground(new Color(51, 65, 85));

        oNhapTimKiem = new JTextField(20);
        oNhapTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTimKiem.setPreferredSize(new Dimension(280, 34));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText", "Họ tên, CCCD, số điện thoại, địa chỉ...");

        nutTimKiem = new JButton("Tìm kiếm");
        UIStyle.setButtonIcon(nutTimKiem, "timkiem.png");
        nutTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutTimKiem.setPreferredSize(new Dimension(110, 34));
        nutTimKiem.setFocusPainted(false);

        khungTimKiem.add(nhanTim);
        khungTimKiem.add(oNhapTimKiem);
        khungTimKiem.add(nutTimKiem);

        khungTren.add(khungTimKiem);
        khungChinh.add(khungTren, BorderLayout.NORTH);

        // ================= CENTER: BẢNG KHÁCH HÀNG =================
        moHinh = new DefaultTableModel(
                new String[] { "Mã KH", "Họ và tên", "Số CCCD", "Số điện thoại", "Email", "Địa chỉ", "Số lần thuê phòng" }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int chiSoCot) {
                if (chiSoCot == 0 || chiSoCot == 6) return Integer.class;
                return String.class;
            }
        };

        bang = new JTable(moHinh);
        bang.setRowHeight(38);
        bang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.setGridColor(new Color(230, 235, 240));
        bang.setShowVerticalLines(false);
        bang.setShowHorizontalLines(true);
        bang.setSelectionBackground(new Color(225, 238, 255));
        bang.setSelectionForeground(new Color(15, 23, 42));

        // Header bảng
        JTableHeader tieuDe = bang.getTableHeader();
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tieuDe.setBackground(new Color(238, 242, 246));
        tieuDe.setForeground(new Color(30, 41, 59));
        tieuDe.setPreferredSize(new Dimension(tieuDe.getWidth(), 38));

        boSapXep = new TableRowSorter<>(moHinh);
        bang.setRowSorter(boSapXep);
        List<RowSorter.SortKey> cacKhoaSapXep = new ArrayList<>();
        cacKhoaSapXep.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        boSapXep.setSortKeys(cacKhoaSapXep);

        DefaultTableCellRenderer boKetXuatGiua = new DefaultTableCellRenderer();
        boKetXuatGiua.setHorizontalAlignment(SwingConstants.CENTER);

        bang.getColumnModel().getColumn(0).setPreferredWidth(60);
        bang.getColumnModel().getColumn(0).setMaxWidth(80);
        bang.getColumnModel().getColumn(0).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(1).setPreferredWidth(170);
        DefaultTableCellRenderer boKetXuatDam = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(1).setCellRenderer(boKetXuatDam);

        bang.getColumnModel().getColumn(2).setPreferredWidth(130);
        bang.getColumnModel().getColumn(2).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(3).setPreferredWidth(120);
        bang.getColumnModel().getColumn(3).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(4).setPreferredWidth(160);
        bang.getColumnModel().getColumn(5).setPreferredWidth(160);

        // Cột 6: Số lần thuê phòng (Badge)
        bang.getColumnModel().getColumn(6).setPreferredWidth(130);
        bang.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean duocChon, boolean tieuDiem,
                                                           int hang, int chiSoCot) {
                super.getTableCellRendererComponent(bang, giaTri, duocChon, tieuDiem, hang, chiSoCot);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (duocChon) {
                    setBackground(bang.getSelectionBackground());
                    setForeground(bang.getSelectionForeground());
                    return this;
                }

                int cacLan = 0;
                try {
                    cacLan = Integer.parseInt(giaTri.toString());
                } catch (Exception biBoQua) {}

                if (cacLan >= 3) {
                    setText(cacLan + " lần (Khách quen)");
                    setBackground(new Color(209, 231, 221));
                    setForeground(new Color(15, 81, 50));
                } else if (cacLan > 0) {
                    setText(cacLan + " lần");
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
        bang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                if (suKien.getClickCount() == 2 && bang.getSelectedRow() != -1) {
                    nutSua.doClick();
                }
            }
        });

        JScrollPane cuonPane = new JScrollPane(bang);
        cuonPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235), 1));
        cuonPane.getViewport().setBackground(Color.WHITE);
        khungChinh.add(cuonPane, BorderLayout.CENTER);

        // ================= BOTTOM: CÁC NÚT THAO TÁC =================
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        khungDuoi.setOpaque(false);

        nutThem = new JButton("Thêm khách hàng");
        UIStyle.setButtonIcon(nutThem, "them.png");
        nutThem.setFont(UIStyle.PHONGCHU_IN_DAM);
        nutThem.setBackground(new Color(13, 110, 253));
        nutThem.setForeground(Color.BLACK);
        nutThem.setPreferredSize(new Dimension(160, 36));
        nutThem.setFocusPainted(false);

        nutSua = new JButton("Sửa thông tin");
        UIStyle.setButtonIcon(nutSua, "sua.png");
        nutSua.setFont(UIStyle.PHONGCHU_IN_DAM);
        nutSua.setBackground(new Color(255, 193, 7));
        nutSua.setForeground(Color.BLACK);
        nutSua.setPreferredSize(new Dimension(130, 36));
        nutSua.setFocusPainted(false);

        nutLichSu = new JButton("Lịch sử thuê phòng");
        nutLichSu.setFont(UIStyle.PHONGCHU_IN_DAM);
        nutLichSu.setBackground(new Color(25, 135, 84));
        nutLichSu.setForeground(Color.BLACK);
        nutLichSu.setPreferredSize(new Dimension(170, 36));
        nutLichSu.setFocusPainted(false);

        nutXoa = new JButton("Xóa khách hàng");
        UIStyle.setButtonIcon(nutXoa, "xoa.png");
        nutXoa.setFont(UIStyle.PHONGCHU_IN_DAM);
        nutXoa.setBackground(new Color(220, 53, 69));
        nutXoa.setForeground(Color.BLACK);
        nutXoa.setPreferredSize(new Dimension(140, 36));
        nutXoa.setFocusPainted(false);

        nutLamMoi = new JButton("Làm mới");
        UIStyle.setButtonIcon(nutLamMoi, "load.png");
        nutLamMoi.setFont(UIStyle.PHONGCHU_IN_DAM);
        nutLamMoi.setForeground(Color.BLACK);
        nutLamMoi.setPreferredSize(new Dimension(120, 36));
        nutLamMoi.setFocusPainted(false);

        khungDuoi.add(nutThem);
        khungDuoi.add(nutSua);
        khungDuoi.add(nutLichSu);
        khungDuoi.add(nutXoa);
        khungDuoi.add(nutLamMoi);

        khungChinh.add(khungDuoi, BorderLayout.SOUTH);
        setContentPane(khungChinh);
    }

    public void lamMoi() {
        oNhapTimKiem.setText("");
        bang.clearSelection();
    }
}
