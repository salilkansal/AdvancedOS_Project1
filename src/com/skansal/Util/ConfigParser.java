package com.skansal.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This reads the configuration value from the properties file and returns it.
 */
public class ConfigParser {

    public static String getStringValue(String key) {
        try {
            Properties properties = new Properties();
            String CONFIGURATION_PATH = "AOSProject/configuration/config.properties";
            InputStream inputStream = new FileInputStream(CONFIGURATION_PATH);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}