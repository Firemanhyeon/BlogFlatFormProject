package org.blog.blogflatformproject.user.service;

import org.blog.blogflatformproject.user.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService  {

    RefreshToken addRefreshToken(RefreshToken refreshToken);
    void deleteRefreshToken(Long userId);
    Optional<RefreshToken> findRefreshToken(String refreshToken);
}
