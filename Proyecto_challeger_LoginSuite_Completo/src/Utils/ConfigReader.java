package Utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    private Properties props = new Properties();

    public ConfigReader(String path) {
        try {
            props.load(new FileInputStream(path));
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo archivo: " + path);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
