
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
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class ShoppingworkflowSen3 {

    private WebDriver driver200;
    private WebDriverWait wait200;

    private static final String URL =
            "https://www.saucedemo.com/";

    @BeforeMethod
    public void setUp() throws InterruptedException {

        System.setProperty(
                "webdriver.edge.driver",
                "C:\\Users\\ahalsheikh\\IdeaProjects\\MyProject\\src\\main\\resources\\msedgedriver.exe"
        );

        driver200 = new EdgeDriver();

        driver200.manage().window().maximize();

        wait200 = new WebDriverWait(
                driver200,
                Duration.ofSeconds(15)
        );

        driver200.get(URL);

        // Wait for the login page
        wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("user-name")
                )
        );

        // Login with valid credentials
        driver200.findElement(By.id("user-name"))
                .sendKeys("standard_user");
        Thread.sleep(3000);
        driver200.findElement(By.id("password"))
                .sendKeys("secret_sauce");
        Thread.sleep(3000);
        driver200.findElement(By.id("login-button"))
                .click();

        // Wait for the Products page
        wait200.until(
                ExpectedConditions.urlContains("inventory.html")
        );

        wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("inventory_item")
                )
        );
    }

    // Add a product to the shopping cart
    private void addProduct(String productName) throws InterruptedException {

        WebElement addButton = wait200.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath(
                                "//div[contains(@class,'inventory_item')]"
                                        + "//div[contains(@class,'inventory_item_name')"
                                        + " and normalize-space()='" + productName + "']"
                                        + "/ancestor::div[contains(@class,'inventory_item')]"
                                        + "//button"
                        )
                )
        );

        addButton.click();
        Thread.sleep(3000);
    }

    // Get the shopping cart badge text
    private String cartBadgeText() {

        return wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("shopping_cart_badge")
                )
        ).getText();
    }

    @Test
    public void completeShoppingWorkflow() throws InterruptedException {

        // ===== Part A: Add products =====

        addProduct("Sauce Labs Backpack");
        addProduct("Sauce Labs Bike Light");

        // Expected: cart badge shows 2
        assertEquals("2", cartBadgeText());

        // ===== Part B: Shopping cart validation =====

        driver200.findElement(
                By.className("shopping_cart_link")
        ).click();

        wait200.until(
                ExpectedConditions.urlContains("cart.html")
        );

        List<WebElement> items = driver200.findElements(
                By.className("inventory_item_name")
        );

        // Both products should be displayed
        assertEquals(2, items.size());

        assertEquals(
                "Sauce Labs Backpack",
                items.get(0).getText()
        );

        assertEquals(
                "Sauce Labs Bike Light",
                items.get(1).getText()
        );
        Thread.sleep(3000);
        // Remove Sauce Labs Bike Light
        driver200.findElement(
                By.id("remove-sauce-labs-bike-light")
        ).click();

        // Expected: only the Backpack remains
        items = driver200.findElements(
                By.className("inventory_item_name")
        );

        assertEquals(1, items.size());

        assertEquals(
                "Sauce Labs Backpack",
                items.get(0).getText()
        );

        // Expected: cart badge shows 1
        assertEquals("1", cartBadgeText());

        // ===== Part C: Checkout =====

        driver200.findElement(By.id("checkout")).click();

        wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first-name")
                )
        );

        driver200.findElement(By.id("first-name"))
                .sendKeys("Test");

        driver200.findElement(By.id("last-name"))
                .sendKeys("Automation");

        driver200.findElement(By.id("postal-code"))
                .sendKeys("12345");

        driver200.findElement(By.id("continue")).click();

        // Wait for the order summary
        wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("inventory_item_name")
                )
        );

        // Verify the remaining product
        String summaryItem = driver200.findElement(
                By.className("inventory_item_name")
        ).getText();

        assertEquals(
                "Sauce Labs Backpack",
                summaryItem
        );

        // Finish the order
        driver200.findElement(By.id("finish")).click();

        // Verify successful order completion
        WebElement successMessage = wait200.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("complete-header")
                )
        );

        assertEquals(
                "Thank you for your order!",
                successMessage.getText()
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver200 != null) {
            driver200.quit();
            driver200 = null;
        }
    }
}
