package com.nyangtodac.tsid;

import io.hypersistence.tsid.TSID;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ACTRecordTsidUtil {
    private static final TSID.Factory FACTORY = TSID.Factory.builder().build();
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static Long generate() {
        return FACTORY.generate().toLong();
    }

    public static LocalDateTime toLocalDateTime(Long tsid) {
        return LocalDateTime.ofInstant(TSID.from(tsid).getInstant(), KST);
    }
}
