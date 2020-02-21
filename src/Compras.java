import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Compras {
	Helpers helper = new Helpers();
	String urlParam;
	public void generarCompras(WebDriver driver, String urlOB, int numeroRepeticionesProceso) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("************ COMPRAS ****************"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		driver.manage().window().maximize();
		helper.sleep(6);
		urlParam = urlOB;
		
		for (int i = 0; i < numeroRepeticionesProceso; i++) {
		helper.buscarVentana(driver,"Factura (Proveedor)");
		crearCompra(driver, urlOB, i);
		}
	}
	
	public void crearCompra(WebDriver driver, String UrlParam, int repeticion){ // VALIDAR EL HTML CARGADO
		helper.sleep(10);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(8);
			System.out.println("Tengo q esperar q cargue el html");
		} 
			helper.sleep(5);
			asignarDatos(driver, UrlParam, repeticion); // llenar formulario			
	}
	public void asignarDatos(WebDriver driver, String UrlParam, int repeticion){
		System.out.println("Llenando datos del formulario .....");
		int attemptsBCDA = 0;
		while (attemptsBCDA < 2) {
			try {
				// SERIAL PARA EL N° de Factura
				driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
				helper.sleep(3);
				break;
			} catch (Exception e) { 
			}
			attemptsBCDA++;			
	} 

		int attemptsB = 0;
		while (attemptsB < 2) {
			try {
				helper.sleep(1);
				WebElement inpDescripcion = driver.findElement(By.xpath("//textarea[@name='description']"));
				inpDescripcion.clear();
				inpDescripcion.sendKeys("servicios prestados");
				inpDescripcion.submit();
				helper.sleep(1);
				WebElement inpTercero = driver.findElement(
						By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
				inpTercero.clear();
				inpTercero.sendKeys("SIDESOFT CIA LTDA");
				inpTercero.sendKeys(Keys.ENTER);
				helper.sleep(1);
				if(repeticion < 10) {
				driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")).sendKeys("000-000-00000000"+repeticion);
				}else if(repeticion >= 10){
					driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")).sendKeys("000-000-0000000"+repeticion);
				}else if(repeticion >= 100 ){
					driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")).sendKeys("000-000-000000"+repeticion);
				}
				helper.sleep(1);
				break;
			} catch (Exception e) {
			}
			attemptsB++;
		}

		int attemptsBCD = 0;
		while (attemptsBCD < 2) {
			try {
				// SERIAL PARA EL N° de Factura
				Random rnd = new Random();
				//int serial = rnd.nextInt() * 8+1; 
				//System.out.println("N° Referencia" + serial);
				
				// N° AUTORIZACION
				int serialAuth = rnd.nextInt() * 9+1; 
				WebElement inpNumAuth = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhNroauthorization']"));
				inpNumAuth.clear();
				inpNumAuth.sendKeys(""+serialAuth+"");
				inpNumAuth.sendKeys(Keys.ENTER);
				
				break;
			} catch (Exception e) { 
			}
			attemptsBCD++;			
	} 
		
		int attemptsInvoice = 0;
		while (attemptsInvoice < 2) {
			try {
				helper.myScroll(driver, "//*[text()='E-Invoice']//span");
				helper.sleep(1);
				WebElement checkInvoice = driver.findElement(By.xpath("//*[text()='E-Invoice']//span"));
				if(checkInvoice.isSelected()){
					System.out.println("E- invoice ya habilitado");
				}else{
					checkInvoice.click(); // habilitar e invoice
					System.out.println(" Habilitando E- invoice");
				}
				break;
			} catch (Exception e) { 
			}
			attemptsInvoice++;			
	}  
		
		
		
		int attemptsBCA = 0;
		while (attemptsBCA < 2) {
			helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']");
			try {
				WebElement ippDateCaducidad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhExpirationdate_dateTextField']"));
				ippDateCaducidad.clear();
				//Aquí colocas tu objeto tipo Date
				Date myDate = new Date();
				ippDateCaducidad.sendKeys(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
				ippDateCaducidad.sendKeys(Keys.ENTER);
				WebElement inpActividad = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='costcenter']"));
				inpActividad.clear();
				inpActividad.sendKeys("N/A");
				inpActividad.sendKeys(Keys.ENTER);

				helper.sleep(1);
				WebElement checkNoPresupuestable = driver.findElement(By.xpath("//*[text()='No Presupuestable']//span"));
				if(checkNoPresupuestable.isSelected()){
					System.out.println("NoPresupuestable ya habilitado");
				}else{
					checkNoPresupuestable.click(); // habilitar e invoice
					System.out.println(" Habilitando NoPresupuestable");
				}
				helper.sleep(1);
				break;
			} catch (Exception e) {
			}
			attemptsBCA++;
		}

		
		int attemptsBCC = 0;
		while (attemptsBCC < 2) {
		     helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCDoctype']");
			try {
				WebElement inpDocumentoRetencion = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCDoctype']"));
				inpDocumentoRetencion.clear();
				inpDocumentoRetencion.sendKeys("RETENCIONES 4009");
				inpDocumentoRetencion.sendKeys(Keys.ENTER);
				helper.sleep(1);
				WebElement inpTipoComprobante = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']"));
				inpTipoComprobante.clear();
				inpTipoComprobante.sendKeys("01 - FACTURA");
				helper.sleep(1);
				inpTipoComprobante.sendKeys(Keys.ENTER);
				WebElement inpCodigoSustento = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']"));
				inpCodigoSustento.clear();
				inpCodigoSustento.sendKeys("02-COSTO O GASTO PARA DECLARACION DE IR (MENOS ACTIVOS FIJOS)");
				inpCodigoSustento.sendKeys(Keys.ENTER);
				break;
			} catch (Exception e) {
			}
			attemptsBCC++;
		}		
		validarFormulario(driver, UrlParam);
	}
	
	
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar formulario completo al aparecer el btn REGISTRO
		Helpers helper = new Helpers();
		System.out.println("Guardando formulario.....");
		helper.sleep(2); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		// Se guarda por defecto al moverme a esta solapa
		agregarLinea(driver, helper, UrlParam);
	
	//	
	} 
	public static void agregarLinea(WebDriver driver, Helpers helper, String UrlParam){
 
		System.out.println("Asignar Lineas al formulario.....");
		helper.sleep(2); // cargando...
		driver.findElement(By.xpath("//*[text()='Create One']")).click();//
		helper.sleep(5);
		String[] lineas = {"1120101007-ANTICIPO EMPLEADOS Y FUNC-332", "1521204001-DECIMOCUARTO SUELDO-332", "1523101001-AGUA POTABLE-332", "1521306001-ALIMENTACION-332"};
		Random r=new Random();
	    int randomNumber=r.nextInt(lineas.length);
		WebElement inpProducto = driver.findElement(By.xpath("//*[@class='OBTabSetChild']//div[@class='OBTabSetChildContainer']//*[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//div[@class='normal']//*[@role='presentation']//table[@role='presentation']//td//table[@role='presentation']//input[@name='product']"));
		// cargando...
		inpProducto.sendKeys(lineas[randomNumber]);
		System.out.println("Linea asignada: " + lineas[randomNumber]);
		helper.sleep(1);
		//cantidad 
		int attemptsB = 0;
		while (attemptsB < 3) {
			try {
				int valorCantidad = (int) Math.floor(Math.random()*6+1); // 1 a 7
				helper.sleep(2); 
				WebElement inpCantidad = driver
						.findElement(By.xpath("//*[@name='invoicedQuantity' and @class='OBFormFieldNumberInputRequired']"));
				inpCantidad.sendKeys(Integer.toString(valorCantidad));
				break;
			} catch (Exception e) {
			}
			attemptsB++;
		}
		helper.sleep(5); // cargando...GUARDAR
		driver.findElement(By.xpath("//*[@class=\"OBGridToolStripIcon\"]//img[@src=\"http://186.69.209.150:8071/museos/web/org.openbravo.userinterface.smartclient/openbravo/skins/Default/org.openbravo.client.application/images/grid/gridButton-save.png\"]")).click();
	
		completar(driver, helper, UrlParam);
		
	}
	public static void completar(WebDriver driver, Helpers helper, String UrlParam) {
		helper.sleep(4); // cargando...
		//driver.findElement(By.xpath("//*[@class='OBToolbarTextButton' and text()='C']//u[text()='o']")).click();
		driver.findElement(By.xpath("//td[@class='OBToolbarTextButtonParent'  and text()='C']")).click();
		helper.sleep(2); // cargando...
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME 2
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']")).click();

		System.out.println("*************************************");
		System.out.println("************ FIN DEL PROCESO COMPRAS ****************");
		System.out.println("*************************************");
		driver.navigate().to(UrlParam);
	}
}

 