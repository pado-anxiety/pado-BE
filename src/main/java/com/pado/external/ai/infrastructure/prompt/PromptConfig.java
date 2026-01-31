package com.pado.external.ai.infrastructure.prompt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptConfig {

    @Bean
    SystemPrompt chatSystemPrompt() {
        return YamlResourceLoader.load("prompts/chat_prompt.yaml", SystemPrompt.class);
    }

    @Bean
    SystemPrompt summarySystemPrompt() {
        return YamlResourceLoader.load("prompts/summary_prompt.yaml", SystemPrompt.class);
    }

    @Bean
    SystemPrompt summaryPrefix() {
        return YamlResourceLoader.load("prompts/summary_prefix.yaml", SystemPrompt.class);
    }

    @Bean
    SystemPrompt actRecommendPrompt() {
        return YamlResourceLoader.load("prompts/act_recommend_prompt.yaml", SystemPrompt.class);
    }
}
