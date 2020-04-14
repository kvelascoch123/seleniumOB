import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class Terceros {
	public static void crearTerceros(WebDriver driver, String urlParam, String nombreTercero) throws InterruptedException {
		System.out.println("*************************************");
		System.out.println("******* Creación de Terceros ********"); 
		System.out.println("*************************************");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("terceros.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject innerObject = (JSONObject) jsonObject.get("Terceros");
		Thread.sleep(7000);
		for (int i = 1; i < innerObject.size(); i++) {
					
		if(driver.findElements(By.xpath("//*[@id='isc_F']")).size() <= 0){
			Thread.sleep(7000);
		}
		driver.findElement(By.xpath("//*[@id='isc_F']")).click();
		driver.findElement(By.xpath("//*[text()='Gestión de Datos Maestros']")).click(); 
		driver.findElement(By.xpath("//nobr[text()='Terceros']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
		System.out.println("Datos del tercero: ");
		// Llenar datos del usuario
		/**************CARGANDO DATOS DEL USUARIO DEL TXT ************/
		formulario(driver,urlParam, i);
		}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // lee archivo
		}
		
	// FORMULARIO CABECERA CREACION DE TERCEROS
	public static void formulario(WebDriver driver, String UrlParam, int contadorJSON) throws InterruptedException {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("terceros.json"));
			JSONObject jsonObject = (JSONObject) obj; 
			JSONObject innerObject = (JSONObject) jsonObject.get("Terceros");
			JSONObject posicion = (JSONObject) innerObject.get(""+contadorJSON+"");

			//******************JSON***************************
			int attemptsX = 0;
			while (attemptsX < 2) {
				try {	
					Thread.sleep(1500);
					WebElement organization = driver.findElement(By.xpath("//input[@name='organization'  and @oninput='isc_OBFKComboItem_0._handleInput()' ]")); // agregar
					organization.clear();
					organization.sendKeys("*"); // Valor por defecto
					organization.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsX++;
			}
		
				// PUEDE ASIGNAR DATOS DEL TXT AL FORMULARIO
				int attemptsA = 0;
				while (attemptsA < 2) {
					try {	
						Thread.sleep(1500);
						WebElement identificador = driver.findElement(By.xpath("//input[@name='searchKey' and @oninput='isc_OBTextItem_0._handleInput()']")); 
						int count = driver.findElements(By.xpath("//input[@name='searchKey' and @oninput='isc_OBTextItem_0._handleInput()']")).size();
						System.out.println(count);
						identificador.clear();
						identificador.sendKeys((String) posicion.get("DNI")); // CAST to STRiING
						identificador.sendKeys(Keys.ENTER);
						break;
					} catch (Exception e) {
					}
					attemptsA++;
				}
				
				int attemptsB = 0;
				while (attemptsB < 2) {
					try {	
						Thread.sleep(1500);
						WebElement name1 = driver.findElement(By.xpath("//input[@name='name' and @oninput='isc_OBTextItem_1._handleInput()']")); // agregar
						name1.clear();
						name1.sendKeys((String) posicion.get("Nombre"));
						name1.sendKeys(Keys.ENTER);
						break;
					} catch (Exception e) {
					}
					attemptsB++;
				}
				int attemptsC = 0;
				while (attemptsC < 2) {
					try {	
						Thread.sleep(1500);
						WebElement name2 = driver.findElement(By.xpath("//input[@name='name2' and @oninput='isc_OBTextItem_2._handleInput()']")); // agregar
						name2.clear();
						name2.sendKeys((String) posicion.get("Nombre"));
						name2.sendKeys(Keys.ENTER);
						break;
					} catch (Exception e) {
					}
					attemptsC++;
				}
				int attemptsD = 0;
				while (attemptsD < 2) {
					try {	
						Thread.sleep(1500);
						WebElement cif = driver.findElement(By.xpath("//input[@name='taxID' and @oninput='isc_OBTextItem_4._handleInput()']")); // agregar
						cif.clear();
						cif.sendKeys((String) posicion.get("DNI"));
						cif.sendKeys(Keys.ENTER);
						break;
					} catch (Exception e) {
					}
					attemptsD++;
				}
				int attemptsE = 0;
				while (attemptsE < 2) {
					try {	
						Thread.sleep(2000);
						WebElement referenceNo = driver.findElement(By.xpath("//input[@name='referenceNo' and @oninput='isc_OBTextItem_6._handleInput()']")); // agregar
						referenceNo.clear();
						referenceNo.sendKeys(".");
						referenceNo.sendKeys(Keys.ENTER);
						break;
					} catch (Exception e) {
					}
					attemptsE++;
				}
				System.out.println("DNI: "+ posicion.get("DNI"));
				System.out.println("Nombre : "+ posicion.get("Nombre"));
				System.out.println("Email: "+ posicion.get("Email"));
				System.out.println("Direccion: "+ posicion.get("Direccion"));
				
			confirmarTerceroDatos(driver,UrlParam, posicion);
			// FORMULARIO FINALIZADO

			
			//******************END - JSON***************************

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // lee archivo
		
	}
	public static void confirmarTerceroDatos(WebDriver driver ,String UrlParam, JSONObject posicion) throws InterruptedException {
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_save OBToolbarIconButton']")).click();
		Thread.sleep(2000);
	 	if(driver.findElements(By.xpath("//*[text()='Error']")).size() >= 1) {
			// VALIDAR CAMBIAR LA CEDULA
	        JOptionPane.showMessageDialog(null, "Aun no validas si exsiste error por DNI ya encontrado o erroneo");

		}else {
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive']")).click();
			Thread.sleep(2000); 
			int attemptsEm = 0;
			while (attemptsEm < 2) {
						// PUEDE ASIGNAR DATOS DEL TXT AL FORMULARIO
					Helpers helper = new Helpers();
					Thread.sleep(2000);
					helper.myScroll(driver,"//*[@name='eEIEmail']");
					WebElement email = driver.findElement(By.xpath("//*[@name='eEIEmail']")); // agregar tercero
					email.clear();
					email.sendKeys((String) posicion.get("Email"));

					break;
			}
				attemptsEm++;
			}
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleInactive' and text()='Direcciones']")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='Create One']")).click();//
			Thread.sleep(2000);
			WebElement asigDirecion = driver.findElement(By.xpath("//*[@src='"+UrlParam+"/web/org.openbravo.userinterface.smartclient/openbravo/skins/Default/org.openbravo.client.application/images/form/search_picker.png' and @style=';margin-top:0px;margin-bottom:0px;margin-left:0px;display:block;']"));
			asigDirecion.click();
			Thread.sleep(3000);
			
				// Encontro el modal...asigne datos
				// AQUI, tengo q entrar a 2 iframes y a 1 frame para tomar el control del MODAL		
				driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
				driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='SELECTOR']"))); // IFRAME 2
				driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='superior']"))); // FRAME 1
					int attemptsA = 0;
					while (attemptsA < 2) {
						try {	
							driver.findElement(By.xpath("//*[@name='inpAddress1']")).sendKeys((String) posicion.get("Direccion1"));
							Thread.sleep(1000);
							driver.findElement(By.xpath("//*[@name='inpAddress2']")).sendKeys((String) posicion.get("Direccion2"));
							Thread.sleep(2000);
							break;
						} catch (Exception e) { 
						}
						attemptsA++;   
					}			 
				Thread.sleep(1000);
				driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']")).click();
				 	
	 	validarMoneda(driver, UrlParam);
		}
	   
	 
	public static void validarMoneda(WebDriver driver, String UrlParam) throws InterruptedException {
		// HTML PRINCIPAL
		Thread.sleep(10000); 
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_save OBToolbarIconButton']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@class='OBToolbarTextButtonParent' and text()='et New Currency']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@name='C_Currency_ID']")).sendKeys("USD");
		Thread.sleep(2000);
		WebElement checkSetAmount = driver.findElement(By.xpath("//*[@class='OBFormFieldLabelRequired']//span"));
		if(checkSetAmount.isSelected()){
			System.out.println("Moneda USD habilitada");
		}else{
			checkSetAmount.click(); // habilitar e invoice
			System.out.println(" Habilitando moneda USD");
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@class='OBFormButton' and text()='Done']")).click();
		Thread.sleep(2000);
		driver.navigate().to(UrlParam);

		}

	}