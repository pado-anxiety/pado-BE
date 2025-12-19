package com.nyangtodac.chat.application;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
import com.nyangtodac.cbt.recommend.CBTRecommendResult;
import com.nyangtodac.cbt.recommend.CBTRecommendService;
import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import com.nyangtodac.chat.controller.dto.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CBTService {

    private final ChattingService chattingService;
    private final CBTRecommendService cbtRecommendService;

    public CBTRecommendResponse recommendCBT(Long userId, CBTRecommendRequest request) {
        CBTRecommendResult result = cbtRecommendService.recommend(userId, request);
        List<Chatting> cbtChat = new ArrayList<>();
        cbtChat.add(new CBTRecommendation(new CBTRecommendation.Options(request.getSymptom(), request.getIntensity(), request.getSituation())));
        cbtChat.add(new Message(result.getMessage(), Sender.SYSTEM));
        chattingService.saveChattings(userId, cbtChat);
        return new CBTRecommendResponse(result.getCbt(), new ChatMessagesResponse(cbtChat));
    }
}
