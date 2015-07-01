package org.luksze.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class Configuration extends HashMap<Object, Object> {

    public Configuration() {
        Properties properties = createProperties(readConfigurationFile());
        for (String key : properties.stringPropertyNames()) {
            String value = readProperty(properties, key);
            System.out.println("property: " + key + " property:" + value);
            put(key, value);
        }
    }

    private String readProperty(Properties properties, String key) {
        String property = properties.getProperty(key);
        if (property.startsWith("$")) {
            return System.getenv(property.substring(1));
        }
        return property;
    }

    private InputStream readConfigurationFile() {
        return Configuration.class.getClassLoader().getResourceAsStream("connection.properties");
    }

    private Properties createProperties(InputStream resourceAsStream) {
        try {
            return propertiesFrom(resourceAsStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Properties propertiesFrom(InputStream resourceAsStream) throws IOException {
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        return properties;
    }
}
