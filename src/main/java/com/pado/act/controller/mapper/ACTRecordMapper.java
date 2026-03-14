package com.pado.act.controller.mapper;

import com.pado.act.application.query.ACTRecordItemView;
import com.pado.act.application.query.ACTRecordsView;
import com.pado.act.controller.dto.ACTRecords;
import com.pado.util.tsid.ACTRecordTsidUtil;

import java.time.ZoneId;

public class ACTRecordMapper {

    public static ACTRecords toACTRecords(ACTRecordsView view, ZoneId zoneId) {
        ACTRecords actRecords = new ACTRecords(view.getCursor(), view.getHasNext());
        for (ACTRecordItemView itemView : view.getActRecordItemViews()) {
            actRecords.addRecord(new ACTRecords.Record(itemView.getTsid(), itemView.getActType(), ACTRecordTsidUtil.toLocalDateTime(itemView.getTsid(), zoneId)));
        }
        return actRecords;
    }
}
