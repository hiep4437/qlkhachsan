package view;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.CustomerDAO;
import dao.RoomDAO;
import model.bookings;
import model.customers;
import model.rooms;

public class DialogThemSuaDatPhong extends JDialog {

    private JTextField txtCCCD;
    private JTextField txtHoTen;
    private JTextField txtSDT;
    private JButton btnKiemTraCCCD;

    private JComboBox<rooms> cboPhong;
    private JTextField txtNgayNhan;
    private JTextField txtNgayTra;
    private JSpinner spnSoNguoi;
    private JComboBox<String> cboTrangThai;
    private JTextField txtGhiChu;

    private JButton btnLuu;
    private JButton btnHuy;

    private boolean saved = false;
    private bookings booking;
    private CustomerDAO customerDAO = new CustomerDAO();
    private RoomDAO roomDAO = new RoomDAO();

    private int selectedCustomerId = 0;
    private int selectedRoomId = 0;
    private float selectedRoomPrice = 0;

    public DialogThemSuaDatPhong(Window parent, bookings bookingToEdit) {
        super(parent, bookingToEdit == null ? "Đặt phòng mới" : "Chỉnh sửa đơn đặt phòng #" + bookingToEdit.getIdDatPhong(),
                ModalityType.APPLICATION_MODAL);

        this.booking = bookingToEdit != null ? bookingToEdit : new bookings();
        boolean isEdit = (bookingToEdit != null && bookingToEdit.getIdDatPhong() > 0);

        setSize(580, 640);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 60, 114));
        pHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(isEdit ? "CHỈNH SỬA ĐƠN ĐẶT PHÒNG #" + this.booking.getIdDatPhong() : "TẠO ĐƠN ĐẶT PHÒNG MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(isEdit ? "Cập nhật ngày nhận/trả, số người, trạng thái hoặc phòng"
                : "Nhập thông tin khách hàng, chọn phòng trống và ngày lưu trú");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(220, 230, 242));

        pHeader.add(lblTitle, BorderLayout.NORTH);
        pHeader.add(lblSub, BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        // Body Form
        JPanel pBody = new JPanel(new GridBagLayout());
        pBody.setBorder(new EmptyBorder(15, 20, 15, 20));
        pBody.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. CCCD Khách hàng
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel lblCCCD = new JLabel("Số CCCD (*):");
        lblCCCD.setFont(fontLabel);
        pBody.add(lblCCCD, g);

        g.gridx = 1; g.weightx = 1;
        JPanel pCCCD = new JPanel(new BorderLayout(5, 0));
        pCCCD.setOpaque(false);
        txtCCCD = new JTextField();
        txtCCCD.setFont(fontInput);
        txtCCCD.setPreferredSize(new Dimension(160, 32));
        btnKiemTraCCCD = new JButton("Tìm");
        btnKiemTraCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnKiemTraCCCD.setFocusPainted(false);
        pCCCD.add(txtCCCD, BorderLayout.CENTER);
        pCCCD.add(btnKiemTraCCCD, BorderLayout.EAST);
        pBody.add(pCCCD, g);

        // 2. Tên khách hàng & SDT
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel lblHoTen = new JLabel("Họ tên khách (*):");
        lblHoTen.setFont(fontLabel);
        pBody.add(lblHoTen, g);

        g.gridx = 1; g.weightx = 1;
        txtHoTen = new JTextField();
        txtHoTen.setFont(fontInput);
        txtHoTen.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtHoTen, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        JLabel lblSDT = new JLabel("Số điện thoại (*):");
        lblSDT.setFont(fontLabel);
        pBody.add(lblSDT, g);

        g.gridx = 1; g.weightx = 1;
        txtSDT = new JTextField();
        txtSDT.setFont(fontInput);
        txtSDT.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtSDT, g);

        // 3. Chọn phòng
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel lblPhong = new JLabel("Chọn phòng (*):");
        lblPhong.setFont(fontLabel);
        pBody.add(lblPhong, g);

        g.gridx = 1; g.weightx = 1;
        cboPhong = new JComboBox<>();
        cboPhong.setFont(fontInput);
        cboPhong.setPreferredSize(new Dimension(200, 32));

        // Load danh sách phòng
        ArrayList<rooms> allRooms = roomDAO.xuat();
        for (rooms r : allRooms) {
            boolean isAvailable = "AVAILABLE".equalsIgnoreCase(r.getTrangThai()) || "TRỐNG".equalsIgnoreCase(r.getTrangThai());
            if (isAvailable || (isEdit && r.getSoPhong().equals(this.booking.getSoPhong()))) {
                cboPhong.addItem(r);
            }
        }
        cboPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof rooms) {
                    rooms rm = (rooms) value;
                    setText("Phòng " + rm.getSoPhong() + " - " + rm.getTenLoaiPhong()
                            + " (Tầng " + rm.getTang() + ", tối đa " + rm.getSucChua() + " khách)");
                }
                return this;
            }
        });
        pBody.add(cboPhong, g);

        // 4. Ngày nhận & Ngày trả (yyyy-MM-dd)
        LocalDate homNay = LocalDate.now();
        LocalDate ngayMai = homNay.plusDays(1);

        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel lblNhan = new JLabel("Ngày nhận (YYYY-MM-DD):");
        lblNhan.setFont(fontLabel);
        pBody.add(lblNhan, g);

        g.gridx = 1; g.weightx = 1;
        txtNgayNhan = new JTextField(homNay.toString());
        txtNgayNhan.setFont(fontInput);
        txtNgayNhan.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtNgayNhan, g);

        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        JLabel lblTra = new JLabel("Ngày trả (YYYY-MM-DD):");
        lblTra.setFont(fontLabel);
        pBody.add(lblTra, g);

        g.gridx = 1; g.weightx = 1;
        txtNgayTra = new JTextField(ngayMai.toString());
        txtNgayTra.setFont(fontInput);
        txtNgayTra.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtNgayTra, g);

        // 5. Số lượng người
        g.gridx = 0; g.gridy = 6; g.weightx = 0;
        JLabel lblSoNguoi = new JLabel("Số người ở:");
        lblSoNguoi.setFont(fontLabel);
        pBody.add(lblSoNguoi, g);

        g.gridx = 1; g.weightx = 1;
        rooms initialRoom = (rooms) cboPhong.getSelectedItem();
        int initialCapacity = initialRoom != null ? Math.max(1, initialRoom.getSucChua()) : 1;
        spnSoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, initialCapacity, 1));
        spnSoNguoi.setFont(fontInput);
        spnSoNguoi.setPreferredSize(new Dimension(200, 32));
        pBody.add(spnSoNguoi, g);
        cboPhong.addActionListener(e -> capNhatSucChuaPhong());

        // 6. Trạng thái
        g.gridx = 0; g.gridy = 7; g.weightx = 0;
        JLabel lblTT = new JLabel("Trạng thái:");
        lblTT.setFont(fontLabel);
        pBody.add(lblTT, g);

        g.gridx = 1; g.weightx = 1;
        cboTrangThai = new JComboBox<>(new String[] { "BOOKED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED" });
        cboTrangThai.setFont(fontInput);
        cboTrangThai.setPreferredSize(new Dimension(200, 32));
        cboTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String s = String.valueOf(value);
                if ("BOOKED".equalsIgnoreCase(s)) setText("Đã đặt (BOOKED)");
                else if ("CHECKED_IN".equalsIgnoreCase(s)) setText("Đã nhận phòng (CHECKED_IN)");
                else if ("CHECKED_OUT".equalsIgnoreCase(s)) setText("Đã trả phòng (CHECKED_OUT)");
                else if ("CANCELLED".equalsIgnoreCase(s)) setText("Đã hủy (CANCELLED)");
                return this;
            }
        });
        pBody.add(cboTrangThai, g);

        // 7. Ghi chú
        g.gridx = 0; g.gridy = 8; g.weightx = 0;
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(fontLabel);
        pBody.add(lblGhiChu, g);

        g.gridx = 1; g.weightx = 1;
        txtGhiChu = new JTextField();
        txtGhiChu.setFont(fontInput);
        txtGhiChu.setPreferredSize(new Dimension(200, 32));
        pBody.add(txtGhiChu, g);

        add(pBody, BorderLayout.CENTER);

        // Xử lý tìm kiếm CCCD
        btnKiemTraCCCD.addActionListener(e -> {
            String cccd = txtCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD để tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            customers tk = new customers();
            tk.setSoCCCD(cccd);
            customers found = customerDAO.timKiem(tk);
            if (found != null) {
                selectedCustomerId = found.getIdKhachHang();
                txtHoTen.setText(found.getNameKhachHang());
                txtSDT.setText(found.getSoDienThoai());
                JOptionPane.showMessageDialog(this, "Tìm thấy khách hàng: " + found.getNameKhachHang(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Chưa có thông tin khách hàng này. Vui lòng nhập họ tên & SĐT để hệ thống tự động lưu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtHoTen.requestFocus();
            }
        });

        // Điền dữ liệu nếu là sửa
        if (isEdit) {
            txtCCCD.setText(this.booking.getCccd() != null ? this.booking.getCccd() : "");
            if (this.booking.getNgayNhanPhong() != null) txtNgayNhan.setText(this.booking.getNgayNhanPhong().toString());
            if (this.booking.getNgayTraPhong() != null) txtNgayTra.setText(this.booking.getNgayTraPhong().toString());
            txtGhiChu.setText(this.booking.getGhiChu() != null ? this.booking.getGhiChu() : "");

            String st = this.booking.getTrangThai();
            if (st != null) {
                if (st.contains("CHECKED_IN") || st.contains("Đã nhận")) cboTrangThai.setSelectedItem("CHECKED_IN");
                else if (st.contains("CHECKED_OUT") || st.contains("Đã trả")) cboTrangThai.setSelectedItem("CHECKED_OUT");
                else if (st.contains("CANCELLED") || st.contains("Đã hủy")) cboTrangThai.setSelectedItem("CANCELLED");
                else cboTrangThai.setSelectedItem("BOOKED");
            }

            // Chọn phòng
            if (this.booking.getSoPhong() != null) {
                for (int i = 0; i < cboPhong.getItemCount(); i++) {
                    rooms r = cboPhong.getItemAt(i);
                    if (r.getSoPhong().equals(this.booking.getSoPhong())) {
                        cboPhong.setSelectedIndex(i);
                        break;
                    }
                }
            }
            capNhatSucChuaPhong();
            int capacity = phongDangChon() != null ? Math.max(1, phongDangChon().getSucChua()) : 1;
            spnSoNguoi.setValue(Math.min(Math.max(1, this.booking.getSoNguoi()), capacity));

            // Tìm thông tin khách hàng
            if (this.booking.getIdKhachHang() > 0) {
                selectedCustomerId = this.booking.getIdKhachHang();
                customers tk = new customers();
                tk.setIdKhachHang(selectedCustomerId);
                customers found = customerDAO.timKiem(tk);
                if (found != null) {
                    txtHoTen.setText(found.getNameKhachHang());
                    txtSDT.setText(found.getSoDienThoai());
                }
            }
        }

        // Footer buttons
        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pFooter.setBackground(new Color(245, 247, 250));
        pFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnHuy.setPreferredSize(new Dimension(100, 34));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());

        btnLuu = new JButton(isEdit ? "Cập nhật" : "Đặt phòng");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(new Color(13, 110, 253));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(130, 34));
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> xuLyLuu());

        pFooter.add(btnHuy);
        pFooter.add(btnLuu);
        add(pFooter, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (cccd.isEmpty() || hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD và họ tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra hoặc tạo mới khách hàng
        customers tk = new customers();
        tk.setSoCCCD(cccd);
        customers found = customerDAO.timKiem(tk);
        if (found == null) {
            customers moi = new customers(0, hoTen, cccd, sdt, "", "");
            customerDAO.them(moi);
            found = customerDAO.timKiem(tk);
        }
        if (found != null) {
            selectedCustomerId = found.getIdKhachHang();
        }

        rooms phongChon = (rooms) cboPhong.getSelectedItem();
        if (phongChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        selectedRoomId = phongChon.getMaPhong();
        selectedRoomPrice = phongChon.getGiaMoiDem();

        Date ngayNhan;
        Date ngayTra;
        try {
            ngayNhan = Date.valueOf(txtNgayNhan.getText().trim());
            ngayTra = Date.valueOf(txtNgayTra.getText().trim());
            if (!ngayTra.after(ngayNhan)) {
                JOptionPane.showMessageDialog(this, "Ngày trả phòng phải sau ngày nhận phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày nhận/trả phải đúng định dạng YYYY-MM-DD (Ví dụ: 2026-10-28)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int soNguoi = (int) spnSoNguoi.getValue();
        if (soNguoi > Math.max(1, phongChon.getSucChua())) {
            JOptionPane.showMessageDialog(this,
                    "Phòng " + phongChon.getSoPhong() + " chỉ nhận tối đa " + phongChon.getSucChua() + " khách!",
                    "Vượt sức chứa", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tt = (String) cboTrangThai.getSelectedItem();
        String ghiChu = txtGhiChu.getText().trim();

        this.booking.setIdKhachHang(selectedCustomerId);
        this.booking.setCccd(cccd);
        this.booking.setNgayNhanPhong(ngayNhan);
        this.booking.setNgayTraPhong(ngayTra);
        this.booking.setSoNguoi(soNguoi);
        this.booking.setTrangThai(tt);
        this.booking.setGhiChu(ghiChu);
        this.booking.setSoPhong(phongChon.getSoPhong());
        this.booking.setMaPhong(phongChon.getMaPhong());
        this.booking.setGiaMoiDem(phongChon.getGiaMoiDem());
        this.booking.setTang("Tầng " + phongChon.getTang());

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public bookings getBooking() {
        return booking;
    }

    public int getSelectedRoomId() {
        return selectedRoomId;
    }

    public float getSelectedRoomPrice() {
        return selectedRoomPrice;
    }

    private rooms phongDangChon() {
        return (rooms) cboPhong.getSelectedItem();
    }

    private void capNhatSucChuaPhong() {
        rooms phong = phongDangChon();
        int sucChua = phong != null ? Math.max(1, phong.getSucChua()) : 1;
        int soNguoi = (int) spnSoNguoi.getValue();
        spnSoNguoi.setModel(new SpinnerNumberModel(Math.min(soNguoi, sucChua), 1, sucChua, 1));
    }
}
