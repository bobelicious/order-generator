package com.augusto.usersystemapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.usersystemapi.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
  
}
