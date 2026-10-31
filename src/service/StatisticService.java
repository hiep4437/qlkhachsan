package service;

import java.util.ArrayList;
import dao.StatisticDAO;

public class StatisticService {

    private StatisticDAO dao = new StatisticDAO();

    public double getTongDoanhThu() {
        return dao.getTongDoanhThu();
    }

    public double getDoanhThuHomNay() {
        return dao.getDoanhThuHomNay();
    }

    public double getDoanhThuThangNay() {
        return dao.getDoanhThuThangNay();
    }

    public int getTongSoLuotDat() {
        return dao.getTongSoLuotDat();
    }

    public ArrayList<StatisticDAO.ThongKePhong> thongKeTheoPhong(String kyLoc) {
        return dao.thongKeTheoPhong(kyLoc);
    }

    public ArrayList<StatisticDAO.ChiTietHoaDonBaoCao> layHoaDonTheoKy(String kyLoc) {
        return dao.layHoaDonTheoKy(kyLoc);
    }
}
