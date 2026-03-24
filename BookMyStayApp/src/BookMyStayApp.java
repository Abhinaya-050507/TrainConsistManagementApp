import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify();
    }

    public synchronized BookingRequest getRequest() {

        while (queue.isEmpty()) {
            try {
                wait(); // wait until request is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return queue.poll();
    }
}

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Integer> counters = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);

        counters.put("Single", 0);
        counters.put("Double", 0);
        counters.put("Suite", 0);
    }

    public synchronized String allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            return null;
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        inventory.put(roomType, available - 1);

        int count = counters.get(roomType) + 1;
        counters.put(roomType, count);

        return roomType + "-" + count;
    }

    public void printInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        for (int i = 0; i < 2; i++) {

            BookingRequest request = queue.getRequest();

            String roomId = inventory.allocateRoom(request.roomType);

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room ID: " + roomId);
            } else {
                System.out.println("Booking failed for Guest: "
                        + request.guestName + " (No availability)");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        System.out.println("Concurrent Booking Simulation\n");

        queue.addRequest(new BookingRequest("Abhi", "Single"));
        queue.addRequest(new BookingRequest("Vanmathi", "Double"));
        queue.addRequest(new BookingRequest("Kural", "Suite"));
        queue.addRequest(new BookingRequest("Subha", "Single"));

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        inventory.printInventory();
    }
}