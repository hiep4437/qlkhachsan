package service;

import java.util.ArrayList;
import dao.PhongDAO;
import model.ThongTinPhongDangO;
import model.Phong;

public class PhongService {

	PhongDAO dao = new PhongDAO();

	// Them phong
	public int them(Phong t) {
		if (t.getSoPhong() == null || t.getSoPhong().isEmpty()) {
			return 0;
		}

		if (t.getGiaMoiDem() <= 0) {
			return 0;
		}

		return dao.them(t);
	}

	// Sua phong
	public int sua(Phong t) {
		if (t.getMaPhong() <= 0) {
			return 0;
		}

		if (t.getSoPhong() == null || t.getSoPhong().isEmpty()) {
			return 0;
		}

		if (t.getGiaMoiDem() <= 0) {
			return 0;
		}

		return dao.sua(t);
	}

	// Xoa phong
	public int xoa(Phong r) {
		if (r.getMaPhong() <= 0) {
			return 0;
		}

		return dao.xoa(r);
	}

	// Xuat danh sach phong
	public ArrayList<Phong> xuat() {
		return dao.xuat();
	}

	// Tim kiem phong
	public Phong timKiem(Phong t) {
		if (t.getSoPhong() == null || t.getSoPhong().isEmpty()) {
			return null;
		}

		return dao.timKiem(t);
	}

	// Doi trang thai phong
	public int doiTrangThai(int roomId, String trangThai) {
		return dao.doiTrangThai(roomId, trangThai);
	}

	// Lay thong tin khach dang o
	public ThongTinPhongDangO layThongTinKhachDangO(int roomId) {
		return dao.layThongTinKhachDangO(roomId);
	}
}
