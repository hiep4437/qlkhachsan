package model;

public class KhachHang {
	private int maKhachHang;
	private String tenKhachHang;
	private String soCCCD;
	private String soDienThoai;
	private String thuDienTu;
	private String diaChi;
	public int getIdKhachHang() {
		return maKhachHang;
	}
	public void setIdKhachHang(int maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public String getNameKhachHang() {
		return tenKhachHang;
	}
	public void setNameKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}
	public String getSoCCCD() {
		return soCCCD;
	}
	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public String getEmail() {
		return thuDienTu;
	}
	public void setEmail(String thuDienTu) {
		this.thuDienTu = thuDienTu;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public KhachHang(int maKhachHang, String tenKhachHang, String soCCCD, String soDienThoai, String thuDienTu,
			String diaChi) {
		super();
		this.maKhachHang = maKhachHang;
		this.tenKhachHang = tenKhachHang;
		this.soCCCD = soCCCD;
		this.soDienThoai = soDienThoai;
		this.thuDienTu = thuDienTu;
		this.diaChi = diaChi;
	}
	public KhachHang() {
		super();
	}

}
