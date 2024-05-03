package Api.senders;

import MessagingEntities.MessageModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserSender {

    private final RabbitTemplate rabbitTemplate;

    public UserSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public MessageModel sendMessage(MessageModel message) {

        return (MessageModel)rabbitTemplate
                .convertSendAndReceive(
                        "api-request-exchange",
                        "user",
                        message);
    }
}
