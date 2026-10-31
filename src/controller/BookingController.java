package controller;

import java.awt.Window;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.bookings;
import service.BookingService;
import view.DialogThemSuaDatPhong;
import view.DialogTraPhongThanhToan;
import view.ViewDatPhong;

public class BookingController {
    ViewDatPhong view;
    BookingService service = new BookingService();

    public BookingController(ViewDatPhong view) {
        this.view = view;

        taiDuLieu();

        // Nút thêm, sửa, check-in, check-out, hủy
        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnCheckIn.addActionListener(e -> checkIn());
        view.btnCheckOut.addActionListener(e -> checkOut());
        view.btnHuyDat.addActionListener(e -> huyDat());

        // Tìm kiếm & Lọc
        view.btnTimKiem.addActionListener(e -> apDungBoLoc());
        view.txtTimKiem.addActionListener(e -> apDungBoLoc());
        view.cboLocTrangThai.addActionListener(e -> apDungBoLoc());
        view.cboLocThoiGian.addActionListener(e -> apDungBoLoc());

        // Nút làm mới (5s countdown & disable)
        view.btnLamMoi.addActionListener(e -> {
            view.batDauDemNguocLamMoi(() -> {
                view.lamMoi();
                taiDuLieu();
            });
        });
    }

    public void taiDuLieu() {
        try {
            view.model.setRowCount(0);

            ArrayList<bookings> ds = service.xuat();
            int tong = ds.size();
            int daNhan = 0;
            int dangCho = 0;

            for (bookings b : ds) {
                String tt = hienThiTrangThai(b.getTrangThai());
                if (tt.contains("Đang ở") || tt.contains("Đã nhận"))
                    daNhan++;
                else if (tt.contains("Đã đặt") || tt.contains("Chờ"))
                    dangCho++;

                String cccd = b.getCccd() != null ? b.getCccd() : "";
                String soPhong = !b.getSoPhong().isEmpty() ? "P." + b.getSoPhong() : "";
                String tenKhach = b.getTenKhachHang() != null ? b.getTenKhachHang() : "";
                String sdt = b.getSoDienThoai() != null ? b.getSoDienThoai() : "";
                int soNguoi = b.getSoNguoi() > 0 ? b.getSoNguoi() : 1;

                view.model.addRow(new Object[] {
                        b.getIdDatPhong(),
                        soPhong,
                        tenKhach,
                        sdt,
                        cccd,
                        b.getNgayNhanPhong(),
                        b.getNgayTraPhong(),
                        soNguoi,
                        tt,
                        b.getGhiChu() != null ? b.getGhiChu() : ""
                });
            }

            view.lblThongKe.setText(String.format("Tổng số: %d đơn | Đang ở: %d phòng | Chờ nhận phòng: %d",
                    tong, daNhan, dangCho));

            apDungBoLoc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void apDungBoLoc() {
        String tuKhoa = chuanHoaTuKhoa(view.txtTimKiem.getText());
        String trangThaiLoc = (String) view.cboLocTrangThai.getSelectedItem();
        String thoiGianLoc = (String) view.cboLocThoiGian.getSelectedItem();

        if (view.sorter == null) return;

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        // 1. Lọc theo mã đơn, phòng, khách hàng, số điện thoại và CCCD
        if (!tuKhoa.isEmpty()) {
            String tuKhoaSo = tuKhoa.matches("[0-9\\s-]+") ? tuKhoa.replaceAll("\\D", "") : "";
            filters.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    for (int i = 0; i <= 4; i++) {
                        Object val = entry.getValue(i);
                        if (val != null && chuanHoaTuKhoa(val.toString()).contains(tuKhoa)) {
                            return true;
                        }
                        if (!tuKhoaSo.isEmpty() && (i == 3 || i == 4) && val != null
                                && val.toString().replaceAll("\\D", "").contains(tuKhoaSo)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // 2. Lọc theo trạng thái
        if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
            String regex = "";
            if (trangThaiLoc.contains("Đang ở")) regex = "Đang ở|ĐÃ NHẬN PHÒNG|CHECKED_IN";
            else if (trangThaiLoc.contains("Chờ")) regex = "Chờ nhận|ĐÃ ĐẶT|BOOKED";
            else if (trangThaiLoc.contains("Đã trả")) regex = "ĐÃ TRẢ PHÒNG|CHECKED_OUT";
            else if (trangThaiLoc.contains("Đã hủy")) regex = "ĐÃ HỦY|CANCELLED";

            if (!regex.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + regex, 8));
            }
        }

        // 3. Lọc theo thời gian (Hôm nay, Tháng này)
        if (thoiGianLoc != null && !thoiGianLoc.equals("Tất cả thời gian")) {
            LocalDate today = LocalDate.now();
            String todayStr = today.toString(); // YYYY-MM-DD
            String currentMonthStr = String.format("%04d-%02d", today.getYear(), today.getMonthValue());

            filters.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    Object valNhan = entry.getValue(5); // Cột ngày nhận
                    Object valTra = entry.getValue(6);  // Cột ngày trả
                    String sNhan = valNhan != null ? valNhan.toString() : "";
                    String sTra = valTra != null ? valTra.toString() : "";

                    if (thoiGianLoc.equals("Hôm nay")) {
                        return sNhan.contains(todayStr) || sTra.contains(todayStr);
                    } else if (thoiGianLoc.equals("Tháng này")) {
                        return sNhan.startsWith(currentMonthStr) || sTra.startsWith(currentMonthStr);
                    }
                    return true;
                }
            });
        }

        if (filters.isEmpty()) {
            view.sorter.setRowFilter(null);
        } else {
            view.sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private String chuanHoaTuKhoa(String value) {
        if (value == null) return "";
        String normalized = Normalizer.normalize(value.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return normalized.replace('đ', 'd').trim();
    }

    // Tìm kiếm toàn bộ đơn đặt phòng theo từ khóa (CCCD, tên khách...), tất cả trạng thái, toàn thời gian
    public void timKiemToanBo(String tuKhoa) {
        view.txtTimKiem.setText(tuKhoa != null ? tuKhoa.trim() : "");
        view.cboLocTrangThai.setSelectedIndex(0); // Tất cả trạng thái
        view.cboLocThoiGian.setSelectedIndex(0);  // Tất cả thời gian
        apDungBoLoc();
    }

    Window getParentWindow() {
        Window w = SwingUtilities.getWindowAncestor(view.getContentPane());
        return w != null ? w : view;
    }

    bookings timTheoMa(int id) {
        bookings t = new bookings();
        t.setIdDatPhong(id);
        return service.timKiem(t);
    }

    String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "Chờ nhận phòng";
        switch (trangThai.trim().toUpperCase()) {
            case "CHECKED_IN":
            case "ĐÃ NHẬN PHÒNG":
            case "1":
                return "Đang ở";
            case "CHECKED_OUT":
            case "ĐÃ TRẢ PHÒNG":
            case "2":
                return "Đã trả phòng";
            case "CANCELLED":
            case "ĐÃ HỦY":
            case "3":
                return "Đã hủy";
            default:
                return "Chờ nhận phòng";
        }
    }

    // Đặt phòng mới bằng Modal Dialog
    void them() {
        try {
            DialogThemSuaDatPhong dialog = new DialogThemSuaDatPhong(getParentWindow(), null);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                bookings b = dialog.getBooking();
                int bookingId = service.them(b);
                if (bookingId > 0) {
                    JOptionPane.showMessageDialog(view, "Đặt phòng mới thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Đặt phòng thất bại! Vui lòng thử lại.", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa đơn đặt phòng bằng Modal Dialog
    void sua() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một đơn đặt phòng trong bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = view.table.convertRowIndexToModel(selectedRow);
            int maDP = (int) view.model.getValueAt(modelRow, 0);

            bookings canSua = timTheoMa(maDP);
            if (canSua == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đơn đặt phòng!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DialogThemSuaDatPhong dialog = new DialogThemSuaDatPhong(getParentWindow(), canSua);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                bookings capNhat = dialog.getBooking();
                if (service.sua(capNhat) > 0) {
                    JOptionPane.showMessageDialog(view, "Cập nhật đơn đặt phòng thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật đơn đặt phòng thất bại!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Nhận phòng (Check-in)
    void checkIn() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đơn đặt phòng cần Check-in!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = view.table.convertRowIndexToModel(row);
        int id = (int) view.model.getValueAt(modelRow, 0);
        String trangThai = view.model.getValueAt(modelRow, 8).toString();

        if (!trangThai.contains("Chờ") && !trangThai.contains("Đã đặt") && !trangThai.equalsIgnoreCase("BOOKED")) {
            JOptionPane.showMessageDialog(view, "Chỉ những đơn ở trạng thái 'Chờ nhận phòng' mới có thể Check-in!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cf = JOptionPane.showConfirmDialog(
                view,
                "Xác nhận thực hiện Check-in nhận phòng cho đơn đặt #" + id + "?\nPhòng sẽ được chuyển sang trạng thái CÓ KHÁCH.",
                "Xác nhận Check-in",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (cf == JOptionPane.YES_OPTION) {
            if (service.nhanPhong(id)) {
                JOptionPane.showMessageDialog(view, "Check-in thành công! Phòng đã chuyển sang CÓ KHÁCH.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Check-in thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Trả phòng & Thanh toán (Check-out) - Hiện Bill chi tiết trước khi xác nhận
    void checkOut() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đơn đặt phòng đang lưu trú cần Trả phòng!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = view.table.convertRowIndexToModel(row);
        int id = (int) view.model.getValueAt(modelRow, 0);
        String trangThai = view.model.getValueAt(modelRow, 8).toString();

        if (!trangThai.contains("Đang ở") && !trangThai.contains("Đã nhận") && !trangThai.equalsIgnoreCase("CHECKED_IN")) {
            JOptionPane.showMessageDialog(view, "Khách chưa nhận phòng (Đang ở) nên không thể thực hiện Trả phòng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        bookings b = timTheoMa(id);
        if (b == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đơn đặt phòng #" + id, "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mở Modal Trả phòng & Thanh toán (Check-out Bill)
        DialogTraPhongThanhToan dlg = new DialogTraPhongThanhToan(getParentWindow(), b);
        dlg.setVisible(true);

        if (dlg.isCompleted()) {
            taiDuLieu();
        }
    }

    // Hủy đặt phòng
    void huyDat() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đơn đặt phòng cần hủy!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = view.table.convertRowIndexToModel(row);
        int id = (int) view.model.getValueAt(modelRow, 0);
        String trangThai = view.model.getValueAt(modelRow, 8).toString();

        if (trangThai.contains("Đã trả") || trangThai.contains("Đã hủy")) {
            JOptionPane.showMessageDialog(view, "Đơn đặt phòng này đã kết thúc hoặc đã hủy từ trước!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn hủy đơn đặt phòng #" + id + " không?\nNếu phòng đang được giữ, hệ thống sẽ trả lại phòng trống.",
                "Xác nhận hủy đặt phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (chon == JOptionPane.YES_OPTION) {
            if (service.huyDatPhong(id)) {
                JOptionPane.showMessageDialog(view, "Đã hủy đơn đặt phòng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Hủy đơn đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
