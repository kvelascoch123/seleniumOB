import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PedidoCompra {
	Helpers helper = new Helpers();
	String urlParam;
	// CARGAR MI fichero
	
	public void generarPedidoCompra(WebDriver driver, String urlOB) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("******* PEDIDO DE COMPRA ************"); // en MUSEOS el item se llama => COMPROMISO
		System.out.println("*************************************"); // EN PROCESO
		//driver.manage().window().maximize();
		helper.sleep(6);
		urlParam = urlOB;
		//Determinar si es museos ya q pedido de compra es == COMPROMISO solo en este ambiente
		String actualUrl = driver.getCurrentUrl();
		System.out.println(actualUrl);
		String ambienteEspecifico = "museo";
		boolean resultado = actualUrl.contains(ambienteEspecifico); //determinar si es museo u otro ambiente

		if(resultado){// es un ambiente de museos
			helper.buscarVentana(driver,"Compromiso");
		}else{ // es otro ambiente si se llama el item PEDIDO DE COMPRA
			helper.buscarVentana(driver,"Pedido de Compra");
		}
		formulario(driver, urlOB);
	}
	public static void formulario(WebDriver driver, String urlOB) {	
		//Formulario de datos
		File archivo;
		FileReader fr;
		archivo = new File("ficheroCompras");
		Helpers helper = new Helpers();
		helper.cargarHtmlFormulario(driver); 
		
		//Nuevo 
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
		
		helper.sleep(2);

		int attemptsBC = 0;
		while (attemptsBC < 2) {
			try {
				// siempre despues de haber asignado un valor asigno el tercero pues su etiqueta cambia
				helper.sleep(1);
				WebElement inpDate = driver.findElement(By.xpath("//input[@name='ssflEnddate_dateTextField' and @class='OBFormFieldDateInput']")); // agregar
				inpDate.clear();
				Date myDate = new Date();
				inpDate.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
				inpDate.sendKeys(Keys.ENTER);	
				break;
			} catch (Exception e) {
			}
			attemptsBC++;
		}
		int attempts = 0;
		while (attempts < 2) {
			try {
				String[] paymentMethod = {"CHEQUE", "CRUCE", "TARJETA", "CONTADO"};
				Random r=new Random();
				int randomNumber=r.nextInt(paymentMethod.length);
				helper.sleep(1);
				WebElement inpPayment = driver.findElement(By.xpath("//*[@name='paymentMethod' and @class='OBFormFieldSelectInput']"));
				// cargando...
				inpPayment.sendKeys(paymentMethod[randomNumber]);
				System.out.println("Linea asignada: " + paymentMethod[randomNumber]);
				inpPayment.sendKeys(Keys.ENTER);

				break;
			} catch (Exception e) {
			}
			attempts++;
		}
		int attemptsB = 0;
		while (attemptsB < 2) {
			try {
				// siempre despues de haber asignado un valor asigno el tercero pues su etiqueta cambia
				helper.sleep(1);
				WebElement tercero = driver.findElement(By.xpath("//input[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']")); // agregar
				tercero.clear();
				tercero.sendKeys("SIDESOFT CIA LTDA");
				tercero.sendKeys(Keys.ENTER);	
				helper.sleep(1);
				helper.myScroll(driver, "//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span");
				helper.sleep(1);
				WebElement checkInvoice = driver.findElement(By.xpath("//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span"));
				if(checkInvoice.isSelected()){
					System.out.println("No presupuestable ya habilitado");
				}else{
					checkInvoice.click(); // habilitar e invoice
					System.out.println(" Habilitando no presupuestable");
				}
				helper.sleep(1);

				helper.myScroll(driver, "//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']");
				helper.sleep(1);
				WebElement inpActividad = driver.findElement(By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']")); // agregar
				inpActividad.clear();
				inpActividad.sendKeys("N/A");
				inpActividad.sendKeys(Keys.ENTER);
				
				break;
			} catch (Exception e) {
			}
			attemptsB++;
		}
		validarFormulario(driver);
		System.out.println("Llenando formulario de datos pedido de compra:");
	}
	
	public static void validarFormulario(WebDriver driver) {// validar formulario completo al aparecer el btn REGISTRO
		System.out.println("Guardando formulario...");
		Helpers helper = new Helpers();
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		helper.sleep(2); // Guradado automatico al moverme a lineas
		
			if (driver.findElements(By.xpath("//*[text()='Create One']")).size() > 0) {
				System.out.println("Proceso crear pedido...");
				agregarLineas(driver);
				}
	} 
	public static void agregarLineas(WebDriver driver) { // agregar productos al pedido de venta
		System.out.println("Asignar Lineas al formulario.....");
		Helpers helper = new Helpers();
		helper.sleep(3); // cargando...
		int attemptsSS = 0;
		while (attemptsSS < 2) {
			try {
				driver.findElement(By.xpath("//*[@class='OBToolbar']//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();//

				break;
			} catch (Exception e) {
			}
			attemptsSS++;
		}
		helper.sleep(5);
		
		String[] lineas = {"1523101001-AGUA POTABLE-332", "1523209001-SERVICIOS DE ASEO-332", "1521106001-SALARIOS UNIFICADOS-332", "1523607001-SERVICIOS TECNICOS ESPECIALIZADOS-344"};
		Random r=new Random();
		int randomNumber=r.nextInt(lineas.length);
		WebElement inpProducto = driver.findElement(By.xpath("//*[@class='OBTabSetChild']//div[@class='OBTabSetChildContainer']//*[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//*[@role='presentation']//table[@role='presentation']//td//table[@role='presentation']//input[@name='product']"));
		// cargando...
		inpProducto.sendKeys(lineas[randomNumber]);
		System.out.println("Linea asignada: " + lineas[randomNumber]);
		helper.sleep(1);	
		int attemptsB = 0;
		while (attemptsB < 3) {
			try {
				int valorCantidad = (int) Math.floor(Math.random()*6+1); // 1 a 7
				helper.sleep(2); 
				WebElement inpCantidad = driver
						.findElement(By.xpath("//*[@name='orderedQuantity' and @class='OBFormFieldNumberInputRequired']"));
				//inpCantidad.clear();
				inpCantidad.sendKeys(Integer.toString(valorCantidad));
				helper.myScroll(driver, "//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span");
				helper.sleep(1);
				WebElement checkInvoice = driver.findElement(By.xpath("//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span"));
				if(checkInvoice.isSelected()){
					System.out.println("No presupuestable ya habilitado");
				}else{
					checkInvoice.click(); // habilitar e invoice
					System.out.println(" Habilitando no presupuestable");
				}
				break;
			} catch (Exception e) {
			}
			attemptsB++;
		}
		
	}
}
