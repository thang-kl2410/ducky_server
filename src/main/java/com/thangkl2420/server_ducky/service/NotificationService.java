package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.*;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.repository.NotificationRepository;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

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

    public void saveNotification(DuckyNotification notification){
        notificationRepository.save(notification);
    }

    public void sendNotificationToAllUsers(String title, String message) {
        List<String> deviceTokens = userRepository.findAllDeviceTokens() != null
                ? userRepository.findAllDeviceTokens()
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
        String tokenDevice = userRepository.findDeviceToken(id).orElseThrow(null);
        if(tokenDevice != null && !tokenDevice.isEmpty()){
            Message message = Message.builder()
                    .putData("title", title)
                    .putData("body", body)
                    .putData("senderId", String.valueOf(senderId))
                    .putData("type", "message")
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
                    .setNotification(
                            Notification
                                    .builder()
                                    .setTitle("Khẩn cấp")
                                    .setBody(rescueCall.getRescue().getRescueName())
                                    .build()
                    )
                    .putData("id", rescueCall.getId().toString())
                    .putData("title", "Khẩn cấp")
                    .putData("body", rescueCall.getRescue().getRescueName())
                    .putData("desc", rescueCall.getDescription())
                    .putData("name", rescueCall.getRescue().getRescueName())
                    .putData("type", "rescue")
                    .addAllTokens(users)
                    .build();
            firebaseMessaging.sendMulticast(multicastMessage);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationRtc(Integer receiver, String room, Principal connectedUser) throws FirebaseMessagingException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        String tokenDevice = userRepository.findDeviceToken(receiver).orElseThrow(null);
        if(tokenDevice != null && !tokenDevice.isEmpty()){
            Message message = Message.builder()
                    .putData("title", "Yêu cầu cuộc gọi")
                    .putData("body", "Từ " + user.getFirstname() + " " + user.getLastname())
                    .putData("room", room)
                    .putData("type", "rtc")
                    .setNotification(
                            Notification.builder()
                                    .setTitle("Yêu cầu cuộc gọi")
                                    .setBody("Từ " + user.getFirstname() + " " + user.getLastname())
                                    .build()
                    )
                    .setToken(tokenDevice)
                    .build();
            firebaseMessaging.send(message);
        }
    }

    public void sendFCMFinishRescue(String tokenDevice, String id) throws FirebaseMessagingException {
        Message message = Message.builder()
                .putData("title", "Xác nhận cứu hộ.")
                .putData("body", "Nhấn vào để xem chi tiết thông tin.")
                .putData("type", "finish")
                .putData("id", id)
                .setNotification(
                        Notification.builder()
                                .setTitle("Xác nhận cứu hộ.")
                                .setBody("Nhấn vào để xem chi tiết thông tin.")
                                .build()
                )
                .setToken(tokenDevice)
                .build();
        firebaseMessaging.send(message);
    }
}
