package com.me.utils;

import com.me.controller.SseController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

public class SseSendUtil {
    public static void SseSend(Integer userId,String context)  {
        SseEmitter sseEmitter = SseController.sseEmitterMap.get(userId.toString());
        if(Objects.isNull(sseEmitter)){
            throw new RuntimeException("用户不在线");
        }
        try {
            sseEmitter.send(context);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
