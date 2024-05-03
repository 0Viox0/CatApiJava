package userMicroservice.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserCreationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import userMicroservice.handlerChain.HandlerBase;
import userMicroservice.messagingMappers.MessagingMapper;
import userMicroservice.services.UserService;

@Component
public class CreateUserHandler extends HandlerBase {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final MessagingMapper messagingMapper;

    public CreateUserHandler(
            UserService userService,
            ObjectMapper objectMapper,
            MessagingMapper messagingMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("create")) {
            try {

                UserCreationMessage userCreationMessage = objectMapper.readValue(
                        message.getPayload().get("User").toString(),
                        UserCreationMessage.class
                );

                userService.addUser(
                        messagingMapper.toUserCreationDto(userCreationMessage)
                );

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");

                return response;
            } catch (JsonProcessingException e) {
                return MessageModelFactory.getExceptionMessage(
                        "JsonProcessingException",
                        e.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}