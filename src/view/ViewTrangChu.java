package view;

import java.awt.*;
import javax.swing.*;
import controller.LoginController;
import controller.RoomController;
import controller.CustomerController;
import controller.BookingController;
import controller.InvoiceController;

public class ViewTrangChu extends JFrame {

    JButton btnPhong, btnKhachHang, btnDatPhong;
    JButton btnHoaDon, btnNguoiDung, btnDangXuat;

    public ViewTrangChu() {
        setTitle("Quản lý khách sạn");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH SẠN");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Thanh menu
        JPanel pMenu = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 5, 5)
        );

        btnPhong = new JButton("Phòng");
        btnKhachHang = new JButton("Khách hàng");
        btnDatPhong = new JButton("Đặt phòng");
        btnHoaDon = new JButton("Hóa đơn");
        btnNguoiDung = new JButton("Người dùng");
        btnDangXuat = new JButton("Đăng xuất");

        pMenu.add(btnPhong);
        pMenu.add(btnKhachHang);
        pMenu.add(btnDatPhong);
        pMenu.add(btnHoaDon);
        pMenu.add(btnNguoiDung);
        pMenu.add(btnDangXuat);

        // Màn hình chào
        JLabel lblChao = new JLabel(
            "<html><center><h1>CHÀO MỪNG ĐẾN VỚI KHÁCH SẠN</h1>"
            + "<p>Hệ thống quản lý khách sạn</p></center></html>"
        );
        lblChao.setHorizontalAlignment(JLabel.CENTER);

        JPanel pTren = new JPanel(new BorderLayout());
        pTren.add(lblTieuDe, BorderLayout.NORTH);
        pTren.add(pMenu, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(pTren, BorderLayout.NORTH);
        add(lblChao, BorderLayout.CENTER);

        // Quản lý phòng
        btnPhong.addActionListener(e -> {
            ViewQuanLyPhong v = new ViewQuanLyPhong();
            new RoomController(v);
            v.setVisible(true);
            dispose();
        });

        // Quản lý khách hàng
        btnKhachHang.addActionListener(e -> {
            ViewQuanLyKhachHang v = new ViewQuanLyKhachHang();
            new CustomerController(v);
            v.setVisible(true);
            dispose();
        });

        // Đặt phòng
        btnDatPhong.addActionListener(e -> {
            ViewDatPhong v = new ViewDatPhong();
            new BookingController(v);
            v.setVisible(true);
            dispose();
        });

        // Hóa đơn
        btnHoaDon.addActionListener(e -> {
            ViewHoaDon v = new ViewHoaDon();
            new InvoiceController(v);
            v.setVisible(true);
            dispose();
        });

        // Người dùng
        btnNguoiDung.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this, "Chức năng quản lý người dùng"
            );
        });

        // Đăng xuất
        btnDangXuat.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );

            if (chon == JOptionPane.YES_OPTION) {
                ViewDangNhap v = new ViewDangNhap();
                new LoginController(v);
                v.setVisible(true);
                dispose();
            }
        });
    }
}
