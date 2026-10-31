package controller;

import java.awt.Window;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.RoomDAO;
import dao.RoomTypeDAO;
import model.rooms;
import model.room_types;
import service.RoomService;
import view.DialogThemSuaPhong;
import view.ViewQuanLyPhong;

public class RoomController {

    ViewQuanLyPhong view;
    RoomService service = new RoomService();
    RoomTypeDAO roomTypeDAO = new RoomTypeDAO();
    DecimalFormat dfCurrency = new DecimalFormat("#,###");

    public RoomController(ViewQuanLyPhong view) {
        this.view = view;

        taiDuLieu();

        // Nút thêm, sửa, xóa
        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnXoa.addActionListener(e -> xoa());

        // Tìm kiếm & Lọc
        view.btnTimKiem.addActionListener(e -> apDungBoLoc());
        view.txtTimKiem.addActionListener(e -> apDungBoLoc());
        view.cboLocTrangThai.addActionListener(e -> apDungBoLoc());

        // Làm mới với đếm ngược 5s & disable
        view.btnLamMoi.addActionListener(e -> {
            view.batDauDemNguocLamMoi(() -> {
                view.lamMoi();
                taiDuLieu();
            });
        });
    }

    // Hiển thị danh sách phòng (JOIN tên loại phòng, không hiển thị mã loại phòng)
    public void taiDuLieu() {
        view.model.setRowCount(0);

        ArrayList<rooms> ds = service.xuat();

        int tong = ds.size();
        int trong = 0;
        int coKhach = 0;
        int dangDon = 0;
        int baoTri = 0;

        for (rooms r : ds) {
            String tt = hienThiTrangThai(r.getTrangThai());
            if ("TRỐNG".equals(tt)) trong++;
            else if ("CÓ KHÁCH".equals(tt)) coKhach++;
            else if ("ĐANG DỌN".equals(tt)) dangDon++;
            else if ("BẢO TRÌ".equals(tt)) baoTri++;

            String tenLoai = (r.getTenLoaiPhong() != null && !r.getTenLoaiPhong().trim().isEmpty())
                    ? r.getTenLoaiPhong() : "Phòng tiêu chuẩn";

            int tang = r.getTang() > 0 ? r.getTang() : 1;
            String giaFmt = dfCurrency.format(r.getGiaMoiDem()) + " đ";

            view.model.addRow(new Object[] {
                r.getMaPhong(),
                r.getSoPhong(),
                tang,
                tenLoai,
                giaFmt,
                tt,
                r.getMoTa() != null ? r.getMoTa() : ""
            });
        }

        view.lblThongKe.setText(String.format("Tổng số: %d phòng  |  Trống: %d  |  Có khách: %d  |  Đang dọn: %d  |  Bảo trì: %d",
                tong, trong, coKhach, dangDon, baoTri));

        apDungBoLoc();
    }

    // Lọc dữ liệu trên bảng theo từ khóa tìm kiếm và trạng thái
    void apDungBoLoc() {
        String tuKhoa = view.txtTimKiem.getText().trim().toLowerCase();
        String trangThaiLoc = (String) view.cboLocTrangThai.getSelectedItem();

        if (view.sorter == null) return;

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        // Lọc từ khóa (số phòng, loại phòng, mô tả...)
        if (!tuKhoa.isEmpty()) {
            filters.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        Object val = entry.getValue(i);
                        if (val != null && val.toString().toLowerCase().contains(tuKhoa)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // Lọc trạng thái
        if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
            filters.add(RowFilter.regexFilter("^" + trangThaiLoc + "$", 5));
        }

        if (filters.isEmpty()) {
            view.sorter.setRowFilter(null);
        } else {
            view.sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    // Lấy cửa sổ cha của view để căn giữa Modal Dialog
    Window getParentWindow() {
        Window w = SwingUtilities.getWindowAncestor(view.getContentPane());
        return w != null ? w : view;
    }

    // Tìm phòng theo mã phòng
    rooms timTheoMa(int ma) {
        for (rooms r : service.xuat()) {
            if (r.getMaPhong() == ma) {
                return r;
            }
        }
        return null;
    }

    // Thêm phòng bằng Modal Dialog
    void them() {
        try {
            ArrayList<room_types> dsLoai = roomTypeDAO.xuat();
            DialogThemSuaPhong dialog = new DialogThemSuaPhong(getParentWindow(), null, dsLoai);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                rooms phongMoi = dialog.getRoom();
                boolean canTaoDonSauKhiThem = RoomDAO.chuyenTrangThaiSangSo(phongMoi.getTrangThai()) == 1;
                if (canTaoDonSauKhiThem) {
                    int chon = JOptionPane.showConfirmDialog(
                            view,
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

                if (service.them(phongMoi) > 0) {
                    String thongBao = canTaoDonSauKhiThem
                            ? "Đã thêm phòng ở trạng thái TRỐNG. Hãy tạo đơn và Check-in trong tab Quản lý đặt phòng để ghi nhận khách."
                            : "Thêm phòng mới thành công!";
                    JOptionPane.showMessageDialog(view, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm phòng thất bại! Có thể số phòng đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa phòng bằng Modal Dialog
    void sua() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một phòng trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = view.table.convertRowIndexToModel(selectedRow);
            int maPhong = (int) view.model.getValueAt(modelRow, 0);

            rooms phongCanSua = timTheoMa(maPhong);
            if (phongCanSua == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin phòng đã chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<room_types> dsLoai = roomTypeDAO.xuat();
            String trangThaiTruocKhiSua = phongCanSua.getTrangThai();
            DialogThemSuaPhong dialog = new DialogThemSuaPhong(getParentWindow(), phongCanSua, dsLoai);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                rooms phongCapNhat = dialog.getRoom();
                String huongDanTrangThai = null;
                int trangThaiCu = RoomDAO.chuyenTrangThaiSangSo(trangThaiTruocKhiSua);
                int trangThaiMoi = RoomDAO.chuyenTrangThaiSangSo(phongCapNhat.getTrangThai());

                if (trangThaiCu != trangThaiMoi && (trangThaiCu == 1 || trangThaiMoi == 1)) {
                    huongDanTrangThai = trangThaiMoi == 1
                            ? "Để ghi nhận khách lưu trú, hãy tạo đơn trong tab Quản lý đặt phòng rồi nhấn Nhận phòng (Check-in)."
                            : "Phòng đang có khách. Hãy dùng Trả phòng & Thanh toán trong tab Quản lý đặt phòng để cập nhật trạng thái.";
                    int chon = JOptionPane.showConfirmDialog(
                            view,
                            huongDanTrangThai + "\n\nBạn có muốn lưu các thông tin phòng khác và giữ nguyên trạng thái hiện tại không?",
                            "Cần thao tác qua quản lý đặt phòng",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (chon != JOptionPane.YES_OPTION) {
                        return;
                    }
                    phongCapNhat.setTrangThai(trangThaiTruocKhiSua);
                }

                if (service.sua(phongCapNhat) > 0) {
                    String thongBao = huongDanTrangThai == null
                            ? "Cập nhật phòng thành công!"
                            : "Đã cập nhật thông tin phòng; trạng thái được giữ nguyên.\n" + huongDanTrangThai;
                    JOptionPane.showMessageDialog(view, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa phòng
    void xoa() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một phòng trong bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = view.table.convertRowIndexToModel(selectedRow);
            int maPhong = (int) view.model.getValueAt(modelRow, 0);
            String soPhong = view.model.getValueAt(modelRow, 1).toString();

            rooms phongCanXoa = timTheoMa(maPhong);
            if (phongCanXoa == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int chon = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc chắn muốn xóa phòng " + soPhong + " không?",
                "Xác nhận xóa phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (chon == JOptionPane.YES_OPTION) {
                if (service.xoa(phongCanXoa) > 0) {
                    JOptionPane.showMessageDialog(view, "Đã xóa phòng " + soPhong + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Không thể xóa phòng " + soPhong + "! (Có thể phòng đang có hóa đơn/đặt phòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hiển thị chuỗi trạng thái tiếng Việt
    String hienThiTrangThai(String s) {
        if (s == null) return "TRỐNG";
        switch (s.trim().toUpperCase()) {
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
                return s;
        }
    }
}
