package com.nyangtodac.act.controller.dto;

import com.nyangtodac.act.ACTType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ACTRecords {
    private final List<Record> content;
    private final Long cursor;
    private final Boolean hasNext;

    public ACTRecords(Long cursor, Boolean hasNext) {
        this.content = new ArrayList<>();
        this.cursor = cursor;
        this.hasNext = hasNext;
    }

    public void addRecord(Record record) {
        content.add(record);
    }

    @Getter
    public static class Record {
        private final Long id;
        private final ACTType type;
        private final LocalDateTime time;

        public Record(Long id, ACTType type, LocalDateTime time) {
            this.id = id;
            this.type = type;
            this.time = time;
        }
    }
}
