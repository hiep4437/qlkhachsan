
package main;

import view.view.ViewDangNhap;
import controller.LoginController;

public class Main {
    public static void main(String[] thamSo) {
        ViewDangNhap viewDangNhap = new ViewDangNhap();
        new LoginController(viewDangNhap);
        viewDangNhap.setVisible(true);
    }
}
