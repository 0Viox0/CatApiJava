package Api.customExceptions;

public class InvalidCatColorException extends RuntimeException {

    public InvalidCatColorException(String message) {
        super(message);
    }
}
