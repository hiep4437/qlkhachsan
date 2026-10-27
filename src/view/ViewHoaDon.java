
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewHoaDon extends JFrame {

    public JTextField txtMaHD, txtMaDatPhong;
    public JTextField txtTienPhong, txtPhuThu;
    public JTextField txtGiamGia, txtTongTien, txtTimKiem;

    public JComboBox<String> cboPhuongThuc, cboTrangThai;

    public JTable table;
    public DefaultTableModel model;

    public JButton btnThem, btnSua, btnXoa;
    public JButton btnTimKiem, btnLamMoi, btnQuayLai;

    public ViewHoaDon() {
        setTitle("Quan ly khach san - Quan ly hoa don");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTieuDe = new JLabel("QUAN LY HOA DON");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setHorizontalAlignment(JLabel.CENTER);

        // O nhap thong tin
        txtMaHD = new JTextField();
        txtMaDatPhong = new JTextField();
        txtTienPhong = new JTextField();
        txtPhuThu = new JTextField();
        txtGiamGia = new JTextField();
        txtTongTien = new JTextField();
        txtTimKiem = new JTextField(18);

        txtMaHD.setEditable(false);
        txtTongTien.setEditable(false);

        cboPhuongThuc = new JComboBox<>(new String[]{
            "Tien mat", "Chuyen khoan", "The"
        });

        cboTrangThai = new JComboBox<>(new String[]{
            "UNPAID", "PAID", "REFUNDED"
        });

        JPanel pNhap = new JPanel(new GridLayout(4, 4, 10, 10));
        pNhap.setBorder(
            BorderFactory.createTitledBorder("Thong tin hoa don")
        );

        pNhap.add(new JLabel("Ma hoa don:"));
        pNhap.add(txtMaHD);
        pNhap.add(new JLabel("Ma dat phong:"));
        pNhap.add(txtMaDatPhong);

        pNhap.add(new JLabel("Tien phong:"));
        pNhap.add(txtTienPhong);
        pNhap.add(new JLabel("Phu thu:"));
        pNhap.add(txtPhuThu);

        pNhap.add(new JLabel("Giam gia:"));
        pNhap.add(txtGiamGia);
        pNhap.add(new JLabel("Tong tien:"));
        pNhap.add(txtTongTien);

        pNhap.add(new JLabel("Phuong thuc:"));
        pNhap.add(cboPhuongThuc);
        pNhap.add(new JLabel("Trang thai:"));
        pNhap.add(cboTrangThai);

        // Bang hoa don
        model = new DefaultTableModel(
            new String[]{
                "Ma HD", "Ma dat phong", "Tien phong",
                "Phu thu", "Giam gia", "Tong tien",
                "Phuong thuc", "Trang thai"
            }, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        // Tim kiem
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTimKiem = new JButton("Tim kiem");

        pTim.add(new JLabel("Ma hoa don:"));
        pTim.add(txtTimKiem);
        pTim.add(btnTimKiem);

        // Cac nut chuc nang
        JPanel pNut = new JPanel();

        btnThem = new JButton("Them");
        btnSua = new JButton("Sua");
        btnXoa = new JButton("Xoa");
        btnLamMoi = new JButton("Dat lai");
        btnQuayLai = new JButton("Quay lai");

        pNut.add(btnThem);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);
        pNut.add(btnQuayLai);

        // Bo cuc
        JPanel pTren = new JPanel(new BorderLayout(5, 5));
        pTren.add(lblTieuDe, BorderLayout.NORTH);
        pTren.add(pNhap, BorderLayout.CENTER);
        pTren.add(pTim, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        add(pTren, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pNut, BorderLayout.SOUTH);

        // Chon hoa don trong bang
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row = table.getSelectedRow();

                txtMaHD.setText(
                    model.getValueAt(row, 0).toString()
                );
                txtMaDatPhong.setText(
                    model.getValueAt(row, 1).toString()
                );
                txtTienPhong.setText(
                    model.getValueAt(row, 2).toString()
                );
                txtPhuThu.setText(
                    model.getValueAt(row, 3).toString()
                );
                txtGiamGia.setText(
                    model.getValueAt(row, 4).toString()
                );
                txtTongTien.setText(
                    model.getValueAt(row, 5).toString()
                );
                cboPhuongThuc.setSelectedItem(
                    model.getValueAt(row, 6).toString()
                );
                cboTrangThai.setSelectedItem(
                    model.getValueAt(row, 7).toString()
                );
            }
        });

        btnLamMoi.addActionListener(e -> lamMoi());

        btnQuayLai.addActionListener(e -> {
            new ViewTrangChu().setVisible(true);
            dispose();
        });
    }

    public void lamMoi() {
        txtMaHD.setText("");
        txtMaDatPhong.setText("");
        txtTienPhong.setText("");
        txtPhuThu.setText("");
        txtGiamGia.setText("");
        txtTongTien.setText("");
        txtTimKiem.setText("");
        cboPhuongThuc.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
