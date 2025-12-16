package com.nyangtodac.cbt.recommend;

import java.util.List;

public enum Symptom {
    BODY {
        @Override
        public String getRandomMessage(int intensity) {
            if (intensity >= 4) {
                return pickRandom(List.of(
                        "몸이 많이 긴장되어 있는 것 같아요.",
                        "숨 쉬기가 힘들 정도로 힘드시군요.",
                        "신체 반응이 강하게 오고 있네요."
                ));
            } else if (intensity >= 2) {
                return pickRandom(List.of(
                        "몸이 조금 불편하신가 봐요.",
                        "신체적으로 긴장감이 느껴지시나요?",
                        "몸의 신호를 알아차리셨네요."
                ));
            } else {
                return pickRandom(List.of(
                        "가벼운 신체 반응이 있으시군요.",
                        "몸이 약간 반응하고 있어요."
                ));
            }
        }
    },

    MIND {
        @Override
        public String getRandomMessage(int intensity) {
            if (intensity >= 4) {
                return pickRandom(List.of(
                        "생각이 너무 많아서 힘드시죠.",
                        "머릿속이 복잡하고 정리가 안 되는 것 같아요.",
                        "불안한 생각들이 계속 떠오르고 있군요."
                ));
            } else if (intensity >= 2) {
                return pickRandom(List.of(
                        "걱정되는 생각이 자꾸 드시나 봐요.",
                        "마음이 조금 불편하신 것 같아요.",
                        "생각이 정리되지 않는 느낌이군요."
                ));
            } else {
                return pickRandom(List.of(
                        "약간의 불안이 느껴지시는군요.",
                        "마음이 살짝 무거워진 것 같아요."
                ));
            }
        }
    };

    public abstract String getRandomMessage(int intensity);

    protected String pickRandom(List<String> options) {
        return options.get(RandomUtil.pickRandom(options.size()));
    }
}