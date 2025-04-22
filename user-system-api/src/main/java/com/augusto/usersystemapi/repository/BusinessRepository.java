package com.augusto.usersystemapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.usersystemapi.model.Business;

public interface BusinessRepository extends JpaRepository<Business,Long> {
    List<Business> findAllByOwnerId(Long id);
    Optional<Business> findByCnpjAndOwnerId(String cnpj, Long ownerId);
}
