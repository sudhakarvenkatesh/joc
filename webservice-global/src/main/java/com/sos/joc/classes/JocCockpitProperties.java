package com.sos.joc.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.misc.ParameterSubstitutor;

public class JocCockpitProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(JocCockpitProperties.class);
    private Properties properties;
    private String propertiesFile;
    private ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();

    public JocCockpitProperties() {
        super();
        propertiesFile = "/joc.properties";
        properties = new Properties();
        readProperties();
    }

    private void substituteProperties() {
        parameterSubstitutor = new ParameterSubstitutor();
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            parameterSubstitutor.addKey(key, value);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    private void readProperties() {
        final InputStream stream = this.getClass().getResourceAsStream(propertiesFile);

        if (stream != null) {
            try {
                properties.load(stream);
                substituteProperties();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        readProperties();
    }

    public String getProperty(String property) {
        String s = properties.getProperty(property);
        if (s != null){
            s = parameterSubstitutor.replaceEnvVars(s);
            s = parameterSubstitutor.replace(s);
        }
        return s;
    }
    
    public String getProperty(String property, String defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        }else{
            return s;
        }
    }

    public int getProperty(String property, int defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        }else{
            try{
            return Integer.parseInt(s);
            }catch (NumberFormatException e){
                LOGGER.warn(String.format("Property Value for %s is not an Integer. Returning 0 %s",property,e.getMessage()));
                return 0;
            }
        }
    }

}
