package com.example.event_processing.controller;

import com.example.event_processing.model.Event;
import com.example.event_processing.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/event")
public class EventController {

    public final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     *
     * create a new event  and publish it to rabbitMQ
     * @param event validated input payload
     * @return create event with location header
     *
     */

    @PostMapping
    public ResponseEntity<Event>createEvent(@Valid @RequestBody Event event) {

        Event events  = eventService.saveAndPublish(event);
        URI location = URI.create("/api/event/" + event.getId());
        return ResponseEntity.created(location).body(events);
    }
}
