package seccion;

import Utils.Evidencias;
import Utils.ReporteEjecucion;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

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

        // BACKPACK
        errores += toggleProducto(
                driver,
                wait,
                "add-to-cart-sauce-labs-backpack",
                "remove-sauce-labs-backpack",
                "#item_4_img_link > img",
                "Sauce Labs Backpack"
        );

        // BOLT T-SHIRT
        errores += toggleProducto(
                driver,
                wait,
                "add-to-cart-sauce-labs-bolt-t-shirt",
                "remove-sauce-labs-bolt-t-shirt",
                "#item_1_img_link > img",
                "Sauce Labs Bolt T-Shirt"
        );

        if (errores == 0) {
            System.out.println("✅ Módulo Producto ejecutado correctamente");
        }

        return errores;
    }

    private int toggleProducto(
            WebDriver driver,
            WebDriverWait wait,
            String addBtn,
            String removeBtn,
            String imgSelector,
            String nombre
    ) {

        int errores = 0;

        try {

            // CLICK ADD
            wait.until(ExpectedConditions.elementToBeClickable(By.id(addBtn))).click();
            System.out.println("➡️ Add clicked: " + nombre);

            // VALIDAR CAMBIO A REMOVE
            WebElement remove = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(removeBtn))
            );

            if (!remove.getText().equalsIgnoreCase("Remove")) {
                throw new AssertionError("El botón no cambió a REMOVE");
            }

            ReporteEjecucion.registrarOK(nombre + " agregado correctamente");

        } catch (Exception e) {

            errores++;

            Evidencias.capturarPantalla(driver, "Error_Add_" + nombre);

            ReporteEjecucion.registrarError(
                    "Toggle Add " + nombre,
                    e.getMessage()
            );

            return errores;
        }

        // =========================
        // VALIDACIÓN IMAGEN (SOFT - NO FRENA EJECUCIÓN)
        // =========================
        try {

            WebElement img = driver.findElement(By.cssSelector(imgSelector));

            if (!img.isDisplayed()) {

                ReporteEjecucion.registrarError(
                        "Imagen " + nombre,
                        "Imagen no visible"
                );

                Evidencias.capturarPantalla(driver, "Error_Img_" + nombre);

            } else {

                String src = img.getAttribute("src");

                if (src == null || src.isEmpty()) {

                    ReporteEjecucion.registrarError(
                            "Imagen " + nombre,
                            "Imagen sin SRC"
                    );

                    Evidencias.capturarPantalla(driver, "Error_Img_" + nombre);

                } else if (src.contains("sl-404")) {

                    ReporteEjecucion.registrarError(
                            "Imagen " + nombre,
                            "Imagen incorrecta (404 detectado): " + src
                    );

                    Evidencias.capturarPantalla(driver, "Error_Img_" + nombre);

                } else {

                    System.out.println("🖼 Imagen válida: " + nombre);
                    System.out.println("🔗 SRC: " + src);

                    ReporteEjecucion.registrarOK(
                            "Imagen " + nombre + " validada correctamente"
                    );
                }
            }

        } catch (Exception e) {

            ReporteEjecucion.registrarError(
                    "Imagen " + nombre,
                    e.getMessage()
            );

            Evidencias.capturarPantalla(driver, "Error_Img_" + nombre);
        }

        try {

            // CLICK REMOVE
            wait.until(ExpectedConditions.elementToBeClickable(By.id(removeBtn))).click();
            System.out.println("➡️ Remove clicked: " + nombre);

            // VALIDAR VUELTA A ADD
            WebElement add = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(addBtn))
            );

            if (!add.getText().equalsIgnoreCase("Add to cart")) {
                throw new AssertionError("El botón no volvió a ADD TO CART");
            }

            ReporteEjecucion.registrarOK(nombre + " removido correctamente");

        } catch (Exception e) {

            errores++;

            Evidencias.capturarPantalla(driver, "Error_Remove_" + nombre);

            ReporteEjecucion.registrarError(
                    "Toggle Remove " + nombre,
                    e.getMessage()
            );
        }

        return errores;
    }
}