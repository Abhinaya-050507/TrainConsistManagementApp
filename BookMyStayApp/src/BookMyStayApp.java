import java.util.*;

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void registerRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }
}

class BookingService {

    private Queue<Reservation> requestQueue;
    private HashMap<String, Set<String>> allocatedRooms;
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        requestQueue = new LinkedList<>();
        allocatedRooms = new HashMap<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public void processBookings() {

        System.out.println("Room Allocation Processing");

        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll();
            String roomType = request.roomType;

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());

                Set<String> roomSet = allocatedRooms.get(roomType);

                String roomId = roomType + "-" + (roomSet.size() + 1);

                roomSet.add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room ID: " + roomId);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.registerRoom("Single", 5);
        inventory.registerRoom("Suite", 2);

        BookingService bookingService = new BookingService(inventory);

        bookingService.addRequest(new Reservation("Abhi", "Single"));
        bookingService.addRequest(new Reservation("Subha", "Single"));
        bookingService.addRequest(new Reservation("Vanmathi", "Suite"));

        bookingService.processBookings();
    }
}