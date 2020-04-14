import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PedidoCompra {
	static Helpers helper = new Helpers();
	String urlParam;
	// CARGAR MI fichero
	
	public void generarPedidoCompra(WebDriver driver, String urlOB, int numeroPruebas) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("******* PEDIDO DE COMPRA ************"); // en MUSEOS el item se llama => COMPROMISO
		System.out.println("*************************************"); // EN PROCESO
		driver.manage().window().maximize();
		helper.sleep(6);
		
		//Determinar si es museos ya q pedido de compra es == COMPROMISO solo en este ambiente
		String actualUrl = driver.getCurrentUrl();
		String ambienteEspecifico = "mus";
		boolean resultado = actualUrl.contains(ambienteEspecifico); //determinar si es museo u otro ambiente
		if(resultado){// es un ambiente de museos
			helper.buscarVentana(driver,"Pedido de Compra");
			for (int i = 1; i <= numeroPruebas; i++) {
				formulario(driver, urlOB);
			}
			
		}else{ // es otro ambiente si se llama el item PEDIDO DE COMPRA
			helper.buscarVentana(driver,"Pedido de Compra");
			for (int i = 1; i <= numeroPruebas; i++) {
				formulario(driver, urlOB);
			}
		}
	
		}
	
	public static void formulario(WebDriver driver, String urlOB) {	

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

		int attemptsB = 0;
		while (attemptsB < 2) {
			try {
				// siempre despues de haber asignado un valor asigno el tercero pues su etiqueta cambia
				helper.sleep(1);
				WebElement tercero = driver.findElement(By.xpath("//input[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']")); // agregar
				tercero.clear();
				tercero.sendKeys("SIDESOFT CIA LTDA");
				tercero.sendKeys(Keys.ENTER);	
				String[] paymentMethod = {"CHEQUE", "EFECTIVO"};
				Random r=new Random();
				int randomNumber=r.nextInt(paymentMethod.length);
				helper.sleep(1);
				WebElement inpPayment = driver.findElement(By.xpath("//*[@name='paymentMethod' and @class='OBFormFieldSelectInput']"));
				inpPayment.clear();
				inpPayment.sendKeys(paymentMethod[randomNumber]);
				System.out.println("Linea asignada: " + paymentMethod[randomNumber]);
				inpPayment.sendKeys(Keys.ENTER);

				helper.sleep(1);
				helper.myScroll(driver, "//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span");
				helper.sleep(1);
				WebElement checkInvoice = driver.findElement(By.xpath("//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span"));
				if(checkInvoice.isSelected()){
					System.out.println("No presupuestable ya habilitado");
				}else{
					checkInvoice.click(); // habilitar e invoice
					System.out.println(" //*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']\"Habilitando no presupuestable");
				}
			
				
				break;
			} catch (Exception e) {
			}
			attemptsB++;
		}
		
		int attemptsD = 0;
		while (attemptsD < 2) {
			try {
				// LEYENDO DATOS DEL JSON
				JSONParser parser = new JSONParser();
				Object obj;
				try {
					obj = parser.parse(new FileReader("pedidoCompras.json"));
					JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
					JSONObject innerObjectActividad = (JSONObject) jsonObject.get("Actividades");// key padre
					int valorActividad = (int) Math.floor(Math.random()*innerObjectActividad.size()+1); //cualquier tipo de actividades
					JSONObject posicionActividad = (JSONObject) innerObjectActividad.get(""+valorActividad+"");// posicion data tercero
					System.out.println(posicionActividad);
					helper.sleep(1);
				helper.myScroll(driver, "//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']");
				helper.sleep(1);
				
				WebElement inpActividad = driver.findElement(By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']")); // agregar
				inpActividad.clear();
				inpActividad.sendKeys((String) posicionActividad.get("Actividad"));
				inpActividad.sendKeys(Keys.ENTER);

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
			} catch (Exception e) {
			}
			attemptsD++;
		}
		
		
		
		
		validarFormulario(driver, urlOB);
		System.out.println("Llenando formulario de datos pedido de compra:");
	}
	
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar formulario completo al aparecer el btn REGISTRO
		System.out.println("Guardando formulario...");
		Helpers helper = new Helpers();
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		helper.sleep(2); // Guradado automatico al moverme a lineas
		
			if (driver.findElements(By.xpath("//*[text()='Create One']")).size() > 0) {
				System.out.println("Proceso crear pedido...");
				agregarLineas(driver, UrlParam);
				}
	} 
	public static void agregarLineas(WebDriver driver, String UrlParam) { // agregar productos al pedido de venta
		System.out.println("Lineas del pedido de venta: ");
		System.out.println("*** DATOS DEL FORMULARIO LINEAS.***");
		helper.sleep(2); // cargando...

		int attemptsSS = 0;
		while (attemptsSS < 2) {
			try {
				driver.findElement(By.xpath("//div[@eventproxy='isc_OBToolbarIconButton_0']")).click();

				break;
			} catch (Exception e) {
			}
			attemptsSS++;
		}
		// LEYENDO DATOS DEL JSON
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("pedidoCompras.json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectLineas = (JSONObject) jsonObject.get("Lineas");// key padre
					//REPETIR PROCESO PARA ASIGNAR VARIOS PRODUCTOS - LINES
			int cantidadLineas = (int) Math.floor(Math.random()*3+1); //cualquier tipo de actividades

				int attemptsBA = 0;
				while (attemptsBA < 2) {
					try {
						//busacr numero aleatorio de la posicion lineas
						int valorCantidad = (int) Math.floor(Math.random()*innerObjectLineas.size()+1); // 1 a 7
						JSONObject posicionLineas = (JSONObject) innerObjectLineas.get(""+valorCantidad+"");// posicion data tercero
					
						helper.sleep(2);
						WebElement inpProducto = driver.findElement(By.xpath("//*[@class='OBTabSetChild']//div[@class='OBTabSetChildContainer']//*[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//*[@role='presentation']//table[@role='presentation']//tr[1]//td//table[@role='presentation']//input[@name='product']"));
						//"//*[@class='OBTabSetChild']//div[@class='OBTabSetChildContainer']//*[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//*[@role='presentation']//table[@role='presentation']//td//table[@role='presentation']//input[@name='product']"));
						helper.sleep(1);
						inpProducto.sendKeys((String) posicionLineas.get("Producto"));
						System.out.println("Linea asignada: " + (String) posicionLineas.get("Producto"));
						helper.sleep(1);
						break;
					} catch (Exception e) {
					}
					attemptsBA++;
				}
				
				//cantidad 
				int attemptsB = 0;
				while (attemptsB < 2) {
					try {
						int valorCantidadProduct = (int) Math.floor(Math.random()*6+1); // 1 a 7
						helper.sleep(2); 
						WebElement inpCantidad = driver
								.findElement(By.xpath("//*[@name='grossUnitPrice']"));
						inpCantidad.clear();
						helper.sleep(2); 
						inpCantidad.sendKeys(""+valorCantidadProduct+"");
						//if(i < cantidadLineas){
							inpCantidad.sendKeys(Keys.ENTER);	
						//}
						System.out.println("Cantidad: " + valorCantidadProduct);
						break;
					} catch (Exception e) {
					}
					attemptsB++;
				}
				helper.sleep(1);
				helper.myScroll(driver, "//div[@class=\"OBTabSetChild\"]//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span");
				helper.sleep(1);
				WebElement checkInvoice = driver.findElement(By.xpath("//div[@class=\"OBTabSetChild\"]//*[@class='OBFormFieldLabel' and text()='No Presupuestable']//span"));
				if(checkInvoice.isSelected()){
					System.out.println("No presupuestable ya habilitado");
				}else{
					checkInvoice.click(); // habilitar e invoice
					System.out.println(" //*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']\"Habilitando no presupuestable");
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
	// VALIDAR EL ERROR Y VOLVER A EJECUTAR EL PROCESO
	helper.sleep(2);
	if(driver.findElements(By.xpath("//*[text()='Error']")).size() >= 1) {
		// VALIDAR CAMBIAR LA CEDULA
        System.out.println("Error en las lineas, cancelar lineas pedidas y volver agregar lineas");
        driver.findElement(By.xpath("//a[text()='cancel all pending changes']")).click();
        agregarLineas(driver, UrlParam);
		}else {
			helper.sleep(2);
			guardarFormulario(driver, UrlParam);
		}
	}
	
	public static void guardarFormulario(WebDriver driver, String UrlParam) {

		helper.sleep(1);
		driver.findElement(By.xpath("//*[text()='Descuentos']")).click(); // me voy a descuentos y se guarda automatico ahora solo registro las lineas
		helper.sleep(1);

			driver.findElement(By.xpath("//*[@class='OBToolbarTextButtonParent' and text()='egistrar']")).click();
			helper.sleep(2);
			// TOMAR CONTROL DE MIS IFRAMES 2 y frame
			// Encontro el modal...asigne datos
			// AQUI, tengo q entrar a 2 iframes y a 1 frame para tomar el control del MODAL		
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME 2
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='mainframe']"))); // FRAME 1
			helper.sleep(1);
			driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']")).click();
			helper.sleep(2);
			//addPayment(driver, UrlParam);
		
	
	}
}
