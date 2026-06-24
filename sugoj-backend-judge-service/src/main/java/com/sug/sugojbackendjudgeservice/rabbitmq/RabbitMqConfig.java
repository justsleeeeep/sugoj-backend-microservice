package com.sug.sugojbackendjudgeservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "code_exchange";
    public static final String QUEUE_NAME = "code_queue";
    public static final String ROUTING_KEY = "my_routingKey";

    // 1. 定义交换机
    @Bean
    public DirectExchange codeExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 2. 定义队列
    @Bean
    public Queue codeQueue() {
        return new Queue(QUEUE_NAME, true); // true 表示持久化
    }

    // 3. 定义绑定关系
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(codeQueue()).to(codeExchange()).with(ROUTING_KEY);
    }
}