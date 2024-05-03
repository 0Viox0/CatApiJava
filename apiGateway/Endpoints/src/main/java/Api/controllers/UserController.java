package Api.controllers;

import Api.exceptionHandlers.MessageExceptionHandler;
import Api.senders.UserSender;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSender userSender;
    private final MessageExceptionHandler messageExceptionHandler;

    public UserController(
            UserSender userSender,
            MessageExceptionHandler messageExceptionHandler
    ) {
        this.userSender = userSender;
        this.messageExceptionHandler = messageExceptionHandler;
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

//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
//    public UserResource getUserById(@PathVariable("id") Long id) {
//        return userResourceMapper.toUserResource(userService.getUserById(id));
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUserById(@PathVariable("id") Long id) {
//        userService.deleteById(id);
//    }
//
//    @PostMapping
//    public UserIdResource createUser(@RequestBody UserCreationResource userCreationResource) {
//        UserCreationDto userCreationDto = userResourceMapper.toUserCreationDto(userCreationResource);
//
//        return userResourceMapper.toUserIdResource(userService.addUser(userCreationDto));
//    }
//
//    @PutMapping("{userId}/cat/{catId}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
//    public UserResource assignCatToUser(
//            @PathVariable("userId") Long userId,
//            @PathVariable("catId") Long catId
//    ) {
//        return userResourceMapper.toUserResource(userService.addCat(userId, catId));
//    }
//
//    @DeleteMapping("{userId}/cat/{catId}")
//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
//    public UserResource deleteCatFromUser(
//            @PathVariable("userId") Long userId,
//            @PathVariable("catId") Long catId
//    ) {
//        return userResourceMapper.toUserResource(userService.deleteCat(userId, catId));
//    }
}