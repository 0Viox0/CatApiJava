package Viox.consumers;

import MessagingEntities.CatColorMessage;
import MessagingEntities.CatIdMessageRes;
import MessagingEntities.CatMessageRes;
import MessagingEntities.MessageModel;
import Viox.dtos.CatIdDto;
import Viox.dtos.CatResponseDto;
import Viox.services.CatService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CatRabbitMqConsumer {

    private final CatService catService;

    public CatRabbitMqConsumer(CatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = { "cat-management-queue" })
    public MessageModel consumeMessage(MessageModel message) {

        if (message.getOperation().equals("getAll")) {

            String color = message.getHeaders().get("Color") != null ? message.getHeaders().toString() : null;
            String breed = message.getHeaders().get("Breed") != null ? message.getHeaders().toString() : null;

            List<CatIdDto> cats = catService.getAllCats(
                    color,
                    breed
            );

            ArrayList<CatIdMessageRes> catsMessaged = new ArrayList<>();

            cats.forEach((cat) -> {
                catsMessaged.add(new CatIdMessageRes(
                        cat.id(),
                        cat.name(),
                        CatColorMessage.fromString(cat.color().toString()),
                        cat.breed(),
                        cat.dateOfBirth(),
                        cat.ownerId())
                );
            });

            MessageModel response = new MessageModel();

            response.setOperation("return");
            response.setPayload(Map.of("Cats", catsMessaged));

            return response;
        } else if (message.getOperation().equals("getOne")) {

            System.out.println(Long.valueOf(message.getHeaders().get("Id").toString()));

            CatResponseDto catResponseDto = catService
                    .getCatById(Long.valueOf(message.getHeaders().get("Id").toString()));

            ArrayList<CatIdMessageRes> friends = new ArrayList<>();

            catResponseDto.friends().forEach((cat) -> {
                friends.add(new CatIdMessageRes(
                        cat.id(),
                        cat.name(),
                        CatColorMessage.fromString(cat.color().toString()),
                        cat.breed(),
                        cat.dateOfBirth(),
                        cat.ownerId())
                );
            });

            MessageModel response = new MessageModel();

            response.setOperation("return");
            response.setPayload(Map.of("Cat", new CatMessageRes(
                    catResponseDto.id(),
                    catResponseDto.name(),
                    CatColorMessage.fromString(catResponseDto.color().toString()),
                    catResponseDto.breed(),
                    catResponseDto.dateOfBirth(),
                    friends,
                    catResponseDto.ownerId()
            )));

            return response;
        }

        MessageModel defaultResponse = new MessageModel();

        defaultResponse.setOperation("default return");

        return defaultResponse;
    }
}