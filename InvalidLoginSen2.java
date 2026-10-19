package Base.Task1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class InvalidLoginSen2 {

    private WebDriver driver90;
    private WebDriverWait wait;
    private static final String URL = "https://www.saucedemo.com/";

    @BeforeMethod
    public void SetUP() {
        System.setProperty("webdriver.edge.driver",
                "C:\\Users\\ahalsheikh\\IdeaProjects\\MyProject\\src\\main\\resources\\msedgedriver.exe");
        driver90=new EdgeDriver();
        driver90.manage().window().maximize();
        wait = new WebDriverWait(driver90, Duration.ofSeconds(10));
        driver90.get(URL);
        System.out.println("Current URL: " + driver90.getCurrentUrl());
        System.out.println("Page title: " + driver90.getTitle());
        System.out.println("Page source: " + driver90.getPageSource().substring(0, 500));
        //open  URL


    }

    @AfterTest
    public void tearDown() {
        if (driver90 != null) {
            driver90.quit();
        }}
    @Test
    public void InvalidLogin() throws InterruptedException {

        By password = By.id("password");
        By username = By.id("user-name");
        By loginButton = By.id("login-button");

        // Wait for the username field
        WebElement userField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(username)
        );

        userField.sendKeys("wrong_user");
        Thread.sleep(1000);
        // Enter an incorrect password
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(password)
        );

        passwordField.sendKeys("Wrong_Password");
        Thread.sleep(1000);
        // Click Login
        driver90.findElement(loginButton).click();

        // Wait for the error message
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        );

        // Verify the expected error message
        assertTrue(error.getText().contains(
                "Username and password do not match any user in this service"
        ));

        // Verify that the browser remains on the login page
        assertEquals(URL, driver90.getCurrentUrl());
    }

}