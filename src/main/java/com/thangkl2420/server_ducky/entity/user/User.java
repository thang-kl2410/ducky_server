package com.thangkl2420.server_ducky.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thangkl2420.server_ducky.entity.auth.Role;
import com.thangkl2420.server_ducky.entity.auth.Token;
import com.thangkl2420.server_ducky.entity.post.Post;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String firstname;
  private String lastname;
  @Column(unique = true)
  private String email;
  private String password;
  private String birthday;
  private String phoneNumber;
  private double longitude;
  private double latitude;
  private String address;
  private String avatar;
  private String background;
  private String idDevice;
  private String description;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int countFollower;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int countWatching;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int countNotification;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int countWaitingMessage;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int countWaitingNotification;
  @Transient
  public Boolean IsFollow;

  @Enumerated(EnumType.STRING)
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "userState_id")
  public UserState userState;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "userAction_id")
  public UserAction userAction;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnoreProperties("user")
  private List<SpecializationUser> specializationUsers;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Post> posts;

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
