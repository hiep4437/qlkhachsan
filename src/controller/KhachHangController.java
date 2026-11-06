package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.DatPhong;
import model.KhachHang;
import service.DatPhongService;
import service.KhachHangService;
import view.dialog.DialogThemSuaKhachHang;
import view.view.ViewQuanLyKhachHang;

public class KhachHangController {

    ViewQuanLyKhachHang viewQuanLyKhachHang;
    KhachHangService dichVu = new KhachHangService();
    DatPhongService dichVuDatPhong = new DatPhongService();
    java.util.function.Consumer<String> khiChuyenTheDatPhong;

    public KhachHangController(ViewQuanLyKhachHang viewQuanLyKhachHang) {
        this(viewQuanLyKhachHang, null);
    }

    public KhachHangController(ViewQuanLyKhachHang viewQuanLyKhachHang, java.util.function.Consumer<String> khiChuyenTheDatPhong) {
        this.viewQuanLyKhachHang = viewQuanLyKhachHang;
        this.khiChuyenTheDatPhong = khiChuyenTheDatPhong;

        taiDuLieu();

        // Nút thêm, sửa, lịch sử, xóa
        viewQuanLyKhachHang.nutThem.addActionListener(suKien -> them());
        viewQuanLyKhachHang.nutSua.addActionListener(suKien -> sua());
        viewQuanLyKhachHang.nutLichSu.addActionListener(suKien -> xemLichSu());
        viewQuanLyKhachHang.nutXoa.addActionListener(suKien -> xoa());

        // Tìm kiếm
        viewQuanLyKhachHang.nutTimKiem.addActionListener(suKien -> apDungBoLoc());
        viewQuanLyKhachHang.oNhapTimKiem.addActionListener(suKien -> apDungBoLoc());

        viewQuanLyKhachHang.nutLamMoi.addActionListener(suKien -> {
            viewQuanLyKhachHang.lamMoi();
            taiDuLieu();
        });
    }

    public void taiDuLieu() {
        viewQuanLyKhachHang.moHinh.setRowCount(0);

        ArrayList<KhachHang> danhSach = dichVu.xuat();
        for (KhachHang khachHang : danhSach) {
            int soLan = dichVu.demSoLanDatPhong(khachHang.getIdKhachHang());

            viewQuanLyKhachHang.moHinh.addRow(new Object[] {
                khachHang.getIdKhachHang(),
                khachHang.getNameKhachHang(),
                khachHang.getSoCCCD() != null ? khachHang.getSoCCCD() : "",
                khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "",
                khachHang.getEmail() != null ? khachHang.getEmail() : "",
                khachHang.getDiaChi() != null ? khachHang.getDiaChi() : "",
                soLan
            });
        }

        viewQuanLyKhachHang.nhanThongKe.setText("Tổng số: " + danhSach.size() + " khách hàng trong hệ thống");
        apDungBoLoc();
    }

    void apDungBoLoc() {
        String tuKhoa = viewQuanLyKhachHang.oNhapTimKiem.getText().trim().toLowerCase();

        if (viewQuanLyKhachHang.boSapXep == null) return;

        if (tuKhoa.isEmpty()) {
            viewQuanLyKhachHang.boSapXep.setRowFilter(null);
            return;
        }

        viewQuanLyKhachHang.boSapXep.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
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

    Window getParentWindow() {
        Window chieuRong = SwingUtilities.getWindowAncestor(viewQuanLyKhachHang.getContentPane());
        return chieuRong != null ? chieuRong : viewQuanLyKhachHang;
    }

    KhachHang timTheoMa(int ma) {
        KhachHang bangHienTai = new KhachHang();
        bangHienTai.setIdKhachHang(ma);
        return dichVu.timKiem(bangHienTai);
    }

    // Xem lịch sử đặt phòng của khách: Chuyển sang tab Quản lý đặt phòng và tự tìm theo CCCD (tất cả trạng thái, toàn thời gian)
    void xemLichSu() {
        int hangDaChon = viewQuanLyKhachHang.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Vui lòng chọn một khách hàng trong bảng để xem lịch sử đặt phòng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangMoHinh = viewQuanLyKhachHang.bang.convertRowIndexToModel(hangDaChon);
        String cccd = (String) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 2);
        String tenKhach = (String) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 1);

        if (cccd == null || cccd.trim().isEmpty()) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Khách hàng " + tenKhach + " chưa cập nhật số CCCD nên không thể tra cứu!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cccdTim = cccd.trim();

        // 1. Kiểm tra nếu có callback
        if (khiChuyenTheDatPhong != null) {
            khiChuyenTheDatPhong.accept(cccdTim);
            return;
        }

        // 2. Kiểm tra nếu Window cha là ViewTrangChu
        Window doiTuongCha = getParentWindow();
        if (doiTuongCha instanceof view.view.ViewTrangChu) {
            ((view.view.ViewTrangChu) doiTuongCha).chuyenSangTabDatPhongVaTimKiem(cccdTim);
            return;
        }

        // 3. Fallback: Nếu không trong ViewTrangChu thì mở modal
        moModalLichSuDatPhong(hangMoHinh, tenKhach, cccdTim);
    }

    private void moModalLichSuDatPhong(int hangMoHinh, String tenKhach, String cccd) {
        int maKhachHang = (int) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 0);

        // Lấy tất cả các đơn đặt phòng của khách này
        ArrayList<DatPhong> tatCa = dichVuDatPhong.xuat();
        ArrayList<DatPhong> cuaKhach = new ArrayList<>();
        for (DatPhong nut : tatCa) {
            if (nut.getIdKhachHang() == maKhachHang || (cccd != null && !cccd.isEmpty() && cccd.equals(nut.getCccd()))) {
                cuaKhach.add(nut);
            }
        }

        JDialog hopThoai = new JDialog(getParentWindow(), "Lịch sử thuê phòng - Khách hàng: " + tenKhach, JDialog.ModalityType.APPLICATION_MODAL);
        hopThoai.setSize(800, 480);
        hopThoai.setLocationRelativeTo(getParentWindow());
        hopThoai.setLayout(new BorderLayout());

        JPanel khungDau = new JPanel(new BorderLayout());
        khungDau.setBackground(new Color(24, 44, 97));
        khungDau.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel nhanTieuDe = new JLabel("LỊCH SỬ THUÊ PHÒNG CỦA KHÁCH HÀNG: " + tenKhach.toUpperCase());
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel("Tổng số lần đặt: " + cuaKhach.size() + " lượt | CCCD: " + cccd);
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(200, 220, 240));

        khungDau.add(nhanTieuDe, BorderLayout.NORTH);
        khungDau.add(nhanPhu, BorderLayout.SOUTH);
        hopThoai.add(khungDau, BorderLayout.NORTH);

        String[] cacCot = { "Mã ĐP", "Phòng", "Loại phòng", "Ngày nhận", "Ngày trả", "Số người", "Trạng thái", "Ghi chú" };
        DefaultTableModel moHinhLichSu = new DefaultTableModel(cacCot, 0) {
            @Override
            public boolean isCellEditable(int hang, int chiSoCot) {
                return false;
            }
        };

        for (DatPhong nut : cuaKhach) {
            String trangThai = nut.getTrangThai();
            if (trangThai.contains("CHECKED_IN") || trangThai.contains("Đã nhận")) trangThai = "Đang ở";
            else if (trangThai.contains("CHECKED_OUT") || trangThai.contains("Đã trả")) trangThai = "Đã trả phòng";
            else if (trangThai.contains("CANCELLED") || trangThai.contains("Đã hủy")) trangThai = "Đã hủy";
            else trangThai = "Chờ nhận phòng";

            moHinhLichSu.addRow(new Object[] {
                nut.getIdDatPhong(),
                !nut.getSoPhong().isEmpty() ? "P." + nut.getSoPhong() : "---",
                nut.getTenLoaiPhong(),
                nut.getNgayNhanPhong(),
                nut.getNgayTraPhong(),
                nut.getSoNguoi(),
                trangThai,
                nut.getGhiChu() != null ? nut.getGhiChu() : ""
            });
        }

        JTable bangLichSu = new JTable(moHinhLichSu);
        bangLichSu.setRowHeight(32);
        bangLichSu.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer giua = new DefaultTableCellRenderer();
        giua.setHorizontalAlignment(SwingConstants.CENTER);
        bangLichSu.getColumnModel().getColumn(0).setCellRenderer(giua);
        bangLichSu.getColumnModel().getColumn(1).setCellRenderer(giua);
        bangLichSu.getColumnModel().getColumn(3).setCellRenderer(giua);
        bangLichSu.getColumnModel().getColumn(4).setCellRenderer(giua);
        bangLichSu.getColumnModel().getColumn(5).setCellRenderer(giua);
        bangLichSu.getColumnModel().getColumn(6).setCellRenderer(giua);

        hopThoai.add(new JScrollPane(bangLichSu), BorderLayout.CENTER);

        JPanel khungChan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nutHang = new JButton("Đóng");
        nutHang.addActionListener(suKien -> hopThoai.dispose());
        khungChan.add(nutHang);
        hopThoai.add(khungChan, BorderLayout.SOUTH);

        hopThoai.setVisible(true);
    }

    // Thêm khách hàng bằng Modal Dialog
    void them() {
        try {
            DialogThemSuaKhachHang hopThoai = new DialogThemSuaKhachHang(getParentWindow(), null);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                KhachHang khoa = hopThoai.getCustomer();
                if (dichVu.them(khoa) > 0) {
                    JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Thêm thất bại! Số CCCD có thể đã tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa khách hàng bằng Modal Dialog
    void sua() {
        int hangDaChon = viewQuanLyKhachHang.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Vui lòng chọn một khách hàng trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int hangMoHinh = viewQuanLyKhachHang.bang.convertRowIndexToModel(hangDaChon);
            int maKhachHang = (int) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 0);

            KhachHang canSua = timTheoMa(maKhachHang);
            if (canSua == null) {
                JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DialogThemSuaKhachHang hopThoai = new DialogThemSuaKhachHang(getParentWindow(), canSua);
            hopThoai.setVisible(true);

            if (hopThoai.isSaved()) {
                KhachHang capNhat = hopThoai.getCustomer();
                if (dichVu.sua(capNhat) > 0) {
                    JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Cập nhật khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Lỗi: " + suKien.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa khách hàng
    void xoa() {
        int hangDaChon = viewQuanLyKhachHang.bang.getSelectedRow();
        if (hangDaChon == -1) {
            JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Vui lòng chọn một khách hàng trong bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangMoHinh = viewQuanLyKhachHang.bang.convertRowIndexToModel(hangDaChon);
        int maKhachHang = (int) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 0);
        String hoTen = (String) viewQuanLyKhachHang.moHinh.getValueAt(hangMoHinh, 1);

        int chon = JOptionPane.showConfirmDialog(
                viewQuanLyKhachHang,
                "Bạn có chắc muốn xóa khách hàng \"" + hoTen + "\" (Mã KH: " + maKhachHang + ") không?\n"
                        + "Lưu ý: Không thể xóa nếu khách hàng đang có đơn đặt phòng hoặc hóa đơn liên quan.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (chon == JOptionPane.YES_OPTION) {
            KhachHang canXoa = new KhachHang();
            canXoa.setIdKhachHang(maKhachHang);
            if (dichVu.xoa(canXoa) > 0) {
                JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Xóa khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(viewQuanLyKhachHang, "Xóa thất bại! Khách hàng này có dữ liệu đặt phòng hoặc hóa đơn ràng buộc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
