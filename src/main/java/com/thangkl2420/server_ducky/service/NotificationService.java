package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.*;
import com.thangkl2420.server_ducky.dto.FilterRequest;
import com.thangkl2420.server_ducky.dto.user.UserNotificationId;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.entity.user.UserNotification;
import com.thangkl2420.server_ducky.repository.NotificationRepository;
import com.thangkl2420.server_ducky.repository.UserNotificationRepository;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private final UserNotificationRepository userNotificationRepository;

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
            saveListNotification("Khẩn cấp",rescueCall.getRescue().getRescueName(), "rescue", String.valueOf(rescueCall.getId()));
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
            saveDuckyNotification(
                    "Yêu cầu cuộc gọi",
                    "Từ " + user.getFirstname() + " " + user.getLastname(),
                    "rtc",
                    room,
                    receiver
            );
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

    public void sendFCMFinishRescue(String tokenDevice, Integer id, Integer idUser) throws FirebaseMessagingException {
        saveDuckyNotification(
                "Xác nhận cứu hộ.",
                "Nhấn vào để xem chi tiết thông tin.",
                "finish",
                    String.valueOf(id),
                    idUser
                );
        Message message = Message.builder()
                .putData("title", "Xác nhận cứu hộ.")
                .putData("body", "Nhấn vào để xem chi tiết thông tin.")
                .putData("type", "finish")
                .putData("id", String.valueOf(id))
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

    public void finishRescueCall(User u) throws FirebaseMessagingException {
        if(u.getIdDevice() != null && !u.getIdDevice().isEmpty()){
            Message message = Message.builder()
                    .putData("title", "Kết thúc yêu cầu cứu hộ")
                    .putData("body", "Hoàn thành")
                    .putData("type", "complete")
                    .setNotification(
                            Notification.builder()
                                    .setTitle("Kết thúc yêu cầu cứu hộ")
                                    .setBody("Hoàn thành")
                                    .build()
                    )
                    .setToken(u.getIdDevice())
                    .build();
            firebaseMessaging.send(message);
        }
    }

    private void saveDuckyNotification(String title, String content, String type, String contentId, Integer id){
        LocalDateTime currentDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = currentDateTime.atZone(ZoneId.systemDefault());
        DuckyNotification notify = new DuckyNotification();
        notify.setTimestamp(zonedDateTime.toInstant().toEpochMilli());
        notify.setTitle(title);
        notify.setContent(content);
        notify.setType(type);
        notify.setIdContent(contentId);
        DuckyNotification dn = notificationRepository.save(notify);
        UserNotification un = new UserNotification();
        un.setId(new UserNotificationId(dn.getId(), id));
        un.setIsSeen(1);
        userNotificationRepository.save(un);
    }

    private void saveListNotification(String title, String content, String type, String contentId){
        LocalDateTime currentDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = currentDateTime.atZone(ZoneId.systemDefault());
        DuckyNotification notify = new DuckyNotification();
        notify.setTimestamp(zonedDateTime.toInstant().toEpochMilli());
        notify.setTitle(title);
        notify.setContent(content);
        notify.setType(type);
        notify.setIdContent(contentId);
        List<Integer> ids = userRepository.findAllIds();
        if(ids.size() != 0){
            DuckyNotification dn = notificationRepository.save(notify);
            List<UserNotification> notifications = new ArrayList<>();
            for (Integer id : ids) {
                UserNotification un = new UserNotification();
                un.setId(new UserNotificationId(dn.getId(), id));
                un.setIsSeen(1);
                notifications.add(un);
            }
            userNotificationRepository.saveAll(notifications);
        }
    }

    public List<DuckyNotification> getNotificationByUser(Integer id){
        return userNotificationRepository.findAllByUserId(id);
    }

    public List<DuckyNotification> filterNotification(FilterRequest request, Integer id){
        Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
        return userNotificationRepository.filterNotification(pageable, request.getStartTime(), request.getEndTime(), request.getKeyword(), id).getContent();
    }
}
