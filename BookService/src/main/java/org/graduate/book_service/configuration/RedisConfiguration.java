package org.graduate.book_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/24
 * Time: 上午11:50
 */

@Configuration
public class RedisConfiguration {
    @Bean(name = "redisTemplate")
    public RedisTemplate<Long, Object> 
	redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Long, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
    }
}
