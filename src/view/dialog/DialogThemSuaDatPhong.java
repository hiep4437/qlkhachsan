package view.dialog;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.KhachHangDAO;
import dao.PhongDAO;
import model.DatPhong;
import model.KhachHang;
import model.Phong;
import service.DatPhongService;
import view.ChonNgay;

public class DialogThemSuaDatPhong extends JDialog {

    private JTextField oNhapCCCD;
    private JTextField oNhapHoTen;
    private JTextField oNhapSDT;
    private JButton btKiemTraCCCD;

    private JComboBox<Phong> hopChonPhong;
    private ChonNgay oNhapNgayNhan;
    private ChonNgay oNhapNgayTra;
    private JSpinner vongQuaySoNguoi;
    private JComboBox<String> hopChonTrangThai;
    private JTextField oNhapGhiChu;

    private JButton btLuu;
    private JButton btHuy;

    private boolean daLuu = false;
    private DatPhong datPhong;
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private PhongDAO phongDAO = new PhongDAO();
    private DatPhongService dichVuDatPhong = new DatPhongService();

    private int maKhachHangDaChon = 0;
    private int maPhongDaChon = 0;
    private float giaPhongDaChon = 0;

    public DialogThemSuaDatPhong(Window doiTuongCha, DatPhong datPhongCanSua) {
        super(doiTuongCha, datPhongCanSua == null ? "Đặt phòng mới" : "Chỉnh sửa đơn đặt phòng #" + datPhongCanSua.getIdDatPhong(),
                ModalityType.APPLICATION_MODAL);

        this.datPhong = datPhongCanSua != null ? datPhongCanSua : new DatPhong();
        boolean dangSua = (datPhongCanSua != null && datPhongCanSua.getIdDatPhong() > 0);

        setSize(580, 640);
        setLocationRelativeTo(doiTuongCha);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(30, 60, 114));
        khungTieuDe.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel nhanTieuDe = new JLabel(dangSua ? "CHỈNH SỬA ĐƠN ĐẶT PHÒNG #" + this.datPhong.getIdDatPhong() : "TẠO ĐƠN ĐẶT PHÒNG MỚI");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nhanTieuDe.setForeground(Color.WHITE);

        JLabel nhanPhu = new JLabel(dangSua ? "Cập nhật ngày nhận/trả, số người, trạng thái hoặc phòng"
                : "Nhập thông tin khách hàng, chọn phòng trống và ngày lưu trú");
        nhanPhu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nhanPhu.setForeground(new Color(220, 230, 242));

        khungTieuDe.add(nhanTieuDe, BorderLayout.NORTH);
        khungTieuDe.add(nhanPhu, BorderLayout.SOUTH);
        add(khungTieuDe, BorderLayout.NORTH);

        // Body Form
        JPanel khungNoiDung = new JPanel(new GridBagLayout());
        khungNoiDung.setBorder(new EmptyBorder(15, 20, 15, 20));
        khungNoiDung.setBackground(Color.WHITE);

        GridBagConstraints gia = new GridBagConstraints();
        gia.fill = GridBagConstraints.HORIZONTAL;
        gia.insets = new Insets(6, 6, 6, 6);

        Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 13);
        Font phongChuNhap = new Font("Segoe UI", Font.PLAIN, 13);

        // 1. CCCD Khách hàng
        gia.gridx = 0; gia.gridy = 0; gia.weightx = 0;
        JLabel nhanCCCD = new JLabel("Số CCCD (*):");
        nhanCCCD.setFont(phongChuNhan);
        khungNoiDung.add(nhanCCCD, gia);

        gia.gridx = 1; gia.weightx = 1;
        JPanel khungCCCD = new JPanel(new BorderLayout(5, 0));
        khungCCCD.setOpaque(false);
        oNhapCCCD = new JTextField();
        oNhapCCCD.setFont(phongChuNhap);
        oNhapCCCD.setPreferredSize(new Dimension(160, 32));
        btKiemTraCCCD = new JButton("Tìm");
        btKiemTraCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btKiemTraCCCD.setFocusPainted(false);
        khungCCCD.add(oNhapCCCD, BorderLayout.CENTER);
        khungCCCD.add(btKiemTraCCCD, BorderLayout.EAST);
        khungNoiDung.add(khungCCCD, gia);

        // 2. Tên khách hàng & SDT
        gia.gridx = 0; gia.gridy = 1; gia.weightx = 0;
        JLabel nhanHoTen = new JLabel("Họ tên khách (*):");
        nhanHoTen.setFont(phongChuNhan);
        khungNoiDung.add(nhanHoTen, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapHoTen = new JTextField();
        oNhapHoTen.setFont(phongChuNhap);
        oNhapHoTen.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapHoTen, gia);

        gia.gridx = 0; gia.gridy = 2; gia.weightx = 0;
        JLabel nhanSDT = new JLabel("Số điện thoại (*):");
        nhanSDT.setFont(phongChuNhan);
        khungNoiDung.add(nhanSDT, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapSDT = new JTextField();
        oNhapSDT.setFont(phongChuNhap);
        oNhapSDT.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapSDT, gia);

        // 3. Chọn phòng
        gia.gridx = 0; gia.gridy = 3; gia.weightx = 0;
        JLabel nhanPhong = new JLabel("Chọn phòng (*):");
        nhanPhong.setFont(phongChuNhan);
        khungNoiDung.add(nhanPhong, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonPhong = new JComboBox<>();
        hopChonPhong.setFont(phongChuNhap);
        hopChonPhong.setPreferredSize(new Dimension(200, 32));

        // Load danh sách phòng
        ArrayList<Phong> tatCaPhong = phongDAO.xuat();
        for (Phong hangHienTai : tatCaPhong) {
            boolean dangTrong = "AVAILABLE".equalsIgnoreCase(hangHienTai.getTrangThai()) || "TRỐNG".equalsIgnoreCase(hangHienTai.getTrangThai());
            if (dangTrong || (dangSua && hangHienTai.getSoPhong().equals(this.datPhong.getSoPhong()))) {
                hopChonPhong.addItem(hangHienTai);
            }
        }
        hopChonPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                if (giaTri instanceof Phong) {
                    Phong phong = (Phong) giaTri;
                    setText("Phòng " + phong.getSoPhong() + " - " + phong.getTenLoaiPhong()
                            + " (Tầng " + phong.getTang() + ", tối đa " + phong.getSucChua() + " khách)");
                }
                return this;
            }
        });
        khungNoiDung.add(hopChonPhong, gia);

        // 4. Ngày nhận & Ngày trả (yyyy-MM-dd)
        LocalDate homNay = LocalDate.now();
        LocalDate ngayMai = homNay.plusDays(1);

        gia.gridx = 0; gia.gridy = 4; gia.weightx = 0;
        JLabel nhanNhan = new JLabel("Ngày nhận:");
        nhanNhan.setFont(phongChuNhan);
        khungNoiDung.add(nhanNhan, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapNgayNhan = new ChonNgay(homNay, homNay);
        oNhapNgayNhan.setPreferredSize(new Dimension(324, 32));
        khungNoiDung.add(oNhapNgayNhan, gia);

        gia.gridx = 0; gia.gridy = 5; gia.weightx = 0;
        JLabel nhanTra = new JLabel("Ngày trả:");
        nhanTra.setFont(phongChuNhan);
        khungNoiDung.add(nhanTra, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapNgayTra = new ChonNgay(ngayMai, ngayMai);
        oNhapNgayTra.setPreferredSize(new Dimension(324, 32));
        khungNoiDung.add(oNhapNgayTra, gia);

        // 5. Số lượng người
        gia.gridx = 0; gia.gridy = 6; gia.weightx = 0;
        JLabel nhanSoNguoi = new JLabel("Số người ở:");
        nhanSoNguoi.setFont(phongChuNhan);
        khungNoiDung.add(nhanSoNguoi, gia);

        gia.gridx = 1; gia.weightx = 1;
        Phong phongBanDau = (Phong) hopChonPhong.getSelectedItem();
        int sucChuaBanDau = phongBanDau != null ? Math.max(1, phongBanDau.getSucChua()) : 1;
        vongQuaySoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, sucChuaBanDau, 1));
        vongQuaySoNguoi.setFont(phongChuNhap);
        vongQuaySoNguoi.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(vongQuaySoNguoi, gia);
        hopChonPhong.addActionListener(ac -> capNhatSucChuaPhong());

        // 6. Trạng thái
        gia.gridx = 0; gia.gridy = 7; gia.weightx = 0;
        JLabel nhanTrangThai = new JLabel("Trạng thái:");
        nhanTrangThai.setFont(phongChuNhan);
        khungNoiDung.add(nhanTrangThai, gia);

        gia.gridx = 1; gia.weightx = 1;
        hopChonTrangThai = new JComboBox<>(dangSua
                ? new String[] { "BOOKED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED" }
                : new String[] { "BOOKED" });
        hopChonTrangThai.setFont(phongChuNhap);
        hopChonTrangThai.setPreferredSize(new Dimension(200, 32));
        hopChonTrangThai.setEnabled(dangSua);
        hopChonTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> danhSach, Object giaTri, int chiSo,
                                                          boolean dangChon, boolean oDangChon) {
                super.getListCellRendererComponent(danhSach, giaTri, chiSo, dangChon, oDangChon);
                String chuoi = String.valueOf(giaTri);
                if ("BOOKED".equalsIgnoreCase(chuoi)) setText("Đã đặt (BOOKED)");
                else if ("CHECKED_IN".equalsIgnoreCase(chuoi)) setText("Đã nhận phòng (CHECKED_IN)");
                else if ("CHECKED_OUT".equalsIgnoreCase(chuoi)) setText("Đã trả phòng (CHECKED_OUT)");
                else if ("CANCELLED".equalsIgnoreCase(chuoi)) setText("Đã hủy (CANCELLED)");
                return this;
            }
        });
        khungNoiDung.add(hopChonTrangThai, gia);

        // 7. Ghi chú
        gia.gridx = 0; gia.gridy = 8; gia.weightx = 0;
        JLabel nhanGhiChu = new JLabel("Ghi chú:");
        nhanGhiChu.setFont(phongChuNhan);
        khungNoiDung.add(nhanGhiChu, gia);

        gia.gridx = 1; gia.weightx = 1;
        oNhapGhiChu = new JTextField();
        oNhapGhiChu.setFont(phongChuNhap);
        oNhapGhiChu.setPreferredSize(new Dimension(200, 32));
        khungNoiDung.add(oNhapGhiChu, gia);

        add(khungNoiDung, BorderLayout.CENTER);

        // Xử lý tìm kiếm CCCD
        btKiemTraCCCD.addActionListener(ac -> {
            String cccd = oNhapCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD để tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            KhachHang taiKhoan = new KhachHang();
            taiKhoan.setSoCCCD(cccd);
            KhachHang timThay = khachHangDAO.timKiem(taiKhoan);
            if (timThay != null) {
                maKhachHangDaChon = timThay.getIdKhachHang();
                oNhapHoTen.setText(timThay.getNameKhachHang());
                oNhapSDT.setText(timThay.getSoDienThoai());
                JOptionPane.showMessageDialog(this, "Tìm thấy khách hàng: " + timThay.getNameKhachHang(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Chưa có thông tin khách hàng này. Vui lòng nhập họ tên & SĐT để hệ thống tự động lưu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                oNhapHoTen.requestFocus();
            }
        });

        // Điền dữ liệu nếu là sửa
        if (dangSua) {
            oNhapCCCD.setText(this.datPhong.getCccd() != null ? this.datPhong.getCccd() : "");
            if (this.datPhong.getNgayNhanPhong() != null) {
                oNhapNgayNhan.setDate(this.datPhong.getNgayNhanPhong().toLocalDate());
            }
            if (this.datPhong.getNgayTraPhong() != null) {
                oNhapNgayTra.setDate(this.datPhong.getNgayTraPhong().toLocalDate());
            }
            oNhapGhiChu.setText(this.datPhong.getGhiChu() != null ? this.datPhong.getGhiChu() : "");

            String luongTrinh = this.datPhong.getTrangThai();
            if (luongTrinh != null) {
                if (luongTrinh.contains("CHECKED_IN") || luongTrinh.contains("Đã nhận")) hopChonTrangThai.setSelectedItem("CHECKED_IN");
                else if (luongTrinh.contains("CHECKED_OUT") || luongTrinh.contains("Đã trả")) hopChonTrangThai.setSelectedItem("CHECKED_OUT");
                else if (luongTrinh.contains("CANCELLED") || luongTrinh.contains("Đã hủy")) hopChonTrangThai.setSelectedItem("CANCELLED");
                else hopChonTrangThai.setSelectedItem("BOOKED");
            }

            // Chọn phòng
            if (this.datPhong.getSoPhong() != null) {
                for (int chiSo = 0; chiSo < hopChonPhong.getItemCount(); chiSo++) {
                    Phong hangHienTai = hopChonPhong.getItemAt(chiSo);
                    if (hangHienTai.getSoPhong().equals(this.datPhong.getSoPhong())) {
                        hopChonPhong.setSelectedIndex(chiSo);
                        break;
                    }
                }
            }
            capNhatSucChuaPhong();
            int sucChua = phongDangChon() != null ? Math.max(1, phongDangChon().getSucChua()) : 1;
            vongQuaySoNguoi.setValue(Math.min(Math.max(1, this.datPhong.getSoNguoi()), sucChua));

            // Tìm thông tin khách hàng
            if (this.datPhong.getIdKhachHang() > 0) {
                maKhachHangDaChon = this.datPhong.getIdKhachHang();
                KhachHang taiKhoan = new KhachHang();
                taiKhoan.setIdKhachHang(maKhachHangDaChon);
                KhachHang timThay = khachHangDAO.timKiem(taiKhoan);
                if (timThay != null) {
                    oNhapHoTen.setText(timThay.getNameKhachHang());
                    oNhapSDT.setText(timThay.getSoDienThoai());
                }
            }
        }

        oNhapNgayTra.setMinimumDate(oNhapNgayNhan.getDate().plusDays(1));
        oNhapNgayNhan.addDateChangeListener(ac -> {
            LocalDate earliestTraPhong = oNhapNgayNhan.getDate().plusDays(1);
            oNhapNgayTra.setMinimumDate(earliestTraPhong);
            if (oNhapNgayTra.getDate().isBefore(earliestTraPhong)) {
                oNhapNgayTra.setDate(earliestTraPhong);
            }
        });

        // Footer buttons
        JPanel khungChan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        khungChan.setBackground(new Color(245, 247, 250));
        khungChan.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        btHuy = new JButton("Hủy bỏ");
        btHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btHuy.setPreferredSize(new Dimension(100, 34));
        btHuy.setFocusPainted(false);
        btHuy.addActionListener(ac -> dispose());

        btLuu = new JButton(dangSua ? "Cập nhật" : "Đặt phòng");
        btLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btLuu.setBackground(new Color(13, 110, 253));
        btLuu.setForeground(Color.WHITE);
        btLuu.setPreferredSize(new Dimension(130, 34));
        btLuu.setFocusPainted(false);
        btLuu.addActionListener(ac -> xuLyLuu());

        khungChan.add(btHuy);
        khungChan.add(btLuu);
        add(khungChan, BorderLayout.SOUTH);
    }

    private void xuLyLuu() {
        String cccd = oNhapCCCD.getText().trim();
        String hoTen = oNhapHoTen.getText().trim();
        String sdt = oNhapSDT.getText().trim();

        if (cccd.isEmpty() || hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CCCD và họ tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Phong phongChon = (Phong) hopChonPhong.getSelectedItem();
        if (phongChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        maPhongDaChon = phongChon.getMaPhong();
        giaPhongDaChon = phongChon.getGiaMoiDem();

        Date ngayNhan;
        Date ngayTra;
        try {
            ngayNhan = Date.valueOf(oNhapNgayNhan.getDate());
            ngayTra = Date.valueOf(oNhapNgayTra.getDate());
            if (ngayNhan.toLocalDate().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Ngày nhận phòng phải từ hôm nay trở đi!", "Ngày nhận không hợp lệ",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ngayTra.after(ngayNhan)) {
                JOptionPane.showMessageDialog(this, "Ngày trả phòng phải sau ngày nhận phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận và ngày trả hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (dichVuDatPhong.phongBiDatTrung(maPhongDaChon, ngayNhan, ngayTra, datPhong.getIdDatPhong())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Phòng " + phongChon.getSoPhong() + " đã được đặt hoặc có khách trong khoảng thời gian này.\n"
                                + "Vui lòng chọn phòng khác hoặc điều chỉnh ngày lưu trú.",
                        "Phòng không khả dụng",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ngoaiLe) {
            JOptionPane.showMessageDialog(this, "Không thể kiểm tra tình trạng phòng: " + ngoaiLe.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int soNguoi = (int) vongQuaySoNguoi.getValue();
        if (soNguoi > Math.max(1, phongChon.getSucChua())) {
            JOptionPane.showMessageDialog(this,
                    "Phòng " + phongChon.getSoPhong() + " chỉ nhận tối đa " + phongChon.getSucChua() + " khách!",
                    "Vượt sức chứa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra hoặc tạo mới khách hàng sau khi xác thực phòng và ngày lưu trú.
        KhachHang taiKhoan = new KhachHang();
        taiKhoan.setSoCCCD(cccd);
        KhachHang timThay = khachHangDAO.timKiem(taiKhoan);
        if (timThay == null) {
            KhachHang moi = new KhachHang(0, hoTen, cccd, sdt, "", "");
            khachHangDAO.them(moi);
            timThay = khachHangDAO.timKiem(taiKhoan);
        }
        if (timThay == null) {
            JOptionPane.showMessageDialog(this, "Không thể lưu thông tin khách hàng. Vui lòng kiểm tra CCCD và thử lại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        maKhachHangDaChon = timThay.getIdKhachHang();

        String trangThai = (String) hopChonTrangThai.getSelectedItem();
        String ghiChu = oNhapGhiChu.getText().trim();

        this.datPhong.setIdKhachHang(maKhachHangDaChon);
        this.datPhong.setCccd(cccd);
        this.datPhong.setNgayNhanPhong(ngayNhan);
        this.datPhong.setNgayTraPhong(ngayTra);
        this.datPhong.setSoNguoi(soNguoi);
        this.datPhong.setTrangThai(trangThai);
        this.datPhong.setGhiChu(ghiChu);
        this.datPhong.setSoPhong(phongChon.getSoPhong());
        this.datPhong.setMaPhong(phongChon.getMaPhong());
        this.datPhong.setGiaMoiDem(phongChon.getGiaMoiDem());
        this.datPhong.setTang("Tầng " + phongChon.getTang());

        daLuu = true;
        dispose();
    }

    public boolean isSaved() {
        return daLuu;
    }

    public DatPhong getBooking() {
        return datPhong;
    }

    public int getSelectedRoomId() {
        return maPhongDaChon;
    }

    public float getSelectedRoomPrice() {
        return giaPhongDaChon;
    }

    private Phong phongDangChon() {
        return (Phong) hopChonPhong.getSelectedItem();
    }

    private void capNhatSucChuaPhong() {
        Phong phong = phongDangChon();
        int sucChua = phong != null ? Math.max(1, phong.getSucChua()) : 1;
        int soNguoi = (int) vongQuaySoNguoi.getValue();
        vongQuaySoNguoi.setModel(new SpinnerNumberModel(Math.min(soNguoi, sucChua), 1, sucChua, 1));
    }
}
