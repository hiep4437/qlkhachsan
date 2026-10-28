package model;

public class rooms {
	private int maPhong;
	private String soPhong;
	private int maLoaiPhong;
	private float giaMoiDem;
	private String trangThai;
	private String moTa;
	private String tenLoaiPhong;

	public int getMaPhong() {
		return maPhong;
	}
	public void setMaPhong(int maPhong) {
		this.maPhong = maPhong;
	}
	public String getSoPhong() {
		return soPhong;
	}
	public void setSoPhong(String soPhong) {
		this.soPhong = soPhong;
	}
	public int getMaLoaiPhong() {
		return maLoaiPhong;
	}
	public void setMaLoaiPhong(int maLoaiPhong) {
		this.maLoaiPhong = maLoaiPhong;
	}
	public float getGiaMoiDem() {
		return giaMoiDem;
	}
	public void setGiaMoiDem(float giaMoiDem) {
		this.giaMoiDem = giaMoiDem;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getTenLoaiPhong() {
		return tenLoaiPhong;
	}
	public void setTenLoaiPhong(String tenLoaiPhong) {
		this.tenLoaiPhong = tenLoaiPhong;
	}

	public rooms(int maPhong, String soPhong, int maLoaiPhong, float giaMoiDem, String trangThai, String moTa) {
		super();
		this.maPhong = maPhong;
		this.soPhong = soPhong;
		this.maLoaiPhong = maLoaiPhong;
		this.giaMoiDem = giaMoiDem;
		this.trangThai = trangThai;
		this.moTa = moTa;
	}

	public rooms(int maPhong, String soPhong, int maLoaiPhong, float giaMoiDem, String trangThai, String moTa, String tenLoaiPhong) {
		super();
		this.maPhong = maPhong;
		this.soPhong = soPhong;
		this.maLoaiPhong = maLoaiPhong;
		this.giaMoiDem = giaMoiDem;
		this.trangThai = trangThai;
		this.moTa = moTa;
		this.tenLoaiPhong = tenLoaiPhong;
	}

	public rooms() {
		super();
	}
}
