package userMicroservice.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import org.springframework.stereotype.Component;
import userMicroservice.customExceptions.UserNotFoundException;
import userMicroservice.handlerChain.HandlerBase;
import userMicroservice.services.UserService;

@Component
public class DeleteUserHandler extends HandlerBase {

    private final UserService userService;

    public DeleteUserHandler(
            UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equalsIgnoreCase("delete")) {
            try {

                userService.deleteById(Long.valueOf(message.getHeaders().get("Id").toString()));

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");

                return response;
            } catch (UserNotFoundException e) {
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