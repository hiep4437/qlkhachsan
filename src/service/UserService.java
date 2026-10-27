
package service;

import java.util.ArrayList;
import dao.UserDAO;
import model.users;

public class UserService {

    UserDAO dao = new UserDAO();

    // Dang nhap
    public boolean dangNhap(String ten, String matKhau) {
        if (ten == null || ten.isEmpty()
                || matKhau == null || matKhau.isEmpty()) {
            return false;
        }

        users t = new users();
        t.setTenDangNhap(ten);
        t.setMatKhau(matKhau);

        users kq = dao.timKiem(t);

        return kq != null && matKhau.equals(kq.getMatKhau());
    }

    // Them nguoi dung
    public int themNguoiDung(users t) {
        if (t.getTenDangNhap().isEmpty()
                || t.getMatKhau().isEmpty()
                || t.getHoTen().isEmpty()) {
            System.out.println("Vui long nhap day du thong tin!");
            return 0;
        }

        return dao.themNguoiDung(t);
    }

    // Sua nguoi dung
    public int suaNguoiDung(users t) {
        if (t.getIdNguoiDung() <= 0) {
            System.out.println("Ma nguoi dung khong hop le!");
            return 0;
        }

        return dao.suaNguoiDung(t);
    }

    // Xoa nguoi dung
    public int xoaNguoiDung(users t) {
        if (t.getIdNguoiDung() <= 0) {
            System.out.println("Ma nguoi dung khong hop le!");
            return 0;
        }

        return dao.xoaNguoiDung(t);
    }

    // Lay danh sach nguoi dung
    public ArrayList<users> xuat() {
        return dao.xuat();
    }

    // Tim nguoi dung
    public users timKiem(users t) {
        if (t.getTenDangNhap() == null
                || t.getTenDangNhap().isEmpty()) {
            return null;
        }

        return dao.timKiem(t);
    }
}
