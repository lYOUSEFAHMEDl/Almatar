package com.Almatar.utilities;

import com.Almatar.constants.GeneralConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Helper {

    private static final PropertiesReader propertiesReader = new PropertiesReader();
    private static final Properties generalConfigurationProperties = propertiesReader.loadPropertiesFromFile(GeneralConstants.PATHS_CONFIGURATION_FILE_NAME);


}
