package com.amqp.demo.service;

import com.amqp.demo.dto.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class Receiver {

    private final ObjectMapper objectMapper;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "${employee.queue}",durable = "true"),exchange = @Exchange(value = "${employee.exchange}",type="direct"),key="${employee.routing.key}")
    })
    public void receiveMessage(Message message, @Header(AmqpHeaders.CONSUMER_QUEUE)String queue,@Header(AmqpHeaders.RECEIVED_EXCHANGE)String exchange) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Exchange from message properties: {}",message.getMessageProperties().getReceivedExchange());
        try {
            Employee employee = objectMapper.readValue(message.getBody(),Employee.class);
            log.debug("Received: {}", employee);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
