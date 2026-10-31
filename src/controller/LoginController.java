package controller;

import dao.DBConnection;
import model.users;
import service.UserService;
import view.ViewDangNhap;
import view.ViewTrangChu;

import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LoginController {
    ViewDangNhap view;
    UserService service = new UserService();

    public LoginController(ViewDangNhap view) {
        this.view = view;
        view.getBtnDangNhap().addActionListener(e -> dangNhap());

        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyEnter();
            }
        };
        view.txtTenDangNhap.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "login-enter");
        view.txtTenDangNhap.getActionMap().put("login-enter", enterAction);
        view.txtMatKhau.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "login-enter");
        view.txtMatKhau.getActionMap().put("login-enter", enterAction);
    }

    private void xuLyEnter() {
        if (view.getTenDangNhap().isEmpty()) {
            view.txtTenDangNhap.requestFocusInWindow();
        } else if (view.getMatKhau().trim().isEmpty()) {
            view.txtMatKhau.requestFocusInWindow();
        } else {
            dangNhap();
        }
    }

    void dangNhap() {
        String ten = view.getTenDangNhap();
        String matKhau = view.getMatKhau();

        if (ten.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiem tra ket noi CSDL truoc
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            int luaChon = JOptionPane.showConfirmDialog(
                    view,
                    "Không thể kết nối đến máy chủ CSDL MySQL (" + DBConnection.getHost() + ":" + DBConnection.getPort() + ").\n"
                    + "Bạn có muốn mở hộp thoại Cấu hình máy chủ để kiểm tra/thay đổi thông tin kết nối không?",
                    "Lỗi kết nối CSDL",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );
            if (luaChon == JOptionPane.YES_OPTION) {
                view.hienThiDialogCauHinhServer();
            }
            return;
        }
        DBConnection.closeConnection(conn);

        users userDangNhap = service.kiemTraDangNhap(ten, matKhau);

        if (userDangNhap != null) {
            JOptionPane.showMessageDialog(view, "Đăng nhập thành công!\nXin chào: " + userDangNhap.getHoTen() + " (" + userDangNhap.getVaiTro() + ")", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ViewTrangChu trangChu = new ViewTrangChu(userDangNhap);
            trangChu.setVisible(true);
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Sai tên đăng nhập hoặc mật khẩu, hoặc tài khoản đã bị vô hiệu hóa!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }
}
