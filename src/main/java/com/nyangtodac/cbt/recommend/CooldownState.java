package com.nyangtodac.cbt.recommend;

import java.util.List;
import java.util.Set;

public enum CooldownState {

    NONE(List.of(
        "오늘 처음 찾아주셨네요!",
        "함께 불안을 다뤄볼까요?",
        "지금 이 순간에 집중해봐요."
    )),

    ONE(null),

    TWO(List.of(
        "여러 기법을 활용하고 계시네요! 조금 다른 접근을 시도해볼까요?",
        "다양하게 시도하시는 모습이 보기 좋아요. 이번엔 이렇게 해봐요."
    )),

    ALL(List.of(
        "열심히 노력하고 계시네요. 가장 적합한 기법을 다시 추천해드릴게요.",
        "많이 힘드셨죠. 지금 당장 도움이 될 방법을 찾아드릴게요.",
        "계속 시도하시는 모습이 대단해요. 이번엔 이 방법이 더 효과적일 거예요."
    )),

    MANY(List.of(
        "다양한 방법을 시도하고 계시는군요. 조금만 더 힘내봐요!",
        "꾸준히 노력하시는 모습이 보여요. 이번 기법도 도움이 될 거예요."
    ));

    private final List<String> messages;

    CooldownState(List<String> messages) {
        this.messages = messages;
    }

    public static CooldownState from(int cooldownCount, int totalCount) {
        if (cooldownCount == 0) return NONE;
        if (cooldownCount == 1) return ONE;
        if (cooldownCount == 2) return TWO;
        if (cooldownCount == totalCount) return ALL;
        return MANY;
    }

    public String message(Set<CBT> cooldownCBTs) {
        if (this == ONE) {
            CBT recent = cooldownCBTs.iterator().next();
            return "최근에 " + recent.getKoreanName() + "을(를) 하셨네요. 이번엔 다른 방법을 추천드릴게요.";
        }
        return messages.get(RandomUtil.pickRandom(messages.size()));
    }
}
