package controller;
// báo cáo thống kê
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
import javax.swing.JTextArea;
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
        viewBaoCaoThongKe.btIn.addActionListener(ac -> xuLyIn());
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

    private void xuLyIn() {
        // Xây dựng nội dung báo cáo dạng văn bản
        StringBuilder sb = new StringBuilder();
        String khoangNgay = viewBaoCaoThongKe.getTuNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " – " + viewBaoCaoThongKe.getDenNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        sb.append("============================================================\n");
        sb.append("        HỆ THỐNG QUẢN LÝ KHÁCH SẠN\n");
        sb.append("        BÁO CÁO THỐNG KÊ DOANH THU\n");
        sb.append("        Kỳ: ").append(khoangNgay).append("\n");
        sb.append("============================================================\n\n");

        // Tổng quan
        sb.append("TỔNG QUAN\n");
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("  %-30s %s\n", "Doanh thu kỳ:", viewBaoCaoThongKe.nhanGiaTriChungDoanhThu.getText()));
        sb.append(String.format("  %-30s %s hóa đơn\n", "Hóa đơn đã thu:", viewBaoCaoThongKe.nhanGiaTriDoanhThuThang.getText()));
        sb.append(String.format("  %-30s %s\n", "Lượt thuê phòng:", viewBaoCaoThongKe.nhanGiaTriDoanhThuNgay.getText()));
        sb.append("\n");

        // Bảng thống kê hóa đơn
        sb.append("THỐNG KÊ DOANH THU CHI TIẾT\n");
        sb.append("------------------------------------------------------------\n");
        javax.swing.table.TableModel moHinhHD = viewBaoCaoThongKe.bangHoaDon.getModel();
        // Header
        sb.append(String.format("%-6s %-20s %-6s %-18s %-12s %-14s %-12s\n",
                "MãHĐ", "Thời gian", "Phòng", "Khách hàng", "Tiền phòng", "Thực thu", "Thanh toán"));
        sb.append("    ").append("-".repeat(90)).append("\n");
        for (int i = 0; i < moHinhHD.getRowCount(); i++) {
            sb.append(String.format("%-6s %-20s %-6s %-18s %-12s %-14s %-12s\n",
                    obj(moHinhHD.getValueAt(i, 0)),   // Mã HĐ
                    obj(moHinhHD.getValueAt(i, 1)),   // Thời gian
                    obj(moHinhHD.getValueAt(i, 2)),   // Phòng
                    obj(moHinhHD.getValueAt(i, 3)),   // Khách hàng
                    obj(moHinhHD.getValueAt(i, 5)),   // Tiền phòng
                    obj(moHinhHD.getValueAt(i, 8)),   // Thực thu
                    obj(moHinhHD.getValueAt(i, 9))    // Thanh toán
            ));
        }
        sb.append("\n");

        // Bảng thống kê theo phòng
        sb.append("TỈ LỆ THUÊ PHÒNG\n");
        sb.append("------------------------------------------------------------\n");
        javax.swing.table.TableModel moHinhPhong = viewBaoCaoThongKe.bangPhong.getModel();
        sb.append(String.format("%-8s %-20s %-8s %-12s %-16s %-8s\n",
                "Phòng", "Loại phòng", "Tầng", "Lượt thuê", "Doanh thu", "Tỷ trọng"));
        sb.append("    ").append("-".repeat(74)).append("\n");
        for (int i = 0; i < moHinhPhong.getRowCount(); i++) {
            sb.append(String.format("%-8s %-20s %-8s %-12s %-16s %-8s\n",
                    obj(moHinhPhong.getValueAt(i, 0)),
                    obj(moHinhPhong.getValueAt(i, 1)),
                    obj(moHinhPhong.getValueAt(i, 2)),
                    obj(moHinhPhong.getValueAt(i, 3)),
                    obj(moHinhPhong.getValueAt(i, 4)),
                    obj(moHinhPhong.getValueAt(i, 5))
            ));
        }
        sb.append("\n");
        sb.append("============================================================\n");
        sb.append("  ").append(viewBaoCaoThongKe.nhanThongKeKy.getText()).append("\n");
        sb.append("============================================================\n");

        // In thật bằng JTextArea.print()
        JTextArea vungIn = new JTextArea(sb.toString());
        vungIn.setFont(new Font("Monospaced", Font.PLAIN, 11));
        try {
            boolean daIn = vungIn.print(
                    new java.awt.print.MessageFormat("BÁO CÁO THỐNG KÊ DOANH THU  –  " + khoangNgay),
                    new java.awt.print.MessageFormat("Trang {0}"),
                    true,
                    null,
                    null,
                    true
            );
            if (daIn) {
                JOptionPane.showMessageDialog(viewBaoCaoThongKe,
                        "In báo cáo thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (java.awt.print.PrinterException ex) {
            JOptionPane.showMessageDialog(viewBaoCaoThongKe,
                    "Lỗi khi in: " + ex.getMessage(), "Lỗi in", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obj(Object o) {
        return o != null ? o.toString() : "";
    }
}
