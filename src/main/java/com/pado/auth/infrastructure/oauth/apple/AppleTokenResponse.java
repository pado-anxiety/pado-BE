package com.pado.auth.infrastructure.oauth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AppleTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("id_token")
	private String idToken;
}