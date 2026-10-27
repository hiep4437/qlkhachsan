
package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

public class ViewQuanLyPhong extends JFrame {

	public JTextField txtMaPhong, txtSoPhong, txtMaLoai;
	public JTextField txtGia, txtMoTa, txtTimKiem;
	public JComboBox<String> cboTrangThai;
	public JTable table;
	public DefaultTableModel model;

	public JButton btnThem, btnSua, btnXoa;
	public JButton btnTimKiem, btnLamMoi, btnQuayLai;

	public ViewQuanLyPhong() {
		setTitle("Quản lý khách sạn - Quản lý phòng");
		setSize(1000, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JLabel lblTieuDe = new JLabel("QUẢN LÝ PHÒNG");
		lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
		lblTieuDe.setHorizontalAlignment(JLabel.CENTER);

		txtMaPhong = new JTextField();
		txtMaPhong.setEditable(false);
		txtSoPhong = new JTextField();
		txtMaLoai = new JTextField();
		txtGia = new JTextField();
		txtMoTa = new JTextField();
		txtTimKiem = new JTextField(15);

		// Trạng thái tiếng Việt
		cboTrangThai = new JComboBox<>(new String[] { "", "TRỐNG", "CÓ KHÁCH", "ĐANG DỌN", "BẢO TRÌ" });

		// Thông tin phòng, không hiện mã phòng
		JPanel pNhap = new JPanel(new GridBagLayout());
		pNhap.setBorder(
		    BorderFactory.createTitledBorder("Thông tin phòng")
		);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weightx = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		pNhap.add(new JLabel("Số phòng:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		pNhap.add(txtSoPhong, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0;
		pNhap.add(new JLabel("Mã loại phòng:"), gbc);

		gbc.gridx = 3;
		gbc.weightx = 1;
		pNhap.add(txtMaLoai, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		pNhap.add(new JLabel("Giá mỗi đêm:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		pNhap.add(txtGia, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0;
		pNhap.add(new JLabel("Trạng thái:"), gbc);

		gbc.gridx = 3;
		gbc.weightx = 1;
		pNhap.add(cboTrangThai, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		pNhap.add(new JLabel("Mô tả:"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 1;
		pNhap.add(txtMoTa, gbc);


		// Bảng phòng
		model = new DefaultTableModel(
				new String[] { "Mã phòng", "Số phòng", "Mã loại", "Giá mỗi đêm", "Trạng thái", "Mô tả" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0 || column == 1 || column == 2)
					return Integer.class;
				if (column == 3)
					return Float.class;
				return String.class;
			}
		};

		table = new JTable(model);
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Sắp xếp theo số phòng tăng dần
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		List<RowSorter.SortKey> keys = new ArrayList<>();
		keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		sorter.setSortKeys(keys);
		sorter.setSortsOnUpdates(true);

		// Màu trạng thái
		table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
					int row, int column) {

				super.getTableCellRendererComponent(table, value, selected, focus, row, column);

				String tt = value == null ? "" : value.toString();

				switch (tt) {
				case "AVAILABLE":
				case "TRỐNG":
					setText("TRỐNG");
					setBackground(Color.GREEN);
					setForeground(Color.BLACK);
					break;

				case "OCCUPIED":
				case "CÓ KHÁCH":
					setText("CÓ KHÁCH");
					setBackground(Color.RED);
					setForeground(Color.WHITE);
					break;

				case "CLEANING":
				case "ĐANG DỌN":
					setText("ĐANG DỌN");
					setBackground(Color.YELLOW);
					setForeground(Color.BLACK);
					break;

				case "MAINTENANCE":
				case "BẢO TRÌ":
					setText("BẢO TRÌ");
					setBackground(Color.LIGHT_GRAY);
					setForeground(Color.BLACK);
					break;

				default:
					setText(tt);
					setBackground(Color.WHITE);
					setForeground(Color.BLACK);
				}

				setHorizontalAlignment(CENTER);
				setFont(new Font("Arial", Font.BOLD, 12));

				if (selected) {
					setBackground(table.getSelectionBackground());
					setForeground(table.getSelectionForeground());
				}

				return this;
			}
		});

		// Tìm kiếm
		JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnTimKiem = new JButton("Tìm kiếm");
		pTim.add(new JLabel("Số phòng hoặc đầu số:"));
		pTim.add(txtTimKiem);
		pTim.add(btnTimKiem);

		// Nút chức năng
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

		JPanel pTren = new JPanel(new BorderLayout(5, 5));
		pTren.add(lblTieuDe, BorderLayout.NORTH);
		pTren.add(pNhap, BorderLayout.CENTER);
		pTren.add(pTim, BorderLayout.SOUTH);

		setLayout(new BorderLayout(10, 10));
		add(pTren, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(pNut, BorderLayout.SOUTH);

		// Chọn phòng trong bảng để sửa hoặc xóa
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {

				int row = table.convertRowIndexToModel(table.getSelectedRow());

				txtMaPhong.setText(model.getValueAt(row, 0).toString());
				txtSoPhong.setText(model.getValueAt(row, 1).toString());
				txtMaLoai.setText(model.getValueAt(row, 2).toString());
				txtGia.setText(model.getValueAt(row, 3).toString());
				txtMoTa.setText(model.getValueAt(row, 5) == null ? "" : model.getValueAt(row, 5).toString());

				String tt = model.getValueAt(row, 4).toString();
				cboTrangThai.setSelectedItem(chuyenTrangThai(tt));
			}
		});

		btnLamMoi.addActionListener(e -> lamMoi());

		btnQuayLai.addActionListener(e -> {
			new ViewTrangChu().setVisible(true);
			dispose();
		});
	}

	public String chuyenTrangThai(String tt) {
		if (tt == null)
			return "";

		switch (tt) {
		case "AVAILABLE":
			return "TRỐNG";
		case "OCCUPIED":
			return "CÓ KHÁCH";
		case "CLEANING":
			return "ĐANG DỌN";
		case "MAINTENANCE":
			return "BẢO TRÌ";
		default:
			return tt;
		}
	}

	public void lamMoi() {
		txtMaPhong.setText("");
		txtSoPhong.setText("");
		txtMaLoai.setText("");
		txtGia.setText("");
		txtMoTa.setText("");
		txtTimKiem.setText("");
		cboTrangThai.setSelectedIndex(0);
		table.clearSelection();
	}

	public static void main(String[] args) {
		new ViewQuanLyPhong().setVisible(true);
	}
}
