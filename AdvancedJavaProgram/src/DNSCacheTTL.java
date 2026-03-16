import java.util.*;
import java.util.concurrent.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheTTL {
    private final int MAX_CACHE_SIZE = 100;
    private final Map<String, DNSEntry> cache;
    private int hits = 0, misses = 0;

    public DNSCacheTTL() {
        // LRU cache using accessOrder = true
        cache = Collections.synchronizedMap(new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        });

        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
        cleaner.scheduleAtFixedRate(() -> {
            synchronized (cache) {
                cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public String resolve(String domain) {
        DNSEntry entry;
        synchronized (cache) {
            entry = cache.get(domain);
        }

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        } else {
            misses++;
            String ip = queryUpstreamDNS(domain);
            DNSEntry newEntry = new DNSEntry(domain, ip, 300); // TTL 300s
            synchronized (cache) {
                cache.put(domain, newEntry);
            }
            return "Cache MISS → " + ip + " (TTL: 300s)";
        }
    }

    private String queryUpstreamDNS(String domain) {
        // Simulate upstream DNS lookup
        return "172.217.14." + new Random().nextInt(255);
    }

    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total > 0 ? (hits * 100.0 / total) : 0;
        return String.format("Hit Rate: %.1f%%, Lookups: %d", hitRate, total);
    }

    // Example usage
    public static void main(String[] args) throws InterruptedException {
        DNSCacheTTL dnsCache = new DNSCacheTTL();
        System.out.println(dnsCache.resolve("google.com")); // MISS
        System.out.println(dnsCache.resolve("google.com")); // HIT
        Thread.sleep(301_000); // wait for TTL to expire
        System.out.println(dnsCache.resolve("google.com")); // EXPIRED → MISS
        System.out.println(dnsCache.getCacheStats());
    }
}