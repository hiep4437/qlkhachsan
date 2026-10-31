package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import dao.StatisticDAO;
import service.StatisticService;
import view.ViewBaoCaoThongKe;

public class StatisticController {

    private ViewBaoCaoThongKe view;
    private StatisticService service = new StatisticService();
    private DecimalFormat df = new DecimalFormat("#,### đ");
    private DecimalFormat dfTyLe = new DecimalFormat("0.0'%'");

    public StatisticController(ViewBaoCaoThongKe view) {
        this.view = view;

        caiDatRenderers();
        taiDuLieu();

        // Sự kiện lọc
        view.btnLoc.addActionListener(e -> taiDuLieuTheoKy());
        view.cboKyBaoCao.addActionListener(e -> taiDuLieuTheoKy());

        // Nút làm mới 5s countdown
        view.btnLamMoi.addActionListener(e -> {
            view.batDauDemNguocLamMoi(() -> {
                taiDuLieu();
            });
        });
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
        // 1. Cập nhật các Cards KPI
        double tongDoanhThu = service.getTongDoanhThu();
        double doanhThuThang = service.getDoanhThuThangNay();
        double doanhThuNgay = service.getDoanhThuHomNay();

        view.lblValTongDoanhThu.setText(df.format(tongDoanhThu));
        view.lblValDoanhThuThang.setText(df.format(doanhThuThang));
        view.lblValDoanhThuNgay.setText(df.format(doanhThuNgay));

        // 2. Cập nhật dữ liệu bảng theo kỳ
        taiDuLieuTheoKy();
    }

    public void taiDuLieuTheoKy() {
        String ky = (String) view.cboKyBaoCao.getSelectedItem();
        if (ky == null) ky = "Tháng này";

        // Tải danh sách hóa đơn theo kỳ
        ArrayList<StatisticDAO.ChiTietHoaDonBaoCao> dsHD = service.layHoaDonTheoKy(ky);
        view.modelHoaDon.setRowCount(0);
        double tongTienKy = 0;

        for (StatisticDAO.ChiTietHoaDonBaoCao h : dsHD) {
            tongTienKy += h.tongTien;
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

        view.lblThongKeKy.setText("Tổng thực thu trong kỳ (" + ky + "): " + df.format(tongTienKy));

        // Tải thống kê doanh thu theo phòng
        ArrayList<StatisticDAO.ThongKePhong> dsPhong = service.thongKeTheoPhong(ky);
        view.modelPhong.setRowCount(0);

        for (StatisticDAO.ThongKePhong p : dsPhong) {
            double tyTrong = (tongTienKy > 0) ? (p.tongDoanhThu / tongTienKy * 100) : 0;
            String tangStr = p.tang > 0 ? "Tầng " + p.tang : "---";

            view.modelPhong.addRow(new Object[] {
                "P." + p.soPhong,
                p.tenLoaiPhong,
                tangStr,
                p.soLuotThue + " lượt",
                df.format(p.tongDoanhThu),
                dfTyLe.format(tyTrong)
            });
        }
    }
}
