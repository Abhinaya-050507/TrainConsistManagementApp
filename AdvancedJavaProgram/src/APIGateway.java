import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class TokenBucket {
    private final int maxTokens;
    private final int refillRatePerHour;
    private AtomicInteger tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, int refillRatePerHour) {
        this.maxTokens = maxTokens;
        this.refillRatePerHour = refillRatePerHour;
        this.tokens = new AtomicInteger(maxTokens);
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        } else {
            return false;
        }
    }

    public synchronized int getRemainingTokens() {
        refillTokens();
        return tokens.get();
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastRefillTime;
        int tokensToAdd = (int) (elapsedMillis * refillRatePerHour / 3600_000);
        if (tokensToAdd > 0) {
            int newTokens = Math.min(maxTokens, tokens.get() + tokensToAdd);
            tokens.set(newTokens);
            lastRefillTime = now;
        }
    }
}

public class APIGateway {
    private final ConcurrentHashMap<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 1000;
    private final int REFILL_RATE = 1000;
    public boolean checkRateLimit(String clientId) {
        TokenBucket bucket = clientBuckets.computeIfAbsent(clientId, k -> new TokenBucket(MAX_REQUESTS, REFILL_RATE));
        boolean allowed = bucket.allowRequest();
        if (allowed) {
            System.out.println("Allowed (" + bucket.getRemainingTokens() + " requests remaining)");
        } else {
            System.out.println("Denied (0 requests remaining, retry after 3600s)");
        }
        return allowed;
    }

    public void getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.computeIfAbsent(clientId, k -> new TokenBucket(MAX_REQUESTS, REFILL_RATE));
        int used = MAX_REQUESTS - bucket.getRemainingTokens();
        long resetTime = System.currentTimeMillis() / 1000 + 3600; // 1 hour reset
        System.out.println("{used: " + used + ", limit: " + MAX_REQUESTS + ", reset: " + resetTime + "}");
    }

    public static void main(String[] args) throws InterruptedException {
        APIGateway api = new APIGateway();
        String client = "abc123";

        api.checkRateLimit(client);
        api.checkRateLimit(client);
        api.getRateLimitStatus(client);
    }
}