package com.me;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {
        NacosConfigAutoConfiguration.class,
})
@EnableScheduling
@MapperScan("com.me.dao")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
