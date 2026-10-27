
package service;

import java.util.ArrayList;
import dao.RoomDAO;
import model.rooms;

public class RoomService {

    RoomDAO dao = new RoomDAO();

    // Them phong
    public int them(rooms t) {
        if (t.getSoPhong() == null || t.getSoPhong().isEmpty()) {
            System.out.println("Vui long nhap so phong!");
            return 0;
        }

        if (t.getMaLoaiPhong() <= 0 || t.getGiaMoiDem() <= 0) {
            System.out.println("Loai phong hoac gia khong hop le!");
            return 0;
        }

        return dao.them(t);
    }

    // Sua phong
    public int sua(rooms t) {
        if (t.getMaPhong() <= 0) {
            System.out.println("Ma phong khong hop le!");
            return 0;
        }

        if (t.getSoPhong() == null || t.getSoPhong().isEmpty()
                || t.getMaLoaiPhong() <= 0 || t.getGiaMoiDem() <= 0) {
            System.out.println("Thong tin phong khong hop le!");
            return 0;
        }

        return dao.sua(t);
    }

    // Xoa phong
    public int xoa(rooms t) {
        if (t.getMaPhong() <= 0) {
            System.out.println("Ma phong khong hop le!");
            return 0;
        }

        return dao.xoa(t);
    }

    // Xuat danh sach phong
    public ArrayList<rooms> xuat() {
        return dao.xuat();
    }

    // Tim phong theo so phong
    public rooms timKiem(rooms t) {
        if (t.getSoPhong() == null || t.getSoPhong().isEmpty()) {
            return null;
        }

        return dao.timKiem(t);
    }
}
