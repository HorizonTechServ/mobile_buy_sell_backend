package com.one.societyAPI.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.one.societyAPI.entity.FcmToken;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.FcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseNotificationService {

    private static final String CLASSNAME = "FirebaseNotificationService";

    private static final DefaultLogger LOGGER = new DefaultLogger(FirebaseNotificationService.class);

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    public void sendNotificationToUsers(List<User> users, String title, String body) {
        final String methodName = "sendNotificationToUsers";
        LOGGER.infoLog(CLASSNAME, methodName, "Sending notifications to " + users.size() + " users.");

        List<FcmToken> tokens = fcmTokenRepository.findByUserIn(users);

        if (tokens == null || tokens.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, methodName, "No FCM tokens found for the provided users.");
            return;
        }

        for (FcmToken token : tokens) {
            try {
                Message message = Message.builder()
                        .setToken(token.getToken())
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build();

                FirebaseMessaging.getInstance().send(message);
                LOGGER.infoLog(CLASSNAME, methodName,
                        "Notification sent to token: " + token.getToken());

            } catch (FirebaseMessagingException e) {
                LOGGER.errorLog(CLASSNAME, methodName,
                        "FirebaseMessagingException while sending notification to token: " + token.getToken()
                                + ". Error: " + e.getMessage(), e);

            } catch (Exception e) {
                LOGGER.errorLog(CLASSNAME, methodName,
                        "Unexpected error while sending notification to token: " + token.getToken()
                                + ". Error: " + e.getMessage(), e);
            }
        }
    }
}