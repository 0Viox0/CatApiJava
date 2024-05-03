package userMicroservice.handlerChain;

import MessagingEntities.MessageModel;
import org.springframework.stereotype.Component;
import userMicroservice.handlerChain.handlers.CreateUserHandler;
import userMicroservice.handlerChain.handlers.DeleteUserHandler;
import userMicroservice.handlerChain.handlers.GetManyUsersHandler;
import userMicroservice.handlerChain.handlers.GetOneUserHandler;

@Component
public class HandlerChainMessageManager {

    private final GetManyUsersHandler getManyUsersHandler;

    public HandlerChainMessageManager(
            GetManyUsersHandler getManyUsersHandler,
            GetOneUserHandler getOneUserHandler,
            CreateUserHandler createUserHandler,
            DeleteUserHandler deleteUserHandler
    ) {
        this.getManyUsersHandler = getManyUsersHandler;

        this.getManyUsersHandler
                .setNext(createUserHandler)
                .setNext(getOneUserHandler)
                .setNext(deleteUserHandler)
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyUsersHandler.handle(message);
    }
}
