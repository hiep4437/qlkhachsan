package view.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import controller.DatPhongController;
import controller.HoaDonController;
import controller.KhachHangController;
import controller.LoginController;
import controller.NguoiDungController;
import controller.PhongController;
import controller.StatisticController;
import model.NguoiDung;
import view.dialog.DialogThongTinTaiKhoan;

public class ViewTrangChu extends JFrame {

    private NguoiDung nguoiDungHienTai;
    private PanelSoDoPhong panelSoDoPhong;
    private NguoiDungController boDieuKhienNguoiDung;
    private DatPhongController boDieuKhienDatPhong;
    private StatisticController statisticBoDieuKhien;

    public ViewTrangChu() {
        this(new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true));
    }

    public ViewTrangChu(NguoiDung nguoiDung) {
        this.nguoiDungHienTai = (nguoiDung != null) ? nguoiDung : new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        initComponents();
        view.AppIcon.apply(this);

        khungDauTrang.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btNguoiDungMenu.setIconTextGap(8);
        btNguoiDungMenu.setForeground(Color.WHITE);
        btNguoiDungMenu.setBackground(new Color(36, 64, 132));
        btNguoiDungMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btNguoiDungMenu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 110, 190), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btNguoiDungMenu.addActionListener(ac -> hienThiUserDropdownMenu(btNguoiDungMenu));
        capNhatNutUserMenu();

        btDangXuat.setIconTextGap(8);
        btDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btDangXuat.addActionListener(ac -> dangXuat());

        GraphicsEnvironment thamSoMoiTruong = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setMaximizedBounds(thamSoMoiTruong.getMaximumWindowBounds());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        initTabs();
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

    public void capNhatSoDoPhong() {
        if (panelSoDoPhong != null) {
            panelSoDoPhong.taiDuLieuSoDoPhong();
        }
    }

    private void initTabs() {
        // Tab 0: Sơ đồ phòng (Màn hình chính)
        tabSoDoPhong.setLayout(new BorderLayout());
        panelSoDoPhong = new PanelSoDoPhong(this);
        tabSoDoPhong.add(panelSoDoPhong, BorderLayout.CENTER);

        // Tab 1: Quản lý phòng
        try {
            ViewQuanLyPhong viewPhong = new ViewQuanLyPhong(nguoiDungHienTai.isAdmin());
            new PhongController(viewPhong);
            tabQuanLyPhong.setLayout(new BorderLayout());
            tabQuanLyPhong.add(viewPhong.getContentPane(), BorderLayout.CENTER);
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewQuanLyPhong: " + ngoaiLe.getMessage());
        }

        // Tab 2: Quản lý đặt phòng
        try {
            ViewDatPhong viewDatPhong = new ViewDatPhong();
            boDieuKhienDatPhong = new DatPhongController(viewDatPhong);
            tabQuanLyDatPhong.setLayout(new BorderLayout());
            tabQuanLyDatPhong.add(viewDatPhong.getContentPane(), BorderLayout.CENTER);
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewDatPhong: " + ngoaiLe.getMessage());
        }

        // Tab 3: Quản lý hóa đơn
        try {
            ViewHoaDon viewHoaDon = new ViewHoaDon();
            new HoaDonController(viewHoaDon);
            tabQuanLyHoaDon.setLayout(new BorderLayout());
            tabQuanLyHoaDon.add(viewHoaDon.getContentPane(), BorderLayout.CENTER);
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewHoaDon: " + ngoaiLe.getMessage());
        }

        // Tab 4: Quản lý khách hàng
        try {
            ViewQuanLyKhachHang viewKhachHang = new ViewQuanLyKhachHang();
            new KhachHangController(viewKhachHang, this::chuyenSangTabDatPhongVaTimKiem);
            tabQuanLyKhachHang.setLayout(new BorderLayout());
            tabQuanLyKhachHang.add(viewKhachHang.getContentPane(), BorderLayout.CENTER);
        } catch (Exception ngoaiLe) {
            System.err.println("Không thể nhúng ViewQuanLyKhachHang: " + ngoaiLe.getMessage());
        }

        // Các Tab chỉ dành riêng cho ADMIN: Báo cáo thống kê & Quản lý tài khoản
        if (nguoiDungHienTai.isAdmin()) {
            // Tab 5: Báo cáo thống kê (ADMIN)
            try {
                ViewBaoCaoThongKe viewThongKe = new ViewBaoCaoThongKe();
                statisticBoDieuKhien = new StatisticController(viewThongKe);
                tabBaoCaoThongKe.setLayout(new BorderLayout());
                tabBaoCaoThongKe.add(viewThongKe.getContentPane(), BorderLayout.CENTER);
            } catch (Exception ngoaiLe) {
                System.err.println("Không thể nhúng ViewBaoCaoThongKe: " + ngoaiLe.getMessage());
            }

            // Tab 6: Quản lý tài khoản (ADMIN)
            try {
                ViewQuanLyTaiKhoan viewTaiKhoan = new ViewQuanLyTaiKhoan(nguoiDungHienTai);
                boDieuKhienNguoiDung = new NguoiDungController(viewTaiKhoan, nguoiDungHienTai);
                tabQuanLyTaiKhoan.setLayout(new BorderLayout());
                tabQuanLyTaiKhoan.add(viewTaiKhoan.getContentPane(), BorderLayout.CENTER);
            } catch (Exception ngoaiLe) {
                System.err.println("Không thể nhúng ViewQuanLyTaiKhoan: " + ngoaiLe.getMessage());
            }
        } else {
            // Ẩn 2 tab admin nếu là nhân viên
            khungTab.remove(tabBaoCaoThongKe);
            khungTab.remove(tabQuanLyTaiKhoan);
        }

        // Khi người dùng chuyển tab thì tự động reload dữ liệu tương ứng
        khungTab.addChangeListener(ac -> {
            int duocChon = khungTab.getSelectedIndex();
            if (duocChon == 0) {
                capNhatSoDoPhong();
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
    }

    private void capNhatNutUserMenu() {
        if (btNguoiDungMenu != null && nguoiDungHienTai != null) {
            String huyHieuVaiTro = nguoiDungHienTai.isAdmin() ? "Quản trị" : "Nhân viên";
            btNguoiDungMenu.setText(huyHieuVaiTro);
//            btNguoiDungMenu.setText("  " + nguoiDungHienTai.getHoTen() + " (" + huyHieuVaiTro + ")  ");
        }
    }

    private void hienThiUserDropdownMenu(Component benGoi) {
        JPopupMenu menuBatLen = new JPopupMenu();
        menuBatLen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Header thông tin rõ nét, không làm mờ
        String chuoiVaiTro = nguoiDungHienTai.isAdmin() ? "Quản trị" : "Nhân viên";
        JPanel panelHeader = new JPanel(new BorderLayout(0, 3));
        panelHeader.setOpaque(true);
        panelHeader.setBackground(new Color(248, 250, 252));
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        javax.swing.JLabel nhanTenHeader = new javax.swing.JLabel(nguoiDungHienTai.getHoTen());
        nhanTenHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nhanTenHeader.setForeground(new Color(30, 41, 59));

        javax.swing.JLabel nhanPhuHeader = new javax.swing.JLabel("@" + nguoiDungHienTai.getTenDangNhap() + " • " + chuoiVaiTro);
        nhanPhuHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhuHeader.setForeground(new Color(71, 85, 105));

        panelHeader.add(nhanTenHeader, BorderLayout.NORTH);
        panelHeader.add(nhanPhuHeader, BorderLayout.SOUTH);

        menuBatLen.add(panelHeader);
        menuBatLen.addSeparator();

        // 1. Xem thông tin tài khoản
        JMenuItem menuThongTin = new JMenuItem("  Xem thông tin tài khoản");
        menuThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuThongTin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuThongTin.addActionListener(ac -> hienThiThongTinTaiKhoan());
        menuBatLen.add(menuThongTin);

        // 2. Quản lý tài khoản (cho ADMIN)
        if (nguoiDungHienTai.isAdmin()) {
            JMenuItem menuQuanLyTaiKhoan = new JMenuItem("  Quản lý tài khoản");
            menuQuanLyTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            menuQuanLyTaiKhoan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuQuanLyTaiKhoan.addActionListener(ac -> moTabQuanLyTaiKhoan());
            menuBatLen.add(menuQuanLyTaiKhoan);
        }

        menuBatLen.addSeparator();

        // 3. Đăng xuất
        JMenuItem menuDangXuat = new JMenuItem("Đăng xuất");
        menuDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menuDangXuat.setForeground(new Color(192, 57, 43));
        menuDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuDangXuat.addActionListener(ac -> dangXuat());
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
        try {
            ViewQuanLyTaiKhoan viewTaiKhoan = new ViewQuanLyTaiKhoan(nguoiDungHienTai);
            boDieuKhienNguoiDung = new NguoiDungController(viewTaiKhoan, nguoiDungHienTai);
            viewTaiKhoan.btQuayLai.setText("Về sơ đồ phòng");
            viewTaiKhoan.btQuayLai.addActionListener(ac -> {
                khungTab.setSelectedIndex(0);
                capNhatSoDoPhong();
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
            for (Window cuaSo : Window.getWindows()) {
                cuaSo.dispose();
            }

            ViewDangNhap giaTriHienTai = new ViewDangNhap();
            new LoginController(giaTriHienTai);
            giaTriHienTai.setVisible(true);
        }
    }

    public static void main(String[] thamSo) {
        SwingUtilities.invokeLater(() -> new ViewTrangChu().setVisible(true));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        khungDauTrang = new javax.swing.JPanel();
        nhanTieuDeUng = new javax.swing.JLabel();
        khungNguoiDung = new javax.swing.JPanel();
        btNguoiDungMenu = new javax.swing.JButton();
        btDangXuat = new javax.swing.JButton();
        khungTab = new javax.swing.JTabbedPane();
        tabSoDoPhong = new javax.swing.JPanel();
        tabQuanLyPhong = new javax.swing.JPanel();
        tabQuanLyDatPhong = new javax.swing.JPanel();
        tabQuanLyHoaDon = new javax.swing.JPanel();
        tabQuanLyKhachHang = new javax.swing.JPanel();
        tabBaoCaoThongKe = new javax.swing.JPanel();
        tabQuanLyTaiKhoan = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hệ thống quản lý khách sạn");
        setMinimumSize(new java.awt.Dimension(1200, 700));

        khungDauTrang.setBackground(new java.awt.Color(24, 44, 97));

        nhanTieuDeUng.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        nhanTieuDeUng.setForeground(new java.awt.Color(255, 255, 255));
        nhanTieuDeUng.setText("QUẢN LÝ KHÁCH SẠN");

        khungNguoiDung.setOpaque(false);

        btNguoiDungMenu.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btNguoiDungMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user.png"))); // NOI18N
        btNguoiDungMenu.setText("Người dùng");
        btNguoiDungMenu.setFocusPainted(false);

        btDangXuat.setBackground(new java.awt.Color(192, 57, 43));
        btDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        btDangXuat.setText("Đăng xuất");
        btDangXuat.setFocusPainted(false);

        javax.swing.GroupLayout khungNguoiDungLayout = new javax.swing.GroupLayout(khungNguoiDung);
        khungNguoiDung.setLayout(khungNguoiDungLayout);
        khungNguoiDungLayout.setHorizontalGroup(
            khungNguoiDungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungNguoiDungLayout.createSequentialGroup()
                .addComponent(btNguoiDungMenu)
                .addGap(12, 12, 12)
                .addComponent(btDangXuat))
        );
        khungNguoiDungLayout.setVerticalGroup(
            khungNguoiDungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(btNguoiDungMenu)
            .addComponent(btDangXuat)
        );

        javax.swing.GroupLayout khungDauTrangLayout = new javax.swing.GroupLayout(khungDauTrang);
        khungDauTrang.setLayout(khungDauTrangLayout);
        khungDauTrangLayout.setHorizontalGroup(
            khungDauTrangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungDauTrangLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(nhanTieuDeUng)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(khungNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        khungDauTrangLayout.setVerticalGroup(
            khungDauTrangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungDauTrangLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(khungDauTrangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanTieuDeUng)
                    .addComponent(khungNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        khungTab.setBackground(new java.awt.Color(255, 255, 255));
        khungTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        khungTab.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N

        javax.swing.GroupLayout tabSoDoPhongLayout = new javax.swing.GroupLayout(tabSoDoPhong);
        tabSoDoPhong.setLayout(tabSoDoPhongLayout);
        tabSoDoPhongLayout.setHorizontalGroup(
            tabSoDoPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabSoDoPhongLayout.setVerticalGroup(
            tabSoDoPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Sơ đồ phòng", tabSoDoPhong);

        javax.swing.GroupLayout tabQuanLyPhongLayout = new javax.swing.GroupLayout(tabQuanLyPhong);
        tabQuanLyPhong.setLayout(tabQuanLyPhongLayout);
        tabQuanLyPhongLayout.setHorizontalGroup(
            tabQuanLyPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabQuanLyPhongLayout.setVerticalGroup(
            tabQuanLyPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Quản lý phòng", tabQuanLyPhong);

        javax.swing.GroupLayout tabQuanLyDatPhongLayout = new javax.swing.GroupLayout(tabQuanLyDatPhong);
        tabQuanLyDatPhong.setLayout(tabQuanLyDatPhongLayout);
        tabQuanLyDatPhongLayout.setHorizontalGroup(
            tabQuanLyDatPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabQuanLyDatPhongLayout.setVerticalGroup(
            tabQuanLyDatPhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Quản lý đặt phòng", tabQuanLyDatPhong);

        javax.swing.GroupLayout tabQuanLyHoaDonLayout = new javax.swing.GroupLayout(tabQuanLyHoaDon);
        tabQuanLyHoaDon.setLayout(tabQuanLyHoaDonLayout);
        tabQuanLyHoaDonLayout.setHorizontalGroup(
            tabQuanLyHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabQuanLyHoaDonLayout.setVerticalGroup(
            tabQuanLyHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Quản lý hóa đơn", tabQuanLyHoaDon);

        javax.swing.GroupLayout tabQuanLyKhachHangLayout = new javax.swing.GroupLayout(tabQuanLyKhachHang);
        tabQuanLyKhachHang.setLayout(tabQuanLyKhachHangLayout);
        tabQuanLyKhachHangLayout.setHorizontalGroup(
            tabQuanLyKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabQuanLyKhachHangLayout.setVerticalGroup(
            tabQuanLyKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Quản lý khách hàng", tabQuanLyKhachHang);

        javax.swing.GroupLayout tabBaoCaoThongKeLayout = new javax.swing.GroupLayout(tabBaoCaoThongKe);
        tabBaoCaoThongKe.setLayout(tabBaoCaoThongKeLayout);
        tabBaoCaoThongKeLayout.setHorizontalGroup(
            tabBaoCaoThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabBaoCaoThongKeLayout.setVerticalGroup(
            tabBaoCaoThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Báo cáo thống kê", tabBaoCaoThongKe);

        javax.swing.GroupLayout tabQuanLyTaiKhoanLayout = new javax.swing.GroupLayout(tabQuanLyTaiKhoan);
        tabQuanLyTaiKhoan.setLayout(tabQuanLyTaiKhoanLayout);
        tabQuanLyTaiKhoanLayout.setHorizontalGroup(
            tabQuanLyTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tabQuanLyTaiKhoanLayout.setVerticalGroup(
            tabQuanLyTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        khungTab.addTab("Quản lý tài khoản", tabQuanLyTaiKhoan);

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungDauTrang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(khungTab)
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addComponent(khungDauTrang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(khungTab))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDangXuat;
    private javax.swing.JButton btNguoiDungMenu;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungDauTrang;
    private javax.swing.JPanel khungNguoiDung;
    private javax.swing.JTabbedPane khungTab;
    private javax.swing.JLabel nhanTieuDeUng;
    private javax.swing.JPanel tabBaoCaoThongKe;
    private javax.swing.JPanel tabQuanLyDatPhong;
    private javax.swing.JPanel tabQuanLyHoaDon;
    private javax.swing.JPanel tabQuanLyKhachHang;
    private javax.swing.JPanel tabQuanLyPhong;
    private javax.swing.JPanel tabQuanLyTaiKhoan;
    private javax.swing.JPanel tabSoDoPhong;
    // End of variables declaration//GEN-END:variables
}
