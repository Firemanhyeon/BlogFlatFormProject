package org.blog.blogflatformproject.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.FollowRepository;
import org.blog.blogflatformproject.user.service.FollowService;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;


    //팔로우하기
    @Transactional
    @Override
    public Follow getFollow(Long followingId , Long followedId){
        User followingUserChk = userService.findByUserId(followingId);
        User followedUserChk =userService.findByUserId(followedId);

        System.out.println("aaa"+followingUserChk.getUserId());
        System.out.println("aaa"+followedUserChk.getUserId());

        Follow checkDuplicate = followRepository.findByFollowerAndFollowing(followedUserChk, followingUserChk);
        if(followingId==followedId || checkDuplicate!=null){
            return null;
        }
        Follow follow = new Follow();
        follow.setFollowing(followingUserChk);
        follow.setFollower(followedUserChk);
        return followRepository.save(follow);
    }
    //언팔로우
    @Transactional
    @Override
    public void getUnfollow(Long followingId, Long followedId) {
        User followingUserChk = userService.findByUserId(followingId);
        User followedUserChk = userService.findByUserId(followedId);
        followRepository.deleteByFollowerAndFollowing(followedUserChk,followingUserChk);
    }

    //팔로워 수 가져오기
    @Override
    public int getFollowerCnt(Long userId){
        return followRepository.getFollowerCount(userId);
    }
    //팔로잉 수 가져오기
    @Override
     public int getFollowingCnt(Long userId){
        return followRepository.getFollowingCount(userId);
     }
     //팔로우했는지안했는지 체크
     @Override
    public boolean chkFollow(User followedUser , User followingUser){
        Follow follow = followRepository.findByFollowerAndFollowing(followedUser,followingUser);
         System.out.println(followedUser.getUserId());
         System.out.println(followingUser.getUserId());
        if(follow!=null){
            return true;
        }else{
            return false;
        }
    }


}
