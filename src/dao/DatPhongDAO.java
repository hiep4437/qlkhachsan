package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.DatPhong;

public class DatPhongDAO {

	// Chuyen doi chuoi trang thai dat phong sang ma so TINYINT (0, 1, 2, 3)
	public static int chuyenTrangThaiSangSo(String luongTrinh) {
		if (luongTrinh == null) return 0;
		luongTrinh = luongTrinh.trim().toUpperCase();
		if (luongTrinh.equals("1") || luongTrinh.contains("CHECKED_IN") || luongTrinh.contains("NHẬN") || luongTrinh.contains("NHAN") || luongTrinh.contains("ĐANG Ở")) return 1;
		if (luongTrinh.equals("2") || luongTrinh.contains("CHECKED_OUT") || luongTrinh.contains("TRẢ") || luongTrinh.contains("TRA")) return 2;
		if (luongTrinh.equals("3") || luongTrinh.contains("CANCEL") || luongTrinh.contains("HỦY") || luongTrinh.contains("HUY")) return 3;
		return 0; // 0: BOOKED / DA DAT
	}

	// Chuyen doi ma so TINYINT sang chuoi trang thai chuan
	public static String chuyenSoSangTrangThai(int soLuong) {
		switch (soLuong) {
			case 1: return "CHECKED_IN";
			case 2: return "CHECKED_OUT";
			case 3: return "CANCELLED";
			case 0:
			default: return "BOOKED";
		}
	}

	// Them dat phong
	public int them(DatPhong bangHienTai) {
		int trangThaiSo = chuyenTrangThaiSangSo(bangHienTai.getTrangThai());
		int soNguoi = bangHienTai.getSoNguoi() > 0 ? bangHienTai.getSoNguoi() : 1;

		String cauLenhSQL = "INSERT INTO dat_phong (ma_khach_hang, ngay_nhan_phong, ngay_tra_phong, so_luong_nguoi, trang_thai, ghi_chu) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection ketNoi = KetNoiCSDL.getConnection()) {
			ketNoi.setAutoCommit(false);
			if (bangHienTai.getMaPhong() > 0 && !phongPhuHopChoDat(ketNoi, bangHienTai.getMaPhong(), soNguoi,
					bangHienTai.getNgayNhanPhong(), bangHienTai.getNgayTraPhong(), 0)) {
				ketNoi.rollback();
				return 0;
			}

			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL, Statement.RETURN_GENERATED_KEYS)) {
				luongIn.setInt(1, bangHienTai.getIdKhachHang());
				luongIn.setDate(2, bangHienTai.getNgayNhanPhong());
				luongIn.setDate(3, bangHienTai.getNgayTraPhong());
				luongIn.setInt(4, soNguoi);
				luongIn.setInt(5, trangThaiSo);
				luongIn.setString(6, bangHienTai.getGhiChu());

				int ketQua = luongIn.executeUpdate();
				if (ketQua > 0) {
					try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
						if (ketQuaTruyVan.next()) {
							bangHienTai.setIdDatPhong(ketQuaTruyVan.getInt(1));
						}
					}
				}
				if (ketQua <= 0) {
					ketNoi.rollback();
					return 0;
				}
			}

			if (bangHienTai.getMaPhong() > 0) {
				themChiTietPhong(ketNoi, bangHienTai.getIdDatPhong(), bangHienTai.getMaPhong(), bangHienTai.getGiaMoiDem());
			}
			ketNoi.commit();
			return bangHienTai.getIdDatPhong();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Dat phong nhanh kem phong va tao hoa don tam tinh
	public int datPhongNhanh(int maKhachHang, Date nhanPhong, Date traPhong, String trangThai, String ghiChu, int maPhong, double giaMoiDem) {
		return datPhongNhanh(maKhachHang, nhanPhong, traPhong, trangThai, ghiChu, maPhong, giaMoiDem, 1);
	}

	public int datPhongNhanh(int maKhachHang, Date nhanPhong, Date traPhong, String trangThai, String ghiChu, int maPhong,
			double giaMoiDem, int soNguoi) {
		Connection ketNoi = null;
		try {
			ketNoi = KetNoiCSDL.getConnection();
			ketNoi.setAutoCommit(false);
			if (soNguoi < 1 || !phongPhuHopChoDat(ketNoi, maPhong, soNguoi, nhanPhong, traPhong, 0)) {
				ketNoi.rollback();
				return 0;
			}

			// 1. Them dat_phong
			int trangThaiDatPhong = chuyenTrangThaiSangSo(trangThai);
			String cauLenhDatBooking = "INSERT INTO dat_phong (ma_khach_hang, ngay_nhan_phong, ngay_tra_phong, so_luong_nguoi, trang_thai, ghi_chu) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			int maDatPhong = 0;
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhDatBooking, Statement.RETURN_GENERATED_KEYS)) {
				luongIn.setInt(1, maKhachHang);
				luongIn.setDate(2, nhanPhong);
				luongIn.setDate(3, traPhong);
				luongIn.setInt(4, soNguoi);
				luongIn.setInt(5, trangThaiDatPhong);
				luongIn.setString(6, ghiChu);
				luongIn.executeUpdate();
				try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
					if (ketQuaTruyVan.next()) {
						maDatPhong = ketQuaTruyVan.getInt(1);
					}
				}
			}

			if (maDatPhong <= 0) {
				ketNoi.rollback();
				return 0;
			}

			// 2. Them chi_tiet_dat_phong
			String cauLenhDatPhongBR = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhDatPhongBR)) {
				luongIn.setInt(1, maDatPhong);
				luongIn.setInt(2, maPhong);
				luongIn.setDouble(3, giaMoiDem);
				luongIn.executeUpdate();
			}

			// 3. Neu check-in ngay (status=1 / CHECKED_IN) thi cap nhat phong sang OCCUPIED (1)
			if (trangThaiDatPhong == 1) {
				String cauLenhPhong = "UPDATE phong SET trang_thai = 1 WHERE ma_phong = ? AND trang_thai = 0";
				try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
					luongIn.setInt(1, maPhong);
					if (luongIn.executeUpdate() == 0) {
						ketNoi.rollback();
						return 0;
					}
				}
			}

			// 4. Tao hoa don tam tinh
			long soNgayChenhLech = Math.max(1, (traPhong.getTime() - nhanPhong.getTime()) / (1000 * 60 * 60 * 24));
			double tongDuKien = soNgayChenhLech * giaMoiDem;
			String cauLenhHoaDon = "INSERT INTO hoa_don (ma_dat_phong, tien_phong, phu_thu, giam_gia, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan) "
					+ "VALUES (?, ?, 0, 0, ?, 'Tiền mặt', 0)";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhHoaDon)) {
				luongIn.setInt(1, maDatPhong);
				luongIn.setDouble(2, tongDuKien);
				luongIn.setDouble(3, tongDuKien);
				luongIn.executeUpdate();
			}

			ketNoi.commit();
			return maDatPhong;
		} catch (Exception suKien) {
			if (ketNoi != null) {
				try { ketNoi.rollback(); } catch (Exception biBoQua) {}
			}
			suKien.printStackTrace();
			return 0;
		} finally {
			if (ketNoi != null) {
				try { ketNoi.setAutoCommit(true); ketNoi.close(); } catch (Exception biBoQua) {}
			}
		}
	}

	// Nhan phong (Check-in): dat_phong -> CHECKED_IN (1), phong -> OCCUPIED (1)
	public boolean nhanPhong(int maDatPhong) {
		Connection ketNoi = null;
		try {
			ketNoi = KetNoiCSDL.getConnection();
			ketNoi.setAutoCommit(false);

			int maPhong;
			String cauLenhSucChua = "SELECT b.so_luong_nguoi, br.ma_phong, COALESCE(lp.suc_chua, 1) AS suc_chua, p.trang_thai "
					+ "FROM dat_phong b "
					+ "JOIN chi_tiet_dat_phong br ON b.ma_dat_phong = br.ma_dat_phong "
					+ "JOIN phong p ON br.ma_phong = p.ma_phong "
					+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
					+ "WHERE b.ma_dat_phong = ? AND b.trang_thai = 0 FOR UPDATE";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSucChua)) {
				luongIn.setInt(1, maDatPhong);
				try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
					if (!ketQuaTruyVan.next() || ketQuaTruyVan.getInt("so_luong_nguoi") > ketQuaTruyVan.getInt("suc_chua")
							|| ketQuaTruyVan.getInt("trang_thai") != 0) {
						ketNoi.rollback();
						return false;
					}
					maPhong = ketQuaTruyVan.getInt("ma_phong");
				}
			}

			String cauLenhDatBooking = "UPDATE dat_phong SET trang_thai = 1 WHERE ma_dat_phong = ? AND trang_thai = 0";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhDatBooking)) {
				luongIn.setInt(1, maDatPhong);
				if (luongIn.executeUpdate() != 1) {
					ketNoi.rollback();
					return false;
				}
			}

			String cauLenhPhong = "UPDATE phong SET trang_thai = 1 WHERE ma_phong = ? AND trang_thai = 0";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
				luongIn.setInt(1, maPhong);
				if (luongIn.executeUpdate() != 1) {
					ketNoi.rollback();
					return false;
				}
			}

			ketNoi.commit();
			return true;
		} catch (Exception suKien) {
			if (ketNoi != null) {
				try { ketNoi.rollback(); } catch (Exception biBoQua) {}
			}
			suKien.printStackTrace();
			return false;
		} finally {
			if (ketNoi != null) {
				try { ketNoi.setAutoCommit(true); ketNoi.close(); } catch (Exception biBoQua) {}
			}
		}
	}

	// Tra phong va thanh toan: dat_phong -> 2, hoa_don -> 1 (PAID), phong -> 2 (CLEANING)
	public boolean traPhongVaThanhToan(int maDatPhong, int maPhong, double tienPhong, double phuThu, double giamGia, double chungTien, String phuongThuc) {
		Connection ketNoi = null;
		try {
			ketNoi = KetNoiCSDL.getConnection();
			ketNoi.setAutoCommit(false);

			// 1. Chuyen trang thai dat_phong sang 2 (CHECKED_OUT - Da tra phong)
			String cauLenhDatBooking = "UPDATE dat_phong SET trang_thai = 2, ngay_tra_phong = CURDATE() WHERE ma_dat_phong = ?";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhDatBooking)) {
				luongIn.setInt(1, maDatPhong);
				luongIn.executeUpdate();
			}

			// 2. Kiem tra xem da co hoa don cho don nay chua, neu co thi UPDATE, chua thi INSERT
			int maHoaDon = 0;
			String cauLenhKiemTraHoaDon = "SELECT ma_hoa_don FROM hoa_don WHERE ma_dat_phong = ?";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhKiemTraHoaDon)) {
				luongIn.setInt(1, maDatPhong);
				try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
					if (ketQuaTruyVan.next()) {
						maHoaDon = ketQuaTruyVan.getInt(1);
					}
				}
			}

			if (maHoaDon > 0) {
				String cauLenhCapNhatHoaDon = "UPDATE hoa_don SET tien_phong = ?, phu_thu = ?, giam_gia = ?, tong_tien = ?, "
						+ "phuong_thuc_thanh_toan = ?, trang_thai_thanh_toan = 1, ngay_lap = NOW() WHERE ma_hoa_don = ?";
				try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhCapNhatHoaDon)) {
					luongIn.setDouble(1, tienPhong);
					luongIn.setDouble(2, phuThu);
					luongIn.setDouble(3, giamGia);
					luongIn.setDouble(4, Math.max(0, chungTien));
					luongIn.setString(5, phuongThuc != null ? phuongThuc : "Tiền mặt");
					luongIn.setInt(6, maHoaDon);
					luongIn.executeUpdate();
				}
			} else {
				String cauLenhThemHoaDon = "INSERT INTO hoa_don (ma_dat_phong, tien_phong, phu_thu, giam_gia, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan, ngay_lap) "
						+ "VALUES (?, ?, ?, ?, ?, ?, 1, NOW())";
				try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhThemHoaDon)) {
					luongIn.setInt(1, maDatPhong);
					luongIn.setDouble(2, tienPhong);
					luongIn.setDouble(3, phuThu);
					luongIn.setDouble(4, giamGia);
					luongIn.setDouble(5, Math.max(0, chungTien));
					luongIn.setString(6, phuongThuc != null ? phuongThuc : "Tiền mặt");
					luongIn.executeUpdate();
				}
			}

			// 3. Chuyen trang thai phong sang 2 (CLEANING - Dang don dep)
			if (maPhong > 0) {
				String cauLenhPhong = "UPDATE phong SET trang_thai = 2 WHERE ma_phong = ?";
				try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
					luongIn.setInt(1, maPhong);
					luongIn.executeUpdate();
				}
			} else {
				String cauLenhPhong = "UPDATE phong p JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong "
						+ "SET p.trang_thai = 2 WHERE ctdp.ma_dat_phong = ?";
				try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
					luongIn.setInt(1, maDatPhong);
					luongIn.executeUpdate();
				}
			}

			ketNoi.commit();
			return true;
		} catch (Exception suKien) {
			if (ketNoi != null) {
				try { ketNoi.rollback(); } catch (Exception biBoQua) {}
			}
			suKien.printStackTrace();
			return false;
		} finally {
			if (ketNoi != null) {
				try { ketNoi.setAutoCommit(true); ketNoi.close(); } catch (Exception biBoQua) {}
			}
		}
	}

	// Huy dat phong: dat_phong -> 3 (CANCELLED), neu phong dang giu thi tra ve AVAILABLE (0)
	public boolean huyDatPhong(int maDatPhong) {
		Connection ketNoi = null;
		try {
			ketNoi = KetNoiCSDL.getConnection();
			ketNoi.setAutoCommit(false);

			String cauLenhDatBooking = "UPDATE dat_phong SET trang_thai = 3 WHERE ma_dat_phong = ?";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhDatBooking)) {
				luongIn.setInt(1, maDatPhong);
				luongIn.executeUpdate();
			}

			// Neu phong dang o trang thai 1 thi tra ve 0
			String cauLenhPhong = "UPDATE phong p JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong "
					+ "SET p.trang_thai = 0 WHERE ctdp.ma_dat_phong = ? AND p.trang_thai = 1";
			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
				luongIn.setInt(1, maDatPhong);
				luongIn.executeUpdate();
			}

			ketNoi.commit();
			return true;
		} catch (Exception suKien) {
			if (ketNoi != null) {
				try { ketNoi.rollback(); } catch (Exception biBoQua) {}
			}
			suKien.printStackTrace();
			return false;
		} finally {
			if (ketNoi != null) {
				try { ketNoi.setAutoCommit(true); ketNoi.close(); } catch (Exception biBoQua) {}
			}
		}
	}

	// Sua dat phong
	public int sua(DatPhong bangHienTai) {
		int trangThaiSo = chuyenTrangThaiSangSo(bangHienTai.getTrangThai());
		int soNguoi = bangHienTai.getSoNguoi() > 0 ? bangHienTai.getSoNguoi() : 1;

		String cauLenhSQL = "UPDATE dat_phong SET ma_khach_hang = ?, ngay_nhan_phong = ?, ngay_tra_phong = ?, so_luong_nguoi = ?, "
				+ "trang_thai = ?, ghi_chu = ? WHERE ma_dat_phong = ?";

		try (Connection ketNoi = KetNoiCSDL.getConnection()) {
			ketNoi.setAutoCommit(false);
			if (bangHienTai.getMaPhong() > 0 && !phongPhuHopChoDat(ketNoi, bangHienTai.getMaPhong(), soNguoi,
					bangHienTai.getNgayNhanPhong(), bangHienTai.getNgayTraPhong(), bangHienTai.getIdDatPhong())) {
				ketNoi.rollback();
				return 0;
			}

			try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
				luongIn.setInt(1, bangHienTai.getIdKhachHang());
				luongIn.setDate(2, bangHienTai.getNgayNhanPhong());
				luongIn.setDate(3, bangHienTai.getNgayTraPhong());
				luongIn.setInt(4, soNguoi);
				luongIn.setInt(5, trangThaiSo);
				luongIn.setString(6, bangHienTai.getGhiChu());
				luongIn.setInt(7, bangHienTai.getIdDatPhong());
				if (luongIn.executeUpdate() != 1) {
					ketNoi.rollback();
					return 0;
				}
			}

			if (bangHienTai.getMaPhong() > 0) {
				try (PreparedStatement luongIn = ketNoi.prepareStatement(
						"DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong = ?")) {
					luongIn.setInt(1, bangHienTai.getIdDatPhong());
					luongIn.executeUpdate();
				}
				themChiTietPhong(ketNoi, bangHienTai.getIdDatPhong(), bangHienTai.getMaPhong(), bangHienTai.getGiaMoiDem());
			}
			ketNoi.commit();
			return 1;
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	public boolean phongBiDatTrung(int maPhong, Date ngayNhan, Date ngayTra, int maDatPhongBoQua) throws Exception {
		if (maPhong <= 0 || ngayNhan == null || ngayTra == null || !ngayTra.after(ngayNhan)) {
			return true;
		}

		try (Connection ketNoi = KetNoiCSDL.getConnection()) {
			return coDonTrung(ketNoi, maPhong, ngayNhan, ngayTra, maDatPhongBoQua);
		}
	}

	private boolean phongPhuHopChoDat(Connection ketNoi, int maPhong, int soNguoi, Date ngayNhan,
			Date ngayTra, int maDatPhongBoQua) throws Exception {
		if (maPhong <= 0 || soNguoi <= 0 || ngayNhan == null || ngayTra == null || !ngayTra.after(ngayNhan)) {
			return false;
		}

		String cauLenhPhong = "SELECT p.trang_thai, COALESCE(lp.suc_chua, 1) AS suc_chua "
				+ "FROM phong p LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "WHERE p.ma_phong = ? FOR UPDATE";
		try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhPhong)) {
			luongIn.setInt(1, maPhong);
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (!ketQuaTruyVan.next() || soNguoi > ketQuaTruyVan.getInt("suc_chua")
						|| ketQuaTruyVan.getInt("trang_thai") == 2 || ketQuaTruyVan.getInt("trang_thai") == 3) {
					return false;
				}
			}
		}

		return !coDonTrung(ketNoi, maPhong, ngayNhan, ngayTra, maDatPhongBoQua);
	}

	private boolean coDonTrung(Connection ketNoi, int maPhong, Date ngayNhan, Date ngayTra,
			int maDatPhongBoQua) throws Exception {
		String cauLenhSQL = "SELECT 1 FROM chi_tiet_dat_phong br "
				+ "JOIN dat_phong b ON b.ma_dat_phong = br.ma_dat_phong "
				+ "WHERE br.ma_phong = ? AND b.trang_thai IN (0, 1) "
				+ "AND b.ngay_nhan_phong < ? AND b.ngay_tra_phong > ? "
				+ "AND b.ma_dat_phong <> ? LIMIT 1";
		try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maPhong);
			luongIn.setDate(2, ngayTra);
			luongIn.setDate(3, ngayNhan);
			luongIn.setInt(4, maDatPhongBoQua);
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				return ketQuaTruyVan.next();
			}
		}
	}

	private void themChiTietPhong(Connection ketNoi, int maDatPhong, int maPhong, double giaMoiDem) throws Exception {
		String cauLenhSQL = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
		try (PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maDatPhong);
			luongIn.setInt(2, maPhong);
			luongIn.setDouble(3, giaMoiDem);
			luongIn.executeUpdate();
		}
	}

	// Xoa dat phong
	public int xoa(DatPhong bangHienTai) {
		String cauLenhSQL = "DELETE FROM dat_phong WHERE ma_dat_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, bangHienTai.getIdDatPhong());
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach dat phong (JOIN voi khach_hang, chi_tiet_dat_phong, phong, loai_phong)
	public ArrayList<DatPhong> xuat() {
		ArrayList<DatPhong> danhSach = new ArrayList<>();

		String cauLenhSQL = "SELECT b.ma_dat_phong, b.ma_khach_hang, b.ngay_nhan_phong, b.ngay_tra_phong, b.so_luong_nguoi, "
				+ "b.trang_thai, b.ngay_tao, b.ghi_chu, "
				+ "c.so_cccd AS cccd, c.ho_ten, c.so_dien_thoai, "
				+ "p.ma_phong, p.so_phong, p.tang, p.gia_moi_dem, p.mo_ta AS room_desc, "
				+ "COALESCE(lp.suc_chua, 1) AS capacity, lp.ten_loai_phong "
				+ "FROM dat_phong b "
				+ "LEFT JOIN khach_hang c ON b.ma_khach_hang = c.ma_khach_hang "
				+ "LEFT JOIN chi_tiet_dat_phong br ON b.ma_dat_phong = br.ma_dat_phong "
				+ "LEFT JOIN phong p ON br.ma_phong = p.ma_phong "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "ORDER BY b.ma_dat_phong DESC";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     Statement luongTrinh = ketNoi.createStatement();
		     ResultSet ketQuaTruyVan = luongTrinh.executeQuery(cauLenhSQL)) {

			while (ketQuaTruyVan.next()) {
				int maDatPhong = ketQuaTruyVan.getInt("ma_dat_phong");
				int maKhachHang = ketQuaTruyVan.getInt("ma_khach_hang");
				Date nhanPhong = ketQuaTruyVan.getDate("ngay_nhan_phong");
				Date traPhong = ketQuaTruyVan.getDate("ngay_tra_phong");
				int soNguoi = ketQuaTruyVan.getInt("so_luong_nguoi");
				int trangThai = ketQuaTruyVan.getInt("trang_thai");
				Date thoiDiemTao = ketQuaTruyVan.getDate("ngay_tao");
				String ghiChu = ketQuaTruyVan.getString("ghi_chu");

				String cccd = ketQuaTruyVan.getString("cccd");
				String hoTen = ketQuaTruyVan.getString("ho_ten");
				String sdt = ketQuaTruyVan.getString("so_dien_thoai");

				int maPhong = ketQuaTruyVan.getInt("ma_phong");
				String soPhong = ketQuaTruyVan.getString("so_phong");
				double giaDem = ketQuaTruyVan.getDouble("gia_moi_dem");
				String tenLoaiPhong = ketQuaTruyVan.getString("ten_loai_phong");

				int tangSo = ketQuaTruyVan.getInt("tang");
				int sucChua = ketQuaTruyVan.getInt("capacity");

				String tangHienThi = (tangSo > 0) ? ("Tầng " + tangSo) : "";
				if (tangHienThi.isEmpty() && soPhong != null && !soPhong.trim().isEmpty()) {
					char thanhPhan = soPhong.trim().charAt(0);
					if (Character.isDigit(thanhPhan)) {
						tangHienThi = "Tầng " + thanhPhan;
					}
				}

				int soNguoiHienThi = (soNguoi > 0) ? soNguoi : sucChua;

				DatPhong bangHienTai = new DatPhong(maDatPhong, maKhachHang, nhanPhong, traPhong, chuyenSoSangTrangThai(trangThai), thoiDiemTao, ghiChu,
						cccd != null ? cccd : "", tangHienThi, soNguoiHienThi, soPhong != null ? soPhong : "",
						maPhong, giaDem, tenLoaiPhong != null ? tenLoaiPhong : "", hoTen != null ? hoTen : "", sdt != null ? sdt : "");
				danhSach.add(bangHienTai);
			}

		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return danhSach;
	}

	// Tim kiem dat phong theo ma
	public DatPhong timKiem(DatPhong bangHienTai) {
		if (bangHienTai == null || bangHienTai.getIdDatPhong() <= 0) return null;

		String cauLenhSQL = "SELECT b.ma_dat_phong, b.ma_khach_hang, b.ngay_nhan_phong, b.ngay_tra_phong, b.so_luong_nguoi, "
				+ "b.trang_thai, b.ngay_tao, b.ghi_chu, "
				+ "c.so_cccd AS cccd, c.ho_ten, c.so_dien_thoai, "
				+ "p.ma_phong, p.so_phong, p.tang, p.gia_moi_dem, p.mo_ta AS room_desc, "
				+ "COALESCE(lp.suc_chua, 1) AS capacity, lp.ten_loai_phong "
				+ "FROM dat_phong b "
				+ "LEFT JOIN khach_hang c ON b.ma_khach_hang = c.ma_khach_hang "
				+ "LEFT JOIN chi_tiet_dat_phong br ON b.ma_dat_phong = br.ma_dat_phong "
				+ "LEFT JOIN phong p ON br.ma_phong = p.ma_phong "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "WHERE b.ma_dat_phong = ?";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, bangHienTai.getIdDatPhong());
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					int trangThai = ketQuaTruyVan.getInt("trang_thai");
					String soPhong = ketQuaTruyVan.getString("so_phong");
					int tangSo = ketQuaTruyVan.getInt("tang");
					String tangHienThi = (tangSo > 0) ? ("Tầng " + tangSo) : "";
					int soNguoi = ketQuaTruyVan.getInt("so_luong_nguoi");

					return new DatPhong(
							ketQuaTruyVan.getInt("ma_dat_phong"),
							ketQuaTruyVan.getInt("ma_khach_hang"),
							ketQuaTruyVan.getDate("ngay_nhan_phong"),
							ketQuaTruyVan.getDate("ngay_tra_phong"),
							chuyenSoSangTrangThai(trangThai),
							ketQuaTruyVan.getDate("ngay_tao"),
							ketQuaTruyVan.getString("ghi_chu"),
							ketQuaTruyVan.getString("cccd"),
							tangHienThi,
							soNguoi,
							soPhong != null ? soPhong : "",
							ketQuaTruyVan.getInt("ma_phong"),
							ketQuaTruyVan.getDouble("gia_moi_dem"),
							ketQuaTruyVan.getString("ten_loai_phong"),
							ketQuaTruyVan.getString("ho_ten"),
							ketQuaTruyVan.getString("so_dien_thoai")
					);
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return null;
	}

	// Cap nhat trang thai dat phong
	public int capNhatTrangThai(int ma, String trangThai) {
		int trangThaiSo = chuyenTrangThaiSangSo(trangThai);
		String cauLenhSQL = "UPDATE dat_phong SET trang_thai = ? WHERE ma_dat_phong = ?";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, trangThaiSo);
			luongIn.setInt(2, ma);

			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}
}
