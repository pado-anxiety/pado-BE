package com.nyangtodac.chat.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nyangtodac.cbt.recommend.Situation;
import com.nyangtodac.cbt.recommend.Symptom;
import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.Getter;

@Getter
public class CBTRecommendation extends Chatting {
    private final Options options;

    public CBTRecommendation(Options options) {
        super(Type.CBT_RECOMMENDATION, TsidUtil.generate());
        this.options = options;
    }

    @JsonCreator
    public CBTRecommendation(
            @JsonProperty("tsid") Long tsid,
            @JsonProperty("options") Options options) {
        super(Type.CBT_RECOMMENDATION, tsid);
        this.options = options;
    }

    @Getter
    public static class Options {

        private final Symptom symptom;
        private final int intensity;
        private final Situation situation;

        @JsonCreator
        public Options(
                @JsonProperty("symptom") Symptom symptom,
                @JsonProperty("intensity") int intensity,
                @JsonProperty("situation") Situation situation
        ) {
            this.symptom = symptom;
            this.intensity = intensity;
            this.situation = situation;
        }
    }
}
