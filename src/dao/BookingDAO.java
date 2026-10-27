
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.bookings;

public class BookingDAO {

	// Them dat phong
	public int them(bookings t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "INSERT INTO bookings " + "(customer_id, check_in_date, check_out_date, status, note) VALUES ("
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

	// Sua dat phong
	public int sua(bookings t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE bookings SET " + "customer_id = " + t.getIdKhachHang() + ", " + "check_in_date = '"
					+ t.getNgayNhanPhong() + "', " + "check_out_date = '" + t.getNgayTraPhong() + "', " + "status = '"
					+ t.getTrangThai() + "', " + "note = '" + t.getGhiChu() + "' " + "WHERE booking_id = "
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

	// Xuat danh sach dat phong
	public ArrayList<bookings> xuat() {
		ArrayList<bookings> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM bookings";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				bookings t = new bookings(rs.getInt("booking_id"), rs.getInt("customer_id"),
						rs.getDate("check_in_date"), rs.getDate("check_out_date"), rs.getString("status"),
						rs.getDate("created_at"), rs.getString("note"));

				ds.add(t);
			}

			conn.close();
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
	         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, trangThai);
	        ps.setInt(2, id);

	        return ps.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
}
