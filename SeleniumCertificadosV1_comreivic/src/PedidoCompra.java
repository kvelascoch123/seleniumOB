import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PedidoCompra {
	static Helpers helper = new Helpers();
	String urlParam;
	// CARGAR MI fichero

	public static void generarPedidoCompra(WebDriver driver, String urlOB) throws InterruptedException { // Pedido de
																											// Venta =>
																											// FACTURA
																											// LIBRO
		System.out.println("**************************************************");
		System.out.println("******* PEDIDO DE COMPRA ó Compromiso************"); // en MUSEOS el item se llama =>
		System.out.println("**************************************************"); // EN PROCESO
		
		helper.sleep(6);

		String actualUrl = driver.getCurrentUrl();
		String ambienteEspecifico = "mus";
		boolean resultado = actualUrl.contains(ambienteEspecifico); 
		if (resultado) {
			helper.buscarVentana(driver, "Compromiso");
			formulario(driver, urlOB);
		} else { 
			helper.buscarVentana(driver, "Pedido de Compra");
			formulario(driver, urlOB);
		}

	}
	public static void formulario(WebDriver driver, String urlOB) throws InterruptedException {
		try {
			System.out.println("Cargando JSON ...formulario en proceso.");
			Helpers helper = new Helpers();
			helper.cargarHtmlFormulario(driver);
			// CARGAR EL ARCHIVO JSON
			JSONParser parser = new JSONParser();
			Object obj;
			String numeroDocumento = "";
			
			driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
			helper.sleep(4);
			try {
				obj = parser.parse(new FileReader("pedidoCompras.json"));
				JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
				// Tercero
				JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
				int valorTerceros = (int) Math.floor(Math.random() * innerObjectTerceros.size() + 1); // cualquier tercero
				JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("" + valorTerceros + "");// posicion data
																						// tercero
				// Actividades
				JSONObject innerObjectActividades = (JSONObject) jsonObject.get("Actividades");// key padre
				int valorActividades = (int) Math.floor(Math.random() * innerObjectActividades.size() + 1); // cualquier																						// tercero
				JSONObject posicionActividades = (JSONObject) innerObjectActividades.get("" + valorActividades + "");//																				// tercero
				System.out.println("DATOS DEL PEDIDO DE COMPRA:");
				
		try {
	WebElement inpDate = driver.findElement(
			By.xpath("//input[@name='scheduledDeliveryDate_dateTextField' and @class='OBFormFieldDateInputRequired']")); // agregar
	inpDate.clear();
	Date myDate = new Date();
	inpDate.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
	inpDate.sendKeys(Keys.TAB);
	helper.sleep(2);
	
		WebElement tercero = driver.findElement(By.xpath(
				"//input[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']")); // agregar
		tercero.clear();
		tercero.sendKeys((String) posicionTerceros.get("Nombre"));
		tercero.sendKeys(Keys.ENTER);
		helper.sleep(2);
	}  catch (org.openqa.selenium.StaleElementReferenceException ex) {
		WebElement inpDate = driver.findElement(
				By.xpath("//input[@name='scheduledDeliveryDate_dateTextField' and @class='OBFormFieldDateInputRequired']")); // agregar
		inpDate.clear();
		Date myDate = new Date();
		inpDate.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
		inpDate.sendKeys(Keys.TAB);
		helper.sleep(2);
		
		WebElement tercero = driver.findElement(By.xpath(
				"//input[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']")); // agregar
		tercero.clear();
		tercero.sendKeys((String) posicionTerceros.get("Nombre"));
		tercero.sendKeys(Keys.ENTER);
		helper.sleep(2);
	}
		try {
			
			WebElement inpPayment = driver
					.findElement(By.xpath("//*[@name='paymentMethod' and @class='OBFormFieldSelectInput']"));
			inpPayment.clear();
			inpPayment.sendKeys("TRANSFERENCIA");
			inpPayment.sendKeys(Keys.ENTER);
			helper.sleep(3);

			//obtener valor para selector de FacturaProveed
			
			WebElement inpNumDoc = driver
					.findElement(By.xpath("//form[@method='POST']//input[@name='documentNo']"));
			String numDocumento = "PCI-" + inpNumDoc.getAttribute("value");
			numeroDocumento = numDocumento.replaceAll("[<->]", "");
			System.out.println("N° DOCUMENTO: " + numDocumento.replaceAll("[<->]", ""));
			helper.sleep(1);
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			// TODO: handle exception
			WebElement inpPayment = driver
					.findElement(By.xpath("//*[@name='paymentMethod' and @class='OBFormFieldSelectInput']"));
			inpPayment.clear();
			inpPayment.sendKeys("TRANSFERENCIA");
			inpPayment.sendKeys(Keys.ENTER);
			
			helper.sleep(3);
			

			WebElement inpNumDoc = driver
					.findElement(By.xpath("//form[@method='POST']//input[@name='documentNo']"));
			String numDocumento = "PCI-" + inpNumDoc.getAttribute("value");
			numeroDocumento = numDocumento.replaceAll("[<->]", "");
			System.out.println("N° DOCUMENTO: " + numDocumento.replaceAll("[<->]", ""));
			helper.sleep(1);

			
		}
		
		try {
			
			helper.myScroll(driver,
					"//*[@class='OBFormFieldSelectInput' and @name='costcenter']");

			WebElement inpActividad = driver.findElement(
					By.xpath("//*[@class='OBFormFieldSelectInput' and @name='costcenter']")); // agregar
			inpActividad.clear();
			inpActividad.sendKeys((String) posicionActividades.get("Actividad"));
			inpActividad.sendKeys(Keys.TAB);

		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			
			helper.myScroll(driver,
					"//*[@class='OBFormFieldSelectInput' and @name='costcenter']");

			WebElement inpActividad = driver.findElement(
					By.xpath("//*[@class='OBFormFieldSelectInput' and @name='costcenter']")); // agregar
			inpActividad.clear();
			inpActividad.sendKeys((String) posicionActividades.get("Actividad"));
			inpActividad.sendKeys(Keys.TAB);
		}
		
	} catch (FileNotFoundException e1) {
		// TODO2020 Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} // lee archivo

	helper.sleep(2);
	validarFormulario(driver, urlOB, numeroDocumento);

} catch (Exception e) {
	// TODO: FUNTION TOTAL REPEAT this proces.
	driver.navigate().to(urlOB);
	generarPedidoCompra(driver, urlOB);
}

	}
	
	
public static void validarFormulario(WebDriver driver, String UrlParam, String numeroDocumento) {// validar
	System.out.println("Validando los datos del formulario cabecera");
	Helpers helper = new Helpers();
	helper.sleep(1); // cargando...
	driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
			.click();
	helper.sleep(2); // Guradado automatico al moverme a lineas

	agregarLineas(driver, UrlParam, numeroDocumento);

	
}
public static void agregarLineas(WebDriver driver, String UrlParam, String numeroDocumento) { // agregar productos
	System.out.println("DATOS DE LAS LINEAS.");
	helper.sleep(3); // cargando...

	int attemptsB = 0;
	while (attemptsB < 2) {
		try {
			driver.findElement(By.xpath("//div[@eventproxy='isc_OBToolbarIconButton_0']")).click();

			break;
		} catch (Exception e) {
		}
		attemptsB++;
	}

	
	helper.sleep(2);
	// LEYENDO DATOS DEL JSON
	JSONParser parser = new JSONParser();
	Object obj;
	try {
		obj = parser.parse(new FileReader("pedidoCompras.json"));
		JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
		JSONObject innerObjectLineas = (JSONObject) jsonObject.get("Productos");// key padre
		int valor = (int) Math.floor(Math.random() * innerObjectLineas.size() + 1); // cualquier tercero
		JSONObject posicionProducto = (JSONObject) innerObjectLineas.get("" + valor + "");// posicion

		// Actividades
		JSONObject innerObjectActividades = (JSONObject) jsonObject.get("Actividades");// key padre
		int valorActividades = (int) Math.floor(Math.random() * innerObjectActividades.size() + 1); // cualquier
		JSONObject posicionActividades = (JSONObject) innerObjectActividades.get("" + valorActividades + "");// posicion


		helper.sleep(2);
		WebElement inpProducto = driver.findElement(By.xpath(
				"//*[@class='OBTabSetChildContainer' and @eventproxy='isc_OBTabSetChild_0_paneContainer' ]//form[@method='POST']//input[@name='product']"));
		inpProducto.sendKeys((String) posicionProducto.get("Producto"));
		inpProducto.sendKeys(Keys.TAB);
		helper.sleep(2);
		helper.sleep(1);
		
		WebElement inpQuantyti = driver.findElement(By.xpath(
				"//*[@class='OBTabSetChildContainer' and @eventproxy='isc_OBTabSetChild_0_paneContainer' ]//form[@method='POST']//input[@name='orderedQuantity']"));
		inpQuantyti.clear();
		int valorQuantyti = (int) Math.floor(Math.random() * 6 + 1);
		inpQuantyti.sendKeys("" + valorQuantyti + "");
		inpQuantyti.sendKeys(Keys.ENTER);
		double valorPrice = (double) Math.floor(Math.random() * 3 + 1);

		try {
			
			helper.sleep(1);
			helper.myScroll(driver, "//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']");
			helper.sleep(1);

			WebElement inpPrice = driver.findElement(By.xpath(
				"//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']"));
			inpPrice.clear();
				inpPrice.sendKeys("" + valorPrice + "");
				helper.sleep(2);
				inpPrice.sendKeys(Keys.TAB);
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			
			helper.sleep(1);
			helper.myScroll(driver, "//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']");
			helper.sleep(1);

			WebElement inpPrice = driver.findElement(By.xpath(
				"//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']"));
			inpPrice.clear();
				inpPrice.sendKeys("" + valorPrice + "");
				helper.sleep(2);
				inpPrice.sendKeys(Keys.TAB);
		}
		
		
		
			
			helper.sleep(2);
			helper.myScroll(driver, "//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']");

			WebElement inpPriceUnit = driver.findElement(By.xpath(
					"//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']"));
			String valorUnitPrice = inpPriceUnit.getAttribute("value");
			
			helper.myScroll(driver, "//*[@name='listPrice' and @class='OBFormFieldNumberInputRequired']");
			WebElement inpPriceTarifa = driver.findElement(By.xpath(
					"//*[@name='listPrice' and @class='OBFormFieldNumberInputRequired']"));
			String valorList = inpPriceTarifa.getAttribute("value");
			
			System.out.println(valorList);
		/*	helper.sleep(2);
			helper.myScroll(driver, "//*[@name='listPrice' and @class='OBFormFieldNumberInputRequired']");

			WebElement inpPriceTarifa = driver.findElement(By.xpath(
					"//*[@name='listPrice' and @class='OBFormFieldNumberInputRequired']"));
				
			inpPriceTarifa.sendKeys("" + valorPrice + "");
					helper.sleep(2);
					inpPriceTarifa.sendKeys(Keys.TAB);
					*/
					helper.sleep(1);	
			
		// ********** SE CREA EL JSON PARA TOMAR VALORES EL
		// FACTURA-PROVEEDOR************
		JSONObject objWrite = new JSONObject();
		JSONArray list = new JSONArray();
		Date myDate = new Date();
		String fecha = new SimpleDateFormat("dd-MM-yyyy").format(myDate);
		DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
		separadoresPersonalizados.setDecimalSeparator('.');
		DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);

		String optionSelectedFacturaProveedor = numeroDocumento + " - " + fecha + " - "
				+ formato1.format((valorQuantyti * Double.parseDouble(valorUnitPrice)) + (((valorQuantyti * Double.parseDouble(valorUnitPrice))*12)/100) );

		list.add(optionSelectedFacturaProveedor);
		// list.add(valorQuantyti * valorPrice);
		objWrite.put("NumerosDocumentos", list);
		try {
			FileWriter file = new FileWriter("numeroDocumentos.json");
			file.write(objWrite.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			// manejar error
		}
		// *****************************************************************************
		helper.sleep(1);
	
		helper.sleep(1); // Actividad
		helper.myScroll(driver,
				"//*[@class='OBTabSetChildContainer' and @eventproxy='isc_OBTabSetChild_0_paneContainer' ]//form[@method='POST']//input[@name='costcenter']");
		int attemptsSSX = 0;
		while (attemptsSSX < 2) {
			try {
				WebElement inpActividad = driver.findElement(By.xpath(
						"//*[@class='OBTabSetChildContainer' and @eventproxy='isc_OBTabSetChild_0_paneContainer' ]//form[@method='POST']//input[@name='costcenter']"));
				inpActividad.clear();
				inpActividad.sendKeys((String) posicionActividades.get("Actividad"));
				inpActividad.sendKeys(Keys.ENTER);
				break;
			} catch (Exception e) {
			}
			attemptsSSX++;
		}

		
		helper.sleep(1); // Fuente financiamiento
		int attemptsBG = 0;
		while (attemptsBG < 2) {
			try {
				driver.findElement(By.xpath("//div[@eventproxy='isc_OBToolbarIconButton_2']")).click();

				break;
			} catch (Exception e) {
			}
			attemptsBG++;
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
	}
	// VALIDAR EL ERROR Y VOLVER A EJECUTAR EL PROCESO
	helper.sleep(2);
	if (driver.findElements(By.xpath("//*[text()='Error']")).size() == 1) {
		driver.navigate().to(UrlParam);
		try {
			generarPedidoCompra(driver, numeroDocumento);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//JOptionPane.showMessageDialog(null, "Existe algun error en el formulario Lineas.");
	}
	helper.sleep(2);
	guardarFormulario(driver, UrlParam, numeroDocumento);
	
}

public static void guardarFormulario(WebDriver driver, String UrlParam, String numeroDocumento) {
	helper.sleep(1);
	driver.findElement(By.xpath("//*[@class='OBToolbarTextButtonParent' and text()='egistrar']")).click();
	helper.sleep(2);
	// cargando...IFRAME
	driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
	driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME
																												// 2
	driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='mainframe']"))); // IFRAME 2
	helper.sleep(2); // cargando...
	driver.findElement(By.xpath(
			"//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']"))
			.click();
	System.out.println("*****************************************************************************");
	System.out.println("************************* FIN DEL PROCESO PEDIDO DE COMPRA ******************");
	System.out.println("*****************************************************************************");
	driver.navigate().to(UrlParam);
}

}
