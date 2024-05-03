package Viox.handlerChain;

import MessagingEntities.MessageModel;
import Viox.handlerChain.handlers.CreateCatHandler;
import Viox.handlerChain.handlers.GetOneCatHandler;
import Viox.handlerChain.handlers.GetManyCatsHandler;
import org.springframework.stereotype.Component;

@Component
public class HandlerChainMessageManager {

    private final GetManyCatsHandler getManyCatsHandler;

    public HandlerChainMessageManager(
            GetManyCatsHandler getManyCatsHandler,
            GetOneCatHandler getOneCatHandler,
            CreateCatHandler createCatHandler
    ) {
        this.getManyCatsHandler = getManyCatsHandler;

        this.getManyCatsHandler
                .setNext(getOneCatHandler)
                .setNext(createCatHandler)
                .setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyCatsHandler.handle(message);
    }
}
