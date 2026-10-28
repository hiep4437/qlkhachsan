package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.users;

public class UserDAO {

    // Chuyen doi chuoi vai tro sang so TINYINT (0: STAFF, 1: ADMIN)
    public static int chuyenVaiTroSangSo(String vt) {
        if (vt != null && (vt.equalsIgnoreCase("ADMIN") || vt.equals("1") || vt.toLowerCase().contains("quản trị"))) {
            return 1;
        }
        return 0; // STAFF
    }

    // Chuyen doi so TINYINT sang chuoi vai tro
    public static String chuyenSoSangVaiTro(int vt) {
        return (vt == 1) ? "ADMIN" : "STAFF";
    }

    // Them nguoi dung moi
    public int themNguoiDung(users t) {
        if (timTheoUsername(t.getTenDangNhap()) != null) {
            System.out.println("Tai khoan da ton tai: " + t.getTenDangNhap());
            return -1; // Da ton tai
        }

        String sql;
        boolean hasId = t.getIdNguoiDung() > 0;
        if (hasId) {
            sql = "INSERT INTO nguoi_dung (ma_nguoi_dung, ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int idx = 1;
            if (hasId) {
                ps.setInt(idx++, t.getIdNguoiDung());
            }
            ps.setString(idx++, t.getTenDangNhap().trim());
            ps.setString(idx++, t.getMatKhau());
            ps.setString(idx++, t.getHoTen().trim());
            ps.setInt(idx++, chuyenVaiTroSangSo(t.getVaiTro()));
            ps.setInt(idx++, t.isTrangThai() ? 1 : 0);

            int kq = ps.executeUpdate();
            if (kq > 0 && !hasId) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        t.setIdNguoiDung(rs.getInt(1));
                    }
                }
            }
            System.out.println("Da them nguoi dung: " + t.getTenDangNhap());
            return kq;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Sua nguoi dung
    public int suaNguoiDung(users t) {
        boolean coDoiMatKhau = t.getMatKhau() != null && !t.getMatKhau().trim().isEmpty();
        String sql;
        if (coDoiMatKhau) {
            sql = "UPDATE nguoi_dung SET ten_dang_nhap = ?, mat_khau = ?, ho_ten = ?, vai_tro = ?, trang_thai = ? WHERE ma_nguoi_dung = ?";
        } else {
            sql = "UPDATE nguoi_dung SET ten_dang_nhap = ?, ho_ten = ?, vai_tro = ?, trang_thai = ? WHERE ma_nguoi_dung = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getTenDangNhap().trim());
            if (coDoiMatKhau) {
                ps.setString(idx++, t.getMatKhau());
            }
            ps.setString(idx++, t.getHoTen().trim());
            ps.setInt(idx++, chuyenVaiTroSangSo(t.getVaiTro()));
            ps.setInt(idx++, t.isTrangThai() ? 1 : 0);
            ps.setInt(idx++, t.getIdNguoiDung());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung theo ID
    public int xoaNguoiDung(int idNguoiDung) {
        String sql = "DELETE FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNguoiDung);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung theo object (backward compatibility)
    public int xoaNguoiDung(users t) {
        if (t == null) return 0;
        return xoaNguoiDung(t.getIdNguoiDung());
    }

    // Xuat danh sach nguoi dung
    public ArrayList<users> xuat() {
        ArrayList<users> ds = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung ORDER BY ma_nguoi_dung ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int vt = rs.getInt("vai_tro");
                int tt = rs.getInt("trang_thai");
                users t = new users(
                        rs.getInt("ma_nguoi_dung"),
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("ho_ten"),
                        chuyenSoSangVaiTro(vt),
                        (tt == 1)
                );
                ds.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Tim kiem theo ten dang nhap (backward compatibility)
    public users timKiem(users t) {
        if (t == null || t.getTenDangNhap() == null) return null;
        return timTheoUsername(t.getTenDangNhap());
    }

    // Tim kiem theo username
    public users timTheoUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        String sql = "SELECT * FROM nguoi_dung WHERE ten_dang_nhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int vt = rs.getInt("vai_tro");
                    int tt = rs.getInt("trang_thai");
                    return new users(
                            rs.getInt("ma_nguoi_dung"),
                            rs.getString("ten_dang_nhap"),
                            rs.getString("mat_khau"),
                            rs.getString("ho_ten"),
                            chuyenSoSangVaiTro(vt),
                            (tt == 1)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tim kiem theo ID
    public users timTheoId(int userId) {
        String sql = "SELECT * FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int vt = rs.getInt("vai_tro");
                    int tt = rs.getInt("trang_thai");
                    return new users(
                            rs.getInt("ma_nguoi_dung"),
                            rs.getString("ten_dang_nhap"),
                            rs.getString("mat_khau"),
                            rs.getString("ho_ten"),
                            chuyenSoSangVaiTro(vt),
                            (tt == 1)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Doi mat khau nguoi dung
    public int doiMatKhau(int userId, String matKhauMoi) {
        String sql = "UPDATE nguoi_dung SET mat_khau = ? WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Doi trang thai hoat dong (khoa / mo khoa)
    public int doiTrangThai(int userId, boolean isActive) {
        String sql = "UPDATE nguoi_dung SET trang_thai = ? WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive ? 1 : 0);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Cap nhat thong tin ca nhan (ho ten)
    public int capNhatHoTen(int userId, String hoTen) {
        String sql = "UPDATE nguoi_dung SET ho_ten = ? WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hoTen.trim());
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
