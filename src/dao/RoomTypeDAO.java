
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.room_types;

public class RoomTypeDAO {

	// Them loai phong
	public int them(room_types t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			room_types p = timKiem(t);

			if (p == null) {
				String sql = "INSERT INTO room_types " + "(type_name, capacity, description) VALUES ('"
						+ t.getTenLoaiPhong() + "', " + t.getSucChua() + ", '" + t.getMoTa() + "')";

				int kq = st.executeUpdate(sql);
				System.out.println("Da them loai phong!");
				conn.close();
				return kq;
			} else {
				System.out.println("Ma loai phong da ton tai!");
				conn.close();
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua loai phong
	public int sua(room_types t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE room_types SET " + "type_name = '" + t.getTenLoaiPhong() + "', " + "capacity = "
					+ t.getSucChua() + ", " + "description = '" + t.getMoTa() + "' " + "WHERE room_type_id = "
					+ t.getMaLoaiPhong();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa loai phong
	public int xoa(room_types t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "DELETE FROM room_types WHERE room_type_id = " + t.getMaLoaiPhong();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach
	public ArrayList<room_types> xuat() {
		ArrayList<room_types> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM room_types";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				room_types t = new room_types(rs.getInt("room_type_id"), rs.getString("type_name"),
						rs.getInt("capacity"), rs.getString("description"));

				ds.add(t);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem theo ten loai phong
	public room_types timKiem(room_types t) {
		room_types kq = null;

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM room_types WHERE type_name = '" + t.getTenLoaiPhong() + "'";

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new room_types(rs.getInt("room_type_id"), rs.getString("type_name"), rs.getInt("capacity"),
						rs.getString("description"));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
