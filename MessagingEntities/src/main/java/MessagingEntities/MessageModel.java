package MessagingEntities;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
public class MessageModel implements Serializable {
    private String endpoint;
    private String operation;

    private Map<String, Object> payload;
    private Map<String, Object> headers;
}
