package com.guerrero.app.ws.io.repositories;
import com.guerrero.app.ws.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends PagingAndSortingRepository<UserEntity, Long> {


  // UserEntity findUserByEmail(String email) ;

  UserEntity findByEmail(String email);

  UserEntity findUserByUserId(String id );




}
