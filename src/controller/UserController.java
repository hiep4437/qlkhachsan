package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.users;
import service.UserService;
import view.ViewQuanLyTaiKhoan;

public class UserController {

    private ViewQuanLyTaiKhoan view;
    private UserService service = new UserService();
    private users currentUser;

    public UserController(ViewQuanLyTaiKhoan view) {
        this(view, null);
    }

    public UserController(ViewQuanLyTaiKhoan view, users currentUser) {
        this.view = view;
        this.currentUser = (currentUser != null) ? currentUser : new users(1, "admin", "123456", "Quản trị viên", "ADMIN", true);

        taiDuLieu();

        // Gan su kien cho cac nut
        view.btnThem.addActionListener(e -> them());
        view.btnSua.addActionListener(e -> sua());
        view.btnXoa.addActionListener(e -> xoa());
        view.btnKhoaMoKhoa.addActionListener(e -> doiTrangThai());
        view.btnDatLaiMatKhau.addActionListener(e -> datLaiMatKhau());

        // Tim kiem va loc
        view.btnTimKiem.addActionListener(e -> taiDuLieu());
        view.txtTimKiem.addActionListener(e -> taiDuLieu());
        view.cboLocVaiTro.addActionListener(e -> taiDuLieu());
        view.cboLocTrangThai.addActionListener(e -> taiDuLieu());

        view.btnLamMoiTim.addActionListener(e -> {
            view.txtTimKiem.setText("");
            view.cboLocVaiTro.setSelectedIndex(0);
            view.cboLocTrangThai.setSelectedIndex(0);
            taiDuLieu();
        });
    }

    public void taiDuLieu() {
        view.model.setRowCount(0);

        String kw = view.txtTimKiem.getText().trim();
        String locVaiTro = view.cboLocVaiTro.getSelectedItem().toString();
        String locTrangThai = view.cboLocTrangThai.getSelectedItem().toString();

        ArrayList<users> ds = service.timKiemNangCao(kw, locVaiTro, locTrangThai);

        for (users u : ds) {
            String trangThaiHienThi = u.isTrangThai() ? "🟢 Hoạt động" : "🔒 Đã khóa";
            view.model.addRow(new Object[] {
                    u.getIdNguoiDung(),
                    u.getTenDangNhap(),
                    u.getHoTen(),
                    u.getVaiTro(),
                    trangThaiHienThi
            });
        }
    }

    private void them() {
        try {
            String username = view.txtTenDangNhap.getText().trim();
            String password = new String(view.txtMatKhau.getPassword()).trim();
            String hoTen = view.txtHoTen.getText().trim();
            String vaiTro = view.cboVaiTro.getSelectedItem().toString();
            boolean trangThai = view.cboTrangThai.getSelectedIndex() == 0;

            if (username.isEmpty() || password.isEmpty() || hoTen.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Tên đăng nhập, Mật khẩu và Họ tên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.length() < 4) {
                JOptionPane.showMessageDialog(view, "Mật khẩu nên có ít nhất 4 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            users u = new users(0, username, password, hoTen, vaiTro, trangThai);

            int kq = service.themNguoiDung(u);
            if (kq == -1) {
                JOptionPane.showMessageDialog(view, "Tên đăng nhập '" + username + "' đã tồn tại! Vui lòng chọn tên khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else if (kq > 0) {
                JOptionPane.showMessageDialog(view, "Thêm tài khoản người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieu();
                view.lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sua() {
        try {
            String idStr = view.txtMaNguoiDung.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản trong bảng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            String username = view.txtTenDangNhap.getText().trim();
            String password = new String(view.txtMatKhau.getPassword()).trim();
            String hoTen = view.txtHoTen.getText().trim();
            String vaiTro = view.cboVaiTro.getSelectedItem().toString();
            boolean trangThai = view.cboTrangThai.getSelectedIndex() == 0;

            if (username.isEmpty() || hoTen.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tên đăng nhập và Họ tên không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiem tra neu tu khoa chinh tai khoan cua minh
            if (id == currentUser.getIdNguoiDung() && !trangThai) {
                JOptionPane.showMessageDialog(view, "Bạn không thể tự khóa tài khoản của chính mình!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            users u = new users(id, username, password, hoTen, vaiTro, trangThai);

            int kq = service.suaNguoiDung(u);
            if (kq == -1) {
                JOptionPane.showMessageDialog(view, "Tên đăng nhập '" + username + "' đã được tài khoản khác sử dụng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else if (kq > 0) {
                JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                if (id == currentUser.getIdNguoiDung()) {
                    currentUser.setTenDangNhap(username);
                    currentUser.setHoTen(hoTen);
                    currentUser.setVaiTro(vaiTro);
                    if (!password.isEmpty()) {
                        currentUser.setMatKhau(password);
                    }
                }
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoa() {
        try {
            String idStr = view.txtMaNguoiDung.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản trong bảng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            String username = view.txtTenDangNhap.getText().trim();

            if (id == currentUser.getIdNguoiDung()) {
                JOptionPane.showMessageDialog(view, "Bạn không thể tự xóa tài khoản của chính mình đang đăng nhập!", "Không thể thực hiện", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int xacNhan = JOptionPane.showConfirmDialog(
                    view,
                    "Bạn có chắc chắn muốn xóa vĩnh viễn tài khoản: '" + username + "' (ID: " + id + ") không?",
                    "Xác nhận xóa tài khoản",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doiTrangThai() {
        try {
            String idStr = view.txtMaNguoiDung.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản trong bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            String username = view.txtTenDangNhap.getText().trim();

            users u = service.timTheoId(id);
            if (u == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean trangThaiHienTai = u.isTrangThai();
            boolean trangThaiMoi = !trangThaiHienTai;

            if (id == currentUser.getIdNguoiDung() && !trangThaiMoi) {
                JOptionPane.showMessageDialog(view, "Bạn không thể tự khóa tài khoản của chính mình!", "Không thể thực hiện", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String hanhDong = trangThaiMoi ? "MỞ KHÓA" : "KHÓA";
            int xacNhan = JOptionPane.showConfirmDialog(
                    view,
                    "Bạn có muốn " + hanhDong + " tài khoản '" + username + "' không?",
                    "Xác nhận đổi trạng thái",
                    JOptionPane.YES_NO_OPTION
            );

            if (xacNhan == JOptionPane.YES_OPTION) {
                int kq = service.doiTrangThai(id, currentUser.getIdNguoiDung(), trangThaiMoi);
                if (kq > 0) {
                    JOptionPane.showMessageDialog(view, "Đã " + hanhDong.toLowerCase() + " tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieu();
                    view.cboTrangThai.setSelectedIndex(trangThaiMoi ? 0 : 1);
                } else {
                    JOptionPane.showMessageDialog(view, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void datLaiMatKhau() {
        try {
            String idStr = view.txtMaNguoiDung.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản trong bảng cần đặt lại mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            String username = view.txtTenDangNhap.getText().trim();

            String passMoi = JOptionPane.showInputDialog(
                    view,
                    "Nhập mật khẩu mới cho tài khoản: '" + username + "':",
                    "Đặt lại mật khẩu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (passMoi == null) {
                return; // User clicked Cancel
            }

            passMoi = passMoi.trim();
            if (passMoi.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Mật khẩu không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int kq = service.datLaiMatKhau(id, passMoi);
            if (kq > 0) {
                JOptionPane.showMessageDialog(view, "Đặt lại mật khẩu thành công cho tài khoản '" + username + "'!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                if (id == currentUser.getIdNguoiDung()) {
                    currentUser.setMatKhau(passMoi);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Đặt lại mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

