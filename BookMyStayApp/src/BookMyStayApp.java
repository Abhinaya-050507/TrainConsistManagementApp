import java.io.*;
import java.util.*;

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void printInventory() {
        System.out.println("Current Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> bookings = new ArrayList<>();

    public void addBooking(String booking) {
        bookings.add(booking);
    }

    public List<String> getBookings() {
        return bookings;
    }
}

class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No valid inventory data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Corrupted data. Starting with clean state.");
        }

        return null;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        RoomInventory inventory;
        BookingHistory history;

        System.out.println("System Recovery");

        SystemState loadedState = persistenceService.load();

        if (loadedState != null) {
            inventory = loadedState.inventory;
            history = loadedState.history;
            System.out.println("System state restored successfully.\n");
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        inventory.printInventory();

        history.addBooking("Guest: Abhi, Room Type: Single");

        SystemState newState = new SystemState(inventory, history);
        persistenceService.save(newState);
    }
}