
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.customers;

public class CustomerDAO {

    // Them khach hang
    public int them(customers t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            customers p = timKiem(t);

            if (p == null) {
                String sql = "INSERT INTO customers "
                        + "(full_name, identity_no, phone, email, address) VALUES ('"
                        + t.getNameKhachHang() + "', '"
                        + t.getSoCCCD() + "', '"
                        + t.getSoDienThoai() + "', '"
                        + t.getEmail() + "', '"
                        + t.getDiaChi() + "')";

                int kq = st.executeUpdate(sql);
                conn.close();
                return kq;
            } else {
                System.out.println("So CCCD da ton tai!");
                conn.close();
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Sua khach hang
    public int sua(customers t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "UPDATE customers SET "
                    + "full_name = '" + t.getNameKhachHang() + "', "
                    + "identity_no = '" + t.getSoCCCD() + "', "
                    + "phone = '" + t.getSoDienThoai() + "', "
                    + "email = '" + t.getEmail() + "', "
                    + "address = '" + t.getDiaChi() + "' "
                    + "WHERE customer_id = " + t.getIdKhachHang();

            int kq = st.executeUpdate(sql);
            conn.close();
            return kq;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa khach hang
    public int xoa(customers t) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "DELETE FROM customers WHERE customer_id = "
                    + t.getIdKhachHang();

            int kq = st.executeUpdate(sql);
            conn.close();
            return kq;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xuat danh sach khach hang
    public ArrayList<customers> xuat() {
        ArrayList<customers> ds = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "SELECT * FROM customers";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                customers t = new customers(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("identity_no"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"));

                ds.add(t);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Tim kiem theo CCCD
    public customers timKiem(customers t) {
        customers kq = null;

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            String sql = "SELECT * FROM customers WHERE identity_no = '"
                    + t.getSoCCCD() + "'";

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                kq = new customers(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("identity_no"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kq;
    }
}
