package com.debrief2.pulsa.mobile.utils.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class RpcPublisher {
    private ConnectionFactory connectionFactory;
    private Channel channel;
    private String queueName;
    private Connection connection;

//    private String routingKey = "";

//    @Value("${rabbitmq.exchange.namel}")
//    private String exchangeName;
//@Value("${cloudamqp.url}")

//    @Autowired
    public RpcPublisher(@Value("${cloudamqp.url}") String url) throws Exception {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUri(url);
//        connectionFactory.setHost("localhost");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setPort(5672);
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            if (channel != null && connection != null){
                channel.close();
                connection.close();
            }
        }

        //Recommended settings
//        connectionFactory.setRequestedHeartBeat(30);
//        connectionFactory.setConnectionTimeout(10000);

        //Set up queue, exchanges and bindings
//        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
//        Queue queue = new Queue("testQueue");
//        admin.declareQueue(queue);
//        DirectExchange exchange = new DirectExchange("dana-pulsa-direct-exchange");
//        admin.declareExchange(exchange);
//        admin.declareBinding(
//                BindingBuilder.bind(queue).to(exchange).with(""));
    }

    public String sendMessage(String queueName, String message) {
        System.out.println("message to send = " + message);
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = null;
        String result = null;
        try {
            replyQueueName = channel.queueDeclare().getQueue();

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            channel.basicPublish("", queueName, props, message.getBytes("UTF-8"));

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.offer(new String(delivery.getBody(), "UTF-8"));
                }
            }, consumerTag -> {
            });


            result = response.take();
            channel.basicCancel(ctag);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
//        Set up queue, exchanges and bindings
//        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
//        Queue queue = new Queue(queueName);
//        admin.declareQueue(queue);
//        DirectExchange exchange = new DirectExchange(exchangeName);
//        admin.declareExchange(exchange);
//        admin.declareBinding(
//                BindingBuilder.bind(queue).to(exchange).with(routingKey));
//
//        //Set up the listener
//        SimpleMessageListenerContainer container =
//                new SimpleMessageListenerContainer(connectionFactory);
//        Object listener = new Object() {
//            public String handleMessage(String foo) {
//                System.out.println(foo);
//                return "message success";
//            }
//        };
//
//        //Send a message
//        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
//        container.setMessageListener(adapter);
//        container.setQueueNames(queueName);
//        container.start();
//
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        String response = (String) template.convertSendAndReceive(exchangeName, routingKey, message);
//        System.out.println("RESPONSEEEE" + response);
//        try{
//            Thread.sleep(1000);
//        } catch(InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        container.stop();
//        return response;
    }
}
