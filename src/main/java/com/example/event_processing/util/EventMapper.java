package com.example.event_processing.util;

import com.example.event_processing.model.Event;
import jakarta.validation.constraints.NotNull;

public class EventMapper {

    public static Event toEntity(@NotNull Event eventDTO) {
        Event event = new Event();
        event.setPayload(eventDTO.getPayload());
        event.setType(eventDTO.getType());
        event.setTimestamp(eventDTO.getTimestamp());
        return event;

    }
}
