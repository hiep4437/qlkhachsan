package model;

import java.sql.Date;

public class ThongTinPhongDangO {
    private int maDatPhong;
    private int maKhachHang;
    private String tenKhachHang;
    private String cccd;
    private String soDienThoai;
    private Date ngayNhan;
    private Date ngayTra;
    private String trangThaiDatPhong;
    private String ghiChu;
    private double giaMoiDem;
    private int maHoaDon;
    private double tienPhong;
    private double phuThu;
    private double giamGia;
    private double chungTien;
    private String phuongThucThanhToan;
    private String trangThaiThanhToan;

    public int getBookingId() { return maDatPhong; }
    public void setBookingId(int maDatPhong) { this.maDatPhong = maDatPhong; }

    public int getCustomerId() { return maKhachHang; }
    public void setCustomerId(int maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Date getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(Date ngayNhan) { this.ngayNhan = ngayNhan; }

    public Date getNgayTra() { return ngayTra; }
    public void setNgayTra(Date ngayTra) { this.ngayTra = ngayTra; }

    public String getBookingStatus() { return trangThaiDatPhong; }
    public void setBookingStatus(String trangThaiDatPhong) { this.trangThaiDatPhong = trangThaiDatPhong; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public double getGiaMoiDem() { return giaMoiDem; }
    public void setGiaMoiDem(double giaMoiDem) { this.giaMoiDem = giaMoiDem; }

    public int getInvoiceId() { return maHoaDon; }
    public void setInvoiceId(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public double getPhuThu() { return phuThu; }
    public void setPhuThu(double phuThu) { this.phuThu = phuThu; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public double getTongTien() { return chungTien; }
    public void setTongTien(double chungTien) { this.chungTien = chungTien; }

    public String getPaymentMethod() { return phuongThucThanhToan; }
    public void setPaymentMethod(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public String getPaymentStatus() { return trangThaiThanhToan; }
    public void setPaymentStatus(String trangThaiThanhToan) { this.trangThaiThanhToan = trangThaiThanhToan; }
}
