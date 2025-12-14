package com.nyangtodac.cbt.application;

import com.nyangtodac.chat.application.MessageService;
import com.nyangtodac.chat.komoran.KomoranUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CBTRecommendService {

    public static final int CBT_RECOMMEND_INTERVAL_OF_MINUTES = 3;

    private final MessageService messageService;
    private final CBTRecommender cbtRecommender;

    public boolean canRecommendCBT(Long userId) {
        return messageService.canRecommendCBT(userId);
    }

    public String recommend(Long userId, String message) {
        List<String> words = KomoranUtil.analyzeMessage(message);
        return cbtRecommender.recommendCBT(words);
    }
}
