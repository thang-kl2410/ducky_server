package com.thangkl2420.server_ducky.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilterRequest {
    @Builder.Default
    private String keyword = "";
    private long startTime;
    private long endTime;
    @Builder.Default
    private Integer pageIndex = 0;
    @Builder.Default
    private Integer pageSize = 10;
}
