package Api.controllers;

import Api.exceptionHandlers.MessageExceptionHandler;
import Api.messagingMappers.MessagingMapper;
import Api.models.cat.CatCreationResource;
import MessagingEntities.MessageModel;
import MessagingEntities.cat.CatCreationMessage;
import MessagingEntities.factories.MessageModelFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import Api.senders.*;

import java.util.*;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatSender catSender;
    private final MessageExceptionHandler messageExceptionHandler;
    private final MessagingMapper messagingMapper;

    public CatController(
            CatSender catSender,
            MessageExceptionHandler messageExceptionHandler,
            MessagingMapper messagingMapper
    ) {
        this.catSender = catSender;
        this.messageExceptionHandler = messageExceptionHandler;
        this.messagingMapper = messagingMapper;
    }

    @GetMapping
    public Object getAllCats(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed) {

        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/cats");
        message.setOperation("getAll");
        message.setHeaders(new HashMap<>() {{
            put("Color", color);
            put("Breed", breed);
        }});

        MessageModel response = catSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("Cats");
    }

    @GetMapping("/{id}")
    public Object getCatById(@PathVariable("id") Long id) {

        MessageModel message = MessageModelFactory.getRegularMessage();

        message.setEndpoint("/cats/id" + id);
        message.setOperation("getOne");
        message.setHeaders(new HashMap<>() {{
            put("Id", id);
        }});

        MessageModel response = catSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("Cat");
    }

    @PostMapping
    public void createCat(@RequestBody CatCreationResource catCreationResource) {

        MessageModel message = MessageModelFactory.getRegularMessage();

        CatCreationMessage catCreationMessage = messagingMapper.toCatCreationMessage(catCreationResource);

        message.setEndpoint("/cats");
        message.setOperation("create");
        message.setPayload(new HashMap<>() {{
            put("Cat", messagingMapper.toJson(catCreationMessage));
        }});

        MessageModel response = catSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);
    }

    // @DeleteMapping("/{id}")
    // public void deleteCatById(@PathVariable("id") Long id) {
    // catService.removeCat(id);
    // }
    //
    // @PutMapping("/{catId}/friendsWithCat/{friendId}")
    // public CatResource makeFriends(
    // @PathVariable("catId") Long catId,
    // @PathVariable("friendId") Long friendId) {
    // return catResourceMapper.toCatResource(catService.befriendCats(catId,
    // friendId));
    // }
    //
    // @DeleteMapping("/{catId}/friendsWithCat/{friendId}")
    // public CatResource deleteFriends(
    // @PathVariable("catId") Long catId,
    // @PathVariable("friendId") Long friendId) {
    // return catResourceMapper.toCatResource(catService.unfriendCats(catId,
    // friendId));
    // }
}
