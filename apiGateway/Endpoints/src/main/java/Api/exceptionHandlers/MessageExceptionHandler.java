package Api.exceptionHandlers;

import MessagingEntities.MessageModel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/*
 * reflection let's gooooo ;)
 */
@Component
public class MessageExceptionHandler {

    @SneakyThrows
    public void checkMessageForExceptions(MessageModel message) {
        if (message.getOperation().equals("throw")) {
            this.throwExceptionByName(
                    "Api.customExceptions." + message.getHeaders().get("Exception").toString(),
                    message.getHeaders().get("Message").toString());

        }
    }

    @SneakyThrows
    public void throwExceptionByName(String exceptionName, String message) {
        Class<?> clazz = Class.forName(exceptionName);

        if (Throwable.class.isAssignableFrom(clazz)) {
            Throwable throwable = (Throwable) clazz.getConstructor(String.class).newInstance(message);
            throw (Exception) throwable;
        } else {
            throw new IllegalArgumentException("Invalid exception class name: " + exceptionName);
        }
    }
}
