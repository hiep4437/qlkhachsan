package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookingRoomDAO {

	// Them phong vao dat phong
	public int them(int maDatPhong, int maPhong, float giaMoiDem) {
		String sql = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDatPhong);
			ps.setInt(2, maPhong);
			ps.setFloat(3, giaMoiDem);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua gia moi dem
	public int sua(int maDatPhong, int maPhong, float giaMoiDem) {
		String sql = "UPDATE chi_tiet_dat_phong SET gia_moi_dem = ? WHERE ma_dat_phong = ? AND ma_phong = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setFloat(1, giaMoiDem);
			ps.setInt(2, maDatPhong);
			ps.setInt(3, maPhong);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa phong khoi dat phong
	public int xoa(int maDatPhong, int maPhong) {
		String sql = "DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong = ? AND ma_phong = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDatPhong);
			ps.setInt(2, maPhong);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach phong da dat
	public ArrayList<String[]> xuat() {
		ArrayList<String[]> ds = new ArrayList<>();
		String sql = "SELECT ma_dat_phong, ma_phong, gia_moi_dem FROM chi_tiet_dat_phong";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String[] dong = {
					String.valueOf(rs.getInt("ma_dat_phong")),
					String.valueOf(rs.getInt("ma_phong")),
					String.valueOf(rs.getBigDecimal("gia_moi_dem"))
				};
				ds.add(dong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim phong theo ma dat phong va ma phong
	public String[] timKiem(int maDatPhong, int maPhong) {
		String[] kq = null;
		String sql = "SELECT ma_dat_phong, ma_phong, gia_moi_dem FROM chi_tiet_dat_phong WHERE ma_dat_phong = ? AND ma_phong = ?";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDatPhong);
			ps.setInt(2, maPhong);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					kq = new String[] {
						String.valueOf(rs.getInt("ma_dat_phong")),
						String.valueOf(rs.getInt("ma_phong")),
						String.valueOf(rs.getBigDecimal("gia_moi_dem"))
					};
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
