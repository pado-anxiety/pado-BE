package com.pado.act.application;

import com.pado.act.controller.dto.ACTRecords;
import com.pado.act.infrastructure.ACTRecordEntity;
import com.pado.tsid.ACTRecordTsidUtil;

import java.util.List;

public class ACTRecordMapper {

    public static ACTRecords toACTRecords(List<ACTRecordEntity> entities, int pageSize) {
        Long cursor = null;
        boolean hasNext = false;
        if (entities.size() > pageSize) {
            hasNext = true;
            entities.remove(entities.size() - 1);
        }
        if (!entities.isEmpty()) {
            cursor = entities.get(entities.size() - 1).getTsid();
        }
        ACTRecords actRecords = new ACTRecords(cursor, hasNext);
        for (ACTRecordEntity entity : entities) {
            actRecords.addRecord(new ACTRecords.Record(entity.getTsid(), entity.getActType(), ACTRecordTsidUtil.toLocalDateTime(entity.getTsid())));
        }
        return actRecords;
    }
}
