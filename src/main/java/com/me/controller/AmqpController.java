package com.me.controller;

import com.me.service.iotservice.IotService;
import com.me.utils.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.jms.JMSException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/amqp")
public class AmqpController {

    private final IotService iotService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String content) throws JMSException {
        log.info(content);
        iotService.sendMessage(content);
        return Message.SEND_SUCCESS;
    }
}