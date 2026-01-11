package com.nyangtodac.act;

import com.nyangtodac.act.application.ACTRecordMapper;
import com.nyangtodac.act.controller.dto.ACTRecords;
import com.nyangtodac.act.infrastructure.ACTRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ACTRecordMapperTest {

    @Test
    @DisplayName("pageSize 초과 시 마지막 1개 제거 + hasNext true")
    void pageSize_초과_시_hasNext_true() {
        //given
        int pageSize = 2;

        ACTRecordEntity e1 = new ACTRecordEntity(1L, 1L, ACTType.EMOTION_NOTE, null);
        ACTRecordEntity e2 = new ACTRecordEntity(2L, 1L, ACTType.ACCEPTANCE, null);
        ACTRecordEntity e3 = new ACTRecordEntity(3L, 1L, ACTType.COGNITIVE_DEFUSION, null); // 초과분

        List<ACTRecordEntity> entities = new ArrayList<>(List.of(e1, e2, e3));

        //when
        ACTRecords result = ACTRecordMapper.toACTRecords(entities, pageSize);
        System.out.println(result);

        //then
        assertThat(result.getHasNext()).isTrue();
        assertThat(result.getCursor()).isEqualTo(2L);
        assertThat(result.getContent()).hasSize(2);

        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getContent().get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("pageSize 이하이면 hasNext false")
    void pageSize_이하_hasNext_false() {
        //given
        int pageSize = 3;

        ACTRecordEntity e1 = new ACTRecordEntity(1L, 1L, ACTType.EMOTION_NOTE, null);
        ACTRecordEntity e2 = new ACTRecordEntity(2L, 1L, ACTType.ACCEPTANCE, null);

        List<ACTRecordEntity> entities = new ArrayList<>(List.of(e1, e2));

        //when
        ACTRecords result = ACTRecordMapper.toACTRecords(entities, pageSize);

        //then
        assertThat(result.getHasNext()).isFalse();
        assertThat(result.getCursor()).isEqualTo(2L);
        assertThat(result.getContent()).hasSize(2);
    }

}
