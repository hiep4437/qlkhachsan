package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import model.rooms;
import service.RoomService;
import view.ViewQuanLyPhong;

public class RoomController {

    ViewQuanLyPhong view;
    RoomService service = new RoomService();

    public RoomController(ViewQuanLyPhong view) {
        this.view = view;

        taiDuLieu();

        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnXoa.addActionListener(e -> xoa());
        view.btnTimKiem.addActionListener(e -> timKiem());
        view.btnLamMoi.addActionListener(e -> {
            view.lamMoi();
            taiDuLieu();
        });
    }

    // Hien thi danh sach phong
    void taiDuLieu() {
        view.model.setRowCount(0);

        ArrayList<rooms> ds = service.xuat();

        for (rooms r : ds) {
            view.model.addRow(new Object[] {
                r.getMaPhong(),
                r.getSoPhong(),
                r.getMaLoaiPhong(),
                r.getGiaMoiDem(),
                hienThiTrangThai(r.getTrangThai()),
                r.getMoTa()
            });
        }
    }

    // Tim phong theo ma phong
    rooms timTheoMa(int ma) {
        for (rooms r : service.xuat()) {
            if (r.getMaPhong() == ma) {
                return r;
            }
        }
        return null;
    }

    // Lay du lieu tu giao dien
    rooms layDuLieu() {
        String soPhong = view.txtSoPhong.getText().trim();
        String maLoai = view.txtMaLoai.getText().trim();
        String gia = view.txtGia.getText().trim();
        String moTa = view.txtMoTa.getText().trim();

        if (soPhong.isEmpty() || maLoai.isEmpty() || gia.isEmpty()) {
            throw new IllegalArgumentException(
                "Vui long nhap so phong, ma loai va gia!"
            );
        }

        rooms r = new rooms();
        r.setSoPhong(soPhong);
        r.setMaLoaiPhong(Integer.parseInt(maLoai));
        r.setGiaMoiDem(Float.parseFloat(gia));
        r.setTrangThai(chuyenTrangThai(
            (String) view.cboTrangThai.getSelectedItem()
        ));
        r.setMoTa(moTa);

        return r;
    }

    // Them phong
    void them() {
        try {
            rooms r = layDuLieu();

            if (service.them(r) > 0) {
                JOptionPane.showMessageDialog(view, "Them phong thanh cong!");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Them phong that bai!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Ma loai va gia phai la so!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }

    // Sua phong: o trong thi giu gia tri cu
    void sua() {
        try {
            String soPhong = view.txtSoPhong.getText().trim();

            if (soPhong.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập số phòng cần sửa!");
                return;
            }

            rooms r = null;

            for (rooms p : service.xuat()) {
                if (p.getSoPhong().equals(soPhong)) {
                    r = p;
                    break;
                }
            }

            if (r == null) {
                JOptionPane.showMessageDialog(view,
                    "Không tìm thấy phòng " + soPhong + "!");
                return;
            }

            String loai = view.txtMaLoai.getText().trim();
            String gia = view.txtGia.getText().trim();
            String moTa = view.txtMoTa.getText().trim();
            String trangThai = (String) view.cboTrangThai.getSelectedItem();

            if (!loai.isEmpty())
                r.setMaLoaiPhong(Integer.parseInt(loai));

            if (!gia.isEmpty())
                r.setGiaMoiDem(Float.parseFloat(gia));

            if (!moTa.isEmpty())
                r.setMoTa(moTa);

            if (trangThai != null && !trangThai.isEmpty())
                r.setTrangThai(chuyenTrangThai(trangThai));

            if (service.sua(r) > 0) {
                JOptionPane.showMessageDialog(view,
                    "Sửa phòng thành công!");
                taiDuLieu();
                view.lamMoi();
            } else {
                JOptionPane.showMessageDialog(view,
                    "Sửa phòng thất bại!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                "Mã loại phòng và giá phải là số!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi: " + e.getMessage());
        }
    }

    // Tim theo dau so phong, vi du 3 tim 301, 302
    void timKiem() {
        String tuKhoa = view.txtTimKiem.getText().trim();

        view.model.setRowCount(0);

        for (rooms r : service.xuat()) {
            if (r.getSoPhong() != null &&
                r.getSoPhong().startsWith(tuKhoa)) {

                view.model.addRow(new Object[] {
                    r.getMaPhong(),
                    r.getSoPhong(),
                    r.getMaLoaiPhong(),
                    r.getGiaMoiDem(),
                    hienThiTrangThai(r.getTrangThai()),
                    r.getMoTa()
                });
            }
        }

        if (view.model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "Khong tim thay phong!");
        }
    }

    // Xoa bang so phong
    void xoa() {
        String soPhong = view.txtSoPhong.getText().trim();

        if (soPhong.isEmpty()) {
            soPhong = view.txtTimKiem.getText().trim();
        }

        if (soPhong.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nhap so phong can xoa!");
            return;
        }

        rooms canXoa = null;

        for (rooms r : service.xuat()) {
            if (soPhong.equals(r.getSoPhong())) {
                canXoa = r;
                break;
            }
        }

        if (canXoa == null) {
            JOptionPane.showMessageDialog(view, "Khong tim thay phong!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
            view,
            "Ban co chac muon xoa phong " + soPhong + "?",
            "Xac nhan xoa",
            JOptionPane.YES_NO_OPTION
        );

        if (chon == JOptionPane.YES_OPTION) {
            if (service.xoa(canXoa) > 0) {
                JOptionPane.showMessageDialog(view, "Xoa phong thanh cong!");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Xoa phong that bai!");
            }
        }
    }

    // Chuyen trang thai tieng Viet sang tieng Anh de luu DB
    String chuyenTrangThai(String s) {
        if (s == null) return "AVAILABLE";

        switch (s) {
            case "TRỐNG":
            case "Trong":
                return "AVAILABLE";

            case "CÓ KHÁCH":
            case "Co khach":
                return "OCCUPIED";

            case "ĐANG DỌN":
            case "Dang don":
                return "CLEANING";

            case "BẢO TRÌ":
            case "Bao tri":
                return "MAINTENANCE";

            default:
                return s;
        }
    }

    String hienThiTrangThai(String s) {
        if (s == null) return "";

        switch (s) {
            case "AVAILABLE":
                return "TRỐNG";

            case "OCCUPIED":
                return "CÓ KHÁCH";

            case "CLEANING":
                return "ĐANG DỌN";

            case "MAINTENANCE":
                return "BẢO TRÌ";

            default:
                return s;
        }
    }
}
