package main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import main.model.NhaCungCap;
import main.model.NhanVien;

@Controller
public class HomeController {

	private Environment env;
	//@Value("${JAVA_PATH}")
	//private String myVariable;
	@Autowired
	HomeController(Environment env){
		this.env = env;
	}
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/")
	public String home(Model model) {
		//System.out.println("==========> " + myVariable);
		System.out.println("+++++++++++ " + env.getProperty("JAVA_HOME"));
		model.addAttribute("nhanvien", new NhanVien());
		return "login";
	}
	
	/*
	 * @param 
	 * Sử dụng khi người dùng ở trang home_warehouse và click vào nhập hàng từ nhà cung cấp
	 */
	@GetMapping("/importproduct")
	public String openViewSearchSupplier(HttpSession session, Model model) {
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		// gửi theo danh sách nhà cung cấp
		List<NhaCungCap> listSupplier = new ArrayList<>();
		listSupplier = Arrays.asList(
				rest.getForObject("http://localhost:8080/suppliers"
						,NhaCungCap[].class));
		model.addAttribute("listSupplier", listSupplier);
		return "search_supplier";
	}
	
	/*
	 * @param 
	 * Sử dụng khi người dùng ở trang home_manager và click vào quản lý mặt hàng
	 */
	@GetMapping("/productmanagement")
	public String openViewProductManagement(HttpSession session, Model model) {
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		return "product_management";
	}
	
	@GetMapping("/logout")
	public String loginView(Model model, HttpSession session) {
		model.addAttribute("nhanvien", new NhanVien());
		session.removeAttribute("nhanvien");
		session.removeAttribute("donnhaphang");
		return "login";
	}
	
	@GetMapping("/developing")
	public String viewDeveloping() {
		return "develop";
	}
}
