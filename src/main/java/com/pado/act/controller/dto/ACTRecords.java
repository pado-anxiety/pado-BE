package com.pado.act.controller.dto;

import com.pado.act.ACTType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ACTRecords {
    private final List<Record> content;
    private final String cursor;
    private final Boolean hasNext;

    public ACTRecords(Long cursor, Boolean hasNext) {
        this.content = new ArrayList<>();
        this.cursor = String.valueOf(cursor);
        this.hasNext = hasNext;
    }

    public void addRecord(Record record) {
        content.add(record);
    }

    @Getter
    public static class Record {
        private final String id;
        private final ACTType type;
        private final LocalDateTime time;

        public Record(Long id, ACTType type, LocalDateTime time) {
            this.id = String.valueOf(id);
            this.type = type;
            this.time = time;
        }
    }
}
