package com.guerrero.app.ws.ui.controller;

import com.guerrero.app.ws.exceptions.UserServiceException;
import com.guerrero.app.ws.service.AddressService;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.dto.AddressDto;
import com.guerrero.app.ws.shared.dto.UserDto;
import com.guerrero.app.ws.ui.model.request.UserDetailRequestModel;
import com.guerrero.app.ws.ui.model.response.AddressRest;
import com.guerrero.app.ws.ui.model.response.ErrorMessages;
import com.guerrero.app.ws.ui.model.response.OperationStatusModel;
import com.guerrero.app.ws.ui.model.response.RequestOperationName;
import com.guerrero.app.ws.ui.model.response.RequestOperationStatus;
import com.guerrero.app.ws.ui.model.response.UserRest;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "users",
    consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // http://localhost:8080/users
public class UserController {
  final private static Logger logger = Logger.getLogger(UserController.class);

  @Autowired
  UserService userService;

  @Autowired
  AddressService addressService;


  @GetMapping(path = "/{id}")
  public UserRest getUser(@PathVariable String id) {

    ModelMapper modelMapper = new ModelMapper();

    UserDto userDto = userService.getUserByUserId(id);

    UserRest returnValue = modelMapper.map(userDto, UserRest.class);
    // BeanUtils.copyProperties(userDto, returnValue);
    return returnValue;
  }

  @PostMapping
  public UserRest createUser(@RequestBody UserDetailRequestModel userDetails) throws Exception {

    if (userDetails.getFirstName() == null)
      throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

    ModelMapper modelMapper = new ModelMapper();
    UserDto userDto = modelMapper.map(userDetails, UserDto.class);

    UserDto createdUser = userService.createUser(userDto);

    UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

    logger.info(returnValue.getFirstName());


    return returnValue;
  }

  @PutMapping(path = "/{id}")
  public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailRequestModel userDetails) {


    UserRest returnValue = new UserRest();
    UserDto userDto = new UserDto();
    BeanUtils.copyProperties(userDetails, userDto);

    // logger.info();
    UserDto updatedUser = userService.updateUser(id, userDto);

    BeanUtils.copyProperties(updatedUser, returnValue);

    return returnValue;

  }

  @DeleteMapping(path = "/{id}")
  public OperationStatusModel deleteUser(@PathVariable String id) {


    OperationStatusModel operationStatusModel = new OperationStatusModel();
    operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
    operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

    userService.deleteUser(id);
    return operationStatusModel;
  }


  /**
   * validacion que la pagina no sea menor a cero
   *
   * @param page
   * @param limit
   * @return
   */
  @GetMapping
  public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                 @RequestParam(value = "limit", defaultValue = "10") int limit) {

    List<UserRest> returnValue = new ArrayList<>();

    List<UserDto> users = userService.getUsers(page, limit);

    for (UserDto userDto1 : users) {
      UserRest userRest = new UserRest();
      BeanUtils.copyProperties(userDto1, userRest);
      returnValue.add(userRest);
    }

    return returnValue;
  }

  @GetMapping(path = "/{id}/addresses")
  public List<AddressRest> getUserAddresses(@PathVariable String id) {

    ModelMapper modelMapper = new ModelMapper();
    List<AddressRest> returnValue = new ArrayList<>();

    List<AddressDto> addressDtoList = addressService.getAddress(id);

    if (addressDtoList != null) {

      Type listType = new TypeToken<List<AddressRest>>() {}.getType();

      returnValue = modelMapper.map(addressDtoList, listType);

    }
    return returnValue;

  }

  @GetMapping(path = "/{id}/addresses/{addressId}")
  public AddressRest getUserAddresd(@PathVariable String addressId) {

    ModelMapper modelMapper = new ModelMapper();
    AddressDto addressDto = addressService.getAddressById(addressId);


    AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);


    return returnValue;

  }

}
