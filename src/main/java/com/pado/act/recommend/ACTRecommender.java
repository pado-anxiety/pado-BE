package com.pado.act.recommend;

import com.pado.act.ACTType;
import com.pado.chat.domain.ChatSummary;
import com.pado.external.ai.application.OpenAiService;
import com.pado.external.ai.application.response.OpenAiActRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ACTRecommender {

    private final OpenAiService openAiService;

    private static final ACTRecommendation DEFAULT_RECOMMENDATION = new ACTRecommendation(
            ACTType.CONTACT_WITH_PRESENT,
            List.of(
                    "무엇을 해야 할지 애매할 때는, 지금 느껴지는 감각에 하나씩 주의를 기울여 보세요.",
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
        return new ACTRecommendation(getResponse.getActType(), getResponse.getReasons());
    }
}
