package controller;

import dao.DBConecter;
import model.NguoiDung;
import service.NguoiDungService;
import view.view.ViewDangNhap;
import view.view.ViewTrangChu;

import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LoginController {
    ViewDangNhap viewDangNhap;
    NguoiDungService dichVu = new NguoiDungService();

    public LoginController(ViewDangNhap viewDangNhap) {
        this.viewDangNhap = viewDangNhap;
        viewDangNhap.getBtnDangNhap().addActionListener(ac -> dangNhap());

        KeyStroke nhap = KeyStroke.getKeyStroke("ENTER");
        Action lenhNhap = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent suKien) {
                xuLyEnter();
            }
        };
        viewDangNhap.oNhapTenDangNhap.getInputMap(JComponent.WHEN_FOCUSED).put(nhap, "login-enter");
        viewDangNhap.oNhapTenDangNhap.getActionMap().put("login-enter", lenhNhap);
        viewDangNhap.oNhapMatKhau.getInputMap(JComponent.WHEN_FOCUSED).put(nhap, "login-enter");
        viewDangNhap.oNhapMatKhau.getActionMap().put("login-enter", lenhNhap);
    }

    private void xuLyEnter() {
        if (viewDangNhap.getTenDangNhap().isEmpty()) {
            viewDangNhap.oNhapTenDangNhap.requestFocusInWindow();
        } else if (viewDangNhap.getMatKhau().trim().isEmpty()) {
            viewDangNhap.oNhapMatKhau.requestFocusInWindow();
        } else {
            dangNhap();
        }
    }

    void dangNhap() {
        String ten = viewDangNhap.getTenDangNhap();
        String matKhau = viewDangNhap.getMatKhau();

        if (ten.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(viewDangNhap, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiem tra ket noi CSDL truoc
        Connection ketNoi = DBConecter.getConnection();
        if (ketNoi == null) {
            int luaChon = JOptionPane.showConfirmDialog(viewDangNhap,
                    "Không thể kết nối đến máy chủ CSDL MySQL (" + DBConecter.getHost() + ":" + DBConecter.getPort() + ").\n"
                    + "Bạn có muốn mở hộp thoại Cấu hình máy chủ để kiểm tra/thay đổi thông tin kết nối không?",
                    "Lỗi kết nối CSDL",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );
            if (luaChon == JOptionPane.YES_OPTION) {
                viewDangNhap.hienThiDialogCauHinhServer();
            }
            return;
        }
        DBConecter.closeConnection(ketNoi);

        NguoiDung nguoiDungDangNhap = dichVu.kiemTraDangNhap(ten, matKhau);

        if (nguoiDungDangNhap != null) {
            JOptionPane.showMessageDialog(viewDangNhap, "Đăng nhập thành công!\nXin chào: " + nguoiDungDangNhap.getHoTen() + " (" + nguoiDungDangNhap.getVaiTro() + ")", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ViewTrangChu trangChu = new ViewTrangChu(nguoiDungDangNhap);
            trangChu.setVisible(true);
            viewDangNhap.dispose();
        } else {
            JOptionPane.showMessageDialog(viewDangNhap, "Sai tên đăng nhập hoặc mật khẩu, hoặc tài khoản đã bị vô hiệu hóa!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }
}
