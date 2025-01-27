package com.clody.infra.external.feign.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInfoResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount,
        String id
) {

    public record KakaoAccount(
            Profile profile,
            String email,
            String name
    ) {

        public record Profile(
                String nickname
        ) {
        }
    }
}
