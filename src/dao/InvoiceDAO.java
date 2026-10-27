
package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.invoices;

public class InvoiceDAO {

	// Them hoa don
	public int them(invoices t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "INSERT INTO invoices " + "(booking_id, room_amount, surcharge, discount, "
					+ "total_amount, payment_method, payment_status) VALUES (" + t.getIdDatPhong() + ", "
					+ t.getTienPhong() + ", " + t.getPhuThu() + ", " + t.getGiamGia() + ", " + t.getTongTien() + ", '"
					+ t.getPhuongThucThanhToan() + "', '" + t.getTrangThaiThanhToan() + "')";

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua hoa don
	public int sua(invoices t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "UPDATE invoices SET " + "booking_id = " + t.getIdDatPhong() + ", " + "room_amount = "
					+ t.getTienPhong() + ", " + "surcharge = " + t.getPhuThu() + ", " + "discount = " + t.getGiamGia()
					+ ", " + "total_amount = " + t.getTongTien() + ", " + "payment_method = '"
					+ t.getPhuongThucThanhToan() + "', " + "payment_status = '" + t.getTrangThaiThanhToan() + "' "
					+ "WHERE invoice_id = " + t.getIdHoaDon();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa hoa don
	public int xoa(invoices t) {
		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "DELETE FROM invoices WHERE invoice_id = " + t.getIdHoaDon();

			int kq = st.executeUpdate(sql);
			conn.close();
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach hoa don
	public ArrayList<invoices> xuat() {
		ArrayList<invoices> ds = new ArrayList<>();

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM invoices";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				invoices t = new invoices(rs.getInt("invoice_id"), rs.getInt("booking_id"),
						rs.getTimestamp("issued_at"), rs.getFloat("room_amount"), rs.getFloat("surcharge"),
						rs.getFloat("discount"), rs.getFloat("total_amount"), rs.getString("payment_method"),
						rs.getString("payment_status"));

				ds.add(t);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim hoa don theo ma
	public invoices timKiem(invoices t) {
		invoices kq = null;

		try {
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();

			String sql = "SELECT * FROM invoices WHERE invoice_id = " + t.getIdHoaDon();

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				kq = new invoices(rs.getInt("invoice_id"), rs.getInt("booking_id"), rs.getTimestamp("issued_at"),
						rs.getFloat("room_amount"), rs.getFloat("surcharge"), rs.getFloat("discount"),
						rs.getFloat("total_amount"), rs.getString("payment_method"), rs.getString("payment_status"));
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kq;
	}
}
