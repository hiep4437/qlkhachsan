package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.bookings;

public class BookingDAO {

	// Chuyen doi chuoi trang thai dat phong sang ma so TINYINT (0, 1, 2, 3)
	public static int chuyenTrangThaiSangSo(String st) {
		if (st == null) return 0;
		st = st.trim().toUpperCase();
		if (st.equals("1") || st.contains("CHECKED_IN") || st.contains("NHẬN") || st.contains("NHAN") || st.contains("ĐANG Ở")) return 1;
		if (st.equals("2") || st.contains("CHECKED_OUT") || st.contains("TRẢ") || st.contains("TRA")) return 2;
		if (st.equals("3") || st.contains("CANCEL") || st.contains("HỦY") || st.contains("HUY")) return 3;
		return 0; // 0: BOOKED / DA DAT
	}

	// Chuyen doi ma so TINYINT sang chuoi trang thai chuan
	public static String chuyenSoSangTrangThai(int num) {
		switch (num) {
			case 1: return "CHECKED_IN";
			case 2: return "CHECKED_OUT";
			case 3: return "CANCELLED";
			case 0:
			default: return "BOOKED";
		}
	}

	// Them dat phong
	public int them(bookings t) {
		int ttSo = chuyenTrangThaiSangSo(t.getTrangThai());
		int soNguoi = t.getSoNguoi() > 0 ? t.getSoNguoi() : 1;

		String sql = "INSERT INTO dat_phong (ma_khach_hang, ngay_nhan_phong, ngay_tra_phong, so_luong_nguoi, trang_thai, ghi_chu) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);
			if (t.getMaPhong() > 0 && !phongDuSucChua(conn, t.getMaPhong(), soNguoi)) {
				conn.rollback();
				return 0;
			}

			try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, t.getIdKhachHang());
				ps.setDate(2, t.getNgayNhanPhong());
				ps.setDate(3, t.getNgayTraPhong());
				ps.setInt(4, soNguoi);
				ps.setInt(5, ttSo);
				ps.setString(6, t.getGhiChu());

				int kq = ps.executeUpdate();
				if (kq > 0) {
					try (ResultSet rs = ps.getGeneratedKeys()) {
						if (rs.next()) {
							t.setIdDatPhong(rs.getInt(1));
						}
					}
				}
				if (kq <= 0) {
					conn.rollback();
					return 0;
				}
			}

			if (t.getMaPhong() > 0) {
				themChiTietPhong(conn, t.getIdDatPhong(), t.getMaPhong(), t.getGiaMoiDem());
			}
			conn.commit();
			return t.getIdDatPhong();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Dat phong nhanh kem phong va tao hoa don tam tinh
	public int datPhongNhanh(int customerId, Date checkIn, Date checkOut, String status, String note, int roomId, double pricePerNight) {
		return datPhongNhanh(customerId, checkIn, checkOut, status, note, roomId, pricePerNight, 1);
	}

	public int datPhongNhanh(int customerId, Date checkIn, Date checkOut, String status, String note, int roomId,
			double pricePerNight, int soNguoi) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			if (soNguoi < 1 || !phongDuSucChua(conn, roomId, soNguoi)) {
				conn.rollback();
				return 0;
			}

			// 1. Them dat_phong
			int ttDatPhong = chuyenTrangThaiSangSo(status);
			String sqlBooking = "INSERT INTO dat_phong (ma_khach_hang, ngay_nhan_phong, ngay_tra_phong, so_luong_nguoi, trang_thai, ghi_chu) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			int bookingId = 0;
			try (PreparedStatement ps = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, customerId);
				ps.setDate(2, checkIn);
				ps.setDate(3, checkOut);
				ps.setInt(4, soNguoi);
				ps.setInt(5, ttDatPhong);
				ps.setString(6, note);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						bookingId = rs.getInt(1);
					}
				}
			}

			if (bookingId <= 0) {
				conn.rollback();
				return 0;
			}

			// 2. Them chi_tiet_dat_phong
			String sqlBR = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlBR)) {
				ps.setInt(1, bookingId);
				ps.setInt(2, roomId);
				ps.setDouble(3, pricePerNight);
				ps.executeUpdate();
			}

			// 3. Neu check-in ngay (status=1 / CHECKED_IN) thi cap nhat phong sang OCCUPIED (1)
			if (ttDatPhong == 1) {
				String sqlRoom = "UPDATE phong SET trang_thai = 1 WHERE ma_phong = ? AND trang_thai = 0";
				try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
					ps.setInt(1, roomId);
					if (ps.executeUpdate() == 0) {
						conn.rollback();
						return 0;
					}
				}
			}

			// 4. Tao hoa don tam tinh
			long diffDays = Math.max(1, (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
			double totalEst = diffDays * pricePerNight;
			String sqlInv = "INSERT INTO hoa_don (ma_dat_phong, tien_phong, phu_thu, giam_gia, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan) "
					+ "VALUES (?, ?, 0, 0, ?, 'Tiền mặt', 0)";
			try (PreparedStatement ps = conn.prepareStatement(sqlInv)) {
				ps.setInt(1, bookingId);
				ps.setDouble(2, totalEst);
				ps.setDouble(3, totalEst);
				ps.executeUpdate();
			}

			conn.commit();
			return bookingId;
		} catch (Exception e) {
			if (conn != null) {
				try { conn.rollback(); } catch (Exception ignored) {}
			}
			e.printStackTrace();
			return 0;
		} finally {
			if (conn != null) {
				try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
			}
		}
	}

	// Nhan phong (Check-in): dat_phong -> CHECKED_IN (1), phong -> OCCUPIED (1)
	public boolean nhanPhong(int bookingId) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			int roomId;
			String sqlCapacity = "SELECT b.so_luong_nguoi, br.ma_phong, COALESCE(lp.suc_chua, 1) AS suc_chua, p.trang_thai "
					+ "FROM dat_phong b "
					+ "JOIN chi_tiet_dat_phong br ON b.ma_dat_phong = br.ma_dat_phong "
					+ "JOIN phong p ON br.ma_phong = p.ma_phong "
					+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
					+ "WHERE b.ma_dat_phong = ? AND b.trang_thai = 0 FOR UPDATE";
			try (PreparedStatement ps = conn.prepareStatement(sqlCapacity)) {
				ps.setInt(1, bookingId);
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next() || rs.getInt("so_luong_nguoi") > rs.getInt("suc_chua")
							|| rs.getInt("trang_thai") != 0) {
						conn.rollback();
						return false;
					}
					roomId = rs.getInt("ma_phong");
				}
			}

			String sqlBooking = "UPDATE dat_phong SET trang_thai = 1 WHERE ma_dat_phong = ? AND trang_thai = 0";
			try (PreparedStatement ps = conn.prepareStatement(sqlBooking)) {
				ps.setInt(1, bookingId);
				if (ps.executeUpdate() != 1) {
					conn.rollback();
					return false;
				}
			}

			String sqlRoom = "UPDATE phong SET trang_thai = 1 WHERE ma_phong = ? AND trang_thai = 0";
			try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
				ps.setInt(1, roomId);
				if (ps.executeUpdate() != 1) {
					conn.rollback();
					return false;
				}
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			if (conn != null) {
				try { conn.rollback(); } catch (Exception ignored) {}
			}
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
			}
		}
	}

	// Tra phong va thanh toan: dat_phong -> 2, hoa_don -> 1 (PAID), phong -> 2 (CLEANING)
	public boolean traPhongVaThanhToan(int bookingId, int roomId, double tienPhong, double phuThu, double giamGia, double tongTien, String phuongThuc) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1. Chuyen trang thai dat_phong sang 2 (CHECKED_OUT - Da tra phong)
			String sqlBooking = "UPDATE dat_phong SET trang_thai = 2, ngay_tra_phong = CURDATE() WHERE ma_dat_phong = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlBooking)) {
				ps.setInt(1, bookingId);
				ps.executeUpdate();
			}

			// 2. Kiem tra xem da co hoa don cho don nay chua, neu co thi UPDATE, chua thi INSERT
			int invoiceId = 0;
			String sqlCheckInv = "SELECT ma_hoa_don FROM hoa_don WHERE ma_dat_phong = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlCheckInv)) {
				ps.setInt(1, bookingId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						invoiceId = rs.getInt(1);
					}
				}
			}

			if (invoiceId > 0) {
				String sqlUpdateInv = "UPDATE hoa_don SET tien_phong = ?, phu_thu = ?, giam_gia = ?, tong_tien = ?, "
						+ "phuong_thuc_thanh_toan = ?, trang_thai_thanh_toan = 1, ngay_lap = NOW() WHERE ma_hoa_don = ?";
				try (PreparedStatement ps = conn.prepareStatement(sqlUpdateInv)) {
					ps.setDouble(1, tienPhong);
					ps.setDouble(2, phuThu);
					ps.setDouble(3, giamGia);
					ps.setDouble(4, Math.max(0, tongTien));
					ps.setString(5, phuongThuc != null ? phuongThuc : "Tiền mặt");
					ps.setInt(6, invoiceId);
					ps.executeUpdate();
				}
			} else {
				String sqlInsertInv = "INSERT INTO hoa_don (ma_dat_phong, tien_phong, phu_thu, giam_gia, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan, ngay_lap) "
						+ "VALUES (?, ?, ?, ?, ?, ?, 1, NOW())";
				try (PreparedStatement ps = conn.prepareStatement(sqlInsertInv)) {
					ps.setInt(1, bookingId);
					ps.setDouble(2, tienPhong);
					ps.setDouble(3, phuThu);
					ps.setDouble(4, giamGia);
					ps.setDouble(5, Math.max(0, tongTien));
					ps.setString(6, phuongThuc != null ? phuongThuc : "Tiền mặt");
					ps.executeUpdate();
				}
			}

			// 3. Chuyen trang thai phong sang 2 (CLEANING - Dang don dep)
			if (roomId > 0) {
				String sqlRoom = "UPDATE phong SET trang_thai = 2 WHERE ma_phong = ?";
				try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
					ps.setInt(1, roomId);
					ps.executeUpdate();
				}
			} else {
				String sqlRoom = "UPDATE phong p JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong "
						+ "SET p.trang_thai = 2 WHERE ctdp.ma_dat_phong = ?";
				try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
					ps.setInt(1, bookingId);
					ps.executeUpdate();
				}
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			if (conn != null) {
				try { conn.rollback(); } catch (Exception ignored) {}
			}
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
			}
		}
	}

	// Huy dat phong: dat_phong -> 3 (CANCELLED), neu phong dang giu thi tra ve AVAILABLE (0)
	public boolean huyDatPhong(int bookingId) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			String sqlBooking = "UPDATE dat_phong SET trang_thai = 3 WHERE ma_dat_phong = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlBooking)) {
				ps.setInt(1, bookingId);
				ps.executeUpdate();
			}

			// Neu phong dang o trang thai 1 thi tra ve 0
			String sqlRoom = "UPDATE phong p JOIN chi_tiet_dat_phong ctdp ON p.ma_phong = ctdp.ma_phong "
					+ "SET p.trang_thai = 0 WHERE ctdp.ma_dat_phong = ? AND p.trang_thai = 1";
			try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
				ps.setInt(1, bookingId);
				ps.executeUpdate();
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			if (conn != null) {
				try { conn.rollback(); } catch (Exception ignored) {}
			}
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
			}
		}
	}

	// Sua dat phong
	public int sua(bookings t) {
		int ttSo = chuyenTrangThaiSangSo(t.getTrangThai());
		int soNguoi = t.getSoNguoi() > 0 ? t.getSoNguoi() : 1;

		String sql = "UPDATE dat_phong SET ma_khach_hang = ?, ngay_nhan_phong = ?, ngay_tra_phong = ?, so_luong_nguoi = ?, "
				+ "trang_thai = ?, ghi_chu = ? WHERE ma_dat_phong = ?";

		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);
			if (t.getMaPhong() > 0 && !phongDuSucChua(conn, t.getMaPhong(), soNguoi)) {
				conn.rollback();
				return 0;
			}

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, t.getIdKhachHang());
				ps.setDate(2, t.getNgayNhanPhong());
				ps.setDate(3, t.getNgayTraPhong());
				ps.setInt(4, soNguoi);
				ps.setInt(5, ttSo);
				ps.setString(6, t.getGhiChu());
				ps.setInt(7, t.getIdDatPhong());
				if (ps.executeUpdate() != 1) {
					conn.rollback();
					return 0;
				}
			}

			if (t.getMaPhong() > 0) {
				try (PreparedStatement ps = conn.prepareStatement(
						"DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong = ?")) {
					ps.setInt(1, t.getIdDatPhong());
					ps.executeUpdate();
				}
				themChiTietPhong(conn, t.getIdDatPhong(), t.getMaPhong(), t.getGiaMoiDem());
			}
			conn.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private boolean phongDuSucChua(Connection conn, int roomId, int soNguoi) throws Exception {
		String sql = "SELECT COALESCE(lp.suc_chua, 1) AS suc_chua FROM phong p "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong WHERE p.ma_phong = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, roomId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && soNguoi > 0 && soNguoi <= rs.getInt("suc_chua");
			}
		}
	}

	private void themChiTietPhong(Connection conn, int bookingId, int roomId, double pricePerNight) throws Exception {
		String sql = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookingId);
			ps.setInt(2, roomId);
			ps.setDouble(3, pricePerNight);
			ps.executeUpdate();
		}
	}

	// Xoa dat phong
	public int xoa(bookings t) {
		String sql = "DELETE FROM dat_phong WHERE ma_dat_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, t.getIdDatPhong());
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach dat phong (JOIN voi khach_hang, chi_tiet_dat_phong, phong, loai_phong)
	public ArrayList<bookings> xuat() {
		ArrayList<bookings> ds = new ArrayList<>();

		String sql = "SELECT b.ma_dat_phong, b.ma_khach_hang, b.ngay_nhan_phong, b.ngay_tra_phong, b.so_luong_nguoi, "
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

		try (Connection conn = DBConnection.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				int bookingId = rs.getInt("ma_dat_phong");
				int customerId = rs.getInt("ma_khach_hang");
				Date checkIn = rs.getDate("ngay_nhan_phong");
				Date checkOut = rs.getDate("ngay_tra_phong");
				int soNguoi = rs.getInt("so_luong_nguoi");
				int tt = rs.getInt("trang_thai");
				Date createdAt = rs.getDate("ngay_tao");
				String note = rs.getString("ghi_chu");

				String cccd = rs.getString("cccd");
				String hoTen = rs.getString("ho_ten");
				String sdt = rs.getString("so_dien_thoai");

				int maPhong = rs.getInt("ma_phong");
				String roomNum = rs.getString("so_phong");
				double giaDem = rs.getDouble("gia_moi_dem");
				String tenLoaiPhong = rs.getString("ten_loai_phong");

				int tangInt = rs.getInt("tang");
				int capacity = rs.getInt("capacity");

				String tangHienThi = (tangInt > 0) ? ("Tầng " + tangInt) : "";
				if (tangHienThi.isEmpty() && roomNum != null && !roomNum.trim().isEmpty()) {
					char c = roomNum.trim().charAt(0);
					if (Character.isDigit(c)) {
						tangHienThi = "Tầng " + c;
					}
				}

				int soNguoiHienThi = (soNguoi > 0) ? soNguoi : capacity;

				bookings t = new bookings(bookingId, customerId, checkIn, checkOut, chuyenSoSangTrangThai(tt), createdAt, note,
						cccd != null ? cccd : "", tangHienThi, soNguoiHienThi, roomNum != null ? roomNum : "",
						maPhong, giaDem, tenLoaiPhong != null ? tenLoaiPhong : "", hoTen != null ? hoTen : "", sdt != null ? sdt : "");
				ds.add(t);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem dat phong theo ma
	public bookings timKiem(bookings t) {
		if (t == null || t.getIdDatPhong() <= 0) return null;

		String sql = "SELECT b.ma_dat_phong, b.ma_khach_hang, b.ngay_nhan_phong, b.ngay_tra_phong, b.so_luong_nguoi, "
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

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, t.getIdDatPhong());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int tt = rs.getInt("trang_thai");
					String roomNum = rs.getString("so_phong");
					int tangInt = rs.getInt("tang");
					String tangHienThi = (tangInt > 0) ? ("Tầng " + tangInt) : "";
					int soNguoi = rs.getInt("so_luong_nguoi");

					return new bookings(
							rs.getInt("ma_dat_phong"),
							rs.getInt("ma_khach_hang"),
							rs.getDate("ngay_nhan_phong"),
							rs.getDate("ngay_tra_phong"),
							chuyenSoSangTrangThai(tt),
							rs.getDate("ngay_tao"),
							rs.getString("ghi_chu"),
							rs.getString("cccd"),
							tangHienThi,
							soNguoi,
							roomNum != null ? roomNum : "",
							rs.getInt("ma_phong"),
							rs.getDouble("gia_moi_dem"),
							rs.getString("ten_loai_phong"),
							rs.getString("ho_ten"),
							rs.getString("so_dien_thoai")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Cap nhat trang thai dat phong
	public int capNhatTrangThai(int id, String trangThai) {
		int ttSo = chuyenTrangThaiSangSo(trangThai);
		String sql = "UPDATE dat_phong SET trang_thai = ? WHERE ma_dat_phong = ?";

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, ttSo);
			ps.setInt(2, id);

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
