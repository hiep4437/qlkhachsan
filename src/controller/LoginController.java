
package controller;

import view.ViewDangNhap;
import view.ViewTrangChu;
import service.UserService;
import javax.swing.*;

public class LoginController {
    ViewDangNhap view;
    UserService service = new UserService();

    public LoginController(ViewDangNhap view) {
        this.view = view;

        view.getBtnDangNhap().addActionListener(e -> dangNhap());
    }

    void dangNhap() {
        String ten = view.getTenDangNhap();
        String matKhau = view.getMatKhau();

        if (ten.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nhap day du tai khoan va mat khau!");
            return;
        }

        if (service.dangNhap(ten, matKhau)) {
            JOptionPane.showMessageDialog(view, "Dang nhap thanh cong!");
            new ViewTrangChu().setVisible(true);
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Sai tai khoan hoac mat khau!");
        }
    }
}
