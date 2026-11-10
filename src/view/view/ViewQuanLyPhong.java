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
import view.IConn;

public class ViewQuanLyPhong extends JFrame {

    // Bảng dữ liệu
    public JTable bang;
    public DefaultTableModel moHinh;
    public TableRowSorter<DefaultTableModel> boSapXep;

    // Tìm kiếm & Lọc
    public JTextField oNhapTimKiem;
    public JButton btTimKiem;
    public JComboBox<String> hopChonLocTrangThai;
    public JLabel nhanThongKe;

    // Các nút chức năng ở bottom
    public JButton btThem, btSua, btXoa, btLamMoi;
    public JButton btQuayLai = new JButton("Quay lại");
    private final boolean coQuyenQuanLyPhong;

    // Khai báo các field cũ để đảm bảo tương thích mã nguồn
    public JTextField oNhapMaPhong = new JTextField();
    public JTextField oNhapSoPhong = new JTextField();
    public JTextField oNhapMaLoai = new JTextField();
    public JTextField oNhapGia = new JTextField();
    public JTextField oNhapMoTa = new JTextField();
    public JComboBox<String> hopChonTrangThai = new JComboBox<>(new String[] { "", "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ" });

    public ViewQuanLyPhong() {
        this(true);
    }

    public ViewQuanLyPhong(boolean coQuyenQuanLyPhong) {
        this.coQuyenQuanLyPhong = coQuyenQuanLyPhong;
        setTitle("Quản lý khách sạn - Quản lý phòng");
        IConn.apply(this);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 550));
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

        JLabel nhanTieuDe = new JLabel("DANH SÁCH QUẢN LÝ PHÒNG");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nhanTieuDe.setForeground(new Color(30, 41, 59));

        nhanThongKe = new JLabel("Tổng số: 0 phòng | Trống: 0 | Có khách: 0 | Đang dọn: 0 | Bảo trì: 0");
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

        oNhapTimKiem = new JTextField(18);
        oNhapTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTimKiem.setPreferredSize(new Dimension(220, 34));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText", "Số phòng, loại phòng, tầng...");

        btTimKiem = new JButton("Tìm kiếm");
        btTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btTimKiem.setPreferredSize(new Dimension(110, 34));
        btTimKiem.setFocusPainted(false);

        JLabel nhanLoc = new JLabel("Lọc trạng thái:");
        nhanLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanLoc.setForeground(new Color(51, 65, 85));

        hopChonLocTrangThai = new JComboBox<>(new String[] {
            "Tất cả trạng thái", "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ"
        });
        hopChonLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hopChonLocTrangThai.setPreferredSize(new Dimension(160, 34));

        khungTimKiem.add(nhanTim);
        khungTimKiem.add(oNhapTimKiem);
        khungTimKiem.add(btTimKiem);
        khungTimKiem.add(Box.createHorizontalStrut(15));
        khungTimKiem.add(nhanLoc);
        khungTimKiem.add(hopChonLocTrangThai);

        khungTren.add(khungTimKiem);
        khungChinh.add(khungTren, BorderLayout.NORTH);

        // ================= CENTER: BẢNG DỮ LIỆU HIỆN ĐẠI =================
        // Cột: Mã phòng, Số phòng, Tầng, Loại phòng, Giá mỗi đêm, Trạng thái, Mô tả
        moHinh = new DefaultTableModel(
                new String[] { "ID", "Số phòng", "Tầng", "Loại phòng", "Giá mỗi đêm", "Trạng thái", "Mô tả" }, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int chiSoCot) {
                if (chiSoCot == 0 || chiSoCot == 2) return Integer.class;
                return String.class;
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

        // Header bảng
        JTableHeader tieuDe = bang.getTableHeader();
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tieuDe.setBackground(new Color(238, 242, 246));
        tieuDe.setForeground(new Color(30, 41, 59));
        tieuDe.setPreferredSize(new Dimension(tieuDe.getWidth(), 38));

        // Sắp xếp
        boSapXep = new TableRowSorter<>(moHinh);
        bang.setRowSorter(boSapXep);
        List<RowSorter.SortKey> cacKhoaSapXep = new ArrayList<>();
        cacKhoaSapXep.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        boSapXep.setSortKeys(cacKhoaSapXep);

        // Căn chỉnh các cột
        DefaultTableCellRenderer boKetXuatGiua = new DefaultTableCellRenderer();
        boKetXuatGiua.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer boKetXuatPhai = new DefaultTableCellRenderer();
        boKetXuatPhai.setHorizontalAlignment(SwingConstants.RIGHT);

        bang.getColumnModel().getColumn(0).setPreferredWidth(50);
        bang.getColumnModel().getColumn(0).setMaxWidth(70);
        bang.getColumnModel().getColumn(0).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(1).setPreferredWidth(100);
        DefaultTableCellRenderer boKetXuatCanGiua = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                          boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(CENTER);
                return thanhPhan;
            }
        };
        bang.getColumnModel().getColumn(1).setCellRenderer(boKetXuatCanGiua);

        bang.getColumnModel().getColumn(2).setPreferredWidth(80);
        bang.getColumnModel().getColumn(2).setCellRenderer(boKetXuatGiua);

        bang.getColumnModel().getColumn(3).setPreferredWidth(180);

        bang.getColumnModel().getColumn(4).setPreferredWidth(140);
        bang.getColumnModel().getColumn(4).setCellRenderer(boKetXuatPhai);

        bang.getColumnModel().getColumn(5).setPreferredWidth(130);
        // Renderer trạng thái dạng badge đẹp
        bang.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean duocChon, boolean tieuDiem,
                                                          int hang, int chiSoCot) {
                super.getTableCellRendererComponent(bang, giaTri, duocChon, tieuDiem, hang, chiSoCot);

                String trangThai = giaTri == null ? "" : giaTri.toString().trim();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (duocChon) {
                    setBackground(bang.getSelectionBackground());
                    setForeground(bang.getSelectionForeground());
                    return this;
                }

                switch (trangThai) {
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
                        setText(trangThai);
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                        break;
                }
                return this;
            }
        });

        bang.getColumnModel().getColumn(6).setPreferredWidth(250);

        // Click đúp vào dòng để mở modal sửa
        bang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                if (suKien.getClickCount() == 2 && bang.getSelectedRow() != -1) {
                    btSua.doClick();
                }
            }
        });

        JScrollPane cuonPane = new JScrollPane(bang);
        cuonPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235), 1));
        cuonPane.getViewport().setBackground(Color.WHITE);
        khungChinh.add(cuonPane, BorderLayout.CENTER);

        // ================= BOTTOM: CÁC NÚT THÊM, SỬA, XÓA, LÀM MỚI =================
        JPanel khungDuoi = new JPanel(new BorderLayout());
        khungDuoi.setOpaque(false);
        khungDuoi.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel khungNutTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungNutTrai.setOpaque(false);

        btThem = new JButton("Thêm phòng");
        btThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btThem.setBackground(new Color(13, 110, 253));
        btThem.setForeground(Color.BLACK);
        btThem.setPreferredSize(new Dimension(140, 36));
        btThem.setFocusPainted(false);
        btThem.setVisible(coQuyenQuanLyPhong);

        btSua = new JButton("Sửa phòng");
        btSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btSua.setBackground(new Color(255, 193, 7));
        btSua.setForeground(Color.BLACK);
        btSua.setPreferredSize(new Dimension(130, 36));
        btSua.setFocusPainted(false);

        btXoa = new JButton("Xóa phòng");
        btXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btXoa.setBackground(new Color(220, 53, 69));
        btXoa.setForeground(Color.BLACK);
        btXoa.setPreferredSize(new Dimension(130, 36));
        btXoa.setFocusPainted(false);
        btXoa.setVisible(coQuyenQuanLyPhong);

        btLamMoi = new JButton("Làm mới");
        btLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btLamMoi.setPreferredSize(new Dimension(140, 36));
        btLamMoi.setFocusPainted(false);

        khungNutTrai.add(btThem);
        khungNutTrai.add(btSua);
        khungNutTrai.add(btXoa);
        khungNutTrai.add(btLamMoi);

        khungDuoi.add(khungNutTrai, BorderLayout.CENTER);
        khungChinh.add(khungDuoi, BorderLayout.SOUTH);

        setContentPane(khungChinh);

        btQuayLai.addActionListener(ac -> {
            new ViewTrangChu().setVisible(true);
            dispose();
        });
    }

    public boolean coQuyenQuanLyPhong() {
        return coQuyenQuanLyPhong;
    }

    public void lamMoi() {
        oNhapTimKiem.setText("");
        hopChonLocTrangThai.setSelectedIndex(0);
        bang.clearSelection();
    }

    public String chuyenTrangThai(String trangThai) {
        if (trangThai == null) return "";
        switch (trangThai) {
            case "AVAILABLE": return "TRỐNG";
            case "OCCUPIED": return "CÓ KHÁCH";
            case "CLEANING": return "ĐANG DỌN";
            case "MAINTENANCE": return "BẢO TRÌ";
            default: return trangThai;
        }
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception biBoQua) {}
            new ViewQuanLyPhong().setVisible(true);
        });
    }
}
