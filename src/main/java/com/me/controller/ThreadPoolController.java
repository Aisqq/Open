package com.me.controller;

import com.me.annotation.Role;
import com.me.config.ThreadPoolConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/threadpool")
@RequiredArgsConstructor
@Role("admin")
public class ThreadPoolController {
    private final ThreadPoolConfig threadPoolConfig;
    @PostMapping("/core-size")
    public void setCorePoolSize(@RequestParam int coreSize) {
        threadPoolConfig.setCorePoolSize(coreSize);
    }
    @PostMapping("/max-size")
    public void setMaxPoolSize(@RequestParam int maxSize) {
        threadPoolConfig.setMaxPoolSize(maxSize);
    }

    @GetMapping("/get")
    public void he(){
        throw new RuntimeException("kk");
    }
}
