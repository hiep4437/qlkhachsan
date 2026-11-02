package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.KhachHang;

public class KhachHangDAO {

    // Them khach hang
    public int them(KhachHang t) {
        KhachHang p = timKiem(t);
        if (p != null) {
            System.out.println("So CCCD da ton tai!");
            return 0;
        }

        String sql = "INSERT INTO khach_hang (ho_ten, so_cccd, so_dien_thoai, email, dia_chi) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, t.getNameKhachHang().trim());
            ps.setString(2, t.getSoCCCD() != null ? t.getSoCCCD().trim() : null);
            ps.setString(3, t.getSoDienThoai() != null ? t.getSoDienThoai().trim() : null);
            ps.setString(4, t.getEmail() != null ? t.getEmail().trim() : null);
            ps.setString(5, t.getDiaChi() != null ? t.getDiaChi().trim() : null);

            int kq = ps.executeUpdate();
            if (kq > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						t.setIdKhachHang(rs.getInt(1));
					}
				}
			}
            return kq;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Sua khach hang
    public int sua(KhachHang t) {
        String sql = "UPDATE khach_hang SET ho_ten = ?, so_cccd = ?, so_dien_thoai = ?, email = ?, dia_chi = ? WHERE ma_khach_hang = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getNameKhachHang().trim());
            ps.setString(2, t.getSoCCCD() != null ? t.getSoCCCD().trim() : null);
            ps.setString(3, t.getSoDienThoai() != null ? t.getSoDienThoai().trim() : null);
            ps.setString(4, t.getEmail() != null ? t.getEmail().trim() : null);
            ps.setString(5, t.getDiaChi() != null ? t.getDiaChi().trim() : null);
            ps.setInt(6, t.getIdKhachHang());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xoa khach hang
    public int xoa(KhachHang t) {
        String sql = "DELETE FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getIdKhachHang());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Xuat danh sach khach hang kem theo so lan dat phong
    public ArrayList<KhachHang> xuat() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY ma_khach_hang DESC";

        try (Connection conn = KetNoiCSDL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                KhachHang t = new KhachHang(
                        rs.getInt("ma_khach_hang"),
                        rs.getString("ho_ten"),
                        rs.getString("so_cccd"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email"),
                        rs.getString("dia_chi")
                );
                ds.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Dem so lan dat phong cua khach hang
    public int demSoLanDatPhong(int maKhachHang) {
        String sql = "SELECT COUNT(*) FROM dat_phong WHERE ma_khach_hang = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tim kiem khach hang theo CCCD hoac ID
    public KhachHang timKiem(KhachHang t) {
        KhachHang kq = null;
        if (t == null) return null;

        String sql = "SELECT * FROM khach_hang WHERE ma_khach_hang = ? OR (so_cccd IS NOT NULL AND so_cccd = ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getIdKhachHang());
            ps.setString(2, t.getSoCCCD() != null ? t.getSoCCCD().trim() : "");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kq = new KhachHang(
                            rs.getInt("ma_khach_hang"),
                            rs.getString("ho_ten"),
                            rs.getString("so_cccd"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("email"),
                            rs.getString("dia_chi")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kq;
    }
}
