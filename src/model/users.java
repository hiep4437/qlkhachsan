package model;

public class users {
	private int idNguoiDung;
	private String tenDangNhap;
	private String matKhau;
	private String hoTen;
	private String vaiTro;
	private boolean trangThai;
	public int getIdNguoiDung() {
		return idNguoiDung;
	}
	public void setIdNguoiDung(int idNguoiDung) {
		this.idNguoiDung = idNguoiDung;
	}
	public String getTenDangNhap() {
		return tenDangNhap;
	}
	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public String getVaiTro() {
		return vaiTro;
	}
	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	public users(int idNguoiDung, String tenDangNhap, String matKhau, String hoTen, String vaiTro, boolean trangThai) {
		super();
		this.idNguoiDung = idNguoiDung;
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.hoTen = hoTen;
		this.vaiTro = vaiTro;
		this.trangThai = trangThai;
	}
	public users() {
		super();
	}
	

}
