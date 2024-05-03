package userMicroservice.handlerChain;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserIdMessageRes;
import userMicroservice.dtos.UserIdDto;
import userMicroservice.messagingMappers.MessagingMapper;
import userMicroservice.services.UserService;

import java.util.HashMap;
import java.util.List;

public class GetManyUsersHandler extends HandlerBase{

    private final UserService userService;
    private final MessagingMapper messagingMapper;

    public GetManyUsersHandler(
            UserService userService,
            MessagingMapper messagingMapper
    ) {
        this.userService = userService;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("getAll")) {

            List<UserIdDto> userIdDtos = userService.getAllUsers();

            List<UserIdMessageRes> usersMessaged = userIdDtos
                    .stream()
                    .map(messagingMapper::toUserIdMessage)
                    .toList();

            MessageModel response = MessageModelFactory.getRegularMessage();

            response.setOperation("return");
            response.setPayload(new HashMap<>() {{
                put("Users", usersMessaged);
            }});

            return response;
        } else {
            return super.handle(message);
        }
    }
}