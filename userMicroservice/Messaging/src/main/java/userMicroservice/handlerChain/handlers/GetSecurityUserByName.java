package userMicroservice.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserSecurityDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import userMicroservice.customExceptions.UserNotFoundException;
import userMicroservice.dtos.UserSecurityDto;
import userMicroservice.handlerChain.HandlerBase;
import userMicroservice.messagingMappers.MessagingMapper;
import userMicroservice.services.UserService;

import java.util.HashMap;

@Component
public class GetSecurityUserByName extends HandlerBase {

    private final UserService userService;
    private final MessagingMapper messagingMapper;
    private final ObjectMapper objectMapper;

    public GetSecurityUserByName(
            UserService userService,
            MessagingMapper messagingMapper, @Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.userService = userService;
        this.messagingMapper = messagingMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equalsIgnoreCase("getSecurityUserByName")) {
            try {

                UserSecurityDto userSecurityDto = userService.getUserByName(
                        message.getHeaders().get("Username").toString()
                );

                UserSecurityDetails userSecurityDetails =
                        messagingMapper.toUserSecurityDetails(userSecurityDto);

                String jsonUserSecurityDetails = objectMapper.writeValueAsString(userSecurityDetails);

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");
                response.setPayload(new HashMap<>() {{
                    put("User", jsonUserSecurityDetails);
                }});

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
