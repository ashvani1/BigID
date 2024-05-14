package stringProcessor;

import java.util.List;
import java.util.Map;

public class Aggregator {
    public static void printResults(Map<String, List<Location>> results) {
        for (Map.Entry<String, List<Location>> entry : results.entrySet()) {
            System.out.print(entry.getKey() + " --> ");
            List<Location> locations = entry.getValue();
            System.out.println(locations);
        }
    }
}
