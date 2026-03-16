import java.util.*;

class UsernameService {

    // username -> userId
    private HashMap<String, Integer> userDatabase;

    // username -> attempt frequency
    private HashMap<String, Integer> attemptFrequency;

    public UsernameService() {
        userDatabase = new HashMap<>();
        attemptFrequency = new HashMap<>();

        // Pre-existing users
        userDatabase.put("john_doe", 1001);
        userDatabase.put("admin", 1002);
        userDatabase.put("alex", 1003);
    }

    // Check username availability in O(1)
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        return !userDatabase.containsKey(username);
    }

    // Suggest similar usernames if taken
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;

            if (!userDatabase.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace underscore with dot suggestion
        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!userDatabase.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }
}

public class SocialMediaUsernameAvailabilityChecker{

    public static void main(String[] args) {

        UsernameService service = new UsernameService();

        System.out.println("checkAvailability(\"john_doe\") → " + service.checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") → " + service.checkAvailability("jane_smith"));

        System.out.println("\nsuggestAlternatives(\"john_doe\") → " + service.suggestAlternatives("john_doe"));

        // Simulate multiple attempts
        for (int i = 0; i < 10543; i++) {
            service.checkAvailability("admin");
        }

        System.out.println("\ngetMostAttempted() → " + service.getMostAttempted());
    }
}