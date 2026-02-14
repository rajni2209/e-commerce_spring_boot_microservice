package com.ecommerce.cartservice.config;

import com.ecommerce.cartservice.model.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Cart> cartRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Cart> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key as String
        template.setKeySerializer(new StringRedisSerializer());

        // Value as Cart (typed JSON)
        Jackson2JsonRedisSerializer<Cart> cartSerializer =
                new Jackson2JsonRedisSerializer<>(Cart.class);

        template.setValueSerializer(cartSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
