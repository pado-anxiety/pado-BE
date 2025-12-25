package com.nyangtodac.openai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@TestConfiguration
public class TestAiClientConfig {

    @Bean
    @Qualifier("openAiRestClient")
    public RestClient.Builder openaiRestClient() {
        return RestClient.builder().baseUrl("http://localhost");
    }
}