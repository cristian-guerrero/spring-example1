package com.guerrero.app.ws.security;

import com.guerrero.app.ws.SpringApplicationContext;

public class SecurityConstants {

  public static final long EXPIRATION_TIME = 864000000; // diez dias
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING ="Authorization";
  public static final String SIGN_UP_URL = "/users";


  public static String getTokenSecret() {

    AppProperties appProperties  = (AppProperties) SpringApplicationContext.getBean("appProperties");

    return appProperties.getTokenSecret();
  }
}
