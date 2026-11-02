package service;

import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Date;
import dao.ThongKeDAO;

public class StatisticService {

    private ThongKeDAO dao = new ThongKeDAO();

    public ArrayList<ThongKeDAO.ThongKePhong> thongKeTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        return dao.thongKeTheoKhoang(tuNgay, denNgay);
    }

    public ArrayList<ThongKeDAO.ChiTietHoaDonBaoCao> layHoaDonTheoKhoang(Date tuNgay, Date denNgay) throws SQLException {
        return dao.layHoaDonTheoKhoang(tuNgay, denNgay);
    }
}
