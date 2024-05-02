package Viox.consumer;

import Viox.dtos.CatIdDto;
import Viox.models.CatColor;
import Viox.services.CatService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatRabbitMqConsumer {

    private final CatService catService;

    public CatRabbitMqConsumer(CatService catService) {
        this.catService = catService;
    }

    @RabbitListener(queues = { "cat-management-queue" })
    public CatIdDto consumeMessage(Message message) {

        switch (message.getMessageProperties().getHeader("action").toString()) {
            case "GET":
                // return catService.getAllCats(null, null);
                // System.out.println(catService.getAllCats(null, null));
                // return catService.getAllCats(null, null);
                System.out.println(catService.getAllCats(null, null));
                // return new ArrayList<>();
                return new CatIdDto(
                        1L,
                        "bruh",
                        CatColor.BLACK,
                        "red skuun",
                        LocalDate.now(),
                        2L);
        }

        return null;
    }
}
