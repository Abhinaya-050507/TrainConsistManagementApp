import java.util.regex.*;

public class TrainConsistManagementApp {

    private static final String TRAIN_ID_REGEX = "TRN-\\d{4}";
    private static final String CARGO_CODE_REGEX = "PET-[A-Z]{2}";

    public static void main(String[] args) {

        String trainId = "TRN-1234";
        String cargoCode = "PET-AB";

        Pattern trainPattern = Pattern.compile(TRAIN_ID_REGEX);
        Pattern cargoPattern = Pattern.compile(CARGO_CODE_REGEX);

        Matcher trainMatcher = trainPattern.matcher(trainId);
        Matcher cargoMatcher = cargoPattern.matcher(cargoCode);

        boolean isTrainValid = trainMatcher.matches();
        boolean isCargoValid = cargoMatcher.matches();

        System.out.println("Train ID: " + trainId + " -> " +
                (isTrainValid ? "Valid" : "Invalid"));

        System.out.println("Cargo Code: " + cargoCode + " -> " +
                (isCargoValid ? "Valid" : "Invalid"));

    }
}