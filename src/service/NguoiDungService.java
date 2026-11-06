package service;

import java.util.ArrayList;
import dao.NguoiDungDAO;
import model.NguoiDung;

public class NguoiDungService {

    private NguoiDungDAO truyCapDuLieu = new NguoiDungDAO();

    // Dang nhap boolean (backward compatibility)
    public boolean dangNhap(String ten, String matKhau) {
        NguoiDung doiTuong = kiemTraDangNhap(ten, matKhau);
        return doiTuong != null;
    }

    // Lay thong tin user neu dang nhap hop le
    public NguoiDung kiemTraDangNhap(String ten, String matKhau) {
        if (ten == null || ten.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()) {
            return null;
        }

        NguoiDung ketQua = truyCapDuLieu.timTheoUsername(ten.trim());

        if (ketQua != null && matKhau.equals(ketQua.getMatKhau()) && ketQua.isTrangThai()) {
            return ketQua;
        }
        return null;
    }

    // Them nguoi dung
    public int themNguoiDung(NguoiDung bangHienTai) {
        if (bangHienTai == null) return 0;
        if (bangHienTai.getTenDangNhap() == null || bangHienTai.getTenDangNhap().trim().isEmpty()
                || bangHienTai.getMatKhau() == null || bangHienTai.getMatKhau().trim().isEmpty()
                || bangHienTai.getHoTen() == null || bangHienTai.getHoTen().trim().isEmpty()) {
            System.out.println("Vui long nhap day du thong tin!");
            return -2; // Thieu thong tin
        }

        // Kiem tra trung ten dang nhap
        if (truyCapDuLieu.timTheoUsername(bangHienTai.getTenDangNhap().trim()) != null) {
            System.out.println("Tai khoan da ton tai!");
            return -1; // Da ton tai
        }

        return truyCapDuLieu.themNguoiDung(bangHienTai);
    }

    // Sua nguoi dung
    public int suaNguoiDung(NguoiDung bangHienTai) {
        if (bangHienTai == null || bangHienTai.getIdNguoiDung() <= 0) {
            System.out.println("Ma nguoi dung khong hop le!");
            return 0;
        }
        if (bangHienTai.getTenDangNhap() == null || bangHienTai.getTenDangNhap().trim().isEmpty()
                || bangHienTai.getHoTen() == null || bangHienTai.getHoTen().trim().isEmpty()) {
            return -2;
        }

        // Kiem tra neu ten dang nhap bi trung voi tai khoan khac
        NguoiDung daTonTai = truyCapDuLieu.timTheoUsername(bangHienTai.getTenDangNhap().trim());
        if (daTonTai != null && daTonTai.getIdNguoiDung() != bangHienTai.getIdNguoiDung()) {
            return -1; // Bi trung voi user khac
        }

        return truyCapDuLieu.suaNguoiDung(bangHienTai);
    }

    // Xoa nguoi dung (co kem kiem tra user dang dang nhap)
    public int xoaNguoiDung(int maNguoiDung, int maNguoiDungHienTai) {
        if (maNguoiDung <= 0) {
            return 0;
        }
        if (maNguoiDung == maNguoiDungHienTai) {
            return -1; // Khong the xoa tai khoan dang dang nhap
        }

        return truyCapDuLieu.xoaNguoiDung(maNguoiDung);
    }

    // Xoa nguoi dung (backward compatibility)
    public int xoaNguoiDung(NguoiDung bangHienTai) {
        if (bangHienTai == null || bangHienTai.getIdNguoiDung() <= 0) {
            return 0;
        }
        return truyCapDuLieu.xoaNguoiDung(bangHienTai.getIdNguoiDung());
    }

    // Doi trang thai hoat dong (Khoa / Mo khoa)
    public int doiTrangThai(int maNguoiDung, int maNguoiDungHienTai, boolean dangHoatDong) {
        if (maNguoiDung == maNguoiDungHienTai && !dangHoatDong) {
            return -1; // Khong the tu khoa tai khoan cua chinh minh
        }
        return truyCapDuLieu.doiTrangThai(maNguoiDung, dangHoatDong);
    }

    // Doi mat khau (khi nguoi dung tu doi trong thong tin ca nhan)
    public int doiMatKhau(int maNguoiDung, String matKhauCu, String matKhauMoi) {
        NguoiDung doiTuong = truyCapDuLieu.timTheoId(maNguoiDung);
        if (doiTuong == null) {
            return 0;
        }
        if (!doiTuong.getMatKhau().equals(matKhauCu)) {
            return -1; // Mat khau cu khong khop
        }
        if (matKhauMoi == null || matKhauMoi.trim().isEmpty()) {
            return -2; // Mat khau moi trong
        }
        return truyCapDuLieu.doiMatKhau(maNguoiDung, matKhauMoi);
    }

    // Dat lai mat khau cho user (Admin thuc hien)
    public int datLaiMatKhau(int maNguoiDung, String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.trim().isEmpty()) {
            return -2;
        }
        return truyCapDuLieu.doiMatKhau(maNguoiDung, matKhauMoi);
    }

    // Cap nhat ho ten ca nhan
    public int capNhatHoTen(int maNguoiDung, String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return -2;
        }
        return truyCapDuLieu.capNhatHoTen(maNguoiDung, hoTen.trim());
    }

    // Lay danh sach tat ca nguoi dung
    public ArrayList<NguoiDung> xuat() {
        return truyCapDuLieu.xuat();
    }

    // Tim kiem theo object (backward compatibility)
    public NguoiDung timKiem(NguoiDung bangHienTai) {
        if (bangHienTai == null || bangHienTai.getTenDangNhap() == null
                || bangHienTai.getTenDangNhap().isEmpty()) {
            return null;
        }
        return truyCapDuLieu.timKiem(bangHienTai);
    }

    // Tim theo ID
    public NguoiDung timTheoId(int ma) {
        return truyCapDuLieu.timTheoId(ma);
    }

    // Tim theo username
    public NguoiDung timTheoUsername(String tenDangNhap) {
        return truyCapDuLieu.timTheoUsername(tenDangNhap);
    }

    // Tim kiem va loc danh sach
    public ArrayList<NguoiDung> timKiemNangCao(String tuKhoa, String locVaiTro, String locTrangThai) {
        ArrayList<NguoiDung> tatCa = truyCapDuLieu.xuat();
        ArrayList<NguoiDung> ketQua = new ArrayList<>();

        String tuKhoaTim = (tuKhoa != null) ? tuKhoa.trim().toLowerCase() : "";

        for (NguoiDung doiTuong : tatCa) {
            // Loc vai tro
            if (locVaiTro != null && !locVaiTro.equals("Tất cả vai trò")) {
                if (!doiTuong.getVaiTro().equalsIgnoreCase(locVaiTro)) {
                    continue;
                }
            }

            // Loc trang thai
            if (locTrangThai != null && !locTrangThai.equals("Tất cả trạng thái")) {
                if (locTrangThai.contains("Hoạt động") && !doiTuong.isTrangThai()) {
                    continue;
                }
                if (locTrangThai.contains("Khóa") && doiTuong.isTrangThai()) {
                    continue;
                }
            }

            // Tim kiem tu khoa theo username hoac hoTen
            if (!tuKhoaTim.isEmpty()) {
                boolean khopTen = doiTuong.getTenDangNhap().toLowerCase().contains(tuKhoaTim);
                boolean khopHoTen = doiTuong.getHoTen().toLowerCase().contains(tuKhoaTim);
                if (!khopTen && !khopHoTen) {
                    continue;
                }
            }

            ketQua.add(doiTuong);
        }

        return ketQua;
    }
}
