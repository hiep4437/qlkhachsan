package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.HoaDon;

public class HoaDonDAO {

	public static int parseStatusToInt(String trangThai) {
		if (trangThai == null) return 0;
		String chuoi = trangThai.trim().toUpperCase();
		if (chuoi.equals("PAID") || chuoi.contains("ĐÃ THANH TOÁN") || chuoi.equals("1")) return 1;
		if (chuoi.equals("REFUNDED") || chuoi.contains("HOÀN TIỀN") || chuoi.equals("2")) return 2;
		return 0; // UNPAID
	}

	public static String parseStatusToString(int trangThai) {
		switch (trangThai) {
			case 1: return "PAID";
			case 2: return "REFUNDED";
			default: return "UNPAID";
		}
	}

	// Them hoa don
	public int them(HoaDon bangHienTai) {
		String cauLenhSQL = "INSERT INTO hoa_don (ma_dat_phong, ngay_lap, tien_phong, phu_thu, giam_gia, "
				+ "tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			float tong = bangHienTai.getTongTien();
			if (tong == 0) {
				tong = bangHienTai.getTienPhong() + bangHienTai.getPhuThu() - bangHienTai.getGiamGia();
			}

			luongIn.setInt(1, bangHienTai.getIdDatPhong());
			luongIn.setTimestamp(2, bangHienTai.getNgayLap() != null ? bangHienTai.getNgayLap() : new Timestamp(System.currentTimeMillis()));
			luongIn.setFloat(3, bangHienTai.getTienPhong());
			luongIn.setFloat(4, bangHienTai.getPhuThu());
			luongIn.setFloat(5, bangHienTai.getGiamGia());
			luongIn.setFloat(6, Math.max(0, tong));
			luongIn.setString(7, bangHienTai.getPhuongThucThanhToan() != null ? bangHienTai.getPhuongThucThanhToan() : "Tiền mặt");
			luongIn.setInt(8, parseStatusToInt(bangHienTai.getTrangThaiThanhToan()));

			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Sua hoa don
	public int sua(HoaDon bangHienTai) {
		String cauLenhSQL = "UPDATE hoa_don SET ma_dat_phong = ?, tien_phong = ?, phu_thu = ?, giam_gia = ?, "
				+ "tong_tien = ?, phuong_thuc_thanh_toan = ?, trang_thai_thanh_toan = ? WHERE ma_hoa_don = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			float tong = bangHienTai.getTongTien();
			if (tong == 0) {
				tong = bangHienTai.getTienPhong() + bangHienTai.getPhuThu() - bangHienTai.getGiamGia();
			}

			luongIn.setInt(1, bangHienTai.getIdDatPhong());
			luongIn.setFloat(2, bangHienTai.getTienPhong());
			luongIn.setFloat(3, bangHienTai.getPhuThu());
			luongIn.setFloat(4, bangHienTai.getGiamGia());
			luongIn.setFloat(5, Math.max(0, tong));
			luongIn.setString(6, bangHienTai.getPhuongThucThanhToan());
			luongIn.setInt(7, parseStatusToInt(bangHienTai.getTrangThaiThanhToan()));
			luongIn.setInt(8, bangHienTai.getIdHoaDon());

			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xac nhan thanh toan nhanh hoa don
	public int capNhatTrangThai(int maHoaDon, String trangThai, String phuongThuc) {
		String cauLenhSQL = "UPDATE hoa_don SET trang_thai_thanh_toan = ?, phuong_thuc_thanh_toan = ?, ngay_lap = NOW() WHERE ma_hoa_don = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, parseStatusToInt(trangThai));
			luongIn.setString(2, phuongThuc);
			luongIn.setInt(3, maHoaDon);
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xoa hoa don
	public int xoa(HoaDon bangHienTai) {
		String cauLenhSQL = "DELETE FROM hoa_don WHERE ma_hoa_don = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, bangHienTai.getIdHoaDon());
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach hoa don JOIN phong, loai_phong, khach_hang
	public ArrayList<HoaDon> xuat() {
		ArrayList<HoaDon> danhSach = new ArrayList<>();
		String cauLenhSQL = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
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

		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL);
			 ResultSet ketQuaTruyVan = luongIn.executeQuery()) {

			while (ketQuaTruyVan.next()) {
				HoaDon bangHienTai = new HoaDon(
					ketQuaTruyVan.getInt("ma_hoa_don"),
					ketQuaTruyVan.getInt("ma_dat_phong"),
					ketQuaTruyVan.getTimestamp("ngay_lap"),
					ketQuaTruyVan.getFloat("tien_phong"),
					ketQuaTruyVan.getFloat("phu_thu"),
					ketQuaTruyVan.getFloat("giam_gia"),
					ketQuaTruyVan.getFloat("tong_tien"),
					ketQuaTruyVan.getString("phuong_thuc_thanh_toan"),
					parseStatusToString(ketQuaTruyVan.getInt("trang_thai_thanh_toan")),
					ketQuaTruyVan.getString("so_phong"),
					ketQuaTruyVan.getString("ten_loai_phong"),
					ketQuaTruyVan.getString("ho_ten"),
					ketQuaTruyVan.getString("so_dien_thoai"),
					ketQuaTruyVan.getString("so_cccd"),
					ketQuaTruyVan.getDate("ngay_nhan_phong"),
					ketQuaTruyVan.getDate("ngay_tra_phong")
				);
				danhSach.add(bangHienTai);
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return danhSach;
	}

	// Tim hoa don theo ma
	public HoaDon timKiem(HoaDon bangHienTai) {
		HoaDon ketQua = null;
		String cauLenhSQL = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
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

		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, bangHienTai.getIdHoaDon());
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					ketQua = new HoaDon(
						ketQuaTruyVan.getInt("ma_hoa_don"),
						ketQuaTruyVan.getInt("ma_dat_phong"),
						ketQuaTruyVan.getTimestamp("ngay_lap"),
						ketQuaTruyVan.getFloat("tien_phong"),
						ketQuaTruyVan.getFloat("phu_thu"),
						ketQuaTruyVan.getFloat("giam_gia"),
						ketQuaTruyVan.getFloat("tong_tien"),
						ketQuaTruyVan.getString("phuong_thuc_thanh_toan"),
						parseStatusToString(ketQuaTruyVan.getInt("trang_thai_thanh_toan")),
						ketQuaTruyVan.getString("so_phong"),
						ketQuaTruyVan.getString("ten_loai_phong"),
						ketQuaTruyVan.getString("ho_ten"),
						ketQuaTruyVan.getString("so_dien_thoai"),
						ketQuaTruyVan.getString("so_cccd"),
						ketQuaTruyVan.getDate("ngay_nhan_phong"),
						ketQuaTruyVan.getDate("ngay_tra_phong")
					);
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return ketQua;
	}

	// Tim hoa don theo ma dat phong
	public HoaDon timTheoDatPhong(int maDatPhong) {
		HoaDon ketQua = null;
		String cauLenhSQL = "SELECT h.ma_hoa_don, h.ma_dat_phong, h.ngay_lap, h.tien_phong, h.phu_thu, h.giam_gia, h.tong_tien, "
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

		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, maDatPhong);
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					ketQua = new HoaDon(
						ketQuaTruyVan.getInt("ma_hoa_don"),
						ketQuaTruyVan.getInt("ma_dat_phong"),
						ketQuaTruyVan.getTimestamp("ngay_lap"),
						ketQuaTruyVan.getFloat("tien_phong"),
						ketQuaTruyVan.getFloat("phu_thu"),
						ketQuaTruyVan.getFloat("giam_gia"),
						ketQuaTruyVan.getFloat("tong_tien"),
						ketQuaTruyVan.getString("phuong_thuc_thanh_toan"),
						parseStatusToString(ketQuaTruyVan.getInt("trang_thai_thanh_toan")),
						ketQuaTruyVan.getString("so_phong"),
						ketQuaTruyVan.getString("ten_loai_phong"),
						ketQuaTruyVan.getString("ho_ten"),
						ketQuaTruyVan.getString("so_dien_thoai"),
						ketQuaTruyVan.getString("so_cccd"),
						ketQuaTruyVan.getDate("ngay_nhan_phong"),
						ketQuaTruyVan.getDate("ngay_tra_phong")
					);
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return ketQua;
	}
}
