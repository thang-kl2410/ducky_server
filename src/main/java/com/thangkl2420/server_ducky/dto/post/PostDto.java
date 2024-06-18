package com.thangkl2420.server_ducky.dto.post;

import com.thangkl2420.server_ducky.entity.post.Post;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostDto {
    private Post post;
    private Boolean haveLike;
}
