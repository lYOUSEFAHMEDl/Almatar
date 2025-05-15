package com.Almatar.pages.Almatar;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends com.Almatar.pages.base.PageBase {
    public HomePage(WebDriver driver) {
        super(driver);
    }

//Hotel section
    By HotelsBtn = By.xpath("//a[@id=\"ngb-nav-0\"]");
    By ClearLocationBtn = By.xpath("//div[@class=\"clearLocation ng-star-inserted\"]");
    By GoingToField = By.xpath("//section[@class=\"auto-compelete-container home\"]");
    By GoingToFirstOption = By.xpath("//button[@id=\"ngb-typeahead-0-0\"]");
    By CheckInDateField = By.xpath("(//div[contains(@class, 'almatar-calendar__date')])[1]");
    By CheckOutDateField = By.xpath("(//div[contains(@class, 'almatar-calendar__date')])[2]");
    By SelectGuestsAndRoomDropDownBtn = By.xpath("(//almatar-icon[@class=\"almatar-flight-search-travelers__chevron ng-star-inserted\"])[1]");


    //Flight section
    By FlightsBtn = By.xpath("//a[@id=\"ngb-nav-1\"]");
    By OneWayFlightBtn = By.xpath("//span[contains(text(),\"One Way\")]");
    By MultiCityFlightBtn = By.xpath("//span[contains(text(),\"Multi City\")]");
    By OneWayDepartFormFiled = By.xpath("(//input[@id=\"typeahead-http\"])[1]");
    By OneWayGoingToField = By.xpath("(//input[@id=\"typeahead-http\"])[2]");
    By OneWayDepartFormFiledFirstOption = By.xpath("//div[@class=\"almatar-flights-results__item\"]");
    By AddReturnFlightBtn = By.xpath("//div[@class=\"almatar-flight-search__return ng-star-inserted\"]");
    By searchFlightsBtn = By.xpath("//button[@class=\"almatar-flight-search__action ng-star-inserted\"]");
    By FiltersAssertion = By.xpath("//h4[@class=\"almatar-filter__title\"]");
    By DepartFromFirstOption=By.xpath("//div[@class=\"almatar-flights-results__item\"]");
    By GoingDoFirstOption = By.xpath("(//div[@class=\"almatar-flights-results__head\"])[1]");
    By PassengerDDLBtn= By.xpath("(//almatar-icon[@class=\"almatar-flight-search-travelers__chevron ng-star-inserted\"])[1]");
    By FlightCategoryDDLBtn=By.xpath("(//almatar-icon[@class=\"almatar-flight-search-travelers__chevron ng-star-inserted\"])[2]");
    public void selectHotels() throws Exception {
    Allure.step("Select Hotels");
    clickJSE(HotelsBtn);
}

public void selectFlights() throws Exception {
    Allure.step("Click on Flights");
    clickJSE(FlightsBtn);
}

 public void selectOneWayFlight() throws Exception {
     Allure.step("Select One Way Flight");
     clickJSE(OneWayFlightBtn);
 }

 public void selectMultiCityFlight() throws Exception {
     Allure.step("Select Multi City Flight");
     clickJSE(MultiCityFlightBtn);
 }

    public void clickOnTheGoingToField() throws Exception {
        Allure.step("Click on Going To Field");
        clickJSE(GoingToField);
    }

    public void clickOnTheGoingToFirstOption() throws Exception {
        Allure.step("Click on the First Option");
        clickJSE(GoingToFirstOption);
    }

    public void clickOnTheCheckInDateField() throws Exception {
        Allure.step("Click on Check In Date Field");
        clickJSE(CheckInDateField);
    }

    public void clickOnTheCheckOutDateField() throws Exception {
        Allure.step("Click on Check Out Date Field");
        clickJSE(CheckOutDateField);
    }

    public void clickOnTheSelectGuestsAndRoomDropDownBtn() throws Exception {
        Allure.step("Click on Select Guests and Room Drop Down Button");
        clickJSE(SelectGuestsAndRoomDropDownBtn);
    }

    public void clickOnTheSelectFlightCategoryDropDownBtn() throws Exception {
        Allure.step("Click on Select Flight Category Drop Down Button");
        clickJSE(FlightCategoryDDLBtn);
    }
    public void oneWaySelectDepartFromField(String City) throws Exception {
        Allure.step("Click on Depart From Field");
        setText(OneWayDepartFormFiled, City);
        Thread.sleep(1500);
        clickJSE(OneWayDepartFormFiled);
        clickJSE(DepartFromFirstOption);

    }

   public void oneWaySelectGoingToField(String City) throws Exception {
        Allure.step("Click on Going To Field");
        setText(OneWayGoingToField, City);
       Thread.sleep(1500);
        clickJSE(OneWayGoingToField);
        clickJSE(GoingDoFirstOption);


    }

    public void clickOnTheOneWayDepartFormFiledFirstOption() throws Exception {
        Allure.step("Click on the First Option");
        clickJSE(OneWayDepartFormFiledFirstOption);
    }

   public void clickOnTheAddReturnFlightBtn() throws Exception {
        Allure.step("Click on Add Return Flight Button");
        clickJSE(AddReturnFlightBtn);
    }

   public void clickOnTheSearchFlightsBtn() throws Exception {
        Allure.step("Click on Search Flights Button");
        clickJSE(searchFlightsBtn);
    }
    public String getFiltersAssertion() throws Exception {
        try {
            return getText(FiltersAssertion).trim().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    public void waitForClassToContainNgValid( By inputLocator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(d -> {
            WebElement element = d.findElement(inputLocator);
            String classAttr = element.getAttribute("class");
            return classAttr.contains("ng-dirty") && !classAttr.contains("ng-pristine");
        });
    }

public void selectNumberOfAdults(int numberOfAdults) throws Exception {
    Allure.step("Select Number of Adults: " + numberOfAdults);
   By AdultPlusBtn = By.xpath("(//button[@class=\"almatar-passengers__button almatar-passengers__button--plus\"])[1]");
 for (int i = 1; i < numberOfAdults; i++) {
     clickJSE(AdultPlusBtn);
 }
    }

public void selectNumberOfChildren(int numberOfChildren) throws Exception {
    Allure.step("Select Number of Children: " + numberOfChildren);
    By ChildrenPlusBtn = By.xpath("(//button[@class=\"almatar-passengers__button almatar-passengers__button--plus\"])[2]");
    for (int i = 0; i < numberOfChildren; i++) {
        clickJSE(ChildrenPlusBtn);
    }
}

    public void selectClass(String travelClass) throws Exception {
        try {
            Allure.step("Selecting travel class: " + travelClass);

            String xpath = String.format(
                    "//div[contains(@class, 'almatar-cabin__item') and " +
                            "normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))='%s']",
                    travelClass.trim().toLowerCase()
            );

            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            element.click();

            // Verify selection was successful
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.attributeContains(element, "class", "almatar-cabin__item--active"));

            Allure.step("Successfully selected: " + travelClass);
        } catch (TimeoutException e) {
            String errorMsg = "Timeout while selecting travel class '" + travelClass + "'";
            Allure.step(errorMsg);
            throw new Exception(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Failed to select travel class '" + travelClass + "'";
            Allure.step(errorMsg);
            throw new Exception(errorMsg, e);
        }
    }

    public String getSelectedClass(String Class) throws Exception {
        Allure.step("Get Selected Class");
       String xpath = String.format(
               "//div[contains(@class, 'almatar-cabin__item') and " +
                       "normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))='%s' and contains(@class, 'almatar-cabin__item--active')]",
               Class.trim().toLowerCase());
       By selectedClass = By.xpath(xpath);
        return getText(selectedClass).trim();
    }

public String getSelectedPassengers() throws Exception {
    Allure.step("Get Selected Passengers");
    By passengersSummary = By.xpath("(//span[@class='almatar-flight-search-travelers__value pr-2'])[position()]");
    return getText(passengersSummary).trim();
}





}
