import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class PedidoVentas {
	Helpers helper = new Helpers();
	String urlParam;
	public void pedidoDeVentas(WebDriver driver, String urlOB) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("******** PEDIDO DE VENTAS ***********"); // HECHO EN MUSEOS
		System.out.println("*************************************");

		helper.sleep(4);
		urlParam = urlOB;
		helper.buscarVentana(driver,"Pedido de venta");
		crearPedido(driver, urlOB);
	}

	public void crearPedido(WebDriver driver , String urlParam) { // Factura (Cliente)
		helper.sleep(8);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(8);
			System.out.println("Tengo q esperar q cargue el html");
		} 
			helper.sleep(2);
			asignarDatos(driver);		
	} 

	public void asignarDatos(WebDriver driver) { // llenar form pedido
		System.out.println("Nuevo Pedido de ventas.");
		System.out.println("Llenando datos del formulario .....");
		helper.sleep(2);
			int attemptsAA = 0;
			while (attemptsAA < 2) {
				try {
					driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
					break;
				} catch (Exception e) {
				}
				attemptsAA++;
			}
			helper.sleep(2);
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
					WebElement inpActividad = driver.findElement(
							By.name("costcenter"));
					inpActividad.clear();
					inpActividad.sendKeys("N/A");
					inpActividad.submit();
					helper.sleep(1);
					WebElement inpTercero = driver.findElement(
							By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
					inpTercero.clear();
					inpTercero.sendKeys("Consumidor Final");
					inpTercero.submit();
					break;
				} catch (Exception e) {
				}
				attemptsB++;
			}
			helper.sleep(2);
			if(driver.findElements(By.xpath("//*[text()='Error']")).isEmpty()) {
				validarFormulario(driver, urlParam);
			}else {
				helper.sleep(1); // cargando...
				validarFormulario(driver, urlParam);
			}
		}
		
	

	public void validarFormulario(WebDriver driver, String urlParam) {// validar formulario completo al aparecer el btn REGISTRO
		System.out.println("Guardando formulario...");
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		helper.sleep(2); // cargando...GUARDAR
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_save OBToolbarIconButton']")).click();
		helper.sleep(2); // cargando...

			if (driver.findElements(By.xpath("//*[text()='Create One']")).size() > 0) {
				System.out.println("Proceso crear pedido...");
				agregarLineas(driver, urlParam);
				}
	} 

	public void agregarLineas(WebDriver driver, String UrlParam) { // agregar productos al pedido de venta
		System.out.println("Asignar Lineas al formulario.....");
		helper.sleep(3); // cargando...
		driver.findElement(By.xpath("//*[text()='Create One']")).click();//
		helper.sleep(5);
		String[] lineas = {"AFRO DESCENDIENTES", "HUMBOLDT", "LIBRO LA OTRA TIERRA", "SALA SIGLO XVII", "AFICHE BOTERO"};
		//Agregar productos
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
								.findElement(By.xpath("//*[@name='orderedQuantity' and @class='OBFormFieldNumberInputRequired']"));
						//inpCantidad.clear();
						inpCantidad.sendKeys(Integer.toString(valorCantidad));
						break;
					} catch (Exception e) {
					}
					attemptsB++;
				}
				
		
			helper.sleep(2);
			guardarFormulario(driver);
		
		}
	public void guardarFormulario(WebDriver driver) {
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
		driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='OK']")).click();
		helper.sleep(2);
		addPayment(driver);
	}
	
	public void addPayment(WebDriver driver) {
		helper.sleep(1);
		driver.findElement(By.xpath("//*[text()='dd Payment']")).click();
		helper.sleep(2);
		helper.myScroll(driver,"//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']");
		WebElement inpActionDocument = driver.findElement(By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']"));
		inpActionDocument.sendKeys("Process Received Payment(s) and Deposit");
		inpActionDocument.sendKeys(Keys.ENTER);
		helper.sleep(2);
		driver.findElement(By.xpath("//*[@class='OBFormButton' and text()='Done']")).click();
		System.out.println("*************************************");
		System.out.println("************** FIN ******************");
		System.out.println("*************************************");
	
	}
}

