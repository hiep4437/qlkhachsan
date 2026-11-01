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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField dateField = new JTextField();
    private final JButton calendarButton = new JButton("Lịch");
    private final javax.swing.event.EventListenerList dateChangeListeners =
            new javax.swing.event.EventListenerList();
    private LocalDate date;
    private LocalDate minimumDate;

    public DatePickerField(LocalDate initialDate, LocalDate minimumDate) {
        super(new BorderLayout(5, 0));
        this.minimumDate = minimumDate;
        this.date = initialDate;

        dateField.setFont(UIStyle.FONT_REGULAR);
        dateField.setEditable(false);
        dateField.setFocusable(false);
        dateField.setHorizontalAlignment(SwingConstants.CENTER);
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER),
                new EmptyBorder(6, 8, 6, 8)));

        calendarButton.setFont(UIStyle.FONT_SMALL_BOLD);
        calendarButton.setForeground(new Color(30, 41, 59));
        calendarButton.setBackground(Color.WHITE);
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        calendarButton.setPreferredSize(new Dimension(64, 32));
        calendarButton.addActionListener(event -> DatePickerCalendar.show(
                calendarButton, date, minimumDate, this::setDate));

        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
        setPreferredSize(new Dimension(200, 32));
        setDate(initialDate);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Ngày không được để trống.");
        }
        this.date = date;
        dateField.setText(date.format(DATE_FORMATTER));
        for (ActionListener listener : dateChangeListeners.getListeners(ActionListener.class)) {
            listener.actionPerformed(new java.awt.event.ActionEvent(
                    this, java.awt.event.ActionEvent.ACTION_PERFORMED, "dateChanged"));
        }
    }

    public void setMinimumDate(LocalDate minimumDate) {
        this.minimumDate = minimumDate;
    }

    public void addDateChangeListener(ActionListener listener) {
        dateChangeListeners.add(ActionListener.class, listener);
    }

    public void removeDateChangeListener(ActionListener listener) {
        dateChangeListeners.remove(ActionListener.class, listener);
    }
}
