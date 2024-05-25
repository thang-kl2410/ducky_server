package com.thangkl2420.server_ducky.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.chat.Message;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_type")
public class MessageType {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String messageType;

    @JsonIgnore
    @OneToMany(mappedBy = "messageType")
    private List<Message> messages;
}
