package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OpenPageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testNavigationToContactsPage() {
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        wait.until(ExpectedConditions.urlContains("contatos.html"));
        assertTrue(driver.getCurrentUrl().endsWith("contatos.html"));

        String pageTitle = driver.getTitle();
        assertEquals("Contatos Cadastrados", pageTitle);
    }

    @Test
    public void testAddValidContact() {
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");

        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement phoneInput = driver.findElement(By.id("phone"));
        WebElement submitButton = driver.findElement(By.cssSelector("form button[type=submit]"));

        nameInput.clear();
        nameInput.sendKeys("João Silva");
        emailInput.clear();
        emailInput.sendKeys("joao.silva@example.com");
        phoneInput.clear();
        phoneInput.sendKeys("11987654321");

        submitButton.click();

        WebElement successMessage = driver.findElement(By.id("success-message"));
        wait.until(ExpectedConditions.textToBePresentInElement(successMessage, "Contato salvo com sucesso!"));

        assertEquals("Contato salvo com sucesso!", successMessage.getText());

        assertEquals("", nameInput.getAttribute("value"));
        assertEquals("", emailInput.getAttribute("value"));
        assertEquals("", phoneInput.getAttribute("value"));
    }

    @Test
    public void testAddContactWithMissingFieldsShowsAlert() {
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");

        // Remove o atributo required para permitir envio vazio
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "['name','email','phone'].forEach(id => document.getElementById(id).removeAttribute('required'));"
        );

        WebElement submitButton = driver.findElement(By.cssSelector("form button[type=submit]"));
        submitButton.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Preencha todos os campos!", alert.getText());
        alert.accept();
    }

    @Test
    public void testEditContact() {
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");

        driver.findElement(By.id("name")).sendKeys("Maria");
        driver.findElement(By.id("email")).sendKeys("maria@example.com");
        driver.findElement(By.id("phone")).sendKeys("11912345678");
        driver.findElement(By.cssSelector("form button[type=submit]")).click();

        driver.findElement(By.linkText("Ver contatos cadastrados")).click();
        wait.until(ExpectedConditions.urlContains("contatos.html"));

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Editar']"));
        editButton.click();

        wait.until(ExpectedConditions.urlContains("index.html"));

        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement phoneInput = driver.findElement(By.id("phone"));

        assertEquals("Maria", nameInput.getAttribute("value"));
        assertEquals("maria@example.com", emailInput.getAttribute("value"));
        assertEquals("11912345678", phoneInput.getAttribute("value"));

        nameInput.clear();
        nameInput.sendKeys("Maria Editada");
        driver.findElement(By.cssSelector("form button[type=submit]")).click();

        WebElement successMessage = driver.findElement(By.id("success-message"));
        wait.until(ExpectedConditions.textToBePresentInElement(successMessage, "Contato salvo com sucesso!"));

        driver.findElement(By.linkText("Ver contatos cadastrados")).click();
        wait.until(ExpectedConditions.urlContains("contatos.html"));

        WebElement firstContactName = driver.findElement(By.cssSelector(".contact strong"));
        assertEquals("Maria Editada", firstContactName.getText());
    }

    @Test
    public void testRemoveContact() {
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");

        driver.findElement(By.id("name")).sendKeys("Remover Teste");
        driver.findElement(By.id("email")).sendKeys("remover@example.com");
        driver.findElement(By.id("phone")).sendKeys("11900000000");
        driver.findElement(By.cssSelector("form button[type=submit]")).click();

        driver.findElement(By.linkText("Ver contatos cadastrados")).click();
        wait.until(ExpectedConditions.urlContains("contatos.html"));

        int contactsCountBefore = driver.findElements(By.cssSelector(".contact")).size();

        WebElement removeButton = driver.findElement(By.xpath("//button[text()='Remover']"));
        removeButton.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Contato removido com sucesso!", alert.getText());
        alert.accept();

        int contactsCountAfter = driver.findElements(By.cssSelector(".contact")).size();
        assertEquals(contactsCountBefore - 1, contactsCountAfter);
    }
}
