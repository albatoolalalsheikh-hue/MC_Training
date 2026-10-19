
package Base.Task1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class SuccessfulLoginTest {

    private WebDriver driver100;
    private WebDriverWait wait100;

    private static final String URL =
            "https://www.saucedemo.com/";

    @BeforeMethod
    public void setUp() {

        System.setProperty(
                "webdriver.edge.driver",
                "C:\\Users\\ahalsheikh\\IdeaProjects\\MyProject\\src\\main\\resources\\msedgedriver.exe"
        );

        driver100 = new EdgeDriver();

        driver100.manage().window().maximize();

        wait100 = new WebDriverWait(
                driver100,
                Duration.ofSeconds(15)
        );

        driver100.get(URL);

        // Wait until the login page is displayed
        wait100.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("user-name")
                )
        );
    }

    @Test
    public void successfulLogin() throws InterruptedException {

        // 1. Enter username
        WebElement username = wait100.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("user-name")
                )
        );

        username.sendKeys("standard_user");
        Thread.sleep(1500);

        // 2. Enter password
        WebElement password = wait100.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("password")
                )
        );

        password.sendKeys("secret_sauce");
        Thread.sleep(1500);

        // 3. Click the Login button
        WebElement loginButton = wait100.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("login-button")
                )
        );

        loginButton.click();
        Thread.sleep(1500);

        // 4. Verify successful navigation
        wait100.until(
                ExpectedConditions.urlContains("inventory.html")
        );

        assertTrue(
                driver100.getCurrentUrl().contains("inventory.html")
        );

        // 5. Verify the Products page title
        WebElement pageTitle = wait100.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("title")
                )
        );

        assertEquals("Products", pageTitle.getText());

        // Keep the Products page visible for 3 seconds
        Thread.sleep(3000);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver100 != null) {
            driver100.quit();
            driver100 = null;
        }
    }
}