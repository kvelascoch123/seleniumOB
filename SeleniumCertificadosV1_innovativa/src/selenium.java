import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class selenium {
 
	public static void main(String[] args) throws InterruptedException {
		System.out.println("**********************************************************");
		System.out.println("PROCESO AUTOMARICO SELENIUM OPENBRAVO VERSION 5_OB CERTIFICADOS");
		System.out.println("**********************************************************");
		seleniumStart(); 
	}    
	// ************** SELENIUM SATRT ***************************
	public static void seleniumStart() throws InterruptedException {
		
		//  System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");//ONLY FOR WINDWOS DWCOMMNET
		
		WebDriver driver = new ChromeDriver();
		String urlOB = JOptionPane.showInputDialog(null, "Ingresar el Url del Ambiente.",
						"http://186.69.209.147:9091/innovativaerp");
		String userOB = JOptionPane.showInputDialog(null, "User", "Openbravo");
		String passwordOB = JOptionPane.showInputDialog(null, "Conraseña del Ambiente", "1234");
		String valor = procesosSelenium(); // DETERMINAR EL PROCESO A REALIZAR
		// INSTANCIAR CLASES 
		PedidoCompra pedidoCompra = new PedidoCompra();
		Compras compra = new Compras();
		Pagos pago = new Pagos();
		int numeroPruebas = Integer.parseInt(JOptionPane.showInputDialog(null, "Número de pedidos de venta a realizar",1)); 

		switch (valor) {
		case "Pedido de Compra, Factura (proveedor) con Certificado":
			int numDocFacturaProv = Integer.parseInt(JOptionPane.showInputDialog(null, "Nº Factura a iniciar el proceso: 000-000-0000000** solo 2 ultimos digitos faltantes", 0));

			// 1) crear pedido,  2)Crear la factura(proveedor)
			startSelenium(driver, urlOB, userOB, passwordOB);
			for (int i = 0; i < numeroPruebas; i++) {
				System.out.println("NUMERO DE PROCESO " + i);

				pedidoCompra.generarPedidoCompra(driver, urlOB); // PEDIDO COMPRA
				compra.generarCompras(driver, urlOB, i, numDocFacturaProv); // FACTURA PROVEEDOR
				pago.generarPago(driver, urlOB); // PAGO 
			}  
			
			break;
			
		case "Compromiso":


			startSelenium(driver, urlOB, userOB, passwordOB);
			for (int i = 0; i < numeroPruebas; i++) {
				System.out.println("NUMERO DE PROCESO " + i);

				pedidoCompra.generarPedidoCompra(driver, urlOB); // PEDIDO COMPRA
				
			}  
			
			break;
			
		case "Factura proveedor":
			int numDocFacturaProvOrigin = Integer.parseInt(JOptionPane.showInputDialog(null, "Nº Factura a iniciar el proceso: 000-000-0000000** solo 2 ultimos digitos faltantes", 0));

			// 1) crear pedido,  2)Crear la factura(proveedor)
			startSelenium(driver, urlOB, userOB, passwordOB);
			for (int i = 0; i < numeroPruebas; i++) {
				System.out.println("NUMERO DE PROCESO " + i);

				compra.generarCompras(driver, urlOB, i, numDocFacturaProvOrigin); // FACTURA PROVEEDOR
			}  
			
			break;
		case "Pago":
			// 1) crear pedido,  2)Crear la factura(proveedor)
			startSelenium(driver, urlOB, userOB, passwordOB);
			for (int i = 0; i < numeroPruebas; i++) {
				System.out.println("NUMERO DE PROCESO " + i);
				System.out.println("POR DEFECTO SOLO REALIZA EL PAGO DEL DOCUMENTO numeroDOcumentos.json, ULTIMA FACTURA PROVEEDOR");
				pago.generarPago(driver, urlOB); // PAGO 
			}  
			
			break;	
		}
		
		
		
	}
	// **************************************************************++
	// PROCESOS
	public static String procesosSelenium() { 
		// SELECIONAR EL PROCESO AUTOMATICO
		String[] list = { "Pedido de Compra, Factura (proveedor) con Certificado", "Compromiso", "Factura proveedor", "Pago"};
		JComboBox jcb = new JComboBox(list); 
		jcb.setEditable(false);
		JOptionPane.showMessageDialog(null, jcb, "Procesos automáticos existentes", JOptionPane.QUESTION_MESSAGE);
		String version;
		version = (String) jcb.getSelectedItem();
		System.out.println(version);
		return version;
	}

	public static void loggin(WebDriver driver, String userOB, String passwordOB) {
		Helpers helper = new Helpers();
		helper.sleep(1);
		WebElement userInput = driver.findElement(By.name("user"));
		WebElement passwordInput = driver.findElement(By.name("password"));
		userInput.clear();
		userInput.sendKeys(userOB);
		passwordInput.clear();
		passwordInput.sendKeys(passwordOB);
		passwordInput.sendKeys(Keys.ENTER);
		
	}

	public static void startSelenium(WebDriver driver, String urlAmbiente, String usuarioOB, String password)
			throws InterruptedException {
		driver.get(urlAmbiente);
		Thread.sleep(3 * 1000);
		loggin(driver, usuarioOB, password);
	}

	
}