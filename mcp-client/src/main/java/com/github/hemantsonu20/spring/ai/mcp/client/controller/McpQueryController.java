package com.github.hemantsonu20.spring.ai.mcp.client.controller;

import com.github.hemantsonu20.spring.ai.mcp.client.model.ChatRequest;
import com.github.hemantsonu20.spring.ai.mcp.client.model.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class McpQueryController {

    private final ChatClient ollamaChatClient;
    private final ChatClient openaiChatClient;

    public McpQueryController(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("openaiChatClient") ChatClient openaiChatClient) {
        this.ollamaChatClient = ollamaChatClient;
        this.openaiChatClient = openaiChatClient;
    }

    @PostMapping
    public ChatResponse ask(@RequestBody ChatRequest request) {
        ChatClient selectedClient = "openai".equalsIgnoreCase(request.provider())
                ? openaiChatClient
                : ollamaChatClient;

        var response = selectedClient.prompt(request.query())
                .call()
                .content();

        return ChatResponse.of(response);
    }
}
