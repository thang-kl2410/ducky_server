package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.*;
import com.thangkl2420.server_ducky.entity.RescueCall;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository repository;

    public void sendFCMNotification(String token, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .setToken(token)
                .build();

        firebaseMessaging.send(message);
    }

    public void sendNotificationToAllUsers(String title, String message) {
        List<String> deviceTokens = repository.findAllDeviceTokens() != null
                ? repository.findAllDeviceTokens()
                : Collections.emptyList();

        deviceTokens = deviceTokens.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (deviceTokens.isEmpty()) {
            return;
        }
        try {
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(Notification.builder().setTitle(title).setBody(message).build())
                    .addAllTokens(deviceTokens)
                    .build();
            firebaseMessaging.sendMulticast(multicastMessage);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendFCMById(Integer id, String title, String body, Integer senderId) throws FirebaseMessagingException {
        String tokenDevice = repository.findDeviceToken(id).orElseThrow();
        if(tokenDevice != null && !tokenDevice.isEmpty()){
            Message message = Message.builder()
                    .putData("title", title)
                    .putData("body", body)
                    .putData("senderId", String.valueOf(senderId))
                    .setNotification(
                            Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build()
                    )
                    .setToken(tokenDevice)
                    .build();
            firebaseMessaging.send(message);
        }
    }

    public void sendNotificationToMultipleUsers(RescueCall rescueCall, List<String> users) {
        if (users.isEmpty()) {
            return;
        }
        try {
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(Notification.builder().setTitle("title").setBody("message").build())
                    .addAllTokens(users)
                    .build();
            firebaseMessaging.sendMulticast(multicastMessage);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
