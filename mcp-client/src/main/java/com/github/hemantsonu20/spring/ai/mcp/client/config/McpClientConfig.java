package com.github.hemantsonu20.spring.ai.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {

        return chatClientBuilder
                .defaultSystem("You are an assistant that can query person data using mcp-server tools.")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultToolCallbacks(syncMcpToolCallbackProvider)
                .build();
    }
}
