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

import main.model.HangNhap;
import main.model.MatHang;
import main.model.NhaCungCap;
import main.model.NhanVien;

@Controller
@RequestMapping("/products")
public class MatHangController {
	private RestTemplate rest = new RestTemplate();
	@Autowired
	public MatHangController() {
	}
	
	// Mở giao diện thêm mới mặt hàng
	@GetMapping("/add")
	public String openViewAddProduct(Model model, HttpSession session) {
		MatHang matHang = new MatHang();
		matHang.setSoLuong(0);
		matHang.setGiaNhap(0);
		matHang.setGiaBan(1);
		
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		model.addAttribute("mathang", matHang);
		return "new_product";
	}
	
	/*
	 * Mở giao diện tìm kiếm để sửa mặt hàng
	 */
	@GetMapping("/edit")
	public String openViewEditProduct(Model model, HttpSession session) {
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		model.addAttribute("listProduct", getAllMatHang() );
		return "search_product";
	}
	/*
	 * Mở giao diện tìm kiếm để xóa mặt hàng
	 */
	@GetMapping("/delete")
	public String openViewDeleteProduct(Model model, HttpSession session) {
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		model.addAttribute("listProduct", getAllMatHang() );
		return "search_product";
	}
	
	/*
	 * Mở giao diện sửa/xóa mặt hàng nếu là nv quản lý
	 * Mở giao diện nhập hàng nếu là nv kho
	 * 
	 */
	@GetMapping("/{id}")
	public String openViewProduct(@PathVariable(name = "id") int id,
			Model model, HttpSession session) {
		MatHang matHang = rest.getForObject("http://localhost:8080/goods/{id}",MatHang.class, id);
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		String url = "";
		if(nv.getViTriCongViec().equals("Nhân viên quản lý")) {
			model.addAttribute("nhanvien", nv);
			model.addAttribute("mathang", matHang);
			url = "edit_delete_product";
		}
		if(nv.getViTriCongViec().equals("Nhân viên kho")) {
			HangNhap hn = new HangNhap();
			hn.setMatHang(matHang);
			hn.setDonGia(matHang.getGiaNhap());
			hn.setSoLuong(1);
			NhaCungCap ncc = (NhaCungCap) session.getAttribute("nhacungcap");
			
			model.addAttribute("nhacungcap", ncc);
			model.addAttribute("nhanvien", nv);
			model.addAttribute("hangnhap", hn);
			
			url = "hangnhap";
		}
		
		return url;
	}
	
	
	/*
	 *Lưu mặt hàng sau khi sửa 
	 *
	 */
	@PostMapping("/edit")
	public String editProduct(@ModelAttribute("mathang") MatHang matHang, 
			Model model,HttpSession session ) {
		
		matHang.setActive(1);
		matHang.setSoLuong(0);
		rest.postForObject("http://localhost:8080/goods",matHang, MatHang.class);
		
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		return "product_management";
	}
	
	/*
	 * Tìm kiếm mặt hàng
	 */
	@GetMapping(path = "/search")
	private String searchProduct(Model model, HttpSession session,
			@ModelAttribute("inputSearch") String name) {
		if(name.equals("")) {
			model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
			model.addAttribute("listProduct", getAllMatHang() );
			return "search_product";
		}
		List<MatHang> listProduct = new ArrayList<MatHang>();
		listProduct = Arrays.asList(
				rest.getForObject("http://localhost:8080/goods/search/{name}"
						,MatHang[].class, name));
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("nhanvien", (NhanVien) session.getAttribute("nhanvien"));
		return "search_product";
	}
	
	
	
	/*
	 * Lưu mặt hàng sau khi thêm
	 */
	@PostMapping("/add")
	public String addNewProduct(@ModelAttribute("mathang") MatHang matHang, 
			Model model,HttpSession session ) {
		
		boolean isValid = false;
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		
		// check validate
		if(matHang.getTenMatHang().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_name", "Tên mặt hàng không được để trống!");
		}
		if(matHang.getLoaiMatHang().trim().equals("")) {
			isValid = true;
			model.addAttribute("error_type", "Loại mặt hàng không được để trống!");
		}
		if(matHang.getGiaBan() <= matHang.getGiaNhap()) {
			isValid = true;
			model.addAttribute("error_price", "Giá bán phải lớn hơn giá nhập!");
		}
		
		if(isValid) {
			return "new_product";
		}
		
		// add product
		matHang.setActive(1);
		matHang.setSoLuong(0);
		rest.postForObject("http://localhost:8080/goods",matHang, MatHang.class);
		
		//check staff
		// kiểm tra nếu là nv kho thì quay về giao diện search mặt hàng
		// nếu là nv ql thì quay về giao diện ql
		if(nv.getViTriCongViec().equals("Nhân viên quản lý")) {
			return "product_management";
		}
		
		model.addAttribute("listProduct", getAllMatHang() );
		return "search_product";
	}
	
	/*
	 * Xóa mặt hàng
	 */
	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") int id,
			Model model, HttpSession session) {
		System.out.print("Xóa mặt hàng");
		rest.delete("http://localhost:8080/goods/delete/{id}", id);
		NhanVien nv = (NhanVien) session.getAttribute("nhanvien");
		model.addAttribute("nhanvien", nv);
		return "product_management";
	}
	
	public List<MatHang> getAllMatHang(){
		List<MatHang> list = new ArrayList<>();
		list = Arrays.asList(
				rest.getForObject("http://localhost:8080/goods"
						,MatHang[].class));
		return list;
	}
	
	
}
