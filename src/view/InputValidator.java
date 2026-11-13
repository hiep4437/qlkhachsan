package view;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public final class InputValidator {

    private InputValidator() {
    }

    // ============================
    //  JTextField validators
    // ============================

    /**
     * Chỉ cho phép nhập chữ số (0-9). Ngăn chặn hoàn toàn việc nhập chữ cái hoặc ký tự đặc biệt.
     */
    public static void chiNhapSo(JTextField textField) {
        if (textField == null) return;
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) return;
                if (text.isEmpty() || text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Chỉ cho phép nhập chữ số (0-9) và giới hạn độ dài tối đa.
     */
    public static void chiNhapSoVaGioiHan(JTextField textField, int doDaiToiDa) {
        if (textField == null) return;
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                int currentLength = fb.getDocument().getLength();
                if (string.matches("\\d+") && (currentLength + string.length() <= doDaiToiDa)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) return;
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + text.length();
                if ((text.isEmpty() || text.matches("\\d+")) && newLength <= doDaiToiDa) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Dành riêng cho các ô ghi giá tiền: chỉ cho phép nhập chữ số (0-9).
     */
    public static void chiNhapGiaTien(JTextField textField) {
        chiNhapSo(textField);
    }

    // ============================
    //  JSpinner validators
    // ============================

    /**
     * Chỉ cho phép nhập số vào spinner số nguyên (ô số người, số lượng...).
     * Lấy JFormattedTextField bên trong editor của JSpinner và áp dụng filter.
     */
    public static void chiNhapSoChoSpinner(JSpinner spinner) {
        if (spinner == null) return;
        JFormattedTextField tf = getSpinnerTextField(spinner);
        if (tf == null) return;
        ((AbstractDocument) tf.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                // Cho phép dấu trừ ở đầu (số âm) và chữ số
                if (string.matches("[\\d-]+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) return;
                if (text.isEmpty() || text.matches("[\\d-]+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Chỉ cho phép nhập chữ số và ký tự đặc biệt dùng trong ngày tháng (-, /, khoảng trắng, :)
     * vào ô nhập ngày (JSpinner với DateEditor).
     */
    public static void chiNhapNgayChoSpinner(JSpinner spinner) {
        if (spinner == null) return;
        JFormattedTextField tf = getSpinnerTextField(spinner);
        if (tf == null) return;
        ((AbstractDocument) tf.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                // Cho phép: chữ số, dấu -, /, khoảng trắng, dấu :
                if (string.matches("[\\d\\-/: ]+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) return;
                if (text.isEmpty() || text.matches("[\\d\\-/: ]+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Lấy JFormattedTextField nằm bên trong editor của JSpinner.
     */
    private static JFormattedTextField getSpinnerTextField(JSpinner spinner) {
        if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        }
        return null;
    }
}

