package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.bookings;

public class BookingDAO {

	// Them dat phong
	public int them(bookings t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "INSERT INTO bookings (customer_id, check_in_date, check_out_date, status, note) VALUES ("
					+ t.getIdKhachHang() + ", '" + t.getNgayNhanPhong() + "', '" + t.getNgayTraPhong() + "', '"
					+ t.getTrangThai() + "', '" + t.getGhiChu() + "')";

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Dat phong nhanh kem phong va tao hoa don tam tinh
	public int datPhongNhanh(int customerId, Date checkIn, Date checkOut, String status, String note, int roomId, double pricePerNight) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1. Them booking
			String sqlBooking = "INSERT INTO bookings (customer_id, check_in_date, check_out_date, status, note) VALUES (?, ?, ?, ?, ?)";
			int bookingId = 0;
			try (PreparedStatement ps = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, customerId);
				ps.setDate(2, checkIn);
				ps.setDate(3, checkOut);
				ps.setString(4, status);
				ps.setString(5, note);
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

			// 2. Them booking_rooms
			String sqlBR = "INSERT INTO booking_rooms (booking_id, room_id, price_per_night) VALUES (?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sqlBR)) {
				ps.setInt(1, bookingId);
				ps.setInt(2, roomId);
				ps.setDouble(3, pricePerNight);
				ps.executeUpdate();
			}

			// 3. Cap nhat phong sang OCCUPIED neu check-in ngay, hoac giu AVAILABLE/BOOKED
			String roomStatus = "CHECKED_IN".equalsIgnoreCase(status) ? "OCCUPIED" : "OCCUPIED";
			String sqlRoom = "UPDATE rooms SET status = ? WHERE room_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
				ps.setString(1, roomStatus);
				ps.setInt(2, roomId);
				ps.executeUpdate();
			}

			// 4. Tao hoa don tam
			long soDem = Math.max(1, (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
			double tienPhong = soDem * pricePerNight;
			String sqlInv = "INSERT INTO invoices (booking_id, room_amount, surcharge, discount, total_amount, payment_method, payment_status) "
					+ "VALUES (?, ?, 0, 0, ?, 'Tien mat', 'UNPAID')";
			try (PreparedStatement ps = conn.prepareStatement(sqlInv)) {
				ps.setInt(1, bookingId);
				ps.setDouble(2, tienPhong);
				ps.setDouble(3, tienPhong);
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

	// Tra phong va thanh toan: chuyen booking thanh CHECKED_OUT, hoa don thanh PAID, va PHONG THANH CLEANING
	public boolean traPhongVaThanhToan(int bookingId, int roomId, double tienPhong, double phuThu, double giamGia, double tongTien, String phuongThuc) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1. Cap nhat booking
			if (bookingId > 0) {
				String sqlB = "UPDATE bookings SET status = 'CHECKED_OUT' WHERE booking_id = ?";
				try (PreparedStatement ps = conn.prepareStatement(sqlB)) {
					ps.setInt(1, bookingId);
					ps.executeUpdate();
				}

				// 2. Cap nhat hoac them hoa don
				String sqlCheckInv = "SELECT invoice_id FROM invoices WHERE booking_id = ?";
				int invoiceId = 0;
				try (PreparedStatement ps = conn.prepareStatement(sqlCheckInv)) {
					ps.setInt(1, bookingId);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) invoiceId = rs.getInt(1);
					}
				}

				if (invoiceId > 0) {
					String sqlUpdateInv = "UPDATE invoices SET room_amount = ?, surcharge = ?, discount = ?, total_amount = ?, "
							+ "payment_method = ?, payment_status = 'PAID', issued_at = NOW() WHERE invoice_id = ?";
					try (PreparedStatement ps = conn.prepareStatement(sqlUpdateInv)) {
						ps.setDouble(1, tienPhong);
						ps.setDouble(2, phuThu);
						ps.setDouble(3, giamGia);
						ps.setDouble(4, tongTien);
						ps.setString(5, phuongThuc);
						ps.setInt(6, invoiceId);
						ps.executeUpdate();
					}
				} else {
					String sqlInsertInv = "INSERT INTO invoices (booking_id, room_amount, surcharge, discount, total_amount, payment_method, payment_status, issued_at) "
							+ "VALUES (?, ?, ?, ?, ?, ?, 'PAID', NOW())";
					try (PreparedStatement ps = conn.prepareStatement(sqlInsertInv)) {
						ps.setInt(1, bookingId);
						ps.setDouble(2, tienPhong);
						ps.setDouble(3, phuThu);
						ps.setDouble(4, giamGia);
						ps.setDouble(5, tongTien);
						ps.setString(6, phuongThuc);
						ps.executeUpdate();
					}
				}
			}

			// 3. QUAN TRỌNG: Chuyen trang thai phong sang CLEANING (Dang don dep)
			String sqlRoom = "UPDATE rooms SET status = 'CLEANING' WHERE room_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
				ps.setInt(1, roomId);
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
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE bookings SET customer_id = " + t.getIdKhachHang() + ", check_in_date = '"
					+ t.getNgayNhanPhong() + "', check_out_date = '" + t.getNgayTraPhong() + "', status = '"
					+ t.getTrangThai() + "', note = '" + t.getGhiChu() + "' WHERE booking_id = "
					+ t.getIdDatPhong();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa dat phong
	public int xoa(bookings t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "DELETE FROM bookings WHERE booking_id = " + t.getIdDatPhong();
			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach dat phong (SQL JOIN voi customers, booking_rooms, rooms, room_types de lay CCCD, tang va so nguoi)
	public ArrayList<bookings> xuat() {
		ArrayList<bookings> ds = new ArrayList<>();

		String sql = "SELECT b.booking_id, b.customer_id, b.check_in_date, b.check_out_date, b.status, b.created_at, b.note, "
				+ "c.identity_no AS cccd, "
				+ "r.room_number, r.description AS room_desc, "
				+ "COALESCE(rt.capacity, 1) AS capacity "
				+ "FROM bookings b "
				+ "LEFT JOIN customers c ON b.customer_id = c.customer_id "
				+ "LEFT JOIN booking_rooms br ON b.booking_id = br.booking_id "
				+ "LEFT JOIN rooms r ON br.room_id = r.room_id "
				+ "LEFT JOIN room_types rt ON r.room_type_id = rt.room_type_id "
				+ "ORDER BY b.booking_id DESC";

		try (Connection conn = DBConnection.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				int bookingId = rs.getInt("booking_id");
				int customerId = rs.getInt("customer_id");
				Date checkIn = rs.getDate("check_in_date");
				Date checkOut = rs.getDate("check_out_date");
				String status = rs.getString("status");
				Date createdAt = rs.getDate("created_at");
				String note = rs.getString("note");

				String cccd = rs.getString("cccd");
				String roomNum = rs.getString("room_number");
				String roomDesc = rs.getString("room_desc");
				int capacity = rs.getInt("capacity");

				// Tinh tang tu description (vi du 'Tang 2') hoac chu so dau tien cua so phong (vi du '201' -> Tầng 2)
				String tangHienThi = "";
				if (roomDesc != null && roomDesc.toLowerCase().contains("tang")) {
					String num = roomDesc.replaceAll("[^0-9]", "");
					if (!num.isEmpty()) {
						tangHienThi = "Tầng " + num;
					}
				}
				if (tangHienThi.isEmpty() && roomNum != null && !roomNum.trim().isEmpty()) {
					char firstChar = roomNum.trim().charAt(0);
					if (Character.isDigit(firstChar)) {
						tangHienThi = "Tầng " + firstChar;
					}
				}

				bookings t = new bookings(bookingId, customerId, checkIn, checkOut, status, createdAt, note,
						cccd != null ? cccd : "", tangHienThi, capacity, roomNum != null ? roomNum : "");
				ds.add(t);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem dat phong theo ma
	public bookings timKiem(bookings t) {
		bookings kq = null;

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM bookings WHERE booking_id = " + t.getIdDatPhong();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new bookings(rs.getInt("booking_id"), rs.getInt("customer_id"), rs.getDate("check_in_date"),
						rs.getDate("check_out_date"), rs.getString("status"), rs.getDate("created_at"),
						rs.getString("note"));
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}

	public int capNhatTrangThai(int id, String trangThai) {
	    String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, trangThai);
	        ps.setInt(2, id);

	        return ps.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}
