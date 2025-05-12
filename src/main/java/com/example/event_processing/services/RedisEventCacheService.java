package com.example.event_processing.services;

import com.example.event_processing.model.Event;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RedisEventCacheService {

    private static final String KEY_PREFIX = "event:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisEventCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheEvent(Event event) {
        String key = getkey(event.getId());
        redisTemplate.opsForValue().set(key, event,TTL);
    }

    public Optional<Event> getCachedEvent(int eventId) {
        String key  = getkey(eventId);
      Event event = (Event) redisTemplate.opsForValue().get(key);
      return Optional.ofNullable(event);
    }

    public String getkey(int eventId) {
        return KEY_PREFIX + eventId;
    }
}
