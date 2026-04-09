import org.junit.jupiter.api.Test;
import java.util.regex.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    private static final String TRAIN_ID_REGEX = "TRN-\\d{4}";
    private static final String CARGO_CODE_REGEX = "PET-[A-Z]{2}";

    private boolean isValidTrainId(String trainId) {
        return Pattern.compile(TRAIN_ID_REGEX)
                .matcher(trainId)
                .matches();
    }

    private boolean isValidCargoCode(String cargoCode) {
        return Pattern.compile(CARGO_CODE_REGEX)
                .matcher(cargoCode)
                .matches();
    }

    @Test
    void testRegex_ValidTrainID() {
        assertTrue(isValidTrainId("TRN-1234"));
    }

    @Test
    void testRegex_InvalidTrainIDFormat() {
        assertFalse(isValidTrainId("TRAIN12"));
        assertFalse(isValidTrainId("TRN12A"));
        assertFalse(isValidTrainId("1234-TRN"));
    }

    @Test
    void testRegex_ValidCargoCode() {
        assertTrue(isValidCargoCode("PET-AB"));
    }

    @Test
    void testRegex_InvalidCargoCodeFormat() {
        assertFalse(isValidCargoCode("PET-ab"));
        assertFalse(isValidCargoCode("PET123"));
        assertFalse(isValidCargoCode("AB-PET"));
    }

    @Test
    void testRegex_TrainIDDigitLengthValidation() {
        assertFalse(isValidTrainId("TRN-123"));    // less digits
        assertFalse(isValidTrainId("TRN-12345"));  // more digits
    }

    @Test
    void testRegex_CargoCodeUppercaseValidation() {
        assertFalse(isValidCargoCode("PET-Ab"));
        assertFalse(isValidCargoCode("PET-aB"));
    }

    @Test
    void testRegex_EmptyInputHandling() {
        assertFalse(isValidTrainId(""));
        assertFalse(isValidCargoCode(""));
    }

    @Test
    void testRegex_ExactPatternMatch() {
        assertFalse(isValidTrainId("TRN-1234-EXTRA"));
        assertFalse(isValidCargoCode("PET-AB123"));
    }
}