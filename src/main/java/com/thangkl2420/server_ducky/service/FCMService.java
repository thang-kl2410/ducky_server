package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.*;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCMService {
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
//    public void sendNotificationWithinRadius(double latitude, double longitude, double radius, String title, String message) {
//        // Lấy danh sách người dùng trong phạm vi 10km từ vị trí đã xác định
//        List<User> usersWithinRadius = userRepository.findUsersWithinRadius(latitude, longitude, radius);
//
//        if (usersWithinRadius.isEmpty()) {
//            return; // Không có người dùng trong phạm vi 10km
//        }
//
//        // Lấy danh sách device token từ danh sách người dùng
//        List<String> deviceTokens = new ArrayList<>();
//        for (User user : usersWithinRadius) {
//            deviceTokens.add(user.getDeviceToken());
//        }
//
//        try {
//            // Gửi thông báo FCM tới danh sách device token
//            MulticastMessage multicastMessage = MulticastMessage.builder()
//                    .setNotification(Notification.builder().setTitle(title).setBody(message).build())
//                    .addAllTokens(deviceTokens)
//                    .build();
//
//            FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
//        } catch (FirebaseMessagingException e) {
//            // Xử lý nếu gặp lỗi khi gửi thông báo
//            e.printStackTrace();
//        }
//    }
}
