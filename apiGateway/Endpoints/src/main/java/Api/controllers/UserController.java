package Api.controllers;

import Api.actions.ApiActions;
import Api.exceptionHandlers.MessageExceptionHandler;
import Api.messagingMappers.MessagingMapper;
import Api.models.user.UserCreationResource;
import Api.models.user.UserResource;
import Api.senders.CatSender;
import Api.senders.UserSender;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserCreationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSender userSender;
    private final MessageExceptionHandler messageExceptionHandler;
    private final MessagingMapper messagingMapper;
    private final CatSender catSender;
    private final ObjectMapper objectMapper;
    private final ApiActions apiActions;

    public UserController(
            UserSender userSender,
            MessageExceptionHandler messageExceptionHandler,
            MessagingMapper messagingMapper,
            CatSender catSender,
            @Qualifier("objectMapper") ObjectMapper objectMapper, ApiActions apiActions) {
        this.userSender = userSender;
        this.messageExceptionHandler = messageExceptionHandler;
        this.messagingMapper = messagingMapper;
        this.catSender = catSender;
        this.objectMapper = objectMapper;
        this.apiActions = apiActions;
    }


    @GetMapping
    public Object getAllUsers() {
        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/users");
        message.setOperation("getAll");

        MessageModel response = userSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("Users");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public UserResource getUserById(@PathVariable("id") Long id) {
        MessageModel userMessage = MessageModelFactory.getRegularMessage();

        userMessage.setEndpoint("/users/{id}");
        userMessage.setOperation("getOne");
        userMessage.setHeaders(new HashMap<>() {{
            put("Id", id);
        }});

        MessageModel userResponse = userSender.sendMessage(userMessage);

        messageExceptionHandler.checkMessageForExceptions(userResponse);

        // second message to get all cats
        MessageModel catMessage = MessageModelFactory.getRegularMessage();

        String color = null;
        String breed = null;

        catMessage.setEndpoint("/cats");
        catMessage.setOperation("getAll");
        catMessage.setHeaders(new HashMap<>() {{
            put("Color", color);
            put("Breed", breed);
        }});

        MessageModel catResponse = catSender.sendMessage(catMessage);
        messageExceptionHandler.checkMessageForExceptions(catResponse);

        return apiActions.getUserResourceFromResponses(userResponse, catResponse);
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

    @PutMapping("{userId}/cat/{catId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
    public UserResource assignCatToUser(
            @PathVariable("userId") Long userId,
            @PathVariable("catId") Long catId
    ) {
        // sending message to change owned by user cats
        MessageModel userMessage = MessageModelFactory.getRegularMessage();

        userMessage.setEndpoint("/users/{userId}/cats/{catId}");
        userMessage.setOperation("addCat");
        userMessage.setHeaders(new HashMap<>() {{
            put("UserId", userId);
            put("CatId", catId);
        }});

        MessageModel userResponse = userSender.sendMessage(userMessage);

        messageExceptionHandler.checkMessageForExceptions(userResponse);

        // sending message to change cats owner id
        MessageModel catAddOwnerMessage = MessageModelFactory.getRegularMessage();

        catAddOwnerMessage.setEndpoint("/cats/{catId}");
        catAddOwnerMessage.setOperation("addOwner");
        catAddOwnerMessage.setHeaders(new HashMap<>() {{
            put("UserId", userId);
            put("CatId", catId);
        }});

        MessageModel catAddOwnerResponse = catSender.sendMessage(catAddOwnerMessage);

        messageExceptionHandler.checkMessageForExceptions(catAddOwnerResponse);

        // sending message to ge all cats to properly get UserResource
        MessageModel catMessage = MessageModelFactory.getRegularMessage();

        String color = null;
        String breed = null;

        catMessage.setEndpoint("/cats");
        catMessage.setOperation("getAll");
        catMessage.setHeaders(new HashMap<>() {{
            put("Color", color);
            put("Breed", breed);
        }});

        MessageModel catResponse = catSender.sendMessage(catMessage);
        messageExceptionHandler.checkMessageForExceptions(catResponse);

        return apiActions.getUserResourceFromResponses(userResponse, catResponse);
    }

    @DeleteMapping("{userId}/cat/{catId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
    public UserResource deleteCatFromUser(
            @PathVariable("userId") Long userId,
            @PathVariable("catId") Long catId
    ) {
        // sending message to change owned by user cats
        MessageModel userMessage = MessageModelFactory.getRegularMessage();

        userMessage.setEndpoint("/users/{userId}/cats/{catId}");
        userMessage.setOperation("deleteCat");
        userMessage.setHeaders(new HashMap<>() {{
            put("UserId", userId);
            put("CatId", catId);
        }});

        MessageModel userResponse = userSender.sendMessage(userMessage);

        messageExceptionHandler.checkMessageForExceptions(userResponse);

        // sending message to change cats owner id
        MessageModel catAddOwnerMessage = MessageModelFactory.getRegularMessage();

        catAddOwnerMessage.setEndpoint("/cats/{catId}");
        catAddOwnerMessage.setOperation("deleteOwner");
        catAddOwnerMessage.setHeaders(new HashMap<>() {{
            put("UserId", userId);
            put("CatId", catId);
        }});

        MessageModel catAddOwnerResponse = catSender.sendMessage(catAddOwnerMessage);

        messageExceptionHandler.checkMessageForExceptions(catAddOwnerResponse);

        // sending message to ge all cats to properly get UserResource
        MessageModel catMessage = MessageModelFactory.getRegularMessage();

        String color = null;
        String breed = null;

        catMessage.setEndpoint("/cats");
        catMessage.setOperation("getAll");
        catMessage.setHeaders(new HashMap<>() {{
            put("Color", color);
            put("Breed", breed);
        }});

        MessageModel catResponse = catSender.sendMessage(catMessage);
        messageExceptionHandler.checkMessageForExceptions(catResponse);

        return apiActions.getUserResourceFromResponses(userResponse, catResponse);
    }
}