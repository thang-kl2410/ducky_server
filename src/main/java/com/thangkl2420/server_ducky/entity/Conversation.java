package com.thangkl2420.server_ducky.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue
    private Integer id;
    private String conversationName;

    @JsonIgnore
    @OneToMany(mappedBy = "conversation")
    private List<Message> messages;
}
