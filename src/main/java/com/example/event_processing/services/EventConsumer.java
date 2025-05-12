package com.example.event_processing.services;

import com.example.event_processing.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {


       private static final Logger logger =  LoggerFactory.getLogger(EventConsumer.class);
       private final RedisEventCacheService redisEventCacheService;

       public EventConsumer(RedisEventCacheService redisEventCacheService) {
              this.redisEventCacheService = redisEventCacheService;
       }

       /**
        *  Listens for message from RabbitMq and process them
        *
        * @param event
        */


       @RabbitListener(queues = "#{T(com.example.event_processing.config.RabbitMQConfig).QUEUE_NAME}")
       public void handleEvent(Event event) {

              try {
                     logger.info("Received event: " + event);
                     simulationProcessing(event);
                     redisEventCacheService.cacheEvent(event);
              } catch (Exception e) {
                     logger.error("Error while handling event: " + event.getId(),e);
              }
       }

       private void simulationProcessing(Event event) {
              logger.info("Simulation processing event: " + event.getType(),event.getPayload());
       }
}
