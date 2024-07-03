package com.donkeys_today.server.application.user.sterategy;

import com.donkeys_today.server.domain.user.Platform;
import com.donkeys_today.server.domain.user.User;
import com.donkeys_today.server.domain.user.UserRepository;
import com.donkeys_today.server.support.dto.type.ErrorType;
import com.donkeys_today.server.support.exception.BusinessException;
import com.donkeys_today.server.support.feign.dto.response.KakaoTokenResponse;
import com.donkeys_today.server.support.feign.dto.response.KakaoUserInfoResponse;
import com.donkeys_today.server.support.feign.kakao.KakaoAuthClient;
import com.donkeys_today.server.support.feign.kakao.KakaoUserInfoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthStrategy implements UserAuthSterategy {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoUserInfoClient kakaoUserInfoClient;
    private final UserRepository userRepository;

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${kakao.redirect-uri}")
    private String redirect_uri;

    @Override
    public User signUp(String authToken) {
        
        KakaoTokenResponse token = getKakaoToken(authToken);
        String accessTokenWithPrefix = KakaoTokenResponse.getTokenWithPrefix(token.access_token());
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(accessTokenWithPrefix);
        validateDuplicateUser(userInfo);
        return User.builder()
                .platformID(userInfo.id())
                .nickName(userInfo.kakaoAccount().name())
                .email(userInfo.kakaoAccount().email())
                .build();
    }

    @Override
    public User signIn(String authToken) {
        return null;
    }

    private KakaoTokenResponse getKakaoToken(String authToken) {

        return kakaoAuthClient.getOauth2AccessToken(
                "authorization_code",
                kakaoClientId,
                kakaoClientSecret,
                redirect_uri,
                authToken
        );
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {

        return kakaoUserInfoClient.getUserInformation(accessToken);
    }

    private void validateDuplicateUser(KakaoUserInfoResponse userInfo) {
        if (userRepository.existsByPlatformAndPlatformID(Platform.KAKAO, userInfo.id())) {
            throw new BusinessException(ErrorType.DUPLICATED_USER_ERROR);
        }
    }

    public String getRedirectUri() {
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=code",
                "https://kauth.kakao.com/oauth/authorize", kakaoClientId, redirect_uri);
    }
}