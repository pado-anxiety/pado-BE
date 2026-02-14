package com.pado.fcm.application;

import com.pado.fcm.infrastructure.FcmEntity;
import com.pado.fcm.infrastructure.FcmRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FcmTxService {

    private final FcmRepository fcmRepository;

    public void saveToken(Long userId, String fcmToken) {
        fcmRepository.save(new FcmEntity(userId, fcmToken));
    }

    @Transactional(readOnly = true)
    public String getToken(Long userId) {
        return getFcm(userId).getToken();
    }

    public void deactivateToken(Long userId) {
        FcmEntity fcm = getFcm(userId);
        fcm.updateIsActive(false);
        fcmRepository.save(fcm);
    }

    private FcmEntity getFcm(Long userId) {
        return fcmRepository.findByUserId(userId).orElseThrow(() -> new FcmNotFoundException(userId));
    }
}
