
import java.util.*;

class Client {
    String id;
    int riskScore;
    double accountBalance;

    public Client(String id, int riskScore, double accountBalance) {
        this.id = id;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return id + "(" + riskScore + ")";
    }
}

class ClientSorter {

    public static void bubbleSortAsc(Client[] clients) {
        int n = clients.length;
        int swaps = 0;

        System.out.println("\nBubble Sort (ascending riskScore):");
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (clients[j].riskScore > clients[j + 1].riskScore) {
                    Client temp = clients[j];
                    clients[j] = clients[j + 1];
                    clients[j + 1] = temp;
                    swaps++;
                    swapped = true;

                    System.out.println("Swapped: " + clients[j].id + " <-> " + clients[j + 1].id);
                }
            }
            if (!swapped) break;
        }

        System.out.println("Sorted (asc): " + Arrays.toString(clients));
        System.out.println("Total swaps: " + swaps);
    }

    public static void insertionSortDesc(Client[] clients) {
        int n = clients.length;

        for (int i = 1; i < n; i++) {
            Client key = clients[i];
            int j = i - 1;

            while (j >= 0 &&
                    (clients[j].riskScore < key.riskScore ||
                            (clients[j].riskScore == key.riskScore &&
                                    clients[j].accountBalance < key.accountBalance))) {

                clients[j + 1] = clients[j];
                j--;
            }

            clients[j + 1] = key;
        }

        System.out.println("\nInsertion Sort (desc riskScore + accountBalance):");
        System.out.println(Arrays.toString(clients));
    }

    public static List<Client> topNRiskClients(Client[] clients, int N) {
        List<Client> top = new ArrayList<>();
        for (int i = 0; i < Math.min(N, clients.length); i++) {
            top.add(clients[i]);
        }
        return top;
    }
}

public class W3_2_ClientRisk {

    public static void main(String[] args) {

        Client[] clients = new Client[] {
                new Client("clientC", 80, 5000.0),
                new Client("clientA", 20, 10000.0),
                new Client("clientB", 50, 7500.0),
                new Client("clientD", 80, 6000.0), // tie on riskScore
                new Client("clientE", 30, 3000.0)
        };

        Client[] bubbleArray = Arrays.copyOf(clients, clients.length);
        ClientSorter.bubbleSortAsc(bubbleArray);

        Client[] insertionArray = Arrays.copyOf(clients, clients.length);
        ClientSorter.insertionSortDesc(insertionArray);
        List<Client> topClients = ClientSorter.topNRiskClients(insertionArray, 10);
        System.out.println("\nTop Risk Clients:");
        for (Client c : topClients) {
            System.out.println(c.id + "(" + c.riskScore + ")");
        }
    }
}