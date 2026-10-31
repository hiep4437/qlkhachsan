package service;

import java.sql.Date;
import java.util.ArrayList;
import dao.BookingDAO;
import model.bookings;

public class BookingService {

    BookingDAO dao = new BookingDAO();

    // Them dat phong
    public int them(bookings t) {
        if (t.getIdKhachHang() <= 0
                || t.getNgayNhanPhong() == null
                || t.getNgayTraPhong() == null) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        if (!t.getNgayTraPhong().after(t.getNgayNhanPhong())) {
            System.out.println("Ngay tra phong phai sau ngay nhan phong!");
            return 0;
        }

        if (t.getTrangThai() == null
                || !(t.getTrangThai().equals("BOOKED")
                || t.getTrangThai().equals("CHECKED_IN")
                || t.getTrangThai().equals("CHECKED_OUT")
                || t.getTrangThai().equals("CANCELLED"))) {
            System.out.println("Trang thai dat phong khong hop le!");
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
    public int sua(bookings t) {
        if (t.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        if (t.getIdKhachHang() <= 0
                || t.getNgayNhanPhong() == null
                || t.getNgayTraPhong() == null
                || !t.getNgayTraPhong().after(t.getNgayNhanPhong())) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        return dao.sua(t);
    }

    // Xoa dat phong
    public int xoa(bookings t) {
        if (t.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        return dao.xoa(t);
    }

    // Xuat danh sach dat phong
    public ArrayList<bookings> xuat() {
        return dao.xuat();
    }

    // Tim dat phong theo ma
    public bookings timKiem(bookings t) {
        if (t.getIdDatPhong() <= 0) {
            return null;
        }

        return dao.timKiem(t);
    }

    public int capNhatTrangThai(int id, String trangThai) {
        return dao.capNhatTrangThai(id, trangThai);
    }
}
