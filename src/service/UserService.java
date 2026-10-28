package service;

import java.util.ArrayList;
import dao.UserDAO;
import model.users;

public class UserService {

    private UserDAO dao = new UserDAO();

    // Dang nhap boolean (backward compatibility)
    public boolean dangNhap(String ten, String matKhau) {
        users u = kiemTraDangNhap(ten, matKhau);
        return u != null;
    }

    // Lay thong tin user neu dang nhap hop le
    public users kiemTraDangNhap(String ten, String matKhau) {
        if (ten == null || ten.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()) {
            return null;
        }

        users kq = dao.timTheoUsername(ten.trim());

        if (kq != null && matKhau.equals(kq.getMatKhau()) && kq.isTrangThai()) {
            return kq;
        }
        return null;
    }

    // Them nguoi dung
    public int themNguoiDung(users t) {
        if (t == null) return 0;
        if (t.getTenDangNhap() == null || t.getTenDangNhap().trim().isEmpty()
                || t.getMatKhau() == null || t.getMatKhau().trim().isEmpty()
                || t.getHoTen() == null || t.getHoTen().trim().isEmpty()) {
            System.out.println("Vui long nhap day du thong tin!");
            return -2; // Thieu thong tin
        }

        // Kiem tra trung ten dang nhap
        if (dao.timTheoUsername(t.getTenDangNhap().trim()) != null) {
            System.out.println("Tai khoan da ton tai!");
            return -1; // Da ton tai
        }

        return dao.themNguoiDung(t);
    }

    // Sua nguoi dung
    public int suaNguoiDung(users t) {
        if (t == null || t.getIdNguoiDung() <= 0) {
            System.out.println("Ma nguoi dung khong hop le!");
            return 0;
        }
        if (t.getTenDangNhap() == null || t.getTenDangNhap().trim().isEmpty()
                || t.getHoTen() == null || t.getHoTen().trim().isEmpty()) {
            return -2;
        }

        // Kiem tra neu ten dang nhap bi trung voi tai khoan khac
        users existing = dao.timTheoUsername(t.getTenDangNhap().trim());
        if (existing != null && existing.getIdNguoiDung() != t.getIdNguoiDung()) {
            return -1; // Bi trung voi user khac
        }

        return dao.suaNguoiDung(t);
    }

    // Xoa nguoi dung (co kem kiem tra user dang dang nhap)
    public int xoaNguoiDung(int idNguoiDung, int currentUserId) {
        if (idNguoiDung <= 0) {
            return 0;
        }
        if (idNguoiDung == currentUserId) {
            return -1; // Khong the xoa tai khoan dang dang nhap
        }

        return dao.xoaNguoiDung(idNguoiDung);
    }

    // Xoa nguoi dung (backward compatibility)
    public int xoaNguoiDung(users t) {
        if (t == null || t.getIdNguoiDung() <= 0) {
            return 0;
        }
        return dao.xoaNguoiDung(t.getIdNguoiDung());
    }

    // Doi trang thai hoat dong (Khoa / Mo khoa)
    public int doiTrangThai(int idNguoiDung, int currentUserId, boolean isActive) {
        if (idNguoiDung == currentUserId && !isActive) {
            return -1; // Khong the tu khoa tai khoan cua chinh minh
        }
        return dao.doiTrangThai(idNguoiDung, isActive);
    }

    // Doi mat khau (khi nguoi dung tu doi trong thong tin ca nhan)
    public int doiMatKhau(int userId, String matKhauCu, String matKhauMoi) {
        users u = dao.timTheoId(userId);
        if (u == null) {
            return 0;
        }
        if (!u.getMatKhau().equals(matKhauCu)) {
            return -1; // Mat khau cu khong khop
        }
        if (matKhauMoi == null || matKhauMoi.trim().isEmpty()) {
            return -2; // Mat khau moi trong
        }
        return dao.doiMatKhau(userId, matKhauMoi);
    }

    // Dat lai mat khau cho user (Admin thuc hien)
    public int datLaiMatKhau(int userId, String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.trim().isEmpty()) {
            return -2;
        }
        return dao.doiMatKhau(userId, matKhauMoi);
    }

    // Cap nhat ho ten ca nhan
    public int capNhatHoTen(int userId, String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return -2;
        }
        return dao.capNhatHoTen(userId, hoTen.trim());
    }

    // Lay danh sach tat ca nguoi dung
    public ArrayList<users> xuat() {
        return dao.xuat();
    }

    // Tim kiem theo object (backward compatibility)
    public users timKiem(users t) {
        if (t == null || t.getTenDangNhap() == null
                || t.getTenDangNhap().isEmpty()) {
            return null;
        }
        return dao.timKiem(t);
    }

    // Tim theo ID
    public users timTheoId(int id) {
        return dao.timTheoId(id);
    }

    // Tim theo username
    public users timTheoUsername(String username) {
        return dao.timTheoUsername(username);
    }

    // Tim kiem va loc danh sach
    public ArrayList<users> timKiemNangCao(String tuKhoa, String locVaiTro, String locTrangThai) {
        ArrayList<users> all = dao.xuat();
        ArrayList<users> kq = new ArrayList<>();

        String kw = (tuKhoa != null) ? tuKhoa.trim().toLowerCase() : "";

        for (users u : all) {
            // Loc vai tro
            if (locVaiTro != null && !locVaiTro.equals("Tất cả vai trò")) {
                if (!u.getVaiTro().equalsIgnoreCase(locVaiTro)) {
                    continue;
                }
            }

            // Loc trang thai
            if (locTrangThai != null && !locTrangThai.equals("Tất cả trạng thái")) {
                if (locTrangThai.contains("Hoạt động") && !u.isTrangThai()) {
                    continue;
                }
                if (locTrangThai.contains("Khóa") && u.isTrangThai()) {
                    continue;
                }
            }

            // Tim kiem tu khoa theo username hoac hoTen
            if (!kw.isEmpty()) {
                boolean matchTen = u.getTenDangNhap().toLowerCase().contains(kw);
                boolean matchHoTen = u.getHoTen().toLowerCase().contains(kw);
                if (!matchTen && !matchHoTen) {
                    continue;
                }
            }

            kq.add(u);
        }

        return kq;
    }
}
