package com.nyangtodac.cbt.recommend;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CBTRecommender {

    private static final double COOLDOWN_PENALTY = 10.0;
    private static final Map<Situation, CBT> SITUATION_BONUS = Map.of(
            Situation.PRESENTATION_EXAM, CBT.BREATHING,
            Situation.RELATIONSHIP, CBT.CALMING_PHRASE,
            Situation.HEALTH_MORTALITY, CBT.GROUNDING,
            Situation.FUTURE_FINANCE, CBT.COGNITIVE_REFRAME,
            Situation.UNKNOWN, CBT.GROUNDING
    );

    public CBT recommend(Symptom symptom, int intensity, Situation situation, Set<CBT> cooldownCBTs) {
        validateIntensity(intensity);

        Map<CBT, Double> scores = new HashMap<>();

        for (CBT cbt : CBT.values()) {
            double totalScore = 0.0;

            //STEP 1: 증상 점수 반영
            if (symptom == Symptom.BODY) {
                totalScore += cbt.getBody();
            } else if (symptom == Symptom.MIND) {
                totalScore += cbt.getMind();
            }

            //STEP 2: 강도 점수 반영 (High/Low 비율 계산)
            double intensityRatio = (intensity - 1) / 4.0; // 0.0 ~ 1.0
            double intensityScore = (intensityRatio * cbt.getHigh()) + ((1 - intensityRatio) * cbt.getLow());
            totalScore += intensityScore;

            //STEP 3: 트리거 매칭 보너스 (+2점)
            if (SITUATION_BONUS.get(situation) == cbt) {
                totalScore += 2.0;
            }

            //STEP 4: 쿨타임 페널티 (-10점)
            if (cooldownCBTs.contains(cbt)) {
                totalScore -= COOLDOWN_PENALTY;
            }

            scores.put(cbt, totalScore);
        }

        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(CBT.BREATHING);
    }

    private void validateIntensity(int intensity) {
        if (intensity < 1 || intensity > 5) {
            throw new IllegalArgumentException(
                    "Intensity must be between 1 and 5, but got: " + intensity
            );
        }
    }

}