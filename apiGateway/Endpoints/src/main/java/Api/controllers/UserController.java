package Api.controllers;

import Api.exceptionHandlers.MessageExceptionHandler;
import Api.messagingMappers.MessagingMapper;
import Api.models.user.UserCreationResource;
import Api.models.user.UserIdResource;
import Api.senders.UserSender;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserCreationMessage;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSender userSender;
    private final MessageExceptionHandler messageExceptionHandler;
    private final MessagingMapper messagingMapper;

    public UserController(
            UserSender userSender,
            MessageExceptionHandler messageExceptionHandler,
            MessagingMapper messagingMapper
    ) {
        this.userSender = userSender;
        this.messageExceptionHandler = messageExceptionHandler;
        this.messagingMapper = messagingMapper;
    }


    @GetMapping
//    @PostFilter("hasRole('ADMIN') or (filterObject.id == principal.id and hasRole('USER'))")
    public Object getAllUsers() {
        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/users");
        message.setOperation("getAll");

        MessageModel response = userSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("Users");
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public Object getUserById(@PathVariable("id") Long id) {
        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/users/{id}");
        message.setOperation("getOne");
        message.setHeaders(new HashMap<>() {{
            put("Id", id);
        }});

        MessageModel response = userSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("User");
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/users/{id}");
        message.setOperation("delete");
        message.setHeaders(new HashMap<>() {{
            put("Id", id);
        }});

        MessageModel response = userSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);
    }

    @PostMapping
    public void createUser(@RequestBody UserCreationResource userCreationResource) {
        MessageModel message = MessageModelFactory.getRegularMessage();

        UserCreationMessage userCreationMessage =
                messagingMapper.toUserCreationMessage(userCreationResource);

        message.setEndpoint("/users");
        message.setOperation("create");
        message.setPayload(new HashMap<>() {{
            put("User", messagingMapper.toJson(userCreationMessage));
        }});

        MessageModel response = userSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);
    }

//    @PutMapping("{userId}/cat/{catId}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
//    public UserResource assignCatToUser(
//            @PathVariable("userId") Long userId,
//            @PathVariable("catId") Long catId
//    ) {
//        return userResourceMapper.toUserResource(userService.addCat(userId, catId));
//    }

//    @DeleteMapping("{userId}/cat/{catId}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
//    public UserResource deleteCatFromUser(
//            @PathVariable("userId") Long userId,
//            @PathVariable("catId") Long catId
//    ) {
//        return userResourceMapper.toUserResource(userService.deleteCat(userId, catId));
//    }
}