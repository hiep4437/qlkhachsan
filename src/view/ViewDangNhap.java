
package view;

import javax.swing.*;
import java.awt.*;

public class ViewDangNhap extends JFrame {
    public JTextField txtTenDangNhap;
    public JPasswordField txtMatKhau;
    public JButton btnDangNhap, btnThoat;
    

    public ViewDangNhap() {
        setTitle("Dang nhap - Quan ly khach san");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        p.add(new JLabel("Ten dang nhap:"));
        txtTenDangNhap = new JTextField();
        p.add(txtTenDangNhap);

        p.add(new JLabel("Mat khau:"));
        txtMatKhau = new JPasswordField();
        p.add(txtMatKhau);

        btnDangNhap = new JButton("Dang nhap");
        btnThoat = new JButton("Thoat");

        p.add(btnDangNhap);
        p.add(btnThoat);

        add(p);

        btnThoat.addActionListener(e -> System.exit(0));
    }

    public String getTenDangNhap() {
        return txtTenDangNhap.getText();
    }

    public String getMatKhau() {
        return new String(txtMatKhau.getPassword());
    }

    public JButton getBtnDangNhap() {
        return btnDangNhap;
    }

    public static void main(String[] args) {
        new ViewDangNhap().setVisible(true);
    }
}
