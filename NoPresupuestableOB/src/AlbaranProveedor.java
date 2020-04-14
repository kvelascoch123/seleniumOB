import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AlbaranProveedor {
	Helpers helper = new Helpers();

	public void generarAlbaranProveedor(WebDriver driver, String UrlParam , int numeroRepeticionesProceso) throws InterruptedException {
		System.out.println("*************************************");
		System.out.println("******** ALBARAN (PROVEEDOR) ********"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		driver.manage().window().maximize();
		helper.sleep(6);

		 for (int i = 0; i < numeroRepeticionesProceso; i++) {
		helper.buscarVentana(driver, "Albaran (Proveedor)");
		crearAlbaran(driver, UrlParam, numeroRepeticionesProceso);
		 }
	}

	private void crearAlbaran(WebDriver driver, String UrlParam, int numeroRepeticionesProceso) {
		// DIRECTORIO DEL/ LOS ARCHIVOs JSON
		helper.sleep(6);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(6);
			System.out.println("Tengo q esperar q cargue el html");
		}
		helper.sleep(5);
		asignarDatos(driver, UrlParam, numeroRepeticionesProceso); // llenar formulario

	}

	public void asignarDatos(WebDriver driver, String UrlParam, int numeroRepeticionesProceso) {
		System.out.println("Llenando datos del formulario.");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("albaran(proveedor).json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
			int valorTercero = (int) Math.floor(Math.random() * innerObjectTerceros.size() + 1); // cualquier tipo de
																									// actividades
			JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("" + valorTercero + "");// posicion data
																										// tercero

			JSONObject innerObjectDescripcionC = (JSONObject) jsonObject.get("DescripcionC");// key padre
			JSONObject posicionDescripcionC = (JSONObject) innerObjectDescripcionC.get("1");// posicion data tercero
			System.out.println(posicionDescripcionC);

			int attemptsBCDA = 0;
			while (attemptsBCDA < 2) {
				try {
					driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']"))
							.click();
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
					inpDescripcion.sendKeys((String) posicionDescripcionC.get("Descripcion"));
					inpDescripcion.submit();
					helper.sleep(1);
					WebElement inpTercero = driver.findElement(
							By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
					inpTercero.clear();
					inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
					inpTercero.sendKeys(Keys.ENTER);
					helper.sleep(1);
					helper.sleep(1);

					helper.sleep(1);
					break;
				} catch (Exception e) {
				}
				attemptsB++;
			}

			int attemptsBCC = 0;
			while (attemptsBCC < 2) {
				Random rnd = new Random();
				int serialAuth = rnd.nextInt() * 9 + 1;
				helper.myScroll(driver, "//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']");
				try {
					helper.sleep(1);
					WebElement inpOrderReference = driver.findElement(
							By.xpath("//div[@class='OBViewForm']//input[@type='TEXT' and @name='orderReference']"));
					inpOrderReference.clear();
					inpOrderReference.sendKeys("" + serialAuth + "");
					helper.sleep(1);
					inpOrderReference.sendKeys(Keys.ENTER);

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
		// VALIDAR EL ERROR Y VOLVER A EJECUTAR EL PROCESO
		helper.sleep(2);
		if (driver.findElements(By.xpath("//*[text()='Error']")).size() == 1) {
			driver.navigate().to(UrlParam);
			try {
				generarAlbaranProveedor(driver, UrlParam, numeroRepeticionesProceso);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// JOptionPane.showMessageDialog(null, "Existe algun error en el formulario
			// Lineas.");
		}
		helper.sleep(3);

		validarFormulario(driver, UrlParam);
	}

	// EN ESTE PEDIDO NO SE REGISTRAN LINEAS SE ASIGNAN DE..
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar formulario completo al aparecer
																				// el btn REGISTRO
		Helpers helper = new Helpers();
		System.out.println("Guardando formulario.....");
		helper.sleep(2); // cargando...
		driver.findElement(By.xpath("//td[@class='OBToolbarTextButton']//u[text()='C']")).click();

		// Se guarda por defecto al moverme a esta solapa
		// agregarLineas(driver, helper, UrlParam);
		completar(driver, helper, UrlParam);
		//
	}

	public static void completar(WebDriver driver, Helpers helper, String UrlParam) {
		helper.sleep(4); // cargando...

		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME
																													// 2
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='frameButton']"))); // Frame

		helper.sleep(1); // cargando...

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			// NUMEOR PEDIDO
			obj = parser.parse(new FileReader("albaran(proveedor).json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectNumeroPedidos = (JSONObject) jsonObject.get("NumeroPedidos");// key padre
			int valorNumeroPedidos = (int) Math.floor(Math.random() * innerObjectNumeroPedidos.size() + 1); // cualquier
																											// tipo de
																											// actividades
			JSONObject posicionNumeroPedidos = (JSONObject) innerObjectNumeroPedidos.get("" + valorNumeroPedidos + "");// posicion
																														// data
																														// tercero
			// ALMACEN
			JSONObject innerObjectAlmacen = (JSONObject) jsonObject.get("Almacenes");// key padre
			int valorAlmacen = (int) Math.floor(Math.random() * innerObjectAlmacen.size() + 1); // cualquier tipo de																	// actividades
			JSONObject posicionAlmacen = (JSONObject) innerObjectAlmacen.get("" + valorAlmacen + "");// posicion data
																										// tercero

			Select select = new Select(driver.findElement(By
					.xpath("//select[@name='inpPurchaseOrder' and @class='Combo Combo_TwoCells_width Combo_focus']")));
			select.selectByIndex(1);
			//select.selectByVisibleText((String) posicionNumeroPedidos.get("Pedido"));
			
			//helper.sleep(1);
			//inpAlmacen.sendKeys(Keys.ENTER);
			helper.sleep(2);
			driver.findElement(By.xpath("//input[@type='checkbox' and @name='inpTodos' ]")).click();
			helper.sleep(2);
			WebElement inpAlmacen = driver.findElement(By.xpath(
					"//*[@name='inpmLocatorId_DES']"));
			inpAlmacen.clear();
			inpAlmacen.sendKeys((String) posicionAlmacen.get("Almacen"));
			helper.sleep(1);
			driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar']")).click();
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
						
				System.out.println("*************************************************************************");
				System.out.println("************ FIN DEL PROCESO COMPRAS Factura(proveedor)****************");
				System.out.println("*************************************************************************");
				driver.navigate().to(UrlParam);	
			}
}
