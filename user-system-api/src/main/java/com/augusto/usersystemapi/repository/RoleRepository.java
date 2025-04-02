package com.augusto.usersystemapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.usersystemapi.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    
}
