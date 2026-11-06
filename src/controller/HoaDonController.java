package controller;

import java.awt.Window;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.HoaDon;
import service.HoaDonService;
import view.dialog.DialogChiTietHoaDon;
import view.view.ViewHoaDon;

public class HoaDonController {

	ViewHoaDon viewHoaDon;
	HoaDonService dichVu = new HoaDonService();
	DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,###");
	SimpleDateFormat dinhDangNgay = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public HoaDonController(ViewHoaDon viewHoaDon) {
		this.viewHoaDon = viewHoaDon;

		taiDuLieu();

		// Nút xem chi tiết hóa đơn
		viewHoaDon.nutXemChiTiet.addActionListener(suKien -> xemChiTiet());

		// Tìm kiếm & Lọc
		viewHoaDon.nutTimKiem.addActionListener(suKien -> apDungBoLoc());
		viewHoaDon.oNhapTimKiem.addActionListener(suKien -> apDungBoLoc());
		viewHoaDon.hopChonLocTrangThai.addActionListener(suKien -> apDungBoLoc());
		viewHoaDon.hopChonLocThoiGian.addActionListener(suKien -> apDungBoLoc());

		viewHoaDon.nutLamMoi.addActionListener(suKien -> {
			viewHoaDon.lamMoi();
			taiDuLieu();
		});
	}

	public void taiDuLieu() {
		viewHoaDon.moHinh.setRowCount(0);

		ArrayList<HoaDon> danhSach = dichVu.xuat();
		int chung = danhSach.size();
		double daThu = 0;
		double chuaThu = 0;

		for (HoaDon chiSo : danhSach) {
			String luongTrinh = chiSo.getTrangThaiThanhToan();
			if (luongTrinh != null && (luongTrinh.contains("PAID") || luongTrinh.contains("Đã thanh toán"))) {
				daThu += chiSo.getTongTien();
			} else {
				chuaThu += chiSo.getTongTien();
			}

			String ngayLapStr = chiSo.getNgayLap() != null ? dinhDangNgay.format(chiSo.getNgayLap()) : "";
			String tienPhongStr = dinhDangTienTeChinh.format(chiSo.getTienPhong()) + " đ";
			String phuThuStr = dinhDangTienTeChinh.format(chiSo.getPhuThu()) + " đ";
			String giamGiaStr = dinhDangTienTeChinh.format(chiSo.getGiamGia()) + " đ";
			String chungTienStr = dinhDangTienTeChinh.format(chiSo.getTongTien()) + " đ";

			String soPhong = !chiSo.getSoPhong().isEmpty() ? "P." + chiSo.getSoPhong() : "---";
			String tenKhach = !chiSo.getTenKhachHang().isEmpty() ? chiSo.getTenKhachHang() : "---";
			String sdt = !chiSo.getSoDienThoai().isEmpty() ? chiSo.getSoDienThoai() : "---";

			viewHoaDon.moHinh.addRow(new Object[] {
				chiSo.getIdHoaDon(),
				soPhong,
				tenKhach,
				sdt,
				ngayLapStr,
				tienPhongStr,
				phuThuStr,
				giamGiaStr,
				chungTienStr,
				chiSo.getPhuongThucThanhToan() != null ? chiSo.getPhuongThucThanhToan() : "Tiền mặt",
				chiSo.getTrangThaiThanhToan() != null ? chiSo.getTrangThaiThanhToan() : "UNPAID"
			});
		}

		viewHoaDon.nhanThongKe.setText(String.format("Tổng số: %d hóa đơn  |  Đã thu: %s đ  |  Chưa thu: %s đ",
				chung, dinhDangTienTeChinh.format(daThu), dinhDangTienTeChinh.format(chuaThu)));

		apDungBoLoc();
	}

	void apDungBoLoc() {
		String tuKhoa = viewHoaDon.oNhapTimKiem.getText().trim().toLowerCase();
		String trangThaiLoc = (String) viewHoaDon.hopChonLocTrangThai.getSelectedItem();
		String thoiGianLoc = (String) viewHoaDon.hopChonLocThoiGian.getSelectedItem();

		if (viewHoaDon.boSapXep == null) return;

		List<RowFilter<DefaultTableModel, Object>> cacBoLoc = new ArrayList<>();

		// 1. Lọc theo từ khóa (Mã HĐ, Số phòng, Khách hàng, SĐT)
		if (!tuKhoa.isEmpty()) {
			cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
					for (int chiSo = 0; chiSo < phanTu.getValueCount(); chiSo++) {
						Object giaTri = phanTu.getValue(chiSo);
						if (giaTri != null && giaTri.toString().toLowerCase().contains(tuKhoa)) {
							return true;
						}
					}
					return false;
				}
			});
		}

		// 2. Lọc theo trạng thái
		if (trangThaiLoc != null && !trangThaiLoc.equals("Tất cả trạng thái")) {
			String bieuThucChinhQuy = "";
			if (trangThaiLoc.contains("PAID")) bieuThucChinhQuy = "PAID|ĐÃ THANH TOÁN";
			else if (trangThaiLoc.contains("REFUNDED")) bieuThucChinhQuy = "REFUNDED|HOÀN TIỀN";
			else bieuThucChinhQuy = "UNPAID|CHƯA THANH TOÁN";
			cacBoLoc.add(RowFilter.regexFilter("(?i)" + bieuThucChinhQuy, 10));
		}

		// 3. Lọc theo thời gian (Hôm nay, Tháng này, Năm nay)
		if (thoiGianLoc != null && !thoiGianLoc.equals("Tất cả thời gian")) {
			LocalDate homNay = LocalDate.now();
			String chuoiHomNay = homNay.toString(); // YYYY-MM-DD
			String chuoiThang = String.format("%04d-%02d", homNay.getYear(), homNay.getMonthValue());
			String chuoiNam = String.valueOf(homNay.getYear());

			cacBoLoc.add(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ?> phanTu) {
					Object giaTriNgay = phanTu.getValue(4); // Cột ngày lập
					if (giaTriNgay == null) return false;
					String chuoi = giaTriNgay.toString();

					if (thoiGianLoc.equals("Hôm nay")) {
						return chuoi.startsWith(chuoiHomNay);
					} else if (thoiGianLoc.equals("Tháng này")) {
						return chuoi.startsWith(chuoiThang);
					} else if (thoiGianLoc.equals("Năm nay")) {
						return chuoi.startsWith(chuoiNam);
					}
					return true;
				}
			});
		}

		if (cacBoLoc.isEmpty()) {
			viewHoaDon.boSapXep.setRowFilter(null);
		} else {
			viewHoaDon.boSapXep.setRowFilter(RowFilter.andFilter(cacBoLoc));
		}
	}

	Window getParentWindow() {
		Window chieuRong = SwingUtilities.getWindowAncestor(viewHoaDon.getContentPane());
		return chieuRong != null ? chieuRong : viewHoaDon;
	}

	HoaDon timTheoMa(int ma) {
		HoaDon bangHienTai = new HoaDon();
		bangHienTai.setIdHoaDon(ma);
		return dichVu.timKiem(bangHienTai);
	}

	// Xem & In hóa đơn
	void xemChiTiet() {
		int hangDaChon = viewHoaDon.bang.getSelectedRow();
		if (hangDaChon == -1) {
			JOptionPane.showMessageDialog(viewHoaDon, "Vui lòng chọn một hóa đơn trong bảng để xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int hangMoHinh = viewHoaDon.bang.convertRowIndexToModel(hangDaChon);
		int maHD = (int) viewHoaDon.moHinh.getValueAt(hangMoHinh, 0);

		HoaDon hoaDon = timTheoMa(maHD);
		if (hoaDon == null) {
			JOptionPane.showMessageDialog(viewHoaDon, "Không tìm thấy hóa đơn #" + maHD, "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DialogChiTietHoaDon hopThoai = new DialogChiTietHoaDon(getParentWindow(), hoaDon);
		hopThoai.setVisible(true);

		if (hopThoai.isDaThanhToanXong()) {
			taiDuLieu();
		}
	}

}
