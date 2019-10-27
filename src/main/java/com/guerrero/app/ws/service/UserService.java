package com.guerrero.app.ws.service;

import com.guerrero.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

// se extiende para aplicar spring security
public interface UserService   extends UserDetailsService {

  UserDto createUser(UserDto user);
  UserDto getUser(String email);

  UserDto getUserByUserId(String id);

  UserDto updateUser(String id ,UserDto user);

  void deleteUser(String userId);


  List<UserDto> getUsers(int page, int limit);
}
