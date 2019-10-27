package com.guerrero.app.ws.security;

import com.guerrero.app.ws.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  // se realiza el enlace desde la interfaz que extendio de UserDetailsService
  private final UserService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        // configurar swagger
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
        .permitAll()
        .anyRequest().authenticated()
        .and().addFilter(getAuthenticationFilter())
        .addFilter(new AuthorizationFilter(authenticationManager()))
    .sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }


  public Authenticationfilter getAuthenticationFilter() throws Exception {

    final Authenticationfilter filter = new Authenticationfilter(authenticationManager());
    filter.setFilterProcessesUrl("/users/login");
    return filter;
  }


}
