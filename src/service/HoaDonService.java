package service;

import java.util.ArrayList;
import dao.HoaDonDAO;
import model.HoaDon;

public class HoaDonService {

	HoaDonDAO truyCapDuLieu = new HoaDonDAO();

	// Them hoa don
	public int them(HoaDon bangHienTai) {
		if (bangHienTai.getIdDatPhong() <= 0) {
			System.out.println("Ma dat phong khong hop le!");
			return 0;
		}

		if (bangHienTai.getTienPhong() < 0 || bangHienTai.getPhuThu() < 0 || bangHienTai.getGiamGia() < 0) {
			System.out.println("Cac khoan tien phai >= 0!");
			return 0;
		}

		return truyCapDuLieu.them(bangHienTai);
	}

	// Sua hoa don
	public int sua(HoaDon bangHienTai) {
		if (bangHienTai.getIdHoaDon() <= 0) {
			System.out.println("Ma hoa don khong hop le!");
			return 0;
		}

		if (bangHienTai.getIdDatPhong() <= 0) {
			System.out.println("Ma dat phong khong hop le!");
			return 0;
		}

		if (bangHienTai.getTienPhong() < 0 || bangHienTai.getPhuThu() < 0 || bangHienTai.getGiamGia() < 0) {
			System.out.println("Cac khoan tien phai >= 0!");
			return 0;
		}

		return truyCapDuLieu.sua(bangHienTai);
	}

	// Xoa hoa don
	public int xoa(HoaDon bangHienTai) {
		if (bangHienTai.getIdHoaDon() <= 0) {
			System.out.println("Ma hoa don khong hop le!");
			return 0;
		}

		return truyCapDuLieu.xoa(bangHienTai);
	}

	// Xuat danh sach hoa don
	public ArrayList<HoaDon> xuat() {
		return truyCapDuLieu.xuat();
	}

	// Tim hoa don theo ma
	public HoaDon timKiem(HoaDon bangHienTai) {
		if (bangHienTai.getIdHoaDon() <= 0) {
			return null;
		}

		return truyCapDuLieu.timKiem(bangHienTai);
	}

	// Tim theo dat phong
	public HoaDon timTheoDatPhong(int maDatPhong) {
		if (maDatPhong <= 0) return null;
		return truyCapDuLieu.timTheoDatPhong(maDatPhong);
	}

	// Cap nhat trang thai thanh toan
	public int capNhatTrangThai(int maHoaDon, String trangThai, String phuongThuc) {
		if (maHoaDon <= 0) return 0;
		return truyCapDuLieu.capNhatTrangThai(maHoaDon, trangThai, phuongThuc);
	}
}
