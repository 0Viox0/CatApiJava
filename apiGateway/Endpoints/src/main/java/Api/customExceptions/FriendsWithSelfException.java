package Api.customExceptions;

public class FriendsWithSelfException extends RuntimeException {

    public FriendsWithSelfException(String message) {
        super(message);
    }
}
