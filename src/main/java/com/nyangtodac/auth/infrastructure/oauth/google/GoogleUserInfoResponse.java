package com.nyangtodac.auth.infrastructure.oauth.google;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class GoogleUserInfoResponse {

    private String id;

    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;

    private String locale;
}

