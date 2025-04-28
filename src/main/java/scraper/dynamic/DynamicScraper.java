package scraper.dynamic;

import data.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class DynamicScraper {
    public static List<Product> scrapeProducts(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        driver.get(url);

        List<Product> products = new ArrayList<>();

        List<WebElement> productElements = driver.findElements(By.cssSelector("li.product"));

        for (WebElement productElement : productElements) {
            Product product = new Product();

            product.setUrl(productElement.findElement(By.tagName("a")).getAttribute("href"));
            product.setImage(productElement.findElement(By.tagName("img")).getAttribute("src"));
            product.setName(productElement.findElement(By.tagName("h2")).getText());
            product.setPrice(productElement.findElement(By.tagName("span")).getText());

            products.add(product);
        }

        driver.quit();
        return products;
    }
}
