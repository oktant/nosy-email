package tech.nosy.nosyemail.nosyemail.config.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class TokenCollection {

  @JsonProperty("access_token")
  String accessToken;

  @JsonProperty("expires_in")
  Integer expiresIn;

  @JsonProperty("refresh_expires_in")
  Integer refreshExpiresIn;

  @JsonProperty("refresh_token")
  String refreshToken;

  @JsonProperty("token_type")
  String tokenType;

  @JsonProperty("not-before-policy")
  Integer notBeforePolicy;

  @JsonProperty("session_state")
  String sessionState;

  @JsonProperty("scope")
  String scope;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Integer getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  public void setRefreshExpiresIn(Integer refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Integer getNotBeforePolicy() {
    return notBeforePolicy;
  }

  public void setNotBeforePolicy(Integer notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
  }

  public String getSessionState() {
    return sessionState;
  }

  public void setSessionState(String sessionState) {
    this.sessionState = sessionState;
  }
}
