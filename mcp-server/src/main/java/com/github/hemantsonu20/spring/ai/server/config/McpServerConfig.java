package com.github.hemantsonu20.spring.ai.server.config;

import com.github.hemantsonu20.spring.ai.server.service.PersonMcpService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider personMcpServiceProvider(PersonMcpService personMcpService) {
        return MethodToolCallbackProvider.builder().toolObjects(personMcpService).build();
    }
}
