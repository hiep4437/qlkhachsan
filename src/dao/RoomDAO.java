package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.ThongTinPhongDangO;
import model.rooms;

public class RoomDAO {

	// Chuyen doi chuoi trang thai sang ma so TINYINT (0, 1, 2, 3)
	public static int chuyenTrangThaiSangSo(String st) {
		if (st == null) return 0;
		st = st.trim().toUpperCase();
		if (st.equals("1") || st.contains("OCCUPIED") || st.contains("CÓ KHÁCH") || st.contains("CO KHACH")) return 1;
		if (st.equals("2") || st.contains("CLEANING") || st.contains("ĐANG DỌN") || st.contains("DỌN")) return 2;
		if (st.equals("3") || st.contains("MAINTENANCE") || st.contains("BẢO TRÌ")) return 3;
		return 0; // 0: AVAILABLE / TRONG
	}

	// Chuyen doi ma so TINYINT sang chuoi trang thai chuan
	public static String chuyenSoSangTrangThai(int num) {
		switch (num) {
			case 1: return "OCCUPIED";
			case 2: return "CLEANING";
			case 3: return "MAINTENANCE";
			case 0:
			default: return "AVAILABLE";
		}
	}

	// Tinh tang tu so phong neu chua co
	private int tinhTangTuSoPhong(String soPhong, int tangHienTai) {
		if (tangHienTai > 0) return tangHienTai;
		if (soPhong != null && !soPhong.trim().isEmpty()) {
			char c = soPhong.trim().charAt(0);
			if (Character.isDigit(c)) {
				return Character.getNumericValue(c);
			}
		}
		return 1;
	}

	// Them phong
	public int them(rooms t) {
		rooms p = timKiem(t);
		if (p != null) {
			System.out.println("So phong da ton tai!");
			return 0;
		}

		int tang = tinhTangTuSoPhong(t.getSoPhong(), t.getTang());
		int trangThaiSo = chuyenTrangThaiSangSo(t.getTrangThai());

		String sql = "INSERT INTO phong (so_phong, ma_loai_phong, tang, gia_moi_dem, trang_thai, mo_ta) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, t.getSoPhong().trim());
			ps.setInt(2, t.getMaLoaiPhong());
			ps.setInt(3, tang);
			ps.setDouble(4, t.getGiaMoiDem());
			ps.setInt(5, trangThaiSo);
			ps.setString(6, t.getMoTa());

			int kq = ps.executeUpdate();
			if (kq > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						t.setMaPhong(rs.getInt(1));
					}
				}
			}
			return kq;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Sua phong
	public int sua(rooms t) {
		int tang = tinhTangTuSoPhong(t.getSoPhong(), t.getTang());
		int trangThaiSo = chuyenTrangThaiSangSo(t.getTrangThai());

		String sql = "UPDATE phong SET so_phong = ?, ma_loai_phong = ?, tang = ?, gia_moi_dem = ?, trang_thai = ?, mo_ta = ? "
				+ "WHERE ma_phong = ?";

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, t.getSoPhong().trim());
			ps.setInt(2, t.getMaLoaiPhong());
			ps.setInt(3, tang);
			ps.setDouble(4, t.getGiaMoiDem());
			ps.setInt(5, trangThaiSo);
			ps.setString(6, t.getMoTa());
			ps.setInt(7, t.getMaPhong());

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Doi trang thai phong
	public int doiTrangThai(int roomId, String trangThai) {
		int ttSo = chuyenTrangThaiSangSo(trangThai);
		String sql = "UPDATE phong SET trang_thai = ? WHERE ma_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, ttSo);
			ps.setInt(2, roomId);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xoa phong
	public int xoa(rooms r) {
		String sql = "DELETE FROM phong WHERE ma_phong = ?";
		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, r.getMaPhong());
			return ps.executeUpdate();
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			JOptionPane.showMessageDialog(
					null,
					"Không thể xóa phòng vì phòng đã có dữ liệu đặt phòng!"
			);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach phong (kem ten loai phong)
	public ArrayList<rooms> xuat() {
		ArrayList<rooms> ds = new ArrayList<>();
		String sql = "SELECT p.*, COALESCE(lp.ten_loai_phong, 'Phòng tiêu chuẩn') AS ten_loai_phong "
				+ "FROM phong p "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "ORDER BY p.so_phong ASC";

		try (Connection conn = DBConnection.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				int tt = rs.getInt("trang_thai");
				rooms t = new rooms(
						rs.getInt("ma_phong"),
						rs.getString("so_phong"),
						rs.getInt("ma_loai_phong"),
						rs.getInt("tang"),
						rs.getFloat("gia_moi_dem"),
						chuyenSoSangTrangThai(tt),
						rs.getString("mo_ta"),
						rs.getString("ten_loai_phong")
				);
				ds.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	// Tim kiem phong theo so phong
	public rooms timKiem(rooms t) {
		if (t == null || t.getSoPhong() == null || t.getSoPhong().trim().isEmpty()) {
			return null;
		}

		String sql = "SELECT p.*, COALESCE(lp.ten_loai_phong, 'Phòng tiêu chuẩn') AS ten_loai_phong "
				+ "FROM phong p "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "WHERE p.so_phong = ?";

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, t.getSoPhong().trim());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int tt = rs.getInt("trang_thai");
					return new rooms(
							rs.getInt("ma_phong"),
							rs.getString("so_phong"),
							rs.getInt("ma_loai_phong"),
							rs.getInt("tang"),
							rs.getFloat("gia_moi_dem"),
							chuyenSoSangTrangThai(tt),
							rs.getString("mo_ta"),
							rs.getString("ten_loai_phong")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Lay thong tin khach va dat phong dang o
	public ThongTinPhongDangO layThongTinKhachDangO(int roomId) {
		String sql = "SELECT b.ma_dat_phong, b.ma_khach_hang, c.ho_ten, c.so_cccd, c.so_dien_thoai, "
				+ "b.ngay_nhan_phong, b.ngay_tra_phong, b.trang_thai AS b_status, b.ghi_chu, "
				+ "br.gia_moi_dem, "
				+ "i.ma_hoa_don, i.tien_phong, i.phu_thu, i.giam_gia, i.tong_tien, i.phuong_thuc_thanh_toan, i.trang_thai_thanh_toan "
				+ "FROM dat_phong b "
				+ "JOIN chi_tiet_dat_phong br ON b.ma_dat_phong = br.ma_dat_phong "
				+ "JOIN khach_hang c ON b.ma_khach_hang = c.ma_khach_hang "
				+ "LEFT JOIN hoa_don i ON b.ma_dat_phong = i.ma_dat_phong "
				+ "WHERE br.ma_phong = ? "
				+ "ORDER BY (CASE WHEN b.trang_thai = 1 THEN 1 WHEN b.trang_thai = 0 THEN 2 ELSE 3 END), b.ma_dat_phong DESC "
				+ "LIMIT 1";

		try (Connection conn = DBConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, roomId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ThongTinPhongDangO info = new ThongTinPhongDangO();
					info.setBookingId(rs.getInt("ma_dat_phong"));
					info.setCustomerId(rs.getInt("ma_khach_hang"));
					info.setTenKhachHang(rs.getString("ho_ten"));
					info.setCccd(rs.getString("so_cccd"));
					info.setSoDienThoai(rs.getString("so_dien_thoai"));
					info.setNgayNhan(rs.getDate("ngay_nhan_phong"));
					info.setNgayTra(rs.getDate("ngay_tra_phong"));

					int bStatus = rs.getInt("b_status");
					String bStatusStr = (bStatus == 1) ? "CHECKED_IN" : (bStatus == 0 ? "BOOKED" : (bStatus == 2 ? "CHECKED_OUT" : "CANCELLED"));
					info.setBookingStatus(bStatusStr);

					info.setGhiChu(rs.getString("ghi_chu"));
					info.setGiaMoiDem(rs.getDouble("gia_moi_dem"));
					info.setInvoiceId(rs.getInt("ma_hoa_don"));
					info.setTienPhong(rs.getDouble("tien_phong"));
					info.setPhuThu(rs.getDouble("phu_thu"));
					info.setGiamGia(rs.getDouble("giam_gia"));
					info.setTongTien(rs.getDouble("tong_tien"));
					info.setPaymentMethod(rs.getString("phuong_thuc_thanh_toan"));

					int pStatus = rs.getInt("trang_thai_thanh_toan");
					info.setPaymentStatus((pStatus == 1) ? "PAID" : (pStatus == 2 ? "REFUNDED" : "UNPAID"));
					return info;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
