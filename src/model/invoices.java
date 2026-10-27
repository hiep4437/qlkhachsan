package model;

import java.sql.Timestamp;
public class invoices {
	private int idHoaDon;
	private int idDatPhong;
	private Timestamp ngayLap;
	private float tienPhong;
	private float phuThu;
	private float giamGia;
	private float tongTien;
	private String phuongThucThanhToan;
	private String trangThaiThanhToan;
	public int getIdHoaDon() {
		return idHoaDon;
	}
	public void setIdHoaDon(int idHoaDon) {
		this.idHoaDon = idHoaDon;
	}
	public int getIdDatPhong() {
		return idDatPhong;
	}
	public void setIdDatPhong(int idDatPhong) {
		this.idDatPhong = idDatPhong;
	}
	public Timestamp getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(Timestamp ngayLap) {
		this.ngayLap = ngayLap;
	}
	public float getTienPhong() {
		return tienPhong;
	}
	public void setTienPhong(float tienPhong) {
		this.tienPhong = tienPhong;
	}
	public float getPhuThu() {
		return phuThu;
	}
	public void setPhuThu(float phuThu) {
		this.phuThu = phuThu;
	}
	public float getGiamGia() {
		return giamGia;
	}
	public void setGiamGia(float giamGia) {
		this.giamGia = giamGia;
	}
	public float getTongTien() {
		return tongTien;
	}
	public void setTongTien(float tongTien) {
		this.tongTien = tongTien;
	}
	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}
	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}
	public String getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}
	public void setTrangThaiThanhToan(String trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}
	public invoices(int idHoaDon, int idDatPhong, Timestamp ngayLap, float tienPhong, float phuThu, float giamGia,
			float tongTien, String phuongThucThanhToan, String trangThaiThanhToan) {
		super();
		this.idHoaDon = idHoaDon;
		this.idDatPhong = idDatPhong;
		this.ngayLap = ngayLap;
		this.tienPhong = tienPhong;
		this.phuThu = phuThu;
		this.giamGia = giamGia;
		this.tongTien = tongTien;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.trangThaiThanhToan = trangThaiThanhToan;
	}
	public invoices() {
		super();
	}
	

}
