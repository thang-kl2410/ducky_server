package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final NotificationService fcmService;
    private final UserRepository userRepository;

    @SneakyThrows
    public void joinVideoCallChanel(Integer idUser, String token, String chanel){
        User user = userRepository.findById(idUser).orElseThrow();
        fcmService.sendFCMNotification(user.getIdDevice(), "Có cuộc gọi đến",chanel);
    }
}
