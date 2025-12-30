package com.nyangtodac.auth.infrastructure.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoUserInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    static class KakaoAccount {

        @JsonProperty("profile")
        private Profile profile;

        @JsonProperty("email")
        private String email;

    }

    @Getter
    static class Profile {
        @JsonProperty("nickname")
        private String nickname;
    }
}
