package com.me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @GetMapping(value = "/stream", produces = "text/plain;charset=utf-8")
    public Flux<String> generateStreamChat(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(new QuestionAnswerAdvisor(vectorStore))// SearchRequest.builder().query(message).build()
                .stream()
                .content();
    }
}

