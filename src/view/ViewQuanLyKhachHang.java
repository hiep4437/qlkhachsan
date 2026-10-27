
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewQuanLyKhachHang extends JFrame {

	public JTextField txtMa, txtHoTen, txtCCCD;
	public JTextField txtSDT, txtEmail, txtDiaChi, txtTimKiem;

	public JTable table;
	public DefaultTableModel model;

	public JButton btnThem, btnSua, btnXoa;
	public JButton btnTimKiem, btnLamMoi, btnQuayLai;

	public ViewQuanLyKhachHang() {

		setTitle("Quản lý khách sạn - Quản lý khách hàng");
		setSize(1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Tiêu đề
		JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
		lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
		lblTieuDe.setHorizontalAlignment(JLabel.CENTER);

		// Các ô nhập
		txtMa = new JTextField();
		txtHoTen = new JTextField();
		txtCCCD = new JTextField();
		txtSDT = new JTextField();
		txtEmail = new JTextField();
		txtDiaChi = new JTextField();
		txtTimKiem = new JTextField(18);

		txtMa.setEditable(false);

		// Thông tin khách hàng
		JPanel pNhap = new JPanel(new GridLayout(3, 4, 10, 10));
		pNhap.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

		pNhap.add(new JLabel("Mã khách hàng:"));
		pNhap.add(txtMa);
		pNhap.add(new JLabel("Họ tên:"));
		pNhap.add(txtHoTen);

		pNhap.add(new JLabel("CCCD:"));
		pNhap.add(txtCCCD);
		pNhap.add(new JLabel("Số điện thoại:"));
		pNhap.add(txtSDT);

		pNhap.add(new JLabel("Email:"));
		pNhap.add(txtEmail);
		pNhap.add(new JLabel("Địa chỉ:"));
		pNhap.add(txtDiaChi);

		// Bảng khách hàng
		model = new DefaultTableModel(
				new String[] { "Mã khách hàng", "Họ tên", "CCCD", "Số điện thoại", "Email", "Địa chỉ" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Tìm kiếm
		JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnTimKiem = new JButton("Tìm kiếm");

		pTim.add(new JLabel("CCCD:"));
		pTim.add(txtTimKiem);
		pTim.add(btnTimKiem);

		// Các nút chức năng
		JPanel pNut = new JPanel();

		btnThem = new JButton("Thêm");
		btnSua = new JButton("Sửa");
		btnXoa = new JButton("Xóa");
		btnLamMoi = new JButton("Đặt lại");
		btnQuayLai = new JButton("Quay lại");

		pNut.add(btnThem);
		pNut.add(btnSua);
		pNut.add(btnXoa);
		pNut.add(btnLamMoi);
		pNut.add(btnQuayLai);

		// Bố cục
		JPanel pTren = new JPanel(new BorderLayout(5, 5));
		pTren.add(lblTieuDe, BorderLayout.NORTH);
		pTren.add(pNhap, BorderLayout.CENTER);
		pTren.add(pTim, BorderLayout.SOUTH);

		setLayout(new BorderLayout(10, 10));
		add(pTren, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(pNut, BorderLayout.SOUTH);

		// Chọn khách hàng trong bảng
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {

				int row = table.getSelectedRow();

				txtMa.setText(model.getValueAt(row, 0).toString());

				txtHoTen.setText(model.getValueAt(row, 1).toString());

				txtCCCD.setText(model.getValueAt(row, 2) == null ? "" : model.getValueAt(row, 2).toString());

				txtSDT.setText(model.getValueAt(row, 3) == null ? "" : model.getValueAt(row, 3).toString());

				txtEmail.setText(model.getValueAt(row, 4) == null ? "" : model.getValueAt(row, 4).toString());

				txtDiaChi.setText(model.getValueAt(row, 5) == null ? "" : model.getValueAt(row, 5).toString());
			}
		});

		// Đặt lại
		btnLamMoi.addActionListener(e -> lamMoi());

		// Quay lại trang chủ
		btnQuayLai.addActionListener(e -> {
			new ViewTrangChu().setVisible(true);
			dispose();
		});
	}

	// Xóa dữ liệu trên các ô nhập
	public void lamMoi() {
		txtMa.setText("");
		txtHoTen.setText("");
		txtCCCD.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		txtDiaChi.setText("");
		txtTimKiem.setText("");
		table.clearSelection();
	}

	public static void main(String[] args) {
		new ViewQuanLyKhachHang().setVisible(true);
	}
}
