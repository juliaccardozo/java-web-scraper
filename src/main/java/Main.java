import scraper.dynamic.DynamicScraper;
import scraper.statics.StaticParallelScraper;
import scraper.statics.StaticScraper;
import data.Product;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {
    private static final String URL = "https://www.scrapingcourse.com/ecommerce/";

    public static void main(String[] args) {
        Instant startTime = Instant.now();
        List<Product> staticProducts = StaticScraper.scrapeAllProducts(URL);
        Instant endTime = Instant.now();

        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Scrapper results: ");
        System.out.println(staticProducts);
        System.out.println("Sequencial Scraper took: " + duration.getSeconds() + " seconds");

        startTime = Instant.now();
        List<Product> parallelStaticProducts = StaticParallelScraper.scrapeAllProductsConcurrently(URL);
        endTime = Instant.now();

        duration = Duration.between(startTime, endTime);
        System.out.println("Parallel Scrapper results: ");
        System.out.println(parallelStaticProducts);
        System.out.println("Parallel StaticScraper took: " + duration.getSeconds() + " seconds");

        startTime = Instant.now();
        List<Product> dynamicProducts = DynamicScraper.scrapeProducts(URL);
        endTime = Instant.now();

        duration = Duration.between(startTime, endTime);
        System.out.println("Dynamic Scrapper results: ");
        System.out.println(dynamicProducts);
        System.out.println("Dynamic Scraper took: " + duration.getSeconds() + " seconds");
    }
}
