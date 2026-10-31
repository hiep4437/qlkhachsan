package service;

import java.sql.Date;
import java.util.ArrayList;
import dao.DatPhongDAO;
import model.DatPhong;

public class DatPhongService {

    DatPhongDAO dao = new DatPhongDAO();

    // Them dat phong
    public int them(DatPhong t) {
        if (t.getIdKhachHang() <= 0
                || t.getMaPhong() <= 0
                || t.getNgayNhanPhong() == null
                || t.getNgayTraPhong() == null) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        if (!t.getNgayTraPhong().after(t.getNgayNhanPhong())) {
            System.out.println("Ngay tra phong phai sau ngay nhan phong!");
            return 0;
        }

        if (!"BOOKED".equals(t.getTrangThai())) {
            System.out.println("Don dat phong moi phai bat dau o trang thai BOOKED!");
            return 0;
        }

        return dao.them(t);
    }

    // Dat phong nhanh kem phong va tao hoa don
    public int datPhongNhanh(int customerId, Date checkIn, Date checkOut, String status, String note, int roomId, double pricePerNight) {
        return dao.datPhongNhanh(customerId, checkIn, checkOut, status, note, roomId, pricePerNight);
    }

    public int datPhongNhanh(int customerId, Date checkIn, Date checkOut, String status, String note, int roomId,
            double pricePerNight, int soNguoi) {
        return dao.datPhongNhanh(customerId, checkIn, checkOut, status, note, roomId, pricePerNight, soNguoi);
    }

    // Nhan phong (Check-in)
    public boolean nhanPhong(int bookingId) {
        return dao.nhanPhong(bookingId);
    }

    // Tra phong va thanh toan, chuyen phong sang CLEANING
    public boolean traPhongVaThanhToan(int bookingId, int roomId, double tienPhong, double phuThu, double giamGia, double tongTien, String phuongThuc) {
        return dao.traPhongVaThanhToan(bookingId, roomId, tienPhong, phuThu, giamGia, tongTien, phuongThuc);
    }

    // Huy dat phong
    public boolean huyDatPhong(int bookingId) {
        return dao.huyDatPhong(bookingId);
    }

    // Sua dat phong
    public int sua(DatPhong t) {
        if (t.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        if (t.getIdKhachHang() <= 0
                || t.getMaPhong() <= 0
                || t.getNgayNhanPhong() == null
                || t.getNgayTraPhong() == null
                || !t.getNgayTraPhong().after(t.getNgayNhanPhong())) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        return dao.sua(t);
    }

    public boolean phongBiDatTrung(int roomId, Date ngayNhan, Date ngayTra, int maDatPhongBoQua) throws Exception {
        return dao.phongBiDatTrung(roomId, ngayNhan, ngayTra, maDatPhongBoQua);
    }

    // Xoa dat phong
    public int xoa(DatPhong t) {
        if (t.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        return dao.xoa(t);
    }

    // Xuat danh sach dat phong
    public ArrayList<DatPhong> xuat() {
        return dao.xuat();
    }

    // Tim dat phong theo ma
    public DatPhong timKiem(DatPhong t) {
        if (t.getIdDatPhong() <= 0) {
            return null;
        }

        return dao.timKiem(t);
    }

    public int capNhatTrangThai(int id, String trangThai) {
        return dao.capNhatTrangThai(id, trangThai);
    }
}
