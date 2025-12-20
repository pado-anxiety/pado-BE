package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyangtodac.chat.domain.CBTRecommendation;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.Message;
import com.nyangtodac.chat.domain.Type;
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
        JsonNode jsonNode = objectMapper.readTree(json);
        Type type = Type.valueOf(jsonNode.get("type").asText());

        return switch (type) {
            case CHAT ->
                    Optional.of(objectMapper.treeToValue(jsonNode, Message.class));
            case CBT_RECOMMENDATION ->
                    Optional.of(objectMapper.treeToValue(jsonNode, CBTRecommendation.class));
        };

    }

}
