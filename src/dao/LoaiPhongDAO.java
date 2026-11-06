package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.LoaiPhong;

public class LoaiPhongDAO {

	// Them loai phong
	public int them(LoaiPhong bangHienTai) {
		LoaiPhong khung = timKiem(bangHienTai);
		if (khung != null) {
			System.out.println("Tên loại phòng đã tồn tại!");
			return 0;
		}

		String cauLenhSQL = "INSERT INTO loai_phong (ten_loai_phong, suc_chua, mo_ta) VALUES (?, ?, ?)";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL, Statement.RETURN_GENERATED_KEYS)) {

			luongIn.setString(1, bangHienTai.getTenLoaiPhong().trim());
			luongIn.setInt(2, bangHienTai.getSucChua());
			luongIn.setString(3, bangHienTai.getMoTa());

			int ketQua = luongIn.executeUpdate();
			if (ketQua > 0) {
				try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
					if (ketQuaTruyVan.next()) {
						bangHienTai.setMaLoaiPhong(ketQuaTruyVan.getInt(1));
					}
				}
			}
			return ketQua;
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Sua loai phong
	public int sua(LoaiPhong bangHienTai) {
		String cauLenhSQL = "UPDATE loai_phong SET ten_loai_phong = ?, suc_chua = ?, mo_ta = ? WHERE ma_loai_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setString(1, bangHienTai.getTenLoaiPhong().trim());
			luongIn.setInt(2, bangHienTai.getSucChua());
			luongIn.setString(3, bangHienTai.getMoTa());
			luongIn.setInt(4, bangHienTai.getMaLoaiPhong());

			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xoa loai phong
	public int xoa(LoaiPhong bangHienTai) {
		String cauLenhSQL = "DELETE FROM loai_phong WHERE ma_loai_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setInt(1, bangHienTai.getMaLoaiPhong());
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach
	public ArrayList<LoaiPhong> xuat() {
		ArrayList<LoaiPhong> danhSach = new ArrayList<>();
		String cauLenhSQL = "SELECT * FROM loai_phong ORDER BY ma_loai_phong ASC";

		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     Statement luongTrinh = ketNoi.createStatement();
		     ResultSet ketQuaTruyVan = luongTrinh.executeQuery(cauLenhSQL)) {

			while (ketQuaTruyVan.next()) {
				LoaiPhong bangHienTai = new LoaiPhong(
						ketQuaTruyVan.getInt("ma_loai_phong"),
						ketQuaTruyVan.getString("ten_loai_phong"),
						ketQuaTruyVan.getInt("suc_chua"),
						ketQuaTruyVan.getString("mo_ta")
				);
				danhSach.add(bangHienTai);
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return danhSach;
	}

	// Tim kiem theo ten loai phong
	public LoaiPhong timKiem(LoaiPhong bangHienTai) {
		if (bangHienTai == null || bangHienTai.getTenLoaiPhong() == null || bangHienTai.getTenLoaiPhong().trim().isEmpty()) {
			return null;
		}

		String cauLenhSQL = "SELECT * FROM loai_phong WHERE ten_loai_phong = ?";
		try (Connection ketNoi = KetNoiCSDL.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setString(1, bangHienTai.getTenLoaiPhong().trim());
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					return new LoaiPhong(
							ketQuaTruyVan.getInt("ma_loai_phong"),
							ketQuaTruyVan.getString("ten_loai_phong"),
							ketQuaTruyVan.getInt("suc_chua"),
							ketQuaTruyVan.getString("mo_ta")
					);
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return null;
	}
}
