package com.Almatar.tests.base;
import com.Almatar.constants.GeneralConstants;
import com.Almatar.utilities.Log;
import com.Almatar.utilities.PropertiesReader;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
public class TestBase {
    public static WebDriver driver;
    public NgWebDriver ngDriver;
    public JavascriptExecutor jse;
    public SoftAssert softAssert;
    protected static final PropertiesReader propertiesReader = new PropertiesReader();
    private static final Properties generalConfigurationProps = propertiesReader.loadPropertiesFromFile(GeneralConstants.GENERAL_CONFIGURATION_FILE_NAME);
    private static final Properties pathsProperties = propertiesReader.loadPropertiesFromFile(GeneralConstants.PATHS_CONFIGURATION_FILE_NAME);

    @BeforeTest(alwaysRun = true)
    public void testSetup() throws Exception {
        try {
            Log.info("Initializing WebDriver");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(setChromeOption());
            jse = (JavascriptExecutor) driver;
            ngDriver = new NgWebDriver(jse).withRootSelector("\"app-root\"");
            driver.get("https://almatar.com/en/");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            Log.info("Selenium WebDriver was initialized successfully");
        } catch (Exception e) {
            Log.error("Error occurred while initializing selenium WebDriver", e);
            driver.quit();
            System.exit(1);
        }
}

//    @AfterTest
//    public void quit() throws InterruptedException {
//        Log.info("Closing selenium Web driver after test");
//        driver.quit();
//    }

    private ChromeOptions setChromeOption() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default.content_settings.popups", 0);
        options.setExperimentalOption("prefs", chromePrefs);
        options.setAcceptInsecureCerts(true);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
//        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-features=SSLClientCertificate,HTTPSOnlyMode");
        options.addArguments("--disable-features=HttpsOnlyMode");
        options.addArguments("--disable-features=SSLVisualIndicator"); // Disable HTTPS visual indicator
        options.addArguments("--ignore-certificate-errors"); // Ignore SSL certificate issues
        options.addArguments("--disable-http2"); // Disable HTTP/2, which can cause issues
        options.addArguments("--allow-running-insecure-content");
        //options.addArguments("--incognito");
        options.addArguments("--disable-ssl-keyring"); // Disable SSL keyring
        // Automatically allow multiple downloads
        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
        //hide popup Chrome message to save password
        chromePrefs.put("credentials_enable_service", false);
        //disable password manager
        chromePrefs.put("profile.password_manager_enabled", false);
        return options;
    }

}

