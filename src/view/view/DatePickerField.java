package view.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DatePickerField extends JPanel {
    private static final DateTimeFormatter NGAY_DINHDANG = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField truongNgay = new JTextField();
    private final JButton nutLich = new JButton("Lịch");
    private final javax.swing.event.EventListenerList danhSachBoNgheNgay =
            new javax.swing.event.EventListenerList();
    private LocalDate ngay;
    private LocalDate ngayToiThieu;

    public DatePickerField(LocalDate ngayBanDau, LocalDate ngayToiThieu) {
        super(new BorderLayout(5, 0));
        this.ngayToiThieu = ngayToiThieu;
        this.ngay = ngayBanDau;

        truongNgay.setFont(UIStyle.PHONGCHU_THUONG);
        truongNgay.setEditable(false);
        truongNgay.setFocusable(false);
        truongNgay.setHorizontalAlignment(SwingConstants.CENTER);
        truongNgay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.MAU_VIEN),
                new EmptyBorder(6, 8, 6, 8)));

        nutLich.setFont(UIStyle.PHONGCHU_SMALL_INDAM);
        nutLich.setForeground(new Color(30, 41, 59));
        nutLich.setBackground(Color.WHITE);
        nutLich.setFocusPainted(false);
        nutLich.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nutLich.setPreferredSize(new Dimension(64, 32));
        nutLich.addActionListener(suKien -> DatePickerCalendar.show(
                nutLich, ngay, ngayToiThieu, this::setDate));

        add(truongNgay, BorderLayout.CENTER);
        add(nutLich, BorderLayout.EAST);
        setPreferredSize(new Dimension(200, 32));
        setDate(ngayBanDau);
    }

    public LocalDate getDate() {
        return ngay;
    }

    public void setDate(LocalDate ngay) {
        if (ngay == null) {
            throw new IllegalArgumentException("Ngày không được để trống.");
        }
        this.ngay = ngay;
        truongNgay.setText(ngay.format(NGAY_DINHDANG));
        for (ActionListener boNghe : danhSachBoNgheNgay.getListeners(ActionListener.class)) {
            boNghe.actionPerformed(new java.awt.event.ActionEvent(
                    this, java.awt.event.ActionEvent.ACTION_PERFORMED, "dateChanged"));
        }
    }

    public void setMinimumDate(LocalDate ngayToiThieu) {
        this.ngayToiThieu = ngayToiThieu;
    }

    public void addDateChangeListener(ActionListener boNghe) {
        danhSachBoNgheNgay.add(ActionListener.class, boNghe);
    }

    public void removeDateChangeListener(ActionListener boNghe) {
        danhSachBoNgheNgay.remove(ActionListener.class, boNghe);
    }
}
