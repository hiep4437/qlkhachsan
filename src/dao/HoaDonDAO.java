package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.HoaDon;

public class HoaDonDAO {

	public static int parseStatusToInt(String status) {
		if (status == null) return 0;
		String s = status.trim().toUpperCase();
		if (s.equals("PAID") || s.contains("ĐÃ THANH TOÁN") || s.equals("1")) return 1;
		if (s.equals("REFUNDED") || s.contains("HOÀN TIỀN") || s.equals("2")) return 2;
		return 0; // UNPAID
	}

	public static String parseStatusToString(int status) {
		switch (status) {
			case 1: return "PAID";
			case 2: return "REFUNDED";
			default: return "UNPAID";
		}
	}

	// Them hoa don
	public int them(HoaDon t) {
		String sql = "INSERT INTO hoa_don (ma_dat_phong, ngay_lap, tien_phong, phu_thu, giam_gia, "
				+ "tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			float total = t.getTongTien();
			if (total == 0) {
				total = t.getTienPhong() + t.getPhuThu() - t.getGiamGia();
			}

			ps.setInt(1, t.getIdDatPhong());
			ps.setTimestamp(2, t.getNgayLap() != null ? t.getNgayLap() : new Timestamp(System.currentTimeMillis()));
			ps.setFloat(3, t.getTienPhong());
			ps.setFloat(4, t.getPhuThu());
			ps.setFloat(5, t.getGiamGia());
			ps.setFloat(6, Math.max(0, total));
			ps.setString(7, t.getPhuongThucThanhToan() != null ? t.getPhuongThucThanhToan() : "Tiền mặt");
			ps.setInt(8, parseStatusToInt(t.getTrangThaiThanhToan()));

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua hoa don
	public int sua(HoaDon t) {
		String sql = "UPDATE hoa_don SET ma_dat_phong = ?, tien_phong = ?, phu_thu = ?, giam_gia = ?, "
				+ "tong_tien = ?, phuong_thuc_thanh_toan = ?, trang_thai_thanh_toan = ? WHERE ma_hoa_don = ?";
		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			float total = t.getTongTien();
			if (total == 0) {
				total = t.getTienPhong() + t.getPhuThu() - t.getGiamGia();
			}

			ps.setInt(1, t.getIdDatPhong());
			ps.setFloat(2, t.getTienPhong());
			ps.setFloat(3, t.getPhuThu());
			ps.setFloat(4, t.getGiamGia());
			ps.setFloat(5, Math.max(0, total));
			ps.setString(6, t.getPhuongThucThanhToan());
			ps.setInt(7, parseStatusToInt(t.getTrangThaiThanhToan()));
			ps.setInt(8, t.getIdHoaDon());

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xac nhan thanh toan nhanh hoa don
	public int capNhatTrangThai(int maHoaDon, String trangThai, String phuongThuc) {
		String sql = "UPDATE hoa_don SET trang_thai_thanh_toan = ?, phuong_thuc_thanh_toan = ?, ngay_lap = NOW() WHERE ma_hoa_don = ?";
		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, parseStatusToInt(trangThai));
			ps.setString(2, phuongThuc);
			ps.setInt(3, maHoaDon);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa hoa don
	public int xoa(HoaDon t) {
		String sql = "DELETE FROM hoa_don WHERE ma_hoa_don = ?";
		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, t.getIdHoaDon());
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach hoa don JOIN phong, loai_phong, khach_hang
	public ArrayList<HoaDon> xuat() {
		ArrayList<HoaDon> ds = new ArrayList<>();
		String sql = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
				+ "h.phuong_thuc_thanh_toan, h.trang_thai_thanh_toan, "
				+ "p.so_phong, lp.ten_loai_phong, kh.ho_ten, kh.so_dien_thoai, kh.so_cccd, "
				+ "dp.ngay_nhan_phong, dp.ngay_tra_phong "
				+ "FROM hoa_don h "
				+ "LEFT JOIN dat_phong dp ON h.ma_dat_phong = dp.ma_dat_phong "
				+ "LEFT JOIN chi_tiet_dat_phong ctdp ON dp.ma_dat_phong = ctdp.ma_dat_phong "
				+ "LEFT JOIN phong p ON ctdp.ma_phong = p.ma_phong "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "LEFT JOIN khach_hang kh ON dp.ma_khach_hang = kh.ma_khach_hang "
				+ "ORDER BY h.ma_hoa_don DESC";

		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				HoaDon t = new HoaDon(
					rs.getInt("ma_hoa_don"),
					rs.getInt("ma_dat_phong"),
					rs.getTimestamp("ngay_lap"),
					rs.getFloat("tien_phong"),
					rs.getFloat("phu_thu"),
					rs.getFloat("giam_gia"),
					rs.getFloat("tong_tien"),
					rs.getString("phuong_thuc_thanh_toan"),
					parseStatusToString(rs.getInt("trang_thai_thanh_toan")),
					rs.getString("so_phong"),
					rs.getString("ten_loai_phong"),
					rs.getString("ho_ten"),
					rs.getString("so_dien_thoai"),
					rs.getString("so_cccd"),
					rs.getDate("ngay_nhan_phong"),
					rs.getDate("ngay_tra_phong")
				);
				ds.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim hoa don theo ma
	public HoaDon timKiem(HoaDon t) {
		HoaDon kq = null;
		String sql = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
				+ "h.phuong_thuc_thanh_toan, h.trang_thai_thanh_toan, "
				+ "p.so_phong, lp.ten_loai_phong, kh.ho_ten, kh.so_dien_thoai, kh.so_cccd, "
				+ "dp.ngay_nhan_phong, dp.ngay_tra_phong "
				+ "FROM hoa_don h "
				+ "LEFT JOIN dat_phong dp ON h.ma_dat_phong = dp.ma_dat_phong "
				+ "LEFT JOIN chi_tiet_dat_phong ctdp ON dp.ma_dat_phong = ctdp.ma_dat_phong "
				+ "LEFT JOIN phong p ON ctdp.ma_phong = p.ma_phong "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "LEFT JOIN khach_hang kh ON dp.ma_khach_hang = kh.ma_khach_hang "
				+ "WHERE h.ma_hoa_don = ?";

		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, t.getIdHoaDon());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					kq = new HoaDon(
						rs.getInt("ma_hoa_don"),
						rs.getInt("ma_dat_phong"),
						rs.getTimestamp("ngay_lap"),
						rs.getFloat("tien_phong"),
						rs.getFloat("phu_thu"),
						rs.getFloat("giam_gia"),
						rs.getFloat("tong_tien"),
						rs.getString("phuong_thuc_thanh_toan"),
						parseStatusToString(rs.getInt("trang_thai_thanh_toan")),
						rs.getString("so_phong"),
						rs.getString("ten_loai_phong"),
						rs.getString("ho_ten"),
						rs.getString("so_dien_thoai"),
						rs.getString("so_cccd"),
						rs.getDate("ngay_nhan_phong"),
						rs.getDate("ngay_tra_phong")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}

	// Tim hoa don theo ma dat phong
	public HoaDon timTheoDatPhong(int maDatPhong) {
		HoaDon kq = null;
		String sql = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
				+ "h.phuong_thuc_thanh_toan, h.trang_thai_thanh_toan, "
				+ "p.so_phong, lp.ten_loai_phong, kh.ho_ten, kh.so_dien_thoai, kh.so_cccd, "
				+ "dp.ngay_nhan_phong, dp.ngay_tra_phong "
				+ "FROM hoa_don h "
				+ "LEFT JOIN dat_phong dp ON h.ma_dat_phong = dp.ma_dat_phong "
				+ "LEFT JOIN chi_tiet_dat_phong ctdp ON dp.ma_dat_phong = ctdp.ma_dat_phong "
				+ "LEFT JOIN phong p ON ctdp.ma_phong = p.ma_phong "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "LEFT JOIN khach_hang kh ON dp.ma_khach_hang = kh.ma_khach_hang "
				+ "WHERE h.ma_dat_phong = ?";

		try (Connection conn = KetNoiCSDL.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, maDatPhong);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					kq = new HoaDon(
						rs.getInt("ma_hoa_don"),
						rs.getInt("ma_dat_phong"),
						rs.getTimestamp("ngay_lap"),
						rs.getFloat("tien_phong"),
						rs.getFloat("phu_thu"),
						rs.getFloat("giam_gia"),
						rs.getFloat("tong_tien"),
						rs.getString("phuong_thuc_thanh_toan"),
						parseStatusToString(rs.getInt("trang_thai_thanh_toan")),
						rs.getString("so_phong"),
						rs.getString("ten_loai_phong"),
						rs.getString("ho_ten"),
						rs.getString("so_dien_thoai"),
						rs.getString("so_cccd"),
						rs.getDate("ngay_nhan_phong"),
						rs.getDate("ngay_tra_phong")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
