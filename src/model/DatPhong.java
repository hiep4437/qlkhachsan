package model;

import java.sql.Date;

public class DatPhong {
	private int idDatPhong;
	private int idKhachHang;
	private Date ngayNhanPhong;
	private Date ngayTraPhong;
	private String trangThai;
	private Date ngayTao;
	private String ghiChu;

	// Thong tin lien ket tu chi_tiet_dat_phong, phong, loai_phong, khach_hang
	private String cccd;
	private String tang;
	private int soNguoi;
	private String soPhong;
	private int maPhong;
	private double giaMoiDem;
	private String tenLoaiPhong;
	private String tenKhachHang;
	private String soDienThoai;

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

	public String getCccd() {
		return cccd != null ? cccd : "";
	}
	public void setCccd(String cccd) {
		this.cccd = cccd;
	}
	public String getTang() {
		return tang != null ? tang : "";
	}
	public void setTang(String tang) {
		this.tang = tang;
	}
	public int getSoNguoi() {
		return soNguoi;
	}
	public void setSoNguoi(int soNguoi) {
		this.soNguoi = soNguoi;
	}
	public String getSoPhong() {
		return soPhong != null ? soPhong : "";
	}
	public void setSoPhong(String soPhong) {
		this.soPhong = soPhong;
	}

	public int getMaPhong() {
		return maPhong;
	}
	public void setMaPhong(int maPhong) {
		this.maPhong = maPhong;
	}
	public double getGiaMoiDem() {
		return giaMoiDem;
	}
	public void setGiaMoiDem(double giaMoiDem) {
		this.giaMoiDem = giaMoiDem;
	}
	public String getTenLoaiPhong() {
		return tenLoaiPhong != null ? tenLoaiPhong : "";
	}
	public void setTenLoaiPhong(String tenLoaiPhong) {
		this.tenLoaiPhong = tenLoaiPhong;
	}
	public String getTenKhachHang() {
		return tenKhachHang != null ? tenKhachHang : "";
	}
	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}
	public String getSoDienThoai() {
		return soDienThoai != null ? soDienThoai : "";
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public DatPhong(int idDatPhong, int idKhachHang, Date ngayNhanPhong, Date ngayTraPhong, String trangThai,
			Date ngayTao, String ghiChu) {
		this.idDatPhong = idDatPhong;
		this.idKhachHang = idKhachHang;
		this.ngayNhanPhong = ngayNhanPhong;
		this.ngayTraPhong = ngayTraPhong;
		this.trangThai = trangThai;
		this.ngayTao = ngayTao;
		this.ghiChu = ghiChu;
	}

	public DatPhong(int idDatPhong, int idKhachHang, Date ngayNhanPhong, Date ngayTraPhong, String trangThai,
			Date ngayTao, String ghiChu, String cccd, String tang, int soNguoi, String soPhong) {
		this(idDatPhong, idKhachHang, ngayNhanPhong, ngayTraPhong, trangThai, ngayTao, ghiChu);
		this.cccd = cccd;
		this.tang = tang;
		this.soNguoi = soNguoi;
		this.soPhong = soPhong;
	}

	public DatPhong(int idDatPhong, int idKhachHang, Date ngayNhanPhong, Date ngayTraPhong, String trangThai,
			Date ngayTao, String ghiChu, String cccd, String tang, int soNguoi, String soPhong,
			int maPhong, double giaMoiDem, String tenLoaiPhong, String tenKhachHang, String soDienThoai) {
		this(idDatPhong, idKhachHang, ngayNhanPhong, ngayTraPhong, trangThai, ngayTao, ghiChu, cccd, tang, soNguoi, soPhong);
		this.maPhong = maPhong;
		this.giaMoiDem = giaMoiDem;
		this.tenLoaiPhong = tenLoaiPhong;
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
	}

	public DatPhong() {
		super();
	}
}
