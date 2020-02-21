import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Helpers {
	public void sleep(int seconds){
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buscarVentana(WebDriver driver, String proceso) throws InterruptedException { // abrir ventana Pedido de venta
		if(driver.findElements(By.xpath("//img[@name='isc_Bmain']")).size() <= 0) {
			Thread.sleep(7 * 1000);
		}
		WebElement aplication = driver.findElement(By.xpath("//img[@name='isc_Bmain']"));
		aplication.click();
		Thread.sleep(1000);
		WebElement valor = driver.findElement(By.xpath("//input[@name='value']"));
		valor.sendKeys(proceso);
		Thread.sleep(2000);
		valor.sendKeys(Keys.ENTER);
	}
	
	public void myScroll(WebDriver driver, String xpath){
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 js.executeScript("window.scrollBy(0,1000)");
	        WebElement Element = driver.findElement(By.xpath(xpath));
	        //This will scroll the page till the element is found		
	        js.executeScript("arguments[0].scrollIntoView();", Element);
			sleep(2);
	}
	
	public void cargarHtmlFormulario(WebDriver driver) { // Factura (Cliente)
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			try {
				Thread.sleep(8500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Tengo q esperar q cargue el html");
		} 
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	} 

}
