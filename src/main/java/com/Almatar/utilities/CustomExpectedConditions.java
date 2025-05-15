package com.Almatar.utilities;

import com.Almatar.pages.base.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;

public class CustomExpectedConditions{

    WebDriver driver;
    JavascriptExecutor jse;

    public CustomExpectedConditions(WebDriver driver) {
        this.driver = driver;
    }
    public ExpectedCondition<Boolean> urlNotContains(final String expectedWord) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !driver.getCurrentUrl().contains(expectedWord);
            }

            @Override
            public String toString() {
                return String.format("URL not to contain '%s'", expectedWord);
            }
        };
    }
    public ExpectedCondition<Boolean> elementIsWithinViewport(final By locator) {
        jse = (JavascriptExecutor) driver;
        return driver -> {
            jse = (JavascriptExecutor) driver;
            assert jse != null;
            return (boolean) (Boolean) jse.executeScript(
                    "var elem = arguments[0],                 " +
                            "  box = elem.getBoundingClientRect(),    " +
                            "  cx = box.left + box.width / 2,         " +
                            "  cy = box.top + box.height / 2,         " +
                            "  e = document.elementFromPoint(cx, cy); " +
                            "for (; e; e = e.parentElement) {         " +
                            "  if (e === elem)                        " +
                            "    return true;                         " +
                            "}                                        " +
                            "return false;                            ", driver.findElement(locator));
        };
    }
    public ExpectedCondition<Boolean> elementIsWithinViewport(final WebElement element) {
        jse = (JavascriptExecutor) driver;
        return driver -> {
            jse = (JavascriptExecutor) driver;
            assert jse != null;
            return (boolean) (Boolean) jse.executeScript(
                    "var elem = arguments[0],                 " +
                            "  box = elem.getBoundingClientRect(),    " +
                            "  cx = box.left + box.width / 2,         " +
                            "  cy = box.top + box.height / 2,         " +
                            "  e = document.elementFromPoint(cx, cy); " +
                            "for (; e; e = e.parentElement) {         " +
                            "  if (e === elem)                        " +
                            "    return true;                         " +
                            "}                                        " +
                            "return false;                            ", element);
        };
    }

    public ExpectedCondition<WebElement> elementToBeNotStale(final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(locator);
                    element.isEnabled();
                    return element;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "element to be not stale: " + locator;
            }
        };
    }
    public ExpectedCondition<WebElement> elementToBeNotStale(final WebElement element) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    element.isEnabled();
                    return element;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "element to be not stale: " + element;
            }
        };
    }
}
