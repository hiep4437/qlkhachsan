package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.ThongTinPhongDangO;
import model.Phong;

public class PhongDAO {

	// Chuyen doi chuoi trang thai sang ma so TINYINT (0, 1, 2, 3)
	public static int chuyenTrangThaiSangSo(String luongTrinh) {
		if (luongTrinh == null) return 0;
		luongTrinh = luongTrinh.trim().toUpperCase();
		if (luongTrinh.equals("1") || luongTrinh.contains("OCCUPIED") || luongTrinh.contains("CÓ KHÁCH") || luongTrinh.contains("CO KHACH")) return 1;
		if (luongTrinh.equals("2") || luongTrinh.contains("CLEANING") || luongTrinh.contains("ĐANG DỌN") || luongTrinh.contains("DỌN")) return 2;
		if (luongTrinh.equals("3") || luongTrinh.contains("MAINTENANCE") || luongTrinh.contains("BẢO TRÌ")) return 3;
		return 0; // 0: AVAILABLE / TRONG
	}

	// Chuyen doi ma so TINYINT sang chuoi trang thai chuan
	public static String chuyenSoSangTrangThai(int soLuong) {
		switch (soLuong) {
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
			char thanhPhan = soPhong.trim().charAt(0);
			if (Character.isDigit(thanhPhan)) {
				return Character.getNumericValue(thanhPhan);
			}
		}
		return 1;
	}

	// Them phong
	public int them(Phong bangHienTai) {
		Phong khung = timKiem(bangHienTai);
		if (khung != null) {
			System.out.println("So phong da ton tai!");
			return 0;
		}

		int tang = tinhTangTuSoPhong(bangHienTai.getSoPhong(), bangHienTai.getTang());
		int trangThaiSo = chuyenTrangThaiSangSo(bangHienTai.getTrangThai());

		String cauLenhSQL = "INSERT INTO phong (so_phong, ma_loai_phong, tang, gia_moi_dem, trang_thai, mo_ta) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL, Statement.RETURN_GENERATED_KEYS)) {

			luongIn.setString(1, bangHienTai.getSoPhong().trim());
			luongIn.setInt(2, bangHienTai.getMaLoaiPhong());
			luongIn.setInt(3, tang);
			luongIn.setDouble(4, bangHienTai.getGiaMoiDem());
			luongIn.setInt(5, trangThaiSo);
			luongIn.setString(6, bangHienTai.getMoTa());

			int ketQua = luongIn.executeUpdate();
			if (ketQua > 0) {
				try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
					if (ketQuaTruyVan.next()) {
						bangHienTai.setMaPhong(ketQuaTruyVan.getInt(1));
					}
				}
			}
			return ketQua;
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Sua phong
	public int sua(Phong bangHienTai) {
		int tang = tinhTangTuSoPhong(bangHienTai.getSoPhong(), bangHienTai.getTang());
		int trangThaiSo = chuyenTrangThaiSangSo(bangHienTai.getTrangThai());

		String cauLenhSQL = "UPDATE phong SET so_phong = ?, ma_loai_phong = ?, tang = ?, gia_moi_dem = ?, trang_thai = ?, mo_ta = ? "
				+ "WHERE ma_phong = ?";

		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setString(1, bangHienTai.getSoPhong().trim());
			luongIn.setInt(2, bangHienTai.getMaLoaiPhong());
			luongIn.setInt(3, tang);
			luongIn.setDouble(4, bangHienTai.getGiaMoiDem());
			luongIn.setInt(5, trangThaiSo);
			luongIn.setString(6, bangHienTai.getMoTa());
			luongIn.setInt(7, bangHienTai.getMaPhong());

			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Doi trang thai phong
	public int doiTrangThai(int maPhong, String trangThai) {
		int trangThaiSo = chuyenTrangThaiSangSo(trangThai);
		String cauLenhSQL = "UPDATE phong SET trang_thai = ? WHERE ma_phong = ?";
		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, trangThaiSo);
			luongIn.setInt(2, maPhong);
			return luongIn.executeUpdate();
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xoa phong
	public int xoa(Phong hangHienTai) {
		String cauLenhSQL = "DELETE FROM phong WHERE ma_phong = ?";
		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, hangHienTai.getMaPhong());
			return luongIn.executeUpdate();
		} catch (java.sql.SQLIntegrityConstraintViolationException suKien) {
			JOptionPane.showMessageDialog(
					null,
					"Không thể xóa phòng vì phòng đã có dữ liệu đặt phòng!"
			);
			return 0;
		} catch (Exception suKien) {
			suKien.printStackTrace();
			return 0;
		}
	}

	// Xuat danh sach phong (kem ten loai phong)
	public ArrayList<Phong> xuat() {
		ArrayList<Phong> danhSach = new ArrayList<>();
		String cauLenhSQL = "SELECT p.*, COALESCE(lp.ten_loai_phong, 'Phòng tiêu chuẩn') AS ten_loai_phong, "
				+ "COALESCE(lp.suc_chua, 1) AS suc_chua "
				+ "FROM phong p "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "ORDER BY p.so_phong ASC";

		try (Connection ketNoi = DBConecter.getConnection();
		     Statement luongTrinh = ketNoi.createStatement();
		     ResultSet ketQuaTruyVan = luongTrinh.executeQuery(cauLenhSQL)) {

			while (ketQuaTruyVan.next()) {
				int trangThai = ketQuaTruyVan.getInt("trang_thai");
				Phong bangHienTai = new Phong(
						ketQuaTruyVan.getInt("ma_phong"),
						ketQuaTruyVan.getString("so_phong"),
						ketQuaTruyVan.getInt("ma_loai_phong"),
						ketQuaTruyVan.getInt("tang"),
						ketQuaTruyVan.getFloat("gia_moi_dem"),
						chuyenSoSangTrangThai(trangThai),
						ketQuaTruyVan.getString("mo_ta"),
						ketQuaTruyVan.getString("ten_loai_phong")
				);
				bangHienTai.setSucChua(ketQuaTruyVan.getInt("suc_chua"));
				danhSach.add(bangHienTai);
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return danhSach;
	}

	// Tim kiem phong theo so phong
	public Phong timKiem(Phong bangHienTai) {
		if (bangHienTai == null || bangHienTai.getSoPhong() == null || bangHienTai.getSoPhong().trim().isEmpty()) {
			return null;
		}

		String cauLenhSQL = "SELECT p.*, COALESCE(lp.ten_loai_phong, 'Phòng tiêu chuẩn') AS ten_loai_phong, "
				+ "COALESCE(lp.suc_chua, 1) AS suc_chua "
				+ "FROM phong p "
				+ "LEFT JOIN loai_phong lp ON p.ma_loai_phong = lp.ma_loai_phong "
				+ "WHERE p.so_phong = ?";

		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

			luongIn.setString(1, bangHienTai.getSoPhong().trim());
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					int trangThai = ketQuaTruyVan.getInt("trang_thai");
					Phong phong = new Phong(
							ketQuaTruyVan.getInt("ma_phong"),
							ketQuaTruyVan.getString("so_phong"),
							ketQuaTruyVan.getInt("ma_loai_phong"),
							ketQuaTruyVan.getInt("tang"),
							ketQuaTruyVan.getFloat("gia_moi_dem"),
							chuyenSoSangTrangThai(trangThai),
							ketQuaTruyVan.getString("mo_ta"),
							ketQuaTruyVan.getString("ten_loai_phong")
					);
					phong.setSucChua(ketQuaTruyVan.getInt("suc_chua"));
					return phong;
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}

		return null;
	}

	// Lay thong tin khach va dat phong dang o
	public ThongTinPhongDangO layThongTinKhachDangO(int maPhong) {
		String cauLenhSQL = "SELECT b.ma_dat_phong, b.ma_khach_hang, c.ho_ten, c.so_cccd, c.so_dien_thoai, "
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

		try (Connection ketNoi = DBConecter.getConnection();
		     PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
			luongIn.setInt(1, maPhong);
			try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
				if (ketQuaTruyVan.next()) {
					ThongTinPhongDangO thongTin = new ThongTinPhongDangO();
					thongTin.setBookingId(ketQuaTruyVan.getInt("ma_dat_phong"));
					thongTin.setCustomerId(ketQuaTruyVan.getInt("ma_khach_hang"));
					thongTin.setTenKhachHang(ketQuaTruyVan.getString("ho_ten"));
					thongTin.setCccd(ketQuaTruyVan.getString("so_cccd"));
					thongTin.setSoDienThoai(ketQuaTruyVan.getString("so_dien_thoai"));
					thongTin.setNgayNhan(ketQuaTruyVan.getDate("ngay_nhan_phong"));
					thongTin.setNgayTra(ketQuaTruyVan.getDate("ngay_tra_phong"));

					int btTrangThai = ketQuaTruyVan.getInt("b_status");
					String chuoiTrangThaiNut = (btTrangThai == 1) ? "CHECKED_IN" : (btTrangThai == 0 ? "BOOKED" : (btTrangThai == 2 ? "CHECKED_OUT" : "CANCELLED"));
					thongTin.setBookingStatus(chuoiTrangThaiNut);

					thongTin.setGhiChu(ketQuaTruyVan.getString("ghi_chu"));
					thongTin.setGiaMoiDem(ketQuaTruyVan.getDouble("gia_moi_dem"));
					thongTin.setInvoiceId(ketQuaTruyVan.getInt("ma_hoa_don"));
					thongTin.setTienPhong(ketQuaTruyVan.getDouble("tien_phong"));
					thongTin.setPhuThu(ketQuaTruyVan.getDouble("phu_thu"));
					thongTin.setGiamGia(ketQuaTruyVan.getDouble("giam_gia"));
					thongTin.setTongTien(ketQuaTruyVan.getDouble("tong_tien"));
					thongTin.setPaymentMethod(ketQuaTruyVan.getString("phuong_thuc_thanh_toan"));

					int khungTrangThai = ketQuaTruyVan.getInt("trang_thai_thanh_toan");
					thongTin.setPaymentStatus((khungTrangThai == 1) ? "PAID" : (khungTrangThai == 2 ? "REFUNDED" : "UNPAID"));
					return thongTin;
				}
			}
		} catch (Exception suKien) {
			suKien.printStackTrace();
		}
		return null;
	}
}
