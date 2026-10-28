package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.room_types;

public class RoomTypeDAO {

	// Them loai phong
	public int them(room_types t) {
		room_types p = timKiem(t);
		if (p != null) {
			System.out.println("Tên loại phòng đã tồn tại!");
			return 0;
		}

		String sql = "INSERT INTO loai_phong (ten_loai_phong, suc_chua, mo_ta) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, t.getTenLoaiPhong().trim());
			ps.setInt(2, t.getSucChua());
			ps.setString(3, t.getMoTa());

			int kq = ps.executeUpdate();
			if (kq > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						t.setMaLoaiPhong(rs.getInt(1));
					}
				}
			}
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua loai phong
	public int sua(room_types t) {
		String sql = "UPDATE loai_phong SET ten_loai_phong = ?, suc_chua = ?, mo_ta = ? WHERE ma_loai_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, t.getTenLoaiPhong().trim());
			ps.setInt(2, t.getSucChua());
			ps.setString(3, t.getMoTa());
			ps.setInt(4, t.getMaLoaiPhong());

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa loai phong
	public int xoa(room_types t) {
		String sql = "DELETE FROM loai_phong WHERE ma_loai_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, t.getMaLoaiPhong());
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach
	public ArrayList<room_types> xuat() {
		ArrayList<room_types> ds = new ArrayList<>();
		String sql = "SELECT * FROM loai_phong ORDER BY ma_loai_phong ASC";

		try (Connection conn = DBConnection.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				room_types t = new room_types(
						rs.getInt("ma_loai_phong"),
						rs.getString("ten_loai_phong"),
						rs.getInt("suc_chua"),
						rs.getString("mo_ta")
				);
				ds.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem theo ten loai phong
	public room_types timKiem(room_types t) {
		if (t == null || t.getTenLoaiPhong() == null || t.getTenLoaiPhong().trim().isEmpty()) {
			return null;
		}

		String sql = "SELECT * FROM loai_phong WHERE ten_loai_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, t.getTenLoaiPhong().trim());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new room_types(
							rs.getInt("ma_loai_phong"),
							rs.getString("ten_loai_phong"),
							rs.getInt("suc_chua"),
							rs.getString("mo_ta")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
