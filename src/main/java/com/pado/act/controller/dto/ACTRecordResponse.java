package com.pado.act.controller.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.ACTType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ACTRecordResponse {

    private final Long id;
    private final LocalDateTime time;
    private final ACTType type;
    private final JsonNode data;

    public ACTRecordResponse(Long id, LocalDateTime time, ACTType type, JsonNode data) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.data = data;
    }
}
