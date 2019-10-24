package com.guerrero.app.ws.service;

import com.guerrero.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

// se extiende para aplicar spring security
public interface UserService   extends UserDetailsService {

  UserDto createUser(UserDto user);
  UserDto getUser(String email);
}
