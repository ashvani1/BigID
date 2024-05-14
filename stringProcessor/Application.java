package stringProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Application {
    public static void main(String[] args) {
        // open the url, read the text from it, TODO: in real time app, below url shouldn't be hardcoded one
        List<String> textParts = readTextFromWebsiteInParts("https://norvig.com/big.txt");
        List<Future<Map<String, List<Location>>>> futures = new ArrayList<>();

        //decide number of threads based on system capacity
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (String part : textParts) {
            futures.add(executor.submit(new Matcher(part)));
        }

        //below will wait matcher to be completed
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait until all tasks are finished
        }

        // Aggregate results
        Map<String, List<Location>> aggregatedResults = new HashMap<>();
        for (Future<Map<String, List<Location>>> future : futures) {
            try {
                Map<String, List<Location>> result = future.get();
                // Merge results
                for (Map.Entry<String, List<Location>> entry : result.entrySet()) {
                    aggregatedResults.merge(entry.getKey(), entry.getValue(), (l1, l2) -> {
                        l1.addAll(l2);
                        return l1;
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Print results
        Aggregator.printResults(aggregatedResults);
    }

    private static List<String> readTextFromWebsiteInParts(String url) {
        List<String> parts = new ArrayList<>();
        try {
            URL website = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));

            String inputLine;
            StringBuilder partBuilder = new StringBuilder();
            int linesRead = 0;
            while ((inputLine = in.readLine()) != null) {
                partBuilder.append(inputLine).append("\n");
                linesRead++;
                if (linesRead >= 1000) {
                    parts.add(partBuilder.toString());
                    partBuilder = new StringBuilder();
                    linesRead = 0;
                }
            }
            in.close();

            if (partBuilder.length() > 0) {
                parts.add(partBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parts;
    }
}
