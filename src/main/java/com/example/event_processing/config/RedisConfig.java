package com.example.event_processing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String,Object> restTemplate = new RedisTemplate<>();
        restTemplate.setConnectionFactory(factory);

        restTemplate.setKeySerializer(new StringRedisSerializer());
        restTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return restTemplate;
    }
}
