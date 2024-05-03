package Viox.handlerChain.handlers;

import MessagingEntities.MessageModel;
import MessagingEntities.cat.CatMessageRes;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.CatNotFoundException;
import Viox.dtos.CatResponseDto;
import Viox.handlerChain.HandlerBase;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UnfriendCatsHandler extends HandlerBase {

    private final CatService catService;
    private final MessagingMapper messagingMapper;

    public UnfriendCatsHandler(
            CatService catService,
            MessagingMapper messagingMapper
    ) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
    }

    @Override
    public MessageModel handle(MessageModel message) {
        if (message.getOperation().equals("unfriend")) {
            try {
                CatResponseDto catResponseDto = catService.unfriendCats(
                        Long.valueOf(message.getHeaders().get("CatId").toString()),
                        Long.valueOf(message.getHeaders().get("FriendId").toString())
                );

                CatMessageRes catMessageRes = messagingMapper.toCatMessage(catResponseDto);

                MessageModel response = MessageModelFactory.getRegularMessage();

                response.setOperation("return");
                response.setPayload(new HashMap<>() {{
                    put("Cat", catMessageRes);
                }});

                return response;
            }
            catch (CatNotFoundException e) {
                return MessageModelFactory.getExceptionMessage(
                        "CatNotFoundException",
                        e.getMessage()
                );
            }
        } else {
            return super.handle(message);
        }
    }
}