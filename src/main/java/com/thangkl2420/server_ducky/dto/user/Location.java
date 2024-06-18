package com.thangkl2420.server_ducky.dto.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Location {
    private double longitude;
    private double latitude;
}
