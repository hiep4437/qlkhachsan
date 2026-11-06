package service;

import java.util.ArrayList;
import dao.KhachHangDAO;
import model.KhachHang;

public class KhachHangService {

    KhachHangDAO truyCapDuLieu = new KhachHangDAO();

    // Them khach hang
    public int them(KhachHang bangHienTai) {
        if (bangHienTai.getNameKhachHang() == null || bangHienTai.getNameKhachHang().trim().isEmpty()) {
            System.out.println("Ho ten khong duoc de trong!");
            return 0;
        }

        if (bangHienTai.getSoCCCD() == null || bangHienTai.getSoCCCD().trim().isEmpty()) {
            System.out.println("So CCCD khong duoc de trong!");
            return 0;
        }

        if (bangHienTai.getSoDienThoai() == null || bangHienTai.getSoDienThoai().trim().isEmpty()) {
            System.out.println("So dien thoai khong duoc de trong!");
            return 0;
        }

        return truyCapDuLieu.them(bangHienTai);
    }

    // Sua khach hang
    public int sua(KhachHang bangHienTai) {
        if (bangHienTai.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        if (bangHienTai.getNameKhachHang() == null || bangHienTai.getNameKhachHang().trim().isEmpty()) {
            System.out.println("Ho ten khong duoc de trong!");
            return 0;
        }

        if (bangHienTai.getSoCCCD() == null || bangHienTai.getSoCCCD().trim().isEmpty()) {
            System.out.println("So CCCD khong duoc de trong!");
            return 0;
        }

        if (bangHienTai.getSoDienThoai() == null || bangHienTai.getSoDienThoai().trim().isEmpty()) {
            System.out.println("So dien thoai khong duoc de trong!");
            return 0;
        }

        return truyCapDuLieu.sua(bangHienTai);
    }

    // Xoa khach hang
    public int xoa(KhachHang bangHienTai) {
        if (bangHienTai.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        return truyCapDuLieu.xoa(bangHienTai);
    }

    // Xuat danh sach khach hang
    public ArrayList<KhachHang> xuat() {
        return truyCapDuLieu.xuat();
    }

    // Dem so lan dat phong
    public int demSoLanDatPhong(int maKhachHang) {
        return truyCapDuLieu.demSoLanDatPhong(maKhachHang);
    }

    // Tim khach hang
    public KhachHang timKiem(KhachHang bangHienTai) {
        if (bangHienTai == null) {
            return null;
        }

        return truyCapDuLieu.timKiem(bangHienTai);
    }
}
