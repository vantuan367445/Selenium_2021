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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import main.model.MatHang;
import main.model.NhaCungCap;
import main.model.NhanVien;

@Controller
@RequestMapping("/suppliers")
public class NhaCungCapController {
	private RestTemplate rest = new RestTemplate();
	
	@Autowired
	public NhaCungCapController() {
		
	}
	
	/*
	 * tìm kiếm nhà cung cấp
	 * @param
	 */
	@GetMapping(path = "/search")
	public String searchSupplier(@ModelAttribute("inputSearch") String name, 
			Model model, HttpSession session) {
		if(name.equals("")) {
			name = " ";
		}
		List<NhaCungCap> listSupplier = new ArrayList<NhaCungCap>();
		listSupplier = Arrays.asList(
				rest.getForObject("http://localhost:8080/suppliers/search/{name}"
						,NhaCungCap[].class, name));
		model.addAttribute("listSupplier", listSupplier);
		model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
		return "search_supplier";
	}
	
	/*
	 * Bấm vào 1 nhà cung cấp sẽ sang giao diện tìm kiếm mặt hàng để nhập
	 */
	@GetMapping("/{id}")
	public String openViewSearchProduct(@PathVariable(name = "id") int id,
			Model model, HttpSession session) {
		NhaCungCap ncc = rest.getForObject("http://localhost:8080/suppliers/{id}",NhaCungCap.class, id);
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		
		// lưu ncc vào session để dùng cho chọn sp
		if(nv.getViTriCongViec().equals("Nhân viên kho")) {
			session.setAttribute("nhacungcap", ncc);
		}
		model.addAttribute("nhanvien", nv);
		model.addAttribute("listProduct", getAllMatHang() );
		return "search_product";
	}
	
	/*
	 * Bấm vào thêm mới để mở giao diện thêm nhà cung cấp
	 * 
	 */
	@GetMapping("/new")
	public String openViewAddSupplier(Model model , HttpSession session) {
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		NhaCungCap ncc = new NhaCungCap();
		
		model.addAttribute("nhacungcap", ncc);
		model.addAttribute("nhanvien", nv);
		return "new_supplier";
	}
	/*
	 * Bấm vào nút thêm mới để thêm ncc
	 */
	@PostMapping("/add")
	public String editProduct(@ModelAttribute("nhacungcap") NhaCungCap nhaCungCap, 
			Model model,HttpSession session ) {
		// lấy nc từ session
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		
		// check Valid
		boolean isValid = false;
		if(nhaCungCap.getTenNhaCungCap().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_name","Tên nhà cung cấp không được để trống");
		}
		if(nhaCungCap.getTenChuCuaHang().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_name2","Tên chủ cửa hàng không được để trống");
		}
		if(nhaCungCap.getDiaChi().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_add","Địa chỉ nhà cung cấp không được để trống");
		}
		if(nhaCungCap.getSoDienThoai().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_phone","Số điện thoại không được để trống");
		}
		if(isValid) {
			return "new_supplier";
		}
		
		// addSupplier
		nhaCungCap.setActive(1);
		rest.postForObject("http://localhost:8080/suppliers",nhaCungCap, NhaCungCap.class);
		
		// getListSupplier
		List<NhaCungCap> listSupplier = new ArrayList<NhaCungCap>();
		listSupplier = Arrays.asList(
				rest.getForObject("http://localhost:8080/suppliers"
						,NhaCungCap[].class));
		model.addAttribute("listSupplier", listSupplier);
		
		// add to model
		model.addAttribute("listSupplier",listSupplier);
		return "search_supplier";
	}
	
	/*
	 * Lấy tất cả mặt hàng
	 */
	public List<MatHang> getAllMatHang(){
		List<MatHang> list = new ArrayList<>();
		list = Arrays.asList(
				rest.getForObject("http://localhost:8080/goods"
						,MatHang[].class));
		return list;
	}
	
}
