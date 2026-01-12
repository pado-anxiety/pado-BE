package com.pado.tsid;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TsidFactoryTest {

    @Test
    @DisplayName("다른 노드 값으로 설정된 TSID는 중복되지 않음을 보장")
    void no_collision_between_nodes() throws Exception {
        TSID.Factory f1 = TSID.Factory.builder().withNode(1).build();
        TSID.Factory f2 = TSID.Factory.builder().withNode(2).build();

        Set<TSID> set = ConcurrentHashMap.newKeySet();
        ExecutorService es = Executors.newFixedThreadPool(2);

        es.submit(() -> {
            for (int i = 0; i < 10_000; i++) {
                set.add(f1.generate());
            }
        });

        es.submit(() -> {
            for (int i = 0; i < 10_000; i++) {
                set.add(f2.generate());
            }
        });

        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(20_000, set.size());
    }
}
