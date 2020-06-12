//package com.debrief2.pulsa.mobile.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Bean
//    Queue testQueue() {
//        return new Queue("testQueue", true);
//    }
//
//    @Bean
//    DirectExchange exchange() {
//        return new DirectExchange("test-direct-exchange");
//    }
//
//    @Bean
//    Binding testBinding(Queue testQueue, DirectExchange exchange) {
//        return BindingBuilder.bind(testQueue).to(exchange).with("test");
//    }
//}
