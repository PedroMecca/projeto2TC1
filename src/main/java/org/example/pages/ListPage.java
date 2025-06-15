package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ListPage {
    private WebDriver driver;

    public ListPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://projeto2-seven-sandy.vercel.app/contatos.html");
    }

}
