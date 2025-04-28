package scraper.statics;

import data.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class StaticScraper {
    public static List<Product> scrapeAllProducts(String url) {
        List<Product> products = new ArrayList<>();
        while (url != null) {

            try {
                Document doc = Jsoup.connect(url).get();

                Elements productElements = doc.select("li.product");

                for (Element productElement : productElements) {
                    Product product = new Product();

                    Element linkElement = productElement.selectFirst(".woocommerce-LoopProduct-link");
                    Element imgElement = productElement.selectFirst(".product-image");
                    Element nameElement = productElement.selectFirst(".product-name");
                    Element priceElement = productElement.selectFirst(".price");

                    product.setUrl(linkElement != null ? linkElement.attr("href") : "N/A");
                    product.setImage(imgElement != null ? imgElement.attr("src") : "N/A");
                    product.setName(nameElement != null ? nameElement.text() : "N/A");
                    product.setPrice(priceElement != null ? priceElement.text() : "N/A");

                    products.add(product);
                }

                Element nextButton = doc.selectFirst("a.next");

                if (nextButton != null) {
                    String nextPageUrl = nextButton.attr("href");
                    if (!nextPageUrl.startsWith("http")) {
                        nextPageUrl = url + nextPageUrl.replaceFirst("^/", "");
                    }
                    url = nextPageUrl;
                } else {
                    url = null;
                }

            } catch (IOException e) {
                System.err.println("Error fetching page: " + e.getMessage());
                break;
            }
        }

        return products;
    }

    public static String scrapePageAndGetNextUrl(String url, List<Product> products, ConcurrentSkipListSet<String> visitedPages) {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements productElements = doc.select("li.product");

            for (Element productElement : productElements) {
                Product product = new Product();

                Element linkElement = productElement.selectFirst(".woocommerce-LoopProduct-link");
                Element imgElement = productElement.selectFirst(".product-image");
                Element nameElement = productElement.selectFirst(".product-name");
                Element priceElement = productElement.selectFirst(".price");

                product.setUrl(linkElement != null ? linkElement.absUrl("href") : "N/A");
                product.setImage(imgElement != null ? imgElement.absUrl("src") : "N/A");
                product.setName(nameElement != null ? nameElement.text() : "N/A");
                product.setPrice(priceElement != null ? priceElement.text() : "N/A");

                products.add(product);
            }

            Element nextButton = doc.selectFirst("a.next");
            return (nextButton != null) ? nextButton.absUrl("href") : null;

        } catch (IOException e) {
            System.err.println("error fetching page: " + e.getMessage());
        }
        return null;
    }
}
