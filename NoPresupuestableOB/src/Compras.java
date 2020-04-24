import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
		//driver.manage().window().maximize();
		helper.sleep(6);
		urlParam = urlOB;
		
		for (int i = 0; i < numeroRepeticionesProceso; i++) {
		helper.buscarVentana(driver,"Factura (Proveedor)");
		crearCompra(driver, urlOB, i);
		}
	}
	
	public void crearCompra(WebDriver driver, String UrlParam, int repeticion){ // VALIDAR EL HTML CARGADO
		//DIRECTORIO DEL/ LOS ARCHIVOs JSON
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
		System.out.println("Llenando datos del formulario.");
		int attemptsBCDA = 0;
		while (attemptsBCDA < 2) {
			try {
				driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
				helper.sleep(3);
				break;
			} catch (Exception e) { 
			}
			attemptsBCDA++;			
		} 
		JSONParser parser = new JSONParser();
		Object obj;
		helper.sleep(2);

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

			int attemptsB = 0;
			while (attemptsB < 2) {
				try {
					helper.myScroll(driver, "//textarea[@name='description']");
					helper.sleep(1);
					WebElement inpDescripcion = driver.findElement(By.xpath("//textarea[@name='description']"));
					inpDescripcion.clear();
					inpDescripcion.sendKeys((String) posicionDescripcionC.get("Descripcion"));
					inpDescripcion.submit();
					helper.sleep(1);
					break;
				} catch (Exception e) {
				}
				attemptsB++;
			}
			int attemptsBCAS = 0;
			while (attemptsBCAS < 2) {
				try {
					helper.myScroll(driver, "//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']");
					helper.sleep(1);
					WebElement inpTercero = driver.findElement(
							By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
					inpTercero.clear();
					inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
					inpTercero.sendKeys(Keys.ENTER);
					helper.sleep(1);
					break;
				} catch (Exception e) {
				}
				attemptsBCAS++;
			}
			int attemptsBCO = 0;
			while (attemptsBCO < 2) {
				try {
					helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']");
					helper.sleep(1);
					WebElement inpOrderReference = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']"));
					inpOrderReference.clear();
					inpOrderReference.sendKeys("000-000-00000000" +repeticion);
					inpOrderReference.sendKeys(Keys.ENTER);
					//driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']")).sendKeys("000-000-00000000" +repeticion);
					break;
				} catch (Exception e) { 
				}
				attemptsBCO++;			
		} 
			int attemptsBCD = 0;
			while (attemptsBCD < 2) {
				try {
					// SERIAL PARA EL N° de Factura
					Random rnd = new Random();
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
					inpActividad.sendKeys((String) posicionActividad.get("Actividad"));
					inpActividad.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsBCA++;
			}
			int attemptsBCAX = 0;
			while (attemptsBCAX < 2) {
				helper.myScroll(driver, "//*[text()='No Presupuestable']//span");
				try {
					helper.myScroll(driver, "//*[text()='No Presupuestable']//span");
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
				attemptsBCAX++;
			}

			
			int attemptsBCC = 0;
			while (attemptsBCC < 2) {
			     helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCDoctype']");
				try {
					WebElement inpDocumentoRetencion = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCDoctype']"));
					inpDocumentoRetencion.clear();
					inpDocumentoRetencion.sendKeys((String)posicionDocsRetencion.get("Retencion"));
					inpDocumentoRetencion.sendKeys(Keys.ENTER);
					helper.sleep(1);
					WebElement inpTipoComprobante = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhLivelihood']"));
					inpTipoComprobante.clear();
					inpTipoComprobante.sendKeys((String)posicionTiposComprobante.get("Comprobante"));
					helper.sleep(1);
					inpTipoComprobante.sendKeys(Keys.ENTER);
					WebElement inpCodigoSustento = driver.findElement(By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='sswhCodelivelihood']"));
					inpCodigoSustento.clear();
					inpCodigoSustento.sendKeys((String)posicionCodigosSustento.get("Codigo"));
					inpCodigoSustento.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsBCC++;
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
	
		
		validarFormulario(driver, UrlParam);
	}
	
	
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar formulario completo al aparecer el btn REGISTRO
		Helpers helper = new Helpers();
		System.out.println("Guardando formulario.....");
		helper.sleep(2); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		// Se guarda por defecto al moverme a esta solapa
		agregarLineas(driver, helper, UrlParam);
	
	//	
	} 
	public static void agregarLineas(WebDriver driver, Helpers helper, String UrlParam) { // agregar productos al pedido de venta
		System.out.println("Lineas de la compra: ");
		helper.sleep(3); // cargando...
		
		driver.findElement(By.xpath("//*[text()='Create One']")).click();//
		helper.sleep(3);
		// LEYENDO DATOS DEL JSON
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("compras.json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectLineas = (JSONObject) jsonObject.get("Lineas");// key padre
					//REPETIR PROCESO PARA ASIGNAR VARIOS PRODUCTOS - LINES
			int cantidadLineas = (int) Math.floor(Math.random()*1+1); //cualquier tipo de actividades
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
					int attemptsBAC= 0;
					while (attemptsBAC < 2) {
						try {
							int valorCantidadProduct = (int) Math.floor(Math.random()*6+1); // 1 a 7
							helper.sleep(1); 
							WebElement inpCantidad = driver.findElement(By.xpath("//*[@name='invoicedQuantity' and @class='OBFormFieldNumberInputRequired']"));
							inpCantidad.sendKeys(""+valorCantidadProduct+"");
							if(i < cantidadLineas){
								inpCantidad.sendKeys(Keys.ENTER); 	
							}
							System.out.println("Cantidad: " + valorCantidadProduct);

							break;
						} catch (Exception e) {
						}
						attemptsBAC++;
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
	        System.out.println("Error en las lineas, cancelar lineas compras y volver agregar lineas");
	        driver.findElement(By.xpath("//a[text()='cancel all pending changes']")).click();
	        agregarLineas(driver, helper, UrlParam);
			}else {
				helper.sleep(2);
				completar(driver,helper, UrlParam);
			}
		}
	public static void completar(WebDriver driver, Helpers helper, String UrlParam) {
		helper.sleep(4); // cargando...
		//driver.findElement(By.xpath("//*[@class='OBToolbarTextButton' and text()='C']//u[text()='o']")).click();
		driver.findElement(By.xpath("//td[@class='OBToolbarTextButtonParent'  and text()='C']")).click();
		helper.sleep(3); // cargando...
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