import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Compras {
	Helpers helper = new Helpers();
	String urlParam; 
	public void generarCompras(WebDriver driver, String urlOB, int numeroRepeticionesProceso, int numDocFacturaProv) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("************ COMPRAS ****************"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		//driver.manage().window().maximize();
		helper.sleep(2);
		urlParam = urlOB;
		helper.buscarVentana(driver,"Factura (Proveedor)");
		crearCompra(driver, urlOB, numeroRepeticionesProceso, numDocFacturaProv);
		
	}
	
	public void crearCompra(WebDriver driver, String UrlParam, int repeticion, int numDocFacturaProv) throws InterruptedException{ // VALIDAR EL HTML CARGADO
		//DIRECTORIO DEL/ LOS ARCHIVOs JSON
		helper.sleep(6);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(6);
			System.out.println("Tengo q esperar q cargue el html");
		} 
			helper.sleep(1);
			asignarDatos(driver, UrlParam, repeticion, numDocFacturaProv); // llenar formulario			
	}
	public void asignarDatos(WebDriver driver, String UrlParam, int repeticion, int numDocFacturaProv) throws InterruptedException{
try {
	driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
	helper.sleep(2);
	
	System.out.println("Llenando datos del formulario.");
	JSONParser parser = new JSONParser();
	Object obj; 
	try {
		obj = parser.parse(new FileReader("compras.json"));
		JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
		JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
		JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("1");// posicion data tercero
		JSONObject innerObjectDescripcionC = (JSONObject) jsonObject.get("DescripcionC");// key padre
		JSONObject posicionDescripcionC = (JSONObject) innerObjectDescripcionC.get("1");// posicion data tercero
		System.out.println(posicionDescripcionC);
		JSONObject innerObjectActividad = (JSONObject) jsonObject.get("Actividades");// key padre
		int valorActividad = (int) Math.floor(Math.random()*innerObjectActividad.size()+1); //cualquier tipo de actividades
		JSONObject posicionActividad = (JSONObject) innerObjectActividad.get(""+valorActividad+"");// posicion data tercero
		System.out.println(posicionActividad);
		JSONObject innerObjectDocsRetencion = (JSONObject) jsonObject.get("Retenciones");// key padre
		JSONObject posicionDocsRetencion = (JSONObject) innerObjectDocsRetencion.get("1");// retencio 4009
		JSONObject innerObjectTiposComprobante = (JSONObject) jsonObject.get("TiposComprobante");// key padre
		int valorTiposComprobante = (int) Math.floor(Math.random()*innerObjectTiposComprobante.size()+1); //cualquier tipo de actividades
		JSONObject posicionTiposComprobante = (JSONObject) innerObjectTiposComprobante.get(""+valorTiposComprobante+"");// posicion data tercero
		JSONObject innerObjectCodigosSustento = (JSONObject) jsonObject.get("CodigosSustento");// key padre
		int valorCodigosSustento = (int) Math.floor(Math.random()*innerObjectCodigosSustento.size()+1); //cualquier tipo de actividades
		JSONObject posicionCodigosSustento = (JSONObject) innerObjectCodigosSustento.get(""+valorCodigosSustento+"");// posicion data tercero
		JSONObject innerObjectCodesHash= (JSONObject) jsonObject.get("CodesHash");// key padre
		int valorCodesHash = (int) Math.floor(Math.random()*innerObjectCodesHash.size()+1); //cualquier tipo de actividades
		JSONObject posicionCodesHash = (JSONObject) innerObjectCodesHash.get(""+valorCodesHash+"");// posicion data tercero
		// PRODUCTO
		JSONObject innerObjectProducto= (JSONObject) jsonObject.get("Productos");// key padre
		int valorProducto = (int) Math.floor(Math.random()*innerObjectProducto.size()+1); //cualquier tercero 
		JSONObject posicionProducto = (JSONObject) innerObjectProducto.get(""+valorProducto+"");// posicion data tercero
	
		
		try {
			helper.myScroll(driver, "//textarea[@name='description']");
			WebElement inpDescripcion = driver.findElement(By.xpath("//textarea[@name='description']"));
			inpDescripcion.sendKeys("SELENIUM OB -");
			inpDescripcion.submit();
			helper.sleep(1);
			
			helper.myScroll(driver, "//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']");
			WebElement inpTercero = driver.findElement(
					By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
			inpTercero.clear();
			inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
			inpTercero.sendKeys(Keys.ENTER);
			helper.sleep(1);
			
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']");		
			helper.sleep(1);
			WebElement inpNumeroReferencia = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")); 
			inpNumeroReferencia.clear();
				//inpNumeroReferencia.sendKeys("000-000-00000000"+repeticion);
				inpNumeroReferencia.sendKeys("000-000-0000000"+(numDocFacturaProv+repeticion));

				helper.sleep(1);
	
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			// TODO: handle exception
			helper.myScroll(driver, "//textarea[@name='description']");
			WebElement inpDescripcion = driver.findElement(By.xpath("//textarea[@name='description']"));
			inpDescripcion.sendKeys((String) posicionDescripcionC.get("Descripcion"));
			inpDescripcion.submit();
			helper.sleep(1);
			
			helper.myScroll(driver, "//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']");
			WebElement inpTercero = driver.findElement(
					By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
			inpTercero.clear();
			inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
			inpTercero.sendKeys(Keys.ENTER);
			helper.sleep(1);
			
			
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']");		
			helper.sleep(1);
			WebElement inpNumeroReferencia = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")); 
			inpNumeroReferencia.clear();
				//inpNumeroReferencia.sendKeys("000-000-00000000"+repeticion);
				inpNumeroReferencia.sendKeys("000-000-0000000"+(numDocFacturaProv+repeticion));

				helper.sleep(1);
		}
		
		try {
			helper.myScroll(driver, "//*[text()='E-Invoice']//span | //*[text()='Factura electrónica']//span");
			helper.sleep(1);
			WebElement checkInvoice = driver.findElement(By.xpath("//*[text()='E-Invoice']//span | //*[text()='Factura electrónica']//span"));
			if (checkInvoice.isSelected()) {
				System.out.println("E- invoice ya habilitado");
			} else {
				checkInvoice.click(); // habilitar e invoice
				System.out.println(" Habilitando E- invoice");
			}
			
			Random rnd = new Random();
			int serialAuth = rnd.nextInt(999999999) ; 
			WebElement inpNumAuth = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhNroauthorization']"));
			inpNumAuth.clear();
			inpNumAuth.sendKeys(""+serialAuth+"2");
			inpNumAuth.sendKeys(Keys.ENTER);

			
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']");
			WebElement ippDateCaducidad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']"));
			ippDateCaducidad.clear();
			//Aquí colocas tu objeto tipo Date
			Date myDate = new Date();
			ippDateCaducidad.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
			ippDateCaducidad.sendKeys(Keys.ENTER);
			
			
			
			
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			helper.myScroll(driver, "//*[text()='E-Invoice']//span | //*[text()='Factura electrónica']//span");
			helper.sleep(1);
			WebElement checkInvoice = driver.findElement(By.xpath("//*[text()='E-Invoice']//span | //*[text()='Factura electrónica']//span"));
			if (checkInvoice.isSelected()) {
				System.out.println("E- invoice ya habilitado");
			} else {
				checkInvoice.click(); // habilitar e invoice
				System.out.println(" Habilitando E- invoice");
			}
			
			Random rnd = new Random();
			int serialAuth = rnd.nextInt(999999999) ; 
			WebElement inpNumAuth = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhNroauthorization']"));
			inpNumAuth.clear();
			inpNumAuth.sendKeys(""+serialAuth+"2");
			inpNumAuth.sendKeys(Keys.ENTER);

			
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']");
			WebElement ippDateCaducidad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']"));
			ippDateCaducidad.clear();
			//Aquí colocas tu objeto tipo Date
			Date myDate = new Date();
			ippDateCaducidad.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
			ippDateCaducidad.sendKeys(Keys.ENTER);
			
			
		}
		
		try {
			helper.sleep(2);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='costcenter']");
		
			WebElement inpActividad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='costcenter']"));
			inpActividad.clear();
			inpActividad.sendKeys((String) posicionActividad.get("Actividad"));
			inpActividad.sendKeys(Keys.ENTER);
			helper.sleep(1);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']");
			WebElement inpTipoComprobante = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']"));
			inpTipoComprobante.clear();
			inpTipoComprobante.sendKeys((String)posicionTiposComprobante.get("Comprobante"));
			helper.sleep(1);
			inpTipoComprobante.sendKeys(Keys.ENTER);
			helper.sleep(1);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']");
			WebElement inpCodigoSustento = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']"));
			inpCodigoSustento.clear();
			inpCodigoSustento.sendKeys((String)posicionCodigosSustento.get("Codigo"));
			inpCodigoSustento.sendKeys(Keys.ENTER);

		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			helper.sleep(2);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='costcenter']");
		
			WebElement inpActividad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='costcenter']"));
			inpActividad.clear();
			inpActividad.sendKeys((String) posicionActividad.get("Actividad"));
			inpActividad.sendKeys(Keys.ENTER);
			helper.sleep(1);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']");
			WebElement inpTipoComprobante = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']"));
			inpTipoComprobante.clear();
			inpTipoComprobante.sendKeys((String)posicionTiposComprobante.get("Comprobante"));
			helper.sleep(1);
			inpTipoComprobante.sendKeys(Keys.ENTER);
			helper.sleep(1);
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']");
			WebElement inpCodigoSustento = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']"));
			inpCodigoSustento.clear();
			inpCodigoSustento.sendKeys((String)posicionCodigosSustento.get("Codigo"));
			inpCodigoSustento.sendKeys(Keys.ENTER);
			
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

	helper.sleep(2);
	validarFormulario(driver, UrlParam);
	
}catch (Exception e) {
	// TODO: FUNTION TOTAL REPEAT this proces.
	driver.navigate().to(UrlParam);
	generarCompras(driver, UrlParam, repeticion, numDocFacturaProv);
}		


	
	}
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar formulario completo al aparecer el btn REGISTRO
		Helpers helper = new Helpers();
		System.out.println("Guardando formulario.....");
		helper.sleep(2); // cargando...
		driver.findElement(By.xpath("//td[@class='OBToolbarTextButton'  and text()='C']//u[text()='r']")).click();
		
		// Se guarda por defecto al moverme a esta solapa
		//agregarLineas(driver, helper, UrlParam);
		completar(driver, helper, UrlParam);
	//	
	} 
	
	public static void completar(WebDriver driver, Helpers helper, String UrlParam) {
		helper.sleep(4); // cargando...
		
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME 2
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='frameButton']"))); // Frame
		
		helper.sleep(1); // cargando...
	//	driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']")).click();
		// agregar el numero de documento
		 JSONParser parser = new JSONParser();

	        try (Reader reader = new FileReader("numeroDocumentos.json")) {
	       
	            JSONObject jsonObject = (JSONObject) parser.parse(reader);
	            System.out.println(jsonObject);

	            // loop array
	            JSONArray msg = (JSONArray) jsonObject.get("NumerosDocumentos");
	            Iterator<String> iterator = msg.iterator();
	            while (iterator.hasNext()) {
	            	String numFactura = iterator.next();
	                Select select=new Select(driver.findElement(By.xpath("//select[@class='Combo Combo_TwoCells_width' and @name='inpPurchaseOrder']")));
	                select.selectByVisibleText(numFactura);
	                helper.sleep(3);
	                driver.findElement(By.xpath("//input[@type='checkbox' and @name='inpTodos' ]")).click();
	                helper.sleep(1);

	                driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar']")).click();
	            }
	            
	           

	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        completar(driver, UrlParam, helper);
	}
	public static void completar(WebDriver driver, String UrlParam, Helpers helper) {
		helper.sleep(2);
	    driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//td[@class='OBToolbarTextButton'  and text()='C']//u[text()='o']")).click();
		helper.sleep(2);
		// cargando...IFRAME
				driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
				driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='process']"))); // IFRAME 1
																															// 2
				helper.sleep(1); // cargando...
				driver.findElement(By.xpath(
						"//*[@class='Button_text Button_width' and text()='Aceptar']"))
						.click();
				
				helper.sleep(4);
		System.out.println("*************************************************************************");
		System.out.println("************ FIN DEL PROCESO COMPRAS Factura(proveedor)****************");
		System.out.println("*************************************************************************");
		driver.navigate().to(UrlParam);	
		
	}
}

  