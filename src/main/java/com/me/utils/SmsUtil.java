package com.me.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SmsUtil {
    public static void main(String[] args) {
        String host = "https://cxkjsms.market.alicloudapi.com";
        String path = "/chuangxinsms/dxjk";
        String method = "POST";
        String appcode = "1a2b70507fa1402d88c810b79cd20f80"; // 开通服务后 买家中心-查看AppCode

        // 请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        // 查询参数
        Map<String, String> querys = new HashMap<>();
        querys.put("content", "【创信】你的验证码是：5873，3分钟内有效！");
        querys.put("mobile", "15979703778");

        try {
            // 构建请求URL，包含查询参数
            String queryString = querys.entrySet().stream()
                    .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                    .collect(Collectors.joining("&"));
            URI uri = URI.create(host + path + "?" + queryString);

            // 创建HttpClient实例
            HttpClient client = HttpClient.newBuilder().build();

            // 构建POST请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .headers(headers.entrySet().stream()
                            .flatMap(e -> java.util.stream.Stream.of(e.getKey(), e.getValue()))
                            .toArray(String[]::new))
                    .POST(HttpRequest.BodyPublishers.noBody()) // 没有请求体
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应信息
            System.out.println("状态码: " + response.statusCode());
            System.out.println("响应体: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 对参数进行URL编码
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}