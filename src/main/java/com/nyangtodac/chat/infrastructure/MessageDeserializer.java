package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyangtodac.chat.domain.Message;
import com.nyangtodac.chat.domain.Type;
import com.nyangtodac.chat.controller.dto.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDeserializer {

    private final ObjectMapper objectMapper;

    public Optional<Message> deserialize(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        String json = new String(decoded, StandardCharsets.UTF_8);
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            Type type = Type.valueOf(jsonNode.get("type").asText());
            if (type == Type.CHAT) {
                Message message = objectMapper.treeToValue(jsonNode, Message.class);
                if (!message.getSender().equals(Sender.SYSTEM.name())) {
                    return Optional.of(message);
                }
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            log.warn("MessageContext 생성 중 역직렬화에 실패했습니다.", e);
            return Optional.empty();
        }
    }

}
