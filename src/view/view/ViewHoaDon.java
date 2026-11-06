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
    public JButton nutXemChiTiet, nutLamMoi;
    public JButton nutQuayLai = new JButton("Quay lại");

    public ViewHoaDon() {
        setTitle("Quản lý khách sạn - Quản lý hóa đơn");
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

        // 1. Tiêu đề
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN & DOANH THU");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanTieuDe.setForeground(new Color(30, 41, 59));

        nhanThongKe = new JLabel("Tổng số: 0 hóa đơn | Đã thu: 0 đ | Chưa thu: 0 đ");
        nhanThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nhanThongKe.setForeground(new Color(100, 116, 139));

        khungTieuDe.add(nhanTieuDe, BorderLayout.WEST);
        khungTieuDe.add(nhanThongKe, BorderLayout.EAST);
        khungTren.add(khungTieuDe);
        khungTren.add(Box.createVerticalStrut(12));

        // 2. Thanh tìm kiếm & lọc
        JPanel khungTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungTimKiem.setOpaque(false);

        JLabel nhanTim = new JLabel("Tìm kiếm:");
        nhanTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanTim.setForeground(new Color(51, 65, 85));

        oNhapTimKiem = new JTextField(16);
        oNhapTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTimKiem.setPreferredSize(new Dimension(200, 34));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText", "Số phòng, tên khách, SĐT, mã HĐ...");

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
            "Đã thanh toán (PAID)",
            "Chưa thanh toán (UNPAID)",
            "Đã hoàn tiền (REFUNDED)"
        });
        hopChonLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hopChonLocTrangThai.setPreferredSize(new Dimension(190, 34));

        JLabel nhanLocTG = new JLabel("Thời gian:");
        nhanLocTG.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanLocTG.setForeground(new Color(51, 65, 85));

        hopChonLocThoiGian = new JComboBox<>(new String[] {
            "Tất cả thời gian",
            "Hôm nay",
            "Tháng này",
            "Năm nay"
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

        // ================= CENTER: BẢNG HÓA ĐƠN =================
        moHinh = new DefaultTableModel(
                new String[] {
                    "Mã HĐ", "Phòng", "Khách hàng", "Số điện thoại", "Ngày lập",
                    "Tiền phòng", "Phụ thu", "Giảm giá", "Tổng tiền", "Phương thức TT", "Trạng thái"
                }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int chiSoCot) {
                if (chiSoCot == 0) return Integer.class;
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

        DefaultTableCellRenderer boKetXuatPhai = new DefaultTableCellRenderer();
        boKetXuatPhai.setHorizontalAlignment(SwingConstants.RIGHT);

        // Cột 0: Mã HĐ
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

        // Cột 3: Số ĐT
        bang.getColumnModel().getColumn(3).setPreferredWidth(110);
        bang.getColumnModel().getColumn(3).setCellRenderer(boKetXuatGiua);

        // Cột 4: Ngày lập
        bang.getColumnModel().getColumn(4).setPreferredWidth(130);
        bang.getColumnModel().getColumn(4).setCellRenderer(boKetXuatGiua);

        // Cột 5: Tiền phòng
        bang.getColumnModel().getColumn(5).setPreferredWidth(100);
        bang.getColumnModel().getColumn(5).setCellRenderer(boKetXuatPhai);

        // Cột 6: Phụ thu
        bang.getColumnModel().getColumn(6).setPreferredWidth(90);
        bang.getColumnModel().getColumn(6).setCellRenderer(boKetXuatPhai);

        // Cột 7: Giảm giá
        bang.getColumnModel().getColumn(7).setPreferredWidth(90);
        bang.getColumnModel().getColumn(7).setCellRenderer(boKetXuatPhai);

        // Cột 8: Tổng tiền
        bang.getColumnModel().getColumn(8).setPreferredWidth(120);
        DefaultTableCellRenderer boKetXuatTong = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(RIGHT);
                setForeground(dangChon ? bang.getSelectionForeground() : new Color(192, 57, 43));
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(8).setCellRenderer(boKetXuatTong);

        // Cột 9: Phương thức TT
        bang.getColumnModel().getColumn(9).setPreferredWidth(110);
        bang.getColumnModel().getColumn(9).setCellRenderer(boKetXuatGiua);

        // Cột 10: Trạng thái badge
        bang.getColumnModel().getColumn(10).setPreferredWidth(140);
        bang.getColumnModel().getColumn(10).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean duocChon, boolean tieuDiem,
                                                           int hang, int chiSoCot) {
                super.getTableCellRendererComponent(bang, giaTri, duocChon, tieuDiem, hang, chiSoCot);
                String trangThai = giaTri == null ? "" : giaTri.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (trangThai.contains("PAID") || trangThai.contains("ĐÃ THANH TOÁN")) {
                    setText("ĐÃ THANH TOÁN");
                    setBackground(new Color(209, 231, 221));
                    setForeground(new Color(15, 81, 50));
                } else if (trangThai.contains("REFUNDED") || trangThai.contains("HOÀN TIỀN")) {
                    setText("ĐÃ HOÀN TIỀN");
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(132, 32, 41));
                } else if (trangThai.contains("UNPAID") || trangThai.contains("CHƯA THANH TOÁN")) {
                    setText("CHƯA THANH TOÁN");
                    setBackground(new Color(255, 243, 205));
                    setForeground(new Color(102, 77, 3));
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

        // Click đúp vào dòng để xem chi tiết
        bang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                if (suKien.getClickCount() == 2 && bang.getSelectedRow() != -1) {
                    nutXemChiTiet.doClick();
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

        nutXemChiTiet = new JButton("Xem & In hóa đơn");
        nutXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutXemChiTiet.setBackground(new Color(13, 110, 253));
        nutXemChiTiet.setForeground(Color.WHITE);
        nutXemChiTiet.setPreferredSize(new Dimension(160, 36));
        nutXemChiTiet.setFocusPainted(false);

        nutLamMoi = new JButton("Làm mới");
        UIStyle.setButtonIcon(nutLamMoi, "load.png");
        nutLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nutLamMoi.setPreferredSize(new Dimension(120, 36));
        nutLamMoi.setFocusPainted(false);

        khungDuoi.add(nutXemChiTiet);
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
