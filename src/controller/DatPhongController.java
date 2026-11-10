package controller;

import java.awt.Window;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.DatPhong;
import service.DatPhongService;
import view.dialog.DialogThemSuaDatPhong;
import view.dialog.DialogTraPhongThanhToan;
import view.view.ViewDatPhong;

public class DatPhongController {
    ViewDatPhong viewDatPhong;
    DatPhongService dichVu = new DatPhongService();

    public DatPhongController(ViewDatPhong viewDatPhong) {
        this.viewDatPhong = viewDatPhong;

        taiDuLieu();

        // Nút thêm, sửa, check-in, check-out, hủy
        viewDatPhong.btThem.addActionListener(ac -> them());
        viewDatPhong.btSua.addActionListener(ac -> sua());
        viewDatPhong.btKiemTraIn.addActionListener(ac -> checkIn());
        viewDatPhong.btKiemTraOut.addActionListener(ac -> checkOut());
        viewDatPhong.btHuyDat.addActionListener(ac -> huyDat());

        // Tìm kiếm & Lọc
        viewDatPhong.btTimKiem.addActionListener(ac -> apDungBoLoc());
        viewDatPhong.oNhapTimKiem.addActionListener(ac -> apDungBoLoc());
        viewDatPhong.hopChonLocTrangThai.addActionListener(ac -> apDungBoLoc());
        viewDatPhong.hopChonLocThoiGian.addActionListener(ac -> apDungBoLoc());

        viewDatPhong.btLamMoi.addActionListener(ac -> {
            viewDatPhong.lamMoi();
            taiDuLieu();
        });
    }

    public void taiDuLieu() {
        try {
            viewDatPhong.moHinh.setRowCount(0);

            ArrayList<DatPhong> danhSach = dichVu.xuat();
            int chung = danhSach.size();
            int daNhan = 0;
            int dangCho = 0;

            for (DatPhong nut : danhSach) {
                String trangThai = hienThiTrangThai(nut.getTrangThai());
                if (trangThai.contains("Đang ở") || trangThai.contains("Đã nhận"))
                    daNhan++;
                else if (trangThai.contains("Đã đặt") || trangThai.contains("Chờ"))
                    dangCho++;

                String cccd = nut.getCccd() != null ? nut.getCccd() : "";
                String soPhong = !nut.getSoPhong().isEmpty() ? "P." + nut.getSoPhong() : "";
                String tenKhach = nut.getTenKhachHang() != null ? nut.getTenKhachHang() : "";
                String sdt = nut.getSoDienThoai() != null ? nut.getSoDienThoai() : "";
                int soNguoi = nut.getSoNguoi() > 0 ? nut.getSoNguoi() : 1;

                viewDatPhong.moHinh.addRow(new Object[] {
                        nut.getIdDatPhong(),
                        soPhong,
                        tenKhach,
                        sdt,
                        cccd,
                        nut.getNgayNhanPhong(),
                        nut.getNgayTraPhong(),
                        soNguoi,
                        trangThai,
                        nut.getGhiChu() != null ? nut.getGhiChu() : ""
                });
            }

            viewDatPhong.nhanThongKe.setText(String.format("Tổng số: %d đơn | Đang ở: %d phòng | Chờ nhận phòng: %d",
                    chung, daNhan, dangCho));

            apDungBoLoc();
        } catch (Exception suKien) {
            suKien.printStackTrace();
        }
    }

    void apDungBoLoc() {
        String tuKhoa = chuanHoaTuKhoa(viewDatPhong.oNhapTimKiem.getText());
        String trangThaiLoc = (String) viewDatPhong.hopChonLocTrangThai.getSelectedItem();
        String thoiGianLoc = (String) viewDatPhong.hopChonLocThoiGian.getSelectedItem();

        if (viewDatPhong.boSapXep == null) return;

        List<RowFilter<DefaultTableModel, Object>> cacBoLoc = new ArrayList<>();

        // 1. Lọc theo mã đơn, phòng, khách hàng, số điện thoại và CCCD
        if (!tuKhoa.isEmpty()) {
            String tuKhoaSo = tuKhoa.matches("[0-9\\s-]+") ? tuKhoa.replaceAll("\\D", "") : "";
            cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
                    for (int chiSo = 0; chiSo <= 4; chiSo++) {
                        Object giaTri = phanTu.getValue(chiSo);
                        if (giaTri != null && chuanHoaTuKhoa(giaTri.toString()).contains(tuKhoa)) {
                            return true;
                        }
                        if (!tuKhoaSo.isEmpty() && (chiSo == 3 || chiSo == 4) && giaTri != null
                                && giaTri.toString().replaceAll("\\D", "").contains(tuKhoaSo)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // 2. Lọc theo trạng thái
        if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
            String trangThaiCanLoc = null;
            if (trangThaiLoc.startsWith("Đang ở")) trangThaiCanLoc = "Đang ở";
            else if (trangThaiLoc.startsWith("Chờ nhận")) trangThaiCanLoc = "Chờ nhận phòng";
            else if (trangThaiLoc.startsWith("Đã trả")) trangThaiCanLoc = "Đã trả phòng";
            else if (trangThaiLoc.startsWith("Đã hủy")) trangThaiCanLoc = "Đã hủy";

            if (trangThaiCanLoc != null) {
                String trangThaiMucTieu = trangThaiCanLoc;
                cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
                        Object giaTri = phanTu.getValue(8);
                        return giaTri != null && trangThaiMucTieu.equalsIgnoreCase(giaTri.toString().trim());
                    }
                });
            }
        }

        // 3. Lọc theo thời gian (Hôm nay, Tháng này)
        if (thoiGianLoc != null && !thoiGianLoc.equals("Tất cả thời gian")) {
            LocalDate homNay = LocalDate.now();
            String chuoiHomNay = homNay.toString(); // YYYY-MM-DD
            String chuoiThangHienTai = String.format("%04d-%02d", homNay.getYear(), homNay.getMonthValue());

            cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
                    Object giaTriNhan = phanTu.getValue(5); // Cột ngày nhận
                    Object giaTriTra = phanTu.getValue(6);  // Cột ngày trả
                    String chuoiNhan = giaTriNhan != null ? giaTriNhan.toString() : "";
                    String chuoiTra = giaTriTra != null ? giaTriTra.toString() : "";

                    if (thoiGianLoc.equals("Hôm nay")) {
                        return chuoiNhan.contains(chuoiHomNay) || chuoiTra.contains(chuoiHomNay);
                    } else if (thoiGianLoc.equals("Tháng này")) {
                        return chuoiNhan.startsWith(chuoiThangHienTai) || chuoiTra.startsWith(chuoiThangHienTai);
                    }
                    return true;
                }
            });
        }

        if (cacBoLoc.isEmpty()) {
            viewDatPhong.boSapXep.setRowFilter(null);
        } else {
            viewDatPhong.boSapXep.setRowFilter(RowFilter.andFilter(cacBoLoc));
        }
    }

    private String chuanHoaTuKhoa(String giaTri) {
        if (giaTri == null) return "";
        String daChuanHoa = Normalizer.normalize(giaTri.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return daChuanHoa.replace('đ', 'd').trim();
    }

    // Tìm kiếm toàn bộ đơn đặt phòng theo từ khóa (CCCD, tên khách...), tất cả trạng thái, toàn thời gian
    public void timKiemToanBo(String tuKhoa) {
        viewDatPhong.oNhapTimKiem.setText(tuKhoa != null ? tuKhoa.trim() : "");
        viewDatPhong.hopChonLocTrangThai.setSelectedIndex(0); // Tất cả trạng thái
        viewDatPhong.hopChonLocThoiGian.setSelectedIndex(0);  // Tất cả thời gian
        apDungBoLoc();
    }

    Window getParentWindow() {
        Window chieuRong = SwingUtilities.getWindowAncestor(viewDatPhong.getContentPane());
        return chieuRong != null ? chieuRong : viewDatPhong;
    }

    DatPhong timTheoMa(int ma) {
        DatPhong bangHienTai = new DatPhong();
        bangHienTai.setIdDatPhong(ma);
        return dichVu.timKiem(bangHienTai);
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
            DialogThemSuaDatPhong hopThoai = new DialogThemSuaDatPhong(getParentWindow(), null);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                DatPhong nut = hopThoai.getBooking();
                int maDatPhong = dichVu.them(nut);
                if (maDatPhong > 0) {
                    JOptionPane.showMessageDialog(viewDatPhong, "Đặt phòng mới thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else if (dichVu.phongBiDatTrung(nut.getMaPhong(), nut.getNgayNhanPhong(),
                        nut.getNgayTraPhong(), nut.getIdDatPhong())) {
                    JOptionPane.showMessageDialog(viewDatPhong,
                            "Phòng đã được đặt trong khoảng thời gian này. Vui lòng chọn phòng khác hoặc điều chỉnh ngày lưu trú.",
                            "Phòng không khả dụng", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(viewDatPhong, "Đặt phòng thất bại! Vui lòng thử lại.", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewDatPhong, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa đơn đặt phòng bằng Modal Dialog
    void sua() {
        int hangDaChon = viewDatPhong.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewDatPhong, "Vui lòng chọn một đơn đặt phòng trong bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int hangMoHinh = viewDatPhong.bang.convertRowIndexToModel(hangDaChon);
            int maDP = (int) viewDatPhong.moHinh.getValueAt(hangMoHinh, 0);

            DatPhong canSua = timTheoMa(maDP);
            if (canSua == null) {
                JOptionPane.showMessageDialog(viewDatPhong, "Không tìm thấy thông tin đơn đặt phòng!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String trangThai = hienThiTrangThai(canSua.getTrangThai());
            if ("Đã hủy".equals(trangThai) || "Đã trả phòng".equals(trangThai)) {
                JOptionPane.showMessageDialog(
                        viewDatPhong,
                        "Không thể sửa đơn đặt phòng đã " + ("Đã hủy".equals(trangThai) ? "hủy." : "trả phòng."),
                        "Không thể sửa đơn",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            DialogThemSuaDatPhong hopThoai = new DialogThemSuaDatPhong(getParentWindow(), canSua);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                DatPhong capNhat = hopThoai.getBooking();
                if (dichVu.sua(capNhat) > 0) {
                    JOptionPane.showMessageDialog(viewDatPhong, "Cập nhật đơn đặt phòng thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else if (dichVu.phongBiDatTrung(capNhat.getMaPhong(), capNhat.getNgayNhanPhong(),
                        capNhat.getNgayTraPhong(), capNhat.getIdDatPhong())) {
                    JOptionPane.showMessageDialog(viewDatPhong,
                            "Phòng đã được đặt trong khoảng thời gian này. Vui lòng chọn phòng khác hoặc điều chỉnh ngày lưu trú.",
                            "Phòng không khả dụng", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(viewDatPhong, "Cập nhật đơn đặt phòng thất bại!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewDatPhong, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Nhận phòng (Check-in)
    void checkIn() {
        int hang = viewDatPhong.bang.getSelectedRow();
        if (hang == -1) {
            JOptionPane.showMessageDialog(viewDatPhong, "Vui lòng chọn đơn đặt phòng cần Check-in!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangMoHinh = viewDatPhong.bang.convertRowIndexToModel(hang);
        int ma = (int) viewDatPhong.moHinh.getValueAt(hangMoHinh, 0);
        String trangThai = viewDatPhong.moHinh.getValueAt(hangMoHinh, 8).toString();

        if (!trangThai.contains("Chờ") && !trangThai.contains("Đã đặt") && !trangThai.equalsIgnoreCase("BOOKED")) {
            JOptionPane.showMessageDialog(viewDatPhong, "Chỉ những đơn ở trạng thái 'Chờ nhận phòng' mới có thể Check-in!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tepCauHinh = JOptionPane.showConfirmDialog(
                viewDatPhong,
                "Xác nhận thực hiện Check-in nhận phòng cho đơn đặt #" + ma + "?\nPhòng sẽ được chuyển sang trạng thái CÓ KHÁCH.",
                "Xác nhận Check-in",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (tepCauHinh == JOptionPane.YES_OPTION) {
            if (dichVu.nhanPhong(ma)) {
                JOptionPane.showMessageDialog(viewDatPhong, "Check-in thành công! Phòng đã chuyển sang CÓ KHÁCH.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(viewDatPhong, "Check-in thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Trả phòng & Thanh toán (Check-out) - Hiện Bill chi tiết trước khi xác nhận
    void checkOut() {
        int hang = viewDatPhong.bang.getSelectedRow();
        if (hang == -1) {
            JOptionPane.showMessageDialog(viewDatPhong, "Vui lòng chọn đơn đặt phòng đang lưu trú cần Trả phòng!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangMoHinh = viewDatPhong.bang.convertRowIndexToModel(hang);
        int ma = (int) viewDatPhong.moHinh.getValueAt(hangMoHinh, 0);
        String trangThai = viewDatPhong.moHinh.getValueAt(hangMoHinh, 8).toString();

        if (!trangThai.contains("Đang ở") && !trangThai.contains("Đã nhận") && !trangThai.equalsIgnoreCase("CHECKED_IN")) {
            JOptionPane.showMessageDialog(viewDatPhong, "Khách chưa nhận phòng (Đang ở) nên không thể thực hiện Trả phòng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DatPhong nut = timTheoMa(ma);
        if (nut == null) {
            JOptionPane.showMessageDialog(viewDatPhong, "Không tìm thấy thông tin đơn đặt phòng #" + ma, "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mở Modal Trả phòng & Thanh toán (Check-out Bill)
        DialogTraPhongThanhToan hopThoai = new DialogTraPhongThanhToan(getParentWindow(), nut);
        hopThoai.setVisible(true);

        if (hopThoai.isCompleted()) {
            taiDuLieu();
        }
    }

    // Hủy đặt phòng
    void huyDat() {
        int hang = viewDatPhong.bang.getSelectedRow();
        if (hang == -1) {
            JOptionPane.showMessageDialog(viewDatPhong, "Vui lòng chọn đơn đặt phòng cần hủy!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangMoHinh = viewDatPhong.bang.convertRowIndexToModel(hang);
        int ma = (int) viewDatPhong.moHinh.getValueAt(hangMoHinh, 0);
        String trangThai = viewDatPhong.moHinh.getValueAt(hangMoHinh, 8).toString();

        if (trangThai.contains("Đã trả") || trangThai.contains("Đã hủy")) {
            JOptionPane.showMessageDialog(viewDatPhong, "Đơn đặt phòng này đã kết thúc hoặc đã hủy từ trước!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
                viewDatPhong,
                "Bạn có chắc muốn hủy đơn đặt phòng #" + ma + " không?\nNếu phòng đang được giữ, hệ thống sẽ trả lại phòng trống.",
                "Xác nhận hủy đặt phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (chon == JOptionPane.YES_OPTION) {
            if (dichVu.huyDatPhong(ma)) {
                JOptionPane.showMessageDialog(viewDatPhong, "Đã hủy đơn đặt phòng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(viewDatPhong, "Hủy đơn đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
