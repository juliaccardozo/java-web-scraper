package scraper.statics;

import data.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StaticParallelScraper {
    private static final int THREAD_POOL_SIZE = 5;

    public static List<Product> scrapeAllProductsConcurrently(String startUrl) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Product> products = new CopyOnWriteArrayList<>();
        List<Future<String>> futures = new ArrayList<>();
        ConcurrentSkipListSet<String> visitedPages = new ConcurrentSkipListSet<>();

        visitedPages.add(startUrl);
        futures.add(executor.submit(() -> StaticScraper.scrapePageAndGetNextUrl(startUrl, products, visitedPages)));

        while (!futures.isEmpty()) {
            List<Future<String>> newFutures = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    String nextUrl = future.get();
                    if (nextUrl != null && visitedPages.add(nextUrl)) {
                        newFutures.add(executor.submit(() -> StaticScraper.scrapePageAndGetNextUrl(nextUrl, products, visitedPages)));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("error executing task: " + e.getMessage());
                }
            }
            futures = newFutures;
        }

        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("executor shutdown interrupted: " + e.getMessage());
        }

        return products;
    }
}
