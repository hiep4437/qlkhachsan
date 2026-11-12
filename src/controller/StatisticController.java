package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import dao.ThongKeDAO;
import service.StatisticService;
import view.view.ViewBaoCaoThongKe;
import view.TableAlignment;

public class StatisticController {

    private ViewBaoCaoThongKe viewBaoCaoThongKe;
    private StatisticService dichVu = new StatisticService();
    private DecimalFormat dinhDangTienTeChinh = new DecimalFormat("#,### đ");
    private DecimalFormat dinhDangTyLe = new DecimalFormat("0.0'%'");

    public StatisticController(ViewBaoCaoThongKe viewBaoCaoThongKe) {
        this.viewBaoCaoThongKe = viewBaoCaoThongKe;

        caiDatRenderers();
        taiDuLieu();

        viewBaoCaoThongKe.btLoc.addActionListener(ac -> taiDuLieu());
    }

    private void caiDatRenderers() {
        TableAlignment.centerData(viewBaoCaoThongKe.bangPhong);
        TableAlignment.centerData(viewBaoCaoThongKe.bangHoaDon);

        // Bảng Phòng
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(0).setPreferredWidth(80);
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(dangChon ? bang.getSelectionForeground() : new Color(31, 78, 121));
                return thanhPhan;
            }
        });
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(1).setPreferredWidth(140);
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(2).setPreferredWidth(80);
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(3).setPreferredWidth(100);
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(4).setPreferredWidth(140);
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(dangChon ? bang.getSelectionForeground() : new Color(192, 57, 43));
                return thanhPhan;
            }
        });
        viewBaoCaoThongKe.bangPhong.getColumnModel().getColumn(5).setPreferredWidth(90);

        // Bảng Hóa đơn
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(0).setPreferredWidth(60);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(1).setPreferredWidth(130);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(2).setPreferredWidth(80);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(3).setPreferredWidth(140);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(5).setPreferredWidth(95);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(6).setPreferredWidth(80);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(7).setPreferredWidth(80);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(8).setPreferredWidth(110);
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable bang, Object giaTri, boolean dangChon,
                                                           boolean hasTieuDiem, int hang, int chiSoCot) {
                Component thanhPhan = super.getTableCellRendererComponent(bang, giaTri, dangChon, hasTieuDiem, hang, chiSoCot);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(dangChon ? bang.getSelectionForeground() : new Color(39, 174, 96));
                return thanhPhan;
            }
        });
        viewBaoCaoThongKe.bangHoaDon.getColumnModel().getColumn(9).setPreferredWidth(100);
    }

    public void taiDuLieu() {
        if (viewBaoCaoThongKe.getDenNgay().isBefore(viewBaoCaoThongKe.getTuNgay())) {
            JOptionPane.showMessageDialog(
                    viewBaoCaoThongKe,
                    "Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.",
                    "Khoảng ngày không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date tuNgay = Date.valueOf(viewBaoCaoThongKe.getTuNgay());
        Date denNgay = Date.valueOf(viewBaoCaoThongKe.getDenNgay());
        try {
            ArrayList<ThongKeDAO.ChiTietHoaDonBaoCao> danhSachHoaDon = dichVu.layHoaDonTheoKhoang(tuNgay, denNgay);
            ArrayList<ThongKeDAO.ThongKePhong> danhSachPhong = dichVu.thongKeTheoKhoang(tuNgay, denNgay);
            double chungDoanhThuKy = 0;

            for (ThongKeDAO.ChiTietHoaDonBaoCao chieuCao : danhSachHoaDon) {
                chungDoanhThuKy += chieuCao.chungTien;
            }

            int chungLuotThue = 0;
            for (ThongKeDAO.ThongKePhong phong : danhSachPhong) {
                chungLuotThue += phong.soLuotThue;
            }

            viewBaoCaoThongKe.nhanGiaTriChungDoanhThu.setText(dinhDangTienTeChinh.format(chungDoanhThuKy));
            viewBaoCaoThongKe.nhanGiaTriDoanhThuThang.setText(Integer.toString(danhSachHoaDon.size()));
            viewBaoCaoThongKe.nhanGiaTriDoanhThuNgay.setText(Integer.toString(chungLuotThue));
            String khoangNgay = viewBaoCaoThongKe.getTuNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " – " + viewBaoCaoThongKe.getDenNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            viewBaoCaoThongKe.nhanThongKeKy.setText("Thực thu " + khoangNgay + ": " + dinhDangTienTeChinh.format(chungDoanhThuKy));

            viewBaoCaoThongKe.moHinhHoaDon.setRowCount(0);
            for (ThongKeDAO.ChiTietHoaDonBaoCao chieuCao : danhSachHoaDon) {
                viewBaoCaoThongKe.moHinhHoaDon.addRow(new Object[] {
                    chieuCao.maHoaDon,
                    chieuCao.ngayLap,
                    chieuCao.soPhong,
                    chieuCao.hoTen,
                    chieuCao.sdt,
                    dinhDangTienTeChinh.format(chieuCao.tienPhong),
                    dinhDangTienTeChinh.format(chieuCao.phuThu),
                    dinhDangTienTeChinh.format(chieuCao.giamGia),
                    dinhDangTienTeChinh.format(chieuCao.chungTien),
                    chieuCao.phuongThuc
                });
            }

            viewBaoCaoThongKe.moHinhPhong.setRowCount(0);
            ArrayList<String> tenPhongBieuThucHien = new ArrayList<>();
            ArrayList<String> chiTietBieuDo = new ArrayList<>();
            ArrayList<Double> doanhThuBieuDo = new ArrayList<>();
            for (ThongKeDAO.ThongKePhong khung : danhSachPhong) {
                double tyTrong = (chungDoanhThuKy > 0) ? (khung.tongDoanhThuKy / chungDoanhThuKy * 100) : 0;
                String tangStr = khung.tang > 0 ? "Tầng " + khung.tang : "---";
                tenPhongBieuThucHien.add("Phòng " + khung.soPhong);
                chiTietBieuDo.add(khung.soLuotThue + " lượt thuê · " + khung.tenLoaiPhong);
                doanhThuBieuDo.add(khung.tongDoanhThuKy);

                viewBaoCaoThongKe.moHinhPhong.addRow(new Object[] {
                    "P." + khung.soPhong,
                    khung.tenLoaiPhong,
                    tangStr,
                    khung.soLuotThue + " lượt",
                    dinhDangTienTeChinh.format(khung.tongDoanhThuKy),
                    dinhDangTyLe.format(tyTrong)
                });
            }
            viewBaoCaoThongKe.capNhatBieuDoPhong(tenPhongBieuThucHien, chiTietBieuDo, doanhThuBieuDo);
        } catch (Exception suKien) {
            JOptionPane.showMessageDialog(
                    viewBaoCaoThongKe,
                    "Không thể xem thống kê: " + suKien.getMessage(),
                    "Lỗi tải thống kê",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
