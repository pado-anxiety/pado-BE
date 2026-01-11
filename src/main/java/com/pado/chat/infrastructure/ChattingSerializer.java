package com.pado.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChattingSerializer {

    private final ObjectMapper objectMapper;

    public String serialize(Chatting chatting) throws JsonProcessingException {
        return objectMapper.writeValueAsString(chatting);
    }

    public Optional<Chatting> deserialize(String json) throws JsonProcessingException {
        return Optional.of(objectMapper.readValue(json, Chatting.class));
    }

}
