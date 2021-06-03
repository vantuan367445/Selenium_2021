package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class NhaCungCap {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idNhaCungCap;
	
	@NotNull
	@NotBlank(message = "Tên nhà cung cấp không được để trống")
	private String tenNhaCungCap;
	
	@NotNull
	@NotBlank(message = "Tên chủ cửa hàng không được để trống")
	private String tenChuCuaHang;
	
	@NotNull
	@NotBlank(message = "Địa chỉ không được để trống")
	private String diaChi;
	
	@NotNull
	@NotBlank(message = "Số điện thoại không được để trống")
	private String soDienThoai;
	
	private int active;
	
	@PrePersist
	void createActive() {
		this.active = 1;
	}
	public NhaCungCap() {}
	public int getIdNhaCungCap() {
		return idNhaCungCap;
	}
	public void setIdNhaCungCap(int idNhaCungCap) {
		this.idNhaCungCap = idNhaCungCap;
	}
	public String getTenNhaCungCap() {
		return tenNhaCungCap;
	}
	public void setTenNhaCungCap(String tenNhaCungCap) {
		this.tenNhaCungCap = tenNhaCungCap;
	}
	public String getTenChuCuaHang() {
		return tenChuCuaHang;
	}
	public void setTenChuCuaHang(String tenChuCuaHang) {
		this.tenChuCuaHang = tenChuCuaHang;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	
	
	
}
