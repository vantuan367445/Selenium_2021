package testnhapdonhang;



import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.AfterTest;

import main.controller.HangNhapController;



public class TestDonNhapHang {
	
	public static  String baseUrl = "http://localhost:8081/";
	public static  WebDriver driver;
	public static CreateDAO dao;
	
	
	@BeforeEach
	public void initTest() {
		System.out.println("initTest");
		dao = new CreateDAO();
		System.setProperty("webdriver.chrome.driver", "C:\\browserdrives\\chromedriver.exe");
		driver =  new ChromeDriver();
		driver.get(baseUrl);
		WebElement tenDN = driver.findElement(By.name("tenDangNhap"));
		WebElement passs = driver.findElement(By.name("matKhau"));
		tenDN.sendKeys("tuan1234");
		passs.sendKeys("tuan1234");
		WebElement btnClick = driver.findElement(By.name("btn_submit"));
		btnClick.click();
		
	}
	@AfterEach
	public void closeDrive() {
		driver.close();
	}

	@Test // expected =  Pass // result  = Pass
	public void addDonNhapHangSuccessful() throws Exception {

		Connection connect = dao.getConnection();
		PreparedStatement pre = connect.prepareStatement("SELECT COUNT(*) FROM tbl_don_nhap_hang;");;
		ResultSet rs = pre.executeQuery();
		rs.next();
	    int count = rs.getInt(1);
	    System.out.println("a1 = " + count);
	    
	      
		WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
		btnClickNhapHang.click();
		
		WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

		ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
		int rows_count = rows_table.size();
		
		Thread.sleep(1500);
		
		if(rows_table.size() > 0) {
			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);

			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click();
			Thread.sleep(1500); // click Nhập hàng
			
			driver.findElement(By.xpath("html/body/a[2]")).click(); // click Lưu hóa đơn nhập
			Thread.sleep(2000);
			
			ResultSet rs2 = pre.executeQuery();
			rs2.next();
			int count2 = rs2.getInt(1);
			System.out.println("a2 = " + count2);
			assertTrue((count+1) == count2);
		}
		Thread.sleep(1000);
}
	
	
	@Test // expected =  Fail // result  = Pass
	public void addDonNhapHangWithHangNhapDonGiaAm() throws Exception {

		Connection connect = dao.getConnection();
		PreparedStatement pre = connect.prepareStatement("SELECT COUNT(*) FROM tbl_don_nhap_hang;");;
		ResultSet rs = pre.executeQuery();
		rs.next();
	    int count = rs.getInt(1);
	    System.out.println("a1 = " + count);
	    
	      
		WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
		btnClickNhapHang.click();
		
		WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));
		System.out.println("1 = "+table.getText());

		ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
		int rows_count = rows_table.size();
		
		Thread.sleep(1500);
		
		if(rows_table.size() > 0) {
			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);

			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);
			
			driver.findElement(By.id("donGia")).clear();
			driver.findElement(By.id("donGia")).sendKeys("-12");
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click();
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("html/body/a[2]")).click(); // click Lưu hóa đơn nhập
			Thread.sleep(2000);
			
			ResultSet rs2 = pre.executeQuery();
			rs2.next();
			int count2 = rs2.getInt(1);
			System.out.println("a2 = " + count2);
			assertTrue(count == count2);
		}
		Thread.sleep(1000);
	}
		
		@Test //expected =  Fail // result  = Fail
		public void addDonNhapHangWithHangNhapSLuongAm() throws Exception {
		      
			WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
			btnClickNhapHang.click();
			
			WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

			ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
			int rows_count = rows_table.size();
			
			Thread.sleep(1500);
			
			if(rows_table.size() > 0) {
				driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
				Thread.sleep(1500);

				driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
				Thread.sleep(1500);
				
				driver.findElement(By.id("soLuong")).clear();
				driver.findElement(By.id("soLuong")).sendKeys("-1");
				Thread.sleep(1500);
				
				
				driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click(); // click nhập hàng
				Thread.sleep(1500);
				
				String errorMessage = driver.findElement(By.id("soLuong")).getAttribute("validationMessage");
				
				assertEquals(errorMessage,"Value must be greater than or equal to 1.");
			}
			Thread.sleep(1000);
	}
		
		@Test // expected =  Pass // result  = Pass
		public void addDonNhapHangWithHangNhapDonGia_Empty() throws Exception {

			Connection connect = dao.getConnection();
			PreparedStatement pre = connect.prepareStatement("SELECT COUNT(*) FROM tbl_don_nhap_hang;");;
			ResultSet rs = pre.executeQuery();
			rs.next();
		    int count = rs.getInt(1);
		    System.out.println("a1 = " + count);
		    
		      
			WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
			btnClickNhapHang.click();
			
			WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

			ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
			int rows_count = rows_table.size();
			
			Thread.sleep(1500);
			
			if(rows_table.size() > 0) {
				driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
				Thread.sleep(1500);

				driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
				Thread.sleep(1500);
				
				driver.findElement(By.id("donGia")).clear();
				driver.findElement(By.id("donGia")).sendKeys("");
				Thread.sleep(1500);
				
				
				driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click();
				Thread.sleep(1500);
				
				
				WebElement result = driver.findElement(By.xpath("/html/body/h1")); // click Lưu hóa đơn nhập
				Thread.sleep(2000);
				
				System.out.print("a = " + result.getText());
			
				assertEquals(result.getText(),"Whitelabel Error Page");
			}
			Thread.sleep(1000);
		}
		
		
		
	@Test //expected =  Pass // result  = Pass
	public void addDonNhapHangWithHangNhapSLuong_Empty() throws Exception {
	      
		WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
		btnClickNhapHang.click();
		
		WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

		ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
		int rows_count = rows_table.size();
		
		Thread.sleep(1500);
		
		if(rows_table.size() > 0) {
			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);

			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);
			
			driver.findElement(By.id("soLuong")).clear();
			driver.findElement(By.id("soLuong")).sendKeys("");
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click(); // click nhập hàng
			Thread.sleep(1500);
			
			WebElement result = driver.findElement(By.xpath("/html/body/h1")); // click Lưu hóa đơn nhập
			Thread.sleep(2000);
			
			System.out.print("a = " + result.getText());
		
			assertEquals(result.getText(),"Whitelabel Error Page");
			
		}
		Thread.sleep(1000);
}

	@Test // expected =  Pass // result  = Pass
	public void addDonNhapHangWithHangNhapDonGia_Lon() throws Exception {

		Connection connect = dao.getConnection();
		PreparedStatement pre = connect.prepareStatement("SELECT COUNT(*) FROM tbl_don_nhap_hang;");;
		ResultSet rs = pre.executeQuery();
		rs.next();
	    int count = rs.getInt(1);
	    System.out.println("a1 = " + count);
	    
	      
		WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
		btnClickNhapHang.click();
		
		WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

		ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
		int rows_count = rows_table.size();
		
		Thread.sleep(1500);
		
		if(rows_table.size() > 0) {
			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);

			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);
			
			driver.findElement(By.id("donGia")).clear();
			driver.findElement(By.id("donGia")).sendKeys("111111111111111");
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click();
			Thread.sleep(1500);
			
			
			WebElement result = driver.findElement(By.xpath("/html/body/h1")); // click Lưu hóa đơn nhập
			Thread.sleep(2000);
			
			System.out.print("a = " + result.getText());
		
			assertEquals(result.getText(),"Whitelabel Error Page");
		}
		Thread.sleep(1000);
	}
	
	@Test //expected =  Pass // result  = Pass
	public void addDonNhapHangWithHangNhapSLuong_Lon() throws Exception {
	      
		WebElement btnClickNhapHang = driver.findElement(By.xpath("/html/body/div[3]/a[2]"));
		btnClickNhapHang.click();
		
		WebElement table = driver.findElement(By.xpath("/html/body/div[3]/table"));

		ArrayList<WebElement> rows_table = (ArrayList<WebElement>) table.findElements(By.tagName("tbody"));
		int rows_count = rows_table.size();
		
		Thread.sleep(1500);
		
		if(rows_table.size() > 0) {
			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);

			driver.findElement(By.xpath("/html/body/div[3]/table/tbody/tr/td[1]/a")).click();
			Thread.sleep(1500);
			
			driver.findElement(By.id("soLuong")).clear();
			driver.findElement(By.id("soLuong")).sendKeys("1111111111111");
			Thread.sleep(1500);
			
			
			driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/button")).click(); // click nhập hàng
			Thread.sleep(1500);
			
			WebElement result = driver.findElement(By.xpath("/html/body/h1")); // click Lưu hóa đơn nhập
			Thread.sleep(2000);
			
			System.out.print("a = " + result.getText());
		
			assertEquals(result.getText(),"Whitelabel Error Page");
			
		}
		Thread.sleep(1000);
}
	
}
