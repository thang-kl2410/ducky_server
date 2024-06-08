package com.thangkl2420.server_ducky.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class DuckyNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private String resource;
    private String type;
    private String idContent;
    private long timestamp;
    private boolean haveIssued;

}
