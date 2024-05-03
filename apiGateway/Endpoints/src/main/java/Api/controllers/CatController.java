package Api.controllers;

import Api.exceptionHandlers.MessageExceptionHandler;
import MessagingEntities.MessageModel;
import org.springframework.web.bind.annotation.*;
import Api.senders.*;

import java.util.*;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatSender catSender;
    private final MessageExceptionHandler throwByName;
    private final MessageExceptionHandler messageExceptionHandler;

    public CatController(CatSender catSender, MessageExceptionHandler throwByName, MessageExceptionHandler messageExceptionHandler) {
        this.catSender = catSender;
        this.throwByName = throwByName;
        this.messageExceptionHandler = messageExceptionHandler;
    }

    @GetMapping
    public Object getAllCats(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed) {

        MessageModel message = new MessageModel();

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

        MessageModel message = new MessageModel();

        message.setEndpoint("/cats/id" + id);
        message.setOperation("getOne");
        message.setHeaders(new HashMap<>() {{
            put("Id", id);
        }});

        MessageModel response = catSender.sendMessage(message);

        messageExceptionHandler.checkMessageForExceptions(response);

        return response.getPayload().get("Cat");
    }

    // @PostMapping
    // public CatIdResource createCat(@RequestBody CatCreationResource
    // catCreationResource) {
    // CatCreationDto catCreationDto =
    // catResourceMapper.toCatCreationDto(catCreationResource);
    //
    // return
    // catResourceMapper.toCatIdResource(catService.createCat(catCreationDto));
    // }
    //
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
