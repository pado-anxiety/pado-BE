package com.nyangtodac.cbt.recommend;

import java.util.List;

public enum Situation {
    PRESENTATION_EXAM(List.of(
            "중요한 순간을 앞두고 계시네요. 긴장을 풀어드릴게요.",
            "발표나 시험 전에는 누구나 긴장하게 마련이에요. 함께 준비해봐요.",
            "곧 다가올 순간에 집중할 수 있도록 도와드릴게요."
    )),

    RELATIONSHIP(List.of(
            "관계에서 오는 불안은 누구에게나 있어요. 괜찮아요.",
            "사람과의 관계는 항상 어려워요. 천천히 생각해봐요.",
            "마음이 복잡하시죠. 지금 느끼는 감정을 인정해주세요."
    )),

    HEALTH_MORTALITY(List.of(
            "건강에 대한 걱정은 당연한 거예요. 지금 이 순간에 집중해봐요.",
            "생명과 건강에 대한 불안은 자연스러운 반응이에요.",
            "몸과 마음의 신호를 알아차리고 계신 거예요. 괜찮아요."
    )),

    FUTURE_FINANCE(List.of(
            "미래에 대한 걱정이 크시군요. 하나씩 정리해봐요.",
            "앞으로의 일이 막연하게 느껴지죠. 현실적으로 생각해봐요.",
            "경제적인 불안은 누구에게나 있어요. 차근차근 접근해봐요."
    )),

    UNKNOWN(List.of(
            "이유 모를 불안도 괜찮아요. 지금 이 순간에 집중해봐요.",
            "막연한 불안감이 드시는군요. 현재로 돌아와봐요.",
            "불안의 원인을 모르는 것도 자연스러워요. 천천히 알아가봐요."
    ));

    private final List<String> messages;

    Situation(List<String> messages) {
        this.messages = messages;
    }

    public String getRandomMessage() {
        return messages.get(RandomUtil.pickRandom(messages.size()));
    }
}