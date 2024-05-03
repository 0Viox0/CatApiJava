package Viox.handlerChain.handlers;

import MessagingEntities.CatIdMessageRes;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.InvalidCatColorException;
import Viox.dtos.CatIdDto;
import Viox.handlerChain.HandlerBase;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetManyCatsHandler extends HandlerBase {

    private final CatService catService;
    private final MessagingMapper messagingMapper;

    public GetManyCatsHandler(
            CatService catService,
            MessagingMapper messagingMapper
    ) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {

        if (message.getOperation().equals("getAll")) {
            try {
                String color = message.getHeaders().get("Color") != null
                        ? message.getHeaders().get("Color").toString() : null;
                String breed = message.getHeaders().get("Breed") != null
                        ? message.getHeaders().get("Breed").toString() : null;

                List<CatIdDto> cats = catService.getAllCats(color, breed);

                List<CatIdMessageRes> catsMessaged = cats
                        .stream()
                        .map(messagingMapper::toCatIdMessage)
                        .toList();

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");
                response.setPayload(Map.of("Cats", catsMessaged));

                return response;
            } catch (InvalidCatColorException ex) {

                return MessageModelFactory.getExceptionMessage(
                        "InvalidCatColorException",
                        ex.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}