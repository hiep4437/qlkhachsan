package view.view;

import view.dialog.DialogThongTinTaiKhoan;
import view.dialog.DialogTraPhongThanhToan;

import controller.DatPhongController;
import controller.KhachHangController;
import controller.HoaDonController;
import controller.LoginController;
import controller.PhongController;
import controller.StatisticController;
import controller.NguoiDungController;
import dao.KetNoiCSDL;
import model.ThongTinPhongDangO;
import model.DatPhong;
import model.KhachHang;
import model.Phong;
import model.NguoiDung;
import service.DatPhongService;
import service.KhachHangService;
import service.PhongService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

public class ViewTrangChu extends JFrame {

    private NguoiDung nguoiDungHienTai;
    private PhongService dichVuPhong = new PhongService();
    private DatPhongService dichVuDatPhong = new DatPhongService();
    private KhachHangService dichVuKhachHang = new KhachHangService();

    private DecimalFormat dinhDangTienTe = new DecimalFormat("#,### đ");

    // UI components
    private JTabbedPane khungTab;
    private JPanel khungLuoiPhong;
    private JComboBox<String> hopChonLocTrangThai;
    private JComboBox<String> hopChonLocTang;
    private JTextField oNhapTimPhong;
    private JLabel nhanStatsChung, nhanStatsTrong, nhanStatsCoKhach, nhanStatsDon, nhanStatsBaoTri;
    private JButton nutNguoiDungMenu;
    private NguoiDungController boDieuKhienNguoiDung;
    private DatPhongController boDieuKhienDatPhong;
    private StatisticController statisticBoDieuKhien;

    public ViewTrangChu() {
        this(new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewTrangChu(NguoiDung nguoiDung) {
        this.nguoiDungHienTai = (nguoiDung != null) ? nguoiDung : new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        setTitle("Hệ thống quản lý khách sạn");
        AppWindowIcon.apply(this);
        GraphicsEnvironment thamSoMoiTruong = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setMaximizedBounds(thamSoMoiTruong.getMaximumWindowBounds());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        taiDuLieuSoDoPhong();
    }

    // Chuyển sang tab Quản lý đặt phòng và tự động tìm theo CCCD của khách
    public void chuyenSangTabDatPhongVaTimKiem(String tuKhoa) {
        if (khungTab == null) return;
        for (int chiSo = 0; chiSo < khungTab.getTabCount(); chiSo++) {
            String deMuc = khungTab.getTitleAt(chiSo);
            if (deMuc != null && (deMuc.contains("đặt phòng") || deMuc.contains("Dat phong"))) {
                khungTab.setSelectedIndex(chiSo);
                break;
            }
        }
        if (boDieuKhienDatPhong != null) {
            boDieuKhienDatPhong.timKiemToanBo(tuKhoa);
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 1. Top Header
        JPanel khungDauTrang = new JPanel(new BorderLayout());
        khungDauTrang.setBackground(new Color(24, 44, 97));
        khungDauTrang.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Header Left: Logo and title
        JPanel khungTieuDe = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungTieuDe.setOpaque(false);
        JLabel nhanBieuTuong = new JLabel("");
        nhanBieuTuong.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel nhanTieuDe = new JLabel("QUẢN LÝ KHÁCH SẠN");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nhanTieuDe.setForeground(Color.WHITE);
        khungTieuDe.add(nhanBieuTuong);
        khungTieuDe.add(nhanTieuDe);

        // Header Right: User info dropdown menu & logout
        JPanel khungNguoiDung = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        khungNguoiDung.setOpaque(false);

        nutNguoiDungMenu = new JButton();
        capNhatNutUserMenu();
        nutNguoiDungMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutNguoiDungMenu.setIcon(UIStyle.createTintedIcon("user.256.png", 18, Color.WHITE));
        nutNguoiDungMenu.setIconTextGap(8);
        nutNguoiDungMenu.setForeground(Color.WHITE);
        nutNguoiDungMenu.setBackground(new Color(36, 64, 132));
        nutNguoiDungMenu.setFocusPainted(false);
        nutNguoiDungMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutNguoiDungMenu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 110, 190), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        nutNguoiDungMenu.addActionListener(suKien -> hienThiUserDropdownMenu(nutNguoiDungMenu));

        JButton nutDangXuat = new JButton("Đăng xuất");
        nutDangXuat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nutDangXuat.setIcon(UIStyle.createTintedIcon("logout.png", 18, Color.WHITE));
        nutDangXuat.setIconTextGap(8);
        nutDangXuat.setBackground(new Color(192, 57, 43));
        nutDangXuat.setForeground(Color.WHITE);
        nutDangXuat.setFocusPainted(false);
        nutDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutDangXuat.setPreferredSize(nutNguoiDungMenu.getPreferredSize());
        nutDangXuat.addActionListener(suKien -> dangXuat());

        khungNguoiDung.add(nutNguoiDungMenu);
        khungNguoiDung.add(nutDangXuat);

        khungDauTrang.add(khungTieuDe, BorderLayout.WEST);
        khungDauTrang.add(khungNguoiDung, BorderLayout.EAST);
        add(khungDauTrang, BorderLayout.NORTH);

        // 2. Main Tabbed Pane
        khungTab = new JTabbedPane();
        khungTab.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab 0: Sơ đồ phòng (Màn hình chính)
        JPanel theSoThucHien = taoPanelSoDoPhong();
        khungTab.addTab("Sơ đồ phòng", theSoThucHien);

        // Tab 1: Quản lý phòng
        try {
            ViewQuanLyPhong viewPhong = new ViewQuanLyPhong(nguoiDungHienTai.isAdmin());
            new PhongController(viewPhong);
            khungTab.addTab("Quản lý phòng", viewPhong.getContentPane());
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewQuanLyPhong: " + ngoaiLe.getMessage());
        }

        // Tab 2: Quản lý đặt phòng
        try {
            ViewDatPhong viewDatPhong = new ViewDatPhong();
            boDieuKhienDatPhong = new DatPhongController(viewDatPhong);
            khungTab.addTab("Quản lý đặt phòng", viewDatPhong.getContentPane());
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewDatPhong: " + ngoaiLe.getMessage());
        }

        // Tab 3: Quản lý hóa đơn
        try {
            ViewHoaDon viewHoaDon = new ViewHoaDon();
            new HoaDonController(viewHoaDon);
            khungTab.addTab("Quản lý hóa đơn", viewHoaDon.getContentPane());
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewHoaDon: " + ngoaiLe.getMessage());
        }

        // Tab 4: Quản lý khách hàng
        try {
            ViewQuanLyKhachHang viewKhachHang = new ViewQuanLyKhachHang();
            new KhachHangController(viewKhachHang, this::chuyenSangTabDatPhongVaTimKiem);
            khungTab.addTab("Quản lý khách hàng", viewKhachHang.getContentPane());
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewQuanLyKhachHang: " + ngoaiLe.getMessage());
        }

        // Các Tab chỉ dành riêng cho ADMIN: Báo cáo thống kê & Quản lý tài khoản
        if (nguoiDungHienTai.isAdmin()) {
            // Tab 5: Báo cáo thống kê (ADMIN)
            try {
                ViewBaoCaoThongKe viewThongKe = new ViewBaoCaoThongKe();
                statisticBoDieuKhien = new StatisticController(viewThongKe);
                khungTab.addTab("Báo cáo thống kê", viewThongKe.getContentPane());
            } catch (Exception ngoaiLe) {
                System.err.println("Không thể nhúng ViewBaoCaoThongKe: " + ngoaiLe.getMessage());
            }

            // Tab 6: Quản lý tài khoản (ADMIN)
            try {
                ViewQuanLyTaiKhoan viewTaiKhoan = new ViewQuanLyTaiKhoan(nguoiDungHienTai);
                boDieuKhienNguoiDung = new NguoiDungController(viewTaiKhoan, nguoiDungHienTai);
                khungTab.addTab("Quản lý tài khoản", viewTaiKhoan.getContentPane());
            } catch (Exception ngoaiLe) {
                System.err.println("Không thể nhúng ViewQuanLyTaiKhoan: " + ngoaiLe.getMessage());
            }
        }

        // Khi người dùng chuyển tab thì tự động reload dữ liệu tương ứng
        khungTab.addChangeListener(suKien -> {
            int duocChon = khungTab.getSelectedIndex();
            if (duocChon == 0) {
                taiDuLieuSoDoPhong();
            } else if (duocChon >= 0 && duocChon < khungTab.getTabCount()) {
                String deMuc = khungTab.getTitleAt(duocChon);
                if (deMuc != null) {
                    if (deMuc.contains("Quản lý tài khoản") && boDieuKhienNguoiDung != null) {
                        boDieuKhienNguoiDung.taiDuLieu();
                    } else if (deMuc.contains("Báo cáo thống kê") && statisticBoDieuKhien != null) {
                        statisticBoDieuKhien.taiDuLieu();
                    }
                }
            }
        });

        add(khungTab, BorderLayout.CENTER);
    }

    private JPanel taoPanelSoDoPhong() {
        JPanel khung = new JPanel(new BorderLayout(10, 10));
        khung.setBackground(new Color(245, 247, 250));
        khung.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 1. Thanh công cụ lọc & thống kê
        JPanel thanhCongCu = new JPanel(new BorderLayout(10, 10));
        thanhCongCu.setBackground(Color.WHITE);
        thanhCongCu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        // Bộ lọc bên trái
        JPanel khungLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        khungLoc.setOpaque(false);

        khungLoc.add(new JLabel("Trạng thái:"));
        hopChonLocTrangThai = new JComboBox<>(new String[] {
                "Tất cả trạng thái",
                "Trống (AVAILABLE)",
                "Có khách (OCCUPIED)",
                "Đang dọn (CLEANING)",
                "Bảo trì (MAINTENANCE)"
        });
        hopChonLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hopChonLocTrangThai.addActionListener(suKien -> taiDuLieuSoDoPhong());
        khungLoc.add(hopChonLocTrangThai);

        khungLoc.add(Box.createHorizontalStrut(5));
        khungLoc.add(new JLabel("Tầng:"));
        hopChonLocTang = new JComboBox<>(new String[] {
                "Tất cả các tầng", "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5"
        });
        hopChonLocTang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hopChonLocTang.addActionListener(suKien -> taiDuLieuSoDoPhong());
        khungLoc.add(hopChonLocTang);

        khungLoc.add(Box.createHorizontalStrut(5));
        khungLoc.add(new JLabel("Tìm số phòng:"));
        oNhapTimPhong = new JTextField(6);
        oNhapTimPhong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        oNhapTimPhong.addActionListener(suKien -> taiDuLieuSoDoPhong());
        khungLoc.add(oNhapTimPhong);

        JButton nutTim = new JButton("Tìm");
        UIStyle.setButtonIcon(nutTim, "timkiem.png");
        nutTim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nutTim.addActionListener(suKien -> taiDuLieuSoDoPhong());
        khungLoc.add(nutTim);

        JButton nutLamMoi = new JButton("Làm mới");
        UIStyle.setButtonIcon(nutLamMoi, "load.png");
        nutLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nutLamMoi.setBackground(new Color(41, 128, 185));
        nutLamMoi.setForeground(Color.WHITE);
        nutLamMoi.setFocusPainted(false);
        nutLamMoi.addActionListener(suKien -> {
            oNhapTimPhong.setText("");
            hopChonLocTrangThai.setSelectedIndex(0);
            hopChonLocTang.setSelectedIndex(0);
            taiDuLieuSoDoPhong();
        });
        khungLoc.add(nutLamMoi);

        // Thống kê nhanh bên phải
        JPanel statsKhung = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        statsKhung.setOpaque(false);

        nhanStatsChung = taoBadge("Tổng: 0", new Color(44, 62, 80), new Color(236, 240, 241));
        nhanStatsTrong = taoBadge("Trống: 0", new Color(39, 174, 96), new Color(234, 250, 241));
        nhanStatsCoKhach = taoBadge("Có khách: 0", new Color(192, 57, 43), new Color(253, 237, 236));
        nhanStatsDon = taoBadge("Đang dọn: 0", new Color(211, 84, 0), new Color(254, 249, 231));
        nhanStatsBaoTri = taoBadge("Bảo trì: 0", new Color(127, 140, 141), new Color(242, 244, 244));

        statsKhung.add(nhanStatsChung);
        statsKhung.add(nhanStatsTrong);
        statsKhung.add(nhanStatsCoKhach);
        statsKhung.add(nhanStatsDon);
        statsKhung.add(nhanStatsBaoTri);

        thanhCongCu.add(khungLoc, BorderLayout.WEST);
        thanhCongCu.add(statsKhung, BorderLayout.EAST);
        khung.add(thanhCongCu, BorderLayout.NORTH);

        // 2. Khu vực hiển thị danh sách phòng phân tầng
        khungLuoiPhong = new JPanel();
        khungLuoiPhong.setLayout(new BoxLayout(khungLuoiPhong, BoxLayout.Y_AXIS));
        khungLuoiPhong.setBackground(new Color(245, 247, 250));

        JScrollPane cuonPane = new JScrollPane(khungLuoiPhong);
        cuonPane.setBorder(null);
        cuonPane.getVerticalScrollBar().setUnitIncrement(16);
        khung.add(cuonPane, BorderLayout.CENTER);

        return khung;
    }

    private JLabel taoBadge(String vanBan, Color mauChu, Color mauNen) {
        JLabel nhan = new JLabel(vanBan);
        nhan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nhan.setForeground(mauChu);
        nhan.setBackground(mauNen);
        nhan.setOpaque(true);
        nhan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mauChu, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return nhan;
    }

    // Tải và vẽ sơ đồ phòng
    public void taiDuLieuSoDoPhong() {
        khungLuoiPhong.removeAll();

        ArrayList<Phong> danhSachPhong = dichVuPhong.xuat();

        // 1. Đếm thống kê
        int soLuongChung = danhSachPhong.size();
        int soLuongTrong = 0;
        int soLuongCoKhach = 0;
        int soLuongDon = 0;
        int soLuongBaoTri = 0;

        for (Phong hangHienTai : danhSachPhong) {
            String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";
            if (luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG"))
                soLuongTrong++;
            else if (luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH"))
                soLuongCoKhach++;
            else if (luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN"))
                soLuongDon++;
            else if (luongTrinh.contains("MAINTENANCE") || luongTrinh.contains("BẢO TRÌ"))
                soLuongBaoTri++;
        }

        nhanStatsChung.setText("Tổng: " + soLuongChung);
        nhanStatsTrong.setText("Trống: " + soLuongTrong);
        nhanStatsCoKhach.setText("Có khách: " + soLuongCoKhach);
        nhanStatsDon.setText("Đang dọn: " + soLuongDon);
        nhanStatsBaoTri.setText("Bảo trì: " + soLuongBaoTri);

        // 2. Lọc theo trạng thái, tầng và từ khóa tìm kiếm
        String locTrangThai = (String) hopChonLocTrangThai.getSelectedItem();
        String locTang = (String) hopChonLocTang.getSelectedItem();
        String tuKhoa = oNhapTimPhong.getText().trim().toLowerCase();

        // Nhóm phòng theo tầng: Tầng 1, Tầng 2, Tầng 3...
        Map<String, ArrayList<Phong>> nhomTheoTang = new LinkedHashMap<>();

        for (Phong hangHienTai : danhSachPhong) {
            // Lọc từ khóa số phòng
            if (!tuKhoa.isEmpty() && !hangHienTai.getSoPhong().toLowerCase().contains(tuKhoa)) {
                continue;
            }

            // Lọc trạng thái
            String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";
            if (locTrangThai.contains("AVAILABLE")
                    && !(luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG")))
                continue;
            if (locTrangThai.contains("OCCUPIED")
                    && !(luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")))
                continue;
            if (locTrangThai.contains("CLEANING")
                    && !(luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")))
                continue;
            if (locTrangThai.contains("MAINTENANCE") && !(luongTrinh.contains("MAINTENANCE") || luongTrinh.contains("BẢO TRÌ")))
                continue;

            // Xác định tầng
            String tenTang = xacDinhTang(hangHienTai);

            // Lọc tầng
            if (!"Tất cả các tầng".equals(locTang) && !tenTang.equalsIgnoreCase(locTang)) {
                continue;
            }

            nhomTheoTang.computeIfAbsent(tenTang, khoa -> new ArrayList<>()).add(hangHienTai);
        }

        if (nhomTheoTang.isEmpty()) {
            JPanel khungTrong = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
            khungTrong.setOpaque(false);
            JLabel nhanTrong = new JLabel("Không tìm thấy phòng nào phù hợp với bộ lọc.");
            nhanTrong.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            nhanTrong.setForeground(Color.GRAY);
            khungTrong.add(nhanTrong);
            khungLuoiPhong.add(khungTrong);
        } else {
            // Sắp xếp tầng từ 1 đến N
            for (String tang : nhomTheoTang.keySet()) {
                ArrayList<Phong> phongCuaTang = nhomTheoTang.get(tang);

                // Section Panel của tầng
                JPanel khuVucTang = new JPanel(new BorderLayout(5, 5));
                khuVucTang.setOpaque(false);
                khuVucTang.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

                // Tiêu đề tầng
                int trongCuaTang = 0;
                for (Phong phong : phongCuaTang) {
                    String luongTrinh = (phong.getTrangThai() != null) ? phong.getTrangThai().toUpperCase() : "AVAILABLE";
                    if (luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG"))
                        trongCuaTang++;
                }

                JPanel tieuDeTang = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
                tieuDeTang.setBackground(new Color(235, 245, 251));
                tieuDeTang.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(41, 128, 185)),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));

                JLabel nhanTieuDeTang = new JLabel(" " + tang.toUpperCase());
                nhanTieuDeTang.setFont(new Font("Segoe UI", Font.BOLD, 14));
                nhanTieuDeTang.setForeground(new Color(21, 67, 96));

                JLabel nhanTangPhu = new JLabel(
                        "(" + phongCuaTang.size() + " phòng | " + trongCuaTang + " phòng trống)");
                nhanTangPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                nhanTangPhu.setForeground(new Color(86, 101, 115));

                tieuDeTang.add(nhanTieuDeTang);
                tieuDeTang.add(nhanTangPhu);
                khuVucTang.add(tieuDeTang, BorderLayout.NORTH);

                // Grid các thẻ phòng của tầng
                JPanel khungCacPhong = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
                khungCacPhong.setOpaque(false);

                for (Phong hangHienTai : phongCuaTang) {
                    JPanel the = taoThePhong(hangHienTai);
                    khungCacPhong.add(the);
                }

                khuVucTang.add(khungCacPhong, BorderLayout.CENTER);
                khungLuoiPhong.add(khuVucTang);
            }
        }

        khungLuoiPhong.revalidate();
        khungLuoiPhong.repaint();
    }

    private String xacDinhTang(Phong hangHienTai) {
        if (hangHienTai.getMoTa() != null && !hangHienTai.getMoTa().isEmpty()) {
            String moTaChuThuong = hangHienTai.getMoTa().toLowerCase();
            if (moTaChuThuong.contains("tầng 1") || moTaChuThuong.contains("tang 1"))
                return "Tầng 1";
            if (moTaChuThuong.contains("tầng 2") || moTaChuThuong.contains("tang 2"))
                return "Tầng 2";
            if (moTaChuThuong.contains("tầng 3") || moTaChuThuong.contains("tang 3"))
                return "Tầng 3";
            if (moTaChuThuong.contains("tầng 4") || moTaChuThuong.contains("tang 4"))
                return "Tầng 4";
            if (moTaChuThuong.contains("tầng 5") || moTaChuThuong.contains("tang 5"))
                return "Tầng 5";
        }
        String soPhong = hangHienTai.getSoPhong();
        if (soPhong != null && !soPhong.isEmpty()) {
            char kyTuDau = soPhong.charAt(0);
            if (Character.isDigit(kyTuDau)) {
                return "Tầng " + kyTuDau;
            }
        }
        return "Tầng 1";
    }

    // Tạo Card phòng tương tác
    private JPanel taoThePhong(Phong hangHienTai) {
        String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";

        Color mauVien;
        Color mauNenTieuDe;
        Color mauChuHuyHieu;
        Color mauNenHuyHieu;
        String chuHuyHieu;
        String chuoiHanhDong;

        ThongTinPhongDangO khachDangO = null;

        if (luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG")) {
            mauVien = new Color(39, 174, 96);
            mauNenTieuDe = new Color(234, 250, 241);
            mauChuHuyHieu = new Color(30, 132, 73);
            mauNenHuyHieu = new Color(212, 239, 223);
            chuHuyHieu = "TRỐNG";
            chuoiHanhDong = " Bấm để Đặt phòng";
        } else if (luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")) {
            mauVien = new Color(231, 76, 60);
            mauNenTieuDe = new Color(253, 237, 236);
            mauChuHuyHieu = new Color(146, 43, 33);
            mauNenHuyHieu = new Color(249, 219, 216);
            chuHuyHieu = "CÓ KHÁCH";

            // Lấy thông tin khách đang ở
            khachDangO = dichVuPhong.layThongTinKhachDangO(hangHienTai.getMaPhong());
            String tenKhach = (khachDangO != null && khachDangO.getTenKhachHang() != null) ? khachDangO.getTenKhachHang()
                    : "Khách lưu trú";
            chuoiHanhDong = " " + tenKhach;
        } else if (luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")) {
            mauVien = new Color(243, 156, 18);
            mauNenTieuDe = new Color(254, 249, 231);
            mauChuHuyHieu = new Color(185, 119, 14);
            mauNenHuyHieu = new Color(252, 243, 207);
            chuHuyHieu = "ĐANG DỌN";
            chuoiHanhDong = " Bấm để xong dọn";
        } else { // MAINTENANCE
            mauVien = new Color(127, 140, 141);
            mauNenTieuDe = new Color(242, 244, 244);
            mauChuHuyHieu = new Color(86, 101, 115);
            mauNenHuyHieu = new Color(230, 234, 234);
            chuHuyHieu = "BẢO TRÌ";
            chuoiHanhDong = " Bấm để hoàn tất";
        }

        JPanel the = new JPanel(new BorderLayout());
        the.setPreferredSize(new Dimension(180, 130));
        the.setBackground(Color.WHITE);
        the.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mauVien, 2),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        the.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Top: Room number + Badge
        JPanel khungTren = new JPanel(new BorderLayout());
        khungTren.setBackground(mauNenTieuDe);
        khungTren.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JLabel nhanSoPhong = new JLabel("P." + hangHienTai.getSoPhong());
        nhanSoPhong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nhanSoPhong.setForeground(new Color(24, 44, 97));

        JLabel nhanHuyHieu = new JLabel(chuHuyHieu);
        nhanHuyHieu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nhanHuyHieu.setForeground(mauChuHuyHieu);
        nhanHuyHieu.setBackground(mauNenHuyHieu);
        nhanHuyHieu.setOpaque(true);
        nhanHuyHieu.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        khungTren.add(nhanSoPhong, BorderLayout.WEST);
        khungTren.add(nhanHuyHieu, BorderLayout.EAST);

        // Center: Type + Price
        JPanel khungGiua = new JPanel();
        khungGiua.setLayout(new BoxLayout(khungGiua, BoxLayout.Y_AXIS));
        khungGiua.setOpaque(false);
        khungGiua.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        String loaiPhong = (hangHienTai.getTenLoaiPhong() != null && !hangHienTai.getTenLoaiPhong().isEmpty()) ? hangHienTai.getTenLoaiPhong()
                : "Phòng tiêu chuẩn";
        JLabel nhanLoai = new JLabel(loaiPhong);
        nhanLoai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanLoai.setForeground(new Color(80, 80, 80));

        JLabel nhanGia = new JLabel(dinhDangTienTe.format(hangHienTai.getGiaMoiDem()) + "/đêm");
        nhanGia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nhanGia.setForeground(new Color(41, 128, 185));

        khungGiua.add(nhanLoai);
        khungGiua.add(Box.createVerticalStrut(2));
        khungGiua.add(nhanGia);

        // Bottom: Action guide or guest name
        JPanel khungDuoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        khungDuoi.setOpaque(false);
        JLabel nhanPhu = new JLabel(chuoiHanhDong);
        nhanPhu.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        if (chuHuyHieu.equals("CÓ KHÁCH")) {
            nhanPhu.setFont(new Font("Segoe UI", Font.BOLD, 11));
            nhanPhu.setForeground(new Color(192, 57, 43));
        } else {
            nhanPhu.setForeground(new Color(100, 100, 100));
        }
        khungDuoi.add(nhanPhu);

        the.add(khungTren, BorderLayout.NORTH);
        the.add(khungGiua, BorderLayout.CENTER);
        the.add(khungDuoi, BorderLayout.SOUTH);

        // Click listener
        final ThongTinPhongDangO thongTinKhach = khachDangO;
        the.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                xuLyClickThePhong(hangHienTai, thongTinKhach);
            }

            @Override
            public void mouseEntered(MouseEvent suKien) {
                the.setBackground(new Color(250, 252, 255));
            }

            @Override
            public void mouseExited(MouseEvent suKien) {
                the.setBackground(Color.WHITE);
            }
        });

        return the;
    }

    // Xử lý sự kiện click vào thẻ phòng theo đúng yêu cầu nghiệp vụ
    private void xuLyClickThePhong(Phong hangHienTai, ThongTinPhongDangO thongTinKhach) {
        String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";

        if (luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG")) {
            // Mở hộp thoại ĐẶT PHÒNG NHANH
            moDialogDatPhongNhanh(hangHienTai);
        } else if (luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")) {
            // Mở hộp thoại TRẢ PHÒNG & HÓA ĐƠN -> Chuyển sang CLEANING
            moDialogTraPhongHoaDon(hangHienTai, thongTinKhach);
        } else if (luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")) {
            // Hộp thoại hoàn tất dọn phòng
            xacNhanDonPhong(hangHienTai);
        } else {
            // Hộp thoại hoàn tất bảo trì
            xacNhanBaoTri(hangHienTai);
        }
    }

    // =========================================================================
    // 1. DIALOG ĐẶT PHÒNG NHANH (Khi phòng TRỐNG)
    // =========================================================================
    private void moDialogDatPhongNhanh(Phong hangHienTai) {
        JDialog hopThoai = new JDialog(this, "Đặt phòng / Check-in - Phòng " + hangHienTai.getSoPhong(), true);
        hopThoai.setSize(520, 560);
        hopThoai.setLocationRelativeTo(this);
        hopThoai.setLayout(new BorderLayout(10, 10));

        // Header
        JPanel khungDau = new JPanel(new GridLayout(2, 1, 4, 4));
        khungDau.setBackground(new Color(234, 250, 241));
        khungDau.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel nhanTieuDe = new JLabel("ĐẶT PHÒNG NHANH: PHÒNG " + hangHienTai.getSoPhong());
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nhanTieuDe.setForeground(new Color(30, 132, 73));

        String loai = (hangHienTai.getTenLoaiPhong() != null) ? hangHienTai.getTenLoaiPhong() : "Phòng tiêu chuẩn";
        JLabel nhanMoTa = new JLabel(
                "Loại: " + loai + "  |  Đơn giá: " + dinhDangTienTe.format(hangHienTai.getGiaMoiDem()) + "/đêm");
        nhanMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        khungDau.add(nhanTieuDe);
        khungDau.add(nhanMoTa);

        // Form fields
        JPanel khungBieuMau = new JPanel(new GridBagLayout());
        khungBieuMau.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(6, 6, 6, 6);

        JTextField oNhapCCCD = new JTextField(12);
        JButton nutKiemTraCCCD = new JButton("Tìm khách");
        UIStyle.setButtonIcon(nutKiemTraCCCD, "timkiem.png");
        JTextField oNhapHoTen = new JTextField(15);
        JTextField oNhapSDT = new JTextField(15);
        JTextField oNhapDiaChi = new JTextField(15);

        LocalDate homNay = LocalDate.now();
        LocalDate ngayMai = homNay.plusDays(1);
        DatePickerField oNhapNgayNhan = new DatePickerField(homNay, homNay);
        DatePickerField oNhapNgayTra = new DatePickerField(ngayMai, ngayMai);
        JTextField oNhapGhiChu = new JTextField("Khách đặt trực tiếp", 15);
        int sucChua = Math.max(1, hangHienTai.getSucChua());
        JSpinner vongQuaySoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, sucChua, 1));

        JLabel nhanTamTinh = new JLabel(dinhDangTienTe.format(hangHienTai.getGiaMoiDem()) + " (1 đêm)");
        nhanTamTinh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanTamTinh.setForeground(new Color(41, 128, 185));

        // Hàng 0: CCCD + Nút tìm
        gia.gridx = 0;
        gia.gridy = 0;
        khungBieuMau.add(new JLabel("CCCD khách hàng:*"), gia);
        JPanel khungCCCD = new JPanel(new BorderLayout(5, 0));
        khungCCCD.add(oNhapCCCD, BorderLayout.CENTER);
        khungCCCD.add(nutKiemTraCCCD, BorderLayout.EAST);
        gia.gridx = 1;
        gia.gridy = 0;
        khungBieuMau.add(khungCCCD, gia);

        // Hàng 1: Họ tên
        gia.gridx = 0;
        gia.gridy = 1;
        khungBieuMau.add(new JLabel("Họ tên khách:*"), gia);
        gia.gridx = 1;
        gia.gridy = 1;
        khungBieuMau.add(oNhapHoTen, gia);

        // Hàng 2: SĐT
        gia.gridx = 0;
        gia.gridy = 2;
        khungBieuMau.add(new JLabel("Số điện thoại:*"), gia);
        gia.gridx = 1;
        gia.gridy = 2;
        khungBieuMau.add(oNhapSDT, gia);

        // Hàng 3: Địa chỉ
        gia.gridx = 0;
        gia.gridy = 3;
        khungBieuMau.add(new JLabel("Địa chỉ:"), gia);
        gia.gridx = 1;
        gia.gridy = 3;
        khungBieuMau.add(oNhapDiaChi, gia);

        // Hàng 4: Ngày nhận
        gia.gridx = 0;
        gia.gridy = 4;
        khungBieuMau.add(new JLabel("Ngày nhận:"), gia);
        gia.gridx = 1;
        gia.gridy = 4;
        khungBieuMau.add(oNhapNgayNhan, gia);

        // Hàng 5: Ngày trả
        gia.gridx = 0;
        gia.gridy = 5;
        khungBieuMau.add(new JLabel("Ngày trả:"), gia);
        gia.gridx = 1;
        gia.gridy = 5;
        khungBieuMau.add(oNhapNgayTra, gia);

        oNhapNgayNhan.addDateChangeListener(suKien -> {
            LocalDate earliestTraPhong = oNhapNgayNhan.getDate().plusDays(1);
            oNhapNgayTra.setMinimumDate(earliestTraPhong);
            if (oNhapNgayTra.getDate().isBefore(earliestTraPhong)) {
                oNhapNgayTra.setDate(earliestTraPhong);
            }
        });

        // Hàng 6: Tạm tính
        gia.gridx = 0;
        gia.gridy = 6;
        khungBieuMau.add(new JLabel("Tạm tính tiền phòng:"), gia);
        gia.gridx = 1;
        gia.gridy = 6;
        khungBieuMau.add(nhanTamTinh, gia);

        // Hàng 7: Số khách
        gia.gridx = 0;
        gia.gridy = 7;
        khungBieuMau.add(new JLabel("Số khách (tối đa " + sucChua + "):"), gia);
        gia.gridx = 1;
        gia.gridy = 7;
        khungBieuMau.add(vongQuaySoNguoi, gia);

        // Hàng 8: Ghi chú
        gia.gridx = 0;
        gia.gridy = 8;
        khungBieuMau.add(new JLabel("Ghi chú:"), gia);
        gia.gridx = 1;
        gia.gridy = 8;
        khungBieuMau.add(oNhapGhiChu, gia);

        // Tính lại số tiền khi đổi ngày
        Runnable tinhTien = () -> {
            long dem = ChronoUnit.DAYS.between(oNhapNgayNhan.getDate(), oNhapNgayTra.getDate());
            nhanTamTinh.setText(dinhDangTienTe.format(dem * hangHienTai.getGiaMoiDem()) + " (" + dem + " đêm)");
        };
        oNhapNgayNhan.addDateChangeListener(suKien -> tinhTien.run());
        oNhapNgayTra.addDateChangeListener(suKien -> tinhTien.run());

        // Tìm khách theo CCCD
        nutKiemTraCCCD.addActionListener(suKien -> {
            String cccd = oNhapCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(hopThoai, "Vui lòng nhập số CCCD cần tìm!");
                return;
            }
            KhachHang taiKhoan = new KhachHang();
            taiKhoan.setSoCCCD(cccd);
            KhachHang timThay = dichVuKhachHang.timKiem(taiKhoan);
            if (timThay != null) {
                oNhapHoTen.setText(timThay.getNameKhachHang());
                oNhapSDT.setText(timThay.getSoDienThoai());
                oNhapDiaChi.setText(timThay.getDiaChi());
                JOptionPane.showMessageDialog(hopThoai, "Tìm thấy khách quen: " + timThay.getNameKhachHang());
            } else {
                JOptionPane.showMessageDialog(hopThoai,
                        "Chưa có thông tin khách này. Vui lòng nhập họ tên và SĐT để tạo mới.");
                oNhapHoTen.requestFocus();
            }
        });

        // Nút hành động
        JPanel khungCacNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        JButton nutKiemTraIn = new JButton(" Nhận phòng");
        nutKiemTraIn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nutKiemTraIn.setBackground(new Color(39, 174, 96));
        nutKiemTraIn.setForeground(Color.WHITE);

        JButton nutHuy = new JButton("Hủy");
        khungCacNut.add(nutKiemTraIn);
        khungCacNut.add(nutHuy);

        nutHuy.addActionListener(suKien -> hopThoai.dispose());

        nutKiemTraIn.addActionListener(suKien -> {
            String cccd = oNhapCCCD.getText().trim();
            String hoTen = oNhapHoTen.getText().trim();
            String sdt = oNhapSDT.getText().trim();

            if (cccd.isEmpty() || hoTen.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(hopThoai, "Vui lòng nhập đủ CCCD, Họ tên và Số điện thoại khách hàng!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date ngayNhan;
            Date ngayTra;
            try {
                ngayNhan = Date.valueOf(oNhapNgayNhan.getDate());
                ngayTra = Date.valueOf(oNhapNgayTra.getDate());
                if (ngayNhan.toLocalDate().isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(hopThoai, "Ngày nhận phòng phải từ hôm nay trở đi!", "Ngày nhận không hợp lệ",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!ngayTra.after(ngayNhan)) {
                    JOptionPane.showMessageDialog(hopThoai, "Ngày trả phòng phải sau ngày nhận phòng!", "Lỗi ngày tháng",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ngoaiLe) {
                JOptionPane.showMessageDialog(hopThoai, "Vui lòng chọn ngày nhận và ngày trả hợp lệ!", "Lỗi ngày tháng",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tìm hoặc thêm khách hàng
            KhachHang taiKhoan = new KhachHang();
            taiKhoan.setSoCCCD(cccd);
            KhachHang khachHang = dichVuKhachHang.timKiem(taiKhoan);
            int maKhachHang;
            if (khachHang == null) {
                KhachHang moi = new KhachHang(0, hoTen, cccd, sdt, "", oNhapDiaChi.getText().trim());
                dichVuKhachHang.them(moi);
                KhachHang khachHangMoi = dichVuKhachHang.timKiem(taiKhoan);
                if (khachHangMoi != null) {
                    maKhachHang = khachHangMoi.getIdKhachHang();
                } else {
                    JOptionPane.showMessageDialog(hopThoai, "Không thể lưu thông tin khách hàng!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                maKhachHang = khachHang.getIdKhachHang();
            }

            // Tiến hành đặt phòng & check-in
            int maDatPhong = dichVuDatPhong.datPhongNhanh(maKhachHang, ngayNhan, ngayTra, "CHECKED_IN",
                    oNhapGhiChu.getText().trim(), hangHienTai.getMaPhong(), hangHienTai.getGiaMoiDem(),
                    (int) vongQuaySoNguoi.getValue());

            if (maDatPhong > 0) {
                JOptionPane.showMessageDialog(this,
                        "Nhận phòng thành công cho khách: " + hoTen + "\nPhòng " + hangHienTai.getSoPhong()
                                + " đã chuyển sang trạng thái CÓ KHÁCH (OCCUPIED).",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                hopThoai.dispose();
                taiDuLieuSoDoPhong();
            } else {
                JOptionPane.showMessageDialog(hopThoai, "Đặt phòng thất bại. Vui lòng thử lại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        hopThoai.add(khungDau, BorderLayout.NORTH);
        hopThoai.add(khungBieuMau, BorderLayout.CENTER);
        hopThoai.add(khungCacNut, BorderLayout.SOUTH);
        hopThoai.setVisible(true);
    }

    // =========================================================================
    // 2. DIALOG TRẢ PHÒNG & XUẤT HÓA ĐƠN -> CHUYỂN SANG CLEANING (Khi phòng CÓ KHÁCH)
    // =========================================================================
    private void moDialogTraPhongHoaDon(Phong hangHienTai, ThongTinPhongDangO thongTin) {
        DatPhong nut = new DatPhong();
        nut.setIdDatPhong(thongTin != null ? thongTin.getBookingId() : 0);
        nut.setMaPhong(hangHienTai.getMaPhong());
        nut.setSoPhong(hangHienTai.getSoPhong());
        nut.setGiaMoiDem((thongTin != null && thongTin.getGiaMoiDem() > 0) ? thongTin.getGiaMoiDem() : hangHienTai.getGiaMoiDem());
        nut.setTenKhachHang((thongTin != null && thongTin.getTenKhachHang() != null) ? thongTin.getTenKhachHang() : "Khách lưu trú");
        nut.setSoDienThoai((thongTin != null && thongTin.getSoDienThoai() != null) ? thongTin.getSoDienThoai() : "");
        nut.setCccd((thongTin != null && thongTin.getCccd() != null) ? thongTin.getCccd() : "");
        nut.setNgayNhanPhong((thongTin != null && thongTin.getNgayNhan() != null) ? thongTin.getNgayNhan() : Date.valueOf(LocalDate.now()));
        nut.setTenLoaiPhong(hangHienTai.getTenLoaiPhong() != null ? hangHienTai.getTenLoaiPhong() : "Tiêu chuẩn");

        DialogTraPhongThanhToan hopThoai = new DialogTraPhongThanhToan(this, nut);
        hopThoai.setVisible(true);

        if (hopThoai.isCompleted()) {
            taiDuLieuSoDoPhong();
        }
    }

    // =========================================================================
    // 3. XÁC NHẬN HOÀN TẤT DỌN PHÒNG (CLEANING -> AVAILABLE)
    // =========================================================================
    private void xacNhanDonPhong(Phong hangHienTai) {
        Object[] cacLuaChon = { " Đã dọn xong (Sẵn sàng đón khách)", " Chuyển sang bảo trì", "Đóng" };
        int chon = JOptionPane.showOptionDialog(
                this,
                "Phòng " + hangHienTai.getSoPhong() + " hiện đang ở trạng thái ĐANG DỌN DẸP (CLEANING).\n"
                        + "Bạn muốn thao tác gì cho phòng này?",
                "Quản lý dọn phòng - Phòng " + hangHienTai.getSoPhong(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                cacLuaChon,
                cacLuaChon[0]);

        if (chon == 0) { // Đã dọn xong -> AVAILABLE
            dichVuPhong.doiTrangThai(hangHienTai.getMaPhong(), "AVAILABLE");
            JOptionPane.showMessageDialog(this, "Phòng " + hangHienTai.getSoPhong()
                    + " đã được chuyển sang trạng thái TRỐNG (AVAILABLE) và sẵn sàng đón khách!");
            taiDuLieuSoDoPhong();
        } else if (chon == 1) { // Bảo trì
            dichVuPhong.doiTrangThai(hangHienTai.getMaPhong(), "MAINTENANCE");
            JOptionPane.showMessageDialog(this, "Phòng " + hangHienTai.getSoPhong() + " đã chuyển sang BẢO TRÌ (MAINTENANCE).");
            taiDuLieuSoDoPhong();
        }
    }

    // =========================================================================
    // 4. XÁC NHẬN HOÀN TẤT BẢO TRÌ (MAINTENANCE -> AVAILABLE)
    // =========================================================================
    private void xacNhanBaoTri(Phong hangHienTai) {
        Object[] cacLuaChon = { " Hoàn tất sửa chữa (Chuyển sang TRỐNG)", " Chuyển sang dọn dẹp", "Đóng" };
        int chon = JOptionPane.showOptionDialog(
                this,
                "Phòng " + hangHienTai.getSoPhong() + " hiện đang BẢO TRÌ (MAINTENANCE).\n"
                        + "Bạn muốn thao tác gì cho phòng này?",
                "Quản lý bảo trì - Phòng " + hangHienTai.getSoPhong(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                cacLuaChon,
                cacLuaChon[0]);

        if (chon == 0) { // AVAILABLE
            dichVuPhong.doiTrangThai(hangHienTai.getMaPhong(), "AVAILABLE");
            JOptionPane.showMessageDialog(this,
                    "Phòng " + hangHienTai.getSoPhong() + " đã chuyển sang trạng thái TRỐNG (AVAILABLE)!");
            taiDuLieuSoDoPhong();
        } else if (chon == 1) { // CLEANING
            dichVuPhong.doiTrangThai(hangHienTai.getMaPhong(), "CLEANING");
            JOptionPane.showMessageDialog(this, "Phòng " + hangHienTai.getSoPhong() + " đã chuyển sang ĐANG DỌN (CLEANING).");
            taiDuLieuSoDoPhong();
        }
    }

    private void capNhatNutUserMenu() {
        if (nutNguoiDungMenu != null && nguoiDungHienTai != null) {
            String huyHieuVaiTro = nguoiDungHienTai.isAdmin() ? "ADMIN" : "STAFF";
            nutNguoiDungMenu.setText("  " + nguoiDungHienTai.getHoTen() + " (" + huyHieuVaiTro + ")  ");
        }
    }

    private void hienThiUserDropdownMenu(Component benGoi) {
        JPopupMenu menuBatLen = new JPopupMenu();
        menuBatLen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Header thông tin
        String chuoiVaiTro = nguoiDungHienTai.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)";
        JMenuItem menuTieuDe = new JMenuItem("<html><b>" + nguoiDungHienTai.getHoTen() + "</b><br><font color='#666666'>@"
                + nguoiDungHienTai.getTenDangNhap() + " • " + chuoiVaiTro + "</font></html>");
        menuTieuDe.setEnabled(false);
        menuTieuDe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuBatLen.add(menuTieuDe);
        menuBatLen.addSeparator();

        // 1. Xem thông tin tài khoản
        JMenuItem menuThongTin = new JMenuItem("  Xem thông tin tài khoản");
        menuThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuThongTin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuThongTin.addActionListener(suKien -> hienThiThongTinTaiKhoan());
        menuBatLen.add(menuThongTin);

        // 2. Quản lý tài khoản (cho ADMIN)
        if (nguoiDungHienTai.isAdmin()) {
            JMenuItem menuQuanLyTaiKhoan = new JMenuItem(" Quản lý tài khoản (Admin)");
            menuQuanLyTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            menuQuanLyTaiKhoan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuQuanLyTaiKhoan.addActionListener(suKien -> moTabQuanLyTaiKhoan());
            menuBatLen.add(menuQuanLyTaiKhoan);
        }

        menuBatLen.addSeparator();

        // 3. Đăng xuất
        JMenuItem menuDangXuat = new JMenuItem("Đăng xuất");
        menuDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menuDangXuat.setForeground(new Color(192, 57, 43));
        menuDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuDangXuat.addActionListener(suKien -> dangXuat());
        menuBatLen.add(menuDangXuat);

        menuBatLen.show(benGoi, 0, benGoi.getHeight() + 4);
    }

    private void hienThiThongTinTaiKhoan() {
        DialogThongTinTaiKhoan hopThoai = new DialogThongTinTaiKhoan(this, nguoiDungHienTai, () -> {
            capNhatNutUserMenu();
            if (boDieuKhienNguoiDung != null) {
                boDieuKhienNguoiDung.taiDuLieu();
            }
        });
        hopThoai.setVisible(true);
    }

    private void moTabQuanLyTaiKhoan() {
        for (int chiSo = 0; chiSo < khungTab.getTabCount(); chiSo++) {
            if (khungTab.getTitleAt(chiSo).contains("Quản lý tài khoản")) {
                khungTab.setSelectedIndex(chiSo);
                if (boDieuKhienNguoiDung != null) {
                    boDieuKhienNguoiDung.taiDuLieu();
                }
                return;
            }
        }
        // Neu chua co tab thi tao moi
        try {
            ViewQuanLyTaiKhoan viewTaiKhoan = new ViewQuanLyTaiKhoan(nguoiDungHienTai);
            boDieuKhienNguoiDung = new NguoiDungController(viewTaiKhoan, nguoiDungHienTai);
            viewTaiKhoan.nutQuayLai.setText("Về sơ đồ phòng");
            viewTaiKhoan.nutQuayLai.addActionListener(suKien -> {
                khungTab.setSelectedIndex(0);
                taiDuLieuSoDoPhong();
            });
            khungTab.addTab(" Quản lý tài khoản", viewTaiKhoan.getContentPane());
            khungTab.setSelectedComponent(viewTaiKhoan.getContentPane());
        } catch (Exception ngoaiLe) {
            ngoaiLe.printStackTrace();
        }
    }

    private void dangXuat() {
        int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (chon == JOptionPane.YES_OPTION) {
            // Đóng tất cả các cửa sổ hiện tại (tránh việc các sub-JFrame bị sót lại)
            for (Window cuaSo : Window.getWindows()) {
                cuaSo.dispose();
            }

            // Khởi tạo và hiển thị form đăng nhập duy nhất
            ViewDangNhap giaTriHienTai = new ViewDangNhap();
            new LoginController(giaTriHienTai);
            giaTriHienTai.setVisible(true);
        }
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> new ViewTrangChu().setVisible(true));
    }
}
