package com.Almatar.dataproviders;
import com.Almatar.constants.Almatar.AlmatarExcelConstants;
import com.Almatar.constants.Almatar.AlmatarFileNameConstants;
import com.Almatar.utilities.ExcelReader;
import com.Almatar.utilities.PropertiesReader;
import org.testng.annotations.DataProvider;

import java.util.Properties;

public class AlmatarDataProvider {
    private final PropertiesReader propertiesReader = new PropertiesReader();
    private final Properties testDataProperties = propertiesReader.loadPropertiesFromFile(AlmatarFileNameConstants.EXCEL_CONFIGURATIONS_FILE_PATH);
    private final ExcelReader excelHandler = new ExcelReader();

    @DataProvider(name = "selectingAllClasses")
    public Object[][] selectingAllClasses() {
        return excelHandler.readExcelData(testDataProperties.getProperty(AlmatarExcelConstants.Almatar_EXCEL_FILE), AlmatarExcelConstants.Different_Types_Of_Class);
    }

}
