package com.springboot.catdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    // 自己自定义了一个RedisTemplate
    // <bean =id="myRedisTemplate" class="RedisTemplate">
    @Bean(value = "myRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        // 为了开发方便，一般使用<String,Object>
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        //使用Jackson3JonRedisSerializer来序列化redis的value值(默认使用JDk序列化方式)
        GenericJackson2JsonRedisSerializer jacksonSeial=new GenericJackson2JsonRedisSerializer();
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field，get，和set以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常，
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator());

//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置具体的序列化方式
        // String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // value的序列化采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jacksonSeial);

        template.afterPropertiesSet();
        return template;
    }
}
