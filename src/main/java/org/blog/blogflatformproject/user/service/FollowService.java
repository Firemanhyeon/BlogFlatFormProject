package org.blog.blogflatformproject.user.service;

import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.User;

public interface FollowService {
    Follow getFollow(Long followingId , Long followedId);
    void getUnfollow(Long followingId, Long followedId);
    int getFollowerCnt(Long userId);
    int getFollowingCnt(Long userId);
    boolean chkFollow(User followedUser , User followingUser);
}
