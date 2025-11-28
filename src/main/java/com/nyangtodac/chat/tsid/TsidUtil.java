package com.nyangtodac.chat.tsid;

import io.hypersistence.tsid.TSID;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TsidUtil {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static Long generate() {
        return TSID.Factory.getTsid().toLong();
    }

    public static LocalDateTime toLocalDateTime(Long tsid) {
        return LocalDateTime.ofInstant(TSID.from(tsid).getInstant(), KST);
    }
}
