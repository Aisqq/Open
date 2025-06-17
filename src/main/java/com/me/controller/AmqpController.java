package com.me.controller;

import com.me.service.SendService;
import com.me.utils.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/amqp")
public class AmqpController {

    private final SendService sendService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String content) throws JMSException {
        log.info(content);
        sendService.sendMessage(content);
        return Message.SEND_SUCCESS;
    }
}