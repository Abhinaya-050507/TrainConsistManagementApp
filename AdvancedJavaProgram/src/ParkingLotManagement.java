import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingLotManagement {
    private static final int SIZE = 500;

    private enum SpotStatus { EMPTY, OCCUPIED, DELETED }

    private static class Spot {
        SpotStatus status = SpotStatus.EMPTY;
        String licensePlate;
        LocalDateTime entryTime;
        int probes;
    }

    private final Spot[] spots = new Spot[SIZE];
    private int totalProbes = 0;
    private int parkedVehicles = 0;

    public ParkingLotManagement() {
        for (int i = 0; i < SIZE; i++) spots[i] = new Spot();
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % SIZE;
    }

    public int parkVehicle(String licensePlate) {
        int preferred = hash(licensePlate);
        int probes = 0;
        for (int i = 0; i < SIZE; i++) {
            int spotIndex = (preferred + i) % SIZE;
            probes = i;
            if (spots[spotIndex].status == SpotStatus.EMPTY || spots[spotIndex].status == SpotStatus.DELETED) {
                spots[spotIndex].licensePlate = licensePlate;
                spots[spotIndex].status = SpotStatus.OCCUPIED;
                spots[spotIndex].entryTime = LocalDateTime.now();
                spots[spotIndex].probes = probes;
                parkedVehicles++;
                totalProbes += probes;
                System.out.println("Assigned spot #" + spotIndex + " (" + probes + " probes)");
                return spotIndex;
            }
        }
        System.out.println("Parking Full!");
        return -1;
    }

    public void exitVehicle(String licensePlate, double ratePerHour) {
        for (int i = 0; i < SIZE; i++) {
            if (spots[i].status == SpotStatus.OCCUPIED && spots[i].licensePlate.equals(licensePlate)) {
                LocalDateTime exitTime = LocalDateTime.now();
                Duration duration = Duration.between(spots[i].entryTime, exitTime);
                double hours = duration.toMinutes() / 60.0;
                double fee = hours * ratePerHour;

                System.out.printf("Spot #%d freed, Duration: %.2f h, Fee: $%.2f%n", i, hours, fee);

                spots[i].status = SpotStatus.DELETED;
                spots[i].licensePlate = null;
                spots[i].entryTime = null;
                parkedVehicles--;
                return;
            }
        }
        System.out.println("Vehicle not found!");
    }

    public void getStatistics() {
        double occupancy = (parkedVehicles * 100.0) / SIZE;
        double avgProbes = parkedVehicles > 0 ? (totalProbes * 1.0 / parkedVehicles) : 0;
        System.out.printf("Occupancy: %.1f%%, Avg Probes: %.2f%n", occupancy, avgProbes);
        // Peak hour can be implemented by tracking entry times in a histogram (not included here)
    }

    // Example usage
    public static void main(String[] args) throws InterruptedException {
        ParkingLotManagement lot = new ParkingLotManagement();
        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking duration
        lot.exitVehicle("ABC-1234", 5.50);
        lot.getStatistics();
    }
}