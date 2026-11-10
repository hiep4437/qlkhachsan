package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.KhachHang;

public class KhachHangDAO {

    // Them khach hang
    public int them(KhachHang bangHienTai) {
        KhachHang khung = timKiem(bangHienTai);
        if (khung != null) {
            System.out.println("So CCCD da ton tai!");
            return 0;
        }

        String cauLenhSQL = "INSERT INTO khach_hang (ho_ten, so_cccd, so_dien_thoai, email, dia_chi) VALUES (?, ?, ?, ?, ?)";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL, Statement.RETURN_GENERATED_KEYS)) {

            luongIn.setString(1, bangHienTai.getNameKhachHang().trim());
            luongIn.setString(2, bangHienTai.getSoCCCD() != null ? bangHienTai.getSoCCCD().trim() : null);
            luongIn.setString(3, bangHienTai.getSoDienThoai() != null ? bangHienTai.getSoDienThoai().trim() : null);
            luongIn.setString(4, bangHienTai.getEmail() != null ? bangHienTai.getEmail().trim() : null);
            luongIn.setString(5, bangHienTai.getDiaChi() != null ? bangHienTai.getDiaChi().trim() : null);

            int ketQua = luongIn.executeUpdate();
            if (ketQua > 0) {
				try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
					if (ketQuaTruyVan.next()) {
						bangHienTai.setIdKhachHang(ketQuaTruyVan.getInt(1));
					}
				}
			}
            return ketQua;
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Sua khach hang
    public int sua(KhachHang bangHienTai) {
        String cauLenhSQL = "UPDATE khach_hang SET ho_ten = ?, so_cccd = ?, so_dien_thoai = ?, email = ?, dia_chi = ? WHERE ma_khach_hang = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

            luongIn.setString(1, bangHienTai.getNameKhachHang().trim());
            luongIn.setString(2, bangHienTai.getSoCCCD() != null ? bangHienTai.getSoCCCD().trim() : null);
            luongIn.setString(3, bangHienTai.getSoDienThoai() != null ? bangHienTai.getSoDienThoai().trim() : null);
            luongIn.setString(4, bangHienTai.getEmail() != null ? bangHienTai.getEmail().trim() : null);
            luongIn.setString(5, bangHienTai.getDiaChi() != null ? bangHienTai.getDiaChi().trim() : null);
            luongIn.setInt(6, bangHienTai.getIdKhachHang());

            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Xoa khach hang
    public int xoa(KhachHang bangHienTai) {
        String cauLenhSQL = "DELETE FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

            luongIn.setInt(1, bangHienTai.getIdKhachHang());
            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Xuat danh sach khach hang kem theo so lan dat phong
    public ArrayList<KhachHang> xuat() {
        ArrayList<KhachHang> danhSach = new ArrayList<>();
        String cauLenhSQL = "SELECT * FROM khach_hang ORDER BY ma_khach_hang DESC";

        try (Connection ketNoi = DBConecter.getConnection();
             Statement luongTrinh = ketNoi.createStatement();
             ResultSet ketQuaTruyVan = luongTrinh.executeQuery(cauLenhSQL)) {

            while (ketQuaTruyVan.next()) {
                KhachHang bangHienTai = new KhachHang(
                        ketQuaTruyVan.getInt("ma_khach_hang"),
                        ketQuaTruyVan.getString("ho_ten"),
                        ketQuaTruyVan.getString("so_cccd"),
                        ketQuaTruyVan.getString("so_dien_thoai"),
                        ketQuaTruyVan.getString("email"),
                        ketQuaTruyVan.getString("dia_chi")
                );
                danhSach.add(bangHienTai);
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }

        return danhSach;
    }

    // Dem so lan dat phong cua khach hang
    public int demSoLanDatPhong(int maKhachHang) {
        String cauLenhSQL = "SELECT COUNT(*) FROM dat_phong WHERE ma_khach_hang = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setInt(1, maKhachHang);
            try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
                if (ketQuaTruyVan.next()) {
                    return ketQuaTruyVan.getInt(1);
                }
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }
        return 0;
    }

    // Tim kiem khach hang theo CCCD hoac ID
    public KhachHang timKiem(KhachHang bangHienTai) {
        KhachHang ketQua = null;
        if (bangHienTai == null) return null;

        String cauLenhSQL = "SELECT * FROM khach_hang WHERE ma_khach_hang = ? OR (so_cccd IS NOT NULL AND so_cccd = ?)";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

            luongIn.setInt(1, bangHienTai.getIdKhachHang());
            luongIn.setString(2, bangHienTai.getSoCCCD() != null ? bangHienTai.getSoCCCD().trim() : "");

            try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
                if (ketQuaTruyVan.next()) {
                    ketQua = new KhachHang(
                            ketQuaTruyVan.getInt("ma_khach_hang"),
                            ketQuaTruyVan.getString("ho_ten"),
                            ketQuaTruyVan.getString("so_cccd"),
                            ketQuaTruyVan.getString("so_dien_thoai"),
                            ketQuaTruyVan.getString("email"),
                            ketQuaTruyVan.getString("dia_chi")
                    );
                }
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }

        return ketQua;
    }
}
