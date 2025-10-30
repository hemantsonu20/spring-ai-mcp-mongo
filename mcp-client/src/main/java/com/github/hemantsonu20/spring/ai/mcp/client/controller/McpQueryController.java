package com.github.hemantsonu20.spring.ai.mcp.client.controller;

import com.github.hemantsonu20.spring.ai.mcp.client.model.ChatRequest;
import com.github.hemantsonu20.spring.ai.mcp.client.model.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class McpQueryController {

    private final ChatClient chatClient;

    public McpQueryController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public ChatResponse ask(@RequestBody ChatRequest request) {
        var response = chatClient.prompt(request.query())
                .call()
                .content();

        return ChatResponse.of(response);
    }
}
