Almatar Test Automation Suite
This repository contains automated UI test cases for the Almatar web application, built using Java, TestNG, and the Page Object Model (POM) design pattern.

📁 Project Structure
com/ └── Almatar/ ├── tests/ │ ├── Almatar/ │ │ └── HomePageTest.java │ └── base/ │ └── TestBase.java ├── pages/ │ └── Almatar/ │ └── HomePage.java ├── dataproviders/ │ └── AlmatarDataProvider.java ├── constants/ │ └── Almatar/ │ ├── AlMatarTestDataConstants.java │ └── AlmatarFileNameConstants.java └── utilities/ ├── PropertiesReader.java ├── SkipException.java └── Waits.java

✅ Test Scenarios
The test cases in HomePageTest.java validate the following:

Search for One-Way Flights:

Ensures the user can search for a one-way flight with valid input data.
Select Passengers and Class:

Validates that users can select the number of adults and children.
Select Different Flight Classes:

Verifies selection of Economy, Business, and other classes using data-driven testing.
🧪 Technologies Used
Java 11+
TestNG
Page Object Model
Maven (for build & dependency management)
Data-Driven Testing via TestNG's @DataProvider
Properties Files for configuration and test data
🚀 Getting Started
Prerequisites
Java JDK
Maven
ChromeDriver or relevant WebDriver
TestNG
Run Tests
mvn clean test
