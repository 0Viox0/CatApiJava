package Viox.handlerChain;

import MessagingEntities.MessageModel;
import Viox.handlerChain.handlers.*;
import org.springframework.stereotype.Component;

@Component
public class HandlerChainMessageManager {

    private final GetManyCatsHandler getManyCatsHandler;

    public HandlerChainMessageManager(
            GetManyCatsHandler getManyCatsHandler,
            GetOneCatHandler getOneCatHandler,
            CreateCatHandler createCatHandler,
            DeleteCatHandler deleteCatHandler,
            BefriendCatsHandler befriendCatsHandler,
            UnfriendCatsHandler unfriendCatsHandler
    ) {
        this.getManyCatsHandler = getManyCatsHandler;

        this.getManyCatsHandler
                .setNext(getOneCatHandler)
                .setNext(createCatHandler)
                .setNext(deleteCatHandler)
                .setNext(befriendCatsHandler)
                .setNext(unfriendCatsHandler)
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyCatsHandler.handle(message);
    }
}
