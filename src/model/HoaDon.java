package model;

import java.sql.Date;
import java.sql.Timestamp;

public class HoaDon {
	private int idHoaDon;
	private int idDatPhong;
	private Timestamp ngayLap;
	private float tienPhong;
	private float phuThu;
	private float giamGia;
	private float tongTien;
	private String phuongThucThanhToan;
	private String trangThaiThanhToan;

	// Thong tin lien ket tu dat_phong, phong, loai_phong, khach_hang
	private String soPhong;
	private String tenLoaiPhong;
	private String tenKhachHang;
	private String soDienThoai;
	private String soCCCD;
	private Date ngayNhanPhong;
	private Date ngayTraPhong;

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

	public String getSoPhong() {
		return soPhong != null ? soPhong : "";
	}
	public void setSoPhong(String soPhong) {
		this.soPhong = soPhong;
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
	public String getSoCCCD() {
		return soCCCD != null ? soCCCD : "";
	}
	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
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

	public HoaDon(int idHoaDon, int idDatPhong, Timestamp ngayLap, float tienPhong, float phuThu, float giamGia,
			float tongTien, String phuongThucThanhToan, String trangThaiThanhToan) {
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

	public HoaDon(int idHoaDon, int idDatPhong, Timestamp ngayLap, float tienPhong, float phuThu, float giamGia,
			float tongTien, String phuongThucThanhToan, String trangThaiThanhToan,
			String soPhong, String tenLoaiPhong, String tenKhachHang, String soDienThoai, String soCCCD,
			Date ngayNhanPhong, Date ngayTraPhong) {
		this(idHoaDon, idDatPhong, ngayLap, tienPhong, phuThu, giamGia, tongTien, phuongThucThanhToan, trangThaiThanhToan);
		this.soPhong = soPhong;
		this.tenLoaiPhong = tenLoaiPhong;
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
		this.soCCCD = soCCCD;
		this.ngayNhanPhong = ngayNhanPhong;
		this.ngayTraPhong = ngayTraPhong;
	}

	public HoaDon() {
		super();
	}
}
