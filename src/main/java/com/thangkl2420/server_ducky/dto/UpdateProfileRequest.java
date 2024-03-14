package com.thangkl2420.server_ducky.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
