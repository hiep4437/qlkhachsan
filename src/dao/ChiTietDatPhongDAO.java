package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChiTietDatPhongDAO {

	// Them phong vao dat phong
	public int them(int maDatPhong, int maPhong, float giaMoiDem) {
		String cauLenhSQL = "INSERT INTO chi_tiet_dat_phong (ma_dat_phong, ma_phong, gia_moi_dem) VALUES (?, ?, ?)";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maDatPhong);
			luongIn.setInt(2, maPhong);
			luongIn.setFloat(3, giaMoiDem);
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Sua gia moi dem
	public int sua(int maDatPhong, int maPhong, float giaMoiDem) {
		String cauLenhSQL = "UPDATE chi_tiet_dat_phong SET gia_moi_dem = ? WHERE ma_dat_phong = ? AND ma_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setFloat(1, giaMoiDem);
			luongIn.setInt(2, maDatPhong);
			luongIn.setInt(3, maPhong);
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xoa phong khoi dat phong
	public int xoa(int maDatPhong, int maPhong) {
		String cauLenhSQL = "DELETE FROM chi_tiet_dat_phong WHERE ma_dat_phong = ? AND ma_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maDatPhong);
			luongIn.setInt(2, maPhong);
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach phong da dat
	public ArrayList<String[]> xuat() {
		ArrayList<String[]> danhSach = new ArrayList<>();
		String cauLenhSQL = "SELECT ma_dat_phong, ma_phong, gia_moi_dem FROM chi_tiet_dat_phong";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL);
			 ResultSet ketQuaTruyVan = luongIn.executeQuery()) {

			while (ketQuaTruyVan.next()) {
				String[] hang = {
					String.valueOf(ketQuaTruyVan.getInt("ma_dat_phong")),
					String.valueOf(ketQuaTruyVan.getInt("ma_phong")),
					String.valueOf(ketQuaTruyVan.getBigDecimal("gia_moi_dem"))
				};
				danhSach.add(hang);
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return danhSach;
	}

	// Tim phong theo ma dat phong va ma phong
	public String[] timKiem(int maDatPhong, int maPhong) {
		String[] ketQua = null;
		String cauLenhSQL = "SELECT ma_dat_phong, ma_phong, gia_moi_dem FROM chi_tiet_dat_phong WHERE ma_dat_phong = ? AND ma_phong = ?";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
			 PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maDatPhong);
			luongIn.setInt(2, maPhong);
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					ketQua = new String[] {
						String.valueOf(ketQuaTruyVan.getInt("ma_dat_phong")),
						String.valueOf(ketQuaTruyVan.getInt("ma_phong")),
						String.valueOf(ketQuaTruyVan.getBigDecimal("gia_moi_dem"))
					};
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return ketQua;
	}
}
