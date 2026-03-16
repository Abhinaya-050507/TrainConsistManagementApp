import java.util.HashMap;
import java.util.Map;

class BookMyStayApp {

    private HashMap<String, Integer> inventory;

    public BookMyStayApp() {
        inventory = new HashMap<>();
    }

    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean bookRoom(String roomType) {
        int available = getAvailability(roomType);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }

        return false;
    }

    public void releaseRoom(String roomType) {
        int available = getAvailability(roomType);
        inventory.put(roomType, available + 1);
    }

    public void displayInventory() {

        System.out.println("Hotel Room Inventory Status\n");

        System.out.println("Single Room:");
        System.out.println("Available Rooms: " + getAvailability("Single Room"));

        System.out.println("\nDouble Room:");
        System.out.println("Available Rooms: " + getAvailability("Double Room"));

        System.out.println("\nSuite Room:");
        System.out.println("Available Rooms: " + getAvailability("Suite Room"));
    }
}

class HotelSystem {

    public static void main(String[] args) {

        BookMyStayApp inventory = new BookMyStayApp();

        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 2);

        inventory.displayInventory();
    }
}