package Viox.consumers;

import MessagingEntities.CatColorMessage;
import MessagingEntities.CatIdMessageRes;
import MessagingEntities.CatInfoMessage;
import Viox.dtos.CatIdDto;
import Viox.services.CatService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatRabbitMqConsumer {

    private final CatService catService;

    public CatRabbitMqConsumer(CatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = { "cat-management-queue" })
    public List<CatIdMessageRes> consumeMessage(CatInfoMessage catInfoMessage) {

        List<CatIdDto> cats = catService
                .getAllCats(catInfoMessage.color(), catInfoMessage.breed());

        ArrayList<CatIdMessageRes> response = new ArrayList<>();

        cats.forEach((cat) -> response.add(new CatIdMessageRes(
                cat.id(),
                cat.name(),
                CatColorMessage.fromString(cat.color().toString()),
                cat.breed(),
                cat.dateOfBirth(),
                cat.ownerId())
        ));

        return response;
    }
}