package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.NguoiDung;

public class NguoiDungDAO {

    // Chuyen doi chuoi vai tro sang so TINYINT (0: STAFF, 1: ADMIN)
    public static int chuyenVaiTroSangSo(String vaiTro) {
        if (vaiTro != null) {
            String vt = vaiTro.trim().toLowerCase();
            if (vt.equals("admin") || vt.equals("1") || vt.contains("quản trị") || vt.contains("quan tri")) {
                return 1;
            }
        }
        return 0; // STAFF
    }

    // Chuyen doi so TINYINT sang chuoi vai tro (hien thi tieng Viet)
    public static String chuyenSoSangVaiTro(int vaiTro) {
        return (vaiTro == 1) ? "Quản trị" : "Nhân viên";
    }

    // Them nguoi dung moi
    public int themNguoiDung(NguoiDung bangHienTai) {
        if (timTheoUsername(bangHienTai.getTenDangNhap()) != null) {
            System.out.println("Tai khoan da ton tai: " + bangHienTai.getTenDangNhap());
            return -1; // Da ton tai
        }

        String cauLenhSQL;
        boolean hasMa = bangHienTai.getIdNguoiDung() > 0;
        if (hasMa) {
            cauLenhSQL = "INSERT INTO nguoi_dung (ma_nguoi_dung, ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            cauLenhSQL = "INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL, Statement.RETURN_GENERATED_KEYS)) {

            int chiSo = 1;
            if (hasMa) {
                luongIn.setInt(chiSo++, bangHienTai.getIdNguoiDung());
            }
            luongIn.setString(chiSo++, bangHienTai.getTenDangNhap().trim());
            luongIn.setString(chiSo++, bangHienTai.getMatKhau());
            luongIn.setString(chiSo++, bangHienTai.getHoTen().trim());
            luongIn.setInt(chiSo++, chuyenVaiTroSangSo(bangHienTai.getVaiTro()));
            luongIn.setInt(chiSo++, bangHienTai.isTrangThai() ? 1 : 0);

            int ketQua = luongIn.executeUpdate();
            if (ketQua > 0 && !hasMa) {
                try (ResultSet ketQuaTruyVan = luongIn.getGeneratedKeys()) {
                    if (ketQuaTruyVan.next()) {
                        bangHienTai.setIdNguoiDung(ketQuaTruyVan.getInt(1));
                    }
                }
            }
            System.out.println("Da them nguoi dung: " + bangHienTai.getTenDangNhap());
            return ketQua;
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Sua nguoi dung
    public int suaNguoiDung(NguoiDung bangHienTai) {
        boolean coDoiMatKhau = bangHienTai.getMatKhau() != null && !bangHienTai.getMatKhau().trim().isEmpty();
        String cauLenhSQL;
        if (coDoiMatKhau) {
            cauLenhSQL = "UPDATE nguoi_dung SET ten_dang_nhap = ?, mat_khau = ?, ho_ten = ?, vai_tro = ?, trang_thai = ? WHERE ma_nguoi_dung = ?";
        } else {
            cauLenhSQL = "UPDATE nguoi_dung SET ten_dang_nhap = ?, ho_ten = ?, vai_tro = ?, trang_thai = ? WHERE ma_nguoi_dung = ?";
        }

        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {

            int chiSo = 1;
            luongIn.setString(chiSo++, bangHienTai.getTenDangNhap().trim());
            if (coDoiMatKhau) {
                luongIn.setString(chiSo++, bangHienTai.getMatKhau());
            }
            luongIn.setString(chiSo++, bangHienTai.getHoTen().trim());
            luongIn.setInt(chiSo++, chuyenVaiTroSangSo(bangHienTai.getVaiTro()));
            luongIn.setInt(chiSo++, bangHienTai.isTrangThai() ? 1 : 0);
            luongIn.setInt(chiSo++, bangHienTai.getIdNguoiDung());

            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung theo ID
    public int xoaNguoiDung(int maNguoiDung) {
        String cauLenhSQL = "DELETE FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setInt(1, maNguoiDung);
            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Xoa nguoi dung theo object (backward compatibility)
    public int xoaNguoiDung(NguoiDung bangHienTai) {
        if (bangHienTai == null) return 0;
        return xoaNguoiDung(bangHienTai.getIdNguoiDung());
    }

    // Xuat danh sach nguoi dung
    public ArrayList<NguoiDung> xuat() {
        ArrayList<NguoiDung> danhSach = new ArrayList<>();
        String cauLenhSQL = "SELECT * FROM nguoi_dung ORDER BY ma_nguoi_dung ASC";

        try (Connection ketNoi = DBConecter.getConnection();
             Statement luongTrinh = ketNoi.createStatement();
             ResultSet ketQuaTruyVan = luongTrinh.executeQuery(cauLenhSQL)) {

            while (ketQuaTruyVan.next()) {
                int vaiTro = ketQuaTruyVan.getInt("vai_tro");
                int trangThai = ketQuaTruyVan.getInt("trang_thai");
                NguoiDung bangHienTai = new NguoiDung(
                        ketQuaTruyVan.getInt("ma_nguoi_dung"),
                        ketQuaTruyVan.getString("ten_dang_nhap"),
                        ketQuaTruyVan.getString("mat_khau"),
                        ketQuaTruyVan.getString("ho_ten"),
                        chuyenSoSangVaiTro(vaiTro),
                        (trangThai == 1)
                );
                danhSach.add(bangHienTai);
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }

        return danhSach;
    }

    // Tim kiem theo ten dang nhap (backward compatibility)
    public NguoiDung timKiem(NguoiDung bangHienTai) {
        if (bangHienTai == null || bangHienTai.getTenDangNhap() == null) return null;
        return timTheoUsername(bangHienTai.getTenDangNhap());
    }

    // Tim kiem theo username
    public NguoiDung timTheoUsername(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return null;
        String cauLenhSQL = "SELECT * FROM nguoi_dung WHERE ten_dang_nhap = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setString(1, tenDangNhap.trim());
            try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
                if (ketQuaTruyVan.next()) {
                    int vaiTro = ketQuaTruyVan.getInt("vai_tro");
                    int trangThai = ketQuaTruyVan.getInt("trang_thai");
                    return new NguoiDung(
                            ketQuaTruyVan.getInt("ma_nguoi_dung"),
                            ketQuaTruyVan.getString("ten_dang_nhap"),
                            ketQuaTruyVan.getString("mat_khau"),
                            ketQuaTruyVan.getString("ho_ten"),
                            chuyenSoSangVaiTro(vaiTro),
                            (trangThai == 1)
                    );
                }
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }
        return null;
    }

    // Tim kiem theo ID
    public NguoiDung timTheoId(int maNguoiDung) {
        String cauLenhSQL = "SELECT * FROM nguoi_dung WHERE ma_nguoi_dung = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setInt(1, maNguoiDung);
            try (ResultSet ketQuaTruyVan = luongIn.executeQuery()) {
                if (ketQuaTruyVan.next()) {
                    int vaiTro = ketQuaTruyVan.getInt("vai_tro");
                    int trangThai = ketQuaTruyVan.getInt("trang_thai");
                    return new NguoiDung(
                            ketQuaTruyVan.getInt("ma_nguoi_dung"),
                            ketQuaTruyVan.getString("ten_dang_nhap"),
                            ketQuaTruyVan.getString("mat_khau"),
                            ketQuaTruyVan.getString("ho_ten"),
                            chuyenSoSangVaiTro(vaiTro),
                            (trangThai == 1)
                    );
                }
            }
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }
        return null;
    }

    // Doi mat khau nguoi dung
    public int doiMatKhau(int maNguoiDung, String matKhauMoi) {
        String cauLenhSQL = "UPDATE nguoi_dung SET mat_khau = ? WHERE ma_nguoi_dung = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setString(1, matKhauMoi);
            luongIn.setInt(2, maNguoiDung);
            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Doi trang thai hoat dong (khoa / mo khoa)
    public int doiTrangThai(int maNguoiDung, boolean dangHoatDong) {
        String cauLenhSQL = "UPDATE nguoi_dung SET trang_thai = ? WHERE ma_nguoi_dung = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setInt(1, dangHoatDong ? 1 : 0);
            luongIn.setInt(2, maNguoiDung);
            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }

    // Cap nhat thong tin ca nhan (ho ten)
    public int capNhatHoTen(int maNguoiDung, String hoTen) {
        String cauLenhSQL = "UPDATE nguoi_dung SET ho_ten = ? WHERE ma_nguoi_dung = ?";
        try (Connection ketNoi = DBConecter.getConnection();
             PreparedStatement luongIn = ketNoi.prepareStatement(cauLenhSQL)) {
            luongIn.setString(1, hoTen.trim());
            luongIn.setInt(2, maNguoiDung);
            return luongIn.executeUpdate();
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return 0;
        }
    }
}
