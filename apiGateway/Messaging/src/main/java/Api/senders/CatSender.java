package Api.senders;

import MessagingEntities.CatIdMessageRes;
import MessagingEntities.CatInfoMessage;
import jakarta.annotation.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatSender {

    private final RabbitTemplate rabbitTemplate;

    public CatSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<CatIdMessageRes> sendMessage(
            @Nullable String color,
            @Nullable String breed) {

        CatInfoMessage catInfoMessage = new CatInfoMessage(color, breed);

        List<CatIdMessageRes> response = (List<CatIdMessageRes>)rabbitTemplate
                .convertSendAndReceive(
                        "api-request-exchange",
                        "cat",
                        catInfoMessage);

        return response;
    }
}
