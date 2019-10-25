package com.guerrero.app.ws.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guerrero.app.ws.SpringApplicationContext;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.dto.UserDto;
import com.guerrero.app.ws.ui.model.request.UserLoginRequestModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Authenticationfilter extends UsernamePasswordAuthenticationFilter {

  final private static Logger logger = Logger.getLogger(Authenticationfilter.class);

  private final AuthenticationManager authenticationManager;

  public Authenticationfilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {
    try {

      UserLoginRequestModel creds = new ObjectMapper()
          .readValue(request.getInputStream(), UserLoginRequestModel.class);
      logger.info("attemptAuthentication for: " +  creds.getEmail());

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              creds.getEmail(),
              creds.getPassword(),
              new ArrayList<>()
          )
      );
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                          FilterChain chain, Authentication auth) throws IOException, ServletException {


    String userName = ((User) auth.getPrincipal()).getUsername();

    logger.info("successfulAuthentication user: " +  userName);

    String token = Jwts.builder()
        .setSubject(userName)
        .setExpiration(new Date((System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME)))
        .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
        .compact();

    // llamamos el bean a travez del contexto con la clase creada (SpringApplicationContext)
    UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
    UserDto userDto = userService.getUser(userName);

    logger.info("successfulAuthentication token : " +  token);
    res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    res.addHeader("UserID", userDto.getUserId());
  }

}
