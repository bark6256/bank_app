package com.tencoding.bank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
// JSON 형식에 스네이크 케이스인 key를 자바 객체 카멜 노테이션으로 할당
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthToken {
	// @JsonProperty("access_token") // 하나씩 지정하는 방법
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private String scope;
	private Integer expiresIn;
	private Integer refreshTokenExpiresIn;
}
