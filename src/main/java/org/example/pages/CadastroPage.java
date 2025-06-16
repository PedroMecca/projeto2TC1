package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CadastroPage {
    private WebDriver driver;

    public CadastroPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");
    }

    public void preencherNome(String nome) {
        driver.findElement(By.id("name")).sendKeys(nome);
    }

    public void preencherEmail(String email) {
        driver.findElement(By.id("email")).sendKeys(email);
    }

    public void preencherTelefone(String telefone) {
        driver.findElement(By.id("phone")).sendKeys(telefone);
    }

    public void clicarCadastrar() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public String obterMensagemSucesso() {
        return driver.findElement(By.id("success-message")).getText();
    }

    public void preencherFormulario(String nome, String email, String telefone) {
    }

    public void submeterFormulario() {

    }
}
