package Viox.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.cat.CatCreationMessage;
import MessagingEntities.factories.MessageModelFactory;
import Viox.dtos.CatCreationDto;
import Viox.handlerChain.HandlerBase;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateCatHandler extends HandlerBase {

    private final CatService catService;
    private final MessagingMapper messagingMapper;
    private final ObjectMapper objectMapper;

    public CreateCatHandler(
            CatService catService,
            MessagingMapper messagingMapper,
            ObjectMapper objectMapper
    ) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {

        if (message.getOperation().equals("create")) {

            try {
                CatCreationMessage catCreationMessage = objectMapper.readValue(
                        message.getPayload().get("Cat").toString(),
                        CatCreationMessage.class
                );

                catService.createCat(
                        messagingMapper.toCatCreationDto(catCreationMessage)
                );

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");

                return response;
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
