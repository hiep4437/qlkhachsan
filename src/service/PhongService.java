package service;

import java.util.ArrayList;
import dao.PhongDAO;
import model.ThongTinPhongDangO;
import model.Phong;

public class PhongService {

	PhongDAO truyCapDuLieu = new PhongDAO();

	// Them phong
	public int them(Phong bangHienTai) {
		if (bangHienTai.getSoPhong() == null || bangHienTai.getSoPhong().isEmpty()) {
			return 0;
		}

		if (bangHienTai.getGiaMoiDem() <= 0) {
			return 0;
		}

		return truyCapDuLieu.them(bangHienTai);
	}

	// Sua phong
	public int sua(Phong bangHienTai) {
		if (bangHienTai.getMaPhong() <= 0) {
			return 0;
		}

		if (bangHienTai.getSoPhong() == null || bangHienTai.getSoPhong().isEmpty()) {
			return 0;
		}

		if (bangHienTai.getGiaMoiDem() <= 0) {
			return 0;
		}

		return truyCapDuLieu.sua(bangHienTai);
	}

	// Xoa phong
	public int xoa(Phong hangHienTai) {
		if (hangHienTai.getMaPhong() <= 0) {
			return 0;
		}

		return truyCapDuLieu.xoa(hangHienTai);
	}

	// Xuat danh sach phong
	public ArrayList<Phong> xuat() {
		return truyCapDuLieu.xuat();
	}

	// Tim kiem phong
	public Phong timKiem(Phong bangHienTai) {
		if (bangHienTai.getSoPhong() == null || bangHienTai.getSoPhong().isEmpty()) {
			return null;
		}

		return truyCapDuLieu.timKiem(bangHienTai);
	}

	// Doi trang thai phong
	public int doiTrangThai(int maPhong, String trangThai) {
		return truyCapDuLieu.doiTrangThai(maPhong, trangThai);
	}

	// Lay thong tin khach dang o
	public ThongTinPhongDangO layThongTinKhachDangO(int maPhong) {
		return truyCapDuLieu.layThongTinKhachDangO(maPhong);
	}
}
