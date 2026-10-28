package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.ThongTinPhongDangO;
import model.rooms;

public class RoomDAO {

	// Them phong
	public int them(rooms t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			rooms p = timKiem(t);

			if (p == null) {
				String sql = "INSERT INTO rooms (room_number, room_type_id, price_per_night, status, description) "
						+ "VALUES ('" + t.getSoPhong() + "', " + t.getMaLoaiPhong() + ", " + t.getGiaMoiDem() + ", '"
						+ t.getTrangThai() + "', '" + t.getMoTa() + "')";

				int kq = st.executeUpdate(sql);
				System.out.println("Da them phong: " + t.getSoPhong());
				conn.close();
				return kq;
			} else {
				System.out.println("Ma phong da ton tai!");
				conn.close();
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua phong
	public int sua(rooms t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE rooms SET room_number = '" + t.getSoPhong() + "', room_type_id = "
					+ t.getMaLoaiPhong() + ", price_per_night = " + t.getGiaMoiDem() + ", status = '"
					+ t.getTrangThai() + "', description = '" + t.getMoTa() + "' WHERE room_id = "
					+ t.getMaPhong();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Doi trang thai phong
	public int doiTrangThai(int roomId, String trangThai) {
		String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, trangThai);
			ps.setInt(2, roomId);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa phong
	public int xoa(rooms r) {
	    try {
	        Connection con = DBConnection.getConnection();
	        Statement st = con.createStatement();

	        String sql = "DELETE FROM rooms WHERE room_id = " + r.getMaPhong();
	        int kq = st.executeUpdate(sql);

	        st.close();
	        con.close();
	        return kq;
	    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
	        JOptionPane.showMessageDialog(
	            null,
	            "Không thể xóa phòng vì phòng đã có dữ liệu đặt phòng!"
	        );
	        return 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	// Xuat danh sach phong (kem ten loai phong)
	public ArrayList<rooms> xuat() {
		ArrayList<rooms> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT r.*, COALESCE(rt.type_name, 'Phòng tiêu chuẩn') as type_name "
					+ "FROM rooms r "
					+ "LEFT JOIN room_types rt ON r.room_type_id = rt.room_type_id "
					+ "ORDER BY r.room_number ASC";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				rooms t = new rooms(
					rs.getInt("room_id"),
					rs.getString("room_number"),
					rs.getInt("room_type_id"),
					rs.getFloat("price_per_night"),
					rs.getString("status"),
					rs.getString("description"),
					rs.getString("type_name")
				);
				ds.add(t);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem phong theo so phong
	public rooms timKiem(rooms t) {
		rooms kq = null;

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT r.*, COALESCE(rt.type_name, 'Phòng tiêu chuẩn') as type_name "
					+ "FROM rooms r "
					+ "LEFT JOIN room_types rt ON r.room_type_id = rt.room_type_id "
					+ "WHERE r.room_number = '" + t.getSoPhong() + "'";

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new rooms(
					rs.getInt("room_id"),
					rs.getString("room_number"),
					rs.getInt("room_type_id"),
					rs.getFloat("price_per_night"),
					rs.getString("status"),
					rs.getString("description"),
					rs.getString("type_name")
				);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}

	// Lay thong tin khach va dat phong dang o
	public ThongTinPhongDangO layThongTinKhachDangO(int roomId) {
		String sql = "SELECT b.booking_id, b.customer_id, c.full_name, c.identity_no, c.phone, "
				+ "b.check_in_date, b.check_out_date, b.status as b_status, b.note, "
				+ "br.price_per_night, "
				+ "i.invoice_id, i.room_amount, i.surcharge, i.discount, i.total_amount, i.payment_method, i.payment_status "
				+ "FROM bookings b "
				+ "JOIN booking_rooms br ON b.booking_id = br.booking_id "
				+ "JOIN customers c ON b.customer_id = c.customer_id "
				+ "LEFT JOIN invoices i ON b.booking_id = i.booking_id "
				+ "WHERE br.room_id = ? "
				+ "ORDER BY (CASE WHEN b.status = 'CHECKED_IN' THEN 1 WHEN b.status = 'BOOKED' THEN 2 ELSE 3 END), b.booking_id DESC "
				+ "LIMIT 1";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, roomId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ThongTinPhongDangO info = new ThongTinPhongDangO();
					info.setBookingId(rs.getInt("booking_id"));
					info.setCustomerId(rs.getInt("customer_id"));
					info.setTenKhachHang(rs.getString("full_name"));
					info.setCccd(rs.getString("identity_no"));
					info.setSoDienThoai(rs.getString("phone"));
					info.setNgayNhan(rs.getDate("check_in_date"));
					info.setNgayTra(rs.getDate("check_out_date"));
					info.setBookingStatus(rs.getString("b_status"));
					info.setGhiChu(rs.getString("note"));
					info.setGiaMoiDem(rs.getDouble("price_per_night"));
					info.setInvoiceId(rs.getInt("invoice_id"));
					info.setTienPhong(rs.getDouble("room_amount"));
					info.setPhuThu(rs.getDouble("surcharge"));
					info.setGiamGia(rs.getDouble("discount"));
					info.setTongTien(rs.getDouble("total_amount"));
					info.setPaymentMethod(rs.getString("payment_method"));
					info.setPaymentStatus(rs.getString("payment_status"));
					return info;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
