package org.blog.blogflatformproject.user.dto;

import lombok.Data;
import org.blog.blogflatformproject.user.domain.Follow;

import java.util.Set;

@Data
public class FollowDto {

    private Set<Follow> followingId;
    private Set<Follow> followedId;
}
