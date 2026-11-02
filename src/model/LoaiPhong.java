package model;

public class LoaiPhong {
	private int maLoaiPhong;
	private String tenLoaiPhong;
	private int sucChua;
	private String moTa;
	public int getMaLoaiPhong() {
		return maLoaiPhong;
	}
	public void setMaLoaiPhong(int maLoaiPhong) {
		this.maLoaiPhong = maLoaiPhong;
	}
	public String getTenLoaiPhong() {
		return tenLoaiPhong;
	}
	public void setTenLoaiPhong(String tenLoaiPhong) {
		this.tenLoaiPhong = tenLoaiPhong;
	}
	public int getSucChua() {
		return sucChua;
	}
	public void setSucChua(int sucChua) {
		this.sucChua = sucChua;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public LoaiPhong(int maLoaiPhong, String tenLoaiPhong, int sucChua, String moTa) {
		super();
		this.maLoaiPhong = maLoaiPhong;
		this.tenLoaiPhong = tenLoaiPhong;
		this.sucChua = sucChua;
		this.moTa = moTa;
	}
	public LoaiPhong() {
		super();
	}
	

}
