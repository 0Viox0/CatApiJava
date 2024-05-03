package userMicroservice.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserMessageRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final ObjectMapper objectMapper;

    public GetOneUserHandler(
            UserService userService,
            MessagingMapper messagingMapper,
            @Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.userService = userService;
        this.messagingMapper = messagingMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("getOne")) {
            try {
                UserResponseDto userResponseDto = userService
                        .getUserById(Long.valueOf(message.getHeaders().get("Id").toString()));

                MessageModel response = MessageModelFactory.getRegularMessage();

                UserMessageRes userMessageRes = messagingMapper.toUserMessage(userResponseDto);

                String jsonUserMessageRes = objectMapper.writeValueAsString(userMessageRes);

                response.setOperation("return");
                response.setPayload(Map.of("User", jsonUserMessageRes));

                return response;
            } catch (UserNotFoundException e) {
                return MessageModelFactory.getExceptionMessage(
                        "UserNotFoundException",
                        e.getMessage()
                );
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
