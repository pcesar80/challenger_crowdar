package Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

    public static WebDriver crearDriver() {

        String browser = System.getProperty("browser", "chrome");
        String headless = System.getProperty("headless", "false");

        switch (browser.toLowerCase()) {

            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();

                if ("true".equals(headless)) {
                    options.addArguments("--headless");
                }

                WebDriver driver = new FirefoxDriver(options);

                if (!"true".equals(headless)) {
                    driver.manage().window().maximize();
                }

                return driver;
            }

            case "edge": {
                EdgeOptions options = new EdgeOptions();

                if ("true".equals(headless)) {
                    options.addArguments("--headless=new");
                }

                WebDriver driver = new EdgeDriver(options);

                if (!"true".equals(headless)) {
                    driver.manage().window().maximize();
                }

                return driver;
            }

            case "chrome":
            default: {
                ChromeOptions options = new ChromeOptions();

                if ("true".equals(headless)) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                } else {
                    options.addArguments("--start-maximized");
                }

                options.addArguments("--remote-allow-origins=*");

                return new ChromeDriver(options);
            }
        }
    }
}