package com.amqp.demo.service;

import com.amqp.demo.dto.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${employee.exchange}")
    private String exchange;
    @Value("${employee.routing.key}")
    private String key;

    public void sendMessage(Employee employee) {
        rabbitTemplate.convertAndSend(exchange, key, buildMessage(employee));
        log.debug("Sent: {}", employee);
    }

    private Message buildMessage(Employee employee) {
        return MessageBuilder.withBody(getDataToSend(employee))
                .setContentType("application/json")
                .build();
    }

    private byte[] getDataToSend(Employee employee) {
        try {
            return objectMapper.writeValueAsBytes(employee);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
