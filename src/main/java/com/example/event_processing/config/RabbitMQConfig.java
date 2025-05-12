package com.example.event_processing.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "event_exchange";
    public static final String QUEUE_NAME = "event_queue";
    public static final String ROUTING_KEY = "event_routing_key";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }


}
