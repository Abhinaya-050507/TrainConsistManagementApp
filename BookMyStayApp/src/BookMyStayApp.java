import java.util.*;

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

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("Booking History Report");

        for (Reservation r : reservations) {
            System.out.println(
                    "Guest: " + r.getGuestName() +
                            ", Room Type: " + r.getRoomType()
            );
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        Reservation r1 = new Reservation("RES1", "Abhi", "Single");
        Reservation r2 = new Reservation("RES2", "Subha", "Double");
        Reservation r3 = new Reservation("RES3", "Vanmathi", "Suite");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        reportService.generateReport(history.getAllReservations());
    }
}