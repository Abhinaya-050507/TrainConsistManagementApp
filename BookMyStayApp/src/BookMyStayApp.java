import java.util.HashMap;
import java.util.Map;

class Room {

    String type;
    int beds;
    int size;
    double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayDetails(int availability) {
        System.out.println(type + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + availability);
        System.out.println();
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

    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {

        System.out.println("Room Search\n");

        for (String roomType : roomCatalog.keySet()) {

            int availability = inventory.getAvailability(roomType);

            if (availability > 0) { // filter unavailable rooms

                Room room = roomCatalog.get(roomType);
                room.displayDetails(availability);

            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.registerRoom("Single Room", 5);
        inventory.registerRoom("Double Room", 3);
        inventory.registerRoom("Suite Room", 2);

        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single Room", new Room("Single Room", 1, 250, 1500.0));
        roomCatalog.put("Double Room", new Room("Double Room", 2, 400, 2500.0));
        roomCatalog.put("Suite Room", new Room("Suite Room", 3, 750, 5000.0));

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(inventory, roomCatalog);
    }
}