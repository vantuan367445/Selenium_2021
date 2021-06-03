package main.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import main.model.NhanVien;

@Controller
@RequestMapping("")
public class NhanVienController {
	
	private RestTemplate rest = new RestTemplate();
	
	@Autowired
	public NhanVienController() {
		
	}
	
	// sau login quay về home nếu login đúng
	@PostMapping("/home")
	public String StaffLogin(@ModelAttribute("nhanvien") /*@Valid*/ NhanVien nhanvien, 
			Model model, HttpSession session, Errors errors) {
		
//		if(errors.hasErrors()) {
//			System.out.print("Errror!!!");
//			return "login";
//		}
		if(nhanvien.getTenDangNhap().equals("")) {
			model.addAttribute("er_username","Tên đăng nhập không được để trống");
			return "login";
		}
		if(nhanvien.getMatKhau().equals("")) {
			model.addAttribute("er_pass","Mật khẩu không được để trống");
			return "login";
		}
		
		nhanvien = rest.postForObject("http://localhost:8080/staffs/login",nhanvien, NhanVien.class);
		if(nhanvien.getTenDangNhap() == null) {
			model.addAttribute("er_account","Tài khoản đăng nhập không tồn tại");
			return "login";
		}
		else {
			model.addAttribute("nhanvien", nhanvien);
			session.setAttribute("nhanvien", nhanvien);
			if(nhanvien.getViTriCongViec().equals("Nhân viên quản lý")) return "home_manager";
			else {
				return "home_warehouse";
			}
			
		}
	}
	
	@GetMapping("/home")
	public String viewHome(Model model, HttpSession session) {
		NhanVien nhanvien = (NhanVien) session.getAttribute("nhanvien");
		if(nhanvien == null) {
			return "login";
		}
		if(nhanvien.getViTriCongViec().equals("Nhân viên kho")) {
			model.addAttribute("nhanvien", nhanvien);
			return "home_warehouse";
		}
		model.addAttribute("nhanvien", nhanvien);
		return "home_manager";
	}
}
