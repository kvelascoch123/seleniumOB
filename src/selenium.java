import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class selenium {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("**************************************");
		System.out.println("PROCESO AUTOMARICO SELENIUM OPENBRAVO");
		System.out.println("**************************************");
		seleniumStart();
	}
 
	// ************** SELENIUM SATRT ***************************
	public static void seleniumStart() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		// Ambiente.","http://192.168.1.28:8080/museospreprod");
		String urlOB = JOptionPane.showInputDialog(null, "Ingresar el Url del Ambiente.",
				"http://186.69.209.150:8071/museos");
		//String urlOB = JOptionPane.showInputDialog(null, "Ingresar el Url del Ambiente.",
		//		"http://192.168.1.28:8080/museospreprod");
		String userOB = JOptionPane.showInputDialog(null, "User", "Openbravo");
		String passwordOB = JOptionPane.showInputDialog(null, "Conraseña del Ambiente", "1234");

		String valor = procesosSelenium(); // DETERMINAR EL PROCESO A REALIZAR
		Terceros tercero = new Terceros();
		PedidoVentas pedidoVenta = new PedidoVentas();
		Compras compras = new Compras();

		switch (valor) {
		case "Crear Tercero":
			startSelenium(driver, urlOB, userOB, passwordOB);
			tercero.crearTerceros(driver, urlOB, "SELENIUM SIDESOFT");

			break;
		case "Pedido de Ventas. Factura (Cliente)":
			int numeroPruebas = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero de pruebas a realizar",1)); 
				startSelenium(driver, urlOB, userOB, passwordOB);
				pedidoVenta.pedidoDeVentas(driver, urlOB);	
			break; 
		case "Compras": //Factura proveedor
			int numeroCompras= Integer.parseInt(JOptionPane.showInputDialog(null, "Numero de compras a realizar",1)); 
				startSelenium(driver, urlOB, userOB, passwordOB);
				compras.generarCompras(driver, urlOB, numeroCompras);
			break;
		case "Pedido de Compra": // COMPROMISO => museos
			PedidoCompra pedidoCompra = new PedidoCompra();
			startSelenium(driver, urlOB, userOB, passwordOB);
			pedidoCompra.generarPedidoCompra(driver, urlOB);
			break;
		case "Albaran (Proveedor)": // COMPROMISO => museos
			AlbaranProveedor albaran = new AlbaranProveedor();
			albaran.generarAlbaranProveedor(driver);
			break;
		case "Proceso Completo":
			System.out.println("-------------- Proceso: Proceso Completo ---------------");
			startSelenium(driver, urlOB, userOB, passwordOB);
			Terceros.crearTerceros(driver, urlOB, "SELENIUM SIDESOFT");	
			pedidoVenta.pedidoDeVentas(driver, urlOB);	
			//compras.generarCompras(driver, urlOB, numeroCompras);
			break;
		default:
		}

	}
	// **************************************************************++

	// PROCESOS
	public static String procesosSelenium() { // ESCOJA EL PROCESO
		// SELECIONAR EL PROCESO AUTOMATICO
		String[] list = { "Crear Tercero", "Pedido de Ventas. Factura (Cliente)",
				"Compras" ,"Pedido de Compra" , "Albaran (Proveedor)", "Proceso Completo" };
		JComboBox jcb = new JComboBox(list); 
		jcb.setEditable(false);
		JOptionPane.showMessageDialog(null, jcb, "Proceso automatico existente", JOptionPane.QUESTION_MESSAGE);
		String version;
		version = (String) jcb.getSelectedItem();
		System.out.println(version);
		return version;
	}

	public static void loggin(WebDriver driver, String userOB, String passwordOB) {
		Helpers helper = new Helpers();
		helper.sleep(1);
		// EMPEZAR A VALIDAR EXISTENCIA DE FRAMESET PARA LOS AMBIENTES EXCLUSIVOS 
		// EJEMPLO: AMBIENTE DE MB... empieza con iframe.. extiende de 

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
		//driver.manage().window().maximize();
		Thread.sleep(3 * 1000);
		loggin(driver, usuarioOB, password);
	}
	// CREAR TERCEROS
	public static void terceros() {
		
	}
	
}