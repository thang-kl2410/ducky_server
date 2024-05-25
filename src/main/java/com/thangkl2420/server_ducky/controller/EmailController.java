//package com.thangkl2420.server_ducky.controller;
//
//import com.thangkl2420.server_ducky.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/email")
//@RequiredArgsConstructor
//public class EmailController {
//    private final EmailService emailService;
//
//    @GetMapping("/send-email")
//    public String sendEmail(@RequestParam String to) {
//        emailService.sendSimpleMessage(to, "Subject", "Email content");
//        return "Email sent!";
//    }
//}
