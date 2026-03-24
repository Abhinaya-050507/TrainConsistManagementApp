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

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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
}

class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservations;
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

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }

        inventory.put(roomType, available - 1);
    }
}

class BookingReportService {
    public void generateReport(List<Reservation> reservations) {

        System.out.println("\nBooking History Report");

        for (Reservation r : reservations) {
            System.out.println("Guest: " + r.getGuestName()
                    + ", Room Type: " + r.getRoomType());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookingValidator validator = new BookingValidator();
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.println("Booking Validation");

        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            validator.validate(guestName, roomType);

            inventory.allocateRoom(roomType);

            String reservationId = UUID.randomUUID().toString();
            Reservation reservation = new Reservation(reservationId, guestName, roomType);

            history.addReservation(reservation);

            System.out.println("Booking successful! Reservation ID: " + reservationId);

        } catch (InvalidBookingException e) {

            System.out.println("Booking failed: " + e.getMessage());
        }

        reportService.generateReport(history.getAllReservations());

        scanner.close();
    }
}