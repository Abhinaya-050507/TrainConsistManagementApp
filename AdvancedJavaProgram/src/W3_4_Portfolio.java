import java.util.*;

class Asset {
    String id;
    double returnRate;
    double volatility;

    public Asset(String id, double returnRate, double volatility) {
        this.id = id;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    @Override
    public String toString() {
        return id + "(return=" + returnRate + ", vol=" + volatility + ")";
    }
}

class AssetSorter {

    public static void mergeSort(Asset[] assets) {
        if (assets.length < 2) return;
        mergeSort(assets, 0, assets.length - 1);
    }

    private static void mergeSort(Asset[] assets, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        mergeSort(assets, left, mid);
        mergeSort(assets, mid + 1, right);
        merge(assets, left, mid, right);
    }

    private static void merge(Asset[] assets, int left, int mid, int right) {
        Asset[] temp = new Asset[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (assets[i].returnRate <= assets[j].returnRate) {
                temp[k++] = assets[i++];
            } else {
                temp[k++] = assets[j++];
            }
        }

        while (i <= mid) temp[k++] = assets[i++];
        while (j <= right) temp[k++] = assets[j++];

        System.arraycopy(temp, 0, assets, left, temp.length);
    }

    public static void quickSortDesc(Asset[] assets) {
        quickSortDesc(assets, 0, assets.length - 1);
    }

    private static void quickSortDesc(Asset[] assets, int low, int high) {
        if (low < high) {
            int pi = partition(assets, low, high);
            quickSortDesc(assets, low, pi - 1);
            quickSortDesc(assets, pi + 1, high);
        }
    }

    private static int partition(Asset[] assets, int low, int high) {
        int pivotIndex = medianOfThree(assets, low, high);
        Asset pivot = assets[pivotIndex];
        swap(assets, pivotIndex, high);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (assets[j].returnRate > pivot.returnRate ||
                    (assets[j].returnRate == pivot.returnRate &&
                            assets[j].volatility < pivot.volatility)) {
                i++;
                swap(assets, i, j);
            }
        }
        swap(assets, i + 1, high);
        return i + 1;
    }

    private static void swap(Asset[] assets, int i, int j) {
        Asset temp = assets[i];
        assets[i] = assets[j];
        assets[j] = temp;
    }

    private static int medianOfThree(Asset[] assets, int low, int high) {
        int mid = low + (high - low) / 2;
        Asset a = assets[low];
        Asset b = assets[mid];
        Asset c = assets[high];

        if ((a.returnRate > b.returnRate) != (a.returnRate > c.returnRate)) return low;
        else if ((b.returnRate > a.returnRate) != (b.returnRate > c.returnRate)) return mid;
        else return high;
    }
}

public class W3_4_Portfolio {

    public static void main(String[] args) {

        Asset[] assets = new Asset[] {
                new Asset("assetA", 0.12, 0.3),
                new Asset("assetB", 0.08, 0.2),
                new Asset("assetC", 0.12, 0.25),
                new Asset("assetD", 0.05, 0.1),
                new Asset("assetE", 0.15, 0.35)
        };

        Asset[] mergeSorted = Arrays.copyOf(assets, assets.length);
        AssetSorter.mergeSort(mergeSorted);
        System.out.println("MergeSort (ascending returnRate):");
        System.out.println(Arrays.toString(mergeSorted));

        Asset[] quickSorted = Arrays.copyOf(assets, assets.length);
        AssetSorter.quickSortDesc(quickSorted);
        System.out.println("\nQuickSort (desc returnRate + asc volatility):");
        System.out.println(Arrays.toString(quickSorted));
    }
}