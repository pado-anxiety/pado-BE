package com.nyangtodac.act.application;

import com.nyangtodac.act.controller.dto.ACTRecords;
import com.nyangtodac.act.infrastructure.ACTRecordEntity;

import java.util.List;

public class ACTRecordMapper {

    public static ACTRecords toACTRecords(List<ACTRecordEntity> entities) {
        ACTRecords actRecords = new ACTRecords();
        for (ACTRecordEntity entity : entities) {
            actRecords.addRecord(new ACTRecords.Record(entity.getId(), entity.getActType(), entity.getTime()));
        }
        return actRecords;
    }
}
