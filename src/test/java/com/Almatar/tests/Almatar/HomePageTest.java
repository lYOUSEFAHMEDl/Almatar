package com.Almatar.tests.Almatar;
import com.Almatar.constants.Almatar.AlMatarTestDataConstants;
import com.Almatar.pages.Almatar.HomePage;
import com.Almatar.tests.base.TestBase;
import com.Almatar.constants.Almatar.AlmatarFileNameConstants;
import com.Almatar.constants.Almatar.AlMatarTestDataConstants;
import com.Almatar.utilities.PropertiesReader;
import com.Almatar.utilities.SkipException;
import com.Almatar.utilities.Waits;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.util.Properties;

public class HomePageTest extends TestBase {

    Waits waits=new Waits(driver);
    public SoftAssert softAssert;
    String id;
    String Actual;
    double ActualDouble;
    private HomePage homepage;
    private final PropertiesReader propertiesReader = new PropertiesReader();
    private final Properties testData = propertiesReader.loadPropertiesFromFile(AlmatarFileNameConstants.TEST_DATA_CONFIGURATIONS_FILE_PATH);



    @BeforeMethod(alwaysRun = true)
    public void methodSetup() throws Exception {
        softAssert = new SoftAssert();
        homepage = new HomePage(driver);
    }


    @Test(description = "ID #1 Verify that the user can search for a one-way flight with valid inputs")
    public void verifyUserCanSearchForOneWayFlightWithValidInputs() throws Exception {
        try {
            homepage.selectFlights();
            homepage.selectOneWayFlight();
            homepage.oneWaySelectDepartFromField("Cairo, Egypt");
            homepage.oneWaySelectGoingToField("Dubai, United Arab Emirates");
            homepage.clickOnTheSearchFlightsBtn();
            Actual = homepage.getFiltersAssertion();
            softAssert.assertEquals(Actual,testData.getProperty(AlMatarTestDataConstants.Filters_Assertion));
            softAssert.assertAll();
        } catch (Exception e) {
            throw new SkipException(e);
        }
    }

    @Test(description = "ID #2 Verify that the system allows selecting number of passengers and class")
    public void verifySystemAllowsSelectingNumberOfPassengers() throws Exception {
        try {
            homepage.selectFlights();
            homepage.selectOneWayFlight();
            homepage.oneWaySelectDepartFromField("Cairo, Egypt");
            homepage.oneWaySelectGoingToField("Dubai, United Arab Emirates");
            homepage.clickOnTheSelectGuestsAndRoomDropDownBtn();
            homepage.selectNumberOfAdults(2);
            homepage.selectNumberOfChildren(1);
            Actual = homepage.getSelectedPassengersAndClass();
            softAssert.assertEquals(Actual,testData.getProperty(AlMatarTestDataConstants.Selected_Passengers_And_Class));
            softAssert.assertAll();
        } catch (Exception e) {
            throw new SkipException(e);
        }
    }

    @Test(description = "ID #2 Verify that the system allows selecting number of passengers and class")
    public void verifySystemAllowsSelectingDiffrentTypeOfClass() throws Exception {
        try {
            homepage.selectFlights();
            homepage.selectOneWayFlight();
            homepage.oneWaySelectDepartFromField("Cairo, Egypt");
            homepage.oneWaySelectGoingToField("Dubai, United Arab Emirates");
            homepage.clickOnTheSelectFlightCategoryDropDownBtn();
            homepage.selectClass("Business");
            Actual = homepage.getSelectedPassengersAndClass();
            softAssert.assertEquals(Actual,testData.getProperty(AlMatarTestDataConstants.Selected_Passengers_And_Class));
            softAssert.assertAll();
        } catch (Exception e) {
            throw new SkipException(e);
        }
    }

}



