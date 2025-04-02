package com.augusto.order_gen_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.order_gen_auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrCpfOrUserNameAndDeletedFalse(String email, String cpf, String userName);
}
