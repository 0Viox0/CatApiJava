package Viox.consumers;

import Viox.dtos.CatIdDto;
import Viox.services.CatService;

import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CatRabbitMqConsumer {

    private final CatService catService;

    public CatRabbitMqConsumer(CatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = { "cat-management-queue" })
    public void consumeMessage(ConsumeClass someRandomString) {

        System.out.println(someRandomString);

        // switch (message.getMessageProperties().getHeader("action").toString()) {
        // case "GET":

        // List<CatIdDto> result = catService.getAllCats(null, null);
        // System.out.println(result);

        // break;
        // }
    }
}
