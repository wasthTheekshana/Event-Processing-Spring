package com.example.event_processing.controller;

import com.example.event_processing.model.Event;
import com.example.event_processing.services.EventService;
import com.example.event_processing.util.EventMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        return eventService.getEventById(id)
                .map(event -> ResponseEntity.ok(EventMapper.toEntity(event)))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
}
