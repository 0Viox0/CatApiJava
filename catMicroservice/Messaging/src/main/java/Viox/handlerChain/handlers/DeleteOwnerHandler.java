package Viox.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.CatNotFoundException;
import Viox.handlerChain.HandlerBase;
import Viox.services.CatService;
import org.springframework.stereotype.Component;

@Component
public class DeleteOwnerHandler extends HandlerBase {

    private final CatService catService;

    public DeleteOwnerHandler(
            CatService catService
    ) {
        this.catService = catService;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("deleteOwner")) {

            try {
                catService.removeOwner(
                        Long.valueOf(message.getHeaders().get("CatId").toString())
                );

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
