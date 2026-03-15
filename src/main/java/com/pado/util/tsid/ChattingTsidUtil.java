package com.pado.util.tsid;

import io.hypersistence.tsid.TSID;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ChattingTsidUtil {
    private static final TSID.Factory FACTORY = TSID.Factory.builder().build();

    public static Long generate() {
        return FACTORY.generate().toLong();
    }

    public static LocalDateTime toLocalDateTime(Long tsid, ZoneId zoneId) {
        return LocalDateTime.ofInstant(TSID.from(tsid).getInstant(), zoneId);
    }
}
