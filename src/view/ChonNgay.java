package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.event.EventListenerList;

public class ChonNgay extends JPanel {
    // Giới hạn các năm có thể chọn trong giao diện.
    private static final int NAM_BAT_DAU = 2026;
    private static final int NAM_KET_THUC = 3000;

    // Ba ô xổ xuống và trạng thái ngày hiện tại.
    private final JComboBox<Integer> hopChonNam = new JComboBox<>();
    private final JComboBox<Integer> hopChonThang = new JComboBox<>();
    private final JComboBox<Integer> hopChonNgay = new JComboBox<>();
    // Lưu các AC cần chạy khi ngày thay đổi.
    private final EventListenerList danhSachAcNgay = new EventListenerList();
    private LocalDate ngay;
    // Ngày tối thiểu, ví dụ ngày trả phòng phải sau ngày nhận phòng.
    private LocalDate ngayToiThieu;
    // Ngăn sự kiện phát sinh khi chương trình tự cập nhật lựa chọn.
    private boolean dangCapNhat;

    // Tạo ô chọn ngày với ngày hiện tại và không đặt giới hạn tối thiểu.
    public ChonNgay() {
        this(LocalDate.now(), null);
    }

    // Tạo các danh sách năm, tháng, ngày và chọn ngày ban đầu.
    public ChonNgay(LocalDate ngayBanDau, LocalDate ngayToiThieu) {
        super(new FlowLayout(FlowLayout.LEFT, 4, 0));
        if (ngayBanDau == null) {
            throw new IllegalArgumentException("Ngày không được để trống.");
        }
        this.ngayToiThieu = ngayToiThieu;

        hopChonNam.setToolTipText("Chọn năm");
        hopChonThang.setToolTipText("Chọn tháng");
        hopChonNgay.setToolTipText("Chọn ngày");
        hopChonNam.setPreferredSize(new Dimension(104, 32));
        hopChonThang.setPreferredSize(new Dimension(96, 32));
        hopChonNgay.setPreferredSize(new Dimension(96, 32));

        for (int nam = NAM_BAT_DAU; nam <= NAM_KET_THUC; nam++) {
            hopChonNam.addItem(nam);
        }
        for (int thang = 1; thang <= 12; thang++) {
            hopChonThang.addItem(thang);
        }
        for (int ngayTrongThang = 1; ngayTrongThang <= 31; ngayTrongThang++) {
            hopChonNgay.addItem(ngayTrongThang);
        }

        hopChonThang.setRenderer(new BoHienThiHaiChuSo());
        hopChonNgay.setRenderer(new BoHienThiHaiChuSo());
        add(hopChonNam);
        add(hopChonThang);
        add(hopChonNgay);
        setPreferredSize(new Dimension(324, 32));

        hopChonNam.addActionListener(ac -> capNhatTheoLuaChon());
        hopChonThang.addActionListener(ac -> capNhatTheoLuaChon());
        hopChonNgay.addActionListener(ac -> capNhatTheoLuaChon());
        capNhatLuaChon(ngayBanDau);
        ngay = ngayBanDau;
    }

    // Trả về ngày đang chọn.
    public LocalDate getDate() {
        return ngay;
    }

    // Đặt ngày từ chương trình và đồng bộ ba ô xổ xuống.
    public void setDate(LocalDate ngay) {
        if (ngay == null) {
            throw new IllegalArgumentException("Ngày không được để trống.");
        }
        this.ngay = ngay;
        capNhatLuaChon(ngay);
        thongBaoNgayThayDoi();
    }

    // Đặt giới hạn ngày sớm nhất được phép chọn.
    public void setMinimumDate(LocalDate ngayToiThieu) {
        this.ngayToiThieu = ngayToiThieu;
    }

    // Đăng ký AC sẽ chạy khi ngày được thay đổi.
    public void addDateChangeListener(ActionListener ac) {
        danhSachAcNgay.add(ActionListener.class, ac);
    }

    // Hủy đăng ký AC theo dõi ngày.
    public void removeDateChangeListener(ActionListener ac) {
        danhSachAcNgay.remove(ActionListener.class, ac);
    }

    // Đọc lựa chọn và kiểm tra ngày lịch có hợp lệ, có đúng giới hạn tối thiểu không.
    private void capNhatTheoLuaChon() {
        if (dangCapNhat
                || hopChonNam.getSelectedItem() == null
                || hopChonThang.getSelectedItem() == null
                || hopChonNgay.getSelectedItem() == null) {
            return;
        }

        try {
            LocalDate ngayMoi = LocalDate.of(
                    (Integer) hopChonNam.getSelectedItem(),
                    (Integer) hopChonThang.getSelectedItem(),
                    (Integer) hopChonNgay.getSelectedItem());
            if (ngayToiThieu != null && ngayMoi.isBefore(ngayToiThieu)) {
                throw new DateTimeException("Ngày phải từ " + ngayToiThieu + " trở đi.");
            }
            ngay = ngayMoi;
            thongBaoNgayThayDoi();
        } catch (DateTimeException loiNgay) {
            JOptionPane.showMessageDialog(
                    this, "Ngày đã chọn không hợp lệ.", "Ngày không hợp lệ", JOptionPane.WARNING_MESSAGE);
            capNhatLuaChon(ngay);
        }
    }

    // Chọn lại năm, tháng, ngày; cờ tránh kích hoạt xử lý như thao tác người dùng.
    private void capNhatLuaChon(LocalDate ngayCanChon) {
        dangCapNhat = true;
        try {
            hopChonNam.setSelectedItem(ngayCanChon.getYear());
            hopChonThang.setSelectedItem(ngayCanChon.getMonthValue());
            hopChonNgay.setSelectedItem(ngayCanChon.getDayOfMonth());
        } finally {
            dangCapNhat = false;
        }
    }

    // Chạy các AC đã đăng ký khi ngày đổi.
    private void thongBaoNgayThayDoi() {
        for (ActionListener ac : danhSachAcNgay.getListeners(ActionListener.class)) {
            ac.actionPerformed(new java.awt.event.ActionEvent(
                    this, java.awt.event.ActionEvent.ACTION_PERFORMED, "dateChanged"));
        }
    }

    // Hiển thị tháng và ngày có số 0 ở đầu, ví dụ 01 thay vì 1.
    private static class BoHienThiHaiChuSo extends DefaultListCellRenderer {
        @Override
        public java.awt.Component getListCellRendererComponent(
                JList<?> danhSach, Object giaTri, int chiSo,
                boolean dangChon, boolean oDangChon) {
            super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
            if (giaTri instanceof Integer) {
                setText(String.format("%02d", giaTri));
            }
            return this;
        }
    }
}
