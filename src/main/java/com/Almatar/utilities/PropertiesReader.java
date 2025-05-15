package com.Almatar.utilities;

import com.Almatar.constants.GeneralConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesReader {

    private Path getConfigurationFilePath(String filePath) {
        return Paths.get(System.getProperty(GeneralConstants.USER_DIR),
                filePath);
    }

    public Properties loadPropertiesFromFile(String fileName) {
        Properties properties = new Properties();
        Path filePath = getConfigurationFilePath(fileName);

        if (!Files.exists(filePath)) {
            Log.error("Error: File '" + fileName + "' does not exist.");
            return properties;
        }

        try (InputStream input = Files.newInputStream(filePath)) {
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException e) {
            Log.error("Error loading properties from file: " + e.getMessage());
        }
        return properties;
    }


}
