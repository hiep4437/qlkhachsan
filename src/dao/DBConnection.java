
package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
<<<<<<< HEAD
import java.util.Properties;

public class DBConnection {
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
                    return null; // Thành công
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
            // Ignored
        }
    }

    public static String getHost() { return host; }
    public static String getPort() { return port; }
    public static String getDatabase() { return database; }
    public static String getUser() { return user; }
    public static String getPass() { return pass; }
=======
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "hotel_management";
    private static final String USER = "root";
    private static final String PASS = "";

    // Ket noi database
    public static Connection getConnection() {
        Connection c = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Ket noi MySQL/MariaDB
            Connection conn = DriverManager.getConnection(
                URL, USER, PASS
            );

            // Tao database neu chua ton tai
            Statement st = conn.createStatement();

            st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS " + DB_NAME
                + " CHARACTER SET utf8mb4"
                + " COLLATE utf8mb4_unicode_ci"
            );

            st.close();
            conn.close();

            // Ket noi vao database quan ly khach san
            String dbUrl = URL + DB_NAME
                + "?useUnicode=true&characterEncoding=UTF-8";

            c = DriverManager.getConnection(
                dbUrl, USER, PASS
            );

            // Tao bang va du lieu mau
            khoiTaoDatabase(c);

            System.out.println("Ket noi database thanh cong!");

        } catch (Exception e) {
            System.out.println("Loi ket noi database!");
            e.printStackTrace();
        }

        return c;
    }

    // Tao cac bang va du lieu mau
    private static void khoiTaoDatabase(Connection c)
            throws SQLException {

        Statement st = c.createStatement();

        // 1. Bang customers
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS customers ("
            + "customer_id INT NOT NULL AUTO_INCREMENT,"
            + "full_name VARCHAR(100) NOT NULL,"
            + "identity_no VARCHAR(20) DEFAULT NULL,"
            + "phone VARCHAR(20) DEFAULT NULL,"
            + "email VARCHAR(100) DEFAULT NULL,"
            + "address VARCHAR(255) DEFAULT NULL,"
            + "PRIMARY KEY (customer_id),"
            + "UNIQUE KEY identity_no (identity_no)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // 2. Bang room_types
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS room_types ("
            + "room_type_id INT NOT NULL AUTO_INCREMENT,"
            + "type_name VARCHAR(50) NOT NULL,"
            + "capacity INT NOT NULL,"
            + "description VARCHAR(255) DEFAULT NULL,"
            + "PRIMARY KEY (room_type_id),"
            + "UNIQUE KEY type_name (type_name)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // 3. Bang users
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS users ("
            + "user_id INT NOT NULL AUTO_INCREMENT,"
            + "username VARCHAR(50) NOT NULL,"
            + "password_hash VARCHAR(255) NOT NULL,"
            + "full_name VARCHAR(100) NOT NULL,"
            + "role ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',"
            + "is_active TINYINT(1) NOT NULL DEFAULT 1,"
            + "PRIMARY KEY (user_id),"
            + "UNIQUE KEY username (username)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // Them du lieu loai phong neu bang rong
        if (bangRong(c, "room_types")) {
            st.executeUpdate(
                "INSERT INTO room_types "
                + "(room_type_id, type_name, capacity, description) VALUES "
                + "(1, 'Phong don', 1, 'Phong cho mot nguoi'),"
                + "(2, 'Phong doi', 2, 'Phong cho hai nguoi'),"
                + "(3, 'Phong VIP', 4, 'Phong cao cap')"
            );
        }

        // Them tai khoan mau neu bang rong
        if (bangRong(c, "users")) {
            st.executeUpdate(
                "INSERT INTO users "
                + "(user_id, username, password_hash, full_name, role, is_active) VALUES "
                + "(1, 'admin', '123456', 'Quan tri vien', 'ADMIN', 1),"
                + "(2, 'nhanvien', '123456', 'Nguyen Van An', 'STAFF', 1),"
                + "(3, '1', '1', '1', 'ADMIN', 1)"
            );
        }

        // 4. Bang rooms
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS rooms ("
            + "room_id INT NOT NULL AUTO_INCREMENT,"
            + "room_number VARCHAR(10) NOT NULL,"
            + "room_type_id INT NOT NULL,"
            + "price_per_night DECIMAL(12,2) NOT NULL,"
            + "status ENUM('AVAILABLE','OCCUPIED','MAINTENANCE','CLEANING') "
            + "NOT NULL DEFAULT 'AVAILABLE',"
            + "description VARCHAR(255) DEFAULT NULL,"
            + "PRIMARY KEY (room_id),"
            + "UNIQUE KEY room_number (room_number),"
            + "KEY room_type_id (room_type_id),"
            + "CONSTRAINT rooms_ibfk_1 FOREIGN KEY (room_type_id) "
            + "REFERENCES room_types(room_type_id)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        if (bangRong(c, "rooms")) {
            st.executeUpdate(
                "INSERT INTO rooms "
                + "(room_id, room_number, room_type_id, price_per_night, status, description) VALUES "
                + "(1, '101', 1, 300000, 'AVAILABLE', 'Tang 1'),"
                + "(2, '102', 1, 300000, 'AVAILABLE', 'Tang 1'),"
                + "(3, '103', 1, 350000, 'AVAILABLE', 'Tang 1'),"
                + "(4, '201', 2, 500000, 'OCCUPIED', 'Tang 2'),"
                + "(5, '202', 2, 500000, 'CLEANING', 'Tang 2'),"
                + "(6, '203', 2, 550000, 'AVAILABLE', 'Tang 2'),"
                + "(7, '301', 3, 1000000, 'MAINTENANCE', 'Tang 3'),"
                + "(8, '302', 3, 1000000, 'AVAILABLE', 'Tang 3'),"
                + "(9, '401', 2, 123000, 'AVAILABLE', 'Tang 4')"
            );
        }

        // 5. Bang bookings
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS bookings ("
            + "booking_id INT NOT NULL AUTO_INCREMENT,"
            + "customer_id INT NOT NULL,"
            + "check_in_date DATE NOT NULL,"
            + "check_out_date DATE NOT NULL,"
            + "status ENUM('BOOKED','CHECKED_IN','CHECKED_OUT','CANCELLED') "
            + "NOT NULL DEFAULT 'BOOKED',"
            + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "note VARCHAR(255) DEFAULT NULL,"
            + "PRIMARY KEY (booking_id),"
            + "KEY customer_id (customer_id),"
            + "CONSTRAINT bookings_ibfk_1 FOREIGN KEY (customer_id) "
            + "REFERENCES customers(customer_id)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // 6. Bang booking_rooms
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS booking_rooms ("
            + "booking_id INT NOT NULL,"
            + "room_id INT NOT NULL,"
            + "price_per_night DECIMAL(12,2) NOT NULL,"
            + "PRIMARY KEY (booking_id, room_id),"
            + "KEY room_id (room_id),"
            + "CONSTRAINT booking_rooms_ibfk_1 FOREIGN KEY (booking_id) "
            + "REFERENCES bookings(booking_id),"
            + "CONSTRAINT booking_rooms_ibfk_2 FOREIGN KEY (room_id) "
            + "REFERENCES rooms(room_id)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // 7. Bang invoices
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS invoices ("
            + "invoice_id INT NOT NULL AUTO_INCREMENT,"
            + "booking_id INT NOT NULL,"
            + "issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "room_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,"
            + "surcharge DECIMAL(12,2) NOT NULL DEFAULT 0.00,"
            + "discount DECIMAL(12,2) NOT NULL DEFAULT 0.00,"
            + "total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,"
            + "payment_method VARCHAR(50) DEFAULT NULL,"
            + "payment_status ENUM('UNPAID','PAID','REFUNDED') "
            + "NOT NULL DEFAULT 'UNPAID',"
            + "PRIMARY KEY (invoice_id),"
            + "UNIQUE KEY booking_id (booking_id),"
            + "CONSTRAINT invoices_ibfk_1 FOREIGN KEY (booking_id) "
            + "REFERENCES bookings(booking_id)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 "
            + "COLLATE=utf8mb4_unicode_ci"
        );

        // Them du lieu mau khi cac bang rong
        if (bangRong(c, "customers")) {
            st.executeUpdate(
                "INSERT INTO customers "
                + "(customer_id, full_name, identity_no, phone, email, address) VALUES "
                + "(1, 'Nguyen Van An', '001234567890', '0901234567', 'an@gmail.com', 'Ha Noi'),"
                + "(2, 'Tran Thi Binh', '001234567891', '0912345678', 'binh@gmail.com', 'Hai Phong'),"
                + "(3, 'Le Van Cuong', '001234567892', '0923456789', 'cuong@gmail.com', 'Da Nang'),"
                + "(4, 'Pham Thi Dung', '001234567893', '0934567890', 'dung@gmail.com', 'Ho Chi Minh'),"
                + "(5, 'Hoang Van Em', '001234567894', '0945678901', 'em@gmail.com', 'Nam Dinh')"
            );
        }

        if (bangRong(c, "bookings")) {
            st.executeUpdate(
                "INSERT INTO bookings "
                + "(booking_id, customer_id, check_in_date, check_out_date, status, note) VALUES "
                + "(1, 1, '2026-10-26', '2026-10-28', 'BOOKED', 'Dat truoc'),"
                + "(2, 2, '2026-10-20', '2026-10-22', 'CHECKED_OUT', 'Da tra phong'),"
                + "(3, 3, '2026-10-25', '2026-10-27', 'CHECKED_IN', 'Dang o'),"
                + "(4, 4, '2026-10-29', '2026-10-31', 'BOOKED', 'Khach dat truoc')"
            );
        }

        if (bangRong(c, "booking_rooms")) {
            st.executeUpdate(
                "INSERT INTO booking_rooms "
                + "(booking_id, room_id, price_per_night) VALUES "
                + "(1, 1, 300000),"
                + "(2, 4, 500000),"
                + "(3, 2, 300000),"
                + "(4, 6, 550000)"
            );
        }

        if (bangRong(c, "invoices")) {
            st.executeUpdate(
                "INSERT INTO invoices "
                + "(invoice_id, booking_id, issued_at, room_amount, surcharge, discount, total_amount, payment_method, payment_status) VALUES "
                + "(1, 1, '2026-10-25 14:08:43', 600000, 0, 0, 600000, 'Tien mat', 'UNPAID'),"
                + "(2, 2, '2026-10-25 14:08:43', 1000000, 50000, 50000, 1000000, 'Chuyen khoan', 'PAID'),"
                + "(3, 3, '2026-10-25 14:08:43', 600000, 0, 0, 600000, 'Tien mat', 'UNPAID'),"
                + "(4, 4, '2026-10-25 14:08:43', 1100000, 0, 0, 1100000, 'The', 'UNPAID')"
            );
        }

        st.close();
    }

    // Kiem tra bang co du lieu hay chua
    private static boolean bangRong(Connection c, String tenBang)
            throws SQLException {

        String sql = "SELECT 1 FROM `" + tenBang + "` LIMIT 1";

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            return !rs.next();
        }
    }

    // Dong ket noi
    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
>>>>>>> 5413adf06544833d3f0a6dcc4f1cb780cf3cf1fc
}
