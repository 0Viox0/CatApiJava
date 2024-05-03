package Viox.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.CatNotFoundException;
import Viox.handlerChain.HandlerBase;
import Viox.services.CatService;
import org.springframework.stereotype.Component;

@Component
public class DeleteCatHandler extends HandlerBase {

    private final CatService catService;

    public DeleteCatHandler(CatService catService) {
        this.catService = catService;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("delete")) {

            try {
                catService.removeCat(Long.valueOf(message.getHeaders().get("Id").toString()));

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");

                return response;
            } catch (CatNotFoundException e) {
                return MessageModelFactory.getExceptionMessage(
                        "CatNotFoundException",
                        e.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}