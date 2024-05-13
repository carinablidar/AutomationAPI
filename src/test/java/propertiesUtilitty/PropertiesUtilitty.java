package propertiesUtilitty;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesUtilitty {

    private Properties properties;

    public PropertiesUtilitty(String fileName) {
        loadFile(fileName);
    }

    @SneakyThrows(Exception.class)
    private void loadFile(String fileName) {
        properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/"+fileName+".properties");
        properties.load(fileInputStream);
    }

    public HashMap<String, String> getAllData() {
        HashMap<String, String> testData = new HashMap<>();
        for(String key: properties.stringPropertyNames()) {
            testData.put(key, properties.getProperty(key));
        }
        return testData;
    }
}
