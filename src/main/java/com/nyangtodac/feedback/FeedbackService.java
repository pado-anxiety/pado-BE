package com.nyangtodac.feedback;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public void saveFeedback(Long userId, String feedback) {
        feedbackRepository.save(new Feedback(userId, feedback));
    }
}
