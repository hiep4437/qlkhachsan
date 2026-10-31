package view;

import controller.BookingController;
import controller.CustomerController;
import controller.InvoiceController;
import controller.LoginController;
import controller.RoomController;
import controller.StatisticController;
import controller.UserController;
import dao.DBConnection;
import model.ThongTinPhongDangO;
import model.bookings;
import model.customers;
import model.rooms;
import model.users;
import service.BookingService;
import service.CustomerService;
import service.RoomService;

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

    private users currentUser;
    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService();
    private CustomerService customerService = new CustomerService();

    private DecimalFormat currencyFormat = new DecimalFormat("#,### đ");

    // UI components
    private JTabbedPane tabbedPane;
    private JPanel panelGridPhong;
    private JComboBox<String> cboLocTrangThai;
    private JComboBox<String> cboLocTang;
    private JTextField txtTimPhong;
    private JLabel lblStatsTong, lblStatsTrong, lblStatsCoKhach, lblStatsDon, lblStatsBaoTri;
    private JButton btnUserMenu;
    private UserController userController;
    private BookingController bookingController;
    private StatisticController statisticController;

    public ViewTrangChu() {
        this(new users(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewTrangChu(users user) {
        this.currentUser = (user != null) ? user : new users(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        setTitle("Hệ thống quản lý khách sạn - Sơ đồ phòng & Vận hành");
        AppWindowIcon.apply(this);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setMaximizedBounds(env.getMaximumWindowBounds());
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
        if (tabbedPane == null) return;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if (title != null && (title.contains("đặt phòng") || title.contains("Dat phong"))) {
                tabbedPane.setSelectedIndex(i);
                break;
            }
        }
        if (bookingController != null) {
            bookingController.timKiemToanBo(tuKhoa);
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 1. Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 44, 97));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Header Left: Logo and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        JLabel lblLogo = new JLabel("");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH SẠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblLogo);
        titlePanel.add(lblTitle);

        // Header Right: User info dropdown menu & logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userPanel.setOpaque(false);

        btnUserMenu = new JButton();
        capNhatNutUserMenu();
        btnUserMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnUserMenu.setForeground(Color.WHITE);
        btnUserMenu.setBackground(new Color(36, 64, 132));
        btnUserMenu.setFocusPainted(false);
        btnUserMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUserMenu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 110, 190), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btnUserMenu.addActionListener(e -> hienThiUserDropdownMenu(btnUserMenu));

        JButton btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDangXuat.setBackground(new Color(192, 57, 43));
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangXuat.setPreferredSize(btnUserMenu.getPreferredSize());
        btnDangXuat.addActionListener(e -> dangXuat());

        userPanel.add(btnUserMenu);
        userPanel.add(btnDangXuat);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Main Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab 0: Sơ đồ phòng (Màn hình chính)
        JPanel tabSoDo = taoPanelSoDoPhong();
        tabbedPane.addTab("Sơ đồ phòng", tabSoDo);

        // Tab 1: Quản lý phòng
        try {
            ViewQuanLyPhong vPhong = new ViewQuanLyPhong();
            new RoomController(vPhong);
            tabbedPane.addTab("Quản lý phòng", vPhong.getContentPane());
        } catch (Exception ex) {
            System.err.println("Không thể nhúng ViewQuanLyPhong: " + ex.getMessage());
        }

        // Tab 2: Quản lý đặt phòng
        try {
            ViewDatPhong vDatPhong = new ViewDatPhong();
            bookingController = new BookingController(vDatPhong);
            tabbedPane.addTab("Quản lý đặt phòng", vDatPhong.getContentPane());
        } catch (Exception ex) {
            System.err.println("Không thể nhúng ViewDatPhong: " + ex.getMessage());
        }

        // Tab 3: Quản lý hóa đơn
        try {
            ViewHoaDon vHoaDon = new ViewHoaDon();
            new InvoiceController(vHoaDon);
            tabbedPane.addTab("Quản lý hóa đơn", vHoaDon.getContentPane());
        } catch (Exception ex) {
            System.err.println("Không thể nhúng ViewHoaDon: " + ex.getMessage());
        }

        // Tab 4: Quản lý khách hàng
        try {
            ViewQuanLyKhachHang vKhach = new ViewQuanLyKhachHang();
            new CustomerController(vKhach, this::chuyenSangTabDatPhongVaTimKiem);
            tabbedPane.addTab("Quản lý khách hàng", vKhach.getContentPane());
        } catch (Exception ex) {
            System.err.println("Không thể nhúng ViewQuanLyKhachHang: " + ex.getMessage());
        }

        // Các Tab chỉ dành riêng cho ADMIN: Báo cáo thống kê & Quản lý tài khoản
        if (currentUser.isAdmin()) {
            // Tab 5: Báo cáo thống kê (ADMIN)
            try {
                ViewBaoCaoThongKe vThongKe = new ViewBaoCaoThongKe();
                statisticController = new StatisticController(vThongKe);
                tabbedPane.addTab("Báo cáo thống kê", vThongKe.getContentPane());
            } catch (Exception ex) {
                System.err.println("Không thể nhúng ViewBaoCaoThongKe: " + ex.getMessage());
            }

            // Tab 6: Quản lý tài khoản (ADMIN)
            try {
                ViewQuanLyTaiKhoan vTaiKhoan = new ViewQuanLyTaiKhoan(currentUser);
                userController = new UserController(vTaiKhoan, currentUser);
                tabbedPane.addTab("Quản lý tài khoản", vTaiKhoan.getContentPane());
            } catch (Exception ex) {
                System.err.println("Không thể nhúng ViewQuanLyTaiKhoan: " + ex.getMessage());
            }
        }

        // Khi người dùng chuyển tab thì tự động reload dữ liệu tương ứng
        tabbedPane.addChangeListener(e -> {
            int selected = tabbedPane.getSelectedIndex();
            if (selected == 0) {
                taiDuLieuSoDoPhong();
            } else if (selected >= 0 && selected < tabbedPane.getTabCount()) {
                String title = tabbedPane.getTitleAt(selected);
                if (title != null) {
                    if (title.contains("Quản lý tài khoản") && userController != null) {
                        userController.taiDuLieu();
                    } else if (title.contains("Báo cáo thống kê") && statisticController != null) {
                        statisticController.taiDuLieu();
                    }
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelSoDoPhong() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 1. Thanh công cụ lọc & thống kê
        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        // Bộ lọc bên trái
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        filterPanel.add(new JLabel("Trạng thái:"));
        cboLocTrangThai = new JComboBox<>(new String[] {
                "Tất cả trạng thái",
                "Trống (AVAILABLE)",
                "Có khách (OCCUPIED)",
                "Đang dọn (CLEANING)",
                "Bảo trì (MAINTENANCE)"
        });
        cboLocTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboLocTrangThai.addActionListener(e -> taiDuLieuSoDoPhong());
        filterPanel.add(cboLocTrangThai);

        filterPanel.add(Box.createHorizontalStrut(5));
        filterPanel.add(new JLabel("Tầng:"));
        cboLocTang = new JComboBox<>(new String[] {
                "Tất cả các tầng", "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5"
        });
        cboLocTang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboLocTang.addActionListener(e -> taiDuLieuSoDoPhong());
        filterPanel.add(cboLocTang);

        filterPanel.add(Box.createHorizontalStrut(5));
        filterPanel.add(new JLabel("Tìm số phòng:"));
        txtTimPhong = new JTextField(6);
        txtTimPhong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTimPhong.addActionListener(e -> taiDuLieuSoDoPhong());
        filterPanel.add(txtTimPhong);

        JButton btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTim.addActionListener(e -> taiDuLieuSoDoPhong());
        filterPanel.add(btnTim);

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLamMoi.setBackground(new Color(41, 128, 185));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.addActionListener(e -> {
            txtTimPhong.setText("");
            cboLocTrangThai.setSelectedIndex(0);
            cboLocTang.setSelectedIndex(0);
            taiDuLieuSoDoPhong();
        });
        filterPanel.add(btnLamMoi);

        // Thống kê nhanh bên phải
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        statsPanel.setOpaque(false);

        lblStatsTong = taoBadge("Tổng: 0", new Color(44, 62, 80), new Color(236, 240, 241));
        lblStatsTrong = taoBadge("Trống: 0", new Color(39, 174, 96), new Color(234, 250, 241));
        lblStatsCoKhach = taoBadge("Có khách: 0", new Color(192, 57, 43), new Color(253, 237, 236));
        lblStatsDon = taoBadge("Đang dọn: 0", new Color(211, 84, 0), new Color(254, 249, 231));
        lblStatsBaoTri = taoBadge("Bảo trì: 0", new Color(127, 140, 141), new Color(242, 244, 244));

        statsPanel.add(lblStatsTong);
        statsPanel.add(lblStatsTrong);
        statsPanel.add(lblStatsCoKhach);
        statsPanel.add(lblStatsDon);
        statsPanel.add(lblStatsBaoTri);

        toolbar.add(filterPanel, BorderLayout.WEST);
        toolbar.add(statsPanel, BorderLayout.EAST);
        panel.add(toolbar, BorderLayout.NORTH);

        // 2. Khu vực hiển thị danh sách phòng phân tầng
        panelGridPhong = new JPanel();
        panelGridPhong.setLayout(new BoxLayout(panelGridPhong, BoxLayout.Y_AXIS));
        panelGridPhong.setBackground(new Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(panelGridPhong);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel taoBadge(String text, Color fg, Color bg) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return lbl;
    }

    // Tải và vẽ sơ đồ phòng
    public void taiDuLieuSoDoPhong() {
        panelGridPhong.removeAll();

        ArrayList<rooms> dsPhong = roomService.xuat();

        // 1. Đếm thống kê
        int cntTong = dsPhong.size();
        int cntTrong = 0;
        int cntCoKhach = 0;
        int cntDon = 0;
        int cntBaoTri = 0;

        for (rooms r : dsPhong) {
            String st = (r.getTrangThai() != null) ? r.getTrangThai().toUpperCase() : "AVAILABLE";
            if (st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG"))
                cntTrong++;
            else if (st.contains("OCCUPIED") || st.contains("CÓ KHÁCH") || st.contains("CO KHACH"))
                cntCoKhach++;
            else if (st.contains("CLEANING") || st.contains("ĐANG DỌN") || st.contains("DỌN"))
                cntDon++;
            else if (st.contains("MAINTENANCE") || st.contains("BẢO TRÌ"))
                cntBaoTri++;
        }

        lblStatsTong.setText("Tổng: " + cntTong);
        lblStatsTrong.setText("Trống: " + cntTrong);
        lblStatsCoKhach.setText("Có khách: " + cntCoKhach);
        lblStatsDon.setText("Đang dọn: " + cntDon);
        lblStatsBaoTri.setText("Bảo trì: " + cntBaoTri);

        // 2. Lọc theo trạng thái, tầng và từ khóa tìm kiếm
        String locTrangThai = (String) cboLocTrangThai.getSelectedItem();
        String locTang = (String) cboLocTang.getSelectedItem();
        String tuKhoa = txtTimPhong.getText().trim().toLowerCase();

        // Nhóm phòng theo tầng: Tầng 1, Tầng 2, Tầng 3...
        Map<String, ArrayList<rooms>> nhomTheoTang = new LinkedHashMap<>();

        for (rooms r : dsPhong) {
            // Lọc từ khóa số phòng
            if (!tuKhoa.isEmpty() && !r.getSoPhong().toLowerCase().contains(tuKhoa)) {
                continue;
            }

            // Lọc trạng thái
            String st = (r.getTrangThai() != null) ? r.getTrangThai().toUpperCase() : "AVAILABLE";
            if (locTrangThai.contains("AVAILABLE")
                    && !(st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG")))
                continue;
            if (locTrangThai.contains("OCCUPIED")
                    && !(st.contains("OCCUPIED") || st.contains("CÓ KHÁCH") || st.contains("CO KHACH")))
                continue;
            if (locTrangThai.contains("CLEANING")
                    && !(st.contains("CLEANING") || st.contains("ĐANG DỌN") || st.contains("DỌN")))
                continue;
            if (locTrangThai.contains("MAINTENANCE") && !(st.contains("MAINTENANCE") || st.contains("BẢO TRÌ")))
                continue;

            // Xác định tầng
            String tenTang = xacDinhTang(r);

            // Lọc tầng
            if (!"Tất cả các tầng".equals(locTang) && !tenTang.equalsIgnoreCase(locTang)) {
                continue;
            }

            nhomTheoTang.computeIfAbsent(tenTang, k -> new ArrayList<>()).add(r);
        }

        if (nhomTheoTang.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
            emptyPanel.setOpaque(false);
            JLabel lblEmpty = new JLabel("Không tìm thấy phòng nào phù hợp với bộ lọc.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            emptyPanel.add(lblEmpty);
            panelGridPhong.add(emptyPanel);
        } else {
            // Sắp xếp tầng từ 1 đến N
            for (String tang : nhomTheoTang.keySet()) {
                ArrayList<rooms> phongCuaTang = nhomTheoTang.get(tang);

                // Section Panel của tầng
                JPanel floorSection = new JPanel(new BorderLayout(5, 5));
                floorSection.setOpaque(false);
                floorSection.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

                // Tiêu đề tầng
                int trongCuaTang = 0;
                for (rooms pr : phongCuaTang) {
                    String st = (pr.getTrangThai() != null) ? pr.getTrangThai().toUpperCase() : "AVAILABLE";
                    if (st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG"))
                        trongCuaTang++;
                }

                JPanel floorHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
                floorHeader.setBackground(new Color(235, 245, 251));
                floorHeader.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(41, 128, 185)),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));

                JLabel lblFloorTitle = new JLabel(" " + tang.toUpperCase());
                lblFloorTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblFloorTitle.setForeground(new Color(21, 67, 96));

                JLabel lblFloorSub = new JLabel(
                        "(" + phongCuaTang.size() + " phòng | " + trongCuaTang + " phòng trống)");
                lblFloorSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblFloorSub.setForeground(new Color(86, 101, 115));

                floorHeader.add(lblFloorTitle);
                floorHeader.add(lblFloorSub);
                floorSection.add(floorHeader, BorderLayout.NORTH);

                // Grid các thẻ phòng của tầng
                JPanel roomsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
                roomsPanel.setOpaque(false);

                for (rooms r : phongCuaTang) {
                    JPanel card = taoThePhong(r);
                    roomsPanel.add(card);
                }

                floorSection.add(roomsPanel, BorderLayout.CENTER);
                panelGridPhong.add(floorSection);
            }
        }

        panelGridPhong.revalidate();
        panelGridPhong.repaint();
    }

    private String xacDinhTang(rooms r) {
        if (r.getMoTa() != null && !r.getMoTa().isEmpty()) {
            String moTaLower = r.getMoTa().toLowerCase();
            if (moTaLower.contains("tầng 1") || moTaLower.contains("tang 1"))
                return "Tầng 1";
            if (moTaLower.contains("tầng 2") || moTaLower.contains("tang 2"))
                return "Tầng 2";
            if (moTaLower.contains("tầng 3") || moTaLower.contains("tang 3"))
                return "Tầng 3";
            if (moTaLower.contains("tầng 4") || moTaLower.contains("tang 4"))
                return "Tầng 4";
            if (moTaLower.contains("tầng 5") || moTaLower.contains("tang 5"))
                return "Tầng 5";
        }
        String soPhong = r.getSoPhong();
        if (soPhong != null && !soPhong.isEmpty()) {
            char firstChar = soPhong.charAt(0);
            if (Character.isDigit(firstChar)) {
                return "Tầng " + firstChar;
            }
        }
        return "Tầng 1";
    }

    // Tạo Card phòng tương tác
    private JPanel taoThePhong(rooms r) {
        String st = (r.getTrangThai() != null) ? r.getTrangThai().toUpperCase() : "AVAILABLE";

        Color borderColor;
        Color headerBg;
        Color badgeFg;
        Color badgeBg;
        String badgeText;
        String subActionText;

        ThongTinPhongDangO khachO = null;

        if (st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG")) {
            borderColor = new Color(39, 174, 96);
            headerBg = new Color(234, 250, 241);
            badgeFg = new Color(30, 132, 73);
            badgeBg = new Color(212, 239, 223);
            badgeText = "TRỐNG";
            subActionText = " Bấm để Đặt phòng";
        } else if (st.contains("OCCUPIED") || st.contains("CÓ KHÁCH") || st.contains("CO KHACH")) {
            borderColor = new Color(231, 76, 60);
            headerBg = new Color(253, 237, 236);
            badgeFg = new Color(146, 43, 33);
            badgeBg = new Color(249, 219, 216);
            badgeText = "CÓ KHÁCH";

            // Lấy thông tin khách đang ở
            khachO = roomService.layThongTinKhachDangO(r.getMaPhong());
            String tenKhach = (khachO != null && khachO.getTenKhachHang() != null) ? khachO.getTenKhachHang()
                    : "Khách lưu trú";
            subActionText = " " + tenKhach;
        } else if (st.contains("CLEANING") || st.contains("ĐANG DỌN") || st.contains("DỌN")) {
            borderColor = new Color(243, 156, 18);
            headerBg = new Color(254, 249, 231);
            badgeFg = new Color(185, 119, 14);
            badgeBg = new Color(252, 243, 207);
            badgeText = "ĐANG DỌN";
            subActionText = " Bấm để xong dọn";
        } else { // MAINTENANCE
            borderColor = new Color(127, 140, 141);
            headerBg = new Color(242, 244, 244);
            badgeFg = new Color(86, 101, 115);
            badgeBg = new Color(230, 234, 234);
            badgeText = "BẢO TRÌ";
            subActionText = " Bấm để hoàn tất";
        }

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 130));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Top: Room number + Badge
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(headerBg);
        pTop.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JLabel lblRoomNum = new JLabel("P." + r.getSoPhong());
        lblRoomNum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRoomNum.setForeground(new Color(24, 44, 97));

        JLabel lblBadge = new JLabel(badgeText);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setForeground(badgeFg);
        lblBadge.setBackground(badgeBg);
        lblBadge.setOpaque(true);
        lblBadge.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        pTop.add(lblRoomNum, BorderLayout.WEST);
        pTop.add(lblBadge, BorderLayout.EAST);

        // Center: Type + Price
        JPanel pMid = new JPanel();
        pMid.setLayout(new BoxLayout(pMid, BoxLayout.Y_AXIS));
        pMid.setOpaque(false);
        pMid.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        String loaiPhong = (r.getTenLoaiPhong() != null && !r.getTenLoaiPhong().isEmpty()) ? r.getTenLoaiPhong()
                : "Phòng tiêu chuẩn";
        JLabel lblType = new JLabel(loaiPhong);
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblType.setForeground(new Color(80, 80, 80));

        JLabel lblPrice = new JLabel(currencyFormat.format(r.getGiaMoiDem()) + "/đêm");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrice.setForeground(new Color(41, 128, 185));

        pMid.add(lblType);
        pMid.add(Box.createVerticalStrut(2));
        pMid.add(lblPrice);

        // Bottom: Action guide or guest name
        JPanel pBot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        pBot.setOpaque(false);
        JLabel lblSub = new JLabel(subActionText);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        if (badgeText.equals("CÓ KHÁCH")) {
            lblSub.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblSub.setForeground(new Color(192, 57, 43));
        } else {
            lblSub.setForeground(new Color(100, 100, 100));
        }
        pBot.add(lblSub);

        card.add(pTop, BorderLayout.NORTH);
        card.add(pMid, BorderLayout.CENTER);
        card.add(pBot, BorderLayout.SOUTH);

        // Click listener
        final ThongTinPhongDangO guestInfo = khachO;
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xuLyClickThePhong(r, guestInfo);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 252, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    // Xử lý sự kiện click vào thẻ phòng theo đúng yêu cầu nghiệp vụ
    private void xuLyClickThePhong(rooms r, ThongTinPhongDangO guestInfo) {
        String st = (r.getTrangThai() != null) ? r.getTrangThai().toUpperCase() : "AVAILABLE";

        if (st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG")) {
            // Mở hộp thoại ĐẶT PHÒNG NHANH
            moDialogDatPhongNhanh(r);
        } else if (st.contains("OCCUPIED") || st.contains("CÓ KHÁCH") || st.contains("CO KHACH")) {
            // Mở hộp thoại TRẢ PHÒNG & HÓA ĐƠN -> Chuyển sang CLEANING
            moDialogTraPhongHoaDon(r, guestInfo);
        } else if (st.contains("CLEANING") || st.contains("ĐANG DỌN") || st.contains("DỌN")) {
            // Hộp thoại hoàn tất dọn phòng
            xacNhanDonPhong(r);
        } else {
            // Hộp thoại hoàn tất bảo trì
            xacNhanBaoTri(r);
        }
    }

    // =========================================================================
    // 1. DIALOG ĐẶT PHÒNG NHANH (Khi phòng TRỐNG)
    // =========================================================================
    private void moDialogDatPhongNhanh(rooms r) {
        JDialog dlg = new JDialog(this, "Đặt phòng / Check-in - Phòng " + r.getSoPhong(), true);
        dlg.setSize(520, 560);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        // Header
        JPanel pHead = new JPanel(new GridLayout(2, 1, 4, 4));
        pHead.setBackground(new Color(234, 250, 241));
        pHead.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel lblTitle = new JLabel("ĐẶT PHÒNG NHANH: PHÒNG " + r.getSoPhong());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 132, 73));

        String loai = (r.getTenLoaiPhong() != null) ? r.getTenLoaiPhong() : "Phòng tiêu chuẩn";
        JLabel lblDesc = new JLabel(
                "Loại: " + loai + "  |  Đơn giá: " + currencyFormat.format(r.getGiaMoiDem()) + "/đêm");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pHead.add(lblTitle);
        pHead.add(lblDesc);

        // Form fields
        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField txtCCCD = new JTextField(12);
        JButton btnKiemTraCCCD = new JButton("Tìm khách");
        JTextField txtHoTen = new JTextField(15);
        JTextField txtSDT = new JTextField(15);
        JTextField txtDiaChi = new JTextField(15);

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        JTextField txtNgayNhan = new JTextField(today.toString(), 10);
        JTextField txtNgayTra = new JTextField(tomorrow.toString(), 10);
        JTextField txtGhiChu = new JTextField("Khách đặt trực tiếp", 15);
        int sucChua = Math.max(1, r.getSucChua());
        JSpinner spnSoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, sucChua, 1));

        JLabel lblTamTinh = new JLabel(currencyFormat.format(r.getGiaMoiDem()) + " (1 đêm)");
        lblTamTinh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTamTinh.setForeground(new Color(41, 128, 185));

        // Hàng 0: CCCD + Nút tìm
        g.gridx = 0;
        g.gridy = 0;
        pForm.add(new JLabel("CCCD khách hàng:*"), g);
        JPanel pCCCD = new JPanel(new BorderLayout(5, 0));
        pCCCD.add(txtCCCD, BorderLayout.CENTER);
        pCCCD.add(btnKiemTraCCCD, BorderLayout.EAST);
        g.gridx = 1;
        g.gridy = 0;
        pForm.add(pCCCD, g);

        // Hàng 1: Họ tên
        g.gridx = 0;
        g.gridy = 1;
        pForm.add(new JLabel("Họ tên khách:*"), g);
        g.gridx = 1;
        g.gridy = 1;
        pForm.add(txtHoTen, g);

        // Hàng 2: SĐT
        g.gridx = 0;
        g.gridy = 2;
        pForm.add(new JLabel("Số điện thoại:*"), g);
        g.gridx = 1;
        g.gridy = 2;
        pForm.add(txtSDT, g);

        // Hàng 3: Địa chỉ
        g.gridx = 0;
        g.gridy = 3;
        pForm.add(new JLabel("Địa chỉ:"), g);
        g.gridx = 1;
        g.gridy = 3;
        pForm.add(txtDiaChi, g);

        // Hàng 4: Ngày nhận
        g.gridx = 0;
        g.gridy = 4;
        pForm.add(new JLabel("Ngày nhận (YYYY-MM-DD):"), g);
        g.gridx = 1;
        g.gridy = 4;
        pForm.add(txtNgayNhan, g);

        // Hàng 5: Ngày trả
        g.gridx = 0;
        g.gridy = 5;
        pForm.add(new JLabel("Ngày trả (YYYY-MM-DD):"), g);
        g.gridx = 1;
        g.gridy = 5;
        pForm.add(txtNgayTra, g);

        // Hàng 6: Tạm tính
        g.gridx = 0;
        g.gridy = 6;
        pForm.add(new JLabel("Tạm tính tiền phòng:"), g);
        g.gridx = 1;
        g.gridy = 6;
        pForm.add(lblTamTinh, g);

        // Hàng 7: Số khách
        g.gridx = 0;
        g.gridy = 7;
        pForm.add(new JLabel("Số khách (tối đa " + sucChua + "):"), g);
        g.gridx = 1;
        g.gridy = 7;
        pForm.add(spnSoNguoi, g);

        // Hàng 8: Ghi chú
        g.gridx = 0;
        g.gridy = 8;
        pForm.add(new JLabel("Ghi chú:"), g);
        g.gridx = 1;
        g.gridy = 8;
        pForm.add(txtGhiChu, g);

        // Tính lại số tiền khi đổi ngày
        Runnable tinhTien = () -> {
            try {
                Date d1 = Date.valueOf(txtNgayNhan.getText().trim());
                Date d2 = Date.valueOf(txtNgayTra.getText().trim());
                long dem = ChronoUnit.DAYS.between(d1.toLocalDate(), d2.toLocalDate());
                if (dem <= 0)
                    dem = 1;
                lblTamTinh.setText(currencyFormat.format(dem * r.getGiaMoiDem()) + " (" + dem + " đêm)");
            } catch (Exception ignored) {
            }
        };
        txtNgayNhan.addActionListener(ev -> tinhTien.run());
        txtNgayTra.addActionListener(ev -> tinhTien.run());

        // Tìm khách theo CCCD
        btnKiemTraCCCD.addActionListener(ev -> {
            String cccd = txtCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập số CCCD cần tìm!");
                return;
            }
            customers tk = new customers();
            tk.setSoCCCD(cccd);
            customers timThay = customerService.timKiem(tk);
            if (timThay != null) {
                txtHoTen.setText(timThay.getNameKhachHang());
                txtSDT.setText(timThay.getSoDienThoai());
                txtDiaChi.setText(timThay.getDiaChi());
                JOptionPane.showMessageDialog(dlg, "Tìm thấy khách quen: " + timThay.getNameKhachHang());
            } else {
                JOptionPane.showMessageDialog(dlg,
                        "Chưa có thông tin khách này. Vui lòng nhập họ tên và SĐT để tạo mới.");
                txtHoTen.requestFocus();
            }
        });

        // Nút hành động
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        JButton btnCheckIn = new JButton(" Nhận phòng (Check-in ngay)");
        btnCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCheckIn.setBackground(new Color(39, 174, 96));
        btnCheckIn.setForeground(Color.WHITE);

        JButton btnHuy = new JButton("Hủy");
        pBtns.add(btnCheckIn);
        pBtns.add(btnHuy);

        btnHuy.addActionListener(ev -> dlg.dispose());

        btnCheckIn.addActionListener(ev -> {
            String cccd = txtCCCD.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();

            if (cccd.isEmpty() || hoTen.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đủ CCCD, Họ tên và Số điện thoại khách hàng!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date dNhan;
            Date dTra;
            try {
                dNhan = Date.valueOf(txtNgayNhan.getText().trim());
                dTra = Date.valueOf(txtNgayTra.getText().trim());
                if (!dTra.after(dNhan)) {
                    JOptionPane.showMessageDialog(dlg, "Ngày trả phòng phải sau ngày nhận phòng!", "Lỗi ngày tháng",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Định dạng ngày không hợp lệ (YYYY-MM-DD)!", "Lỗi ngày tháng",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tìm hoặc thêm khách hàng
            customers tk = new customers();
            tk.setSoCCCD(cccd);
            customers kh = customerService.timKiem(tk);
            int customerId;
            if (kh == null) {
                customers moi = new customers(0, hoTen, cccd, sdt, "", txtDiaChi.getText().trim());
                customerService.them(moi);
                customers khMoi = customerService.timKiem(tk);
                if (khMoi != null) {
                    customerId = khMoi.getIdKhachHang();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Không thể lưu thông tin khách hàng!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                customerId = kh.getIdKhachHang();
            }

            // Tiến hành đặt phòng & check-in
            int bookingId = bookingService.datPhongNhanh(customerId, dNhan, dTra, "CHECKED_IN",
                    txtGhiChu.getText().trim(), r.getMaPhong(), r.getGiaMoiDem(),
                    (int) spnSoNguoi.getValue());

            if (bookingId > 0) {
                JOptionPane.showMessageDialog(this,
                        "Nhận phòng thành công cho khách: " + hoTen + "\nPhòng " + r.getSoPhong()
                                + " đã chuyển sang trạng thái CÓ KHÁCH (OCCUPIED).",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
                taiDuLieuSoDoPhong();
            } else {
                JOptionPane.showMessageDialog(dlg, "Đặt phòng thất bại. Vui lòng thử lại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dlg.add(pHead, BorderLayout.NORTH);
        dlg.add(pForm, BorderLayout.CENTER);
        dlg.add(pBtns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // =========================================================================
    // 2. DIALOG TRẢ PHÒNG & XUẤT HÓA ĐƠN -> CHUYỂN SANG CLEANING (Khi phòng CÓ KHÁCH)
    // =========================================================================
    private void moDialogTraPhongHoaDon(rooms r, ThongTinPhongDangO info) {
        bookings b = new bookings();
        b.setIdDatPhong(info != null ? info.getBookingId() : 0);
        b.setMaPhong(r.getMaPhong());
        b.setSoPhong(r.getSoPhong());
        b.setGiaMoiDem((info != null && info.getGiaMoiDem() > 0) ? info.getGiaMoiDem() : r.getGiaMoiDem());
        b.setTenKhachHang((info != null && info.getTenKhachHang() != null) ? info.getTenKhachHang() : "Khách lưu trú");
        b.setSoDienThoai((info != null && info.getSoDienThoai() != null) ? info.getSoDienThoai() : "");
        b.setCccd((info != null && info.getCccd() != null) ? info.getCccd() : "");
        b.setNgayNhanPhong((info != null && info.getNgayNhan() != null) ? info.getNgayNhan() : Date.valueOf(LocalDate.now()));
        b.setTenLoaiPhong(r.getTenLoaiPhong() != null ? r.getTenLoaiPhong() : "Tiêu chuẩn");

        DialogTraPhongThanhToan dlg = new DialogTraPhongThanhToan(this, b);
        dlg.setVisible(true);

        if (dlg.isCompleted()) {
            taiDuLieuSoDoPhong();
        }
    }

    // =========================================================================
    // 3. XÁC NHẬN HOÀN TẤT DỌN PHÒNG (CLEANING -> AVAILABLE)
    // =========================================================================
    private void xacNhanDonPhong(rooms r) {
        Object[] options = { " Đã dọn xong (Sẵn sàng đón khách)", " Chuyển sang bảo trì", "Đóng" };
        int chon = JOptionPane.showOptionDialog(
                this,
                "Phòng " + r.getSoPhong() + " hiện đang ở trạng thái ĐANG DỌN DẸP (CLEANING).\n"
                        + "Bạn muốn thao tác gì cho phòng này?",
                "Quản lý dọn phòng - Phòng " + r.getSoPhong(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (chon == 0) { // Đã dọn xong -> AVAILABLE
            roomService.doiTrangThai(r.getMaPhong(), "AVAILABLE");
            JOptionPane.showMessageDialog(this, "Phòng " + r.getSoPhong()
                    + " đã được chuyển sang trạng thái TRỐNG (AVAILABLE) và sẵn sàng đón khách!");
            taiDuLieuSoDoPhong();
        } else if (chon == 1) { // Bảo trì
            roomService.doiTrangThai(r.getMaPhong(), "MAINTENANCE");
            JOptionPane.showMessageDialog(this, "Phòng " + r.getSoPhong() + " đã chuyển sang BẢO TRÌ (MAINTENANCE).");
            taiDuLieuSoDoPhong();
        }
    }

    // =========================================================================
    // 4. XÁC NHẬN HOÀN TẤT BẢO TRÌ (MAINTENANCE -> AVAILABLE)
    // =========================================================================
    private void xacNhanBaoTri(rooms r) {
        Object[] options = { " Hoàn tất sửa chữa (Chuyển sang TRỐNG)", " Chuyển sang dọn dẹp", "Đóng" };
        int chon = JOptionPane.showOptionDialog(
                this,
                "Phòng " + r.getSoPhong() + " hiện đang BẢO TRÌ (MAINTENANCE).\n"
                        + "Bạn muốn thao tác gì cho phòng này?",
                "Quản lý bảo trì - Phòng " + r.getSoPhong(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (chon == 0) { // AVAILABLE
            roomService.doiTrangThai(r.getMaPhong(), "AVAILABLE");
            JOptionPane.showMessageDialog(this,
                    "Phòng " + r.getSoPhong() + " đã chuyển sang trạng thái TRỐNG (AVAILABLE)!");
            taiDuLieuSoDoPhong();
        } else if (chon == 1) { // CLEANING
            roomService.doiTrangThai(r.getMaPhong(), "CLEANING");
            JOptionPane.showMessageDialog(this, "Phòng " + r.getSoPhong() + " đã chuyển sang ĐANG DỌN (CLEANING).");
            taiDuLieuSoDoPhong();
        }
    }

    private void capNhatNutUserMenu() {
        if (btnUserMenu != null && currentUser != null) {
            String roleBadge = currentUser.isAdmin() ? "ADMIN" : "STAFF";
            btnUserMenu.setText("  " + currentUser.getHoTen() + " (" + roleBadge + ")  ");
        }
    }

    private void hienThiUserDropdownMenu(Component invoker) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Header thông tin
        String roleStr = currentUser.isAdmin() ? "Quản trị viên (ADMIN)" : "Nhân viên (STAFF)";
        JMenuItem mnuHeader = new JMenuItem("<html><b>" + currentUser.getHoTen() + "</b><br><font color='#666666'>@"
                + currentUser.getTenDangNhap() + " • " + roleStr + "</font></html>");
        mnuHeader.setEnabled(false);
        mnuHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        popup.add(mnuHeader);
        popup.addSeparator();

        // 1. Xem thông tin tài khoản
        JMenuItem mnuThongTin = new JMenuItem("  Xem thông tin tài khoản");
        mnuThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mnuThongTin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mnuThongTin.addActionListener(e -> hienThiThongTinTaiKhoan());
        popup.add(mnuThongTin);

        // 2. Quản lý tài khoản (cho ADMIN)
        if (currentUser.isAdmin()) {
            JMenuItem mnuQuanLyTaiKhoan = new JMenuItem(" Quản lý tài khoản (Admin)");
            mnuQuanLyTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            mnuQuanLyTaiKhoan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            mnuQuanLyTaiKhoan.addActionListener(e -> moTabQuanLyTaiKhoan());
            popup.add(mnuQuanLyTaiKhoan);
        }

        popup.addSeparator();

        // 3. Đăng xuất
        JMenuItem mnuDangXuat = new JMenuItem("Đăng xuất");
        mnuDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mnuDangXuat.setForeground(new Color(192, 57, 43));
        mnuDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mnuDangXuat.addActionListener(e -> dangXuat());
        popup.add(mnuDangXuat);

        popup.show(invoker, 0, invoker.getHeight() + 4);
    }

    private void hienThiThongTinTaiKhoan() {
        DialogThongTinTaiKhoan dialog = new DialogThongTinTaiKhoan(this, currentUser, () -> {
            capNhatNutUserMenu();
            if (userController != null) {
                userController.taiDuLieu();
            }
        });
        dialog.setVisible(true);
    }

    private void moTabQuanLyTaiKhoan() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).contains("Quản lý tài khoản")) {
                tabbedPane.setSelectedIndex(i);
                if (userController != null) {
                    userController.taiDuLieu();
                }
                return;
            }
        }
        // Neu chua co tab thi tao moi
        try {
            ViewQuanLyTaiKhoan vTaiKhoan = new ViewQuanLyTaiKhoan(currentUser);
            userController = new UserController(vTaiKhoan, currentUser);
            vTaiKhoan.btnQuayLai.setText("Về sơ đồ phòng");
            vTaiKhoan.btnQuayLai.addActionListener(e -> {
                tabbedPane.setSelectedIndex(0);
                taiDuLieuSoDoPhong();
            });
            tabbedPane.addTab(" Quản lý tài khoản", vTaiKhoan.getContentPane());
            tabbedPane.setSelectedComponent(vTaiKhoan.getContentPane());
        } catch (Exception ex) {
            ex.printStackTrace();
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
            for (Window window : Window.getWindows()) {
                window.dispose();
            }

            // Khởi tạo và hiển thị form đăng nhập duy nhất
            ViewDangNhap v = new ViewDangNhap();
            new LoginController(v);
            v.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewTrangChu().setVisible(true));
    }
}
