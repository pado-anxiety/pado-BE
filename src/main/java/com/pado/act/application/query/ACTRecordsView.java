package com.pado.act.application.query;

import lombok.Getter;

import java.util.List;

@Getter
public class ACTRecordsView {
    private final List<ACTRecordItemView> actRecordItemViews;
    private final Long cursor;
    private final Boolean hasNext;

    public ACTRecordsView(List<ACTRecordItemView> actRecordItemViews, Long cursor, Boolean hasNext) {
        this.actRecordItemViews = actRecordItemViews;
        this.cursor = cursor;
        this.hasNext = hasNext;
    }
}
