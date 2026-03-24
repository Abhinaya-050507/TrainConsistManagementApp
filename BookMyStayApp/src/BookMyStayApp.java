import java.util.*;

class Service {

    private String serviceName;

    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {

    private Map<String, List<Service>> servicesByReservation;

    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, Service service) {

        List<Service> services = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        services.add(service);

        servicesByReservation.put(reservationId, services);
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<Service> services = servicesByReservation.get(reservationId);

        if (services == null) {
            return 0.0;
        }

        double total = 0.0;
        for (Service service : services) {
            total += service.getCost();
        }

        return total;
    }

    public void printServices(String reservationId) {
        List<Service> services = servicesByReservation.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for reservation: " + reservationId);
            return;
        }

        System.out.println("Services for reservation " + reservationId + ":");
        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " (" + s.getCost() + ")");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "RES123";

        Service breakfast = new Service("Breakfast", 20.0);
        Service airportPickup = new Service("Airport Pickup", 50.0);
        Service spa = new Service("Spa Access", 35.0);

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        manager.printServices(reservationId);
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Total add-on cost: " + totalCost);
    }
}