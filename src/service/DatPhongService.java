package service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import dao.DatPhongDAO;
import model.DatPhong;

public class DatPhongService {

    DatPhongDAO truyCapDuLieu = new DatPhongDAO();

    // Them dat phong
    public int them(DatPhong bangHienTai) {
        if (bangHienTai.getIdKhachHang() <= 0
                || bangHienTai.getMaPhong() <= 0
                || bangHienTai.getNgayNhanPhong() == null
                || bangHienTai.getNgayTraPhong() == null
                || bangHienTai.getNgayNhanPhong().toLocalDate().isBefore(LocalDate.now())) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        if (!bangHienTai.getNgayTraPhong().after(bangHienTai.getNgayNhanPhong())) {
            System.out.println("Ngay tra phong phai sau ngay nhan phong!");
            return 0;
        }

        if (!"BOOKED".equals(bangHienTai.getTrangThai())) {
            System.out.println("Don dat phong moi phai bat dau o trang thai BOOKED!");
            return 0;
        }

        return truyCapDuLieu.them(bangHienTai);
    }

    // Dat phong nhanh kem phong va tao hoa don
    public int datPhongNhanh(int maKhachHang, Date nhanPhong, Date traPhong, String trangThai, String ghiChu, int maPhong, double giaMoiDem) {
        return datPhongNhanh(maKhachHang, nhanPhong, traPhong, trangThai, ghiChu, maPhong, giaMoiDem, 1);
    }

    public int datPhongNhanh(int maKhachHang, Date nhanPhong, Date traPhong, String trangThai, String ghiChu, int maPhong,
            double giaMoiDem, int soNguoi) {
        if (nhanPhong == null || nhanPhong.toLocalDate().isBefore(LocalDate.now())
                || traPhong == null || !traPhong.after(nhanPhong)) {
            System.out.println("Ngay nhan phong phai tu hom nay tro di va ngay tra phai sau ngay nhan!");
            return 0;
        }
        return truyCapDuLieu.datPhongNhanh(maKhachHang, nhanPhong, traPhong, trangThai, ghiChu, maPhong, giaMoiDem, soNguoi);
    }

    // Nhan phong (Check-in)
    public boolean nhanPhong(int maDatPhong) {
        return truyCapDuLieu.nhanPhong(maDatPhong);
    }

    // Tra phong va thanh toan, chuyen phong sang CLEANING
    public boolean traPhongVaThanhToan(int maDatPhong, int maPhong, double tienPhong, double phuThu, double giamGia, double chungTien, String phuongThuc) {
        return truyCapDuLieu.traPhongVaThanhToan(maDatPhong, maPhong, tienPhong, phuThu, giamGia, chungTien, phuongThuc);
    }

    // Huy dat phong
    public boolean huyDatPhong(int maDatPhong) {
        return truyCapDuLieu.huyDatPhong(maDatPhong);
    }

    // Sua dat phong
    public int sua(DatPhong bangHienTai) {
        if (bangHienTai.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        if (bangHienTai.getIdKhachHang() <= 0
                || bangHienTai.getMaPhong() <= 0
                || bangHienTai.getNgayNhanPhong() == null
                || bangHienTai.getNgayTraPhong() == null
                || bangHienTai.getNgayNhanPhong().toLocalDate().isBefore(LocalDate.now())
                || !bangHienTai.getNgayTraPhong().after(bangHienTai.getNgayNhanPhong())) {
            System.out.println("Thong tin dat phong khong hop le!");
            return 0;
        }

        return truyCapDuLieu.sua(bangHienTai);
    }

    public boolean phongBiDatTrung(int maPhong, Date ngayNhan, Date ngayTra, int maDatPhongBoQua) throws Exception {
        return truyCapDuLieu.phongBiDatTrung(maPhong, ngayNhan, ngayTra, maDatPhongBoQua);
    }

    // Xoa dat phong
    public int xoa(DatPhong bangHienTai) {
        if (bangHienTai.getIdDatPhong() <= 0) {
            System.out.println("Ma dat phong khong hop le!");
            return 0;
        }

        return truyCapDuLieu.xoa(bangHienTai);
    }

    // Xuat danh sach dat phong
    public ArrayList<DatPhong> xuat() {
        return truyCapDuLieu.xuat();
    }

    // Tim dat phong theo ma
    public DatPhong timKiem(DatPhong bangHienTai) {
        if (bangHienTai.getIdDatPhong() <= 0) {
            return null;
        }

        return truyCapDuLieu.timKiem(bangHienTai);
    }

    public int capNhatTrangThai(int ma, String trangThai) {
        return truyCapDuLieu.capNhatTrangThai(ma, trangThai);
    }
}
