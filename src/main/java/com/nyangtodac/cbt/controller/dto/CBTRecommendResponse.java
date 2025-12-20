package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.CBT;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.controller.dto.ChattingResponse;
import com.nyangtodac.chat.controller.dto.mapper.ChattingResponseMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class CBTRecommendResponse {
    private final List<ChattingResponse> content;
    private final CBT cbt;

    public CBTRecommendResponse(List<Chatting> content, CBT cbt) {
        this.content = content.stream().map(ChattingResponseMapper::from).toList();
        this.cbt = cbt;
    }
}
