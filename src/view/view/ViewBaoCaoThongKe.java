package view.view;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public class ViewBaoCaoThongKe extends JFrame {

    public DefaultTableModel moHinhPhong;
    public DefaultTableModel moHinhHoaDon;

    private LocalDate tuNgay = LocalDate.now().withDayOfMonth(1);
    private LocalDate denNgay = LocalDate.now();
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ViewBaoCaoThongKe() {
        initComponents();
        view.AppIcon.apply(this);
        setSize(1240, 760);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        moHinhPhong = (DefaultTableModel) bangPhong.getModel();
        moHinhHoaDon = (DefaultTableModel) bangHoaDon.getModel();

        hopChonKyBaoCao.addActionListener(ac -> capNhatTheoKyNhanh());
        // Lắng nghe thay đổi ngày khi người dùng nhập thủ công
        oNhapTuNgay.addActionListener(ac -> {
            LocalDate parsed = docNgayTuText(oNhapTuNgay.getText());
            if (parsed != null) { tuNgay = parsed; datKyTuyChinh(); }
        });
        oNhapDenNgay.addActionListener(ac -> {
            LocalDate parsed = docNgayTuText(oNhapDenNgay.getText());
            if (parsed != null) { denNgay = parsed; datKyTuyChinh(); }
        });
        capNhatONgay();
    }

    private LocalDate docNgayTuText(String chuoi) {
        try { return LocalDate.parse(chuoi.trim(), DINH_DANG_NGAY); } catch (Exception e) { return null; }
    }

    private void capNhatTheoKyNhanh() {
        String luaChonSan = (String) hopChonKyBaoCao.getSelectedItem();
        if (luaChonSan == null || luaChonSan.equals("Tùy chỉnh")) {
            return;
        }

        LocalDate homNay = LocalDate.now();
        switch (luaChonSan) {
            case "Hôm nay":
                tuNgay = homNay;
                denNgay = homNay;
                break;
            case "7 ngày gần đây":
                tuNgay = homNay.minusDays(6);
                denNgay = homNay;
                break;
            case "30 ngày gần đây":
                tuNgay = homNay.minusDays(29);
                denNgay = homNay;
                break;
            case "Tháng trước":
                YearMonth truocThang = YearMonth.from(homNay).minusMonths(1);
                tuNgay = truocThang.atDay(1);
                denNgay = truocThang.atEndOfMonth();
                break;
            case "Năm nay":
                tuNgay = homNay.withDayOfYear(1);
                denNgay = homNay;
                break;
            case "Toàn bộ thời gian":
                tuNgay = LocalDate.of(2026, 1, 1);
                denNgay = homNay;
                break;
            case "Tháng này":
            default:
                tuNgay = homNay.withDayOfMonth(1);
                denNgay = homNay;
                break;
        }
        capNhatONgay();
    }

    private void datKyTuyChinh() {
        if (hopChonKyBaoCao.getSelectedItem() != null
                && !hopChonKyBaoCao.getSelectedItem().equals("Tùy chỉnh")) {
            hopChonKyBaoCao.setSelectedItem("Tùy chỉnh");
        }
    }

    private void capNhatONgay() {
        if (oNhapTuNgay != null) {
            oNhapTuNgay.setText(tuNgay.format(DINH_DANG_NGAY));
        }
        if (oNhapDenNgay != null) {
            oNhapDenNgay.setText(denNgay.format(DINH_DANG_NGAY));
        }
    }

    public LocalDate getTuNgay() {
        if (oNhapTuNgay != null) {
            LocalDate parsed = docNgayTuText(oNhapTuNgay.getText());
            if (parsed != null) return parsed;
        }
        return tuNgay;
    }

    public LocalDate getDenNgay() {
        if (oNhapDenNgay != null) {
            LocalDate parsed = docNgayTuText(oNhapDenNgay.getText());
            if (parsed != null) return parsed;
        }
        return denNgay;
    }

    public void capNhatBieuDoPhong(List<String> tenPhong, List<String> chiTiet, List<Double> doanhThu) {
        // Hỗ trợ hiển thị biểu đồ phòng nếu cần mở rộng
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        khungChinh = new javax.swing.JPanel();
        nhanTieuDe = new javax.swing.JLabel();
        khungCard1 = new javax.swing.JPanel();
        nhanTieuDeCard1 = new javax.swing.JLabel();
        nhanGiaTriChungDoanhThu = new javax.swing.JLabel();
        khungCard2 = new javax.swing.JPanel();
        nhanTieuDeCard2 = new javax.swing.JLabel();
        nhanGiaTriDoanhThuThang = new javax.swing.JLabel();
        khungCard3 = new javax.swing.JPanel();
        nhanTieuDeCard3 = new javax.swing.JLabel();
        nhanGiaTriDoanhThuNgay = new javax.swing.JLabel();
        nhanKyNhanh = new javax.swing.JLabel();
        hopChonKyBaoCao = new javax.swing.JComboBox<>();
        nhanTuNgay = new javax.swing.JLabel();
        oNhapTuNgay = new javax.swing.JTextField();
        nhanDenNgay = new javax.swing.JLabel();
        oNhapDenNgay = new javax.swing.JTextField();
        btLoc = new javax.swing.JButton();
        nhanThongKeKy = new javax.swing.JLabel();
        cacTabBaoCao = new javax.swing.JTabbedPane();
        thePhong = new javax.swing.JPanel();
        cuonPanePhong = new javax.swing.JScrollPane();
        bangPhong = new javax.swing.JTable();
        theHoaDon = new javax.swing.JPanel();
        cuonPaneHoaDon = new javax.swing.JScrollPane();
        bangHoaDon = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Báo cáo doanh thu");

        khungChinh.setBackground(new java.awt.Color(255, 255, 255));

        nhanTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        nhanTieuDe.setForeground(new java.awt.Color(24, 44, 97));
        nhanTieuDe.setText("BÁO CÁO THỐNG KÊ DOANH THU");

        khungCard1.setBackground(new java.awt.Color(248, 250, 252));
        khungCard1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        nhanTieuDeCard1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanTieuDeCard1.setForeground(new java.awt.Color(100, 116, 139));
        nhanTieuDeCard1.setText("DOANH THU KỲ");

        nhanGiaTriChungDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        nhanGiaTriChungDoanhThu.setForeground(new java.awt.Color(37, 99, 235));
        nhanGiaTriChungDoanhThu.setText("0 đ");

        javax.swing.GroupLayout khungCard1Layout = new javax.swing.GroupLayout(khungCard1);
        khungCard1.setLayout(khungCard1Layout);
        khungCard1Layout.setHorizontalGroup(
            khungCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(khungCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDeCard1)
                    .addComponent(nhanGiaTriChungDoanhThu))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        khungCard1Layout.setVerticalGroup(
            khungCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(nhanTieuDeCard1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhanGiaTriChungDoanhThu)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        khungCard2.setBackground(new java.awt.Color(248, 250, 252));
        khungCard2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        nhanTieuDeCard2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanTieuDeCard2.setForeground(new java.awt.Color(100, 116, 139));
        nhanTieuDeCard2.setText("HÓA ĐƠN ĐÃ THU");

        nhanGiaTriDoanhThuThang.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        nhanGiaTriDoanhThuThang.setForeground(new java.awt.Color(22, 163, 74));
        nhanGiaTriDoanhThuThang.setText("0");

        javax.swing.GroupLayout khungCard2Layout = new javax.swing.GroupLayout(khungCard2);
        khungCard2.setLayout(khungCard2Layout);
        khungCard2Layout.setHorizontalGroup(
            khungCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(khungCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDeCard2)
                    .addComponent(nhanGiaTriDoanhThuThang))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        khungCard2Layout.setVerticalGroup(
            khungCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(nhanTieuDeCard2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhanGiaTriDoanhThuThang)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        khungCard3.setBackground(new java.awt.Color(248, 250, 252));
        khungCard3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)));

        nhanTieuDeCard3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nhanTieuDeCard3.setForeground(new java.awt.Color(100, 116, 139));
        nhanTieuDeCard3.setText("LƯỢT THUÊ PHÒNG");

        nhanGiaTriDoanhThuNgay.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        nhanGiaTriDoanhThuNgay.setForeground(new java.awt.Color(217, 119, 6));
        nhanGiaTriDoanhThuNgay.setText("0");

        javax.swing.GroupLayout khungCard3Layout = new javax.swing.GroupLayout(khungCard3);
        khungCard3.setLayout(khungCard3Layout);
        khungCard3Layout.setHorizontalGroup(
            khungCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(khungCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nhanTieuDeCard3)
                    .addComponent(nhanGiaTriDoanhThuNgay))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        khungCard3Layout.setVerticalGroup(
            khungCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungCard3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(nhanTieuDeCard3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhanGiaTriDoanhThuNgay)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        nhanKyNhanh.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanKyNhanh.setText("Kỳ nhanh:");

        hopChonKyBaoCao.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hopChonKyBaoCao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng này", "Hôm nay", "7 ngày gần đây", "30 ngày gần đây", "Tháng trước", "Năm nay", "Toàn bộ thời gian", "Tùy chỉnh" }));

        nhanTuNgay.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanTuNgay.setText("Từ ngày:");

        nhanDenNgay.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        nhanDenNgay.setText("Đến ngày:");

        btLoc.setBackground(new java.awt.Color(24, 44, 97));
        btLoc.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btLoc.setForeground(new java.awt.Color(255, 255, 255));
        btLoc.setText("Xem thống kê");
        btLoc.setFocusPainted(false);

        nhanThongKeKy.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        nhanThongKeKy.setForeground(new java.awt.Color(37, 99, 235));
        nhanThongKeKy.setText("Thực thu trong kỳ: 0 đ");

        cacTabBaoCao.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N

        bangPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Phòng", "Loại phòng", "Tầng", "Lượt thuê", "Doanh thu", "Tỷ trọng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cuonPanePhong.setViewportView(bangPhong);

        javax.swing.GroupLayout thePhongLayout = new javax.swing.GroupLayout(thePhong);
        thePhong.setLayout(thePhongLayout);
        thePhongLayout.setHorizontalGroup(
            thePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cuonPanePhong, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        thePhongLayout.setVerticalGroup(
            thePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cuonPanePhong, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );

        cacTabBaoCao.addTab("Tổng quan theo phòng", thePhong);

        bangHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HĐ", "Thời gian", "Phòng", "Khách hàng", "Số điện thoại", "Tiền phòng", "Phụ thu", "Giảm giá", "Thực thu", "Thanh toán"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cuonPaneHoaDon.setViewportView(bangHoaDon);

        javax.swing.GroupLayout theHoaDonLayout = new javax.swing.GroupLayout(theHoaDon);
        theHoaDon.setLayout(theHoaDonLayout);
        theHoaDonLayout.setHorizontalGroup(
            theHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cuonPaneHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        theHoaDonLayout.setVerticalGroup(
            theHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cuonPaneHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );

        cacTabBaoCao.addTab("Chi tiết hóa đơn", theHoaDon);

        javax.swing.GroupLayout khungChinhLayout = new javax.swing.GroupLayout(khungChinh);
        khungChinh.setLayout(khungChinhLayout);
        khungChinhLayout.setHorizontalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cacTabBaoCao)
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(khungCard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(khungCard2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(khungCard3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(nhanTieuDe)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(khungChinhLayout.createSequentialGroup()
                        .addComponent(nhanKyNhanh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hopChonKyBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nhanTuNgay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oNhapTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nhanDenNgay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oNhapDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nhanThongKeKy, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        khungChinhLayout.setVerticalGroup(
            khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khungChinhLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(nhanTieuDe)
                .addGap(18, 18, 18)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(khungCard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(khungCard2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(khungCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(khungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhanKyNhanh)
                    .addComponent(hopChonKyBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhanTuNgay)
                    .addComponent(oNhapTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhanDenNgay)
                    .addComponent(oNhapDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhanThongKeKy))
                .addGap(18, 18, 18)
                .addComponent(cacTabBaoCao, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(khungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable bangHoaDon;
    public javax.swing.JTable bangPhong;
    public javax.swing.JButton btLoc;
    private javax.swing.JTabbedPane cacTabBaoCao;
    private javax.swing.JScrollPane cuonPaneHoaDon;
    private javax.swing.JScrollPane cuonPanePhong;
    public javax.swing.JComboBox<String> hopChonKyBaoCao;
    private javax.swing.JPanel khungCard1;
    private javax.swing.JPanel khungCard2;
    private javax.swing.JPanel khungCard3;
    private javax.swing.JPanel khungChinh;
    private javax.swing.JLabel nhanDenNgay;
    public javax.swing.JLabel nhanGiaTriChungDoanhThu;
    public javax.swing.JLabel nhanGiaTriDoanhThuNgay;
    public javax.swing.JLabel nhanGiaTriDoanhThuThang;
    private javax.swing.JLabel nhanKyNhanh;
    public javax.swing.JLabel nhanThongKeKy;
    private javax.swing.JLabel nhanTieuDe;
    private javax.swing.JLabel nhanTieuDeCard1;
    private javax.swing.JLabel nhanTieuDeCard2;
    private javax.swing.JLabel nhanTieuDeCard3;
    private javax.swing.JLabel nhanTuNgay;
    public javax.swing.JTextField oNhapDenNgay;
    public javax.swing.JTextField oNhapTuNgay;
    private javax.swing.JPanel theHoaDon;
    private javax.swing.JPanel thePhong;
    // End of variables declaration//GEN-END:variables
}

