package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager configManager;
    private static final Properties properties = new Properties();

    private ConfigManager() throws IOException {
        InputStream inputStream = ConfigManager.class.getResourceAsStream("/config.properties");
        properties.load(inputStream);
    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static ConfigManager getInstance() {
        if (configManager == null) {
            synchronized (ConfigManager.class) {
                try {
                    configManager = new ConfigManager();
                } catch (IOException ex) {
                    Log.error(ex.getMessage());
                }
            }
        }
        return configManager;
    }

    public int getUserId() {
        String id = getProperty("access_token");
        System.out.println(id);
        return Integer.parseInt(getProperty("user_id"));
    }

    public int getUserIdWithRestrictedPhoto() {
        return Integer.parseInt(getProperty("user_id_with_restricted_photo"));
    }

    public String getAccessToken() {
        return getProperty("access_token");
    }

}
