package controller;

import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.NguoiDung;
import service.NguoiDungService;
import view.dialog.DialogThemSuaTaiKhoan;
import view.view.ViewQuanLyTaiKhoan;

public class NguoiDungController {

    private ViewQuanLyTaiKhoan view;
    private NguoiDungService service = new NguoiDungService();
    private NguoiDung currentUser;

    public NguoiDungController(ViewQuanLyTaiKhoan view) {
        this(view, null);
    }

    public NguoiDungController(ViewQuanLyTaiKhoan view, NguoiDung currentUser) {
        this.view = view;
        this.currentUser = (currentUser != null)
                ? currentUser
                : new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        taiDuLieu();

        // ── Nút thao tác ──────────────────────────────────────────────────────
        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnXoa.addActionListener(e -> xoa());
        view.btnKhoaMoKhoa.addActionListener(e -> doiTrangThai());
        view.btnDatLaiMatKhau.addActionListener(e -> datLaiMatKhau());

        // ── Tìm kiếm & lọc ────────────────────────────────────────────────────
        view.btnTimKiem.addActionListener(e -> apDungBoLoc());
        view.txtTimKiem.addActionListener(e -> apDungBoLoc());
        view.cboLocVaiTro.addActionListener(e -> apDungBoLoc());
        view.cboLocTrangThai.addActionListener(e -> apDungBoLoc());

        view.btnLamMoiTim.addActionListener(e -> {
            view.txtTimKiem.setText("");
            view.cboLocVaiTro.setSelectedIndex(0);
            view.cboLocTrangThai.setSelectedIndex(0);
            apDungBoLoc();
        });

        view.btnLamMoi.addActionListener(e -> {
            view.lamMoiForm();
            taiDuLieu();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tải dữ liệu toàn bộ lên bảng
    // ─────────────────────────────────────────────────────────────────────────
    public void taiDuLieu() {
        view.model.setRowCount(0);

        ArrayList<NguoiDung> ds = service.xuat();
        int tong  = ds.size();
        int admin = 0;
        int nv    = 0;

        for (NguoiDung u : ds) {
            if ("ADMIN".equalsIgnoreCase(u.getVaiTro())) admin++;
            else nv++;

            String trangThaiHienThi = u.isTrangThai() ? "Hoạt động" : "Đã khóa";
            view.model.addRow(new Object[]{
                u.getIdNguoiDung(),
                u.getTenDangNhap(),
                u.getHoTen(),
                u.getVaiTro(),
                trangThaiHienThi
            });
        }

        view.lblThongKe.setText(String.format(
                "Tổng số: %d tài khoản  |  Admin: %d  |  Nhân viên: %d", tong, admin, nv));

        apDungBoLoc();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lọc RowFilter kết hợp từ khóa + vai trò + trạng thái
    // ─────────────────────────────────────────────────────────────────────────
    void apDungBoLoc() {
        String tuKhoa = view.txtTimKiem.getText().trim().toLowerCase();
        String locVT  = (String) view.cboLocVaiTro.getSelectedItem();
        String locTT  = (String) view.cboLocTrangThai.getSelectedItem();

        if (view.sorter == null) return;

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        if (!tuKhoa.isEmpty()) {
            filters.add(new RowFilter<DefaultTableModel, Object>() {
                @Override public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        Object val = entry.getValue(i);
                        if (val != null && val.toString().toLowerCase().contains(tuKhoa)) return true;
                    }
                    return false;
                }
            });
        }

        if (locVT != null && !locVT.equals("Tất cả vai trò")) {
            filters.add(RowFilter.regexFilter("(?i)^" + locVT + "$", 3));
        }

        if (locTT != null && !locTT.equals("Tất cả trạng thái")) {
            filters.add(RowFilter.regexFilter("(?i)" + locTT, 4));
        }

        view.sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper: cửa sổ cha cho dialog
    // ─────────────────────────────────────────────────────────────────────────
    private Window getParentWindow() {
        Window w = SwingUtilities.getWindowAncestor(view.getContentPane());
        return w != null ? w : view;
    }

    // Helper: lấy ID từ dòng đang chọn trong bảng
    private int getSelectedId() {
        int viewRow = view.table.getSelectedRow();
        if (viewRow == -1) return -1;
        int modelRow = view.table.convertRowIndexToModel(viewRow);
        return (int) view.model.getValueAt(modelRow, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Thêm tài khoản – mở Modal Dialog
    // ─────────────────────────────────────────────────────────────────────────
    private void them() {
        DialogThemSuaTaiKhoan dialog = new DialogThemSuaTaiKhoan(getParentWindow(), null);
        dialog.setVisible(true);

        if (!dialog.isSaved()) return;

        NguoiDung u = dialog.getUser();
        int kq  = service.themNguoiDung(u);

        if (kq == -1) {
            JOptionPane.showMessageDialog(view,
                    "Tên đăng nhập '" + u.getTenDangNhap() + "' đã tồn tại! Vui lòng chọn tên khác.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else if (kq > 0) {
            JOptionPane.showMessageDialog(view,
                    "Thêm tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            taiDuLieu();
            view.lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Sửa tài khoản – mở Modal Dialog (cũng kích hoạt bởi double-click)
    // ─────────────────────────────────────────────────────────────────────────
    private void sua() {
        int id = getSelectedId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn tài khoản trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiDung u = service.timTheoId(id);
        if (u == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DialogThemSuaTaiKhoan dialog = new DialogThemSuaTaiKhoan(getParentWindow(), u);
        dialog.setVisible(true);

        if (!dialog.isSaved()) return;

        NguoiDung capNhat = dialog.getUser();

        // Không cho tự khóa tài khoản đang đăng nhập
        if (capNhat.getIdNguoiDung() == currentUser.getIdNguoiDung() && !capNhat.isTrangThai()) {
            JOptionPane.showMessageDialog(view,
                    "Bạn không thể tự khóa tài khoản của chính mình!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kq = service.suaNguoiDung(capNhat);
        if (kq == -1) {
            JOptionPane.showMessageDialog(view,
                    "Tên đăng nhập '" + capNhat.getTenDangNhap() + "' đã được tài khoản khác sử dụng!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else if (kq > 0) {
            // Cập nhật currentUser nếu sửa chính mình
            if (id == currentUser.getIdNguoiDung()) {
                currentUser.setTenDangNhap(capNhat.getTenDangNhap());
                currentUser.setHoTen(capNhat.getHoTen());
                currentUser.setVaiTro(capNhat.getVaiTro());
                if (capNhat.getMatKhau() != null && !capNhat.getMatKhau().isEmpty()) {
                    currentUser.setMatKhau(capNhat.getMatKhau());
                }
            }
            JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            taiDuLieu();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Xóa tài khoản
    // ─────────────────────────────────────────────────────────────────────────
    private void xoa() {
        int id = getSelectedId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn tài khoản trong bảng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (id == currentUser.getIdNguoiDung()) {
            JOptionPane.showMessageDialog(view,
                    "Bạn không thể tự xóa tài khoản đang đăng nhập!", "Không thể thực hiện", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int viewRow  = view.table.getSelectedRow();
        int modelRow = view.table.convertRowIndexToModel(viewRow);
        String username = view.model.getValueAt(modelRow, 1).toString();

        int xacNhan = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa vĩnh viễn tài khoản: '" + username + "' (ID: " + id + ") không?",
                "Xác nhận xóa tài khoản", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (xacNhan == JOptionPane.YES_OPTION) {
            int kq = service.xoaNguoiDung(id, currentUser.getIdNguoiDung());
            if (kq > 0) {
                JOptionPane.showMessageDialog(view, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
                view.lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(view, "Không thể xóa tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Khóa / Mở khóa tài khoản
    // ─────────────────────────────────────────────────────────────────────────
    private void doiTrangThai() {
        int id = getSelectedId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn tài khoản trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiDung u = service.timTheoId(id);
        if (u == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean trangThaiMoi = !u.isTrangThai();

        if (id == currentUser.getIdNguoiDung() && !trangThaiMoi) {
            JOptionPane.showMessageDialog(view,
                    "Bạn không thể tự khóa tài khoản của chính mình!", "Không thể thực hiện", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hanhDong = trangThaiMoi ? "MỞ KHÓA" : "KHÓA";
        int xacNhan = JOptionPane.showConfirmDialog(view,
                "Bạn có muốn " + hanhDong + " tài khoản '" + u.getTenDangNhap() + "' không?",
                "Xác nhận đổi trạng thái", JOptionPane.YES_NO_OPTION);

        if (xacNhan == JOptionPane.YES_OPTION) {
            int kq = service.doiTrangThai(id, currentUser.getIdNguoiDung(), trangThaiMoi);
            if (kq > 0) {
                JOptionPane.showMessageDialog(view,
                        "Đã " + hanhDong.toLowerCase() + " tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Đặt lại mật khẩu
    // ─────────────────────────────────────────────────────────────────────────
    private void datLaiMatKhau() {
        int id = getSelectedId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn tài khoản cần đặt lại mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int viewRow  = view.table.getSelectedRow();
        int modelRow = view.table.convertRowIndexToModel(viewRow);
        String username = view.model.getValueAt(modelRow, 1).toString();

        JPasswordField pf1 = new JPasswordField(16);
        JPasswordField pf2 = new JPasswordField(16);
        JPanel pInput = new JPanel(new GridLayout(4, 1, 0, 6));
        pInput.add(new JLabel("Mật khẩu mới cho tài khoản: '" + username + "'"));
        pInput.add(pf1);
        pInput.add(new JLabel("Xác nhận mật khẩu mới:"));
        pInput.add(pf2);

        int opt = JOptionPane.showConfirmDialog(view, pInput, "Đặt lại mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt != JOptionPane.OK_OPTION) return;

        String passMoi = new String(pf1.getPassword()).trim();
        String passXn  = new String(pf2.getPassword()).trim();

        if (passMoi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mật khẩu không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (passMoi.length() < 4) {
            JOptionPane.showMessageDialog(view, "Mật khẩu phải có ít nhất 4 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!passMoi.equals(passXn)) {
            JOptionPane.showMessageDialog(view, "Mật khẩu xác nhận không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kq = service.datLaiMatKhau(id, passMoi);
        if (kq > 0) {
            JOptionPane.showMessageDialog(view,
                    "Đặt lại mật khẩu thành công cho tài khoản '" + username + "'!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (id == currentUser.getIdNguoiDung()) currentUser.setMatKhau(passMoi);
        } else {
            JOptionPane.showMessageDialog(view, "Đặt lại mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
