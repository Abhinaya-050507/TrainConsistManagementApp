import java.util.*;
import java.util.concurrent.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class WebsiteTraffic {
    private final Map<String, Integer> pageViews = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private final Map<String, Integer> trafficSources = new ConcurrentHashMap<>();
    private final PriorityQueue<Map.Entry<String, Integer>> topPages = new PriorityQueue<>(
            Map.Entry.comparingByValue()
    );

    public WebsiteTraffic() {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::getDashboard, 5, 5, TimeUnit.SECONDS);
    }

    public void processEvent(PageViewEvent event) {

        pageViews.merge(event.url, 1, Integer::sum);

        uniqueVisitors.computeIfAbsent(event.url, k -> ConcurrentHashMap.newKeySet()).add(event.userId);

        trafficSources.merge(event.source, 1, Integer::sum);
    }

    public void getDashboard() {
        System.out.println("Top Pages:");
        topPages.clear();
        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            topPages.offer(entry);
            if (topPages.size() > 10) topPages.poll();
        }
        List<Map.Entry<String, Integer>> topList = new ArrayList<>(topPages);
        topList.sort((a, b) -> b.getValue() - a.getValue());
        for (int i = 0; i < topList.size(); i++) {
            Map.Entry<String, Integer> e = topList.get(i);
            int unique = uniqueVisitors.getOrDefault(e.getKey(), Collections.emptySet()).size();
            System.out.printf("%d. %s - %d views (%d unique)%n", i + 1, e.getKey(), e.getValue(), unique);
        }

        System.out.println("\nTraffic Sources:");
        int total = trafficSources.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percent = total > 0 ? (entry.getValue() * 100.0 / total) : 0;
            System.out.printf("%s: %.0f%%\n", entry.getKey(), percent);
        }
        System.out.println("-----------\n");
    }

    public static void main(String[] args) throws InterruptedException {
        WebsiteTraffic analytics = new WebsiteTraffic();
        analytics.processEvent(new PageViewEvent("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new PageViewEvent("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new PageViewEvent("/sports/championship", "user_789", "direct"));

        Thread.sleep(15000);
    }
}