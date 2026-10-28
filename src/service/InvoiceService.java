package service;

import java.util.ArrayList;
import dao.InvoiceDAO;
import model.invoices;

public class InvoiceService {

	InvoiceDAO dao = new InvoiceDAO();

	// Them hoa don
	public int them(invoices t) {
		if (t.getIdDatPhong() <= 0) {
			System.out.println("Ma dat phong khong hop le!");
			return 0;
		}

		if (t.getTienPhong() < 0 || t.getPhuThu() < 0 || t.getGiamGia() < 0) {
			System.out.println("Cac khoan tien phai >= 0!");
			return 0;
		}

		return dao.them(t);
	}

	// Sua hoa don
	public int sua(invoices t) {
		if (t.getIdHoaDon() <= 0) {
			System.out.println("Ma hoa don khong hop le!");
			return 0;
		}

		if (t.getIdDatPhong() <= 0) {
			System.out.println("Ma dat phong khong hop le!");
			return 0;
		}

		if (t.getTienPhong() < 0 || t.getPhuThu() < 0 || t.getGiamGia() < 0) {
			System.out.println("Cac khoan tien phai >= 0!");
			return 0;
		}

		return dao.sua(t);
	}

	// Xoa hoa don
	public int xoa(invoices t) {
		if (t.getIdHoaDon() <= 0) {
			System.out.println("Ma hoa don khong hop le!");
			return 0;
		}

		return dao.xoa(t);
	}

	// Xuat danh sach hoa don
	public ArrayList<invoices> xuat() {
		return dao.xuat();
	}

	// Tim hoa don theo ma
	public invoices timKiem(invoices t) {
		if (t.getIdHoaDon() <= 0) {
			return null;
		}

		return dao.timKiem(t);
	}

	// Tim theo dat phong
	public invoices timTheoDatPhong(int maDatPhong) {
		if (maDatPhong <= 0) return null;
		return dao.timTheoDatPhong(maDatPhong);
	}

	// Cap nhat trang thai thanh toan
	public int capNhatTrangThai(int maHoaDon, String trangThai, String phuongThuc) {
		if (maHoaDon <= 0) return 0;
		return dao.capNhatTrangThai(maHoaDon, trangThai, phuongThuc);
	}
}
