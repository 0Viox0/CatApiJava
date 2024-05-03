package Viox.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.CatNotFoundException;
import Viox.dtos.CatResponseDto;
import Viox.handlerChain.HandlerBase;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetOneCatHandler extends HandlerBase {

    private final CatService catService;
    private final MessagingMapper messagingMapper;

    public GetOneCatHandler(
            CatService catService,
            MessagingMapper messagingMapper
    ) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {

        if (message.getOperation().equals("getOne")) {
            try {
                CatResponseDto catResponseDto = catService
                        .getCatById(Long.valueOf(message.getHeaders().get("Id").toString()));

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");
                response.setPayload(Map.of("Cat", messagingMapper.toCatMessage(catResponseDto)));

                return response;
            } catch (CatNotFoundException ex) {

                return MessageModelFactory.getExceptionMessage(
                        "CatNotFoundException",
                        ex.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}