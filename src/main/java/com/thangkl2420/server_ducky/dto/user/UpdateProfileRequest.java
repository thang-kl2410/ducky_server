package com.thangkl2420.server_ducky.dto.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private String birthday;
    private String phoneNumber;
    private String avatar;
    private String background;
    private String description;
    private String address;
}
