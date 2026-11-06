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

public class ViewDatPhong extends JFrame {

    // Bảng dữ liệu
    public JTable bang;
    public DefaultTableModel moHinh;
    public TableRowSorter<DefaultTableModel> boSapXep;

    // Tìm kiếm & Lọc
    public JTextField oNhapTimKiem;
    public JButton nutTimKiem;
    public JComboBox<String> hopChonLocTrangThai;
    public JComboBox<String> hopChonLocThoiGian;
    public JLabel nhanThongKe;

    // Các nút chức năng ở bottom
    public JButton nutThem, nutKiemTraIn, nutKiemTraOut, nutSua, nutHuyDat, nutLamMoi;
    public JButton nutQuayLai = new JButton("Quay lại");

    // Khai báo field cũ để đảm bảo tương thích
    public JTextField oNhapCCCD = new JTextField();
    public JTextField oNhapSoNguoi = new JTextField();
    public JTextField oNhapGhiChu = new JTextField();
    public JComboBox<String> hopChonNgayNhan = new JComboBox<>();
    public JComboBox<String> hopChonNgayTra = new JComboBox<>();
    public JComboBox<String> hopChonTang = new JComboBox<>();
    public JComboBox<String> hopChonTrangThai = new JComboBox<>();
    public JButton nutKiemTraCCCD = new JButton();
    public JButton nutTimPhong = new JButton();
    public JButton nutLayGia = new JButton();
    public JButton nutDatLai = new JButton();

    public ViewDatPhong() {
        setTitle("Quản lý khách sạn - Quản lý đặt phòng");
        AppWindowIcon.apply(this);
        setSize(1180, 700);
        setMinimumSize(new Dimension(1000, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel khungChinh = new JPanel(new BorderLayout(0, 10));
        khungChinh.setBackground(new Color(245, 247, 250));
        khungChinh.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ================= TOP: HEADER & THANH TÌM KIẾM =================
        JPanel khungTren = new JPanel();
        khungTren.setLayout(new BoxLayout(khungTren, BoxLayout.Y_AXIS));
        khungTren.setBackground(new Color(245, 247, 250));

        // 1. Tiêu đề & Thống kê
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("QUẢN LÝ ĐẶT PHÒNG & LƯU TRÚ");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanTieuDe.setForeground(new Color(30, 41, 59));

        nhanThongKe = new JLabel("Tổng số: 0 đơn | Đang ở: 0 | Chờ nhận phòng: 0");
        nhanThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nhanThongKe.setForeground(new Color(100, 116, 139));

        khungTieuDe.add(nhanTieuDe, BorderLayout.WEST);
        khungTieuDe.add(nhanThongKe, BorderLayout.EAST);
        khungTren.add(khungTieuDe);
        khungTren.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & bộ lọc nghiệp vụ
        JPanel khungTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungTimKiem.setOpaque(false);

        JLabel nhanTim = new JLabel("Tìm kiếm:");
        nhanTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanTim.setForeground(new Color(51, 65, 85));

        oNhapTimKiem = new JTextField(16);
        oNhapTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTimKiem.setPreferredSize(new Dimension(200, 34));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText", "Tên khách, SĐT, số phòng, CCCD...");

        nutTimKiem = new JButton("Tìm kiếm");
        UIStyle.setButtonIcon(nutTimKiem, "timkiem.png");
        nutTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutTimKiem.setPreferredSize(new Dimension(100, 34));
        nutTimKiem.setFocusPainted(false);

        JLabel nhanLocTrangThai = new JLabel("Trạng thái:");
        nhanLocTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanLocTrangThai.setForeground(new Color(51, 65, 85));

        hopChonLocTrangThai = new JComboBox<>(new String[] {
            "Tất cả trạng thái",
            "Đang ở (Đã nhận phòng)",
            "Chờ nhận phòng (Đã đặt)",
            "Đã trả phòng",
            "Đã hủy"
        });
        hopChonLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hopChonLocTrangThai.setPreferredSize(new Dimension(190, 34));

        JLabel nhanLocTG = new JLabel("Thời gian:");
        nhanLocTG.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanLocTG.setForeground(new Color(51, 65, 85));

        hopChonLocThoiGian = new JComboBox<>(new String[] {
            "Tất cả thời gian",
            "Hôm nay",
            "Tháng này"
        });
        hopChonLocThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hopChonLocThoiGian.setPreferredSize(new Dimension(140, 34));

        khungTimKiem.add(nhanTim);
        khungTimKiem.add(oNhapTimKiem);
        khungTimKiem.add(nutTimKiem);
        khungTimKiem.add(Box.createHorizontalStrut(10));
        khungTimKiem.add(nhanLocTrangThai);
        khungTimKiem.add(hopChonLocTrangThai);
        khungTimKiem.add(Box.createHorizontalStrut(10));
        khungTimKiem.add(nhanLocTG);
        khungTimKiem.add(hopChonLocThoiGian);

        khungTren.add(khungTimKiem);
        khungChinh.add(khungTren, BorderLayout.NORTH);

        // ================= CENTER: BẢNG ĐẶT PHÒNG =================
        moHinh = new DefaultTableModel(
                new String[] {
                    "Mã ĐP", "Phòng", "Khách hàng", "Số điện thoại", "Số CCCD",
                    "Ngày nhận", "Ngày trả", "Số người", "Trạng thái", "Ghi chú"
                }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int chiSoCot) {
                if (chiSoCot == 0 || chiSoCot == 7) return Integer.class;
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

        // Cột 0: Mã ĐP
        bang.getColumnModel().getColumn(0).setPreferredWidth(60);
        bang.getColumnModel().getColumn(0).setMaxWidth(80);
        bang.getColumnModel().getColumn(0).setCellRenderer(boKetXuatGiua);

        // Cột 1: Phòng
        bang.getColumnModel().getColumn(1).setPreferredWidth(80);
        DefaultTableCellRenderer boKetXuatPhong = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(dangChon ? bang.getSelectionForeground() : new Color(31, 78, 121));
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(1).setCellRenderer(boKetXuatPhong);

        // Cột 2: Khách hàng
        bang.getColumnModel().getColumn(2).setPreferredWidth(160);
        DefaultTableCellRenderer boKetXuatKhach = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(2).setCellRenderer(boKetXuatKhach);

        // Cột 3: SĐT
        bang.getColumnModel().getColumn(3).setPreferredWidth(110);
        bang.getColumnModel().getColumn(3).setCellRenderer(boKetXuatGiua);

        // Cột 4: CCCD
        bang.getColumnModel().getColumn(4).setPreferredWidth(120);
        bang.getColumnModel().getColumn(4).setCellRenderer(boKetXuatGiua);

        // Cột 5: Ngày nhận
        bang.getColumnModel().getColumn(5).setPreferredWidth(100);
        bang.getColumnModel().getColumn(5).setCellRenderer(boKetXuatGiua);

        // Cột 6: Ngày trả
        bang.getColumnModel().getColumn(6).setPreferredWidth(100);
        bang.getColumnModel().getColumn(6).setCellRenderer(boKetXuatGiua);

        // Cột 7: Số người
        bang.getColumnModel().getColumn(7).setPreferredWidth(70);
        bang.getColumnModel().getColumn(7).setCellRenderer(boKetXuatGiua);

        // Cột 8: Badge trạng thái
        bang.getColumnModel().getColumn(8).setPreferredWidth(140);
        bang.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean duocChon, boolean tieuDiem,
                                                           int hang, int chiSoCot) {
                super.getTableCellRendererComponent(bang, giaTri, duocChon, tieuDiem, hang, chiSoCot);
                String trangThai = giaTri == null ? "" : giaTri.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (trangThai.contains("Đang ở") || trangThai.contains("Đã nhận") || trangThai.contains("CHECKED_IN")) {
                    setText("ĐANG Ở");
                    setBackground(new Color(254, 237, 237));
                    setForeground(new Color(192, 57, 43));
                } else if (trangThai.contains("Chờ nhận") || trangThai.contains("Đã đặt") || trangThai.contains("BOOKED")) {
                    setText("CHỜ NHẬN PHÒNG");
                    setBackground(new Color(225, 238, 255));
                    setForeground(new Color(13, 110, 253));
                } else if (trangThai.contains("Đã trả") || trangThai.contains("CHECKED_OUT")) {
                    setText("ĐÃ TRẢ PHÒNG");
                    setBackground(new Color(230, 235, 240));
                    setForeground(new Color(100, 110, 120));
                } else if (trangThai.contains("Đã hủy") || trangThai.contains("CANCELLED")) {
                    setText("ĐÃ HỦY");
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(132, 32, 41));
                } else {
                    setText(trangThai);
                    setBackground(bang.getBackground());
                    setForeground(bang.getForeground());
                }

                if (duocChon) {
                    setBackground(bang.getSelectionBackground());
                    setForeground(bang.getSelectionForeground());
                }
                return this;
            }
        });

        // Cột 9: Ghi chú
        bang.getColumnModel().getColumn(9).setPreferredWidth(150);

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

        nutThem = new JButton("Đặt phòng mới");
        nutThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutThem.setBackground(new Color(13, 110, 253));
        nutThem.setForeground(Color.WHITE);
        nutThem.setPreferredSize(new Dimension(150, 36));
        nutThem.setFocusPainted(false);

        nutKiemTraIn = new JButton("Nhận phòng");
        nutKiemTraIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutKiemTraIn.setBackground(new Color(25, 135, 84));
        nutKiemTraIn.setForeground(Color.WHITE);
        nutKiemTraIn.setPreferredSize(new Dimension(170, 36));
        nutKiemTraIn.setFocusPainted(false);

        nutKiemTraOut = new JButton("Trả phòng & Thanh toán");
        nutKiemTraOut.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutKiemTraOut.setBackground(new Color(192, 57, 43));
        nutKiemTraOut.setForeground(Color.WHITE);
        nutKiemTraOut.setPreferredSize(new Dimension(185, 36));
        nutKiemTraOut.setFocusPainted(false);

        nutSua = new JButton("Sửa đơn");
        UIStyle.setButtonIcon(nutSua, "sua.png");
        nutSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutSua.setBackground(new Color(255, 193, 7));
        nutSua.setForeground(Color.BLACK);
        nutSua.setPreferredSize(new Dimension(100, 36));
        nutSua.setFocusPainted(false);

        nutHuyDat = new JButton("Hủy đặt");
        UIStyle.setButtonIcon(nutHuyDat, "xoa.png");
        nutHuyDat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutHuyDat.setBackground(new Color(108, 117, 125));
        nutHuyDat.setForeground(Color.BLACK);
        nutHuyDat.setPreferredSize(new Dimension(100, 36));
        nutHuyDat.setFocusPainted(false);

        nutLamMoi = new JButton("Làm mới");
        UIStyle.setButtonIcon(nutLamMoi, "load.png");
        nutLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutLamMoi.setPreferredSize(new Dimension(120, 36));
        nutLamMoi.setFocusPainted(false);

        khungDuoi.add(nutThem);
        khungDuoi.add(nutKiemTraIn);
        khungDuoi.add(nutKiemTraOut);
        khungDuoi.add(nutSua);
        khungDuoi.add(nutHuyDat);
        khungDuoi.add(nutLamMoi);

        khungChinh.add(khungDuoi, BorderLayout.SOUTH);
        setContentPane(khungChinh);
    }

    public void lamMoi() {
        oNhapTimKiem.setText("");
        hopChonLocTrangThai.setSelectedIndex(0);
        hopChonLocThoiGian.setSelectedIndex(0);
        bang.clearSelection();
    }
}
