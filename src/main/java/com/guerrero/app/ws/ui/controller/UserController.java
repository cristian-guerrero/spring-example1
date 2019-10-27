package com.guerrero.app.ws.ui.controller;

import com.guerrero.app.ws.exceptions.UserServiceException;
import com.guerrero.app.ws.service.UserService;
import com.guerrero.app.ws.shared.dto.UserDto;
import com.guerrero.app.ws.ui.model.request.UserDetailRequestModel;
import com.guerrero.app.ws.ui.model.response.ErrorMessages;
import com.guerrero.app.ws.ui.model.response.UserRest;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.PageAttributes;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {
  final private static Logger logger = Logger.getLogger(UserController.class);

  @Autowired
  UserService userService;

  @GetMapping(path = "/{id}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public UserRest getUser(@PathVariable String id) {

    UserRest returnValue = new UserRest();

    UserDto userDto = userService.getUserByUserId(id);

    BeanUtils.copyProperties(userDto, returnValue);
    return returnValue;
  }

  @PostMapping(
      consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public UserRest createUser(@RequestBody UserDetailRequestModel userDetails) throws Exception {

    if(userDetails.getFirstName() == null ) throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

    UserRest returnValue = new UserRest();
    UserDto userDto = new UserDto();
    BeanUtils.copyProperties(userDetails, userDto);

    logger.info(userDetails.getFirstName());
    UserDto createdUser = userService.createUser(userDto);

    BeanUtils.copyProperties(createdUser, returnValue);

    return returnValue;
  }

  @PutMapping
  public String updateUser() {
    return "update user was called";
  }

  @DeleteMapping
  public String deleteUser() {
    return "delete user was called";
  }
}
