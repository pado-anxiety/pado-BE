package com.nyangtodac.cbt.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CBTRecommender {

    // 각 CBT를 추천하기 위한 최소 매칭 수
    @Value("${cbt.threshold.min-matches}")
    private int minMatches;

    private final Map<String, List<String>> cbtKeywords;
    private final List<String> intensityAdverbs;
    private final List<String> negativeWords;

    public CBTRecommender(@Value("${cbt.breathing.keywords}") List<String> breathingKeywords,
                          @Value("${cbt.grounding.keywords}") List<String> groundingKeywords,
                          @Value("${cbt.cognitive.keywords}") List<String> cognitiveKeywords,
                          @Value("${cbt.common.intensity-adverbs}") List<String> intensityAdverbs,
                          @Value("${cbt.common.negative-words}") List<String> negativeWords) {
        this.cbtKeywords = Map.of(
                "breathing", breathingKeywords,
                "grounding", groundingKeywords,
                "cognitive", cognitiveKeywords
        );
        this.intensityAdverbs = intensityAdverbs;
        this.negativeWords = negativeWords;
    }

    public String recommendCBT(List<String> userWords) {
        String bestCBT = null;
        int maxMatches = 0;

        // 강도 부사 카운트
        int intensityCount = 0;
        for (String word : userWords) {
            if (intensityAdverbs.contains(word)) {
                intensityCount++;
            }
        }

        // 부정 표현 카운트
        int negativeCount = 0;
        for (String word : userWords) {
            if (negativeWords.contains(word)) {
                negativeCount++;
            }
        }

        for (Map.Entry<String, List<String>> entry : cbtKeywords.entrySet()) {
            String cbtType = entry.getKey();
            List<String> keywords = entry.getValue();

            int matchCount = countMatches(userWords, keywords);

            // 강도 부사 보너스 (모든 CBT에 적용)
            if (intensityCount > 0) {
                matchCount += intensityCount;
            }

            // 부정 표현 보너스 (인지 재구성에만 강하게 적용)
            if (negativeCount > 0) {
                if (cbtType.equals("cognitive")) {
                    matchCount += negativeCount;
                } else {
                    matchCount += negativeCount;
                }
            }

            if (matchCount > maxMatches) {
                maxMatches = matchCount;
                bestCBT = cbtType;
            }
        }

        if (maxMatches < minMatches) {
            return null;
        }

        return bestCBT;
    }

    /**
     * 사용자 단어와 키워드 매칭 개수 세기
     */
    private int countMatches(List<String> userWords, List<String> keywords) {
        int count = 0;

        for (String userWord : userWords) {
            // 정확히 일치
            if (keywords.contains(userWord)) {
                count++;
                continue;
            }

            // 부분 매칭 (어간)
            for (String keyword : keywords) {
                if (isStemMatch(userWord, keyword)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    /**
     * 어간 매칭 확인 (힘들 ↔ 힘들다)
     */
    private boolean isStemMatch(String word1, String word2) {
        // 한쪽이 다른 쪽의 접두사이고, 길이 차이가 2 이하
        if (word1.startsWith(word2) && word1.length() - word2.length() <= 2) {
            return true;
        }
        return word2.startsWith(word1) && word2.length() - word1.length() <= 2;
    }
}