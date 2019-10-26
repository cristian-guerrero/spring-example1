package com.guerrero.app.ws.security;

import io.jsonwebtoken.Jwts;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

  final private static Logger logger = Logger.getLogger(AuthorizationFilter.class);

  public AuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader(SecurityConstants.HEADER_STRING);

    if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    // super.doFilterInternal(request, response, chain);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

    String token = request.getHeader(SecurityConstants.HEADER_STRING);

    if (token != null) {

      token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
      logger.info("getAuthentication token: " +  token);

      String user = Jwts.parser()
          .setSigningKey(SecurityConstants.getTokenSecret())
          .parseClaimsJws(token)
          .getBody()
          .getSubject();


      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
    }
    return null;
  }

}

