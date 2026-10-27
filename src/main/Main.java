
package main;

import view.ViewDangNhap;
import controller.LoginController;

public class Main {
    public static void main(String[] args) {
        ViewDangNhap view = new ViewDangNhap();
        new LoginController(view);
        view.setVisible(true);
    }
}
