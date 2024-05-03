package Viox.handlerChain.handlers;

import MessagingEntities.cat.CatIdMessageRes;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.InvalidCatColorException;
import Viox.dtos.CatIdDto;
import Viox.handlerChain.HandlerBase;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetManyCatsHandler extends HandlerBase {

    private final CatService catService;
    private final MessagingMapper messagingMapper;
    private final ObjectMapper objectMapper;

    public GetManyCatsHandler(
            CatService catService,
            MessagingMapper messagingMapper,
            @Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
        this.objectMapper = objectMapper;
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

                String jsonCatsMessaged = objectMapper.writeValueAsString(catsMessaged);

                response.setOperation("return");
                response.setPayload(Map.of("Cats", jsonCatsMessaged));

                return response;
            } catch (InvalidCatColorException ex) {
                return MessageModelFactory.getExceptionMessage(
                        "InvalidCatColorException",
                        ex.getMessage()
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