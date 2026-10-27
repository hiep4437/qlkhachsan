
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.rooms;

public class RoomDAO {

	// Them phong
	public int them(rooms t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			rooms p = timKiem(t);

			if (p == null) {
				String sql = "INSERT INTO rooms " + "(room_number, room_type_id, price_per_night, status, description) "
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

			String sql = "UPDATE rooms SET " + "room_number = '" + t.getSoPhong() + "', " + "room_type_id = "
					+ t.getMaLoaiPhong() + ", " + "price_per_night = " + t.getGiaMoiDem() + ", " + "status = '"
					+ t.getTrangThai() + "', " + "description = '" + t.getMoTa() + "' " + "WHERE room_id = "
					+ t.getMaPhong();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;

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

	        String sql = "DELETE FROM rooms WHERE room_id = "
	                   + r.getMaPhong();

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

	// Xuat danh sach phong
	public ArrayList<rooms> xuat() {
		ArrayList<rooms> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM rooms";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				rooms t = new rooms(rs.getInt("room_id"), rs.getString("room_number"), rs.getInt("room_type_id"),
						rs.getFloat("price_per_night"), rs.getString("status"), rs.getString("description"));

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

			String sql = "SELECT * FROM rooms WHERE room_number = '" + t.getSoPhong() + "'";

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new rooms(rs.getInt("room_id"), rs.getString("room_number"), rs.getInt("room_type_id"),
						rs.getFloat("price_per_night"), rs.getString("status"), rs.getString("description"));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
