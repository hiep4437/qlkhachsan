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
    private static final String CONFIG_FILE = "db.properties";

    private static String host = "localhost";
    private static String port = "3306";
    private static String database = "hotel_management";
    private static String user = "root";
    private static String pass = "";

    static {
        loadConfig();
    }

    public static void loadConfig() {
        File f = new File(CONFIG_FILE);
        if (f.exists()) {
            try (InputStream is = new FileInputStream(f)) {
                Properties props = new Properties();
                props.load(is);
                host = props.getProperty("host", "localhost");
                port = props.getProperty("port", "3306");
                database = props.getProperty("database", "hotel_management");
                user = props.getProperty("user", "root");
                pass = props.getProperty("pass", "");
            } catch (Exception e) {
                System.err.println("Không thể đọc file " + CONFIG_FILE + ", sử dụng mặc định: " + e.getMessage());
            }
        }
    }

    public static boolean saveConfig(String newHost, String newPort, String newDb, String newUser, String newPass) {
        try {
            host = newHost;
            port = newPort;
            database = newDb;
            user = newUser;
            pass = newPass;

            Properties props = new Properties();
            props.setProperty("host", host);
            props.setProperty("port", port);
            props.setProperty("database", database);
            props.setProperty("user", user);
            props.setProperty("pass", pass);

            try (OutputStream os = new FileOutputStream(CONFIG_FILE)) {
                props.store(os, "Hotel Management Database Connection Configuration");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String testConnection(String tHost, String tPort, String tDb, String tUser, String tPass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + tHost.trim() + ":" + tPort.trim() + "/" + tDb.trim()
                    + "?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            try (Connection conn = DriverManager.getConnection(url, tUser, tPass)) {
                if (conn != null && !conn.isClosed()) {
                    return null; // thành công
                }
            }
            return "Không thể mở kết nối tới CSDL.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host.trim() + ":" + port.trim() + "/" + database.trim()
                    + "?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            c = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.err.println("Lỗi kết nối CSDL (" + host + ":" + port + "/" + database + "): " + e.getMessage());
        }
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            // không biết viết gì =))
        }
    }

    public static String getHost() {
        return host;
    }

    public static String getPort() {
        return port;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}
