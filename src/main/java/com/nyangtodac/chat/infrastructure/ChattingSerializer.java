package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyangtodac.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChattingSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Chatting chatting) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(chatting);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<Chatting> deserialize(String base64) throws JsonProcessingException {
        byte[] decoded = Base64.getDecoder().decode(base64);
        String json = new String(decoded, StandardCharsets.UTF_8);
        return Optional.of(objectMapper.readValue(json, Chatting.class));
    }

}
