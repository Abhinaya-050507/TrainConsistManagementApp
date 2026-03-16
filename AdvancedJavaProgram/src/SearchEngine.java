import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd;
    int frequency;
}

public class SearchEngine {
    private final TrieNode root = new TrieNode();

    public void addQuery(String query, int freq) {
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEnd = true;
        node.frequency += freq;
    }

    public void updateFrequency(String query) {
        addQuery(query, 1);
    }

    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return Collections.emptyList();
        }

        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(Map.Entry.comparingByValue());
        dfs(node, new StringBuilder(prefix), heap, 10);

        List<String> result = new ArrayList<>();
        while (!heap.isEmpty()) result.add(0, heap.poll().getKey()); // reverse order
        return result;
    }

    private void dfs(TrieNode node, StringBuilder path, PriorityQueue<Map.Entry<String, Integer>> heap, int k) {
        if (node.isEnd) {
            heap.offer(new AbstractMap.SimpleEntry<>(path.toString(), node.frequency));
            if (heap.size() > k) heap.poll();
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            path.append(entry.getKey());
            dfs(entry.getValue(), path, heap, k);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        SearchEngine ac = new SearchEngine();
        ac.addQuery("java tutorial", 1234567);
        ac.addQuery("javascript", 987654);
        ac.addQuery("java download", 456789);

        System.out.println(ac.search("jav")); // Top 10 suggestions
        ac.updateFrequency("java 21 features"); // trending query
    }
}