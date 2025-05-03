package com.amqp.demo.controller;

import com.amqp.demo.dto.Employee;
import com.amqp.demo.service.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq")
@RequiredArgsConstructor
public class RabbitMqController {
    private final Publisher publisher;

    @GetMapping("/send")
    public String sendMessage(@RequestBody Employee employee) {
        publisher.sendMessage(employee);
        return "Message sent: " + employee;
    }
}
