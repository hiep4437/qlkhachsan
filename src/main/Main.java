package main;

import view.view.ViewDangNhap;
import controller.LoginController;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] thamSo) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewDangNhap viewDangNhap = new ViewDangNhap();
        new LoginController(viewDangNhap);
        viewDangNhap.setVisible(true);
    }
}