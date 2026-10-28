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

import model.bookings;
import model.customers;
import service.BookingService;
import service.CustomerService;
import view.DialogThemSuaKhachHang;
import view.ViewQuanLyKhachHang;

public class CustomerController {

    ViewQuanLyKhachHang view;
    CustomerService service = new CustomerService();
    BookingService bookingService = new BookingService();
    java.util.function.Consumer<String> onChuyenTabDatPhong;

    public CustomerController(ViewQuanLyKhachHang view) {
        this(view, null);
    }

    public CustomerController(ViewQuanLyKhachHang view, java.util.function.Consumer<String> onChuyenTabDatPhong) {
        this.view = view;
        this.onChuyenTabDatPhong = onChuyenTabDatPhong;

        taiDuLieu();

        // Nút thêm, sửa, lịch sử, xóa
        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnLichSu.addActionListener(e -> xemLichSu());
        view.btnXoa.addActionListener(e -> xoa());

        // Tìm kiếm
        view.btnTimKiem.addActionListener(e -> apDungBoLoc());
        view.txtTimKiem.addActionListener(e -> apDungBoLoc());

        // Nút làm mới (5s countdown & disable)
        view.btnLamMoi.addActionListener(e -> {
            view.batDauDemNguocLamMoi(() -> {
                view.lamMoi();
                taiDuLieu();
            });
        });
    }

    public void taiDuLieu() {
        view.model.setRowCount(0);

        ArrayList<customers> ds = service.xuat();
        for (customers kh : ds) {
            int soLan = service.demSoLanDatPhong(kh.getIdKhachHang());

            view.model.addRow(new Object[] {
                kh.getIdKhachHang(),
                kh.getNameKhachHang(),
                kh.getSoCCCD() != null ? kh.getSoCCCD() : "",
                kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "",
                kh.getEmail() != null ? kh.getEmail() : "",
                kh.getDiaChi() != null ? kh.getDiaChi() : "",
                soLan
            });
        }

        view.lblThongKe.setText("Tổng số: " + ds.size() + " khách hàng trong hệ thống");
        apDungBoLoc();
    }

    void apDungBoLoc() {
        String tuKhoa = view.txtTimKiem.getText().trim().toLowerCase();

        if (view.sorter == null) return;

        if (tuKhoa.isEmpty()) {
            view.sorter.setRowFilter(null);
            return;
        }

        view.sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
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

    Window getParentWindow() {
        Window w = SwingUtilities.getWindowAncestor(view.getContentPane());
        return w != null ? w : view;
    }

    customers timTheoMa(int id) {
        customers t = new customers();
        t.setIdKhachHang(id);
        return service.timKiem(t);
    }

    // Xem lịch sử đặt phòng của khách: Chuyển sang tab Quản lý đặt phòng và tự tìm theo CCCD (tất cả trạng thái, toàn thời gian)
    void xemLichSu() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng trong bảng để xem lịch sử đặt phòng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = view.table.convertRowIndexToModel(selectedRow);
        String cccd = (String) view.model.getValueAt(modelRow, 2);
        String tenKhach = (String) view.model.getValueAt(modelRow, 1);

        if (cccd == null || cccd.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Khách hàng " + tenKhach + " chưa cập nhật số CCCD nên không thể tra cứu!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cccdTim = cccd.trim();

        // 1. Kiểm tra nếu có callback
        if (onChuyenTabDatPhong != null) {
            onChuyenTabDatPhong.accept(cccdTim);
            return;
        }

        // 2. Kiểm tra nếu Window cha là ViewTrangChu
        Window parent = getParentWindow();
        if (parent instanceof view.ViewTrangChu) {
            ((view.ViewTrangChu) parent).chuyenSangTabDatPhongVaTimKiem(cccdTim);
            return;
        }

        // 3. Fallback: Nếu không trong ViewTrangChu thì mở modal
        moModalLichSuDatPhong(modelRow, tenKhach, cccdTim);
    }

    private void moModalLichSuDatPhong(int modelRow, String tenKhach, String cccd) {
        int maKH = (int) view.model.getValueAt(modelRow, 0);

        // Lấy tất cả các đơn đặt phòng của khách này
        ArrayList<bookings> tatCa = bookingService.xuat();
        ArrayList<bookings> cuaKhach = new ArrayList<>();
        for (bookings b : tatCa) {
            if (b.getIdKhachHang() == maKH || (cccd != null && !cccd.isEmpty() && cccd.equals(b.getCccd()))) {
                cuaKhach.add(b);
            }
        }

        JDialog dlg = new JDialog(getParentWindow(), "Lịch sử thuê phòng - Khách hàng: " + tenKhach, JDialog.ModalityType.APPLICATION_MODAL);
        dlg.setSize(800, 480);
        dlg.setLocationRelativeTo(getParentWindow());
        dlg.setLayout(new BorderLayout());

        JPanel pHead = new JPanel(new BorderLayout());
        pHead.setBackground(new Color(24, 44, 97));
        pHead.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblTitle = new JLabel("LỊCH SỬ THUÊ PHÒNG CỦA KHÁCH HÀNG: " + tenKhach.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Tổng số lần đặt: " + cuaKhach.size() + " lượt | CCCD: " + cccd);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 220, 240));

        pHead.add(lblTitle, BorderLayout.NORTH);
        pHead.add(lblSub, BorderLayout.SOUTH);
        dlg.add(pHead, BorderLayout.NORTH);

        String[] cols = { "Mã ĐP", "Phòng", "Loại phòng", "Ngày nhận", "Ngày trả", "Số người", "Trạng thái", "Ghi chú" };
        DefaultTableModel modelLS = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (bookings b : cuaKhach) {
            String tt = b.getTrangThai();
            if (tt.contains("CHECKED_IN") || tt.contains("Đã nhận")) tt = "Đang ở";
            else if (tt.contains("CHECKED_OUT") || tt.contains("Đã trả")) tt = "Đã trả phòng";
            else if (tt.contains("CANCELLED") || tt.contains("Đã hủy")) tt = "Đã hủy";
            else tt = "Chờ nhận phòng";

            modelLS.addRow(new Object[] {
                b.getIdDatPhong(),
                !b.getSoPhong().isEmpty() ? "P." + b.getSoPhong() : "---",
                b.getTenLoaiPhong(),
                b.getNgayNhanPhong(),
                b.getNgayTraPhong(),
                b.getSoNguoi(),
                tt,
                b.getGhiChu() != null ? b.getGhiChu() : ""
            });
        }

        JTable tblLS = new JTable(modelLS);
        tblLS.setRowHeight(32);
        tblLS.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tblLS.getColumnModel().getColumn(0).setCellRenderer(center);
        tblLS.getColumnModel().getColumn(1).setCellRenderer(center);
        tblLS.getColumnModel().getColumn(3).setCellRenderer(center);
        tblLS.getColumnModel().getColumn(4).setCellRenderer(center);
        tblLS.getColumnModel().getColumn(5).setCellRenderer(center);
        tblLS.getColumnModel().getColumn(6).setCellRenderer(center);

        dlg.add(new JScrollPane(tblLS), BorderLayout.CENTER);

        JPanel pFoot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(ev -> dlg.dispose());
        pFoot.add(btnDong);
        dlg.add(pFoot, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // Thêm khách hàng bằng Modal Dialog
    void them() {
        try {
            DialogThemSuaKhachHang dialog = new DialogThemSuaKhachHang(getParentWindow(), null);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                customers k = dialog.getCustomer();
                if (service.them(k) > 0) {
                    JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm thất bại! Số CCCD có thể đã tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sửa khách hàng bằng Modal Dialog
    void sua() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = view.table.convertRowIndexToModel(selectedRow);
            int maKH = (int) view.model.getValueAt(modelRow, 0);

            customers canSua = timTheoMa(maKH);
            if (canSua == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DialogThemSuaKhachHang dialog = new DialogThemSuaKhachHang(getParentWindow(), canSua);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                customers capNhat = dialog.getCustomer();
                if (service.sua(capNhat) > 0) {
                    JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa khách hàng
    void xoa() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng trong bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = view.table.convertRowIndexToModel(selectedRow);
        int maKH = (int) view.model.getValueAt(modelRow, 0);
        String hoTen = (String) view.model.getValueAt(modelRow, 1);

        int chon = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa khách hàng \"" + hoTen + "\" (Mã KH: " + maKH + ") không?\n"
                        + "Lưu ý: Không thể xóa nếu khách hàng đang có đơn đặt phòng hoặc hóa đơn liên quan.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (chon == JOptionPane.YES_OPTION) {
            customers canXoa = new customers();
            canXoa.setIdKhachHang(maKH);
            if (service.xoa(canXoa) > 0) {
                JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại! Khách hàng này có dữ liệu đặt phòng hoặc hóa đơn ràng buộc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
