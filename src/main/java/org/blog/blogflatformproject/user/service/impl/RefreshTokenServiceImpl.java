package org.blog.blogflatformproject.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.RefreshToken;
import org.blog.blogflatformproject.user.repository.RefreshTokenRepository;
import org.blog.blogflatformproject.user.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken addRefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public void deleteRefreshToken(Long userId){
        refreshTokenRepository.deleteByUserId(userId);
    }
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findRefreshToken(String refreshToken){
        return refreshTokenRepository.findByValue(refreshToken);
    }


}
