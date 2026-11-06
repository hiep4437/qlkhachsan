package controller;

import java.awt.Window;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.PhongDAO;
import dao.LoaiPhongDAO;
import model.Phong;
import model.LoaiPhong;
import service.PhongService;
import view.dialog.DialogThemSuaPhong;
import view.view.ViewQuanLyPhong;

public class PhongController {

    ViewQuanLyPhong viewQuanLyPhong;
    PhongService dichVu = new PhongService();
    LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
    DecimalFormat dinhDangGiaTien = new DecimalFormat("#,###");

    public PhongController(ViewQuanLyPhong viewQuanLyPhong) {
        this.viewQuanLyPhong = viewQuanLyPhong;

        taiDuLieu();

        // Nút thêm, sửa, xóa
        viewQuanLyPhong.nutThem.addActionListener(suKien -> them());
        viewQuanLyPhong.nutSua.addActionListener(suKien -> sua());
        viewQuanLyPhong.nutXoa.addActionListener(suKien -> xoa());

        // Tìm kiếm & Lọc
        viewQuanLyPhong.nutTimKiem.addActionListener(suKien -> apDungBoLoc());
        viewQuanLyPhong.oNhapTimKiem.addActionListener(suKien -> apDungBoLoc());
        viewQuanLyPhong.hopChonLocTrangThai.addActionListener(suKien -> apDungBoLoc());

        viewQuanLyPhong.nutLamMoi.addActionListener(suKien -> {
            viewQuanLyPhong.lamMoi();
            taiDuLieu();
        });
    }

    // Hiển thị danh sách phòng (JOIN tên loại phòng, không hiển thị mã loại phòng)
    public void taiDuLieu() {
        viewQuanLyPhong.moHinh.setRowCount(0);

        ArrayList<Phong> danhSach = dichVu.xuat();

        int chung = danhSach.size();
        int trong = 0;
        int coKhach = 0;
        int dangDon = 0;
        int baoTri = 0;

        for (Phong hangHienTai : danhSach) {
            String trangThai = hienThiTrangThai(hangHienTai.getTrangThai());
            if ("TRỐNG".equals(trangThai)) trong++;
            else if ("CÓ KHÁCH".equals(trangThai)) coKhach++;
            else if ("ĐANG DỌN".equals(trangThai)) dangDon++;
            else if ("BẢO TRÌ".equals(trangThai)) baoTri++;

            String tenLoai = (hangHienTai.getTenLoaiPhong() != null && !hangHienTai.getTenLoaiPhong().trim().isEmpty())
                    ? hangHienTai.getTenLoaiPhong() : "Phòng tiêu chuẩn";

            int tang = hangHienTai.getTang() > 0 ? hangHienTai.getTang() : 1;
            String giaFmt = dinhDangGiaTien.format(hangHienTai.getGiaMoiDem()) + " đ";

            viewQuanLyPhong.moHinh.addRow(new Object[] {
                hangHienTai.getMaPhong(),
                hangHienTai.getSoPhong(),
                tang,
                tenLoai,
                giaFmt,
                trangThai,
                hangHienTai.getMoTa() != null ? hangHienTai.getMoTa() : ""
            });
        }

        viewQuanLyPhong.nhanThongKe.setText(String.format("Tổng số: %d phòng  |  Trống: %d  |  Có khách: %d  |  Đang dọn: %d  |  Bảo trì: %d",
                chung, trong, coKhach, dangDon, baoTri));

        apDungBoLoc();
    }

    // Lọc dữ liệu trên bảng theo từ khóa tìm kiếm và trạng thái
    void apDungBoLoc() {
        String tuKhoa = viewQuanLyPhong.oNhapTimKiem.getText().trim().toLowerCase();
        String trangThaiLoc = (String) viewQuanLyPhong.hopChonLocTrangThai.getSelectedItem();

        if (viewQuanLyPhong.boSapXep == null) return;

        List<RowFilter<DefaultTableModel, Object>> cacBoLoc = new ArrayList<>();

        // Lọc từ khóa (số phòng, loại phòng, mô tả...)
        if (!tuKhoa.isEmpty()) {
            cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
                    for (int chiSo = 0; chiSo < phanTu.getValueCount(); chiSo++) {
                        Object giaTri = phanTu.getValue(chiSo);
                        if (giaTri != null && giaTri.toString().toLowerCase().contains(tuKhoa)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // Lọc trạng thái
        if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
            cacBoLoc.add(RowFilter.regexFilter("^" + trangThaiLoc + "$", 5));
        }

        if (cacBoLoc.isEmpty()) {
            viewQuanLyPhong.boSapXep.setRowFilter(null);
        } else {
            viewQuanLyPhong.boSapXep.setRowFilter(RowFilter.andFilter(cacBoLoc));
        }
    }

    // Lấy cửa sổ cha của view để căn giữa Modal Dialog
    Window getParentWindow() {
        Window chieuRong = SwingUtilities.getWindowAncestor(viewQuanLyPhong.getContentPane());
        return chieuRong != null ? chieuRong : viewQuanLyPhong;
    }

    // Tìm phòng theo mã phòng
    Phong timTheoMa(int ma) {
        for (Phong hangHienTai : dichVu.xuat()) {
            if (hangHienTai.getMaPhong() == ma) {
                return hangHienTai;
            }
        }
        return null;
    }

    // Thêm phòng bằng Modal Dialog
    void them() {
        if (!viewQuanLyPhong.coQuyenQuanLyPhong()) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Nhân viên không có quyền thêm phòng.", "Không đủ quyền", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ArrayList<LoaiPhong> danhSachLoai = loaiPhongDAO.xuat();
            DialogThemSuaPhong hopThoai = new DialogThemSuaPhong(getParentWindow(), null, danhSachLoai);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                Phong phongMoi = hopThoai.getRoom();
                boolean canTaoDonSauKhiThem = PhongDAO.chuyenTrangThaiSangSo(phongMoi.getTrangThai()) == 1;
                if (canTaoDonSauKhiThem) {
                    int chon = JOptionPane.showConfirmDialog(
                            viewQuanLyPhong,
                            "Không thể thêm phòng ở trạng thái CÓ KHÁCH khi chưa có đơn lưu trú.\n"
                                    + "Bạn có muốn lưu phòng ở trạng thái TRỐNG rồi tạo đơn và Check-in trong tab Quản lý đặt phòng không?",
                            "Cần tạo đơn lưu trú",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (chon != JOptionPane.YES_OPTION) {
                        return;
                    }
                    phongMoi.setTrangThai("AVAILABLE");
                }

                if (dichVu.them(phongMoi) > 0) {
                    String thongBao = canTaoDonSauKhiThem
                            ? "Đã thêm phòng ở trạng thái TRỐNG. Hãy tạo đơn và Check-in trong tab Quản lý đặt phòng để ghi nhận khách."
                            : "Thêm phòng mới thành công!";
                    JOptionPane.showMessageDialog(viewQuanLyPhong, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(viewQuanLyPhong, "Thêm phòng thất bại! Có thể số phòng đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa phòng bằng Modal Dialog
    void sua() {
        int hangDaChon = viewQuanLyPhong.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Vui lòng chọn một phòng trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int hangMoHinh = viewQuanLyPhong.bang.convertRowIndexToModel(hangDaChon);
            int maPhong = (int) viewQuanLyPhong.moHinh.getValueAt(hangMoHinh, 0);

            Phong phongCanSua = timTheoMa(maPhong);
            if (phongCanSua == null) {
                JOptionPane.showMessageDialog(viewQuanLyPhong, "Không tìm thấy thông tin phòng đã chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<LoaiPhong> danhSachLoai = loaiPhongDAO.xuat();
            String trangThaiTruocKhiSua = phongCanSua.getTrangThai();
            DialogThemSuaPhong hopThoai = new DialogThemSuaPhong(getParentWindow(), phongCanSua, danhSachLoai);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                Phong phongCapNhat = hopThoai.getRoom();
                String huongDanTrangThai = null;
                int trangThaiCu = PhongDAO.chuyenTrangThaiSangSo(trangThaiTruocKhiSua);
                int trangThaiMoi = PhongDAO.chuyenTrangThaiSangSo(phongCapNhat.getTrangThai());

                if (trangThaiCu != trangThaiMoi && (trangThaiCu == 1 || trangThaiMoi == 1)) {
                    huongDanTrangThai = trangThaiMoi == 1
                            ? "Để ghi nhận khách lưu trú, hãy tạo đơn trong tab Quản lý đặt phòng rồi nhấn Nhận phòng (Check-in)."
                            : "Phòng đang có khách. Hãy dùng Trả phòng & Thanh toán trong tab Quản lý đặt phòng để cập nhật trạng thái.";
                    int chon = JOptionPane.showConfirmDialog(
                            viewQuanLyPhong,
                            huongDanTrangThai + "\n\nBạn có muốn lưu các thông tin phòng khác và giữ nguyên trạng thái hiện tại không?",
                            "Cần thao tác qua quản lý đặt phòng",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (chon != JOptionPane.YES_OPTION) {
                        return;
                    }
                    phongCapNhat.setTrangThai(trangThaiTruocKhiSua);
                }

                if (dichVu.sua(phongCapNhat) > 0) {
                    String thongBao = huongDanTrangThai == null
                            ? "Cập nhật phòng thành công!"
                            : "Đã cập nhật thông tin phòng; trạng thái được giữ nguyên.\n" + huongDanTrangThai;
                    JOptionPane.showMessageDialog(viewQuanLyPhong, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(viewQuanLyPhong, "Cập nhật phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa phòng
    void xoa() {
        if (!viewQuanLyPhong.coQuyenQuanLyPhong()) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Nhân viên không có quyền xóa phòng.", "Không đủ quyền", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int hangDaChon = viewQuanLyPhong.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Vui lòng chọn một phòng trong bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int hangMoHinh = viewQuanLyPhong.bang.convertRowIndexToModel(hangDaChon);
            int maPhong = (int) viewQuanLyPhong.moHinh.getValueAt(hangMoHinh, 0);
            String soPhong = viewQuanLyPhong.moHinh.getValueAt(hangMoHinh, 1).toString();

            Phong phongCanXoa = timTheoMa(maPhong);
            if (phongCanXoa == null) {
                JOptionPane.showMessageDialog(viewQuanLyPhong, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int chon = JOptionPane.showConfirmDialog(
                viewQuanLyPhong,
                "Bạn có chắc chắn muốn xóa phòng " + soPhong + " không?",
                "Xác nhận xóa phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (chon == JOptionPane.YES_OPTION) {
                if (dichVu.xoa(phongCanXoa) > 0) {
                    JOptionPane.showMessageDialog(viewQuanLyPhong, "Đã xóa phòng " + soPhong + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(viewQuanLyPhong, "Không thể xóa phòng " + soPhong + "! (Có thể phòng đang có hóa đơn/đặt phòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewQuanLyPhong, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hiển thị chuỗi trạng thái tiếng Việt
    String hienThiTrangThai(String chuoi) {
        if (chuoi == null) return "TRỐNG";
        switch (chuoi.trim().toUpperCase()) {
            case "AVAILABLE":
            case "TRỐNG":
            case "0":
                return "TRỐNG";
            case "OCCUPIED":
            case "CÓ KHÁCH":
            case "1":
                return "CÓ KHÁCH";
            case "CLEANING":
            case "ĐANG DỌN":
            case "2":
                return "ĐANG DỌN";
            case "MAINTENANCE":
            case "BẢO TRÌ":
            case "3":
                return "BẢO TRÌ";
            default:
                return chuoi;
        }
    }
}
