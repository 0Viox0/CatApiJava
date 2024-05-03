package Viox.handlerChain;

import MessagingEntities.MessageModel;
import Viox.handlerChain.handlers.CreateCatHandler;
import Viox.handlerChain.handlers.DeleteCatHandler;
import Viox.handlerChain.handlers.GetOneCatHandler;
import Viox.handlerChain.handlers.GetManyCatsHandler;
import org.springframework.stereotype.Component;

@Component
public class HandlerChainMessageManager {

    private final GetManyCatsHandler getManyCatsHandler;

    public HandlerChainMessageManager(
            GetManyCatsHandler getManyCatsHandler,
            GetOneCatHandler getOneCatHandler,
            CreateCatHandler createCatHandler,
            DeleteCatHandler deleteCatHandler
    ) {
        this.getManyCatsHandler = getManyCatsHandler;

        this.getManyCatsHandler
                .setNext(getOneCatHandler)
                .setNext(createCatHandler)
                .setNext(deleteCatHandler)
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyCatsHandler.handle(message);
    }
}
