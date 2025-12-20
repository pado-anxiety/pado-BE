package com.nyangtodac.chat.application;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
import com.nyangtodac.cbt.recommend.CBTRecommendResult;
import com.nyangtodac.cbt.recommend.CBTRecommendService;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.domain.CBTRecommendation;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CBTService {

    private final ChattingService chattingService;
    private final CBTRecommendService cbtRecommendService;

    public CBTRecommendResponse recommendCBT(Long userId, CBTRecommendRequest request) {
        CBTRecommendResult result = cbtRecommendService.recommend(userId, request);
        List<Chatting> cbtChat = createCBTRecommendChattings(request, result);
        chattingService.saveChattings(userId, cbtChat);
        return new CBTRecommendResponse(cbtChat, result.getCbt());
    }

    private List<Chatting> createCBTRecommendChattings(CBTRecommendRequest request, CBTRecommendResult result) {
        return List.of(new CBTRecommendation(new CBTRecommendation.Options(request.getSymptom(), request.getIntensity(), request.getSituation())), new Message(result.getMessage(), Sender.SYSTEM));
    }
}
