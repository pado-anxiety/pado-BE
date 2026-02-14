package com.pado.external.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            String config = System.getenv("FIREBASE_ADMIN_SDK");
            FirebaseOptions options = FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(config.getBytes())))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
