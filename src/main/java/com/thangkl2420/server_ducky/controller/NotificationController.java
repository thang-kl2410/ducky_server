package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.post.NotificationRequest;
import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.service.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService fcmService;

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

    @PostMapping("/send-by-id")
    public String sendNotificationById(@Param(value = "id") Integer id) {
        try {
            fcmService.sendFCMById(id, "test", "test", null);
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

    @GetMapping("/notification")
    public ResponseEntity<List<DuckyNotification>> getAllByUser(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return ResponseEntity.ok(fcmService.getNotificationByUser(user.getId()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DuckyNotification>> filterNotification(@Param(value = "startTime") long startTime, @Param(value = "endTime") long endTime, @Param(value = "keyword") String keyword){
        return ResponseEntity.ok(fcmService.filterNotification(startTime, endTime, keyword));
    }
}
