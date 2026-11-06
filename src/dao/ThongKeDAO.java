package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThongKeDAO {

    public static class ThongKePhong {
        public String soPhong;
        public String tenLoaiPhong;
        public int tang;
        public int soLuotThue;
        public double tongDoanhThuKy;

        public ThongKePhong(String soPhong, String tenLoaiPhong, int tang, int soLuotThue, double tongDoanhThuKy) {
            this.soPhong = soPhong;
            this.tenLoaiPhong = tenLoaiPhong;
            this.tang = tang;
            this.soLuotThue = soLuotThue;
            this.tongDoanhThuKy = tongDoanhThuKy;
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
        public double chungTien;
        public String phuongThuc;

        public ChiTietHoaDonBaoCao(int maHoaDon, String ngayLap, String soPhong, String hoTen, String sdt,
                                   double tienPhong, double phuThu, double giamGia, double chungTien, String phuongThuc) {
            this.maHoaDon = maHoaDon;
            this.ngayLap = ngayLap;
            this.soPhong = soPhong;
            this.hoTen = hoTen;
            this.sdt = sdt;
            this.tienPhong = tienPhong;
            this.phuThu = phuThu;
            this.giamGia = giamGia;
            this.chungTien = chungTien;
            this.phuongThuc = phuongThuc;
        }
    }

    private Connection moKetNoi() throws SQLException {
        Connection ketNoi = KetNoiCSDL.getConnection();
        if (ketNoi == null) {
            throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
        }
        return ketNoi;
    }

    // Thống kê doanh thu theo từng phòng trong khoảng ngày
    public ArrayList<ThongKePhong> thongKeTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        kiemTraKhoangNgay(tuNgay, denNgay);
        ArrayList<ThongKePhong> danhSach = new ArrayList<>();

        StringBuilder cauLenhSQL = new StringBuilder();
        cauLenhSQL.append("SELECT p.so_phong, COALESCE(lp.ten_loai_phong, 'Tiêu chuẩn') as ten_loai_phong, p.tang, ")
           .append("COUNT(DISTINCT CASE WHEN hd.ma_hoa_don IS NOT NULL THEN dp.ma_dat_phong END) as so_luot, ")
           .append("COALESCE(SUM(hd.tong_tien * ctdp.gia_moi_dem / NULLIF(room_totals.tong_gia, 0)), 0) as tong_tien ")
           .append("FROM phong p ")
           .append("LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong ")
           .append("LEFT JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong ")
           .append("LEFT JOIN (SELECT ma_dat_phong, SUM(gia_moi_dem) as tong_gia ")
           .append("FROM chi_tiet_dat_phong GROUP BY ma_dat_phong) room_totals ")
           .append("ON ctdp.ma_dat_phong = room_totals.ma_dat_phong ")
           .append("LEFT JOIN dat_phong dp ON ctdp.ma_dat_phong = dp.ma_dat_phong ")
           .append("LEFT JOIN hoa_don hd ON dp.ma_dat_phong = hd.ma_dat_phong ")
           .append("AND hd.trang_thai_thanh_toan = 1 ")
           .append("AND hd.ngay_lap >= ? AND hd.ngay_lap < DATE_ADD(?, INTERVAL 1 DAY) ");

        cauLenhSQL.append("GROUP BY p.ma_phong, p.so_phong, lp.ten_loai_phong, p.tang ")
           .append("ORDER BY tong_tien DESC, p.so_phong ASC");

        try (Connection ketNoi = moKetNoi();
             PreparedStatement luongTrinh = ketNoi.prepareStatement(cauLenhSQL.toString())) {
            luongTrinh.setDate(1, tuNgay);
            luongTrinh.setDate(2, denNgay);
            try (ResultSet ketQuaTruyVan = luongTrinh.executeQuery()) {

                while (ketQuaTruyVan.next()) {
                    danhSach.add(new ThongKePhong(
                        ketQuaTruyVan.getString("so_phong"),
                        ketQuaTruyVan.getString("ten_loai_phong"),
                        ketQuaTruyVan.getInt("tang"),
                        ketQuaTruyVan.getInt("so_luot"),
                        ketQuaTruyVan.getDouble("tong_tien")
                    ));
                }
            }
        } catch (SQLException suKien) {
            throw new SQLException("Không thể tải thống kê doanh thu theo phòng.", suKien);
        }

        return danhSach;
    }

    // Chi tiết các hóa đơn đã thanh toán trong khoảng ngày
    public ArrayList<ChiTietHoaDonBaoCao> layHoaDonTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        kiemTraKhoangNgay(tuNgay, denNgay);
        ArrayList<ChiTietHoaDonBaoCao> danhSach = new ArrayList<>();

        StringBuilder cauLenhSQL = new StringBuilder();
        cauLenhSQL.append("SELECT hd.ma_hoa_don, hd.ngay_lap, rooms.so_phong, kh.ho_ten, kh.so_dien_thoai, ")
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
           .append("WHERE hd.trang_thai_thanh_toan = 1 ")
           .append("AND hd.ngay_lap >= ? AND hd.ngay_lap < DATE_ADD(?, INTERVAL 1 DAY) ");

        cauLenhSQL.append("ORDER BY hd.ngay_lap DESC, hd.ma_hoa_don DESC");

        try (Connection ketNoi = moKetNoi();
             PreparedStatement luongTrinh = ketNoi.prepareStatement(cauLenhSQL.toString())) {
            luongTrinh.setDate(1, tuNgay);
            luongTrinh.setDate(2, denNgay);
            try (ResultSet ketQuaTruyVan = luongTrinh.executeQuery()) {
                while (ketQuaTruyVan.next()) {
                    String ngayStr = ketQuaTruyVan.getTimestamp("ngay_lap") != null ? ketQuaTruyVan.getTimestamp("ngay_lap").toString() : "";
                    if (ngayStr.length() > 16) ngayStr = ngayStr.substring(0, 16);

                    danhSach.add(new ChiTietHoaDonBaoCao(
                        ketQuaTruyVan.getInt("ma_hoa_don"),
                        ngayStr,
                        ketQuaTruyVan.getString("so_phong") != null ? "P." + ketQuaTruyVan.getString("so_phong") : "---",
                        ketQuaTruyVan.getString("ho_ten") != null ? ketQuaTruyVan.getString("ho_ten") : "---",
                        ketQuaTruyVan.getString("so_dien_thoai") != null ? ketQuaTruyVan.getString("so_dien_thoai") : "---",
                        ketQuaTruyVan.getDouble("tien_phong"),
                        ketQuaTruyVan.getDouble("phu_thu"),
                        ketQuaTruyVan.getDouble("giam_gia"),
                        ketQuaTruyVan.getDouble("tong_tien"),
                        ketQuaTruyVan.getString("phuong_thuc_thanh_toan") != null ? ketQuaTruyVan.getString("phuong_thuc_thanh_toan") : "Tiền mặt"
                    ));
                }
            }
        } catch (SQLException suKien) {
            throw new SQLException("Không thể tải danh sách hóa đơn ngày thống kê.", suKien);
        }

        return danhSach;
    }

    private void kiemTraKhoangNgay(Date tuNgay, Date denNgay) {
        if (tuNgay == null || denNgay == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc là bắt buộc.");
        }
        if (denNgay.before(tuNgay)) {
            throw new IllegalArgumentException("Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.");
        }
    }
}
