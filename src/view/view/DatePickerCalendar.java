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
    private static final Color COLOR_TEXT = new Color(30, 41, 59);
    private static final Color COLOR_MUTED = new Color(100, 116, 139);

    private DatePickerCalendar() {
    }

    public static void show(Component parent, LocalDate selectedDate, LocalDate minimumDate,
            Consumer<LocalDate> onDateSelected) {
        JDialog calendar = new JDialog(
                SwingUtilities.getWindowAncestor(parent), "Chọn ngày", Dialog.ModalityType.APPLICATION_MODAL);
        calendar.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(8, 10));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel monthHeader = new JPanel(new BorderLayout(8, 0));
        monthHeader.setOpaque(false);
        YearMonth[] displayedMonth = { YearMonth.from(selectedDate) };
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(UIStyle.FONT_BOLD);
        monthLabel.setForeground(COLOR_TEXT);

        JButton previousMonth = new JButton("<");
        JButton nextMonth = new JButton(">");
        previousMonth.setFocusable(false);
        nextMonth.setFocusable(false);
        monthHeader.add(previousMonth, BorderLayout.WEST);
        monthHeader.add(monthLabel, BorderLayout.CENTER);
        monthHeader.add(nextMonth, BorderLayout.EAST);

        JPanel daysGrid = new JPanel(new GridLayout(0, 7, 4, 4));
        daysGrid.setOpaque(false);

        Runnable[] updateCalendar = new Runnable[1];
        updateCalendar[0] = () -> {
            monthLabel.setText(displayedMonth[0].getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("vi"))
                    + " " + displayedMonth[0].getYear());
            previousMonth.setEnabled(minimumDate == null
                    || displayedMonth[0].isAfter(YearMonth.from(minimumDate)));
            daysGrid.removeAll();

            String[] weekDays = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
            for (String weekDay : weekDays) {
                JLabel dayHeading = new JLabel(weekDay, SwingConstants.CENTER);
                dayHeading.setFont(UIStyle.FONT_SMALL_BOLD);
                dayHeading.setForeground(COLOR_MUTED);
                daysGrid.add(dayHeading);
            }

            LocalDate firstDay = displayedMonth[0].atDay(1);
            int leadingDays = firstDay.getDayOfWeek().getValue() - 1;
            for (int i = 0; i < leadingDays; i++) {
                daysGrid.add(new JLabel());
            }

            for (int day = 1; day <= displayedMonth[0].lengthOfMonth(); day++) {
                LocalDate date = displayedMonth[0].atDay(day);
                JButton dayButton = new JButton(Integer.toString(day));
                dayButton.setFocusable(false);
                dayButton.setFont(UIStyle.FONT_REGULAR);
                dayButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
                dayButton.setEnabled(minimumDate == null || !date.isBefore(minimumDate));
                if (date.equals(selectedDate)) {
                    dayButton.setBackground(UIStyle.COLOR_PRIMARY);
                    dayButton.setForeground(Color.WHITE);
                    dayButton.setOpaque(true);
                } else {
                    dayButton.setBackground(Color.WHITE);
                    dayButton.setForeground(COLOR_TEXT);
                }
                dayButton.addActionListener(event -> {
                    onDateSelected.accept(date);
                    calendar.dispose();
                });
                daysGrid.add(dayButton);
            }
            daysGrid.revalidate();
            daysGrid.repaint();
            calendar.pack();
        };

        previousMonth.addActionListener(event -> {
            displayedMonth[0] = displayedMonth[0].minusMonths(1);
            updateCalendar[0].run();
        });
        nextMonth.addActionListener(event -> {
            displayedMonth[0] = displayedMonth[0].plusMonths(1);
            updateCalendar[0].run();
        });

        content.add(monthHeader, BorderLayout.NORTH);
        content.add(daysGrid, BorderLayout.CENTER);
        calendar.setContentPane(content);
        updateCalendar[0].run();
        calendar.setResizable(false);
        calendar.setLocationRelativeTo(parent);
        calendar.setVisible(true);
    }
}
