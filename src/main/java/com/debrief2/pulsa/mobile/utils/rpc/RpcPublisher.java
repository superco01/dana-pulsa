package com.debrief2.pulsa.mobile.utils.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class RpcPublisher {
    private ConnectionFactory connectionFactory;
    private Channel channel;
    private String queueName;
    private Connection connection;

    @Autowired
    public RpcPublisher(@Value("${cloudamqp.url}") String url) throws Exception {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUri(url);
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            if (channel != null && connection != null){
                channel.close();
                connection.close();
            }
        }
    }

    public String sendMessage(String queueName, String message) {
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
    }
}
