package com.example.event_processing.util;

import com.example.event_processing.model.Event;
import com.example.event_processing.model.dto.EventDTO;

public class EventMapper {

    public static Event toEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setPayload(eventDTO.getPayload());
        event.setType(eventDTO.getType());
        event.setTimestamp(eventDTO.getTimestamp());
        return event;

    }
}
