package de.linus.deepltranslator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;

public class DriverWaitUtils {

    /**
     * Wait until the attribute's value is blank.
     */
    static ExpectedCondition<Boolean> attributeBlank(By locator, String attribute) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(locator).getAttribute(attribute).trim().isEmpty();
            }

            @Override
            public String toString() {
                return String.format("value found by %s to be not blank.", locator);
            }
        };
    }

    /**
     * Wait until the attribute's value is not blank.
     */
    static ExpectedCondition<Boolean> attributeNotBlank(By locator, String attribute) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !driver.findElement(locator).getAttribute(attribute).trim().isEmpty();
            }

            @Override
            public String toString() {
                return String.format("value found by %s to be not blank.", locator);
            }
        };
    }

    /**
     * Wait until the attribute's value does not contain a specific subtext.
     */
    static ExpectedCondition<Boolean> attributeNotContains(By locator, String attribute, String value) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !driver.findElement(locator).getAttribute(attribute).contains(value);
            }

            @Override
            public String toString() {
                return String.format("value found by %s to not contain \"%s\".", locator, value);
            }
        };
    }

    /**
     * Wait until the attribute's value has not changed for a specific minimum duration.
     */
    static ExpectedCondition<Boolean> attributeNotChanged(By locator, String attribute, Duration minDurationNotChanged) {
        return new ExpectedCondition<Boolean>() {
            private String lastValue;
            private long lastChanged;

            @Override
            public Boolean apply(WebDriver driver) {
                String currentValue = driver.findElement(locator).getAttribute(attribute);
                long currentTime = System.currentTimeMillis();

                if (lastValue == null) {
                    lastValue = currentValue;
                    lastChanged = currentTime;
                    return false;
                }

                if (currentValue.equals(lastValue)) {
                    return currentTime >= lastChanged + minDurationNotChanged.toMillis();
                } else {
                    lastValue = currentValue;
                    lastChanged = currentTime;
                }

                return false;
            }

            @Override
            public String toString() {
                return String.format("value found by %s to not having changed during the last %d milliseconds.", locator, minDurationNotChanged.toMillis());
            }
        };
    }

}
