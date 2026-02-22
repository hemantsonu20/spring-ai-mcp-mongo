package com.github.hemantsonu20.spring.ai.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfig {

    private static final String SYSTEM_PROMPT = """
            You are a helpful assistant with access to a person database through specialized tools.

            You can query person information using the following capabilities:
            - Search people by city (case-insensitive)
            - Search people by state (case-insensitive)
            - Search people by last name (exact match)
            - Find people within an age range
            - Find people older than a specific age
            - List all people in the database

            When responding to queries:
            1. Use the most appropriate tool(s) for the user's request
            2. Provide clear, concise summaries of the results
            3. If multiple people match, present the information in an organized format
            4. If no results are found, suggest alternative search criteria
            5. Be helpful and conversational in your responses

            Always prioritize accuracy and use the tools to fetch real-time data rather than making assumptions.
            """;

    @Bean
    @Qualifier("ollamaChatClient")
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {

        return ChatClient.builder(ollamaChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultToolCallbacks(syncMcpToolCallbackProvider)
                .build();
    }

    @Bean
    @Qualifier("openaiChatClient")
    public ChatClient openaiChatClient(OpenAiChatModel openAiChatModel, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {

        return ChatClient.builder(openAiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultToolCallbacks(syncMcpToolCallbackProvider)
                .build();
    }
}
