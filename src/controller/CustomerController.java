
package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import model.customers;
import service.CustomerService;
import view.ViewQuanLyKhachHang;

public class CustomerController {

	ViewQuanLyKhachHang view;
	CustomerService service = new CustomerService();

	public CustomerController(ViewQuanLyKhachHang view) {
		this.view = view;

		taiDuLieu();

		view.btnThem.addActionListener(e -> them());
		view.btnSua.addActionListener(e -> sua());
		view.btnXoa.addActionListener(e -> xoa());
		view.btnTimKiem.addActionListener(e -> timKiem());

		view.btnLamMoi.addActionListener(e -> {
			view.lamMoi();
			taiDuLieu();
		});
	}

	// Hiển thị danh sách khách hàng
	void taiDuLieu() {
		view.model.setRowCount(0);

		ArrayList<customers> ds = service.xuat();

		for (customers c : ds) {
			view.model.addRow(new Object[] { c.getIdKhachHang(), c.getNameKhachHang(), c.getSoCCCD(),
					c.getSoDienThoai(), c.getEmail(), c.getDiaChi() });
		}
	}

	// Tìm khách hàng theo CCCD
	customers timTheoCCCD(String cccd) {
		for (customers c : service.xuat()) {
			if (c.getSoCCCD() != null && c.getSoCCCD().equals(cccd)) {
				return c;
			}
		}
		return null;
	}

	// Lấy dữ liệu để thêm khách hàng
	customers layDuLieuThem() {
		String hoTen = view.txtHoTen.getText().trim();
		String cccd = view.txtCCCD.getText().trim();
		String sdt = view.txtSDT.getText().trim();
		String email = view.txtEmail.getText().trim();
		String diaChi = view.txtDiaChi.getText().trim();

		if (hoTen.isEmpty() || cccd.isEmpty()) {
			throw new IllegalArgumentException("Vui lòng nhập họ tên và CCCD!");
		}

		return new customers(0, hoTen, cccd, sdt, email, diaChi);
	}

	// Thêm khách hàng
	void them() {
		try {
			customers c = layDuLieuThem();

			if (service.them(c) > 0) {
				JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công!");
				taiDuLieu();
				view.lamMoi();
			} else {
				JOptionPane.showMessageDialog(view, "Thêm khách hàng thất bại!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
		}
	}

	// Sửa: chỉ cập nhật các ô đã nhập
	void sua() {
		try {
			String cccd = view.txtCCCD.getText().trim();

			if (cccd.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD của khách hàng cần sửa!");
				return;
			}

			customers c = timTheoCCCD(cccd);

			if (c == null) {
				JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng có CCCD này!");
				return;
			}

			String hoTen = view.txtHoTen.getText().trim();
			String sdt = view.txtSDT.getText().trim();
			String email = view.txtEmail.getText().trim();
			String diaChi = view.txtDiaChi.getText().trim();

			if (!hoTen.isEmpty())
				c.setNameKhachHang(hoTen);

			if (!sdt.isEmpty())
				c.setSoDienThoai(sdt);

			if (!email.isEmpty())
				c.setEmail(email);

			if (!diaChi.isEmpty())
				c.setDiaChi(diaChi);

			if (service.sua(c) > 0) {
				JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thành công!");
				taiDuLieu();
				view.lamMoi();
			} else {
				JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thất bại!");
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
		}
	}

	// Xóa chỉ bằng CCCD
	void xoa() {
		String cccd = view.txtCCCD.getText().trim();

		if (cccd.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD cần xóa!");
			return;
		}

		customers c = timTheoCCCD(cccd);

		if (c == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng!");
			return;
		}

		int chon = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa khách hàng có CCCD " + cccd + "?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);

		if (chon == JOptionPane.YES_OPTION) {
			if (service.xoa(c) > 0) {
				JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công!");
				taiDuLieu();
				view.lamMoi();
			} else {
				JOptionPane.showMessageDialog(view,
						"Không thể xóa khách hàng vì có thể đang liên kết với dữ liệu đặt phòng!");
			}
		}
	}

	// Tìm kiếm theo CCCD

	void timKiem() {
		String cccd = view.txtTimKiem.getText().trim();

		if (cccd.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD cần tìm!");
			return;
		}

		view.model.setRowCount(0);

		ArrayList<customers> ds = service.xuat();
		boolean timThay = false;

		for (customers c : ds) {
			if (c.getSoCCCD() != null && c.getSoCCCD().trim().contains(cccd)) {

				view.model.addRow(new Object[] { c.getIdKhachHang(), c.getNameKhachHang(), c.getSoCCCD(),
						c.getSoDienThoai(), c.getEmail(), c.getDiaChi() });

				timThay = true;
			}
		}

		if (!timThay) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng có CCCD: " + cccd);
		}
	}

}
