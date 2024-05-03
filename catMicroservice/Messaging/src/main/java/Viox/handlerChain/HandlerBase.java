package Viox.handlerChain;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;

public abstract class HandlerBase {
    private HandlerBase nextHandler;

    public HandlerBase setNext(HandlerBase handler) {
        nextHandler = handler;

        return handler;
    }

    public MessageModel handle(MessageModel message) {
        if (nextHandler != null) {
            return nextHandler.handle(message);
        } else {
            MessageModel defaultResponse = MessageModelFactory.getRegularMessage();

            defaultResponse.setOperation("default return");

            return defaultResponse;
        }
    }
}