package seccion;

import Utils.Evidencias;
import Utils.ReporteEjecucion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Producto implements ModuloEjecucion {

    @Override
    public String getNombre() {
        return "Producto";
    }

    @Override
    public int ejecutar(WebDriver driver) {

        int errores = 0;

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        // Backpack
        try {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("add-to-cart-sauce-labs-backpack")
                    )
            ).click();

            System.out.println("➡️ Click en Sauce Labs Backpack");

        } catch (Exception e) {

            errores++;

            Evidencias.capturarPantalla(
                    driver,
                    "Error_Boton_Backpack"
            );

            ReporteEjecucion.registrarError(
                    "Producto Backpack",
                    e.getMessage()
            );
        }

        // Bike Light
        try {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("add-to-cart-sauce-labs-bike-light")
                    )
            ).click();

            System.out.println("➡️ Click en Sauce Labs Bike Light");

        } catch (Exception e) {

            errores++;

            Evidencias.capturarPantalla(
                    driver,
                    "Error_Boton_BikeLight"
            );

            ReporteEjecucion.registrarError(
                    "Producto Bike Light",
                    e.getMessage()
            );
        }

        if (errores == 0) {
            System.out.println("✅ Módulo Producto ejecutado");
        }

        return errores;
    }
}