package com.augusto.productsystemapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.productsystemapi.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
  Optional<Store> findByCnpj(String cnpj);
}
