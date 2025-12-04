package com.nyangtodac.openai;

import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequest;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.infrastructure.OpenAiClient;
import com.nyangtodac.external.ai.retry.OpenAiRetryConfig;
import com.nyangtodac.external.ai.retry.OpenAiServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(value = OpenAiClient.class)
@Import(OpenAiRetryConfig.class)
public class OpenAiRetryTest {

    private static final String URL = "/v1/chat/completions";

    @Autowired
    OpenAiClient openAiClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Test
    @DisplayName("openai 요청 2번 실패 후 1번 성공 응답")
    void retry_should_retry_until_success() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withSuccess("{\"response\":\"ok\"}", MediaType.APPLICATION_JSON));

        ChatCompletionRequest request = new ChatCompletionRequest();

        ChatCompletionResponse response = openAiClient.sendRequest(request);

        assertThat(response).isNotNull();

        mockServer.verify();
    }

    @Test
    @DisplayName("openai 요청 3번 모두 실패")
    void retry_should_fail_after_max_attempts() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());

        ChatCompletionRequest request = new ChatCompletionRequest();

        assertThatThrownBy(() -> openAiClient.sendRequest(request))
                .isInstanceOf(OpenAiServerException.class);

        mockServer.verify();
    }
}
