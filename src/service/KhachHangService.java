package service;

import java.util.ArrayList;
import dao.KhachHangDAO;
import model.KhachHang;

public class KhachHangService {

    KhachHangDAO dao = new KhachHangDAO();

    // Them khach hang
    public int them(KhachHang t) {
        if (t.getNameKhachHang() == null || t.getNameKhachHang().trim().isEmpty()) {
            System.out.println("Ho ten khong duoc de trong!");
            return 0;
        }

        if (t.getSoCCCD() == null || t.getSoCCCD().trim().isEmpty()) {
            System.out.println("So CCCD khong duoc de trong!");
            return 0;
        }

        if (t.getSoDienThoai() == null || t.getSoDienThoai().trim().isEmpty()) {
            System.out.println("So dien thoai khong duoc de trong!");
            return 0;
        }

        return dao.them(t);
    }

    // Sua khach hang
    public int sua(KhachHang t) {
        if (t.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        if (t.getNameKhachHang() == null || t.getNameKhachHang().trim().isEmpty()) {
            System.out.println("Ho ten khong duoc de trong!");
            return 0;
        }

        if (t.getSoCCCD() == null || t.getSoCCCD().trim().isEmpty()) {
            System.out.println("So CCCD khong duoc de trong!");
            return 0;
        }

        if (t.getSoDienThoai() == null || t.getSoDienThoai().trim().isEmpty()) {
            System.out.println("So dien thoai khong duoc de trong!");
            return 0;
        }

        return dao.sua(t);
    }

    // Xoa khach hang
    public int xoa(KhachHang t) {
        if (t.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        return dao.xoa(t);
    }

    // Xuat danh sach khach hang
    public ArrayList<KhachHang> xuat() {
        return dao.xuat();
    }

    // Dem so lan dat phong
    public int demSoLanDatPhong(int maKhachHang) {
        return dao.demSoLanDatPhong(maKhachHang);
    }

    // Tim khach hang
    public KhachHang timKiem(KhachHang t) {
        if (t == null) {
            return null;
        }

        return dao.timKiem(t);
    }
}
