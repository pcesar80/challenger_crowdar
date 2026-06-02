import Utils.DriverFactory;
import Utils.ReporteEjecucion;
import org.openqa.selenium.WebDriver;
import seccion.Login;
import seccion.MercadoLibreAPI;

public class challenger_automa {

    public static void main(String[] args) {

        WebDriver driver = DriverFactory.crearDriver();

        try {

            new Login().ejecutar(driver);

            new MercadoLibreAPI().ejecutar(driver);

        } finally {

            ReporteEjecucion.generarReporteFinal();

            driver.quit();
        }
    }
}