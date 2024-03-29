package com.guerrero.app.ws.service.impl;

import com.guerrero.app.ws.entity.UserEntity;
import com.guerrero.app.ws.exceptions.UserServiceException;
import com.guerrero.app.ws.io.repositories.UserRepository;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.Utils;
import com.guerrero.app.ws.shared.dto.AddressDto;
import com.guerrero.app.ws.shared.dto.UserDto;
import com.guerrero.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
   *
   * @param s
   * @return
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(s);

    if (userEntity == null) {
      throw new UsernameNotFoundException(s);
    }
    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto user) {


    setAddressesProperties(user);
    ModelMapper modelMapper = new ModelMapper();


    UserEntity userEntity = modelMapper.map(user, UserEntity.class);


    userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    String uuid = utils.randomUUID();
    // String publicUserId = utils.generateUserId(30);
    // userEntity.setUserId(publicUserId);
    userEntity.setUserId(uuid);
    //

    UserEntity storeUserDetails = userRepository.save(userEntity);
    UserDto returnValue = modelMapper.map(storeUserDetails, UserDto.class);

    // BeanUtils.copyProperties(storeUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public UserDto getUser(String email) {

    UserEntity userEntity = userRepository.findByEmail(email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    UserDto returnValue = new UserDto();

    BeanUtils.copyProperties(userEntity, returnValue);
    return returnValue;
  }

  @Transactional
  @Override
  public UserDto getUserByUserId(String id) {

    UserEntity userEntity = userRepository.findUserByUserId(id);

    if (userEntity == null) {
      throw new UsernameNotFoundException(id);
    }

    // todo corregir el error que no carga las direcciones para el usuario dado

    ModelMapper modelMapper = new ModelMapper();
    UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

    return returnValue;

  }

  @Override
  public UserDto updateUser(String id, UserDto user) {

    UserEntity userEntity = userRepository.findUserByUserId(id);

    if (userEntity == null) {
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }


    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());


    UserEntity storeUserDetails = userRepository.save(userEntity);
    UserDto returnValue = new UserDto();

    BeanUtils.copyProperties(storeUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public void deleteUser(String userId) {

    UserEntity userEntity = userRepository.findUserByUserId(userId);
    if (userEntity == null) {
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }

    userRepository.delete(userEntity);

  }

  /**
   * metodo para retornar paginado
   *
   * @param page
   * @param limit
   * @return
   */
  @Override
  public List<UserDto> getUsers(int page, int limit) {
    List<UserDto> returnValue = new ArrayList<>();

    Pageable pageable = PageRequest.of(page, limit);

    Page<UserEntity> usersPage = userRepository.findAll(pageable);

    List<UserEntity> users = usersPage.getContent();

    for (UserEntity userDto1 : users) {
      UserDto userDto = new UserDto();
      BeanUtils.copyProperties(userDto1, userDto);
      returnValue.add(userDto);
    }
    return returnValue;
  }


  private void  setAddressesProperties(UserDto userDto) {
    if (userDto.getAddresses() != null) {

      for (AddressDto a : userDto.getAddresses()) {
        a.setAddressId(utils.randomUUID());
        a.setUserDetails(userDto);
      }
    }
  }
}
