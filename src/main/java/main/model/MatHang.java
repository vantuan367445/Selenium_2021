package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
@Entity
public class MatHang {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idMatHang;
	
	@NotNull
	private String loaiMatHang;
	
	@NotNull
	private String tenMatHang;

	private int soLuong;
	
	@NotNull
	private int giaNhap;
	
	@NotNull
	private int giaBan;
	
	private int active;
	
	@PrePersist
	void createActive() {
		this.active = 1;
	}
	
	public MatHang() {
		
	}

	public int getIdMatHang() {
		return idMatHang;
	}

	public void setIdMatHang(int idMatHang) {
		this.idMatHang = idMatHang;
	}

	public String getLoaiMatHang() {
		return loaiMatHang;
	}

	public void setLoaiMatHang(String loaiMatHang) {
		this.loaiMatHang = loaiMatHang;
	}

	public String getTenMatHang() {
		return tenMatHang;
	}

	public void setTenMatHang(String tenMatHang) {
		this.tenMatHang = tenMatHang;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public int getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(int giaNhap) {
		this.giaNhap = giaNhap;
	}

	public int getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(int giaBan) {
		this.giaBan = giaBan;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
}
