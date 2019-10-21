package com.guerrero.app.ws.service.impl;

import com.guerrero.app.ws.entity.UserEntity;
import com.guerrero.app.ws.repository.UserRepository;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.Utils;
import com.guerrero.app.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Override
  public UserDto createUser(UserDto user) {

    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);

    //
    userEntity.setEncryptedPassword("cambiar despues");

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
}
