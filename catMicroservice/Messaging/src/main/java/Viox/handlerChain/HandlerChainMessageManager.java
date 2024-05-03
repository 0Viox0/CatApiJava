package Viox.handlerChain;

import MessagingEntities.MessageModel;
import Viox.handlerChain.handlers.GetOneCatHandler;
import Viox.handlerChain.handlers.GetManyCatsHandler;
import org.springframework.stereotype.Component;

@Component
public class HandlerChainMessageManager {

    private final GetOneCatHandler getOneCatHandler;
    private final GetManyCatsHandler getManyCatsHandler;

    public HandlerChainMessageManager(
            GetOneCatHandler getOneCatHandler,
            GetManyCatsHandler getManyCatsHandler
    ) {
        this.getOneCatHandler = getOneCatHandler;
        this.getManyCatsHandler = getManyCatsHandler;

        this.getManyCatsHandler.setNext(this.getOneCatHandler);
        getOneCatHandler.setNext(null);
    }

    public MessageModel handleMessage(MessageModel message) {
        return getManyCatsHandler.handle(message);
    }
}
