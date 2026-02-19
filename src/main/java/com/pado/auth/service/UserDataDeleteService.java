package com.pado.auth.service;

import com.pado.act.infrastructure.ACTRecordRepository;
import com.pado.act.recommend.infrastructure.AiACTRecommendationRepository;
import com.pado.chat.infrastructure.ChattingDBRepository;
import com.pado.chat.infrastructure.summary.ChatSummaryRepository;
import com.pado.feedback.FeedbackRepository;
import com.pado.notification.infrastructure.fcm.infrastructure.FcmRepository;
import com.pado.user.application.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDataDeleteService {

    private final UserRepository userRepository;

    private final ACTRecordRepository actRecordRepository;
    private final AiACTRecommendationRepository aiACTRecommendationRepository;
    private final ChatSummaryRepository chatSummaryRepository;
    private final ChattingDBRepository chattingDBRepository;
    private final FeedbackRepository feedbackRepository;
    private final FcmRepository fcmRepository;

    public void deleteAllUserData(Long userId) {
        actRecordRepository.deleteAllByUserId(userId);
        aiACTRecommendationRepository.deleteAllByUserId(userId);
        chatSummaryRepository.deleteAllByUserId(userId);
        chattingDBRepository.deleteAllByUserId(userId);
        feedbackRepository.deleteAllByUserId(userId);
        fcmRepository.deleteAllByUserId(userId);

        userRepository.deleteUserByUserId(userId);
    }
}
