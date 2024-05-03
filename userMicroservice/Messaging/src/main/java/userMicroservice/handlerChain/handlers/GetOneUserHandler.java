package userMicroservice.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import org.springframework.stereotype.Component;
import userMicroservice.customExceptions.UserNotFoundException;
import userMicroservice.dtos.UserResponseDto;
import userMicroservice.handlerChain.HandlerBase;
import userMicroservice.messagingMappers.MessagingMapper;
import userMicroservice.services.UserService;

import java.util.Map;

@Component
public class GetOneUserHandler extends HandlerBase {

    private final UserService userService;
    private final MessagingMapper messagingMapper;

    public GetOneUserHandler(
            UserService userService,
            MessagingMapper messagingMapper
    ) {
        this.userService = userService;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("getOne")) {
            try {
                UserResponseDto userResponseDto = userService
                        .getUserById(Long.valueOf(message.getHeaders().get("Id").toString()));

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");
                response.setPayload(Map.of("User", messagingMapper.toUserMessage(userResponseDto)));

                return response;
            } catch (UserNotFoundException e) {
                return MessageModelFactory.getExceptionMessage(
                        "UserNotFoundException",
                        e.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}
