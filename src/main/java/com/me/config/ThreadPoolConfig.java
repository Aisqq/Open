package com.me.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class ThreadPoolConfig implements WebMvcConfigurer {
    private ThreadPoolExecutor executor;
    @Bean("taskExecutor")
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(12);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        this.executor = executor.getThreadPoolExecutor();
        return executor;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(taskExecutor());
        configurer.setDefaultTimeout(30000);
    }


    public synchronized void setCorePoolSize(int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
        log.info("core:"+executor.getCorePoolSize());
    }


    public synchronized void setMaxPoolSize(int maxPoolSize) {
        executor.setMaximumPoolSize(maxPoolSize);
        log.info("max:"+executor.getMaximumPoolSize());
    }

}
