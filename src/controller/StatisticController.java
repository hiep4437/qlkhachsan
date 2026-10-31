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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import dao.ThongKeDAO;
import service.StatisticService;
import view.view.ViewBaoCaoThongKe;

public class StatisticController {

    private ViewBaoCaoThongKe view;
    private StatisticService service = new StatisticService();
    private DecimalFormat df = new DecimalFormat("#,### đ");
    private DecimalFormat dfTyLe = new DecimalFormat("0.0'%'");

    public StatisticController(ViewBaoCaoThongKe view) {
        this.view = view;

        caiDatRenderers();
        taiDuLieu();

        view.btnLoc.addActionListener(e -> taiDuLieu());
    }

    private void caiDatRenderers() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        // Bảng Phòng
        view.tblPhong.getColumnModel().getColumn(0).setPreferredWidth(80);
        view.tblPhong.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setForeground(isSelected ? table.getSelectionForeground() : new Color(31, 78, 121));
                return c;
            }
        });
        view.tblPhong.getColumnModel().getColumn(1).setPreferredWidth(140);
        view.tblPhong.getColumnModel().getColumn(2).setPreferredWidth(80);
        view.tblPhong.getColumnModel().getColumn(2).setCellRenderer(center);
        view.tblPhong.getColumnModel().getColumn(3).setPreferredWidth(100);
        view.tblPhong.getColumnModel().getColumn(3).setCellRenderer(center);
        view.tblPhong.getColumnModel().getColumn(4).setPreferredWidth(140);
        view.tblPhong.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(RIGHT);
                setForeground(isSelected ? table.getSelectionForeground() : new Color(192, 57, 43));
                return c;
            }
        });
        view.tblPhong.getColumnModel().getColumn(5).setPreferredWidth(90);
        view.tblPhong.getColumnModel().getColumn(5).setCellRenderer(center);

        // Bảng Hóa đơn
        view.tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(60);
        view.tblHoaDon.getColumnModel().getColumn(0).setCellRenderer(center);
        view.tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(130);
        view.tblHoaDon.getColumnModel().getColumn(1).setCellRenderer(center);
        view.tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(80);
        view.tblHoaDon.getColumnModel().getColumn(2).setCellRenderer(center);
        view.tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(140);
        view.tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
        view.tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(center);
        view.tblHoaDon.getColumnModel().getColumn(5).setPreferredWidth(95);
        view.tblHoaDon.getColumnModel().getColumn(5).setCellRenderer(right);
        view.tblHoaDon.getColumnModel().getColumn(6).setPreferredWidth(80);
        view.tblHoaDon.getColumnModel().getColumn(6).setCellRenderer(right);
        view.tblHoaDon.getColumnModel().getColumn(7).setPreferredWidth(80);
        view.tblHoaDon.getColumnModel().getColumn(7).setCellRenderer(right);
        view.tblHoaDon.getColumnModel().getColumn(8).setPreferredWidth(110);
        view.tblHoaDon.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(RIGHT);
                setForeground(isSelected ? table.getSelectionForeground() : new Color(39, 174, 96));
                return c;
            }
        });
        view.tblHoaDon.getColumnModel().getColumn(9).setPreferredWidth(100);
        view.tblHoaDon.getColumnModel().getColumn(9).setCellRenderer(center);
    }

    public void taiDuLieu() {
        if (view.getDenNgay().isBefore(view.getTuNgay())) {
            JOptionPane.showMessageDialog(
                    view,
                    "Ngày kết thúc phải bằng hoặc sau ngày bắt đầu.",
                    "Khoảng ngày không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date tuNgay = Date.valueOf(view.getTuNgay());
        Date denNgay = Date.valueOf(view.getDenNgay());
        try {
            ArrayList<ThongKeDAO.ChiTietHoaDonBaoCao> dsHD = service.layHoaDonTheoKhoang(tuNgay, denNgay);
            ArrayList<ThongKeDAO.ThongKePhong> dsPhong = service.thongKeTheoKhoang(tuNgay, denNgay);
            double tongDoanhThuKy = 0;

            for (ThongKeDAO.ChiTietHoaDonBaoCao h : dsHD) {
                tongDoanhThuKy += h.tongTien;
            }

            int tongLuotThue = 0;
            for (ThongKeDAO.ThongKePhong phong : dsPhong) {
                tongLuotThue += phong.soLuotThue;
            }

            view.lblValTongDoanhThu.setText(df.format(tongDoanhThuKy));
            view.lblValDoanhThuThang.setText(Integer.toString(dsHD.size()));
            view.lblValDoanhThuNgay.setText(Integer.toString(tongLuotThue));
            String khoangNgay = view.getTuNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " – " + view.getDenNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            view.lblThongKeKy.setText("Thực thu " + khoangNgay + ": " + df.format(tongDoanhThuKy));

            view.modelHoaDon.setRowCount(0);
            for (ThongKeDAO.ChiTietHoaDonBaoCao h : dsHD) {
                view.modelHoaDon.addRow(new Object[] {
                    h.maHoaDon,
                    h.ngayLap,
                    h.soPhong,
                    h.hoTen,
                    h.sdt,
                    df.format(h.tienPhong),
                    df.format(h.phuThu),
                    df.format(h.giamGia),
                    df.format(h.tongTien),
                    h.phuongThuc
                });
            }

            view.modelPhong.setRowCount(0);
            ArrayList<String> tenPhongBieuDo = new ArrayList<>();
            ArrayList<String> chiTietBieuDo = new ArrayList<>();
            ArrayList<Double> doanhThuBieuDo = new ArrayList<>();
            for (ThongKeDAO.ThongKePhong p : dsPhong) {
                double tyTrong = (tongDoanhThuKy > 0) ? (p.tongDoanhThu / tongDoanhThuKy * 100) : 0;
                String tangStr = p.tang > 0 ? "Tầng " + p.tang : "---";
                tenPhongBieuDo.add("Phòng " + p.soPhong);
                chiTietBieuDo.add(p.soLuotThue + " lượt thuê · " + p.tenLoaiPhong);
                doanhThuBieuDo.add(p.tongDoanhThu);

                view.modelPhong.addRow(new Object[] {
                    "P." + p.soPhong,
                    p.tenLoaiPhong,
                    tangStr,
                    p.soLuotThue + " lượt",
                    df.format(p.tongDoanhThu),
                    dfTyLe.format(tyTrong)
                });
            }
            view.capNhatBieuDoPhong(tenPhongBieuDo, chiTietBieuDo, doanhThuBieuDo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    view,
                    "Không thể xem thống kê: " + e.getMessage(),
                    "Lỗi tải thống kê",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
