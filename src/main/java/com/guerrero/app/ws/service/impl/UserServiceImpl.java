package com.guerrero.app.ws.service.impl;

import com.guerrero.app.ws.entity.UserEntity;
import com.guerrero.app.ws.io.repositories.UserRepository;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.Utils;
import com.guerrero.app.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * metodo sobre escrito para poder implementar spring security
   * @param s
   * @return
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(s);

    if(userEntity == null) {
      throw new UsernameNotFoundException(s);
    }
    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto user) {

    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);

    //
    userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    String uuid = UUID.randomUUID().toString();
    // String publicUserId = utils.generateUserId(30);
    // userEntity.setUserId(publicUserId);
    userEntity.setUserId(uuid);
    //

    UserEntity storeUserDetails = userRepository.save(userEntity);
    UserDto returnValue = new UserDto();

    BeanUtils.copyProperties(storeUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public UserDto getUser(String email) {

    UserEntity userEntity = userRepository.findByEmail(email);
    if(userEntity == null) { throw new UsernameNotFoundException(email); }

    UserDto returnValue = new UserDto();

    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }
}
