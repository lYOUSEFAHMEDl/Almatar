package com.Almatar.utilities;

import com.Almatar.constants.GeneralConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.SystemProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExcelReader {

    public Object[][] readExcelData(String filePath, String sheetName) {
        filePath = SystemProperties.getProperty(GeneralConstants.USER_DIR)+filePath;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                com.Almatar.utilities.Log.error("Sheet not found");
                return new Object[][]{};
            }

            // Automatic skipping first header row
            int lastRowNum = sheet.getLastRowNum() + 1;
            if (lastRowNum < 1) {
                com.Almatar.utilities.Log.error("No data found in sheet " + sheetName);
                return new Object[][]{};
            }

            int lastCellNum = sheet.getRow(0).getLastCellNum();
            List<Object[]> data = new ArrayList<>();

            for (int i = 0; i < lastRowNum; i++) {
                Row row = sheet.getRow(i + 1); // Skip header row
                if (row != null) {
                    List<Object> rowData = new ArrayList<>();
                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                String stringValue = cell.getStringCellValue();
                                if (stringValue != null && !stringValue.trim().isEmpty()) {
                                    rowData.add(stringValue);
                                } else {
                                    rowData.add("");
                                }
                                break;
                            case NUMERIC:
                                rowData.add(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                rowData.add(cell.getBooleanCellValue());
                                break;
                            default:
                                rowData.add(" ");
                        }
                    }
                    if (!rowData.isEmpty()) {
                        data.add(rowData.toArray());
                    }
                }
            }

            return data.toArray(new Object[0][]);

        } catch (IOException e) {
            System.out.println("Error reading the Excel file: " + e.getMessage());
            return new Object[][]{};
        }
    }

}
