package com.augusto.productsystemapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.productsystemapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByCode(String code);
}
