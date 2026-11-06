package view.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public final class DatePickerCalendar {
    private static final Color MAU_VANBAN = new Color(30, 41, 59);
    private static final Color MAU_MUTED = new Color(100, 116, 139);

    private DatePickerCalendar() {
    }

    public static void show(Component doiTuongCha, LocalDate ngayDaChon, LocalDate ngayToiThieu,
            Consumer<LocalDate> khiNgayDuocChon) {
        JDialog lich = new JDialog(
                SwingUtilities.getWindowAncestor(doiTuongCha), "Chọn ngày", Dialog.ModalityType.APPLICATION_MODAL);
        lich.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel noiDung = new JPanel(new BorderLayout(8, 10));
        noiDung.setBackground(Color.WHITE);
        noiDung.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel tieuDeThang = new JPanel(new BorderLayout(8, 0));
        tieuDeThang.setOpaque(false);
        YearMonth[] thangDangHien = { YearMonth.from(ngayDaChon) };
        JLabel nhanThang = new JLabel("", SwingConstants.CENTER);
        nhanThang.setFont(UIStyle.PHONGCHU_IN_DAM);
        nhanThang.setForeground(MAU_VANBAN);

        JButton truocThang = new JButton("<");
        JButton thangTiepTheo = new JButton(">");
        truocThang.setFocusable(false);
        thangTiepTheo.setFocusable(false);
        tieuDeThang.add(truocThang, BorderLayout.WEST);
        tieuDeThang.add(nhanThang, BorderLayout.CENTER);
        tieuDeThang.add(thangTiepTheo, BorderLayout.EAST);

        JPanel luoiNgay = new JPanel(new GridLayout(0, 7, 4, 4));
        luoiNgay.setOpaque(false);

        Runnable[] capNhatLich = new Runnable[1];
        capNhatLich[0] = () -> {
            nhanThang.setText(thangDangHien[0].getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("vi"))
                    + " " + thangDangHien[0].getYear());
            truocThang.setEnabled(ngayToiThieu == null
                    || thangDangHien[0].isAfter(YearMonth.from(ngayToiThieu)));
            luoiNgay.removeAll();

            String[] cacNgayTrongTuan = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
            for (String ngayTrongTuan : cacNgayTrongTuan) {
                JLabel tieuDeNgay = new JLabel(ngayTrongTuan, SwingConstants.CENTER);
                tieuDeNgay.setFont(UIStyle.PHONGCHU_SMALL_INDAM);
                tieuDeNgay.setForeground(MAU_MUTED);
                luoiNgay.add(tieuDeNgay);
            }

            LocalDate ngayDau = thangDangHien[0].atDay(1);
            int soNgayTruocThang = ngayDau.getDayOfWeek().getValue() - 1;
            for (int chiSo = 0; chiSo < soNgayTruocThang; chiSo++) {
                luoiNgay.add(new JLabel());
            }

            for (int ngayTrongThang = 1; ngayTrongThang <= thangDangHien[0].lengthOfMonth(); ngayTrongThang++) {
                LocalDate ngay = thangDangHien[0].atDay(ngayTrongThang);
                JButton nutNgay = new JButton(Integer.toString(ngayTrongThang));
                nutNgay.setFocusable(false);
                nutNgay.setFont(UIStyle.PHONGCHU_THUONG);
                nutNgay.setMargin(new java.awt.Insets(4, 4, 4, 4));
                nutNgay.setEnabled(ngayToiThieu == null || !ngay.isBefore(ngayToiThieu));
                if (ngay.equals(ngayDaChon)) {
                    nutNgay.setBackground(UIStyle.MAU_CHINH);
                    nutNgay.setForeground(Color.WHITE);
                    nutNgay.setOpaque(true);
                } else {
                    nutNgay.setBackground(Color.WHITE);
                    nutNgay.setForeground(MAU_VANBAN);
                }
                nutNgay.addActionListener(suKien -> {
                    khiNgayDuocChon.accept(ngay);
                    lich.dispose();
                });
                luoiNgay.add(nutNgay);
            }
            luoiNgay.revalidate();
            luoiNgay.repaint();
            lich.pack();
        };

        truocThang.addActionListener(suKien -> {
            thangDangHien[0] = thangDangHien[0].minusMonths(1);
            capNhatLich[0].run();
        });
        thangTiepTheo.addActionListener(suKien -> {
            thangDangHien[0] = thangDangHien[0].plusMonths(1);
            capNhatLich[0].run();
        });

        noiDung.add(tieuDeThang, BorderLayout.NORTH);
        noiDung.add(luoiNgay, BorderLayout.CENTER);
        lich.setContentPane(noiDung);
        capNhatLich[0].run();
        lich.setResizable(false);
        lich.setLocationRelativeTo(doiTuongCha);
        lich.setVisible(true);
    }
}
