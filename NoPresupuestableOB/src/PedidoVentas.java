import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class PedidoVentas {
	Helpers helper = new Helpers();
	String urlParam;
	public void pedidoDeVentas(WebDriver driver, String urlOB, int numeroPruebas) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("******** PEDIDO DE VENTAS ***********"); 
		System.out.println("*************************************");
		helper.sleep(1);
		urlParam = urlOB;
		// REPETIR EL PROCESO SEGUN EL NUMERO DE VECES DE ASIGNACION por JOPTIONPANE
		int ejecutadas = 0;
		
		for (int i = 1; i <= numeroPruebas; i++) {
			System.out.println("Número de Pedido de Venta: " + i);
			int repeticionesParam = numeroPruebas - i;
			helper.buscarVentana(driver,"Pedido de venta");
			crearPedido(driver, urlOB,repeticionesParam);
			ejecutadas = i;
		}
	}
	// CARGAR EL HTML
	public void crearPedido(WebDriver driver, String UrlParam, int numeroPruebas){ 
		helper.sleep(6);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(8);
			System.out.println("Tengo q esperar q cargue el html");
		} 
			helper.sleep(1);
			asignarDatos(driver, UrlParam, numeroPruebas); // llenar formulario			
	}
	// FORMULARIO DEL PEDIDO
	public void asignarDatos(WebDriver driver,String UrlParam, int repeticionesParam) { 
		System.out.println("Llenando datos del formulario.");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("pedidoVentas.json"));
			JSONObject jsonObject = (JSONObject) obj; 
			JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
			JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("1");// posicion data tercero
			JSONObject innerObjectActividad = (JSONObject) jsonObject.get("Actividades");// key padre
			int valorActividad = (int) Math.floor(Math.random()*innerObjectActividad.size()+1); //cualquier tipo de actividades
			JSONObject posicionActividad = (JSONObject) innerObjectActividad.get(""+valorActividad+"");// posicion data tercero

			//******************JSON***************************
			int attemptsAA = 0;
			while (attemptsAA < 2) {
				try {
					driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
					break;
				} catch (Exception e) {
				}
				attemptsAA++;
			}
			helper.sleep(1);
			int attempts = 0;
			while (attempts < 2) {
				try {
					WebElement inpDocumentoTransaccion = driver.findElement(
							By.xpath("//*[@name='transactionDocument' and @oninput='isc_OBFKComboItem_1._handleInput()']"));
					inpDocumentoTransaccion.clear();
					inpDocumentoTransaccion.sendKeys("PEDIDO DE VENTA");
					inpDocumentoTransaccion.submit();

					break;
				} catch (Exception e) {
				}
				attempts++; 
			} 
			int attemptsB = 0;
			while (attemptsB < 2) {
				try {
					helper.sleep(1);
					helper.myScroll(driver, "//*[@name='costcenter' and @class='OBFormFieldSelectInputRequired']");
					WebElement inpActividad = driver.findElement(
							By.xpath("//*[@name='costcenter' and @class='OBFormFieldSelectInputRequired']"));
					inpActividad.clear();
					inpActividad.sendKeys((String) posicionActividad.get("Actividad"));// N/A
					inpActividad.submit();
					helper.sleep(1);
					helper.myScroll(driver, "//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']");
					WebElement inpTercero = driver.findElement(
							By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
					inpTercero.clear();
					inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
					inpTercero.sendKeys(Keys.ENTER);
					helper.sleep(1);
					break;
				} catch (Exception e) {
				}
				attemptsB++;
			}
			
			System.out.println("Nombre del tercero: " + (String) posicionTerceros.get("Nombre"));
			System.out.println("Tipo de actividad: " +(String) posicionActividad.get("Actividad"));
			//VALIDANDO SI EXISTE ERROR EN FORMULARIO DATOS PEDIDOS VENTA....alun campo vacio
			helper.sleep(2);
			validarFormulario(driver, urlParam,repeticionesParam);
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
		
	

	public void validarFormulario(WebDriver driver, String urlParam, int repeticionesParam) {// validar formulario completo al aparecer el btn REGISTRO
		System.out.println("Guardando formulario...");
		helper.sleep(2); // cargando...
		
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		helper.sleep(1); // cargando...GUARDAR
		if(driver.findElements(By.xpath("//*[text()='Error']")).size() >= 1) {
			//VALIDANDO SI EXISTE ERROR EN FORMULARIO....alun campo vacio
	        System.out.println("Algun campo no fue llenado en el formulario, volviendo a realizar pedido");
			driver.navigate().to(urlParam);
			try {
				pedidoDeVentas(driver, urlParam, repeticionesParam);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//else {
		//helper.sleep(2); // cargando...GUARDAR
		//driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_save OBToolbarIconButton']")).click();
		//helper.sleep(2); // cargando...

			if (driver.findElements(By.xpath("//*[text()='Create One']")).size() > 0) {
				agregarLineas(driver, urlParam);
				}
	//} 
	}
	public void agregarLineas(WebDriver driver, String UrlParam) { // agregar productos al pedido de venta
		System.out.println("Lineas del pedido de venta: ");
		helper.sleep(1); // cargando...
		
		driver.findElement(By.xpath("//*[text()='Create One']")).click();//
		helper.sleep(2);
		// LEYENDO DATOS DEL JSON
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("pedidoVentas.json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectLineas = (JSONObject) jsonObject.get("Lineas");// key padre
					//REPETIR PROCESO PARA ASIGNAR VARIOS PRODUCTOS - LINES
			int cantidadLineas = (int) Math.floor(Math.random()*3+1); //cualquier tipo de actividades

				for (int i = 0; i <= cantidadLineas; i++) {
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
									.findElement(By.xpath("//*[@name='orderedQuantity' and @class='OBFormFieldNumberInputRequired']"));
							inpCantidad.clear();
							inpCantidad.sendKeys(""+valorCantidadProduct+"");
							if(i < cantidadLineas){
								inpCantidad.sendKeys(Keys.ENTER);	
							}
							System.out.println("Cantidad: " + valorCantidadProduct);
							break;
						} catch (Exception e) {
						}
						attemptsB++;
					}
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
	public void guardarFormulario(WebDriver driver, String UrlParam) {

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
			addPayment(driver, UrlParam);
		
	
	}
	
	public void addPayment(WebDriver driver, String UrlParam) {
		helper.sleep(1);
		driver.findElement(By.xpath("//*[text()='dd Payment'] | //*[text()='gregar Pago']")).click();
		helper.sleep(2);
		helper.myScroll(driver,"//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']");
		WebElement inpActionDocument = driver.findElement(By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']"));
		inpActionDocument.sendKeys("Process Received Payment(s) and Deposit");
		inpActionDocument.sendKeys(Keys.ENTER);
		helper.sleep(2);
		driver.findElement(By.xpath("//*[@class='OBFormButton' and text()='Done']")).click();
		System.out.println("Process Received Payment(s) and Deposit");
		System.out.println("*************************************");
		System.out.println("***** Pedido ventas realizado *******");
		System.out.println("*************************************");
		driver.navigate().to(UrlParam);
	}
}

