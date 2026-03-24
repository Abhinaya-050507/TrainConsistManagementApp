import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp; // format HH:MM

    public Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return id + ":" + fee + "@" + timestamp;
    }
}


/**
 * Sorting Utility
 */
class TransactionSorter {

    /**
     * Bubble Sort by fee (ascending), stable
     */
    public static void bubbleSortFee(List<Transaction> transactions) {
        int n = transactions.size();
        int passes = 0;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            passes++;
            for (int j = 0; j < n - i - 1; j++) {
                if (transactions.get(j).fee > transactions.get(j + 1).fee) {
                    Collections.swap(transactions, j, j + 1);
                    swapped = true;
                    swaps++;
                }
            }
            if (!swapped) break; // early termination
        }

        System.out.println("BubbleSort (fees): " + transactions + " // " + passes + " passes, " + swaps + " swaps");
    }

    public static void insertionSortFeeTimestamp(List<Transaction> transactions) {
        int n = transactions.size();

        for (int i = 1; i < n; i++) {
            Transaction key = transactions.get(i);
            int j = i - 1;

            while (j >= 0 &&
                    (transactions.get(j).fee > key.fee ||
                            (transactions.get(j).fee == key.fee &&
                                    transactions.get(j).timestamp.compareTo(key.timestamp) > 0))) {

                transactions.set(j + 1, transactions.get(j));
                j--;
            }

            transactions.set(j + 1, key);
        }

        System.out.println("InsertionSort (fee+ts): " + transactions);
    }

    public static void flagHighFeeOutliers(List<Transaction> transactions) {
        List<Transaction> outliers = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.fee > 50) {
                outliers.add(t);
            }
        }

        if (outliers.isEmpty()) {
            System.out.println("High-fee outliers: none");
        } else {
            System.out.println("High-fee outliers: " + outliers);
        }
    }
}

public class W3_1_AuditCompliance {

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));
        transactions.add(new Transaction("id4", 55.0, "08:45"));

        List<Transaction> bubbleList = new ArrayList<>(transactions.subList(0, 3));
        TransactionSorter.bubbleSortFee(bubbleList);
        TransactionSorter.flagHighFeeOutliers(bubbleList);

        List<Transaction> insertionList = new ArrayList<>(transactions);
        TransactionSorter.insertionSortFeeTimestamp(insertionList);
        TransactionSorter.flagHighFeeOutliers(insertionList);
    }
}