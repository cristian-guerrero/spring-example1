package com.guerrero.app.ws.io.repositories;

import com.guerrero.app.ws.entity.AddressEntity;
import com.guerrero.app.ws.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository  extends PagingAndSortingRepository<AddressEntity, Long>  {


  List<AddressEntity> findByUserDetails(UserEntity userId);


  AddressEntity findByAddressId (String id);

}
