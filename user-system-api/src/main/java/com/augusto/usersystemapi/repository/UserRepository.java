package com.augusto.usersystemapi.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.usersystemapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserCode(String userCode);

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    List<User> findAllByDeletedFalse();

    Optional<User> findByEmailOrCpfOrUserNameAndDeletedFalse(String email, String cpf, String username);
}
