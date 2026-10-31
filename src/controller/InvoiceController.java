package controller;

import java.awt.Window;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.invoices;
import service.InvoiceService;
import view.DialogChiTietHoaDon;
import view.ViewHoaDon;

public class InvoiceController {

	ViewHoaDon view;
	InvoiceService service = new InvoiceService();
	DecimalFormat df = new DecimalFormat("#,###");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public InvoiceController(ViewHoaDon view) {
		this.view = view;

		taiDuLieu();

		// Nút xem chi tiết hóa đơn
		view.btnXemChiTiet.addActionListener(e -> xemChiTiet());

		// Tìm kiếm & Lọc
		view.btnTimKiem.addActionListener(e -> apDungBoLoc());
		view.txtTimKiem.addActionListener(e -> apDungBoLoc());
		view.cboLocTrangThai.addActionListener(e -> apDungBoLoc());
		view.cboLocThoiGian.addActionListener(e -> apDungBoLoc());

		// Nút làm mới (5s countdown & disable)
		view.btnLamMoi.addActionListener(e -> {
			view.batDauDemNguocLamMoi(() -> {
				view.lamMoi();
				taiDuLieu();
			});
		});
	}

	public void taiDuLieu() {
		view.model.setRowCount(0);

		ArrayList<invoices> ds = service.xuat();
		int tong = ds.size();
		double daThu = 0;
		double chuaThu = 0;

		for (invoices i : ds) {
			String st = i.getTrangThaiThanhToan();
			if (st != null && (st.contains("PAID") || st.contains("Đã thanh toán"))) {
				daThu += i.getTongTien();
			} else {
				chuaThu += i.getTongTien();
			}

			String ngayLapStr = i.getNgayLap() != null ? sdf.format(i.getNgayLap()) : "";
			String tienPhongStr = df.format(i.getTienPhong()) + " đ";
			String phuThuStr = df.format(i.getPhuThu()) + " đ";
			String giamGiaStr = df.format(i.getGiamGia()) + " đ";
			String tongTienStr = df.format(i.getTongTien()) + " đ";

			String soPhong = !i.getSoPhong().isEmpty() ? "P." + i.getSoPhong() : "---";
			String tenKhach = !i.getTenKhachHang().isEmpty() ? i.getTenKhachHang() : "---";
			String sdt = !i.getSoDienThoai().isEmpty() ? i.getSoDienThoai() : "---";

			view.model.addRow(new Object[] {
				i.getIdHoaDon(),
				soPhong,
				tenKhach,
				sdt,
				ngayLapStr,
				tienPhongStr,
				phuThuStr,
				giamGiaStr,
				tongTienStr,
				i.getPhuongThucThanhToan() != null ? i.getPhuongThucThanhToan() : "Tiền mặt",
				i.getTrangThaiThanhToan() != null ? i.getTrangThaiThanhToan() : "UNPAID"
			});
		}

		view.lblThongKe.setText(String.format("Tổng số: %d hóa đơn  |  Đã thu: %s đ  |  Chưa thu: %s đ",
				tong, df.format(daThu), df.format(chuaThu)));

		apDungBoLoc();
	}

	void apDungBoLoc() {
		String tuKhoa = view.txtTimKiem.getText().trim().toLowerCase();
		String trangThaiLoc = (String) view.cboLocTrangThai.getSelectedItem();
		String thoiGianLoc = (String) view.cboLocThoiGian.getSelectedItem();

		if (view.sorter == null) return;

		List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

		// 1. Lọc theo từ khóa (Mã HĐ, Số phòng, Khách hàng, SĐT)
		if (!tuKhoa.isEmpty()) {
			filters.add(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
					for (int i = 0; i < entry.getValueCount(); i++) {
						Object val = entry.getValue(i);
						if (val != null && val.toString().toLowerCase().contains(tuKhoa)) {
							return true;
						}
					}
					return false;
				}
			});
		}

		// 2. Lọc theo trạng thái
		if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
			String regex = "";
			if (trangThaiLoc.contains("PAID")) regex = "PAID|ĐÃ THANH TOÁN";
			else if (trangThaiLoc.contains("REFUNDED")) regex = "REFUNDED|HOÀN TIỀN";
			else regex = "UNPAID|CHƯA THANH TOÁN";
			filters.add(RowFilter.regexFilter("(?i)" + regex, 10));
		}

		// 3. Lọc theo thời gian (Hôm nay, Tháng này, Năm nay)
		if (thoiGianLoc != null && !thoiGianLoc.equals("Tất cả thời gian")) {
			LocalDate today = LocalDate.now();
			String todayStr = today.toString(); // YYYY-MM-DD
			String monthStr = String.format("%04d-%02d", today.getYear(), today.getMonthValue());
			String yearStr = String.valueOf(today.getYear());

			filters.add(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
					Object valNgay = entry.getValue(4); // Cột ngày lập
					if (valNgay == null) return false;
					String s = valNgay.toString();

					if (thoiGianLoc.equals("Hôm nay")) {
						return s.startsWith(todayStr);
					} else if (thoiGianLoc.equals("Tháng này")) {
						return s.startsWith(monthStr);
					} else if (thoiGianLoc.equals("Năm nay")) {
						return s.startsWith(yearStr);
					}
					return true;
				}
			});
		}

		if (filters.isEmpty()) {
			view.sorter.setRowFilter(null);
		} else {
			view.sorter.setRowFilter(RowFilter.andFilter(filters));
		}
	}

	Window getParentWindow() {
		Window w = SwingUtilities.getWindowAncestor(view.getContentPane());
		return w != null ? w : view;
	}

	invoices timTheoMa(int id) {
		invoices t = new invoices();
		t.setIdHoaDon(id);
		return service.timKiem(t);
	}

	// Xem & In hóa đơn
	void xemChiTiet() {
		int selectedRow = view.table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một hóa đơn trong bảng để xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int modelRow = view.table.convertRowIndexToModel(selectedRow);
		int maHD = (int) view.model.getValueAt(modelRow, 0);

		invoices inv = timTheoMa(maHD);
		if (inv == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy hóa đơn #" + maHD, "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DialogChiTietHoaDon dlg = new DialogChiTietHoaDon(getParentWindow(), inv);
		dlg.setVisible(true);

		if (dlg.isDaThanhToanXong()) {
			taiDuLieu();
		}
	}

}
