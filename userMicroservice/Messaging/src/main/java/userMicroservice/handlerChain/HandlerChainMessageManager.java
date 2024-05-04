package userMicroservice.handlerChain;

import MessagingEntities.MessageModel;
import org.springframework.stereotype.Component;
import userMicroservice.handlerChain.handlers.*;

@Component
public class HandlerChainMessageManager {

    private final GetManyUsersHandler getManyUsersHandler;

    public HandlerChainMessageManager(
            GetManyUsersHandler getManyUsersHandler,
            GetOneUserHandler getOneUserHandler,
            CreateUserHandler createUserHandler,
            DeleteUserHandler deleteUserHandler,
            AddCatToUserHandler addCatToUserHandler
    ) {
        this.getManyUsersHandler = getManyUsersHandler;

        this.getManyUsersHandler
                .setNext(createUserHandler)
                .setNext(getOneUserHandler)
                .setNext(deleteUserHandler)
                .setNext(addCatToUserHandler)
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyUsersHandler.handle(message);
    }
}
