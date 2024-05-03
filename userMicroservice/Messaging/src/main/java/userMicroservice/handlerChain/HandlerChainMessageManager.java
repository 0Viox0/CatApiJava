package userMicroservice.handlerChain;

import MessagingEntities.MessageModel;
import org.springframework.stereotype.Component;

@Component
public class HandlerChainMessageManager {

    private final GetManyUsersHandler getManyUsersHandler;

    public HandlerChainMessageManager(
            GetManyUsersHandler getManyUsersHandler
    ) {
        this.getManyUsersHandler = getManyUsersHandler;

        this.getManyUsersHandler
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyUsersHandler.handle(message);
    }
}
