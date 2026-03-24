import java.util.*;

class TransactionLog {
    String accountId;

    public TransactionLog(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return accountId;
    }
}

class TransactionSearch {

    public static int linearFirst(TransactionLog[] logs, String target) {
        int comparisons = 0;
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].accountId.equals(target)) {
                System.out.println("Linear first " + target + ": index " + i + " (" + comparisons + " comparisons)");
                return i;
            }
        }
        System.out.println("Linear first " + target + ": not found (" + comparisons + " comparisons)");
        return -1;
    }

    public static int linearLast(TransactionLog[] logs, String target) {
        int comparisons = 0;
        int lastIndex = -1;
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].accountId.equals(target)) lastIndex = i;
        }
        if (lastIndex != -1)
            System.out.println("Linear last " + target + ": index " + lastIndex + " (" + comparisons + " comparisons)");
        else
            System.out.println("Linear last " + target + ": not found (" + comparisons + " comparisons)");
        return lastIndex;
    }

    public static int binarySearchFirst(TransactionLog[] logs, String target) {
        int comparisons = 0;
        int low = 0, high = logs.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            int cmp = logs[mid].accountId.compareTo(target);

            if (cmp == 0) {
                result = mid;
                high = mid - 1; // look left for first
            } else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }

        if (result != -1)
            System.out.println("Binary first " + target + ": index " + result + " (" + comparisons + " comparisons)");
        else
            System.out.println("Binary first " + target + ": not found (" + comparisons + " comparisons)");
        return result;
    }

    public static int binarySearchLast(TransactionLog[] logs, String target) {
        int comparisons = 0;
        int low = 0, high = logs.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            int cmp = logs[mid].accountId.compareTo(target);

            if (cmp == 0) {
                result = mid;
                low = mid + 1; // look right for last
            } else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }

        if (result != -1)
            System.out.println("Binary last " + target + ": index " + result + " (" + comparisons + " comparisons)");
        else
            System.out.println("Binary last " + target + ": not found (" + comparisons + " comparisons)");
        return result;
    }

    public static int countOccurrences(TransactionLog[] logs, String target) {
        int first = binarySearchFirst(logs, target);
        if (first == -1) return 0;
        int last = binarySearchLast(logs, target);
        int count = last - first + 1;
        System.out.println("Count of " + target + ": " + count);
        return count;
    }
}

public class W3_5_TransactionLogs {

    public static void main(String[] args) {

        TransactionLog[] logs = new TransactionLog[] {
                new TransactionLog("accB"),
                new TransactionLog("accA"),
                new TransactionLog("accB"),
                new TransactionLog("accC")
        };

        System.out.println("Original logs: " + Arrays.toString(logs));

        TransactionSearch.linearFirst(logs, "accB");
        TransactionSearch.linearLast(logs, "accB");

        TransactionLog[] sortedLogs = Arrays.copyOf(logs, logs.length);
        Arrays.sort(sortedLogs, Comparator.comparing(l -> l.accountId));
        System.out.println("Sorted logs: " + Arrays.toString(sortedLogs));

        TransactionSearch.binarySearchFirst(sortedLogs, "accB");
        TransactionSearch.binarySearchLast(sortedLogs, "accB");
        TransactionSearch.countOccurrences(sortedLogs, "accB");
    }
}