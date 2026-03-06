package com.pado.notification.infrastructure.fcm.application;

import com.pado.notification.infrastructure.fcm.infrastructure.FcmEntity;
import com.pado.notification.infrastructure.fcm.infrastructure.FcmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FcmTxService {

    private final FcmRepository fcmRepository;

    public void saveToken(Long userId, String fcmToken) {
        Optional<FcmEntity> get = fcmRepository.findByUserId(userId);
        if (get.isEmpty()) {
            fcmRepository.save(new FcmEntity(userId, fcmToken));
        } else {
            FcmEntity fcmEntity = get.get();
            fcmEntity.updateToken(fcmToken);
            fcmEntity.updateIsActive(true);
            fcmRepository.save(fcmEntity);
        }
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
