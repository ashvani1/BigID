package stringProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Matcher implements Callable<Map<String, List<Location>>> {
    private final String text;

    public Matcher(String text) {
        this.text = text;
    }

    @Override
    public Map<String, List<Location>> call() {
        Map<String, List<Location>> results = new HashMap<>();
        // Populate results based on matching logic
        String[] names = {
                "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas",
                "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
                "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
                "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
                "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
        };
        for (String name : names) {
            int index = -1;
            while ((index = text.indexOf(name, index + 1)) != -1) {
                Location location = getLocation(text, index);
                results.computeIfAbsent(name, k -> new ArrayList<>()).add(location);
            }
        }
        return results;
    }

    private Location getLocation(String text, int index) {
        int lineOffset = 0;
        int charOffset = 0;
        for (int i = 0; i < index; i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                lineOffset++;
                charOffset = 0;
            } else {
                charOffset++;
            }
        }
        return new Location(lineOffset, charOffset);
    }
}
