package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StatisticDAO {

    public static class ThongKePhong {
        public String soPhong;
        public String tenLoaiPhong;
        public int tang;
        public int soLuotThue;
        public double tongDoanhThu;

        public ThongKePhong(String soPhong, String tenLoaiPhong, int tang, int soLuotThue, double tongDoanhThu) {
            this.soPhong = soPhong;
            this.tenLoaiPhong = tenLoaiPhong;
            this.tang = tang;
            this.soLuotThue = soLuotThue;
            this.tongDoanhThu = tongDoanhThu;
        }
    }

    public static class ChiTietHoaDonBaoCao {
        public int maHoaDon;
        public String ngayLap;
        public String soPhong;
        public String hoTen;
        public String sdt;
        public double tienPhong;
        public double phuThu;
        public double giamGia;
        public double tongTien;
        public String phuongThuc;

        public ChiTietHoaDonBaoCao(int maHoaDon, String ngayLap, String soPhong, String hoTen, String sdt,
                                   double tienPhong, double phuThu, double giamGia, double tongTien, String phuongThuc) {
            this.maHoaDon = maHoaDon;
            this.ngayLap = ngayLap;
            this.soPhong = soPhong;
            this.hoTen = hoTen;
            this.sdt = sdt;
            this.tienPhong = tienPhong;
            this.phuThu = phuThu;
            this.giamGia = giamGia;
            this.tongTien = tongTien;
            this.phuongThuc = phuongThuc;
        }
    }

    // 1. Tổng doanh thu từ các hóa đơn đã thanh toán
    public double getTongDoanhThu() {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don WHERE trang_thai_thanh_toan = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Doanh thu hôm nay
    public double getDoanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don WHERE trang_thai_thanh_toan = 1 AND DATE(ngay_lap) = CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 3. Doanh thu tháng này
    public double getDoanhThuThangNay() {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don WHERE trang_thai_thanh_toan = 1 "
                + "AND MONTH(ngay_lap) = MONTH(CURDATE()) AND YEAR(ngay_lap) = YEAR(CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Tổng số lượt đặt phòng
    public int getTongSoLuotDat() {
        String sql = "SELECT COUNT(*) FROM dat_phong";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 5. Thống kê doanh thu theo từng phòng
    public ArrayList<ThongKePhong> thongKeTheoPhong(String kyLoc) {
        ArrayList<ThongKePhong> ds = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.so_phong, COALESCE(lp.ten_loai_phong, 'Tiêu chuẩn') as ten_loai_phong, p.tang, ")
           .append("COUNT(DISTINCT dp.ma_dat_phong) as so_luot, ")
           .append("COALESCE(SUM(hd.tong_tien), 0) as tong_tien ")
           .append("FROM phong p ")
           .append("LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong ")
           .append("LEFT JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong ")
           .append("LEFT JOIN dat_phong dp ON ctdp.ma_dat_phong = dp.ma_dat_phong ")
           .append("LEFT JOIN hoa_don hd ON dp.ma_dat_phong = hd.ma_dat_phong AND hd.trang_thai_thanh_toan = 1 ");

        if (kyLoc != null) {
            if (kyLoc.equals("Hôm nay")) {
                sql.append("AND DATE(hd.ngay_lap) = CURDATE() ");
            } else if (kyLoc.equals("7 ngày qua")) {
                sql.append("AND hd.ngay_lap >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ");
            } else if (kyLoc.equals("Tháng này")) {
                sql.append("AND MONTH(hd.ngay_lap) = MONTH(CURDATE()) AND YEAR(hd.ngay_lap) = YEAR(CURDATE()) ");
            } else if (kyLoc.equals("Tháng trước")) {
                sql.append("AND MONTH(hd.ngay_lap) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ")
                   .append("AND YEAR(hd.ngay_lap) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ");
            } else if (kyLoc.equals("Năm nay")) {
                sql.append("AND YEAR(hd.ngay_lap) = YEAR(CURDATE()) ");
            }
        }

        sql.append("GROUP BY p.ma_phong, p.so_phong, lp.ten_loai_phong, p.tang ")
           .append("ORDER BY tong_tien DESC, p.so_phong ASC");

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {

            while (rs.next()) {
                ds.add(new ThongKePhong(
                    rs.getString("so_phong"),
                    rs.getString("ten_loai_phong"),
                    rs.getInt("tang"),
                    rs.getInt("so_luot"),
                    rs.getDouble("tong_tien")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // 6. Chi tiết hóa đơn doanh thu theo kỳ
    public ArrayList<ChiTietHoaDonBaoCao> layHoaDonTheoKy(String kyLoc) {
        ArrayList<ChiTietHoaDonBaoCao> ds = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT hd.ma_hoa_don, hd.ngay_lap, rooms.so_phong, kh.ho_ten, kh.so_dien_thoai, ")
           .append("hd.tien_phong, hd.phu_thu, hd.giam_gia, hd.tong_tien, hd.phuong_thuc_thanh_toan ")
           .append("FROM hoa_don hd ")
           .append("JOIN dat_phong dp ON hd.ma_dat_phong = dp.ma_dat_phong ")
           .append("LEFT JOIN (")
           .append("SELECT ctdp.ma_dat_phong, ")
           .append("GROUP_CONCAT(DISTINCT p.so_phong ORDER BY p.so_phong SEPARATOR ', ') AS so_phong ")
           .append("FROM chi_tiet_dat_phong ctdp ")
           .append("JOIN phong p ON ctdp.ma_phong = p.ma_phong ")
           .append("GROUP BY ctdp.ma_dat_phong")
           .append(") rooms ON dp.ma_dat_phong = rooms.ma_dat_phong ")
           .append("LEFT JOIN khach_hang kh ON dp.ma_khach_hang = kh.ma_khach_hang ")
           .append("WHERE hd.trang_thai_thanh_toan = 1 ");

        if (kyLoc != null) {
            if (kyLoc.equals("Hôm nay")) {
                sql.append("AND DATE(hd.ngay_lap) = CURDATE() ");
            } else if (kyLoc.equals("7 ngày qua")) {
                sql.append("AND hd.ngay_lap >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ");
            } else if (kyLoc.equals("Tháng này")) {
                sql.append("AND MONTH(hd.ngay_lap) = MONTH(CURDATE()) AND YEAR(hd.ngay_lap) = YEAR(CURDATE()) ");
            } else if (kyLoc.equals("Tháng trước")) {
                sql.append("AND MONTH(hd.ngay_lap) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ")
                   .append("AND YEAR(hd.ngay_lap) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) ");
            } else if (kyLoc.equals("Năm nay")) {
                sql.append("AND YEAR(hd.ngay_lap) = YEAR(CURDATE()) ");
            }
        }

        sql.append("ORDER BY hd.ngay_lap DESC, hd.ma_hoa_don DESC");

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {

            while (rs.next()) {
                String ngayStr = rs.getTimestamp("ngay_lap") != null ? rs.getTimestamp("ngay_lap").toString() : "";
                if (ngayStr.length() > 16) ngayStr = ngayStr.substring(0, 16);

                ds.add(new ChiTietHoaDonBaoCao(
                    rs.getInt("ma_hoa_don"),
                    ngayStr,
                    rs.getString("so_phong") != null ? "P." + rs.getString("so_phong") : "---",
                    rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "---",
                    rs.getString("so_dien_thoai") != null ? rs.getString("so_dien_thoai") : "---",
                    rs.getDouble("tien_phong"),
                    rs.getDouble("phu_thu"),
                    rs.getDouble("giam_gia"),
                    rs.getDouble("tong_tien"),
                    rs.getString("phuong_thuc_thanh_toan") != null ? rs.getString("phuong_thuc_thanh_toan") : "Tiền mặt"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
}
