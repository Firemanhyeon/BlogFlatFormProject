package org.blog.blogflatformproject.user.service;

import lombok.RequiredArgsConstructor;

import org.blog.blogflatformproject.user.domain.SocialUser;
import org.blog.blogflatformproject.user.repository.SocialUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;

    public SocialUser saveOrUpdateUser(String socialId, String provider , String username, String email, String avatarUrl){
        Optional<SocialUser> existingUser = socialUserRepository.findBySocialIdAndProvider(socialId,provider);
        SocialUser socialUser;
        if(existingUser.isPresent()){
            socialUser = existingUser.get();
            socialUser.setUsername(username);
            socialUser.setEmail(email);
            socialUser.setAvatarUrl(avatarUrl);
        }else{
            socialUser = new SocialUser();
            socialUser.setSocialId(socialId);
            socialUser.setProvider(provider);
            socialUser.setUsername(username);
            socialUser.setEmail(email);
            socialUser.setAvatarUrl(avatarUrl);
        }
        return socialUserRepository.save(socialUser);
    }
}
