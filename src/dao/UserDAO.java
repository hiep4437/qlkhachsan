
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.users;

public class UserDAO {

    // Them nguoi dung
    public int themNguoiDung(users t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            users u = timKiem(t);

            if (u == null) {
                String sql = "INSERT INTO users "
                        + "(user_id, username, password_hash, full_name, role, is_active) "
                        + "VALUES (" + t.getIdNguoiDung() + ", '"
                        + t.getTenDangNhap() + "', '"
                        + t.getMatKhau() + "', '"
                        + t.getHoTen() + "', '"
                        + t.getVaiTro() + "', "
                        + (t.isTrangThai() ? 1 : 0) + ")";

                int kq = st.executeUpdate(sql);
                System.out.println("Da them nguoi dung: " + t.getTenDangNhap());
                conn.close();
                return kq;
            } else {
                System.out.println("Tai khoan da ton tai.");
                conn.close();
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Sua nguoi dung
    public int suaNguoiDung(users t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "UPDATE users SET "
                    + "username = '" + t.getTenDangNhap() + "', "
                    + "password_hash = '" + t.getMatKhau() + "', "
                    + "full_name = '" + t.getHoTen() + "', "
                    + "role = '" + t.getVaiTro() + "', "
                    + "is_active = " + (t.isTrangThai() ? 1 : 0)
                    + " WHERE user_id = " + t.getIdNguoiDung();

            int kq = st.executeUpdate(sql);
            conn.close();
            return kq;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung
    public int xoaNguoiDung(users t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "DELETE FROM users WHERE user_id = "
                    + t.getIdNguoiDung();

            int kq = st.executeUpdate(sql);
            conn.close();
            return kq;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xuat danh sach nguoi dung
    public ArrayList<users> xuat() {
        ArrayList<users> ds = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "SELECT * FROM users";
            ResultSet rs = st.executeQuery(sql);

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

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Tim kiem theo ten dang nhap
    public users timKiem(users t) {
        users kq = null;

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "SELECT * FROM users WHERE username = '"
                    + t.getTenDangNhap() + "'";

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                kq = new users(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
                );
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kq;
    }
}
