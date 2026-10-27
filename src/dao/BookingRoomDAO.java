
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookingRoomDAO {

	// Them phong vao dat phong
	public int them(int maDatPhong, int maPhong, float giaMoiDem) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "INSERT INTO booking_rooms " + "(booking_id, room_id, price_per_night) VALUES (" + maDatPhong
					+ ", " + maPhong + ", " + giaMoiDem + ")";

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua gia moi dem
	public int sua(int maDatPhong, int maPhong, float giaMoiDem) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE booking_rooms SET price_per_night = " + giaMoiDem + " WHERE booking_id = " + maDatPhong
					+ " AND room_id = " + maPhong;

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa phong khoi dat phong
	public int xoa(int maDatPhong, int maPhong) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "DELETE FROM booking_rooms " + "WHERE booking_id = " + maDatPhong + " AND room_id = "
					+ maPhong;

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach phong da dat
	public ArrayList<String[]> xuat() {
		ArrayList<String[]> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM booking_rooms";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String[] dong = { rs.getString("booking_id"), rs.getString("room_id"),
						rs.getString("price_per_night") };

				ds.add(dong);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim phong theo ma dat phong va ma phong
	public String[] timKiem(int maDatPhong, int maPhong) {
		String[] kq = null;

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM booking_rooms " + "WHERE booking_id = " + maDatPhong + " AND room_id = "
					+ maPhong;

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new String[] { rs.getString("booking_id"), rs.getString("room_id"),
						rs.getString("price_per_night") };
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
