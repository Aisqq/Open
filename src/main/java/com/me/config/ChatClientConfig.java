package com.me.config;

import com.me.entity.User;
import com.me.service.ToolsService;
import com.me.utils.UserHolder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ChatClientConfig {

    private final ToolsService toolsService;

    private static final ConcurrentMap<String, ChatMemory> userMemories = new ConcurrentHashMap<>();

    public ChatClientConfig(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ChatClient chatClient(ChatClient.Builder builder) {
        User user = UserHolder.getUser();
        if (user == null) {
            throw new IllegalStateException("用户未登录，无法创建ChatClient");
        }

        String userId = String.valueOf(user.getUserId());

        ChatMemory userMemory = userMemories.computeIfAbsent(userId, key -> {
            InMemoryChatMemory memory = new InMemoryChatMemory();
            return new ChatMemory() {
                public void addMessage(Message message) {
                    if (message instanceof UserMessage) {
                        memory.add(userId, Collections.singletonList(message));
                    }
                }

                public List<Message> getMessages() {
                    return memory.get(userId, Integer.MAX_VALUE);
                }

                public void clear() {
                    memory.clear(userId);
                }

                @Override
                public void add(String conversationId, List<Message> messages) {
                    memory.add(conversationId, messages);
                }

                @Override
                public List<Message> get(String conversationId, int lastN) {
                    return memory.get(conversationId, lastN);
                }

                @Override
                public void clear(String conversationId) {
                    memory.clear(conversationId);
                }
            };
        });

        return builder
                .defaultSystem(String.format("""
        你是一个智能助手,当需要获取老人情况时需要调用对应的方法回答
        当前用户: %s,对应老人Id：%d
        """, user.getUsername() != null ? user.getUsername() : "未知用户", user.getElderId() != null ? user.getElderId() : 0)
                )
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(userMemory),
                        new SimpleLoggerAdvisor()
                )
                .defaultTools(toolsService)
                .build();
    }
}