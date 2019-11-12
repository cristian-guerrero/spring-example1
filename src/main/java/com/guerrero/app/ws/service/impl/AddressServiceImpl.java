package com.guerrero.app.ws.service.impl;

import com.guerrero.app.ws.entity.AddressEntity;
import com.guerrero.app.ws.entity.UserEntity;
import com.guerrero.app.ws.io.repositories.AddressRepository;
import com.guerrero.app.ws.io.repositories.UserRepository;
import com.guerrero.app.ws.service.AddressService;
import com.guerrero.app.ws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AddressRepository addressRepository;

  // se debe agregar esta anotacion cuando se esta utilizando
  // lazy mode en un entity que tiene relaciones
  @Transactional
  @Override
  public List<AddressDto> getAddress(String id) {

    List<AddressDto> returnValue = new ArrayList<>();

    UserEntity userEntity = userRepository.findUserByUserId(id);


    List<AddressEntity> addressEntities = addressRepository.findByUserDetails(userEntity);

    ModelMapper modelMapper = new ModelMapper();


    if (addressEntities != null) {

      Type listType = new TypeToken<List<AddressDto>>() {}.getType();

      returnValue = modelMapper.map(addressEntities, listType);

    }


    return returnValue;
  }

  @Transactional
  @Override
  public AddressDto getAddressById(String id) {

    AddressEntity addressEntity = addressRepository.findByAddressId(id);

    ModelMapper modelMapper = new ModelMapper();

    AddressDto addressDto = modelMapper.map(addressEntity, AddressDto.class);
    return addressDto;
  }
}
