package com.pado.tsid;

import com.pado.util.tsid.ACTRecordTsidUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class TsidUtilTest {

    @Test
    @DisplayName("TSID를 ZoneId에 맞게 LocalDateTime으로 변환한다")
    void toLocalDateTime_respectsZoneId() {
        //given
        Long tsid = ACTRecordTsidUtil.generate();

        ZoneId seoul = ZoneId.of("Asia/Seoul");
        ZoneId utc = ZoneId.of("UTC");

        //when
        LocalDateTime seoulTime = ACTRecordTsidUtil.toLocalDateTime(tsid, seoul);
        LocalDateTime utcTime = ACTRecordTsidUtil.toLocalDateTime(tsid, utc);

        //then
        Instant seoulInstant = seoulTime.atZone(seoul).toInstant();
        Instant utcInstant = utcTime.atZone(utc).toInstant();

        assertThat(seoulInstant).isEqualTo(utcInstant);

        //(UTC ↔ KST = 9시간)
        assertThat(seoulTime).isEqualTo(utcTime.plusHours(9));
    }
}
