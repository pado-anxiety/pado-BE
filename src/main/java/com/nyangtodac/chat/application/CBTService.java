package com.nyangtodac.chat.application;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
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

    public ChatMessagesResponse recommendCBT(Long userId, CBTRecommendRequest request) {
        CBTRecommendResponse response = cbtRecommendService.recommend(userId, request);
        List<Chatting> cbtChat = new ArrayList<>();
        cbtChat.add(new CBTRecommendation(new CBTRecommendation.Options(request.getSymptom(), request.getIntensity(), request.getSituation())));
        for (String message : response.getMessages()) {
            cbtChat.add(new Message(message, Sender.SYSTEM));
        }
        chattingService.saveChattings(userId, cbtChat);
        return new ChatMessagesResponse(cbtChat);
    }
}
