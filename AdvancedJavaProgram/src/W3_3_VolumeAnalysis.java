import java.util.*;

class Trade {
    String id;
    int volume;

    public Trade(String id, int volume) {
        this.id = id;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return id + ":" + volume;
    }
}

class TradeSorter {

    public static void mergeSort(Trade[] trades) {
        if (trades.length < 2) return;
        mergeSort(trades, 0, trades.length - 1);
    }

    private static void mergeSort(Trade[] trades, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        mergeSort(trades, left, mid);
        mergeSort(trades, mid + 1, right);
        merge(trades, left, mid, right);
    }

    private static void merge(Trade[] trades, int left, int mid, int right) {
        Trade[] temp = new Trade[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (trades[i].volume <= trades[j].volume) {
                temp[k++] = trades[i++];
            } else {
                temp[k++] = trades[j++];
            }
        }

        while (i <= mid) temp[k++] = trades[i++];
        while (j <= right) temp[k++] = trades[j++];

        for (i = 0; i < temp.length; i++) {
            trades[left + i] = temp[i];
        }
    }

    public static void quickSortDesc(Trade[] trades) {
        quickSortDesc(trades, 0, trades.length - 1);
    }

    private static void quickSortDesc(Trade[] trades, int low, int high) {
        if (low < high) {
            int pi = partition(trades, low, high);
            quickSortDesc(trades, low, pi - 1);
            quickSortDesc(trades, pi + 1, high);
        }
    }

    private static int partition(Trade[] trades, int low, int high) {
        int pivot = trades[high].volume; // Lomuto pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (trades[j].volume >= pivot) { // descending
                i++;
                Trade temp = trades[i];
                trades[i] = trades[j];
                trades[j] = temp;
            }
        }

        Trade temp = trades[i + 1];
        trades[i + 1] = trades[high];
        trades[high] = temp;

        return i + 1;
    }

    public static Trade[] mergeTwoSorted(Trade[] a, Trade[] b) {
        int n = a.length, m = b.length;
        Trade[] merged = new Trade[n + m];
        int i = 0, j = 0, k = 0;

        while (i < n && j < m) {
            if (a[i].volume <= b[j].volume) {
                merged[k++] = a[i++];
            } else {
                merged[k++] = b[j++];
            }
        }

        while (i < n) merged[k++] = a[i++];
        while (j < m) merged[k++] = b[j++];

        return merged;
    }

    public static long totalVolume(Trade[] trades) {
        long sum = 0;
        for (Trade t : trades) sum += t.volume;
        return sum;
    }
}

public class W3_3_VolumeAnalysis {

    public static void main(String[] args) {

        Trade[] morningTrades = new Trade[] {
                new Trade("trade3", 500),
                new Trade("trade1", 100),
                new Trade("trade2", 300)
        };

        Trade[] afternoonTrades = new Trade[] {
                new Trade("trade4", 200),
                new Trade("trade5", 400)
        };

        Trade[] morningSorted = Arrays.copyOf(morningTrades, morningTrades.length);
        TradeSorter.mergeSort(morningSorted);
        System.out.println("MergeSort (ascending): " + Arrays.toString(morningSorted));

        Trade[] morningQuick = Arrays.copyOf(morningTrades, morningTrades.length);
        TradeSorter.quickSortDesc(morningQuick);
        System.out.println("QuickSort (descending): " + Arrays.toString(morningQuick));

        TradeSorter.mergeSort(afternoonTrades);
        Trade[] mergedTrades = TradeSorter.mergeTwoSorted(morningSorted, afternoonTrades);
        System.out.println("Merged morning+afternoon: " + Arrays.toString(mergedTrades));

        long total = TradeSorter.totalVolume(mergedTrades);
        System.out.println("Merged morning+afternoon total volume: " + total);
    }
}