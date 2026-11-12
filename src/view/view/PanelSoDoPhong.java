package view.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.DatPhong;
import model.Phong;
import model.ThongTinPhongDangO;
import service.DatPhongService;
import service.PhongService;
import view.dialog.DialogCapNhatTrangThaiPhong;
import view.dialog.DialogDatPhongNhanh;
import view.dialog.DialogTraPhongThanhToan;

public class PanelSoDoPhong extends javax.swing.JPanel {

    private final Window parentWindow;
    private final PhongService dichVuPhong = new PhongService();
    private final DatPhongService dichVuDatPhong = new DatPhongService();
    private final DecimalFormat dinhDangTienTe = new DecimalFormat("#,### đ");
    private boolean dangMoDialog = false;

    public PanelSoDoPhong() {
        this(null);
    }

    public PanelSoDoPhong(Window parentWindow) {
        this.parentWindow = parentWindow;
        initComponents();
        khoiTaoGiaoDien();
        if (!Beans.isDesignTime()) {
            try {
                taiDuLieuSoDoPhong();
            } catch (Exception ignored) {
            }
        }
    }

    private void khoiTaoGiaoDien() {
        // Badges styling
        dinhDangBadge(nhanStatsChung, new Color(44, 62, 80), new Color(236, 240, 241));
        dinhDangBadge(nhanStatsTrong, new Color(39, 174, 96), new Color(234, 250, 241));
        dinhDangBadge(nhanStatsCoKhach, new Color(192, 57, 43), new Color(253, 237, 236));
        dinhDangBadge(nhanStatsDon, new Color(211, 84, 0), new Color(254, 249, 231));
        dinhDangBadge(nhanStatsBaoTri, new Color(127, 140, 141), new Color(242, 244, 244));

        cuonPane.getVerticalScrollBar().setUnitIncrement(16);

        // Actions
        hopChonLocTrangThai.addActionListener(e -> taiDuLieuSoDoPhong());
        hopChonLocTang.addActionListener(e -> taiDuLieuSoDoPhong());
        oNhapTimPhong.addActionListener(e -> taiDuLieuSoDoPhong());
        btTim.addActionListener(e -> taiDuLieuSoDoPhong());
        btLamMoi.addActionListener(e -> {
            oNhapTimPhong.setText("");
            hopChonLocTrangThai.setSelectedIndex(0);
            hopChonLocTang.setSelectedIndex(0);
            taiDuLieuSoDoPhong();
        });
    }

    private void dinhDangBadge(JLabel nhan, Color mauChu, Color mauNen) {
        nhan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nhan.setForeground(mauChu);
        nhan.setBackground(mauNen);
        nhan.setOpaque(true);
        nhan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mauChu, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    public void taiDuLieuSoDoPhong() {
        if (khungLuoiPhong == null) return;
        khungLuoiPhong.removeAll();

        ArrayList<Phong> danhSachPhong;
        try {
            danhSachPhong = dichVuPhong.xuat();
        } catch (Exception e) {
            danhSachPhong = new ArrayList<>();
        }

        // 1. Thống kê
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

        // 2. Lọc
        String locTrangThai = (String) hopChonLocTrangThai.getSelectedItem();
        String locTang = (String) hopChonLocTang.getSelectedItem();
        String tuKhoa = oNhapTimPhong.getText().trim().toLowerCase();

        Map<String, ArrayList<Phong>> nhomTheoTang = new LinkedHashMap<>();

        for (Phong hangHienTai : danhSachPhong) {
            if (!tuKhoa.isEmpty() && !hangHienTai.getSoPhong().toLowerCase().contains(tuKhoa)) {
                continue;
            }

            String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";
            if (locTrangThai != null && locTrangThai.contains("AVAILABLE")
                    && !(luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG")))
                continue;
            if (locTrangThai != null && locTrangThai.contains("OCCUPIED")
                    && !(luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")))
                continue;
            if (locTrangThai != null && locTrangThai.contains("CLEANING")
                    && !(luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")))
                continue;
            if (locTrangThai != null && locTrangThai.contains("MAINTENANCE")
                    && !(luongTrinh.contains("MAINTENANCE") || luongTrinh.contains("BẢO TRÌ")))
                continue;

            String tenTang = xacDinhTang(hangHienTai);
            if (locTang != null && !"Tất cả các tầng".equals(locTang) && !tenTang.equalsIgnoreCase(locTang)) {
                continue;
            }

            nhomTheoTang.computeIfAbsent(tenTang, k -> new ArrayList<>()).add(hangHienTai);
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
            for (String tang : nhomTheoTang.keySet()) {
                ArrayList<Phong> phongCuaTang = nhomTheoTang.get(tang);

                JPanel khuVucTang = new JPanel(new BorderLayout(5, 5));
                khuVucTang.setOpaque(false);
                khuVucTang.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

                int trongCuaTang = 0;
                for (Phong p : phongCuaTang) {
                    String st = (p.getTrangThai() != null) ? p.getTrangThai().toUpperCase() : "AVAILABLE";
                    if (st.contains("AVAILABLE") || st.contains("TRỐNG") || st.contains("TRONG"))
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

                JLabel nhanTangPhu = new JLabel("(" + phongCuaTang.size() + " phòng | " + trongCuaTang + " phòng trống)");
                nhanTangPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                nhanTangPhu.setForeground(new Color(86, 101, 115));

                tieuDeTang.add(nhanTieuDeTang);
                tieuDeTang.add(nhanTangPhu);
                khuVucTang.add(tieuDeTang, BorderLayout.NORTH);

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
            if (moTaChuThuong.contains("tầng 1") || moTaChuThuong.contains("tang 1")) return "Tầng 1";
            if (moTaChuThuong.contains("tầng 2") || moTaChuThuong.contains("tang 2")) return "Tầng 2";
            if (moTaChuThuong.contains("tầng 3") || moTaChuThuong.contains("tang 3")) return "Tầng 3";
            if (moTaChuThuong.contains("tầng 4") || moTaChuThuong.contains("tang 4")) return "Tầng 4";
            if (moTaChuThuong.contains("tầng 5") || moTaChuThuong.contains("tang 5")) return "Tầng 5";
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

            try {
                khachDangO = dichVuPhong.layThongTinKhachDangO(hangHienTai.getMaPhong());
            } catch (Exception ignored) {
            }
            String tenKhach = (khachDangO != null && khachDangO.getTenKhachHang() != null) ? khachDangO.getTenKhachHang() : "Khách lưu trú";
            chuoiHanhDong = " " + tenKhach;
        } else if (luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")) {
            mauVien = new Color(243, 156, 18);
            mauNenTieuDe = new Color(254, 249, 231);
            mauChuHuyHieu = new Color(185, 119, 14);
            mauNenHuyHieu = new Color(252, 243, 207);
            chuHuyHieu = "ĐANG DỌN";
            chuoiHanhDong = " Bấm để đổi trạng thái";
        } else {
            mauVien = new Color(127, 140, 141);
            mauNenTieuDe = new Color(242, 244, 244);
            mauChuHuyHieu = new Color(86, 101, 115);
            mauNenHuyHieu = new Color(230, 234, 234);
            chuHuyHieu = "BẢO TRÌ";
            chuoiHanhDong = " Bấm để đổi trạng thái";
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

        // Bottom
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

        final ThongTinPhongDangO thongTinKhach = khachDangO;
        ganSuKienClickThe(the, () -> xuLyClickThePhong(hangHienTai, thongTinKhach), the);

        return the;
    }

    private void ganSuKienClickThe(Component comp, Runnable hanhDong, JPanel the) {
        comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (dangMoDialog) return;
                    dangMoDialog = true;
                    SwingUtilities.invokeLater(() -> {
                        try {
                            hanhDong.run();
                        } finally {
                            dangMoDialog = false;
                        }
                    });
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                the.setBackground(new Color(250, 252, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                the.setBackground(Color.WHITE);
            }
        });

        if (comp instanceof Container) {
            for (Component con : ((Container) comp).getComponents()) {
                ganSuKienClickThe(con, hanhDong, the);
            }
        }
    }

    private void xuLyClickThePhong(Phong hangHienTai, ThongTinPhongDangO thongTinKhach) {
        String luongTrinh = (hangHienTai.getTrangThai() != null) ? hangHienTai.getTrangThai().toUpperCase() : "AVAILABLE";
        Window cuaSoCha = parentWindow != null ? parentWindow : SwingUtilities.getWindowAncestor(this);

        if (luongTrinh.contains("AVAILABLE") || luongTrinh.contains("TRỐNG") || luongTrinh.contains("TRONG")) {
            DialogDatPhongNhanh dialog = new DialogDatPhongNhanh(cuaSoCha, hangHienTai, this::taiDuLieuSoDoPhong);
            dialog.setVisible(true);
        } else if (luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")) {
            DatPhong datPhong = new DatPhong();
            datPhong.setIdDatPhong(thongTinKhach != null ? thongTinKhach.getBookingId() : 0);
            datPhong.setMaPhong(hangHienTai.getMaPhong());
            datPhong.setSoPhong(hangHienTai.getSoPhong());
            datPhong.setGiaMoiDem((thongTinKhach != null && thongTinKhach.getGiaMoiDem() > 0) ? thongTinKhach.getGiaMoiDem() : hangHienTai.getGiaMoiDem());
            datPhong.setTenKhachHang((thongTinKhach != null && thongTinKhach.getTenKhachHang() != null) ? thongTinKhach.getTenKhachHang() : "Khách lưu trú");
            datPhong.setSoDienThoai((thongTinKhach != null && thongTinKhach.getSoDienThoai() != null) ? thongTinKhach.getSoDienThoai() : "");
            datPhong.setCccd((thongTinKhach != null && thongTinKhach.getCccd() != null) ? thongTinKhach.getCccd() : "");
            datPhong.setNgayNhanPhong((thongTinKhach != null && thongTinKhach.getNgayNhan() != null) ? thongTinKhach.getNgayNhan() : Date.valueOf(LocalDate.now()));
            datPhong.setTenLoaiPhong(hangHienTai.getTenLoaiPhong() != null ? hangHienTai.getTenLoaiPhong() : "Tiêu chuẩn");

            DialogTraPhongThanhToan hopThoai = new DialogTraPhongThanhToan(cuaSoCha, datPhong);
            hopThoai.setVisible(true);
            if (hopThoai.isCompleted()) {
                taiDuLieuSoDoPhong();
            }
        } else {
            DialogCapNhatTrangThaiPhong dialog = new DialogCapNhatTrangThaiPhong(cuaSoCha, hangHienTai, this::taiDuLieuSoDoPhong);
            dialog.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        nhanLocTrangThai = new javax.swing.JLabel();
        hopChonLocTrangThai = new javax.swing.JComboBox<>();
        nhanLocTang = new javax.swing.JLabel();
        hopChonLocTang = new javax.swing.JComboBox<>();
        nhanTimPhong = new javax.swing.JLabel();
        oNhapTimPhong = new javax.swing.JTextField();
        btTim = new javax.swing.JButton();
        btLamMoi = new javax.swing.JButton();
        nhanStatsChung = new javax.swing.JLabel();
        nhanStatsTrong = new javax.swing.JLabel();
        nhanStatsCoKhach = new javax.swing.JLabel();
        nhanStatsDon = new javax.swing.JLabel();
        nhanStatsBaoTri = new javax.swing.JLabel();
        cuonPane = new javax.swing.JScrollPane();
        khungLuoiPhong = new javax.swing.JPanel();

        khungChinh.setBackground(new java.awt.Color(245, 247, 250));

        nhanLocTrangThai.setText("Trạng thái:");

        hopChonLocTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả trạng thái", "Trống (AVAILABLE)", "Có khách (OCCUPIED)", "Đang dọn (CLEANING)", "Bảo trì (MAINTENANCE)" }));

        nhanLocTang.setText("Tầng:");

        hopChonLocTang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả các tầng", "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5" }));

        nhanTimPhong.setText("Tìm số phòng:");

        btTim.setBackground(new java.awt.Color(41, 128, 185));
        btTim.setForeground(new java.awt.Color(255, 255, 255));
        btTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/timkiem.png"))); // NOI18N
        btTim.setText("Tìm");
        btTim.setFocusPainted(false);

        btLamMoi.setBackground(new java.awt.Color(41, 128, 185));
        btLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/load.png"))); // NOI18N
        btLamMoi.setText("Làm mới");
        btLamMoi.setFocusPainted(false);

        nhanStatsChung.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanStatsChung.setText("Tổng: 0");

        nhanStatsTrong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanStatsTrong.setText("Trống: 0");

        nhanStatsCoKhach.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanStatsCoKhach.setText("Có khách: 0");

        nhanStatsDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanStatsDon.setText("Đang dọn: 0");

        nhanStatsBaoTri.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanStatsBaoTri.setText("Bảo trì: 0");

        cuonPane.setBorder(null);

        khungLuoiPhong.setBackground(new java.awt.Color(245, 247, 250));
        khungLuoiPhong.setLayout(new javax.swing.BoxLayout(khungLuoiPhong, javax.swing.BoxLayout.Y_AXIS));
        cuonPane.setViewportView(khungLuoiPhong);

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cuonPane)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(nhanLocTrangThai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hopChonLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(nhanLocTang)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hopChonLocTang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(nhanTimPhong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oNhapTimPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTim, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nhanStatsChung)
                            .addComponent(nhanStatsTrong))
                        .addGap(18, 18, 18)
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nhanStatsCoKhach)
                            .addGroup(khungChinhLayout.createSequentialGroup()
                                .addComponent(nhanStatsDon)
                                .addGap(18, 18, 18)
                                .addComponent(nhanStatsBaoTri)))))
                .addGap(15, 15, 15))
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nhanLocTrangThai)
                            .addComponent(hopChonLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nhanLocTang)
                            .addComponent(hopChonLocTang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nhanTimPhong)
                            .addComponent(oNhapTimPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btTim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, khungChinhLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, khungChinhLayout.createSequentialGroup()
                                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nhanStatsChung)
                                    .addComponent(nhanStatsCoKhach))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nhanStatsTrong)
                                    .addComponent(nhanStatsDon)))
                            .addComponent(nhanStatsBaoTri, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)))
                .addComponent(cuonPane, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btLamMoi;
    private javax.swing.JButton btTim;
    private javax.swing.JScrollPane cuonPane;
    private javax.swing.JComboBox<String> hopChonLocTang;
    private javax.swing.JComboBox<String> hopChonLocTrangThai;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JPanel khungLuoiPhong;
    private javax.swing.JLabel nhanLocTang;
    private javax.swing.JLabel nhanLocTrangThai;
    private javax.swing.JLabel nhanStatsBaoTri;
    private javax.swing.JLabel nhanStatsChung;
    private javax.swing.JLabel nhanStatsCoKhach;
    private javax.swing.JLabel nhanStatsDon;
    private javax.swing.JLabel nhanStatsTrong;
    private javax.swing.JLabel nhanTimPhong;
    private javax.swing.JTextField oNhapTimPhong;
    // End of variables declaration//GEN-END:variables
}
