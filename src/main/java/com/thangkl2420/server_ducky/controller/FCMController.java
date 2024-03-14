package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.NotificationRequest;
import com.thangkl2420.server_ducky.service.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;

    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody NotificationRequest notificationRequest) {
        try {
            fcmService.sendFCMNotification(notificationRequest.getToken(), notificationRequest.getTitle(), notificationRequest.getBody());
            return "Notification sent successfully!";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed to send notification: " + e.getMessage();
        }
    }

    @PostMapping("/send-all")
    public ResponseEntity<String> sendToAllUsers(@RequestBody NotificationRequest notificationRequest) {
        fcmService.sendNotificationToAllUsers(notificationRequest.getTitle(), notificationRequest.getBody());
        return ResponseEntity.status(HttpStatus.OK).body("Notification sent to all users successfully.");
    }
}
