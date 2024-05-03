package Api.customExceptions;

public class CatNotFoundException extends RuntimeException {

    public CatNotFoundException(String message) {
        super(message);
    }
}
