package com.me.controller;

import com.me.entity.User;
import com.me.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping("/api/sse")
public class SseController {
    public static final Map<String ,SseEmitter>  sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 推送数据给在线用户
     * @param
     * @return
     */
    @GetMapping(path = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc(@PathVariable("id") String id) {
        String userId = UserHolder.getUser().getUserId().toString();
        if(!id.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足");
        }
        UserHolder.removeUser();
        SseEmitter emitter = sseEmitterMap.get(id);
        if(Objects.isNull(emitter)){
            emitter = new SseEmitter(0L);
            emitter.onCompletion(()->{
                log.info("关闭SSE");
                sseEmitterMap.remove(id);
            });
            emitter.onTimeout(()->{
                log.info("超时SSE");
                sseEmitterMap.remove(id);
            });
            emitter.onError((ex)->{
                log.info("错误SSE");
                sseEmitterMap.remove(id);
            });
            sseEmitterMap.put(id,emitter);
        }
        return emitter;
    }
}
