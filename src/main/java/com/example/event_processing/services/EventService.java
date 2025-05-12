package com.example.event_processing.services;

import com.example.event_processing.model.Event;
import com.example.event_processing.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {


    public final EventRepository eventRepository;
    public final MessagePublisher messagePublisher;
    public RedisEventCacheService redisEventCacheService;


    public EventService(EventRepository eventRepository, MessagePublisher messagePublisher, RedisEventCacheService redisEventCacheService) {
        this.eventRepository = eventRepository;
        this.messagePublisher = messagePublisher;
        this.redisEventCacheService = redisEventCacheService;
    }

    public Event saveAndPublish(Event event) {
         Event eventSaved  =eventRepository.save(event);
         messagePublisher.publishEvent(eventSaved);
         return eventSaved;

    }

    public Optional<Event> getEventById(int id) {
    return redisEventCacheService.getCachedEvent(id)
            .or(() -> eventRepository.findById(id));
    }
}
