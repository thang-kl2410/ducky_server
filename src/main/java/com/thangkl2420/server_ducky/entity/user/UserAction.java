package com.thangkl2420.server_ducky.entity.user;

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
@Table(name = "user_action")
public class UserAction {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String action;

    @JsonIgnore
    @OneToMany(mappedBy = "userAction")
    private List<User> users;
}

