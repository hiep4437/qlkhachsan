package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.users;

public class UserDAO {

    // Them nguoi dung moi
    public int themNguoiDung(users t) {
        if (timTheoUsername(t.getTenDangNhap()) != null) {
            System.out.println("Tai khoan da ton tai: " + t.getTenDangNhap());
            return -1; // Da ton tai
        }

        String sql;
        boolean hasId = t.getIdNguoiDung() > 0;
        if (hasId) {
            sql = "INSERT INTO users (user_id, username, password_hash, full_name, role, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO users (username, password_hash, full_name, role, is_active) VALUES (?, ?, ?, ?, ?)";
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
            ps.setString(idx++, t.getVaiTro() != null ? t.getVaiTro() : "STAFF");
            ps.setBoolean(idx++, t.isTrangThai());

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
            sql = "UPDATE users SET username = ?, password_hash = ?, full_name = ?, role = ?, is_active = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE users SET username = ?, full_name = ?, role = ?, is_active = ? WHERE user_id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getTenDangNhap().trim());
            if (coDoiMatKhau) {
                ps.setString(idx++, t.getMatKhau());
            }
            ps.setString(idx++, t.getHoTen().trim());
            ps.setString(idx++, t.getVaiTro());
            ps.setBoolean(idx++, t.isTrangThai());
            ps.setInt(idx++, t.getIdNguoiDung());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung theo ID
    public int xoaNguoiDung(int idNguoiDung) {
        String sql = "DELETE FROM users WHERE user_id = ?";
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
        String sql = "SELECT * FROM users ORDER BY user_id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users t = new users(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
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
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new users(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("full_name"),
                            rs.getString("role"),
                            rs.getBoolean("is_active")
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
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new users(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("full_name"),
                            rs.getString("role"),
                            rs.getBoolean("is_active")
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
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
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
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isActive);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Cap nhat thong tin ca nhan (ho ten)
    public int capNhatHoTen(int userId, String hoTen) {
        String sql = "UPDATE users SET full_name = ? WHERE user_id = ?";
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
