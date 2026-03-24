import java.util.*;

class RiskBandSearch {

    public static int linearSearch(int[] bands, int target) {
        int comparisons = 0;
        for (int i = 0; i < bands.length; i++) {
            comparisons++;
            if (bands[i] == target) {
                System.out.println("Linear: threshold=" + target + " → found at index " + i + " (" + comparisons + " comps)");
                return i;
            }
        }
        System.out.println("Linear: threshold=" + target + " → not found (" + comparisons + " comps)");
        return -1;
    }

    public static int binaryFloor(int[] sortedBands, int target) {
        int low = 0, high = sortedBands.length - 1;
        int comparisons = 0;
        int floor = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            if (sortedBands[mid] <= target) {
                floor = sortedBands[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Binary floor(" + target + "): " + floor + " (" + comparisons + " comps)");
        return floor;
    }

    public static int binaryCeiling(int[] sortedBands, int target) {
        int low = 0, high = sortedBands.length - 1;
        int comparisons = 0;
        int ceil = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            if (sortedBands[mid] >= target) {
                ceil = sortedBands[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        System.out.println("Binary ceiling(" + target + "): " + ceil + " (" + comparisons + " comps)");
        return ceil;
    }

    public static int binaryInsertionIndex(int[] sortedBands, int target) {
        int low = 0, high = sortedBands.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sortedBands[mid] < target) low = mid + 1;
            else high = mid;
        }
        System.out.println("Insertion index for " + target + ": " + low);
        return low;
    }
}

public class W3_6_RiskThreshold {

    public static void main(String[] args) {

        int[] riskBands = new int[] {10, 25, 50, 100};
        int threshold = 30;

        System.out.println("Risk Bands: " + Arrays.toString(riskBands));

        int[] unsortedBands = new int[] {50, 10, 100, 25};
        RiskBandSearch.linearSearch(unsortedBands, threshold);

        RiskBandSearch.binaryFloor(riskBands, threshold);
        RiskBandSearch.binaryCeiling(riskBands, threshold);

        RiskBandSearch.binaryInsertionIndex(riskBands, threshold);
    }
}