package com.me.service;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ToolsService {
    @Tool(description = "获取当前时间或日期")
    public String getDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String var10000 = now.format(formatter);
        return "当前时间：" + var10000 + "。";
    }
    @Tool(description = "获取天气信息")
    public String getWeather(@ToolParam(description = "地区格式,例如：北京") String location){
        return location+"暴雨25度";
    }
}
