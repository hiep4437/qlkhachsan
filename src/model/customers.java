package model;

public class customers {
	private int idKhachHang;
	private String nameKhachHang;
	private String soCCCD;
	private String soDienThoai;
	private String email;
	private String diaChi;
	public int getIdKhachHang() {
		return idKhachHang;
	}
	public void setIdKhachHang(int idKhachHang) {
		this.idKhachHang = idKhachHang;
	}
	public String getNameKhachHang() {
		return nameKhachHang;
	}
	public void setNameKhachHang(String nameKhachHang) {
		this.nameKhachHang = nameKhachHang;
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
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public customers(int idKhachHang, String nameKhachHang, String soCCCD, String soDienThoai, String email,
			String diaChi) {
		super();
		this.idKhachHang = idKhachHang;
		this.nameKhachHang = nameKhachHang;
		this.soCCCD = soCCCD;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.diaChi = diaChi;
	}
	public customers() {
		super();
	}

}
