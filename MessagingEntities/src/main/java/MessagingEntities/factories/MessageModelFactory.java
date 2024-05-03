package MessagingEntities.factories;

import MessagingEntities.MessageModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MessageModelFactory {

    public static MessageModel getRegularMessage() {
        return new MessageModel();
    }

    public static MessageModel getExceptionMessage(
            String exceptionName,
            String exceptionMessage
    ) {
        MessageModel message = new MessageModel();

        message.setOperation("throw");
        message.setHeaders(new HashMap<>() {{
            put("Exception", exceptionName);
            put("Message", exceptionMessage);
        }});

        return message;
    }
}
