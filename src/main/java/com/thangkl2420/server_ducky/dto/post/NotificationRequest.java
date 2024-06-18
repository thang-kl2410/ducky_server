package com.thangkl2420.server_ducky.dto.post;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationRequest {
    private String token;
    private String body;
    private String title;
}
