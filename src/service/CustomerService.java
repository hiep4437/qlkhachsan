package service;

import java.util.ArrayList;
import dao.CustomerDAO;
import model.customers;

public class CustomerService {

    CustomerDAO dao = new CustomerDAO();

    // Them khach hang
    public int them(customers t) {
        if (t.getNameKhachHang() == null
                || t.getNameKhachHang().trim().isEmpty()
                || t.getSoDienThoai() == null
                || t.getSoDienThoai().trim().isEmpty()) {
            System.out.println("Vui long nhap ten va so dien thoai!");
            return 0;
        }

        return dao.them(t);
    }

    // Sua khach hang
    public int sua(customers t) {
        if (t.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        if (t.getNameKhachHang() == null
                || t.getNameKhachHang().trim().isEmpty()
                || t.getSoDienThoai() == null
                || t.getSoDienThoai().trim().isEmpty()) {
            System.out.println("Vui long nhap ten va so dien thoai!");
            return 0;
        }

        return dao.sua(t);
    }

    // Xoa khach hang
    public int xoa(customers t) {
        if (t.getIdKhachHang() <= 0) {
            System.out.println("Ma khach hang khong hop le!");
            return 0;
        }

        return dao.xoa(t);
    }

    // Xuat danh sach khach hang
    public ArrayList<customers> xuat() {
        return dao.xuat();
    }

    // Tim khach hang theo CCCD
    public customers timKiem(customers t) {
        if (t.getSoCCCD() == null || t.getSoCCCD().trim().isEmpty()) {
            return null;
        }

        return dao.timKiem(t);
    }
}
