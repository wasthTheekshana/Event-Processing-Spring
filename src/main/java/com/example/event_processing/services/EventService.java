package com.example.event_processing.services;

import com.example.event_processing.model.Event;
import com.example.event_processing.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {


    public final EventRepository eventRepository;
    public final MessagePublisher messagePublisher;

    @Autowired
    public EventService(EventRepository eventRepository, MessagePublisher messagePublisher) {
        this.eventRepository = eventRepository;
        this.messagePublisher = messagePublisher;
    }

    public Event saveAndPublish(Event event) {
         Event eventSaved  =eventRepository.save(event);
         messagePublisher.publishEvent(eventSaved);
         return eventSaved;

    }
}
