package com.one.societyAPI.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.one.societyAPI.logger.DefaultLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final String CLASSNAME = "FirebaseConfig";
    private static final DefaultLogger LOGGER = new DefaultLogger(FirebaseConfig.class);

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initializeFirebase() {
        final String methodName = "initializeFirebase";
        LOGGER.infoLog(CLASSNAME, methodName, "Initializing Firebase with service account: " + firebaseConfigPath);

        try (InputStream serviceAccount = new FileInputStream(firebaseConfigPath)) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                LOGGER.infoLog(CLASSNAME, methodName, "Firebase initialized successfully.");
            } else {
                LOGGER.warnLog(CLASSNAME, methodName, "Firebase already initialized. Skipping initialization.");
            }

        } catch (IOException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "IOException while initializing Firebase: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Unexpected error during Firebase initialization: " + e.getMessage(), e);
        }
    }
}