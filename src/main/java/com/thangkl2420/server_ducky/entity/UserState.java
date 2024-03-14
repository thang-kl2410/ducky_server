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
@Table(name = "user_state")
public class UserState {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String state;

    @JsonIgnore
    @OneToMany(mappedBy = "userState")
    private List<User> users;
}
