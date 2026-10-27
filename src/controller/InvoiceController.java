
package controller;

import view.ViewHoaDon;
import model.invoices;
import service.InvoiceService;
import javax.swing.*;

public class InvoiceController {
	ViewHoaDon view;
	InvoiceService service = new InvoiceService();

	public InvoiceController(ViewHoaDon view) {
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

	void taiDuLieu() {
		view.model.setRowCount(0);

		for (invoices i : service.xuat()) {
			view.model.addRow(new Object[] { i.getIdHoaDon(), i.getIdDatPhong(), i.getTienPhong(), i.getPhuThu(),
					i.getGiamGia(), i.getTongTien(), i.getPhuongThucThanhToan(), i.getTrangThaiThanhToan() });
		}
	}

	invoices layDuLieu() {
		return new invoices(0, Integer.parseInt(view.txtMaDatPhong.getText()), null,
				Float.parseFloat(view.txtTienPhong.getText()), Float.parseFloat(view.txtPhuThu.getText()),
				Float.parseFloat(view.txtGiamGia.getText()), 0, view.cboPhuongThuc.getSelectedItem().toString(),
				view.cboTrangThai.getSelectedItem().toString());
	}

	void them() {
		try {
			service.them(layDuLieu());
			taiDuLieu();
			JOptionPane.showMessageDialog(view, "Them hoa don thanh cong!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Loi: " + e.getMessage());
		}
	}

	void sua() {
		try {
			invoices i = layDuLieu();
			i.setIdHoaDon(Integer.parseInt(view.txtMaHD.getText()));
			service.sua(i);
			taiDuLieu();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Loi: " + e.getMessage());
		}
	}

	void xoa() {
		try {
			invoices i = new invoices(Integer.parseInt(view.txtMaHD.getText()), 0, null, 0, 0, 0, 0, "", "");

			service.xoa(i);
			taiDuLieu();

			JOptionPane.showMessageDialog(view, "Xoa thanh cong!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Loi: " + e.getMessage());
		}
	}

	void timKiem() {
		try {
			invoices i = new invoices(0, Integer.parseInt(view.txtTimKiem.getText()), null, 0, 0, 0, 0, "", "");

			invoices kq = service.timKiem(i);
			view.model.setRowCount(0);

			if (kq != null) {
				view.model.addRow(new Object[] { kq.getIdHoaDon(), kq.getIdDatPhong(), kq.getTienPhong(),
						kq.getPhuThu(), kq.getGiamGia(), kq.getTongTien(), kq.getPhuongThucThanhToan(),
						kq.getTrangThaiThanhToan() });
			} else {
				JOptionPane.showMessageDialog(view, "Khong tim thay hoa don!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Nhap ma hoa don hop le!");
		}
	}

}
