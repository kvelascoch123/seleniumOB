import org.openqa.selenium.WebDriver;

public class AlbaranProveedor {
	Helpers helper = new Helpers();
	public void generarAlbaranProveedor(WebDriver driver) throws InterruptedException{
		System.out.println("*************************************");
		System.out.println("******** ALBARAN (PROVEEDOR) ********"); // HECHO EN MUSEOS
		System.out.println("*************************************");
		driver.manage().window().maximize();
		helper.sleep(6);
				
	//	for (int i = 0; i < numeroRepeticionesProceso; i++) {
		helper.buscarVentana(driver,"Albaran (Proveedor)");
		crearAlbaran(driver);
	//	}
	}
	private void crearAlbaran(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
}
