package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class KetNoiCSDL {
    private static final String CAUHINH_TEP = "db.properties";

    private static String mayChu = "localhost";
    private static String cong = "3306";
    private static String coSoDuLieu = "hotel_management";
    private static String nguoiDung = "root";
    private static String matKhau = "";

    static {
        loadConfig();
    }

    public static void loadConfig() {
        File tieuDiem = new File(CAUHINH_TEP);
        if (tieuDiem.exists()) {
            try (InputStream dangLa = new FileInputStream(tieuDiem)) {
                Properties thuocTinh = new Properties();
                thuocTinh.load(dangLa);
                mayChu = thuocTinh.getProperty("host", "localhost");
                cong = thuocTinh.getProperty("port", "3306");
                coSoDuLieu = thuocTinh.getProperty("database", "hotel_management");
                nguoiDung = thuocTinh.getProperty("user", "root");
                matKhau = thuocTinh.getProperty("pass", "");
            } catch (Exception suKien) {
                System.err.println("Không thể đọc file " + CAUHINH_TEP + ", sử dụng mặc định: " + suKien.getMessage());
            }
        }
    }

    public static boolean saveConfig(String mayChuMoi, String congMoi, String coSoDuLieuMoi, String nguoiDungMoi, String matKhauMoi) {
        try {
            mayChu = mayChuMoi;
            cong = congMoi;
            coSoDuLieu = coSoDuLieuMoi;
            nguoiDung = nguoiDungMoi;
            matKhau = matKhauMoi;

            Properties thuocTinh = new Properties();
            thuocTinh.setProperty("host", mayChu);
            thuocTinh.setProperty("port", cong);
            thuocTinh.setProperty("database", coSoDuLieu);
            thuocTinh.setProperty("user", nguoiDung);
            thuocTinh.setProperty("pass", matKhau);

            try (OutputStream heDieuHanh = new FileOutputStream(CAUHINH_TEP)) {
                thuocTinh.store(heDieuHanh, "Hotel Management Database Connection Configuration");
            }
            return true;
        } catch (Exception suKien) {
            suKien.printStackTrace();
            return false;
        }
    }

    public static String testConnection(String bangHienTaiMayChu, String bangHienTaiCong, String bangHienTaiCoSoDuLieu, String bangHienTaiNguoiDung, String bangHienTaiMatKhau) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String duongDan = "jdbc:mysql://" + bangHienTaiMayChu.trim() + ":" + bangHienTaiCong.trim() + "/" + bangHienTaiCoSoDuLieu.trim()
                    + "?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            try (Connection ketNoi = DriverManager.getConnection(duongDan, bangHienTaiNguoiDung, bangHienTaiMatKhau)) {
                if (ketNoi != null && !ketNoi.isClosed()) {
                    return null; // thành công
                }
            }
            return "Không thể mở kết nối tới CSDL.";
        } catch (Exception suKien) {
            return suKien.getMessage();
        }
    }

    public static Connection getConnection() {
        Connection thanhPhan = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String duongDan = "jdbc:mysql://" + mayChu.trim() + ":" + cong.trim() + "/" + coSoDuLieu.trim()
                    + "?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            thanhPhan = DriverManager.getConnection(duongDan, nguoiDung, matKhau);
        } catch (Exception suKien) {
            System.err.println("Lỗi kết nối CSDL (" + mayChu + ":" + cong + "/" + coSoDuLieu + "): " + suKien.getMessage());
        }
        return thanhPhan;
    }

    public static void closeConnection(Connection thanhPhan) {
        try {
            if (thanhPhan != null && !thanhPhan.isClosed()) {
                thanhPhan.close();
            }
        } catch (Exception suKien) {
            // không biết viết gì =))
        }
    }

    public static String getHost() {
        return mayChu;
    }

    public static String getPort() {
        return cong;
    }

    public static String getDatabase() {
        return coSoDuLieu;
    }

    public static String getUser() {
        return nguoiDung;
    }

    public static String getPass() {
        return matKhau;
    }
}
