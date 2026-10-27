
package controller;

import view.ViewDatPhong;
import model.bookings;
import model.customers;
import model.rooms;
import service.BookingService;
import service.CustomerService;
import service.RoomService;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.swing.*;

public class BookingController {
    ViewDatPhong view;
    BookingService service = new BookingService();
    CustomerService customerService = new CustomerService();
    RoomService roomService = new RoomService();

    int idKhachHang = 0;

    public BookingController(ViewDatPhong view) {
        this.view = view;

        taiDuLieu();

        view.btnCheckIn.addActionListener(e -> checkIn());
        view.btnCheckOut.addActionListener(e -> checkOut());
        view.btnLayGia.addActionListener(e -> layGiaTien());
        view.btnKiemTraCCCD.addActionListener(e -> kiemTraCCCD());
        view.btnTimPhong.addActionListener(e -> timPhongTrong());
        view.btnTimKiem.addActionListener(e -> timKiemTheoCCCD());

        view.btnDatLai.addActionListener(e -> {
            view.lamMoi();
            idKhachHang = 0;
            taiDuLieu();
        });
    }

    // Tai danh sach dat phong
    void taiDuLieu() {
        try {
            view.model.setRowCount(0);

            ArrayList<customers> dsKhach = customerService.xuat();

            for (bookings b : service.xuat()) {
                String cccd = "";

                for (customers kh : dsKhach) {
                    if (kh.getIdKhachHang() == b.getIdKhachHang()) {
                        cccd = kh.getSoCCCD();
                        break;
                    }
                }

                view.model.addRow(new Object[]{
                    b.getIdDatPhong(),
                    cccd,
                    b.getNgayNhanPhong(),
                    b.getNgayTraPhong(),
                    "",
                    "",
                    hienThiTrangThai(b.getTrangThai()),
                    b.getGhiChu()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Loi tai du lieu: " + e.getMessage());
        }
    }

    // Chuyen trang thai CSDL sang tieng Viet
    String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "";

        switch (trangThai) {
            case "BOOKED":
                return "Đã đặt";
            case "CHECKED_IN":
                return "Đã nhận phòng";
            case "CHECKED_OUT":
                return "Đã trả phòng";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return trangThai;
        }
    }

    // Kiem tra CCCD, neu chua co thi them khach hang
    void kiemTraCCCD() {
        String cccd = view.txtCCCD.getText().trim();

        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD!");
            return;
        }

        customers tk = new customers();
        tk.setSoCCCD(cccd);

        customers kh = customerService.timKiem(tk);

        if (kh != null) {
            idKhachHang = kh.getIdKhachHang();
            JOptionPane.showMessageDialog(view,
                    "Tìm thấy khách hàng: " + kh.getNameKhachHang());
            return;
        }

        JTextField ten = new JTextField();
        JTextField sdt = new JTextField();
        JTextField diaChi = new JTextField();
        JTextField email = new JTextField();

        Object[] form = {
            "CCCD:", new JLabel(cccd),
            "Họ tên:", ten,
            "Số điện thoại:", sdt,
            "Địa chỉ:", diaChi,
            "Email:", email
        };

        int chon = JOptionPane.showConfirmDialog(
                view, form, "Thêm khách hàng",
                JOptionPane.OK_CANCEL_OPTION);

        if (chon != JOptionPane.OK_OPTION) return;

        if (ten.getText().trim().isEmpty()
                || sdt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập họ tên và số điện thoại!");
            return;
        }

        customers moi = new customers(
                0,
                ten.getText().trim(),
                cccd,
                sdt.getText().trim(),
                email.getText().trim(),
                diaChi.getText().trim()
        );

        if (customerService.them(moi) > 0) {
            customers timLai = customerService.timKiem(tk);

            if (timLai != null) {
                idKhachHang = timLai.getIdKhachHang();
                JOptionPane.showMessageDialog(view,
                        "Thêm khách hàng thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(view,
                    "Thêm khách hàng thất bại!");
        }
    }

    // Check-in
    void checkIn() {
        int row = view.table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Hãy chọn lượt đặt phòng!");
            return;
        }

        int id = Integer.parseInt(
                view.model.getValueAt(row, 0).toString());

        String trangThai = view.model.getValueAt(row, 6).toString();

        if (!trangThai.equals("BOOKED")
                && !trangThai.equals("Đã đặt")) {
            JOptionPane.showMessageDialog(view,
                    "Lượt đặt phòng không thể check-in!");
            return;
        }

        if (service.capNhatTrangThai(id, "CHECKED_IN") > 0) {
            JOptionPane.showMessageDialog(view, "Check-in thành công!");
            taiDuLieu();
        } else {
            JOptionPane.showMessageDialog(view, "Check-in thất bại!");
        }
    }

    // Check-out, chi cho phep khach da check-in
    void checkOut() {
        int row = view.table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Hãy chọn lượt đặt phòng!");
            return;
        }

        String trangThai = view.model.getValueAt(row, 6).toString();

        if (!trangThai.equals("CHECKED_IN")
                && !trangThai.equals("Đã nhận phòng")) {
            JOptionPane.showMessageDialog(view,
                    "Khách chưa check-in nên không thể check-out!");
            return;
        }

        int id = Integer.parseInt(
                view.model.getValueAt(row, 0).toString());

        if (service.capNhatTrangThai(id, "CHECKED_OUT") > 0) {
            JOptionPane.showMessageDialog(view, "Check-out thành công!");
            taiDuLieu();
        } else {
            JOptionPane.showMessageDialog(view, "Check-out thất bại!");
        }
    }

    // Tinh so dem va tam tinh tien phong da chon
    void layGiaTien() {
        try {
            Date ngayNhan = Date.valueOf(
                    view.cboNgayNhan.getSelectedItem().toString());

            Date ngayTra = Date.valueOf(
                    view.cboNgayTra.getSelectedItem().toString());

            long soDem = ChronoUnit.DAYS.between(
                    ngayNhan.toLocalDate(),
                    ngayTra.toLocalDate());

            if (soDem <= 0) {
                JOptionPane.showMessageDialog(view,
                        "Ngày trả phải sau ngày nhận!");
                return;
            }

            int row = view.table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(view,
                        "Số đêm: " + soDem
                        + "\nHãy chọn phòng để tính tổng tiền.");
                return;
            }

            JOptionPane.showMessageDialog(view,
                    "Số đêm: " + soDem
                    + "\nChức năng tính tiền cần mã phòng và giá phòng.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Ngày không hợp lệ!");
        }
    }

    // Lay tang tu so phong, vi du 101 la tang 1
    int layTang(String soPhong) {
        try {
            if (soPhong == null || soPhong.isEmpty()) return -1;
            return Integer.parseInt(soPhong.substring(0, 1));
        } catch (Exception e) {
            return -1;
        }
    }

    // Tim phong theo tang dang chon
    void timPhongTrong() {
        try {
            String tangChon = view.cboTang.getSelectedItem().toString();
            int tang = Integer.parseInt(tangChon.replaceAll("[^0-9]", ""));

            Date ngayNhan = Date.valueOf(
                    view.cboNgayNhan.getSelectedItem().toString());

            Date ngayTra = Date.valueOf(
                    view.cboNgayTra.getSelectedItem().toString());

            if (!ngayTra.after(ngayNhan)) {
                JOptionPane.showMessageDialog(view,
                        "Ngày trả phải sau ngày nhận!");
                return;
            }

            ArrayList<rooms> dsPhong = roomService.xuat();
            String[] cot = {
                "Mã phòng", "Số phòng", "Mã loại phòng",
                "Giá mỗi đêm", "Trạng thái"
            };

            javax.swing.table.DefaultTableModel bang =
                    new javax.swing.table.DefaultTableModel(cot, 0);

            for (rooms p : dsPhong) {
                int tangPhong = layTang(p.getSoPhong());

                if (tangPhong == tang
                        && ("AVAILABLE".equalsIgnoreCase(p.getTrangThai())
                        || "TRỐNG".equalsIgnoreCase(p.getTrangThai())
                        || "TRONG".equalsIgnoreCase(p.getTrangThai()))) {

                    bang.addRow(new Object[]{
                        p.getMaPhong(),
                        p.getSoPhong(),
                        p.getMaLoaiPhong(),
                        p.getGiaMoiDem(),
                        p.getTrangThai()
                    });
                }
            }

            JTable tablePhong = new JTable(bang);
            JOptionPane.showMessageDialog(view,
                    new JScrollPane(tablePhong),
                    "Danh sách phòng tầng " + tang,
                    JOptionPane.INFORMATION_MESSAGE);

            if (bang.getRowCount() == 0) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy phòng trống ở tầng " + tang);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi tìm phòng: " + e.getMessage());
        }
    }

    // Tim dat phong theo CCCD
    void timKiemTheoCCCD() {
        try {
            String cccd = view.txtTimKiem.getText().trim();
            view.model.setRowCount(0);

            customers kh = null;

            if (!cccd.isEmpty()) {
                customers tk = new customers();
                tk.setSoCCCD(cccd);
                kh = customerService.timKiem(tk);

                if (kh == null) {
                    JOptionPane.showMessageDialog(view,
                            "Không tìm thấy khách hàng!");
                    return;
                }
            }

            ArrayList<customers> dsKhach = customerService.xuat();

            for (bookings b : service.xuat()) {
                if (kh == null
                        || b.getIdKhachHang() == kh.getIdKhachHang()) {

                    String soCCCD = "";

                    for (customers c : dsKhach) {
                        if (c.getIdKhachHang() == b.getIdKhachHang()) {
                            soCCCD = c.getSoCCCD();
                            break;
                        }
                    }

                    view.model.addRow(new Object[]{
                        b.getIdDatPhong(),
                        soCCCD,
                        b.getNgayNhanPhong(),
                        b.getNgayTraPhong(),
                        "",
                        "",
                        hienThiTrangThai(b.getTrangThai()),
                        b.getGhiChu()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi tìm kiếm: " + e.getMessage());
        }
    }
    
}
