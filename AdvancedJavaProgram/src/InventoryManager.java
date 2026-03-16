import java.util.*;
        import java.util.concurrent.*;
        import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {
    private final ConcurrentHashMap<String, AtomicInteger> stockMap = new ConcurrentHashMap<>();
    private final Map<String, Queue<Integer>> waitingList = Collections.synchronizedMap(new LinkedHashMap<>());

    public void addProduct(String productId, int quantity) {
        stockMap.put(productId, new AtomicInteger(quantity));
    }

    public int checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        return stock != null ? stock.get() : 0;
    }

    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock != null) {
            int remaining = stock.decrementAndGet();
            if (remaining >= 0) {
                return "Success, " + remaining + " units remaining";
            } else {
                stock.incrementAndGet(); // rollback
                addToWaitingList(productId, userId);
                return "Out of stock, added to waiting list";
            }
        } else {
            return "Product not found";
        }
    }

    private void addToWaitingList(String productId, int userId) {
        synchronized (waitingList) {
            waitingList.computeIfAbsent(productId, k -> new LinkedList<>()).add(userId);
        }
    }

    public void restock(String productId, int quantity) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock != null) {
            stock.addAndGet(quantity);
            synchronized (waitingList) {
                Queue<Integer> queue = waitingList.get(productId);
                while (queue != null && !queue.isEmpty() && stock.get() > 0) {
                    int userId = queue.poll();
                    stock.decrementAndGet();
                    System.out.println("Notified user " + userId + ": Item now available!");
                }
            }
        }
    }
}