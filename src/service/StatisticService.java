package service;

import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Date;
import dao.ThongKeDAO;

public class StatisticService {

    private ThongKeDAO truyCapDuLieu = new ThongKeDAO();

    public ArrayList<ThongKeDAO.ThongKePhong> thongKeTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        return truyCapDuLieu.thongKeTheoKhoang(tuNgay, denNgay);
    }

    public ArrayList<ThongKeDAO.ChiTietHoaDonBaoCao> layHoaDonTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        return truyCapDuLieu.layHoaDonTheoKhoang(tuNgay, denNgay);
    }
}
