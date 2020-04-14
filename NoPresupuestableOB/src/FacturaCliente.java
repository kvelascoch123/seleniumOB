import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
 
public class FacturaCliente {
	Helpers helper = new Helpers();
	String urlParam;
	public void generarVenta(WebDriver driver, String urlOB, int numeroRepeticionesProceso) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("********** FacturaCliente ***********"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		driver.manage().window().maximize();
		helper.sleep(6);
		urlParam = urlOB;
		for (int i = 0; i < numeroRepeticionesProceso; i++) {
			helper.buscarVentana(driver,"Factura (Cliente)");
			crearFactura(driver, urlOB);
			}
	}
	public void crearFactura(WebDriver driver, String UrlParam){ // VALIDAR EL HTML CARGADO
		helper.sleep(10);
		if (driver.findElements(By.xpath("//*[@class='OBGridHeaderCellTitle' and text()='Organización']"))
				.size() <= 0) {
			helper.sleep(8);
			System.out.println("Tengo q esperar q cargue el html");
		} 
		
			helper.sleep(5);
			asignarDatos(driver, UrlParam); // llenar formulario			
	}
	
	public void asignarDatos(WebDriver driver, String UrlParam){
		System.out.println("Llenando datos del formulario.");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("factura(cliente).json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
			JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("1");// posicion data tercero
			// Metodos de pago
			JSONObject innerObjectPayment = (JSONObject) jsonObject.get("PaymentMethods");// key padre
			int valorPayment = (int) Math.floor(Math.random() * innerObjectPayment.size() + 1); // cualquier tercero
			JSONObject posicionPayment = (JSONObject) innerObjectPayment.get("" + valorPayment + "");// posicion data
			// Tarifa
			JSONObject innerObjectTarifa = (JSONObject) jsonObject.get("Tarifas");// key padre
			int valorTarifa = (int) Math.floor(Math.random() * innerObjectTarifa.size() + 1); // cualquier tercero
			JSONObject posicionTarifa = (JSONObject) innerObjectTarifa.get("" + valorTarifa + "");// posicion data
			// Actividades
			JSONObject innerObjectActividades = (JSONObject) jsonObject.get("Actividades");// key padre
			int valorActividades = (int) Math.floor(Math.random() * innerObjectActividades.size() + 1); // cualquier																						// tercero
			JSONObject posicionActividades = (JSONObject) innerObjectActividades.get("" + valorActividades + "");// posicion
							
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
					WebElement inp = driver.findElement(
							By.xpath("//*[@name='orderReference' and @class='OBFormFieldInput' and @oninput='isc_OBTextItem_4._handleInput()']"));
					inp.clear();
					inp.sendKeys(".", Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attempts++;
			}
			helper.sleep(1);
			int attemptAs = 0;
			while (attemptAs < 3) {
				try {
					helper.myScroll(driver, "//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']");
					WebElement inpTercero = driver.findElement(
							By.xpath("//*[@name='businessPartner' and @class='OBFormFieldSelectInputRequiredError']"));
					inpTercero.clear();
					inpTercero.sendKeys((String) posicionTerceros.get("Nombre"));
					helper.sleep(1);
					inpTercero.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptAs++;
			}
			// TIPO PAGO
			helper.sleep(2);
			int attemptsXS = 0;
			while (attemptsXS < 2) {
				try {
					helper.myScroll(driver,"//*[@name='paymentMethod' and @class='OBFormFieldSelectInputRequired']" );
					WebElement inpPayment = driver
							.findElement(By.xpath("//*[@name='paymentMethod' and @class='OBFormFieldSelectInputRequired']"));
					inpPayment.clear();
					inpPayment.sendKeys((String) posicionPayment.get("Pago"));
					inpPayment.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsXS++;
			} 
			// TARIFA
			helper.sleep(1);
			int attemptsXST = 0;
			while (attemptsXST < 2) {
				try {
					helper.myScroll(driver,"//*[@name='priceList' and @class='OBFormFieldSelectInputRequired']" );
					WebElement inpTarifa = driver
							.findElement(By.xpath("//*[@name='priceList' and @class='OBFormFieldSelectInputRequired']"));
					inpTarifa.clear();
					inpTarifa.sendKeys((String) posicionTarifa.get("Tarifa"), Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsXST++;
			} 
			helper.sleep(1);
			int attemptsX = 0;
			while (attemptsX < 2) {
				try {
				    helper.myScroll(driver, "//*[@name='description' and @class='OBFormFieldInputRequired']");
					WebElement inpDescripcion = driver.findElement(
							By.xpath("//*[@name='description' and @class='OBFormFieldInputRequired']"));
					inpDescripcion.clear();
					inpDescripcion.sendKeys("SELENIUM OB FACTURA CLIENTE", Keys.ENTER);
				
					break;
				} catch (Exception e) {
				}
				attemptsX++;
			} 

			
			helper.sleep(1);
			helper.myScroll(driver, "//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']");
			int attemptsO = 0;
			while (attemptsO < 2) {
				try {
					helper.myScroll(driver,
							"//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']");

					WebElement inpActividad = driver.findElement(
							By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='costcenter']")); // agregar
					inpActividad.clear();
					inpActividad.sendKeys((String) posicionActividades.get("Actividad"));
					inpActividad.sendKeys(Keys.ENTER);
					break;
				} catch (Exception e) {
				}
				attemptsO++;
			}
			helper.sleep(2);
			validarFormulario(driver, urlParam);
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
		
	}
	public void validarFormulario(WebDriver driver, String urlParam) {// validar formulario completo al aparecer el btn REGISTRO
		System.out.println("Guardando formulario...");
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='OBTabBarButtonChildTitleSelectedInactive' and text()='Líneas']"))
		.click();
		helper.sleep(1); // cargando...GUARDAR
		if(driver.findElements(By.xpath("//*[text()='Error']")).size() >= 1) {
			//VALIDANDO SI EXISTE ERROR EN FORMULARIO....alun campo vacio
	        System.out.println("Algun campo no fue llenado en el formulario, volviendo a realizar pedido");

		}else {
		
		helper.sleep(2); // cargando...
			if (driver.findElements(By.xpath("//*[text()='Create One']")).size() > 0) {
				agregarLineas(driver, urlParam);
				}
	} 
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
			obj = parser.parse(new FileReader("factura(cliente).json"));
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
				/*	//cantidad 
					int attemptsB = 0;
					while (attemptsB < 2) {
						try {
							int valorCantidadProduct = (int) Math.floor(Math.random()*5+1); // 1 a 7
							helper.sleep(2); 
							WebElement inpCantidad = driver
									.findElement(By.xpath("//*[@name='invoicedQuantity' and @class='OBFormFieldNumberInputRequired']"));
							inpCantidad.clear();
							inpCantidad.sendKeys(""+valorCantidadProduct+"");
				
							System.out.println("Cantidad: " + valorCantidadProduct);
							break;
						} catch (Exception e) {
						}
						attemptsB++;
					}*/
					//prescio 
					int attemptsBP = 0;
					while (attemptsBP < 2) {
						try {
							int valorCantidadProduct = (int) Math.floor(Math.random()*6+1); // 1 a 7
							helper.sleep(2); 
							WebElement inpPrecio = driver
									.findElement(By.xpath("//*[@name='unitPrice' and @class='OBFormFieldNumberInputRequired']"));
							inpPrecio.clear();
							inpPrecio.sendKeys(""+valorCantidadProduct+"");
							if(i < cantidadLineas){
								inpPrecio.sendKeys(Keys.ENTER);	
							}
							System.out.println("Cantidad: " + valorCantidadProduct);
							break;
						} catch (Exception e) {
						}
						attemptsBP++;
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
				guardarFormulario(driver, UrlParam, helper);
			}
		}
	public static void guardarFormulario(WebDriver driver, String UrlParam,Helpers helper) {
		helper.sleep(2);
		driver.findElement(By.xpath("//*[@class='OBToolbarTextButtonParent' and text()='C']//u[text()='o']")).click();
		helper.sleep(2); // cargando...
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='OBClassicPopup_iframe']"))); // IFRAME 1
		driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='MDIPopupContainer' and @name='process']"))); // IFRAME 2
		helper.sleep(1); // cargando...
		driver.findElement(By.xpath("//*[@class='Button_text Button_width' and text()='Aceptar'] | //*[@class='Button_text Button_width' and text()='OK']")).click();

		System.out.println("*************************************");
		System.out.println("************ FIN DEL PROCESO Factura (cliente) ****************");
		System.out.println("*************************************");
		driver.navigate().to(UrlParam);
	
	}

}