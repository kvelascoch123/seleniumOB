import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Pagos {
	
	Helpers helper = new Helpers();
	String urlParam;  
	public void generarPago(WebDriver driver, String urlOB) throws InterruptedException { // Pedido de Venta => FACTURA LIBRO
		System.out.println("*************************************");
		System.out.println("************** Pagos ****************"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		//driver.manage().window().maximize();
		helper.sleep(6);
		urlParam = urlOB;
		
		Thread.sleep(12000);
		WebElement aplication = driver.findElement(By.xpath("//img[@name='isc_Bmain']"));
		aplication.click();
		Thread.sleep(1000);
		WebElement valor = driver.findElement(By.xpath("//input[@name='value']"));
		valor.sendKeys("Pago");
		Thread.sleep(1000);
		//System.out.println(valor.findElement(By.xpath("//*[text()='Pago']")));
		// Buscar una etiqueta hija dentro la etiqueta padre
		valor.sendKeys(Keys.DOWN);
		//System.out.println("EL VALOR" +Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.DOWN);
		valor.sendKeys(Keys.ENTER);
		helper.sleep(5);
		formulario(driver, urlOB);

	}//input[@name='referenceNo' and @class='OBFormFieldInputRequired']
	public static void formulario(WebDriver driver, String urlOB) throws InterruptedException {
		System.out.println("Cargando JSON ...formulario en proceso.");
		Helpers helper = new Helpers();
		helper.cargarHtmlFormulario(driver);
		// Nuevo pedido de compra
		driver.findElement(By.xpath("//*[@class='OBToolbarIconButton_icon_newDoc OBToolbarIconButton']")).click();
		helper.sleep(2);
		// CARGAR EL ARCHIVO JSON
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("pedidoCompras.json"));
			JSONObject jsonObject = (JSONObject) obj; // transformalo el objeto leido a objetoJSON
			// Tercero
			JSONObject innerObjectTerceros = (JSONObject) jsonObject.get("Terceros");// key padre
			int valorTerceros = (int) Math.floor(Math.random() * innerObjectTerceros.size() + 1); // cualquier tercero
			JSONObject posicionTerceros = (JSONObject) innerObjectTerceros.get("" + valorTerceros + "");// posicion data
																												// tercero
			System.out.println("DATOS DEL PAGO:");
			int attemptsB = 0;
			while (attemptsB < 3) {
				try {

					Random rnd = new Random();
					int serial = rnd.nextInt() * 9+1; 
					WebElement inpNumReferencia = driver.findElement(
							By.xpath("//input[@name='referenceNo' and @class='OBFormFieldInputRequired']")); // agregar
					inpNumReferencia.clear();
					inpNumReferencia.sendKeys(""+serial+"");
					inpNumReferencia.sendKeys(Keys.ENTER);
					
					helper.sleep(2);
					WebElement tercero = driver.findElement(By.xpath(
							"//input[@name='businessPartner' and @class='OBFormFieldSelectInput']")); // agregar
					tercero.clear();
					tercero.sendKeys((String) posicionTerceros.get("Nombre"));
					tercero.sendKeys(Keys.ENTER);
					
					// EMPIEZA EL PROCESO
				

					
					helper.sleep(1);

					break;
				} catch (Exception e) {
				}
				attemptsB++;
			}

			System.out.println("DOC TRANSACCION : Compromiso Presupuesto Operativo");
			System.out.println("TERCERO: " + (String) posicionTerceros.get("Nombre"));
		
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
		validarFormulario(driver, urlOB );  
		}	
	
	public static void validarFormulario(WebDriver driver, String UrlParam) {// validar
		Helpers helper = new Helpers();
		helper.sleep(2);
		driver.findElement(By.xpath("//*[text()='dd Details'] | //*[text()='gregar detalles']")).click();
		completarPago(driver, helper,UrlParam);
	}
	public static void completarPago(WebDriver driver, Helpers helper, String UrlParam) {
	
		helper.sleep(3);

		driver.findElement(By.xpath("//div[@eventproxy='isc_OBPickAndExecuteGrid_0filterEditor' and @class='OBGridFilterBase']//*[@class='OBGridFilterFunnelIcon']//img[@src='"+UrlParam+"/web/org.openbravo.userinterface.smartclient/openbravo/skins/Default/org.openbravo.client.application/images/grid/funnel-icon.png']")).click();
		helper.sleep(2);
		driver.findElement(By.xpath("//div[@eventproxy='isc_OBPickAndExecuteGrid_0_headerLayout' and @class='normal']//span")).click();
		helper.myScroll(driver, "//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']");
		helper.sleep(1);
		driver.findElement(By.xpath("//*[@class='OBFormFieldSelectInputRequired' and @name='document_action']")).sendKeys("Process Made Payment(s) and Withdrawal",Keys.ENTER);
		helper.sleep(2);
		driver.findElement(By.xpath("//*[text()='Done']")).click();
		System.out.println("*****************************************************************************");
		System.out.println("************************* FIN DEL PROCESO PAGOS ******************");
		System.out.println("*****************************************************************************");
		driver.navigate().to(UrlParam);
	}
	
}

