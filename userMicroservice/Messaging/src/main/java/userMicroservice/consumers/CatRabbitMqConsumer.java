package userMicroservice.consumers;

import MessagingEntities.MessageModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import userMicroservice.handlerChain.HandlerChainMessageManager;

@Service
public class CatRabbitMqConsumer {

    private final HandlerChainMessageManager handlerChainMessageManager;

    public CatRabbitMqConsumer(
            HandlerChainMessageManager handlerChainMessageManager
    ) {
        this.handlerChainMessageManager = handlerChainMessageManager;
    }

    @RabbitListener(queues = { "user-management-queue" })
    public MessageModel consumeMessage(MessageModel message) {
        return handlerChainMessageManager.handleMessage(message);
    }
}