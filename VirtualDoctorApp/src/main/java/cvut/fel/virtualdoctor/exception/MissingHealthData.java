package cvut.fel.virtualdoctor.exception;

public class MissingHealthData extends RuntimeException {
    public MissingHealthData(String message) {
        super(message);
    }
}
