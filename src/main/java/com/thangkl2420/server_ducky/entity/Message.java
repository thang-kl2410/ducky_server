package com.thangkl2420.server_ducky.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer receiverId;
    private Integer senderId;
    private String content;
    private double timestamp;
    private double isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "messageType_id")
    private MessageType messageType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
