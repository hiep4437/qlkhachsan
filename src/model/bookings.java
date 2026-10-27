package model;

import java.sql.Date;

public class bookings {
	private int idDatPhong;
	private int idKhachHang;
	private Date ngayNhanPhong;
	private Date ngayTraPhong;
	private String trangThai;
	private Date ngayTao;
	private String ghiChu;
	public int getIdDatPhong() {
		return idDatPhong;
	}
	public void setIdDatPhong(int idDatPhong) {
		this.idDatPhong = idDatPhong;
	}
	public int getIdKhachHang() {
		return idKhachHang;
	}
	public void setIdKhachHang(int idKhachHang) {
		this.idKhachHang = idKhachHang;
	}
	public Date getNgayNhanPhong() {
		return ngayNhanPhong;
	}
	public void setNgayNhanPhong(Date ngayNhanPhong) {
		this.ngayNhanPhong = ngayNhanPhong;
	}
	public Date getNgayTraPhong() {
		return ngayTraPhong;
	}
	public void setNgayTraPhong(Date ngayTraPhong) {
		this.ngayTraPhong = ngayTraPhong;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public Date getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	public bookings(int idDatPhong, int idKhachHang, Date ngayNhanPhong, Date ngayTraPhong, String trangThai,
			Date ngayTao, String ghiChu) {
		super();
		this.idDatPhong = idDatPhong;
		this.idKhachHang = idKhachHang;
		this.ngayNhanPhong = ngayNhanPhong;
		this.ngayTraPhong = ngayTraPhong;
		this.trangThai = trangThai;
		this.ngayTao = ngayTao;
		this.ghiChu = ghiChu;
	}
	public bookings() {
		super();
	}
	

}
