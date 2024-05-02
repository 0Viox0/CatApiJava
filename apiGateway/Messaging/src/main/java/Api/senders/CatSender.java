package Api.senders;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CatSender {

    private final RabbitTemplate rabbitTemplate;

    public CatSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Long id, String name) {

        ConsumeClass consumeClass = new ConsumeClass();

        consumeClass.setId(id);
        consumeClass.setName(name);

        rabbitTemplate.convertAndSend("api-request-exchange", "cat", consumeClass);
    }
}
