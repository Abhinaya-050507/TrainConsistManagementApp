import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    private List<Bogie> getSampleBogies() {
        return Arrays.asList(
                new Bogie("Sleeper", 72),
                new Bogie("AC Chair", 60),
                new Bogie("Sleeper", 72),
                new Bogie("First Class", 40),
                new Bogie("AC Chair", 60)
        );
    }

    @Test
    void testReduce_TotalSeatCalculation() {
        int total = getSampleBogies().stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertEquals(304, total); // 72+60+72+40+60
    }

    @Test
    void testReduce_MultipleBogiesAggregation() {
        int total = getSampleBogies().stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertTrue(total > 0);
    }

    @Test
    void testReduce_SingleBogieCapacity() {
        List<Bogie> single = Arrays.asList(
                new Bogie("Sleeper", 72)
        );

        int total = single.stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertEquals(72, total);
    }

    @Test
    void testReduce_EmptyBogieList() {
        List<Bogie> empty = new ArrayList<>();

        int total = empty.stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertEquals(0, total);
    }

    @Test
    void testReduce_CorrectCapacityExtraction() {
        List<Bogie> list = getSampleBogies();

        List<Integer> capacities = list.stream()
                .map(Bogie::getCapacity)
                .toList();

        assertEquals(Arrays.asList(72, 60, 72, 40, 60), capacities);
    }

    @Test
    void testReduce_AllBogiesIncluded() {
        int expected = 72 + 60 + 72 + 40 + 60;

        int actual = getSampleBogies().stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertEquals(expected, actual);
    }

    @Test
    void testReduce_OriginalListUnchanged() {
        List<Bogie> original = getSampleBogies();
        int sizeBefore = original.size();

        int total = original.stream()
                .map(Bogie::getCapacity)
                .reduce(0, Integer::sum);

        assertEquals(sizeBefore, original.size());
    }
}