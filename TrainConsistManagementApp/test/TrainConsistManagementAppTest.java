import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    private List<Bogie> getSampleBogies() {
        return Arrays.asList(
                new Bogie("Sleeper", 72),
                new Bogie("AC Chair", 60),
                new Bogie("General", 50),
                new Bogie("First Class", 80)
        );
    }

    @Test
    void testLoopFilteringLogic() {
        List<Bogie> result = new ArrayList<>();

        for (Bogie b : getSampleBogies()) {
            if (b.getCapacity() > 60) {
                result.add(b);
            }
        }

        assertEquals(2, result.size()); // 72 & 80
    }

    @Test
    void testStreamFilteringLogic() {
        List<Bogie> result = getSampleBogies().stream()
                .filter(b -> b.getCapacity() > 60)
                .collect(Collectors.toList());

        assertEquals(2, result.size());
    }

    @Test
    void testLoopAndStreamResultsMatch() {
        List<Bogie> list = getSampleBogies();

        List<Bogie> loopResult = new ArrayList<>();
        for (Bogie b : list) {
            if (b.getCapacity() > 60) {
                loopResult.add(b);
            }
        }

        List<Bogie> streamResult = list.stream()
                .filter(b -> b.getCapacity() > 60)
                .collect(Collectors.toList());

        assertEquals(loopResult.size(), streamResult.size());
    }

    @Test
    void testExecutionTimeMeasurement() {
        List<Bogie> list = getSampleBogies();

        long start = System.nanoTime();

        list.stream()
                .filter(b -> b.getCapacity() > 60)
                .collect(Collectors.toList());

        long end = System.nanoTime();

        long duration = end - start;

        assertTrue(duration > 0);
    }

    @Test
    void testLargeDatasetProcessing() {
        List<Bogie> bogies = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            bogies.add(new Bogie("Type", i % 100));
        }

        List<Bogie> result = bogies.stream()
                .filter(b -> b.getCapacity() > 60)
                .collect(Collectors.toList());

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}