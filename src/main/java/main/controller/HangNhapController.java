package main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import main.model.DonNhapHang;
import main.model.HangNhap;
import main.model.MatHang;
import main.model.NhaCungCap;
import main.model.NhanVien;

@Controller
@RequestMapping("/goods")
public class HangNhapController {
	private DonNhapHang dnh;

	private RestTemplate rest = new RestTemplate();
	@Autowired
	public HangNhapController() {
	}
	
	
	
	@PostMapping("/new")
	public String openViewOrder(@ModelAttribute("hangnhap") HangNhap hangNhap,
			Model model, HttpSession session) {
		
		dnh = (DonNhapHang) session.getAttribute("donnhaphang");
		List<HangNhap> listHangNhap = new ArrayList<>();
		if(dnh == null) {
			dnh = new DonNhapHang();
			NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
			NhaCungCap ncc = (NhaCungCap) session.getAttribute("nhacungcap");
			listHangNhap.add(hangNhap);
			dnh.setListHangNhap(listHangNhap);
			dnh.setNhanVien(nv);
			dnh.setNhaCungCap(ncc);
		}
		else {
			listHangNhap = dnh.getListHangNhap();
			listHangNhap = checkExits(hangNhap, listHangNhap);
			dnh.setListHangNhap(listHangNhap);
		}
		
		session.setAttribute("donnhaphang", dnh);
		
		long money = 0;
		for(int i = 0; i < listHangNhap.size(); i++) {
			money += listHangNhap.get(i).getDonGia() * listHangNhap.get(i).getSoLuong();
		}
		dnh.setTongTien(money);
		
		model.addAttribute("donnhaphang", dnh);
		
		return "donnhaphang";
	}
	
	@GetMapping("/continue")
	public String continueOrder(Model model, HttpSession session) {
		model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
		model.addAttribute("listProduct", getAllMatHang() );
		return "search_product";
	}
	
	@GetMapping("/order")
	public String saveOrder(Model model, HttpSession session) {
		// lưu đơn nhập hàng
		DonNhapHang donNhapHang = (DonNhapHang) session.getAttribute("donnhaphang");
		rest.postForObject("http://localhost:8080/orders",donNhapHang, DonNhapHang.class);
		
		// cập nhật lại số lượng các mặt hàng
		List<HangNhap> listHangNhap = donNhapHang.getListHangNhap();
		for(int i = 0; i < listHangNhap.size(); i++) {
			rest.postForObject("http://localhost:8080/goods/update",
					listHangNhap.get(i), HangNhap.class);
			
		}
		// xóa khỏi session
		session.removeAttribute("donnhaphang");
		
		// quay về trang chủ
		model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
		return "home_warehouse";
	}
	
	@GetMapping("/back")
	public String backToHomeView(Model model, HttpSession session) {
		
		model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
		session.removeAttribute("donnhaphang");
		return "home_warehouse";
	}
	
	// kiểm tra hàng nhập đã tồn tại chưa
	public List<HangNhap> checkExits(HangNhap hangNhap, List<HangNhap> list){
		boolean isCheck = false;
		for(int i = 0; i < list.size(); i++) {
			if(hangNhap.getMatHang().getIdMatHang() == list.get(i).getMatHang().getIdMatHang()) {
				list.get(i).setSoLuong(list.get(i).getSoLuong() + hangNhap.getSoLuong());
				isCheck = true;
				break;
			}
		}
		if(!isCheck) {
			list.add(hangNhap);
		}
		
		return list;
	}
	
	// lấy list mặt hàng hiển thị
	public List<MatHang> getAllMatHang(){
		List<MatHang> list = new ArrayList<>();
		list = Arrays.asList(
				rest.getForObject("http://localhost:8080/goods"
						,MatHang[].class));
		return list;
	}
}
