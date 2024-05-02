package Viox.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    public Queue catQueue() {
        return new Queue("cat-management-queue");
    }

    @Bean
    public TopicExchange mainApiExchange() {
        return new TopicExchange("api-request-exchange");
    }

    @Bean
    public Binding catBinding() {
        return BindingBuilder
                .bind(catQueue())
                .to(mainApiExchange())
                .with("cat");
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

//     @Bean
//     public MessageConverter converter() {
//     return new Jackson2JsonMessageConverter();
//     }

     @Bean
     public ConnectionFactory connectionFactory() {
     CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

     connectionFactory.setHost("localhost");
     connectionFactory.setPort(5672);
     connectionFactory.setUsername("guest");
     connectionFactory.setPassword("guest");

     return connectionFactory;
     }

     @Bean
     public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
     RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

     rabbitTemplate.setMessageConverter(converter());

     return rabbitTemplate;
     }
}
