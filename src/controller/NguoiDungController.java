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

    private ViewQuanLyTaiKhoan viewQuanLyTaiKhoan;
    private NguoiDungService dichVu = new NguoiDungService();
    private NguoiDung nguoiDungHienTai;

    public NguoiDungController(ViewQuanLyTaiKhoan viewQuanLyTaiKhoan) {
        this(viewQuanLyTaiKhoan, null);
    }

    public NguoiDungController(ViewQuanLyTaiKhoan viewQuanLyTaiKhoan, NguoiDung nguoiDungHienTai) {
        this.viewQuanLyTaiKhoan = viewQuanLyTaiKhoan;
        this.nguoiDungHienTai = (nguoiDungHienTai != null)
                ? nguoiDungHienTai
                : new NguoiDung(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        taiDuLieu();

        // ── Nút thao tác ──────────────────────────────────────────────────────
        viewQuanLyTaiKhoan.btThem.addActionListener(ac -> them());
        viewQuanLyTaiKhoan.btSua.addActionListener(ac -> sua());
        viewQuanLyTaiKhoan.btXoa.addActionListener(ac -> xoa());
        viewQuanLyTaiKhoan.btKhoaMoKhoa.addActionListener(ac -> doiTrangThai());
        viewQuanLyTaiKhoan.btDatLaiMatKhau.addActionListener(ac -> datLaiMatKhau());

        // ── Tìm kiếm & lọc ────────────────────────────────────────────────────
        viewQuanLyTaiKhoan.btTimKiem.addActionListener(ac -> apDungBoLoc());
        viewQuanLyTaiKhoan.oNhapTimKiem.addActionListener(ac -> apDungBoLoc());
        viewQuanLyTaiKhoan.hopChonLocVaiTro.addActionListener(ac -> apDungBoLoc());
        viewQuanLyTaiKhoan.hopChonLocTrangThai.addActionListener(ac -> apDungBoLoc());

        viewQuanLyTaiKhoan.btLamMoiTim.addActionListener(ac -> {
            viewQuanLyTaiKhoan.oNhapTimKiem.setText("");
            viewQuanLyTaiKhoan.hopChonLocVaiTro.setSelectedIndex(0);
            viewQuanLyTaiKhoan.hopChonLocTrangThai.setSelectedIndex(0);
            apDungBoLoc();
        });

        viewQuanLyTaiKhoan.btLamMoi.addActionListener(ac -> {
            viewQuanLyTaiKhoan.lamMoiForm();
            taiDuLieu();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tải dữ liệu toàn bộ lên bảng
    // ─────────────────────────────────────────────────────────────────────────
    public void taiDuLieu() {
        viewQuanLyTaiKhoan.moHinh.setRowCount(0);

        ArrayList<NguoiDung> danhSach = dichVu.xuat();
        int chung  = danhSach.size();
        int quanTri = 0;
        int nguoiDung    = 0;

        for (NguoiDung doiTuong : danhSach) {
            if ("ADMIN".equalsIgnoreCase(doiTuong.getVaiTro())) quanTri++;
            else nguoiDung++;

            String trangThaiHienThi = doiTuong.isTrangThai() ? "Hoạt động" : "Đã khóa";
            viewQuanLyTaiKhoan.moHinh.addRow(new Object[]{
                doiTuong.getIdNguoiDung(),
                doiTuong.getTenDangNhap(),
                doiTuong.getHoTen(),
                doiTuong.getVaiTro(),
                trangThaiHienThi
            });
        }

        viewQuanLyTaiKhoan.nhanThongKe.setText(String.format(
                "Tổng số: %d tài khoản  |  Admin: %d  |  Nhân viên: %d", chung, quanTri, nguoiDung));

        apDungBoLoc();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lọc RowFilter kết hợp từ khóa + vai trò + trạng thái
    // ─────────────────────────────────────────────────────────────────────────
    void apDungBoLoc() {
        String tuKhoa = viewQuanLyTaiKhoan.oNhapTimKiem.getText().trim().toLowerCase();
        String locVaiTro  = (String) viewQuanLyTaiKhoan.hopChonLocVaiTro.getSelectedItem();
        String locTrangThai  = (String) viewQuanLyTaiKhoan.hopChonLocTrangThai.getSelectedItem();

        if (viewQuanLyTaiKhoan.boSapXep == null) return;

        List<RowFilter<DefaultTableModel, Object>> cacBoLoc = new ArrayList<>();

        if (!tuKhoa.isEmpty()) {
            cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
                @Override public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
                    for (int chiSo = 0; chiSo < phanTu.getValueCount(); chiSo++) {
                        Object giaTri = phanTu.getValue(chiSo);
                        if (giaTri != null && giaTri.toString().toLowerCase().contains(tuKhoa)) return true;
                    }
                    return false;
                }
            });
        }

        if (locVaiTro != null && !locVaiTro.equals("Tất cả vai trò")) {
            cacBoLoc.add(RowFilter.regexFilter("(?i)^" + locVaiTro + "$", 3));
        }

        if (locTrangThai != null && !locTrangThai.equals("Tất cả trạng thái")) {
            cacBoLoc.add(RowFilter.regexFilter("(?i)" + locTrangThai, 4));
        }

        viewQuanLyTaiKhoan.boSapXep.setRowFilter(cacBoLoc.isEmpty() ? null : RowFilter.andFilter(cacBoLoc));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper: cửa sổ cha cho dialog
    // ─────────────────────────────────────────────────────────────────────────
    private Window getParentWindow() {
        Window chieuRong = SwingUtilities.getWindowAncestor(viewQuanLyTaiKhoan.getContentPane());
        return chieuRong != null ? chieuRong : viewQuanLyTaiKhoan;
    }

    // Helper: lấy ID từ dòng đang chọn trong bảng
    private int getSelectedId() {
        int hangHienThi = viewQuanLyTaiKhoan.bang.getSelectedRow();
        if (hangHienThi == -1) return -1;
        int hangMoHinh = viewQuanLyTaiKhoan.bang.convertRowIndexToModel(hangHienThi);
        return (int) viewQuanLyTaiKhoan.moHinh.getValueAt(hangMoHinh, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Thêm tài khoản – mở Modal Dialog
    // ─────────────────────────────────────────────────────────────────────────
    private void them() {
        DialogThemSuaTaiKhoan hopThoai = new DialogThemSuaTaiKhoan(getParentWindow(), null);
        hopThoai.setVisible(true);

        if (!hopThoai.isSaved()) return;

        NguoiDung doiTuong = hopThoai.getUser();
        int ketQua  = dichVu.themNguoiDung(doiTuong);

        if (ketQua == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Tên đăng nhập '" + doiTuong.getTenDangNhap() + "' đã tồn tại! Vui lòng chọn tên khác.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else if (ketQua > 0) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Thêm tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            taiDuLieu();
            viewQuanLyTaiKhoan.lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Sửa tài khoản – mở Modal Dialog (cũng kích hoạt bởi double-click)
    // ─────────────────────────────────────────────────────────────────────────
    private void sua() {
        int ma = getSelectedId();
        if (ma == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Vui lòng chọn tài khoản trong bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiDung doiTuong = dichVu.timTheoId(ma);
        if (doiTuong == null) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DialogThemSuaTaiKhoan hopThoai = new DialogThemSuaTaiKhoan(getParentWindow(), doiTuong);
        hopThoai.setVisible(true);

        if (!hopThoai.isSaved()) return;

        NguoiDung capNhat = hopThoai.getUser();

        // Không cho tự khóa tài khoản đang đăng nhập
        if (capNhat.getIdNguoiDung() == nguoiDungHienTai.getIdNguoiDung() && !capNhat.isTrangThai()) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Bạn không thể tự khóa tài khoản của chính mình!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ketQua = dichVu.suaNguoiDung(capNhat);
        if (ketQua == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Tên đăng nhập '" + capNhat.getTenDangNhap() + "' đã được tài khoản khác sử dụng!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else if (ketQua > 0) {
            // Cập nhật currentUser nếu sửa chính mình
            if (ma == nguoiDungHienTai.getIdNguoiDung()) {
                nguoiDungHienTai.setTenDangNhap(capNhat.getTenDangNhap());
                nguoiDungHienTai.setHoTen(capNhat.getHoTen());
                nguoiDungHienTai.setVaiTro(capNhat.getVaiTro());
                if (capNhat.getMatKhau() != null && !capNhat.getMatKhau().isEmpty()) {
                    nguoiDungHienTai.setMatKhau(capNhat.getMatKhau());
                }
            }
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            taiDuLieu();
        } else {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Xóa tài khoản
    // ─────────────────────────────────────────────────────────────────────────
    private void xoa() {
        int ma = getSelectedId();
        if (ma == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Vui lòng chọn tài khoản trong bảng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ma == nguoiDungHienTai.getIdNguoiDung()) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Bạn không thể tự xóa tài khoản đang đăng nhập!", "Không thể thực hiện", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int hangHienThi  = viewQuanLyTaiKhoan.bang.getSelectedRow();
        int hangMoHinh = viewQuanLyTaiKhoan.bang.convertRowIndexToModel(hangHienThi);
        String tenDangNhap = viewQuanLyTaiKhoan.moHinh.getValueAt(hangMoHinh, 1).toString();

        int xacNhan = JOptionPane.showConfirmDialog(viewQuanLyTaiKhoan,
                "Bạn có chắc muốn xóa vĩnh viễn tài khoản: '" + tenDangNhap + "' (ID: " + ma + ") không?",
                "Xác nhận xóa tài khoản", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (xacNhan == JOptionPane.YES_OPTION) {
            int ketQua = dichVu.xoaNguoiDung(ma, nguoiDungHienTai.getIdNguoiDung());
            if (ketQua > 0) {
                JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
                viewQuanLyTaiKhoan.lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Không thể xóa tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Khóa / Mở khóa tài khoản
    // ─────────────────────────────────────────────────────────────────────────
    private void doiTrangThai() {
        int ma = getSelectedId();
        if (ma == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Vui lòng chọn tài khoản trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NguoiDung doiTuong = dichVu.timTheoId(ma);
        if (doiTuong == null) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean trangThaiMoi = !doiTuong.isTrangThai();

        if (ma == nguoiDungHienTai.getIdNguoiDung() && !trangThaiMoi) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Bạn không thể tự khóa tài khoản của chính mình!", "Không thể thực hiện", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hanhHang = trangThaiMoi ? "MỞ KHÓA" : "KHÓA";
        int xacNhan = JOptionPane.showConfirmDialog(viewQuanLyTaiKhoan,
                "Bạn có muốn " + hanhHang + " tài khoản '" + doiTuong.getTenDangNhap() + "' không?",
                "Xác nhận đổi trạng thái", JOptionPane.YES_NO_OPTION);

        if (xacNhan == JOptionPane.YES_OPTION) {
            int ketQua = dichVu.doiTrangThai(ma, nguoiDungHienTai.getIdNguoiDung(), trangThaiMoi);
            if (ketQua > 0) {
                JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                        "Đã " + hanhHang.toLowerCase() + " tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Đặt lại mật khẩu
    // ─────────────────────────────────────────────────────────────────────────
    private void datLaiMatKhau() {
        int ma = getSelectedId();
        if (ma == -1) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Vui lòng chọn tài khoản cần đặt lại mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int hangHienThi  = viewQuanLyTaiKhoan.bang.getSelectedRow();
        int hangMoHinh = viewQuanLyTaiKhoan.bang.convertRowIndexToModel(hangHienThi);
        String tenDangNhap = viewQuanLyTaiKhoan.moHinh.getValueAt(hangMoHinh, 1).toString();

        JPasswordField truongMatKhau1 = new JPasswordField(16);
        JPasswordField truongMatKhau2 = new JPasswordField(16);
        JPanel khungNhap = new JPanel(new GridLayout(4, 1, 0, 6));
        khungNhap.add(new JLabel("Mật khẩu mới cho tài khoản: '" + tenDangNhap + "'"));
        khungNhap.add(truongMatKhau1);
        khungNhap.add(new JLabel("Xác nhận mật khẩu mới:"));
        khungNhap.add(truongMatKhau2);

        int tuyChon = JOptionPane.showConfirmDialog(viewQuanLyTaiKhoan, khungNhap, "Đặt lại mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (tuyChon != JOptionPane.OK_OPTION) return;

        String matKhauMoi = new String(truongMatKhau1.getPassword()).trim();
        String matKhauXacNhan  = new String(truongMatKhau2.getPassword()).trim();

        if (matKhauMoi.isEmpty()) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Mật khẩu không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (matKhauMoi.length() < 4) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Mật khẩu phải có ít nhất 4 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!matKhauMoi.equals(matKhauXacNhan)) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Mật khẩu xác nhận không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ketQua = dichVu.datLaiMatKhau(ma, matKhauMoi);
        if (ketQua > 0) {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan,
                    "Đặt lại mật khẩu thành công cho tài khoản '" + tenDangNhap + "'!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            if (ma == nguoiDungHienTai.getIdNguoiDung()) nguoiDungHienTai.setMatKhau(matKhauMoi);
        } else {
            JOptionPane.showMessageDialog(viewQuanLyTaiKhoan, "Đặt lại mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
