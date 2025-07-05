package com.me.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/sse")
public class SseController {
    public static final Map<String ,SseEmitter>  sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 推送数据给在线用户
     * @param id 用户id
     * @return
     */
    @GetMapping(path = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc(@PathVariable("id") String id) {
        SseEmitter emitter = sseEmitterMap.get(id);
        if(Objects.isNull(emitter)){
            emitter = new SseEmitter(0L);
            emitter.onCompletion(()->sseEmitterMap.remove(id));
            emitter.onTimeout(()->sseEmitterMap.remove(id));
            emitter.onError((ex)->sseEmitterMap.remove(id));
            sseEmitterMap.put(id,emitter);
        }
        return emitter;
    }
}
