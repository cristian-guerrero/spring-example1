package com.guerrero.app.ws.service;

import com.guerrero.app.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {


  List<AddressDto> getAddress(String id );
}
