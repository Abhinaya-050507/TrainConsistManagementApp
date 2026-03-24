import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }
}

class BookingHistory {

    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public Collection<Reservation> getAllReservations() {
        return reservations.values();
    }
}

class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Single", "Double", "Suite"));

    public void validate(String guestName, String roomType) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
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

    public String allocateRoom(String roomType) throws InvalidBookingException {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available.");
        }

        inventory.put(roomType, available - 1);

        int count = counters.get(roomType) + 1;
        counters.put(roomType, count);

        return roomType + "-" + count;
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory,
                              Map<String, String> reservationToRoomMap)
            throws InvalidBookingException {

        Reservation reservation = history.getReservation(reservationId);

        if (reservation == null) {
            throw new InvalidBookingException("Reservation does not exist.");
        }

        if (reservation.isCancelled()) {
            throw new InvalidBookingException("Reservation already cancelled.");
        }

        String roomId = reservationToRoomMap.get(reservationId);

        rollbackStack.push(roomId);

        inventory.releaseRoom(reservation.getRoomType());

        reservation.cancel();

        System.out.println("Booking cancelled successfully. Inventory restored for room type: "
                + reservation.getRoomType());
    }

    public void printRollbackHistory(RoomInventory inventory, String roomType) {

        System.out.println("\nRollback History (Most Recent First):");

        Stack<String> temp = (Stack<String>) rollbackStack.clone();

        while (!temp.isEmpty()) {
            System.out.println("Released Reservation ID: " + temp.pop());
        }

        System.out.println("Updated " + roomType + " Room Availability: "
                + inventory.getAvailability(roomType));
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingValidator validator = new BookingValidator();
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        Map<String, String> reservationToRoomMap = new HashMap<>();

        try {

            String guestName = "Abhi";
            String roomType = "Single";

            validator.validate(guestName, roomType);

            String roomId = inventory.allocateRoom(roomType);

            String reservationId = UUID.randomUUID().toString();

            Reservation reservation = new Reservation(reservationId, guestName, roomType);

            history.addReservation(reservation);
            reservationToRoomMap.put(reservationId, roomId);

            System.out.println("Booking successful: " + reservationId + " -> " + roomId);

            System.out.println("\nBooking Cancellation");
            cancellationService.cancelBooking(
                    reservationId, history, inventory, reservationToRoomMap
            );

            cancellationService.printRollbackHistory(inventory, roomType);

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}