package fi.ambientia.atlassian.properties;

import com.atlassian.config.util.BootstrapUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Component
public class PluginProperties {

    private static final Logger logger = Logger.getLogger(PluginProperties.class);
    private final Properties p;

    public PluginProperties() {

        final String applicationHome = getApplicationHome();
        p = new Properties();
        final File file = new File(applicationHome, "ABTestPlugin.properties");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream( file );

            try {
                p.load( fileInputStream );
            } catch (IOException e) {
                logger.warn("Cannot load Properties-file located at " + file.getAbsolutePath() + ". That's fine - let's use the defaults", e);
            }

        } catch (FileNotFoundException e) {
            logger.warn("Cannot load Properties-file located at " + file.getAbsolutePath() + ". That's fine - let's use the defaults", e);
        }
    }

    protected String getApplicationHome() {
        return BootstrapUtils.getBootstrapManager().getApplicationHome();
    }

    public String propertyOrDefault(String key, String defaultString) {
        if ( p.containsKey(key) ){
            return (String) p.get( key );
        }
        return defaultString;
    }

    public Integer propertyOrDefault(String key, int i) {
        if ( p.containsKey(key) ){
            return Integer.valueOf ( (String) p.get( key ) );
        }
        return i;
    }
}
