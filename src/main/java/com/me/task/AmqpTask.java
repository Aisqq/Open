package com.me.task;


import com.me.service.iotservice.AmqpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpTask {
    private final AmqpService amqpService;
    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {
        log.info("开始执行AMQP数据接收任务");
        amqpService.receiveMessage();
    }
}