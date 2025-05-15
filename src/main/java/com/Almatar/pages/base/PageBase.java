package com.Almatar.pages.base;
import com.Almatar.constants.GeneralConstants;
import com.Almatar.utilities.CustomExpectedConditions;
import com.Almatar.utilities.Log;
import com.Almatar.utilities.PropertiesReader;
import com.Almatar.utilities.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class PageBase {

    public WebDriver driver;
    JavascriptExecutor jse;
    Actions actions;

    protected Waits waits;
    protected CustomExpectedConditions customExpectedConditions;

    public static PropertiesReader propertiesReader = new PropertiesReader();
    public static final Properties pathsProperties = propertiesReader.loadPropertiesFromFile(GeneralConstants.PATHS_CONFIGURATION_FILE_NAME);
     private static final Duration waitTime = Duration.ofSeconds(30);
    private static final Duration pollingTime = Duration.ofMillis(500);


    public PageBase(WebDriver driver) {
        this.driver = driver;
        this.jse = (JavascriptExecutor) driver;
        this.customExpectedConditions = new CustomExpectedConditions(driver);
        this.waits = new Waits(driver);
    }


    //////////////////////////////// ELEMENTS HANDLING ////////////////////////////////
    public WebElement findElement(By locator) {
        return waits.waitForElementToBePresent(locator);
    }

    public List<WebElement> findElements(By locator) {

        return driver.findElements(locator);
    }

    public boolean isDisplayed(By locator) {
        return findElement(locator).isDisplayed();
    }

    public boolean waitForElementInvisibility(By locator) {
        try {

            waits.waitForInvisibility(locator);
            return true;  // Element became invisible within the timeout
        }   catch (TimeoutException e) {
            return false; // Timeout occurred before element became invisible
        }
    }

    public boolean isElementPresent(By locator) {
        try {
            findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }


    //////////////////////////////// LIST HANDLING ////////////////////////////////
    public WebElement getElementFromListByIndex(By listLocator, int index) {
        waits.waitForVisibility(listLocator);
        List<WebElement> elements = driver.findElements(listLocator);
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for list of size " + elements.size());
        }
        return elements.get(index);
    }

    public WebElement getElementFromListByText(By listLocator, String targetText) throws Exception {
        waits.waitForVisibility(listLocator);
        List<WebElement> elements = driver.findElements(listLocator);
        for (WebElement element : elements) {
            if (element.getText().trim().toLowerCase().contains(targetText.trim().toLowerCase())) {
                return element;
            }
        }
        throw new NoSuchElementException("Element with text " + targetText + " not found");
    }

    public int getElementIndexByText(By locator, String text) {
        List<WebElement> elements = driver.findElements(locator);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().equals(text)) {
                return i;
            }
        }
        throw new NoSuchElementException("Element with text " + text + " not found");
    }

    public WebElement getElementFromListByWholeText(By listLocator, String targetText) throws Exception {
        waits.waitForVisibility(listLocator);
        List<WebElement> elements = findElements(listLocator);
        for (WebElement element : elements) {
            if (element.getText().trim().equalsIgnoreCase(targetText.trim())) {
                return element;
            }
        }
        throw new NoSuchElementException("Element with text " + targetText + " not found");
    }

    public String getTheElementTextByIndex(By locator, int index) throws Exception {
        if (locator != null) {
            return getText(getElementFromListByIndex(locator, index));
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public String getTheLastElementText(By locator) throws Exception {
        if (locator != null) {
            return getText(getElementFromListByIndex(locator, findElements(locator).size() - 1));
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void selectFromListByIndex(By listLocator, int index) throws Exception {
        if (listLocator != null) {
            waits.waitForVisibilityOfList(listLocator);
            waits.waitForElementToBeClickable(getElementFromListByIndex(listLocator, index)).click();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void selectFromListByText(By listLocator, String targetText) throws Exception {
        waits.waitForVisibility(listLocator);
        getElementFromListByText(listLocator, targetText).click();
    }

    public void selectFromListByWholeText(By listLocator, String targetText) throws Exception {
        waits.waitForVisibility(listLocator);
        getElementFromListByWholeText(listLocator, targetText).click();
    }

    // Method to click on a random element in a list of WebElements
    public void selectRandomElementFromList(By locator) {
        waits.waitForVisibility(locator);
        List<WebElement> elements = findElements(locator);
        if (elements == null || elements.isEmpty()) {
            Log.error("The list of WebElements is empty or null.");
            return;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(elements.size());
        waits.waitForElementToBeClickable(elements.get(randomIndex)).click();
    }

    public List<String> getListValues(By list) throws Exception {
        waits.waitForVisibility(list);
        List<String> values = new LinkedList<>();
        List<WebElement> elementList = findElements(list);
        for (WebElement element : elementList) {
            values.add(getText(element));
        }
        return values;
    }

    public List<String> getListValues(List<WebElement> elements) throws Exception {
        List<String> textList = new ArrayList<>();
        for (WebElement element : elements) {
            textList.add(getText(element));
        }
        return textList;
    }

    public int getListSize(By list) {
        return findElements(list).size();
    }

    public boolean checkTextInListOfElements(By locator, String targetText) throws Exception {
        waits.waitForListToBeNotEmpty(locator);
        List<String> elementsText = getListValues(locator);
        for (String str : elementsText) {
            if (str.contains(targetText)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkValueInList(List<String> options, String... searchTerms) {
        for (String term : searchTerms) {
            boolean found = false;
            for (String option : options) {
                if (option.contains(term)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public boolean isTextPresentInAllElements(By locator, String targetText) throws Exception {
        waits.waitForVisibilityOfList(locator);
        List<WebElement> elements = findElements(locator);
        for (WebElement element : elements) {
            String elementText = getText(element);
            if (!elementText.contains(targetText)) {
                return false;
            }
        }
        return true;
    }
    public boolean isTextPresentInAllElements(List<String> list, String targetText) throws Exception {
        for (String str : list) {
            if (!str.contains(targetText)) {
                return false;
            }
        }
        return true;
    }


    public boolean checkItemExistenceInPaginatedTable(String targetText, By currentPageItemList, By lastPageArrow, By firstPageArrow, By nextPageButton) throws Exception {
        waits.waitForVisibilityOfList(currentPageItemList);
        if (getAttributeValue(lastPageArrow, "class").contains("p-disabled")) { // Check if only one page exists by verifying if the last page arrow is disabled
            return checkTextInListOfElements(currentPageItemList, targetText); // Single page check
        } else { // More than one page exists, Navigate to the last page and check for the item
            scrollAndClickByJSE(lastPageArrow);
            waits.waitForVisibilityOfList(currentPageItemList);
            if (checkTextInListOfElements(currentPageItemList, targetText)) {
                return true;
            }
            scrollAndClickByJSE(firstPageArrow); // Return to the first page
            waits.waitForVisibilityOfList(currentPageItemList);
            do { // Iterate through each page, checking for the target item
                if (checkTextInListOfElements(currentPageItemList, targetText)) {
                    return true;
                }
                if (!getAttributeValue(nextPageButton, "class").contains("p-disabled")) { // Proceed to the next page if the button is enabled
                    scrollAndClickByJSE(nextPageButton);
                    waits.waitForVisibilityOfList(currentPageItemList);
                } else {
                    break; // Exit the loop if no more pages exist
                }
            } while (true);
        }
        return false; // Item not found on any page
    }


    //////////////////////////////// SCROLL HANDLING ////////////////////////////////
    public void scrollToElement(By locator) {
        jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView({block: 'center'});", findElement(locator));
        waits.waitForElementToBeInViewport(locator);
    }

    public void scrollToElement(WebElement element) {
        jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        waits.waitForElementToBeInViewport(element);
    }

    public void scrollIntoViewAndClick(By locator) throws Exception {
        scrollToElement(locator);
        clickJSE(locator);
    }

    public void scrollDown() {
        jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,200)", "");
    }

    public void scrollToPageEnd() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollIntoView(By locator) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", findElement(locator));
        waits.waitForElementToBeInViewport(locator);
    }

    public void scrollToElementUsingTextPresentInElement(By locator) {
        jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView();", findElement(locator));
        waits.waitForTextToBePresentInElement(locator);
    }

    public void scrollEndOfPageUsingKeys(By locator) {
        driver.findElement(locator).sendKeys(Keys.END);
    }

    public void clearTextField(By locator) {
        waits.waitForElementToBeClickable(locator);
        WebElement textField = findElement(locator);
        textField.clear();
        String currentText = getAttributeValue(locator, "value");
        for (int i = 0; i < currentText.length(); i++) {
            textField.sendKeys(Keys.BACK_SPACE);
        }
    }


    //////////////////////////////// CLICK HANDLING////////////////////////////////
    public void click(By locator) throws Exception {
        if (locator != null) {
            waits.waitForElementToBeClickable(locator).click();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void click(WebElement element) throws Exception {
        if (element != null) {
            waits.waitForElementToBeClickable(element).click();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void clickJSE(By locator) throws Exception {
        if (locator != null) {
            jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].click();", waits.waitForElementToBeClickable(locator));
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void clickJSE(WebElement element) throws Exception {
        if (element != null) {
            jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].click();", waits.waitForElementToBeClickable(element));
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void scrollAndClickByJSE(By locator) throws Exception {
        if (locator != null) {
            scrollToElement(locator);
            clickJSE(locator);
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void scrollAndClickByJSE(WebElement element) throws Exception {
        if (element != null) {
            scrollToElement(element);
            clickJSE(element);
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }


    //////////////////////////////// TEXT HANDLING ////////////////////////////////
    public void setText(By locator, String text) throws Exception {
        if (locator != null) {
            scrollAndClickByJSE(locator);
            clear(locator);
            findElement(locator).sendKeys(text);
            loseFocusFromField();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void setText(By locator, Object text) throws Exception {
        if (locator != null) {
            scrollAndClickByJSE(locator);
            clear(locator);
            if (text != null) {
                findElement(locator).sendKeys(text.toString());
            } else {
                throw new Exception("Provided text is null");
            }
            loseFocusFromField();
        } else {
            throw new Exception("Web element 'locator' is null .. it could not be located");
        }
    }


    public String getText(By locator) throws Exception {
        if (locator != null) {
            waits.waitForVisibility(locator);
            return findElement(locator).getText();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public String getText(WebElement element) throws Exception {
        if (element != null) {
            waits.waitForVisibility(element);
            return element.getText();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public String getTextBoxCurrentValue(By locator) throws Exception {
        if (locator != null) {
            waits.waitForVisibility(locator);
            return findElement(locator).getAttribute("value");
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void clear(By locator) throws Exception {
        if (locator != null) {
            waits.waitForVisibility(locator);
            findElement(locator).clear();
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");

    }

    public void clearUsingBackspace(By locator, int counter) throws Exception {
        if (locator != null) {
            waits.waitForVisibility(locator);
            for (int i = 0; i < counter; i++)
                findElement(locator).sendKeys(Keys.BACK_SPACE);
        } else
            throw new Exception("Web element 'locator' is null .. it could not be located");
    }

    public void loseFocusFromField() {
        jse = (JavascriptExecutor) driver;
        jse.executeScript("if (document.activeElement) document.activeElement.blur();");
    }

    public String getLastNChars(String input, int n) {
        if (input == null || n <= 0) {
            return "";
        }
        if (input.length() <= n) {
            return input;
        }
        return input.substring(input.length() - n);
    }


    //////////////////////////////// URLs HANDLING ////////////////////////////////
    public static String injectEnvironmentToURL(String url, String environment) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            int startIndex = url.indexOf("//") + 2;
            int endIndex = url.indexOf("/", startIndex);
            if (endIndex != -1) {
                return url.substring(0, startIndex) + environment + url.substring(endIndex);
            } else {
                return url.substring(0, startIndex) + environment;
            }
        }
        return url;
    }

    public String extractEnvironmentFromCurrentURL() {
        String url = driver.getCurrentUrl();
        if (url.startsWith("https://") || url.startsWith("http://")) {
            int startIndex = url.indexOf("//") + 2;
            int endIndex = url.indexOf("/", startIndex);
            if (endIndex != -1) {
                return url.substring(startIndex, endIndex);
            } else {
                return url.substring(startIndex);
            }
        }
        return null;
    }

    public void navigateTo(String URL, String environment) {
        driver.get(injectEnvironmentToURL(URL, environment));
    }

    public void navigateToURLAndWaitForElement(String URL, String environment, By elementShouldBeLoaded) {
        navigateTo(URL, environment);
        waits.waitForVisibility(elementShouldBeLoaded);
    }

    //check page is opened by get its url
    public boolean isURLHas(String url) {
        return driver.getCurrentUrl().contains(url);
    }

    public void waitForURLToChange(String expectedURL) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        wait.until(ExpectedConditions.urlToBe(expectedURL));
    }

    public void navigateURLWithWait(String AppURl, By AppIMG) {
        waits.waitForVisibility(AppIMG);
        driver.get(AppURl);
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }


    //////////////////////////////// HOVER HANDLING ////////////////////////////////
    public void hover(WebElement element) {
        waits.waitForVisibility(element);
        actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public void hover(By locator) {
        waits.waitForVisibility(locator);
        actions = new Actions(driver);
        actions.moveToElement(findElement(locator)).perform();
    }


    /////////////////////////////////////////////////////////////// DATE HANDLING /////////////////////////////////////////////////////////////////


    protected void selectSingleDate(By dateField, By monthLocator, By yearLocator, By previousBtnLocator, String targetDate) throws Exception {
        scrollAndClickByJSE(dateField);
        String[] dateParts = targetDate.split(" "); // Example: ["15", "August", "2024"]
        int targetDay = Integer.parseInt(dateParts[0]);
        String targetMonth = dateParts[1];
        String targetYear = dateParts[2];
        selectDate(monthLocator, yearLocator, previousBtnLocator, targetMonth, targetYear);
        selectDay(targetDay);
    }
    public void selectMonthAndYear(By monthLocator, By yearLocator, By navigationBtnLocator, String targetMonth, String targetYear) throws Exception {
        scrollToElement(monthLocator);
        String currentMonth = getText(monthLocator);
        String currentYear = getText(yearLocator);

        while (!(currentMonth.equals(targetMonth) && currentYear.equals(targetYear))) {
            click(navigationBtnLocator);
            currentMonth = getText(monthLocator);
            currentYear = getText(yearLocator);
        }
    }



    public void selectDateRange(By dateField, By monthTextLocator, By yearTextLocator, By nextBtnLocator, By previousBtnLocator, String dateFrom, String dateTo, By closeBtnLocator) throws Exception {
        scrollAndClickByJSE(dateField);

        String[] fromDateParts = dateFrom.split(" ");
        String[] toDateParts = dateTo.split(" ");

        int fromDay = Integer.parseInt(fromDateParts[0].trim());
        String fromMonth = fromDateParts[1].trim();
        String fromYear = fromDateParts[2].trim();

        int toDay = Integer.parseInt(toDateParts[0].trim());
        String toMonth = toDateParts[1].trim();
        String toYear = toDateParts[2].trim();

        // Select Date From
        selectDate(monthTextLocator, yearTextLocator, previousBtnLocator, fromMonth, fromYear);
        selectDay(fromDay);

        // Select Date To
        selectDate(monthTextLocator, yearTextLocator, nextBtnLocator, toMonth, toYear);
        selectDay(toDay);

        click(closeBtnLocator);
    }

    private void selectDate(By monthLocator, By yearLocator, By navigationBtnLocator, String targetMonth, String targetYear) throws Exception {
        String currentMonth = getText(monthLocator);
        String currentYear = getText(yearLocator);

        while (!(currentMonth.equals(targetMonth) && currentYear.equals(targetYear))) {
            click(navigationBtnLocator);
            currentMonth = getText(monthLocator);
            currentYear = getText(yearLocator);
        }
    }


    private void selectDay(int day) throws Exception {
        click(By.xpath("//span[text()='" + day + "' and not(contains(@class,'p-disabled'))]"));
    }

    public void pickDateAfterDays(By dateField, By dateIcon, By activeDateNumbersList, By datePickerNextBtn, int daysToAdd) throws Exception {
        if (daysToAdd < 0) {
            Log.error("Days to add must be positive");
        }
        scrollAndClickByJSE(dateField);
        int dateListSize = getListSize(activeDateNumbersList);
        if (dateListSize == 0) {
            clickJSE(datePickerNextBtn);
            dateListSize = getListSize(activeDateNumbersList);
        }
        if (daysToAdd >= dateListSize) {
            int monthsToAdvance = (daysToAdd - 1) / 30;
            int finalDayIndex = (daysToAdd - 1) % 30;
            for (int i = 0; i < monthsToAdvance; i++) {
                clickJSE(datePickerNextBtn);
            }
            clickJSE(datePickerNextBtn);
            selectFromListByIndex(activeDateNumbersList, finalDayIndex + 1);
        } else {
            selectFromListByIndex(activeDateNumbersList, daysToAdd);
        }
        clickJSE(dateIcon);
    }

    public void pickDateDaysBefore(By dateField, By dateIcon, By activeDateNumbersList, By datePickerPreviousBtn, int daysToRemove) throws Exception {
        if (daysToRemove < 0) {
            Log.error("Days to add must be positive");
        }
        scrollAndClickByJSE(dateField);
        int dateListSize = getListSize(activeDateNumbersList);
        if (dateListSize == 0) {
            clickJSE(datePickerPreviousBtn);
            dateListSize = getListSize(activeDateNumbersList);
        }
        if (daysToRemove >= dateListSize) {
            int monthsToAdvance = (daysToRemove - 1) / 30;
            int finalDayIndex = (daysToRemove - 1) % 30;
            for (int i = 0; i < monthsToAdvance; i++) {
                clickJSE(datePickerPreviousBtn);
            }
            clickJSE(datePickerPreviousBtn);
            selectFromListByIndex(activeDateNumbersList, dateListSize - finalDayIndex - 1);
        } else {
            selectFromListByIndex(activeDateNumbersList, dateListSize - daysToRemove - 1);
        }
        clickJSE(dateIcon);
    }

    public String getCurrentDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public boolean checkDateWithinRange(String dateFrom, String dateTo, String fromToDatePattern, List<String> targetDates, String targetDatePattern) throws ParseException {
        try {
            // Define date formats for input strings
            SimpleDateFormat dateFormatFromTo = new SimpleDateFormat(fromToDatePattern);
            SimpleDateFormat dateFormatTarget = new SimpleDateFormat(targetDatePattern);

            // Parse the dateFrom and dateTo strings
            Date fromDate = dateFormatFromTo.parse(dateFrom);
            Date toDate = dateFormatFromTo.parse(dateTo);

            // Iterate over each target date and check if it's within the range
            for (String targetDate : targetDates) {
                String targetDateOnly = targetDate.split(" ")[0];
                Date target = dateFormatTarget.parse(targetDateOnly);

                // Check if target date is within the range
                if (!(target.equals(fromDate) || target.equals(toDate) || (target.after(fromDate) && target.before(toDate)))) {
                    return false; // If any date is outside the range, return false
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkDateWithinRange(String dateFrom, String dateTo, String fromToDatePattern, String targetDate, String targetDatePattern) throws ParseException {
        // Define date formats for input strings
        SimpleDateFormat dateFormatFromTo = new SimpleDateFormat(fromToDatePattern);
        SimpleDateFormat dateFormatTarget = new SimpleDateFormat(targetDatePattern);

        // Parse the dateFrom and dateTo strings
        Date fromDate = dateFormatFromTo.parse(dateFrom);
        Date toDate = dateFormatFromTo.parse(dateTo);

        // Extract date part from targetDate and parse it
        String targetDateOnly = targetDate.split(" ")[0];
        Date target = dateFormatTarget.parse(targetDateOnly);

        // Check if target date is within the range
        return target.equals(fromDate) || target.equals(toDate) || (target.after(fromDate) && target.before(toDate));
    }


    //////////////////////////////// FILES AND DIRECTORIES HANDLING ////////////////////////////////
    public boolean isDirEmpty(String directoryPath) throws IOException {
        final Path directory = (Path) Paths.get(directoryPath);
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

    public void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (Exception e) {
            Log.warning("File deletion failed: " + e.getMessage());
        }
    }

    public void clearDirectory(String dirPath) {
        try (Stream<Path> files = Files.list(Paths.get(dirPath))) {
            files.forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    Log.error("Failed to delete file: " + file + " - " + e.getMessage());
                }
            });
        } catch (IOException e) {
            Log.warning("Failed to list files in directory: " + dirPath + " - " + e.getMessage());
        }
    }

    //wait for a specific file to be downloaded
    public void     waitForFileToBeDownloaded(String path) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until((WebDriver wd) -> {
                File file = new File(path);
                return file.exists();
            });
        } catch (TimeoutException e) {
            throw new TimeoutException("\nWait for file to be downloaded at path [" + path + "]\n(tried for " + waitTime + " second(s))", e);
        }
    }

    public void waitForDirectoryToBeNotEmpty(String dirPath) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until((WebDriver wd) -> {
                try (Stream<Path> files = Files.list(Paths.get(dirPath))) {
                    return files.findFirst().isPresent();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to list files in directory: " + dirPath, e);
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException("\nWait for directory to be not empty at path [" + dirPath + "]\n(tried for " + waitTime + " second(s))", e);
        }
    }



    public void uploadFiles(By uploadButton, String filePath) {
        findElement(uploadButton).sendKeys(filePath);
    }



    public void copyAndRenameFile(String sourceFilePath, String destinationFilePath) {
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetPath = Paths.get(destinationFilePath);

        try {
            Files.copy(sourcePath, targetPath);
        } catch (Exception e) {
            Log.warning("File copy and rename failed: " + e.getMessage());
        }
    }

    public String insertIdInFileName(String oldFileName, String id) {
        String fileName = new File(oldFileName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex != -1
                ? fileName.substring(0, dotIndex) + "-" + id + fileName.substring(dotIndex)
                : fileName + id;
    }


    public Boolean isFileAvailable(String expectedFileName, String path) {
        File file = new File(path);
        File[] listOfFiles = file.listFiles();
        Boolean isFileAvailable = false;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String fileName = listOfFile.getName();
                if (fileName.matches(expectedFileName)) {
                    isFileAvailable = true;
                }
            }
        }
        return isFileAvailable;
    }

    public void waitForFileToBeExist(String filePath) {
        File file = new File(filePath);
        int attempts = 0;
        while (attempts < 6) {
            if (file.exists()) {
                return;
            }
            try {
                Thread.sleep(5000); // Wait for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attempts++;
        }
        throw new RuntimeException("File not found within the expected time frame: " + filePath);
    }

    public boolean isFileExist(String filePath) {
        waitForFileToBeExist(filePath);
        return new File(filePath).exists();
    }

    public void createFile(String dirPath, String fileName) {
        File directory = new File(dirPath);

        // Ensure the directory exists, or create it if it doesn't
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file in the specified directory
        File file = new File(directory, fileName);
        try {
            if (file.createNewFile()) {
                Log.info("File created: " + file.getAbsolutePath());
            } else {
                Log.info("File already exists: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.error("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    public void createGitKeepFile(String downloadDirectoryPath) {
        createFile(downloadDirectoryPath, ".gitkeep");
    }



    //////////////////////////////// TABS HANDLING ////////////////////////////////
    //switch to the new tab
    public void switchToNewTab() throws InterruptedException {
        //list of all tabs id
        List<String> windowTabs = new ArrayList<String>(driver.getWindowHandles());
        //switch to next new tab
        driver.switchTo().window(windowTabs.get(1));
    }

    public void switchToMainTab() throws InterruptedException {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        Thread.sleep(500);
    }

    public void openNewTab() {
        // Open a new tab
        ((JavascriptExecutor) driver).executeScript("window.open();");
        String originalHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    // Method to close the current tab and switch back to the main one
    public void closeCurrentTabAndSwitchToMain() throws InterruptedException {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) {
            driver.close();
            driver.switchTo().window(tabs.get(0));
        } else {
            Log.error("No additional tabs open to close.");
        }
        Thread.sleep(500);
    }

    public void clearCookiesAndCache() {
        try {
            driver.manage().deleteAllCookies();
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
            driver.navigate().refresh();
            waitForPageToLoad();
        } catch (Exception e) {
            Log.error("Failed to clear cache and refresh: " + e.getMessage());
        }
    }

    public void refresh() {
        driver.navigate().refresh();
        waitForPageToLoad();
    }

    public void waitForPageToLoad() {
        new WebDriverWait(driver, waitTime).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    public String appendRandomName(String inputName) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return inputName + " " + stringBuilder.toString();
    }

    public String generateID() {
        Random random = new Random();
        return String.valueOf(random.nextInt(90000) + 10000);
    }

    public String generateLongID() {
        Random random = new Random();
        long upperBound = 99999999999999L;
        long lowerBound = 10000000000000L;
        long number = lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
        return String.valueOf(number);
    }

    public String generateNationalID() {
        Random random = new Random();
        StringBuilder nationalID = new StringBuilder();
        nationalID.append(random.nextInt(9) + 1);//first digit is not zero
        for (int i = 1; i < 14; i++) {
            nationalID.append(random.nextInt(10));
        }

        return nationalID.toString();
    }

    public String generateRandomName() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public String generateRandomString() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public String generateRandomChars() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    // Method to generate a random mobile number
    public String generateRandomMobileNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String[] prefixes = {"010", "011", "012", "015"};
        String randomPrefix = prefixes[random.nextInt(prefixes.length)];
        sb.append(randomPrefix);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public String generateRandomInValidMobileNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String[] prefixes = {"013", "014", "016", "017", "018", "019"};
        String randomPrefix = prefixes[random.nextInt(prefixes.length)];
        sb.append(randomPrefix);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public void pressEnter() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER); //press enter key
        robot.keyRelease(KeyEvent.VK_ENTER); //release enter key
    }


    public boolean allValuesContainTerm(List<String> options, String searchTerm) {
        for (String option : options) {
            if (!option.contains(searchTerm)) {
                Log.error("The term [" + searchTerm + "] was not found in the element: " + option);
                return false;
            }
        }
        return true;
    }


    public boolean isClickable(By locator) {
        waits.waitForElementToBePresent(locator);
        waits.waitForVisibility(locator);
        return findElement(locator).isEnabled();
    }
    public boolean invisiblity(By locator) {
        try {
            waits.waitForInvisibility(locator);
            return true;
        }
        catch
        (Exception e)
        { return false;
        }
    }


    public String getAttributeValue(By locator, String attribute) {
        return findElement(locator).getAttribute(attribute);
    }

    public void scrollThenHoverAndClick(By locator) throws Exception {
        scrollToElement(locator);
        hover(locator);
        click(locator);
    }

    public boolean isSelected(By locator) {
        waits.waitForElementToBeClickable(locator);
        return findElement(locator).isSelected();
    }

    public String textSubstring(String text, int indexStart, int indexEnd) {
        return text.substring(indexStart, indexEnd);
    }

    public void login(String mail, String password, By mailLocator, By passwordLocator, By loginBtn) {
        try {
            setText(mailLocator, mail);
            setText(passwordLocator, password);
            click(loginBtn);
        } catch (Exception e) {
            Log.error("BLOCKING ISSUE - CAN NOT LOGIN TO APPLICATION WITH MAIL: " + mail + "PASSWORD: " + password, e);
        }
    }

    public int extractInteger(String input) {
        String cleanedString = input.replaceAll("[^0-9.]", "");
        String[] parts = cleanedString.split("\\.");
        if (parts.length > 0) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }


}



