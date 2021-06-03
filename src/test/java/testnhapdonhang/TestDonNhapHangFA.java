package testnhapdonhang;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class TestDonNhapHangFA {
	
	public static  String baseUrl = "http://localhost:8081/";
	public static  WebDriver driver;
	
//	public static void main(String[] args) {
//		System.setProperty("webdriver.chrome.driver", "C:\\browserdrives\\chromedriver.exe");
//		driver =  new ChromeDriver();
//		driver.get(baseUrl);
//	}
	
	@BeforeEach
	public void initTest() {
		System.setProperty("webdriver.chrome.driver", "C:\\browserdrives\\chromedriver.exe");
		driver =  new ChromeDriver();
		driver.get(baseUrl);
	}
	
	@Test
	public void test1() throws InterruptedException {
		WebElement tenDN = driver.findElement(By.name("tenDangNhap"));
		WebElement passs = driver.findElement(By.name("matKhau"));
		tenDN.sendKeys("tuan1234");
		passs.sendKeys("tuan1234");
		WebElement btnClick = driver.findElement(By.name("btn_submit"));
		btnClick.click();
		
		Thread.sleep(2000);
		WebElement title = driver.findElement(By.name("title_notify"));
		assertEquals(title.getText(), "Tài khoản đăng nhập không tồn tại.");
	}
}
