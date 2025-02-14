package com.augusto.productsystemapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.productsystemapi.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
  
}
