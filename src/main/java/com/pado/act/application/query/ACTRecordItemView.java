package com.pado.act.application.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.ACTType;
import lombok.Getter;

@Getter
public class ACTRecordItemView {

    private final Long tsid;
    private final ACTType actType;
    private final JsonNode data;

    public ACTRecordItemView(Long tsid, ACTType actType, JsonNode data) {
        this.tsid = tsid;
        this.actType = actType;
        this.data = data;
    }
}
