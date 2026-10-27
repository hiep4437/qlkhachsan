package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewDatPhong extends JFrame {

    public JTextField txtCCCD, txtSoNguoi, txtGhiChu, txtTimKiem;
    public JComboBox<String> cboNgayNhan, cboNgayTra, cboTang, cboTrangThai;

    public JTable table;
    public DefaultTableModel model;

    public JButton btnKiemTraCCCD, btnTimPhong;
    public JButton btnCheckIn, btnCheckOut, btnLayGia;
    public JButton btnTimKiem, btnDatLai, btnQuayLai;

    public ViewDatPhong() {
        setTitle("Quản lý khách sạn - Đặt phòng");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ ĐẶT PHÒNG");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setHorizontalAlignment(JLabel.CENTER);

        // Khởi tạo các trường nhập liệu
        txtCCCD = new JTextField(12);
        txtSoNguoi = new JTextField(5);
        txtGhiChu = new JTextField(15);
        txtTimKiem = new JTextField(15);

        // Ngày nhận và ngày trả dùng JComboBox
        String[] dsNgay = {"2026-10-21", "2026-10-22", "2026-10-23", "2026-10-24", "2026-10-25", "2026-10-26", "2026-10-27", "2026-10-28", "2026-10-29", "2026-10-30", "2026-10-31"};
        cboNgayNhan = new JComboBox<>(dsNgay);
        cboNgayTra = new JComboBox<>(dsNgay);

        // Tầng đặt phòng dùng JComboBox
        String[] dsTang = {"Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5"};
        cboTang = new JComboBox<>(dsTang);

        // Trạng thái đặt phòng
        cboTrangThai = new JComboBox<>(new String[]{
            "Đã đặt", "Đã nhận phòng", "Đã trả phòng", "Đã hủy"
        });

        // Panel nhập thông tin đặt phòng
        JPanel pNhap = new JPanel(new GridLayout(4, 4, 10, 10));
        pNhap.setBorder(BorderFactory.createTitledBorder("Thông tin đặt phòng"));

        // Hàng 1: CCCD khách hàng + Nút kiểm tra
        pNhap.add(new JLabel("CCCD khách hàng:"));
        JPanel pCCCD = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnKiemTraCCCD = new JButton("Kiểm tra");
        pCCCD.add(txtCCCD);
        pCCCD.add(Box.createHorizontalStrut(5));
        pCCCD.add(btnKiemTraCCCD);
        pNhap.add(pCCCD);

        pNhap.add(new JLabel("Tầng đặt phòng:"));
        pNhap.add(cboTang);

        // Hàng 2: Ngày nhận & Ngày trả
        pNhap.add(new JLabel("Ngày nhận phòng:"));
        pNhap.add(cboNgayNhan);

        pNhap.add(new JLabel("Ngày trả phòng:"));
        pNhap.add(cboNgayTra);

        // Hàng 3: Số lượng người & Trạng thái
        pNhap.add(new JLabel("Số lượng người:"));
        pNhap.add(txtSoNguoi);

        pNhap.add(new JLabel("Trạng thái:"));
        pNhap.add(cboTrangThai);

        // Hàng 4: Ghi chú & Nút tìm phòng trống
        pNhap.add(new JLabel("Ghi chú:"));
        pNhap.add(txtGhiChu);

        pNhap.add(new JLabel("Kiểm tra phòng:"));
        btnTimPhong = new JButton("Tìm phòng trống");
        pNhap.add(btnTimPhong);

        // Bảng danh sách đặt phòng
        model = new DefaultTableModel(
            new String[]{
                "Mã đặt phòng", "CCCD khách hàng", "Ngày nhận", 
                "Ngày trả", "Tầng", "Số người", "Trạng thái", "Ghi chú"
            }, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Panel tìm kiếm theo CCCD khách hàng
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTimKiem = new JButton("Tìm kiếm");

        pTim.add(new JLabel("Tìm kiếm theo CCCD khách hàng:"));
        pTim.add(txtTimKiem);
        pTim.add(btnTimKiem);

        // Các nút chức năng mới
        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnCheckIn = new JButton("Check-in");
        btnCheckOut = new JButton("Check-out");
        btnLayGia = new JButton("Lấy giá tiền");
        btnDatLai = new JButton("Đặt lại");
        btnQuayLai = new JButton("Quay lại");

        pNut.add(btnCheckIn);
        pNut.add(btnCheckOut);
        pNut.add(btnLayGia);
        pNut.add(btnDatLai);
        pNut.add(btnQuayLai);

        // Bố cục tổng thể
        JPanel pTren = new JPanel(new BorderLayout(5, 5));
        pTren.add(lblTieuDe, BorderLayout.NORTH);
        pTren.add(pNhap, BorderLayout.CENTER);
        pTren.add(pTim, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        add(pTren, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pNut, BorderLayout.SOUTH);

        // Sự kiện chọn dòng trên bảng đổ ngược lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtCCCD.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
                cboNgayNhan.setSelectedItem(model.getValueAt(row, 2));
                cboNgayTra.setSelectedItem(model.getValueAt(row, 3));
                cboTang.setSelectedItem(model.getValueAt(row, 4));
                txtSoNguoi.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                cboTrangThai.setSelectedItem(model.getValueAt(row, 6));
                txtGhiChu.setText(model.getValueAt(row, 7) == null ? "" : model.getValueAt(row, 7).toString());
            }
        });

        btnDatLai.addActionListener(e -> lamMoi());

        btnQuayLai.addActionListener(e -> {
            new ViewTrangChu().setVisible(true);
            dispose();
        });
    }

    public void lamMoi() {
        txtCCCD.setText("");
        txtSoNguoi.setText("");
        txtGhiChu.setText("");
        txtTimKiem.setText("");
        if (cboNgayNhan.getItemCount() > 0) cboNgayNhan.setSelectedIndex(0);
        if (cboNgayTra.getItemCount() > 0) cboNgayTra.setSelectedIndex(0);
        if (cboTang.getItemCount() > 0) cboTang.setSelectedIndex(0);
        if (cboTrangThai.getItemCount() > 0) cboTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}