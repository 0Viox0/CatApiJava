package Viox.consumers;

import MessagingEntities.CatIdMessageRes;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import Viox.customExceptions.CatNotFoundException;
import Viox.customExceptions.InvalidCatColorException;
import Viox.dtos.CatIdDto;
import Viox.dtos.CatResponseDto;
import Viox.messagingMappers.MessagingMapper;
import Viox.services.CatService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CatRabbitMqConsumer {

    private final CatService catService;
    private final MessagingMapper messagingMapper;

    public CatRabbitMqConsumer(
            CatService catService,
            MessagingMapper messagingMapper
    ) {
        this.catService = catService;
        this.messagingMapper = messagingMapper;
    }

    @RabbitListener(queues = { "cat-management-queue" })
    public MessageModel consumeMessage(MessageModel message) {

        if (message.getOperation().equals("getAll")) {

            try {
                String color = message.getHeaders().get("Color") != null ? message.getHeaders().toString() : null;
                String breed = message.getHeaders().get("Breed") != null ? message.getHeaders().toString() : null;

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

        } else if (message.getOperation().equals("getOne")) {

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
        }

        MessageModel defaultResponse = MessageModelFactory.getRegularMessage();

        defaultResponse.setOperation("default return");

        return defaultResponse;
    }
}