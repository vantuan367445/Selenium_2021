package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class HangNhap {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idHangNhap;
	
	private int soLuong;
	private int donGia;
	
	@ManyToOne
    @JoinColumn(name = "idMathang")
    private MatHang matHang;
	
	public HangNhap() {}

	public int getIdHangNhap() {
		return idHangNhap;
	}

	public void setIdHangNhap(int idHangNhap) {
		this.idHangNhap = idHangNhap;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public int getDonGia() {
		return donGia;
	}

	public void setDonGia(int donGia) {
		this.donGia = donGia;
	}

	public MatHang getMatHang() {
		return matHang;
	}

	public void setMatHang(MatHang matHang) {
		this.matHang = matHang;
	}
	
	
}
