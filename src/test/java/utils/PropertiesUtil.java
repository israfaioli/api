package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil extends Properties {

    public static String obterProperties(String propertiesKey) {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        FileInputStream file = null;
        try {
            file = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/application.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            propertiesUtil.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesUtil.getProperty(propertiesKey);
    }
}
