import java.util.*;

public class PlagiarismDetectionSystem {
    private final int N = 5;
    private final Map<String, Set<String>> ngramIndex = new HashMap<>();

    // Add a document to the database
    public void addDocument(String docId, String content) {
        List<String> ngrams = getNGrams(content);
        for (String ng : ngrams) {
            ngramIndex.computeIfAbsent(ng, k -> new HashSet<>()).add(docId);
        }
    }

    // Analyze a new document
    public void analyzeDocument(String docId, String content) {
        List<String> ngrams = getNGrams(content);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String ng : ngrams) {
            Set<String> docs = ngramIndex.get(ng);
            if (docs != null) {
                for (String existingDoc : docs) {
                    matchCount.put(existingDoc, matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / ngrams.size();
            String status = similarity > 50 ? "PLAGIARISM DETECTED" : "suspicious";
            System.out.printf("Found %d matching n-grams with \"%s\" → Similarity: %.1f%% (%s)%n",
                    entry.getValue(), entry.getKey(), similarity, status);
        }
    }

    // Helper: extract n-grams from text
    private List<String> getNGrams(String text) {
        String[] words = text.split("\\s+");
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) sb.append(words[i + j]).append(" ");
            ngrams.add(sb.toString().trim());
        }
        return ngrams;
    }

    // Example usage
    public static void main(String[] args) {
        PlagiarismDetectionSystem detector = new PlagiarismDetectionSystem();
        detector.addDocument("essay_089.txt", "this is a sample essay text for testing plagiarism detection system");
        detector.addDocument("essay_092.txt", "another example essay text to test plagiarism detection accurately");

        detector.analyzeDocument("essay_123.txt", "this is a sample essay text to test plagiarism detection system");
    }
}