package org.blog.blogflatformproject.user.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.FollowRepository;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    //팔로우하기
    @Transactional
    public Follow getFollow(Long followingId , Long followedId){
        User followingUserChk = userRepository.findById(followingId).orElse(null);
        User followedUserChk = userRepository.findById(followedId).orElse(null);

        Follow checkDuplicate = followRepository.findByFollowerAndFollowing(followedUserChk, followingUserChk);
        if(followingId==followedId || checkDuplicate!=null){
            return null;
        }
        Follow follow = new Follow();
        User followingUser = userService.findByUserId(followingId);
        User followedUser = userService.findByUserId(followedId);
        follow.setFollowing(followingUser);
        follow.setFollower(followedUser);
        return followRepository.save(follow);
    }
    //언팔로우
    @Transactional
    public void getUnfollow(Long followingId, Long followedId) {
        User followingUserChk = userRepository.findById(followingId).orElse(null);
        User followedUserChk = userRepository.findById(followedId).orElse(null);
        followRepository.deleteByFollowerAndFollowing(followedUserChk,followingUserChk);
    }

    //팔로워 수 가져오기
    public int getFollowerCnt(Long userId){
        return followRepository.getFollowerCount(userId);
    }
    //팔로잉 수 가져오기
     public int getFollwingCnt(Long userId){
        return followRepository.getFollowingCount(userId);
     }
     //팔로우했는지안했는지 체크
    public boolean chkFollow(User followedUser , User followingUser){
        Follow follow = followRepository.findByFollowerAndFollowing(followedUser,followingUser);
        if(follow!=null){
            return true;
        }else{
            return false;
        }
    }


}
