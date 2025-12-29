package com.nyangtodac.act.recommend;

import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.external.ai.application.OpenAiService;
import com.nyangtodac.external.ai.application.response.OpenAiActRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ACTRecommender {

    private final OpenAiService openAiService;

    private static final ACTRecommendation DEFAULT_RECOMMENDATION = new ACTRecommendation(
            ACT.EMOTION_NOTE,
            List.of(
                    "무엇을 해야 할지 애매할 때는, 먼저 지금 상태를 정리해보는 것만으로도 충분해요.",
                    "감정 노트를 통해 지금의 감정과 생각을 있는 그대로 적어보세요.",
                    "그 다음에, 원하신다면 다른 ACT도 선택할 수 있어요."
            )
    );

    public ACTRecommendation getDefaultRecommendation() {
        return DEFAULT_RECOMMENDATION;
    }

    public ACTRecommendation getRecommendation(ChatSummary chatSummary) {
        Optional<OpenAiActRecommendationResponse> response = openAiService.getACTRecommendation(chatSummary);
        if (response.isEmpty()) {
            return getDefaultRecommendation();
        }
        OpenAiActRecommendationResponse getResponse = response.get();
        return new ACTRecommendation(getResponse.getAct(), getResponse.getReasons());
    }
}
