package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.CBT;
import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import lombok.Getter;

@Getter
public class CBTRecommendResponse {
    private final CBT cbt;
    private final ChatMessagesResponse chatMessagesResponse;

    public CBTRecommendResponse(CBT cbt, ChatMessagesResponse chatMessagesResponse) {
        this.cbt = cbt;
        this.chatMessagesResponse = chatMessagesResponse;
    }
}
