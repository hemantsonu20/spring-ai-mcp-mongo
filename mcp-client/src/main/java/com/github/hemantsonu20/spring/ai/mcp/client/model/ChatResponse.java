package com.github.hemantsonu20.spring.ai.mcp.client.model;

import com.fasterxml.jackson.annotation.JsonRawValue;

public record ChatResponse(@JsonRawValue String response) {

    public static ChatResponse of(String response) {
        return new ChatResponse(response);
    }
}
