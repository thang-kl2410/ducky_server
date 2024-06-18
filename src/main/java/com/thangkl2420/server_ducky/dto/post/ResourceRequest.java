package com.thangkl2420.server_ducky.dto.post;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ResourceRequest {
    private Integer id;
    private String author;
    private String isbn;
}
